public class CheckPalindrome implements IStack {
    private final int size = 100;
    private final char[] stack = new char[size];
    private int top = -1;

    boolean isPalindrome(String str){
        int mid = str.length()/2;
        for(int i=0;i<mid;i++){
            push(str.charAt(i));
        }
        int start = (str.length()%2==1)? mid+1:mid;
        for(int i=start;i<str.length();i++){
//            if(str.length()%2==1) continue;这样会一直越过循环，不满足要求
            char ch = str.charAt(i);
            if(ch != peek()){
                return false;
            }
            pop();
        }
        return true;
    }

    @Override
    public void push(char o) {
        stack[++top] = o;
    }

    @Override
    public char pop() {
        if(top == -1) throw new RuntimeException("栈为空");
        return stack[top--];
    }

    @Override
    public char peek() {
        if(top == -1) throw new RuntimeException("栈为空");
        return stack[top];
    }

    public static void main(String[] args) {
        CheckPalindrome test = new CheckPalindrome();
        String s1 = "abba";
        String s2 = "abdba";
        String s3 = "abcdedcbe";
        String s4 = "1221";
        String s5 = "12322";
        System.out.println(test.isPalindrome(s1));
        System.out.println(test.isPalindrome(s2));
        System.out.println(test.isPalindrome(s3));
        System.out.println(test.isPalindrome(s4));
        System.out.println(test.isPalindrome(s5));
    }
}
