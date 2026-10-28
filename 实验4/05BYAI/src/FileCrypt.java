import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 实验5 设计性实验：文本文件的异或加密 / 解密。
 *
 * <p>加密原理：设明文字符编码为 a，密钥为 k，则密文 b = a ^ k；
 * 解密时再异或一次即 b ^ k = a ^ k ^ k = a。异或的逆运算是它自己，
 * 所以本类里 encrypt 和 decrypt 其实是同一段代码，只是语义不同。</p>
 *
 * <p>三个必须守住的实现约定：</p>
 * <ol>
 *   <li><b>全程使用字节流（InputStream / OutputStream），不用字符流。</b>
 *       异或之后的字节序列一般不再是合法的 UTF-8 序列，若用 FileReader 读取，
 *       解码器会把非法字节替换成 U+FFFD（一个字节变三个字节），数据当场不可逆。</li>
 *   <li><b>逐字节处理，不按行读取。</b> 回车（ASCII 13）和换行（ASCII 10）
 *       也是普通字节，必须一起参与异或。用 readLine() 会剥掉 \r\n，
 *       加密文件再解密出来虽然字都在，但整个文件会被挤成一行。</li>
 *   <li><b>密钥下标必须跨缓冲区连续。</b> 读到 8192 字节的边界处不能把
 *       密钥指针重置为 0，否则多字节密钥在边界处会错位，解密无法还原。</li>
 * </ol>
 *
 * <p>关于密钥长度：实验文档中的单字节密钥 k 是本实现的特例。
 * 实际使用时密钥可以是一个较大的正整数（如 237598），程序把它按字节拆开循环使用，
 * 称为循环密钥。单字节密钥只有 255 种可能，用词频统计很容易破解，循环密钥强度更高。</p>
 *
 * @author 徐子超
 */
public final class FileCrypt {

    /** 缓冲区大小：8KB 是磁盘 IO 与内存占用的常见折中，与文件大小无关。 */
    private static final int BUF_SIZE = 8192;

    /** 工具类不允许实例化。 */
    private FileCrypt() {
    }

    // ==================== 对外接口 ====================

    /** 加密：把 src 的每个字节与密钥异或后写入 dst。 */
    public static void encrypt(Path src, Path dst, String key) throws IOException {
        xorFile(src, dst, key);
    }

    /** 解密：对密码文件执行同样的异或运算，还原成明文。 */
    public static void decrypt(Path src, Path dst, String key) throws IOException {
        xorFile(src, dst, key);
    }

    /** 加密（整数密钥版本）。 */
    public static void encrypt(Path src, Path dst, int key) throws IOException {
        xorFile(src, dst, key);
    }

    /** 解密（整数密钥版本）。 */
    public static void decrypt(Path src, Path dst, int key) throws IOException {
        xorFile(src, dst, key);
    }

    /**
     * 整数密钥版本。任意非负整数都可以，程序按字节拆开循环使用。
     * 例如 90（=0x5A）拆出 1 个字节 "5A"，237598（=0x03A01E）拆出 3 个字节 "03 A0 1E"。
     */
    public static void xorFile(Path src, Path dst, int key) throws IOException {
        if (key < 0) {
            throw new IllegalArgumentException("密钥不能为负数：" + key);
        }
        xorFile(src, dst, keyBytes(String.valueOf(key)));
    }

    /** 字符串密钥版本：密钥按 UTF-8 编码成字节后循环使用。 */
    public static void xorFile(Path src, Path dst, String key) throws IOException {
        xorFile(src, dst, keyBytes(key));
    }

    /**
     * 把用户输入的密钥文本转换成真正参与运算的密钥字节数组。
     *
     * <p>两种解析规则：</p>
     * <ul>
     *   <li>纯数字：按整数拆分字节。如 "237598" → {0x03, 0xA0, 0x1E}，
     *       这样输入一个 6 位数字也能得到 3 字节的密钥，不会像单字节密钥那样只有 255 种可能</li>
     *   <li>其它文本：按 UTF-8 编码取字节。如 "abc" → {0x61, 0x62, 0x63}</li>
     * </ul>
     *
     * @param keyText 用户输入的密钥文本
     * @return 密钥字节数组，长度至少为 1，且不全为 0
     */
    public static byte[] keyBytes(String keyText) {
        if (keyText == null || keyText.trim().isEmpty()) {
            throw new IllegalArgumentException("密钥不能为空");
        }
        String s = keyText.trim();

        byte[] bytes;
        if (s.matches("\\d+")) {
            try {
                bytes = bytesOfLong(Long.parseLong(s));
            } catch (NumberFormatException e) {
                // 位数太多超出 long 范围，退回按字符串处理
                bytes = s.getBytes(StandardCharsets.UTF_8);
            }
        } else {
            bytes = s.getBytes(StandardCharsets.UTF_8);
        }

        if (allZero(bytes)) {
            throw new IllegalArgumentException("密钥不能全为 0（那等于没有加密）");
        }
        return bytes;
    }

    /**
     * 真正的异或变换。加密和解密共用，因为异或两次就还原了。
     */
    public static void xorFile(Path src, Path dst, byte[] keyBytes) throws IOException {
        if (src == null || dst == null) {
            throw new IllegalArgumentException("源文件路径和目标文件路径不能为 null");
        }
        if (keyBytes == null || keyBytes.length == 0) {
            throw new IllegalArgumentException("密钥不能为空");
        }
        if (!Files.exists(src)) {
            throw new IOException("源文件不存在：" + src.toAbsolutePath());
        }
        // 同路径会先把文件截断成 0 字节再读，原始数据直接清空，必须挡住
        if (Files.exists(dst) && Files.isSameFile(src, dst)) {
            throw new IllegalArgumentException("源文件和目标文件不能是同一个文件：" + src.toAbsolutePath());
        }

        Path parent = dst.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (InputStream in = Files.newInputStream(src);
             OutputStream out = Files.newOutputStream(dst)) {

            byte[] buf = new byte[BUF_SIZE];
            int keyIndex = 0;          // 必须声明在循环外，跨缓冲区保持连续
            int n;
            while ((n = in.read(buf)) != -1) {
                for (int i = 0; i < n; i++) {
                    buf[i] ^= keyBytes[keyIndex];
                    keyIndex++;
                    if (keyIndex == keyBytes.length) {
                        keyIndex = 0;  // 密钥用完了，从头再来
                    }
                }
                out.write(buf, 0, n);  // 只写本次实际读到的 n 个字节
            }
        }
    }

    // ==================== 内部工具 ====================

    /** 把非负 long 转成最短的大端字节序列，去掉前导的 0 字节。 */
    private static byte[] bytesOfLong(long v) {
        byte[] all = new byte[8];
        for (int i = 0; i < 8; i++) {
            all[i] = (byte) (v >>> (8 * (7 - i)));
        }
        int start = 0;
        while (start < 7 && all[start] == 0) {
            start++;
        }
        byte[] out = new byte[8 - start];
        System.arraycopy(all, start, out, 0, out.length);
        return out;
    }

    /** 判断字节数组是否全为 0。 */
    private static boolean allZero(byte[] data) {
        for (byte b : data) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }
}
