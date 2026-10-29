import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 实验4-05 设计性实验：文本文件加密解密演示程序（交互式）。
 *
 * <p>运行后按菜单选择加密或解密，依次输入源文件名、目标文件名和密钥，
 * 程序完成变换并输出结果提示。加密与解密共用同一套异或逻辑。</p>
 *
 * <p>输入的文件名可以是绝对路径（如 {@code D:\lab\test.txt}），
 * 也可以是相对于当前工作目录的路径。</p>
 *
 * @author 徐子超
 */
public class FileCryptTest {

    private static final Scanner IN = new Scanner(System.in);

    /** 模块标识文件：用它当作"模块根目录"的锚点，不写死任何绝对路径。 */
    private static final String MODULE_MARKER = "05BYAI.iml";
    private static final String MODULE_DIR_NAME = "05BYAI";

    public static void main(String[] args) {
        System.out.println("文本文件加密解密演示程序");

        int choice = askChoice();
        boolean encrypting = (choice == 1);

        String srcRaw = ask(encrypting ? "请输入需要被加密的文本文件名："
                                       : "请输入需要被解密的文本文件名：");
        Path src = toPath(srcRaw, true);

        String dstRaw = ask(encrypting ? "请输入加密后的文本文件名："
                                       : "请输入解密后的文本文件名：");
        Path dst = toPath(dstRaw, false);

        String keyText = ask(encrypting ? "请输入加密密钥：" : "请输入解密密钥：");

        // 源文件必须存在，否则给出明确提示而不是抛一堆堆栈
        if (!Files.isRegularFile(src)) {
            reportMissing(src);
            return;
        }

        try {
            byte[] keyBytes = FileCrypt.keyBytes(keyText);
            FileCrypt.xorFile(src, dst, keyBytes);
        } catch (IllegalArgumentException e) {
            System.out.println("错误：" + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("错误：读写文件失败 —— " + e.getMessage());
            return;
        }

        // 成功提示里只输文件名时原样显示（与实验文档示例逐字一致）；
        // 输了路径则显示规范化后的路径，避免 f:\\test.txt 的双斜杠碍眼
        System.out.println((encrypting ? "加密成功！文件：" : "解密成功！文件：")
                + display(srcRaw, src) + (encrypting ? " 已被加密为：" : " 已被解密为：") + display(dstRaw, dst));
    }

    /** 只输文件名时原样返回；输入里带了路径分隔符则返回规范化后的完整路径。 */
    private static String display(String raw, Path p) {
        boolean hasSeparator = raw.indexOf('\\') >= 0 || raw.indexOf('/') >= 0;
        return hasSeparator ? p.toString() : raw;
    }

    // ==================== 输入处理 ====================

    /** 读一行非空输入；提示语单独占一行，与示例输出保持一致。 */
    private static String ask(String prompt) {
        while (true) {
            System.out.println(prompt);
            if (!IN.hasNextLine()) {
                System.out.println("未读到输入，程序退出。");
                System.exit(1);
            }
            String line = IN.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("输入不能为空，请重新输入。");
        }
    }

    /** 读取菜单选择，只接受 1 或 2。 */
    private static int askChoice() {
        while (true) {
            String s = ask("请选择？（1-加密 2-解密）：");
            if ("1".equals(s) || "2".equals(s)) {
                return Integer.parseInt(s);
            }
            System.out.println("输入有误，请输入 1 或 2。");
        }
    }

    /**
     * 把输入的文本转成 Path。
     *
     * <p>顺手把连续的分隔符压成一个，这样即使照着文档把路径写成
     * {@code f:\\test.txt}（双反斜杠）也能正常打开。以两个反斜杠开头的
     * UNC 网络路径（{@code \\server\share}）保持原样，不做压缩。</p>
     *
     * <p><b>相对路径的解析规则</b>（这是避免"文件不存在"的关键）：
     * IDEA 运行时的工作目录可能是模块根 {@code 05BYAI}，也可能是项目根 {@code 实验4}，
     * 同一个 {@code test.txt} 在这两种情况下指向不同位置。所以这里按顺序尝试：</p>
     * <ol>
     *   <li>工作目录下的同名文件（真的存在就用它，尊重用户的显式放置）</li>
     *   <li>模块根目录下的同名文件（{@code 05BYAI\test.txt}）</li>
     *   <li>要写出的新文件：直接放到模块根目录，保证产物不散落到模块外</li>
     * </ol>
     *
     * @param raw       用户输入的原始路径
     * @param mustExist 是否是"要读取的已有文件"（true 用于源文件，false 用于目标文件）
     */
    private static Path toPath(String raw, boolean mustExist) {
        String s = raw.trim();
        if (s.startsWith("\\\\")) {                 // UNC 网络路径，原样使用
            return Paths.get(s);
        }
        s = s.replaceAll("\\\\{2,}", "\\\\").replaceAll("/{2,}", "/");

        Path p = Paths.get(s);
        if (p.isAbsolute()) {
            return p;                               // 绝对路径不做任何猜测
        }

        Path byCwd = p.toAbsolutePath().normalize();
        if (Files.exists(byCwd)) {
            return byCwd;
        }

        Path byRoot = moduleRoot().resolve(s).normalize();
        if (Files.exists(byRoot) || !mustExist) {
            return byRoot;
        }
        return byCwd;                               // 两处都没有，返回当前目录下的路径用于报错
    }

    /**
     * 定位模块根目录：以 {@code 05BYAI.iml} 为锚点。
     *
     * <p>IDEA 从代码行号旁启动程序时，工作目录可能是模块根 {@code 05BYAI}、
     * 项目根 {@code 实验4}、更上层的工程根 {@code 实验项目报告}，
     * 也可能是 {@code src}。四种情况都靠这里落到 {@code 05BYAI}：</p>
     * <ol>
     *   <li>向上逐级查找（覆盖模块根、src、实验4 三种）</li>
     *   <li>向下浅层查找（覆盖工作目录是工程根、模块被套在 实验4 下的情况）</li>
     * </ol>
     */
    private static Path moduleRoot() {
        Path cwd = Paths.get("").toAbsolutePath();

        for (Path up = cwd; up != null; up = up.getParent()) {
            if (Files.isRegularFile(up.resolve(MODULE_MARKER))) {
                return up;
            }
            Path sub = up.resolve(MODULE_DIR_NAME);
            if (Files.isRegularFile(sub.resolve(MODULE_MARKER))) {
                return sub;
            }
        }
        // 向下找：工程根下面隔着 实验4 一层，靠向下浅层搜索兜住
        try (java.util.stream.Stream<Path> walk = Files.walk(cwd, 3)) {
            return walk.filter(f -> MODULE_MARKER.equals(String.valueOf(f.getFileName())))
                       .map(Path::getParent)
                       .findFirst()
                       .orElse(cwd);
        } catch (IOException e) {
            return cwd;                             // 兜底
        }
    }

    /**
     * 源文件找不到时打印诊断信息：把两个候选位置都列出来，
     * 并列出模块目录下现有的 txt，省得用户去猜文件该放哪。
     */
    private static void reportMissing(Path src) {
        Path cwd = Paths.get("").toAbsolutePath();
        Path root = moduleRoot();
        String name = src.getFileName() == null ? "" : src.getFileName().toString();

        System.out.println("错误：文件不存在 —— " + src);
        System.out.println("  当前工作目录：" + cwd);
        System.out.println("  模块目录    ：" + root);
        if (!cwd.equals(root)) {
            System.out.println("  已尝试的位置：");
            System.out.println("    " + cwd.resolve(name));
            System.out.println("    " + root.resolve(name));
        }

        System.out.print("  模块目录下现有的 txt 文件：");
        boolean any = false;
        try (java.util.stream.Stream<Path> list = Files.list(root)) {
            StringBuilder sb = new StringBuilder();
            list.filter(f -> f.getFileName().toString().toLowerCase().endsWith(".txt"))
                .forEach(f -> sb.append(f.getFileName()).append("  "));
            if (sb.length() > 0) {
                any = true;
                System.out.println(sb.toString().trim());
            }
        } catch (IOException ignored) {
            // 列目录失败不影响主流程
        }
        if (!any) {
            System.out.println("(无)");
        }
        System.out.println("  提示：可先运行 FileCryptSelfTest 生成 test.txt，或直接输入完整绝对路径。");
    }
}
