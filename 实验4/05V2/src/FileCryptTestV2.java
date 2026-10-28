import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileCryptTestV2 {
    private static final Scanner IN = new Scanner(System.in);

    private static final String MODULE_MARKER = "05V2.iml";
    private static final String MODULE_DIR_NAME = "05V2";

    public static void main(String[] args) {
        System.out.println("文本文件加密解密演示程序");

        int choice = askChoice();
        boolean encrypting = (choice == 1);

        String srcRaw = ask(encrypting ? "请输入需要被加密的文本文件名："
                                        :"请输入需要被解密的文本文件名：");
        Path src = toPath(srcRaw,true);

        String dstRaw = ask(encrypting ? "请输入加密后的文本文件名："
                                        :"请输入解密后的文本文件名：");
        Path dst = toPath(dstRaw,false);

        String keyText = ask(encrypting ? "请输入加密密钥："
                                        : "请输入解密密钥：");

        if (!Files.isRegularFile(src)) {
            reportMissing(src);
            return;
        }

        try {
            byte[] keyBytes = FileCryptV2.keyBytes(keyText);
            FileCryptV2.xorFile(src, dst, keyBytes);
        } catch (IllegalArgumentException e) {
            System.out.println("错误：" + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("错误：读写文件失败 —— " + e.getMessage());
            return;
        }

        System.out.println((encrypting ? "加密成功！文件：" : "解密成功！文件：")
                + display(srcRaw, src) + (encrypting ? " 已被加密为：" : " 已被解密为：") + display(dstRaw, dst));
    }

    private static String display(String raw, Path p) {
        boolean hasSeparator = raw.indexOf('\\') >= 0 || raw.indexOf('/') >= 0;
        return hasSeparator ? p.toString() : raw;
    }

    private static int askChoice(){
        while (true) {
            String s = ask("请选择？（1-加密 2-解密）：");
            if("1".equals(s) || "2".equals(s)) {
                return Integer.parseInt(s);
            }
            System.out.println("输入有误，请输入 1 或 2");
        }
    }

    private static String ask(String prompt) {
        while (true) {
            System.out.println(prompt);
            if(!IN.hasNextLine()) {
                System.out.println("未读到输入，程序退出");
                System.exit(1);
            }
            String line = IN.nextLine().trim();
            if(!line.isEmpty()){
                return line;
            }
            System.out.println("输入不能为空，请重新输入");
        }
    }

    private static Path toPath(String raw,boolean mustExist){
        String s = raw.trim();
        if(s.startsWith("\\\\")){
            return Paths.get(s);
        }
        s = s.replaceAll("\\\\{2,}", "\\\\").replaceAll("/{2,}","/");

        Path p = Paths.get(s);
        if(p.isAbsolute()){
            return p;
        }

        Path byCwd = p.toAbsolutePath().normalize();
        if(Files.exists(byCwd)){
            return byCwd;
        }

        Path byRoot = moduleRoot().resolve(s).normalize();
        if(Files.exists(byRoot) || !mustExist){
            return byRoot;
        }
        return byCwd;
    }

    private static Path moduleRoot(){
        Path cwd = Paths.get("").toAbsolutePath();

        if (Files.isRegularFile(cwd.resolve(MODULE_MARKER))) {
            return cwd;
        }
        Path child = cwd.resolve(MODULE_DIR_NAME);
        if (Files.isRegularFile(child.resolve(MODULE_MARKER))) {
            return child;
        }
        for (Path up = cwd.getParent(); up != null; up = up.getParent()) {
            if (Files.isRegularFile(up.resolve(MODULE_MARKER))) {
                return up;
            }
            Path sub = up.resolve(MODULE_DIR_NAME);
            if (Files.isRegularFile(sub.resolve(MODULE_MARKER))) {
                return sub;
            }
        }
        return cwd;
    }
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

        }
        if (!any) {
            System.out.println("(无)");
        }
        System.out.println("  提示：请先在模块目录下准备一个 txt 文件，或直接输入完整绝对路径。");
    }
}
