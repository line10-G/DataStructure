public class CompareTest {
    static int pass = 0, fail = 0;
    static void check(String label, long got, long want) {
        if (got == want) {
            pass++;
            System.out.println("  ok    " + label + " = " + got);
        } else {
            fail++;
            System.out.println("  错    " + label + " = " + got + "，期望 " + want);
        }
    }

    static SeqString str(String s) {
        SeqString x = new SeqString();
        x.strAssign(s);
        return x;
    }

    static void row(String algo, MatchStat st) {
        System.out.println(String.format("  %-4s  %8d  %8d  %8d  %8d",
                algo, st.index, st.compares, st.preprocess, st.total()));
    }

    static void head() {
        System.out.println("           返回位置      匹配比较    预处理    合计");
        System.out.println("           --------     --------   ------   ----");
    }

    static void case1() {
        String text = "cdbbacc", pat = "abcd";
        System.out.println("\n======== 用例 1：S=\"" + text + "\"（n=" + text.length()
                + "），T=\"" + pat + "\"（m=" + pat.length() + "），pos=1 ========");
        head();

        MatchStat bf = BruteForce.match(str(text), str(pat), 1);
        MatchStat kmp = KMP.match(str(text), str(pat), 1);
        row("BF", bf);
        row("KMP", kmp);
        System.out.println("  判定：");
        check("BF.index", bf.index, -1);
        check("BF.compares", bf.compares, 8);
        check("KMP.index", kmp.index, -1);
        check("KMP.compares", kmp.compares, 8);
        check("KMP.preprocess", kmp.preprocess, 2);
    }

    static void case2() {
        String text = "aaaaaaaaaa", pat = "aaaab";
        System.out.println("\n======== 用例 2：S=\"" + text + "\"（n=" + text.length()
                + "），T=\"" + pat + "\"（m=" + pat.length() + "），pos=1 ========");
        head();

        MatchStat bf = BruteForce.match(str(text), str(pat), 1);
        MatchStat kmp = KMP.match(str(text), str(pat), 1);
        row("BF", bf);
        row("KMP", kmp);
        System.out.println("  判定：");
        check("BF.index", bf.index, -1);
        check("BF.compares", bf.compares, 34);
        check("KMP.index", kmp.index, -1);
        check("KMP.compares", kmp.compares, 16);
        check("KMP.preprocess", kmp.preprocess, 3);

    }

    public static void main(String[] args) {
        case1();
        case2();

        System.out.println("\n======== 汇总 ========");
        System.out.println("  通过 " + pass + " 项，失败 " + fail + " 项");
        if (fail > 0) {
            throw new AssertionError("有 " + fail + " 项断言失败，请检查上面的输出");
        }
        System.out.println("  全部通过。");
    }
}
