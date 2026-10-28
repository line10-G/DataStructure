import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileCrypt {

    private static final int BUF_SIZE = 8192;
    private FileCrypt(){
    }

    public static void xorFile(Path src, Path dst, int key) throws IOException{
        if(key<0){
            throw new IllegalArgumentException("密钥不能为负数："+key);
        }
        xorFile(src,dst,keyBytes(String.valueOf(key)));
    }

    public static void xorFile(Path src, Path dst, String key) throws IOException{
        xorFile(src,dst,keyBytes(key));
    }

    public static byte[] keyBytes(String keyText){
        if(keyText==null || keyText.trim().isEmpty()){
            throw new IllegalArgumentException("密钥不能为空");
        }
        String s = keyText.trim();

        byte[] bytes;
        if(s.matches("\\d+")){
            try{
                bytes = bytesOfLong(Long.parseLong(s));
            } catch (NumberFormatException e){
                bytes = s.getBytes(StandardCharsets.UTF_8);
            }
        }else{
            bytes = s.getBytes(StandardCharsets.UTF_8);
        }

        if(allZero(bytes)){
            throw new IllegalArgumentException("密钥不能全为0");
        }
        return bytes;
    }

    public static void xorFile(Path src, Path dst, byte[] keyBytes) throws IOException {
        if(src==null || dst==null){
            throw new IllegalArgumentException("源文件路径和目标文件路径不能为null");
        }
        if(keyBytes==null || keyBytes.length==0){
            throw new IllegalArgumentException("密钥不为空");
        }
        if(!Files.exists(src)){
            throw new IOException("原文件不存在："+src.toAbsolutePath());
        }

        if(isSamePath(src, dst)){
            throw new IllegalArgumentException("源文件和目标文件不能相同："+src.toAbsolutePath());
        }

        Path parent = dst.toAbsolutePath().getParent();
        if(parent!=null){
            Files.createDirectories(parent);
        }

        try(InputStream in = Files.newInputStream(src);
            OutputStream out = Files.newOutputStream(dst)){

            byte[] buf = new byte[BUF_SIZE];
            int keyIndex = 0;
            int n;
            while((n = in.read(buf)) != -1){
                for(int i=0; i<n; i++){
                    buf[i] ^= keyBytes[keyIndex++];
                    if(keyIndex == keyBytes.length){
                        keyIndex = 0;
                    }
                }
                out.write(buf,0,n);
            }
        }
    }

    private static byte[] bytesOfLong(long v){
        byte[] all = new byte[8];
        for(int i=0; i<8; i++){
            all[i] = (byte)(v>>>(8*(7-i)));
        }
        int start = 0;
        while(start<7 && all[start]==0){
            start++;
        }
        byte[] out =  new byte[8 - start];
        System.arraycopy(all,start,out,0,out.length);
        return out;
    }

    private static boolean allZero(byte[] data){
        for(byte b:data){
            if(b!=0){
                return false;
            }
        }
        return true;
    }

    private static boolean isSamePath(Path a, Path b) throws IOException {
        if(a.toAbsolutePath().normalize().equals(b.toAbsolutePath().normalize())){
            return true;
        }
        if(Files.exists(a) && Files.exists(b)){
            return Files.isSameFile(a, b);
        }
        return false;
    }

}