/**
 * 实验4-02：BF 算法视角的最小串接口。
 *
 * 与 01 的完整 ADT 接口不同，这里只声明 BF 算法实际需要的读操作——
 * 任何"可读的字符序列"都能作为 BF 的输入（接口隔离原则：
 * 不强迫实现类依赖它用不到的方法）。
 */
public interface ADTString {
    /** 返回串长 */
    int length();
    /** 取第 pos 个字符，位置从 1 开始，越界抛 IndexOutOfBoundsException */
    char charAt(int pos);
    /** 转成 Java 字符串，便于打印 */
    String toString();
}
