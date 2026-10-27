public class KMPTest {
    public static void main(String[] args) {
        SeqString kmpString = new SeqString();
        kmpString.strAssign("ababcabcacbab");
        SeqString kmptest1 = new SeqString();
        kmptest1.strAssign("abcac");
        System.out.println(KMP.index(kmpString,kmptest1,1));//6
        SeqString BFtest2 = new SeqString();
        BFtest2.strAssign("a");
        System.out.println(KMP.index(kmpString,BFtest2,1));//1
        System.out.println(KMP.index(kmpString,BFtest2,2));//3
        SeqString BFtest3 = new SeqString();
        BFtest3.strAssign("bab");
        System.out.println(KMP.index(kmpString,BFtest3,10));//11
        System.out.println(KMP.index(kmpString,kmpString,1));//1
        SeqString BFtest4 = new SeqString();
        BFtest4.strAssign("xyz");
        System.out.println(KMP.index(kmpString,BFtest4,1));//-1
        SeqString BFtest5 = new SeqString();
        BFtest5.strAssign("abcacbax");
        System.out.println(KMP.index(kmpString,BFtest5,1));//-1
        SeqString BFtest6 = new SeqString();
        BFtest6.strAssign("");
        System.out.println(KMP.index(kmpString,BFtest6,3));//3
        System.out.println(KMP.index(BFtest6,BFtest6,1));//1
     }

}
