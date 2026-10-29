public class BFTest {
    public static void main(String[] args) {
        SeqString BFString = new SeqString();
        BFString.strAssign("ababcabcacbab");
        SeqString BFtest1 = new SeqString();
        BFtest1.strAssign("abcac");
        System.out.println(BruteForce.index(BFString,BFtest1,1));//6
        SeqString BFtest2 = new SeqString();
        BFtest2.strAssign("a");
        System.out.println(BruteForce.index(BFString,BFtest2,1));//1
        System.out.println(BruteForce.index(BFString,BFtest2,2));//3
        SeqString BFtest3 = new SeqString();
        BFtest3.strAssign("bab");
        System.out.println(BruteForce.index(BFString,BFtest3,10));//11
        System.out.println(BruteForce.index(BFString,BFString,1));//1
        SeqString BFtest4 = new SeqString();
        BFtest4.strAssign("xyz");
        System.out.println(BruteForce.index(BFString,BFtest4,1));//-1
        SeqString BFtest5 = new SeqString();
        BFtest5.strAssign("abcacbax");
        System.out.println(BruteForce.index(BFString,BFtest5,1));//-1
        SeqString BFtest6 = new SeqString();
        BFtest6.strAssign("");
        System.out.println(BruteForce.index(BFString,BFtest6,3));//3
        System.out.println(BruteForce.index(BFString,BFtest6,1));//1
    }
}
