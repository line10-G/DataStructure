import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileCryptV1 {
    private static final String MODULE_MARKER = "05V1.iml";

    public static void decode(String inputPath, String outputPath, int key) throws IOException {
        if (key <= 0) {
            throw new IllegalArgumentException("密钥必须是正整数：" + key);
        }

        Path in  = locate(inputPath, true);
        Path out = locate(outputPath, false);
        if (in.equals(out)) {
            throw new IllegalArgumentException("源文件和目标文件不能相同");
        }

        try (FileInputStream fin = new FileInputStream(in.toFile());
             FileOutputStream fout = new FileOutputStream(out.toFile())) {
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

    private static Path locate(String name, boolean mustExist) {
        Path p = Paths.get(name);
        if (p.isAbsolute()) {
            return p.normalize();
        }
        Path byCwd = p.toAbsolutePath().normalize();
        if (Files.exists(byCwd)) {
            return byCwd;
        }
        Path byRoot = moduleRoot().resolve(name).normalize();
        return (!mustExist || Files.exists(byRoot)) ? byRoot : byCwd;
    }

    private static Path moduleRoot() {
        Path cwd = Paths.get("").toAbsolutePath();
        for (Path up = cwd; up != null; up = up.getParent()) {
            if (Files.isRegularFile(up.resolve(MODULE_MARKER))) {
                return up;
            }
        }
        try (Stream<Path> walk = Files.walk(cwd, 3)) {
            return walk.filter(f -> MODULE_MARKER.equals(String.valueOf(f.getFileName())))
                       .map(Path::getParent)
                       .findFirst()
                       .orElse(cwd);
        } catch (IOException e) {
            return cwd;
        }
    }
}
