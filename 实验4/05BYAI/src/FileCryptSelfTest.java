import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 实验5 自动自检：对文本文件执行「加密 → 解密」并逐字节验证还原是否完全正确。
 *
 * <p>与 {@link FileCryptTest} 的分工：FileCryptTest 是给人看的交互式演示，
 * 用来截图；本类不需要任何输入，自己造数据、自己断言，用来证明实现正确。</p>
 *
 * <p>产出三个文件（与实验文档要求一致，均在模块根目录 05BYAI 下）：</p>
 * <ul>
 *   <li>test.txt —— 明文</li>
 *   <li>testencrypt.txt —— 密码文件</li>
 *   <li>testdecrypt.txt —— 解密还原出的文件</li>
 * </ul>
 *
 * <p>判定「解密成功」的标准不是肉眼看文字像不像，而是<b>两个文件逐字节完全相等</b>：
 * 文字看起来一样但换行数不同、结尾少了字节，都算失败。</p>
 *
 * @author 徐子超
 */
public class FileCryptSelfTest {

    /** 单字节密钥，90 = 'Z'，与实验文档中的密钥 k 对应。 */
    private static final int KEY = 0x5A;

    /** 多字节循环密钥，用于验证密钥下标跨缓冲区是否连续。 */
    private static final String KEY_STR = "SUEP2026";

    /** 演示程序示例里用的 6 位数字密钥。 */
    private static final String KEY_NUM = "237598";

    /** 与 FileCrypt.BUF_SIZE 保持一致，用于构造跨缓冲区的测试数据。 */
    private static final int BUF_SIZE = 8192;

    private static final String CRLF = "\r\n";

    /** 模块目录名与模块锚点文件，用于把输出文件锁定在 05BYAI 内部。 */
    private static final String MODULE_DIR_NAME = "05BYAI";
    private static final String MODULE_MARKER = "05BYAI.iml";

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws IOException {
        Path dir = moduleRoot();
        Path plain = dir.resolve("test.txt");
        Path enc = dir.resolve("testencrypt.txt");
        Path dec = dir.resolve("testdecrypt.txt");

        banner("实验5  文本文件加密解密自检");
        System.out.println("工作目录：" + Paths.get("").toAbsolutePath());
        System.out.println("输出目录：" + dir + "   ← 三个 txt 都生成在这里");
        System.out.println("单字节密钥 KEY     = " + KEY + "  (0x"
                + Integer.toHexString(KEY).toUpperCase() + " , '" + (char) KEY + "')");
        System.out.println("循环密钥 KEY_STR   = \"" + KEY_STR + "\"");
        System.out.println("数字密钥 KEY_NUM   = \"" + KEY_NUM + "\"");

        // ---------------- 步骤 1：生成明文 ----------------
        section("步骤 1：生成明文文件 test.txt");
        String sourceText = buildSourceText();
        byte[] sourceBytes = sourceText.getBytes(StandardCharsets.UTF_8);
        Files.write(plain, sourceBytes);
        System.out.println("  路径：" + plain);
        System.out.println("  大小：" + sourceBytes.length + " 字节");
        System.out.println("  内容：");
        printBox(sourceText);
        System.out.println("  前 32 字节（十六进制）：");
        System.out.println("    " + hexLine(sourceBytes, 0, 32));

        check("test.txt 已生成", Files.exists(plain), true);
        check("test.txt 非空", sourceBytes.length > 0, true);

        // ---------------- 步骤 2：加密 ----------------
        section("步骤 2：用密钥 " + KEY + " 加密，生成 testencrypt.txt");
        FileCrypt.encrypt(plain, enc, KEY);
        byte[] encBytes = Files.readAllBytes(enc);
        System.out.println("  路径：" + enc);
        System.out.println("  大小：" + encBytes.length + " 字节");
        System.out.println("  前 32 字节（十六进制）：");
        System.out.println("    " + hexLine(encBytes, 0, 32));

        check("密码文件已生成", Files.exists(enc), true);
        check("密码文件与明文大小相同（异或不改变长度）", encBytes.length, sourceBytes.length);
        check("密码文件内容已改变（确实做了异或）", !Arrays.equals(encBytes, sourceBytes), true);
        check("首字节符合 b = a ^ k", encBytes[0] & 0xFF, (sourceBytes[0] & 0xFF) ^ KEY);

        // ---------------- 步骤 3：解密 ----------------
        section("步骤 3：用同一密钥 " + KEY + " 解密，生成 testdecrypt.txt");
        FileCrypt.decrypt(enc, dec, KEY);
        byte[] decBytes = Files.readAllBytes(dec);
        System.out.println("  路径：" + dec);
        System.out.println("  大小：" + decBytes.length + " 字节");
        System.out.println("  前 32 字节（十六进制）：");
        System.out.println("    " + hexLine(decBytes, 0, 32));
        System.out.println("  还原内容：");
        printBox(new String(decBytes, StandardCharsets.UTF_8));

        check("解密文件已生成", Files.exists(dec), true);
        check("解密结果与原文大小相同", decBytes.length, sourceBytes.length);
        check("解密结果与原文逐字节完全一致", Arrays.equals(decBytes, sourceBytes), true);
        check("解密结果转回文本与原文相同",
                new String(decBytes, StandardCharsets.UTF_8).equals(sourceText), true);
        check("首个不一致字节的位置（-1 表示没有）", firstDiff(sourceBytes, decBytes), -1);

        // ---------------- 步骤 4：特殊字符（回车 13 / 换行 10） ----------------
        section("步骤 4：验证回车(13)、换行(10) 被完整保留");
        int srcCR = countByte(sourceBytes, (byte) 0x0D);
        int srcLF = countByte(sourceBytes, (byte) 0x0A);
        int decCR = countByte(decBytes, (byte) 0x0D);
        int decLF = countByte(decBytes, (byte) 0x0A);
        System.out.println("  原文    ：回车 " + srcCR + " 个，换行 " + srcLF + " 个");
        System.out.println("  解密后  ：回车 " + decCR + " 个，换行 " + decLF + " 个");
        System.out.println("  原文行数：" + sourceText.split("\r\n", -1).length
                + "    解密后行数：" + new String(decBytes, StandardCharsets.UTF_8).split("\r\n", -1).length);

        check("原文中确实存在回车符", srcCR > 0, true);
        check("解密后回车数量不变", decCR, srcCR);
        check("解密后换行数量不变", decLF, srcLF);
        check("密码文件里的回车已被改写（证明回车参与了异或）", crlfChanged(sourceBytes, encBytes), true);

        // ---------------- 步骤 5：错误密钥 ----------------
        section("步骤 5：用错误密钥解密，应当无法还原");
        Path wrong = dir.resolve("testdecrypt_wrongkey.txt");
        FileCrypt.decrypt(enc, wrong, KEY + 1);
        byte[] wrongBytes = Files.readAllBytes(wrong);
        System.out.println("  错误密钥 = " + (KEY + 1) + "，解密结果前 24 字节：");
        System.out.println("    " + hexLine(wrongBytes, 0, 24));
        check("错误密钥无法还原原文", !Arrays.equals(wrongBytes, sourceBytes), true);
        check("错误密钥解出的文本也与原文不同",
                !new String(wrongBytes, StandardCharsets.UTF_8).equals(sourceText), true);
        Files.deleteIfExists(wrong);

        // ---------------- 步骤 6：密钥解析规则 ----------------
        section("步骤 6：密钥解析规则（数字拆字节 / 文本取 UTF-8）");
        byte[] k90 = FileCrypt.keyBytes("90");
        byte[] kNum = FileCrypt.keyBytes(KEY_NUM);
        byte[] kAbc = FileCrypt.keyBytes("abc");
        System.out.println("  keyBytes(\"90\")     = " + hexLine(k90, 0, k90.length)
                + "  （长度 " + k90.length + "）");
        System.out.println("  keyBytes(\"237598\") = " + hexLine(kNum, 0, kNum.length)
                + "  （长度 " + kNum.length + "，237598 = 0x03A01E）");
        System.out.println("  keyBytes(\"abc\")    = " + hexLine(kAbc, 0, kAbc.length)
                + "  （长度 " + kAbc.length + "，即 'a''b''c' 的 ASCII）");

        check("keyBytes(\"90\") 长度为 1", k90.length, 1);
        check("keyBytes(\"90\")[0] = 0x5A", k90[0] & 0xFF, 0x5A);
        check("keyBytes(\"237598\") 长度为 3", kNum.length, 3);
        check("keyBytes(\"237598\") 首字节 = 0x03", kNum[0] & 0xFF, 0x03);
        check("keyBytes(\"abc\") 长度为 3", kAbc.length, 3);

        // 用数字密钥走一遍完整往返
        Path numEnc = dir.resolve("tmp_num_enc.txt");
        Path numDec = dir.resolve("tmp_num_dec.txt");
        FileCrypt.encrypt(plain, numEnc, KEY_NUM);
        FileCrypt.decrypt(numEnc, numDec, KEY_NUM);
        check("数字密钥 237598 往返还原一致",
                Arrays.equals(Files.readAllBytes(numDec), sourceBytes), true);
        Files.deleteIfExists(numEnc);
        Files.deleteIfExists(numDec);

        // ---------------- 步骤 7：边界情况 ----------------
        section("步骤 7：边界情况（空文件 / 单字节 / 跨缓冲区）");
        Path tmpIn = dir.resolve("tmp_in.txt");
        Path tmpEnc = dir.resolve("tmp_enc.txt");
        Path tmpDec = dir.resolve("tmp_dec.txt");

        // 7.1 空文件
        Files.write(tmpIn, new byte[0]);
        FileCrypt.encrypt(tmpIn, tmpEnc, KEY);
        FileCrypt.decrypt(tmpEnc, tmpDec, KEY);
        check("空文件加密后仍为 0 字节", Files.size(tmpEnc), 0L);
        check("空文件解密后仍为 0 字节", Files.size(tmpDec), 0L);

        // 7.2 单字节
        Files.write(tmpIn, new byte[]{'A'});
        FileCrypt.encrypt(tmpIn, tmpEnc, KEY);
        FileCrypt.decrypt(tmpEnc, tmpDec, KEY);
        check("单字节加密结果 = 'A' ^ k", Files.readAllBytes(tmpEnc)[0] & 0xFF, ('A' & 0xFF) ^ KEY);
        check("单字节解密还原", Files.readAllBytes(tmpDec)[0] & 0xFF, 'A' & 0xFF);

        // 7.3 跨缓冲区：用循环密钥，检查密钥下标没有在缓冲区边界处被重置
        StringBuilder bigText = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            bigText.append(buildSourceText()).append(CRLF);
        }
        byte[] bigBytes = bigText.toString().getBytes(StandardCharsets.UTF_8);
        Files.write(tmpIn, bigBytes);
        FileCrypt.encrypt(tmpIn, tmpEnc, KEY_STR);
        FileCrypt.decrypt(tmpEnc, tmpDec, KEY_STR);
        System.out.println("  跨缓冲区用例大小：" + bigBytes.length + " 字节（缓冲区 "
                + BUF_SIZE + " 字节，跨 " + ((bigBytes.length + BUF_SIZE - 1) / BUF_SIZE) + " 个缓冲区）");
        check("大文件（循环密钥）解密后逐字节一致",
                Arrays.equals(Files.readAllBytes(tmpDec), bigBytes), true);
        check("大文件首个不一致字节的位置", firstDiff(bigBytes, Files.readAllBytes(tmpDec)), -1);

        // ---------------- 步骤 8：参数校验 ----------------
        section("步骤 8：参数校验");
        check("负整数密钥抛 IllegalArgumentException", throwsIAE(dir, -1), true);
        check("空串密钥抛 IllegalArgumentException", throwsIAEStr(dir, ""), true);
        check("null 密钥抛 IllegalArgumentException", throwsIAEStr(dir, null), true);
        check("密钥 \"0\" 抛 IllegalArgumentException（等于不加密）", throwsIAEStr(dir, "0"), true);

        Files.deleteIfExists(tmpIn);
        Files.deleteIfExists(tmpEnc);
        Files.deleteIfExists(tmpDec);

        // ---------------- 汇总 ----------------
        banner("自检汇总");
        System.out.println("  通过 " + passed + " 项，失败 " + failed + " 项");
        if (failed > 0) {
            System.out.println("  存在失败项，请检查上面标 [失败] 的行");
            System.exit(1);
        }
        System.out.println("  解密验证标准：test.txt 与 testdecrypt.txt 逐字节完全一致");
        System.out.println();
        System.out.println("  可截图的三个文件：");
        System.out.println("    " + plain);
        System.out.println("    " + enc);
        System.out.println("    " + dec);
    }

    // ==================== 被测数据 ====================

    /**
     * 构造测试文本：中英文混排 + 空行 + 显式的 \r\n，逼出所有容易出错的字节。
     * 最后一行故意不带换行，用来检查文件结尾是否被吞掉。
     */
    private static String buildSourceText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Data Structure -- Lab 5: File Encryption").append(CRLF);
        sb.append("20252660 Xu Zichao").append(CRLF);
        sb.append(CRLF);
        sb.append("第一行：异或加密就是把每个字节与密钥做一次 ^ 运算。").append(CRLF);
        sb.append("第二行：因为 a ^ k ^ k = a，所以加密和解密是同一段代码。").append(CRLF);
        sb.append(CRLF);
        sb.append("回车(13)和换行(10)必须原样保留，否则解密后全文会挤成一行。").append(CRLF);
        sb.append("ASCII: The quick brown fox jumps over the lazy dog. 0123456789").append(CRLF);
        sb.append("末行故意不带换行符，用于检查文件结尾是否被吞。");
        return sb.toString();
    }

    // ==================== 断言与输出工具 ====================

    private static void check(String label, Object actual, Object expected) {
        boolean ok = (actual == null) ? expected == null : actual.equals(expected);
        if (ok) {
            passed++;
            System.out.println("  [通过] " + label + "  →  " + actual);
        } else {
            failed++;
            System.out.println("  [失败] " + label + "  →  实际 " + actual + "，期望 " + expected);
        }
    }

    private static void banner(String title) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("  " + title);
        System.out.println("============================================================");
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("  " + title);
        System.out.println("------------------------------------------------------------");
    }

    /** 把文本按行加框打印，方便看清换行结构。 */
    private static void printBox(String text) {
        String[] lines = text.split("\r\n", -1);
        for (String line : lines) {
            System.out.println("    | " + line);
        }
    }

    /** 打印从 from 开始的 n 个字节的十六进制形式。 */
    private static String hexLine(byte[] data, int from, int n) {
        StringBuilder sb = new StringBuilder();
        int end = Math.min(data.length, from + n);
        for (int i = from; i < end; i++) {
            sb.append(String.format("%02X ", data[i]));
        }
        if (data.length == 0) {
            sb.append("(空文件)");
        }
        return sb.toString().trim();
    }

    /** 返回第一个不一致字节的下标，完全一致返回 -1。 */
    private static int firstDiff(byte[] a, byte[] b) {
        int n = Math.min(a.length, b.length);
        for (int i = 0; i < n; i++) {
            if (a[i] != b[i]) {
                return i;
            }
        }
        return a.length == b.length ? -1 : n;
    }

    /** 统计某个字节值在数组中出现的次数。 */
    private static int countByte(byte[] data, byte target) {
        int c = 0;
        for (byte b : data) {
            if (b == target) {
                c++;
            }
        }
        return c;
    }

    /** 检查原文中所有回车、换行位置在密码文件里是否都被改写。 */
    private static boolean crlfChanged(byte[] src, byte[] enc) {
        for (int i = 0; i < src.length; i++) {
            if ((src[i] == 0x0D || src[i] == 0x0A) && src[i] == enc[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean throwsIAE(Path dir, int key) {
        Path tmp = dir.resolve("tmp_x.txt");
        try {
            FileCrypt.encrypt(dir.resolve("test.txt"), tmp, key);
            Files.deleteIfExists(tmp);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean throwsIAEStr(Path dir, String key) {
        Path tmp = dir.resolve("tmp_x2.txt");
        try {
            FileCrypt.xorFile(dir.resolve("test.txt"), tmp, key);
            Files.deleteIfExists(tmp);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 确定模块根目录，保证三个 txt 一定生成在 05BYAI 里，不受"工作目录"影响。
     *
     * <p>为什么不能直接用 Paths.get("")：相对路径是相对<b>工作目录</b>解析的，
     * 而工作目录由运行配置决定——IDEA 默认是项目根目录（实验4），
     * 命令行从 src 下运行则是 src，两种情况都会把文件生成到模块外面去。</p>
     *
     * <p>这里用模块的 .iml 文件当"锚点"逐级查找，三种常见启动位置都能命中：</p>
     * <ol>
     *   <li>工作目录就是 05BYAI  → 直接命中</li>
     *   <li>工作目录是项目根 实验4 → 向下找到 05BYAI</li>
     *   <li>工作目录是 05BYAI\src → 向上找到 05BYAI</li>
     * </ol>
     */
    private static Path moduleRoot() {
        Path cwd = Paths.get("").toAbsolutePath();

        // 1. 自己就是模块根
        if (Files.isRegularFile(cwd.resolve(MODULE_MARKER))) {
            return cwd;
        }
        // 2. 向下找一层（工作目录是项目根）
        Path child = cwd.resolve(MODULE_DIR_NAME);
        if (Files.isRegularFile(child.resolve(MODULE_MARKER))) {
            return child;
        }
        // 3. 向上逐级找（工作目录是 src 或更深的目录）
        for (Path p = cwd.getParent(); p != null; p = p.getParent()) {
            if (Files.isRegularFile(p.resolve(MODULE_MARKER))) {
                return p;
            }
            Path sub = p.resolve(MODULE_DIR_NAME);
            if (Files.isRegularFile(sub.resolve(MODULE_MARKER))) {
                return sub;
            }
        }
        // 4. 兜底：仍然用工作目录，但会给出警告
        System.out.println("  [警告] 未找到 " + MODULE_MARKER + "，文件将生成在工作目录：" + cwd);
        return cwd;
    }
}
