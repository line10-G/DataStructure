public class StringTest {
    public static void main(String[] args) {
        SeqString s = new SeqString();
        System.out.println("s.length() = " + s.length());
        SeqString s2 = new SeqString();
        s2.strAssign("data structure");
        s = s.concat(s2); // 原代码：s = (SeqString) s.concat(s2); 协变返回后无需强转
        System.out.println("s = \"" + s + "\"");
        System.out.println("s.length() = " + s.length());
        System.out.println("substring(1,4) = \"" + s.substring(1, 4) + "\"");
        System.out.println("index(\"structure\") = " + s.index(new SeqString("structure"), 1));
        System.out.println("insert(6,\"[\") = \"" + s.insert(6, new SeqString("[")) + "\"");
        System.out.println("delete(1,5)   = \"" + s.delete(1, 5) + "\"");
        System.out.println("replace(1,4,\"DATA\") = \"" + s.replace(1, 4, new SeqString("DATA")) + "\"");
        System.out.println("replaceAll(\" \",\"_\") = \"" + s.replaceAll(new SeqString(" "), new SeqString("_")) + "\"");
        System.out.println("concat(\"!!!\") = \"" + s.concat(new SeqString("!!!")) + "\"");

        System.out.println();
        System.out.println("--- copy 测试 ---");
        SeqString origin = new SeqString("hello world");
        SeqString copied = origin.copy();

        System.out.println("origin = \"" + origin + "\"");
        System.out.println("copied = \"" + copied + "\"");
        System.out.println("copy 内容一致: " + (origin.compareTo(copied) == 0));

        SeqString changed = copied.setCharAt(1, 'H');
        System.out.println("修改副本后 copied = \"" + changed + "\"");
        System.out.println("修改副本后 origin = \"" + origin + "\" (应该还是 hello world)");
        System.out.println("copy 是不同对象: " + (origin != copied));

        SeqString emptyCopy = new SeqString("").copy();
        System.out.println("空串 copy 长度 = " + emptyCopy.length() + " (应该是 0)");

        System.out.println();
        System.out.println("--- compareTo 测试 ---");
        SeqString a = new SeqString("apple");
        SeqString b = new SeqString("apple");
        SeqString c = new SeqString("banana");
        SeqString d = new SeqString("app");

        System.out.println("apple vs apple = " + a.compareTo(b) + " (应该是 0)");
        System.out.println("apple vs banana = " + a.compareTo(c) + " (应该是负数, a<b)");
        System.out.println("banana vs apple = " + c.compareTo(a) + " (应该是正数, b>a)");
        System.out.println("app vs apple = " + d.compareTo(a) + " (应该是负数, 前缀更短的是小串)");
        System.out.println("apple vs app = " + a.compareTo(d) + " (应该是正数)");

    }
}
