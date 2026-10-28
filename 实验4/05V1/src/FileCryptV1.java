import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileCryptV1 {
    public static void decode(String inputPath, String outputPath, int key) throws IOException {
        if (key <= 0) {
            throw new IllegalArgumentException("密钥必须是正整数：" + key);
        }

        Path in  = Paths.get(inputPath).toAbsolutePath().normalize();
        Path out = Paths.get(outputPath).toAbsolutePath().normalize();
        if (in.equals(out)) {
            throw new IllegalArgumentException("源文件和目标文件不能相同");
        }

        try (FileInputStream fin = new FileInputStream(inputPath);
             FileOutputStream fout = new FileOutputStream(outputPath)) {
            byte[] keyBytes = Integer.toString(key).getBytes(StandardCharsets.UTF_8);
            int keyIndex = 0;
            int data;
            while ((data = fin.read()) != -1) {
                fout.write(data ^ keyBytes[keyIndex++]);
                if (keyIndex == keyBytes.length) {
                    keyIndex = 0;
                }
            }
        }
    }
}
