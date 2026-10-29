import java.util.Random;

public class StringTest {

    private static int pass = 0;
    private static int fail = 0;

    public static void main(String[] args) {
        System.out.println("=== 一、功能测试：顺序串 ===");
        suite(new SeqString("hello world"), "SeqString");

        System.out.println();
        System.out.println("=== 二、功能测试：链串（同一套用例） ===");
        suite(new LinkString("hello world"), "LinkString");

        System.out.println();
        System.out.println("=== 三、KMP 与 BF 对照（随机 3000 组） ===");
        crossCheck();

        System.out.println();
        System.out.println("=== 四、操作演示 ===");
        demo();

        System.out.println();
        System.out.println("---------------------------------------");
        System.out.println("通过 " + pass + " 项，失败 " + fail + " 项");
        if (fail > 0) {
            System.exit(1);
        }
    }

    private static void suite(ADTString s, String impl) {
        check(impl + " length", s.length(), 11);
        check(impl + " isEmpty", s.isEmpty(), false);
        check(impl + " charAt(1)", s.charAt(1), 'h');
        check(impl + " charAt(11)", s.charAt(11), 'd');
        check(impl + " toString", s.toString(), "hello world");

        check(impl + " substring(1,5)", s.substring(1, 5).toString(), "hello");
        check(impl + " substring(7,5)", s.substring(7, 5).toString(), "world");
        check(impl + " substring(12,0)", s.substring(12, 0).toString(), "");
        check(impl + " concat", s.concat(new SeqString("!")).toString(), "hello world!");
        check(impl + " insert(1)", s.insert(1, new SeqString(">>")).toString(), ">>hello world");
        check(impl + " insert(len+1)", s.insert(12, new SeqString("<<")).toString(), "hello world<<");
        check(impl + " delete(6,1)", s.delete(6, 1).toString(), "helloworld");
        check(impl + " delete(1,11)", s.delete(1, 11).toString(), "");
        check(impl + " replace(1,5)", s.replace(1, 5, new SeqString("HI")).toString(), "HI world");
        check(impl + " setCharAt(1)", s.setCharAt(1, 'H').toString(), "Hello world");
        check(impl + " 原对象未被修改", s.toString(), "hello world");

        check(impl + " index(world)", s.index(new SeqString("world"), 1), 7);
        check(impl + " index(o,1)", s.index(new SeqString("o"), 1), 5);
        check(impl + " index(o,6)", s.index(new SeqString("o"), 6), 8);
        check(impl + " index 未找到", s.index(new SeqString("xyz"), 1), -1);
        check(impl + " index 空模式", s.index(new SeqString(""), 3), 3);
        check(impl + " index 整串", s.index(new SeqString("hello world"), 1), 1);

        check(impl + " compareTo 相等", s.compareTo(new SeqString("hello world")), 0);
        check(impl + " compareTo 小于", s.compareTo(new SeqString("hello zzz")) < 0, true);
        check(impl + " compareTo 前缀", new SeqString("abc").compareTo(new SeqString("abcd")) < 0, true);

        check(impl + " replaceAll(o->0)", s.replaceAll(new SeqString("o"), new SeqString("0")).toString(), "hell0 w0rld");
        check(impl + " replaceAll(空目标)", s.replaceAll(new SeqString("zz"), new SeqString("-")).toString(), "hello world");

        ADTString c = s.copy();
        c.strAssign("changed");
        check(impl + " copy 独立性", s.toString(), "hello world");
        check(impl + " copy 内容", s.copy().toString(), "hello world");

        ADTString e = new SeqString("");
        check(impl + " 空串 length", e.length(), 0);
        check(impl + " 空串 isEmpty", e.isEmpty(), true);
        check(impl + " 空串 concat", e.concat(new SeqString("ab")).toString(), "ab");
        check(impl + " 空串 insert(1)", e.insert(1, new SeqString("ab")).toString(), "ab");

        check(impl + " charAt(0) 抛异常", throwsIOOBE(s, 0), true);
        check(impl + " charAt(12) 抛异常", throwsIOOBE(s, 12), true);
        check(impl + " substring 越界", throwsSub(s), true);
        check(impl + " insert(13) 越界", throwsInsert(s), true);
    }

    private static boolean throwsIOOBE(ADTString s, int pos) {
        try {
            s.charAt(pos);
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private static boolean throwsSub(ADTString s) {
        try {
            s.substring(10, 5);
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private static boolean throwsInsert(ADTString s) {
        try {
            s.insert(13, new SeqString("x"));
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private static void crossCheck() {
        Random r = new Random(20261021L);
        char[] alphabet = {'a', 'b', 'c'};
        int mismatch = 0;
        for (int round = 0; round < 3000; round++) {
            StringBuilder text = new StringBuilder();
            int n = r.nextInt(30);
            for (int i = 0; i < n; i++) {
                text.append(alphabet[r.nextInt(alphabet.length)]);
            }
            StringBuilder pat = new StringBuilder();
            int m = 1 + r.nextInt(5);
            for (int i = 0; i < m; i++) {
                pat.append(alphabet[r.nextInt(alphabet.length)]);
            }
            int pos = 1 + r.nextInt(n + 1);

            SeqString t = new SeqString(text.toString());
            ADTString p = new SeqString(pat.toString());
            int kmp = t.index(p, pos);
            int bf = t.bfIndex(p, pos);
            if (kmp != bf) {
                mismatch++;
                if (mismatch <= 5) {
                    System.out.println("  不一致：text=\"" + text + "\" pat=\"" + pat
                            + "\" pos=" + pos + " kmp=" + kmp + " bf=" + bf);
                }
            }
        }
        check("KMP 与 BF 结果一致（3000 组）", mismatch, 0);
    }

    private static void demo() {
        ADTString s = new SeqString("data structure");
        System.out.println("s = \"" + s + "\"  (长度 " + s.length() + ")");
        System.out.println("substring(1,4) = \"" + s.substring(1, 4) + "\"");
        System.out.println("index(\"structure\") = " + s.index(new SeqString("structure"), 1));
        System.out.println("insert(6,\"[\") = \"" + s.insert(6, new SeqString("[")) + "\"");
        System.out.println("delete(1,5)   = \"" + s.delete(1, 5) + "\"");
        System.out.println("replace(1,4,\"DATA\") = \"" + s.replace(1, 4, new SeqString("DATA")) + "\"");
        System.out.println("replaceAll(\" \",\"_\") = \"" + s.replaceAll(new SeqString(" "), new SeqString("_")) + "\"");
        System.out.println("concat(\"!!!\") = \"" + s.concat(new SeqString("!!!")) + "\"");

        ADTString[] both = {new SeqString("abc"), new LinkString("abc")};
        for (int i = 0; i < both.length; i++) {
            ADTString x = both[i];
            System.out.println(x.getClass().getSimpleName()
                    + "：长度=" + x.length()
                    + " index(\"c\")=" + x.index(new SeqString("c"), 1)
                    + " 拼接=\"" + x.concat(new SeqString("de")) + "\"");
        }
    }

    private static void check(String label, Object got, Object want) {
        boolean ok = (got == null) ? want == null : got.equals(want);
        if (ok) {
            pass++;
        } else {
            fail++;
            System.out.println("  [FAIL] " + label + "：期望=" + want + "，实际=" + got);
        }
    }

    private static void check(String label, int got, int want) {
        check(label, Integer.valueOf(got), Integer.valueOf(want));
    }

    private static void check(String label, char got, char want) {
        check(label, Character.valueOf(got), Character.valueOf(want));
    }

    private static void check(String label, boolean got, boolean want) {
        check(label, Boolean.valueOf(got), Boolean.valueOf(want));
    }
}
