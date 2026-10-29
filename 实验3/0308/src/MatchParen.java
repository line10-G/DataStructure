public class MatchParen implements IStack{
    private final String[] strings = new String[30];
    private int top = -1;

    @Override
    public void push(String c) {
        strings[++top] = c;
    }

    @Override
    public String pop() {
        if(top == -1) return "";
        return strings[top--];
    }

    @Override
    public String peek() {
        if(top == -1) return "";
        return strings[top];
    }

    @Override
    public void clear() {
        top = -1;
    }

    public boolean match(String s){
        clear();//注意要重置top数值
        int len = s.length();
        boolean flag = false;
        for(int i=0;i<len;i++){
            char c = s.charAt(i);
            if(flag){
                flag = false;
                continue;
            }
            if(c=='/' && s.charAt(i+1)=='*'){
                push("/*");
                flag = true;
            }
            else if(c=='('){
                push("(");
            }
            else if(c=='['){
                push("[");
            }
            else if(c=='{'){
                push("{");
            }
            else if(c==')'){
                if(!peek().equals("(")){
                    return false;
                }
                pop();
            }
            else if(c==']'){
                if(!peek().equals("[")){
                    return false;
                }
                pop();
            }
            else if(c=='}'){
                if(!peek().equals("{")){
                    return false;
                }
                pop();
            }
            else if(c=='*' && s.charAt(i+1)=='/'){
                if(!peek().equals("/*")){
                    return false;
                }
                pop();
                flag = true;
            }
        }
        return top == -1;
    }

    public static void main(String[] args) {
        MatchParen mp = new MatchParen();
        String s1 = "(())/*abc{[)(]}*/";
        String s2 = "(()))abc/*{[]}*/";
        String s3 = "(()()abc{[]}/*";
        String s4 = "(())abc{[]}/**/";
        System.out.println(mp.match(s1));
        System.out.println(mp.match(s2));
        System.out.println(mp.match(s3));
        System.out.println(mp.match(s4));

    }
}
