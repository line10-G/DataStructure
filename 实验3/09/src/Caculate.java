public class Caculate implements IStack{
    private final static int CAPACITY = 30;
    private final double[] stack = new double[CAPACITY];
    private int top = -1;

    @Override
    public void push(double i) {
        stack[++top] = i;
    }

    @Override
    public double pop() {
        return stack[top--];
    }

    public double caculate(String s){
        int index = 0;
        double a,b,c,d;
        while(index < s.length()){
            char ch = s.charAt(index);
            switch(ch){
                case '+':
                    a = pop();
                    b = pop();
                    c = b + a;
                    push(c);
                    break;
                case '-':
                    a = pop();
                    b = pop();
                    c = b - a;
                    push(c);
                    break;
                case '*':
                    a = pop();
                    b = pop();
                    c = b * a;
                    push(c);
                    break;
                case '/':
                    a = pop();
                    b = pop();
                    if(a!=0) {
                        c = b / a;
                        push(c);
                    }else{
                        throw new RuntimeException("运行错误：除零");
                    }
                    break;
                default:
                    d=0;
                    while(ch>='0' && ch<='9'){
                        d=10*d+(ch-'0');
                        index++;
                        ch = s.charAt(index);
                    }
                    push(d);
                    break;
            }
            index++;
        }
        return pop();
    }

    public static void main(String[] args) {
        Caculate c = new Caculate();
        String s1 =  new String("1#2#+");
        String s2 =  new String("10#5#-");
        String s3 =  new String("6#7#*");
        String s4 =  new String("20#4#/");
        String s5 =  new String("56#20#-4#2#+/");
        System.out.println(c.caculate(s1));
        System.out.println(c.caculate(s2));
        System.out.println(c.caculate(s3));
        System.out.println(c.caculate(s4));
        System.out.println(c.caculate(s5));
    }
}
