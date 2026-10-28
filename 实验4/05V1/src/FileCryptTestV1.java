import java.io.IOException;
import java.util.Scanner;

public class FileCryptTestV1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("文本文件加密解密演示程序");

        int choice = askChoice(scanner);
        String operation = choice == 1 ? "加密" : "解密";

        System.out.println("请输入需要被" + operation + "的文本文件名：");
        String inputPath = scanner.nextLine().trim();
        System.out.println("请输入" + operation + "后的文本文件名：");
        String outputPath = scanner.nextLine().trim();
        int key = askKey(scanner, operation);

        try {
            FileCryptV1.decode(inputPath, outputPath, key);
        } catch (IllegalArgumentException e) {
            System.out.println("错误：" + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("错误：读写失败 —— " + e.getMessage());
            return;
        }

        System.out.println(operation + "成功！文件：" + inputPath
                + " 已被" + operation + "为：" + outputPath);
    }

    private static int askChoice(Scanner scanner) {
        while (true) {
            System.out.println("请选择？（1-加密 2-解密）：");
            String line = scanner.nextLine().trim();
            if ("1".equals(line) || "2".equals(line)) {
                return Integer.parseInt(line);
            }
            System.out.println("输入有误，请输入 1 或 2");
        }
    }

    private static int askKey(Scanner scanner, String operation) {
        while (true) {
            System.out.println("请输入" + operation + "密钥：");
            String line = scanner.nextLine().trim();
            if (line.matches("\\d{1,9}") && Integer.parseInt(line) > 0) {
                return Integer.parseInt(line);
            }
            System.out.println("密钥必须是正整数，请重新输入");
        }
    }
}
