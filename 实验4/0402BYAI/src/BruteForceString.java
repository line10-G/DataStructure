public class BruteForceString {

    /**
     * BF（Brute-Force，朴素）模式匹配算法。
     * 返回模式串 t 在主串 s 中从第 pos 个字符开始第一次出现的位置（位置从 1 开始计数），
     * 未找到返回 -1。
     *
     * 算法思想：从主串的第 pos 个字符起，逐个字符与模式串比较；
     * 一旦失配，主串指针回溯到本轮起点的下一个位置，模式串指针回到第 1 个字符重新比较。
     * 时间复杂度 O(n*m)，n 为主串长度，m 为模式串长度。
     */
    public static int index(ADTString s, ADTString t, int pos) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        int n = s.length();
        int m = t.length();
        if (pos < 1 || pos > n + 1) {
            throw new IndexOutOfBoundsException(
                    "起始位置越界：pos = " + pos + "，允许范围 1.." + (n + 1));
        }
        if (m == 0) {
            return pos; // 空模式串约定匹配在 pos 处
        }

        int i = pos; // 主串当前比较位置（从 1 开始）
        int j = 1;   // 模式串当前比较位置（从 1 开始）
        while (i <= n && j <= m) {
            if (s.charAt(i) == t.charAt(j)) {
                i++;
                j++;
            } else {
                i = i - j + 2; // 主串指针回溯到本轮起点的下一个位置
                j = 1;         // 模式串指针回到开头
            }
        }
        if (j > m) {
            return i - m; // 匹配成功，返回匹配起点
        }
        return -1;
    }
}
