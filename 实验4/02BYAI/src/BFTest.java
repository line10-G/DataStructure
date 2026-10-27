/**
 * 实验4-02 测试：用最小顺序串实现验证 BF 算法。
 * 接口只有 length/charAt/toString 三个方法，无需任何空壳实现。
 */
public class BFTest {

    /** 最小只读顺序串：仅实现 BF 需要的操作 */
    static class MyString implements ADTString {
        private final char[] data;

        MyString(String s) {
            if (s == null) throw new IllegalArgumentException("参数不能为 null");
            this.data = s.toCharArray();
        }

        @Override
        public int length() {
            return data.length;
        }

        @Override
        public char charAt(int pos) {
            if (pos < 1 || pos > data.length) {
                throw new IndexOutOfBoundsException("位置越界：pos = " + pos);
            }
            return data[pos - 1];
        }

        @Override
        public String toString() {
            return new String(data);
        }
    }

    private static int pass = 0;
    private static int fail = 0;

    public static void main(String[] args) {
        MyString s = new MyString("ababcabcacbab");

        check("index(abcac, 1)", BruteForceString.index(s, new MyString("abcac"), 1), 6);
        check("index(a, 1)", BruteForceString.index(s, new MyString("a"), 1), 1);
        check("index(a, 2)", BruteForceString.index(s, new MyString("a"), 2), 3);
        check("index(bab, 10)", BruteForceString.index(s, new MyString("bab"), 10), 11);
        check("index 整串", BruteForceString.index(s, s, 1), 1);
        check("index 未找到", BruteForceString.index(s, new MyString("xyz"), 1), -1);
        check("index 尾部不完整", BruteForceString.index(s, new MyString("abcacbax"), 1), -1);
        check("index 空模式串", BruteForceString.index(s, new MyString(""), 3), 3);
        check("index 空主串+空模式", BruteForceString.index(new MyString(""), new MyString(""), 1), 1);

        // 越界检查
        try {
            BruteForceString.index(s, new MyString("a"), 0);
            check("pos=0 抛异常", false, true);
        } catch (IndexOutOfBoundsException e) {
            check("pos=0 抛异常", true, true);
        }

        // 与 Java 标准库 indexOf 随机对照 3000 组
        java.util.Random r = new java.util.Random(20261025L);
        char[] alphabet = {'a', 'b', 'c'};
        int mismatch = 0;
        for (int round = 0; round < 3000; round++) {
            StringBuilder text = new StringBuilder();
            int n = r.nextInt(30);
            for (int i = 0; i < n; i++) text.append(alphabet[r.nextInt(alphabet.length)]);
            StringBuilder pat = new StringBuilder();
            int m = 1 + r.nextInt(5);
            for (int i = 0; i < m; i++) pat.append(alphabet[r.nextInt(alphabet.length)]);
            int pos = 1 + r.nextInt(n + 1);

            int bf = BruteForceString.index(new MyString(text.toString()),
                    new MyString(pat.toString()), pos);
            // 标准库从 0 开始计数，转成从 1 开始；未找到时 indexOf 返回 -1，保持 -1
            int idx = text.toString().indexOf(pat.toString(), pos - 1);
            int expected = (idx < 0) ? -1 : idx + 1;
            if (bf != expected) {
                mismatch++;
                if (mismatch <= 5) {
                    System.out.println("  不一致：text=\"" + text + "\" pat=\"" + pat
                            + "\" pos=" + pos + " bf=" + bf + " expected=" + expected);
                }
            }
        }
        check("BF 与标准库 indexOf 一致（3000 组）", mismatch, 0);

        System.out.println("---------------------------------------");
        System.out.println("通过 " + pass + " 项，失败 " + fail + " 项");
        if (fail > 0) System.exit(1);
    }

    private static void check(String label, int got, int want) {
        if (got == want) {
            pass++;
        } else {
            fail++;
            System.out.println("  [FAIL] " + label + "：期望=" + want + "，实际=" + got);
        }
    }

    private static void check(String label, boolean got, boolean want) {
        if (got == want) pass++;
        else {
            fail++;
            System.out.println("  [FAIL] " + label);
        }
    }
}
