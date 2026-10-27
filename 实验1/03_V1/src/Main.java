public class Main {

    public static String toStr(double a,double b) {
        if(a!=0) {
            if (b > 0) return String.format("%.1f+%.1fi", a, b);
            else if (b < 0) return String.format("%.1f-%.1fi", a, -b);
            else return String.format("%.1f", a);
        }else{
            if (b > 0) return String.format("%.1fi", b);
            else if (b < 0) return String.format("-%.1fi", -b);
            else return "0";
        }
    }

    public static void add(double[] a, double[] b){
        double res_1 = a[0]+b[0];
        double res_2 = a[1]+b[1];
        System.out.println(toStr(res_1,res_2));
    }

    public static void minus(double[] a,double[] b){
        double res_1 = a[0]-b[0];
        double res_2 = a[1]-b[1];
        System.out.println(toStr(res_1,res_2));
    }

    public static void cmp(double[] a, double[] b){
        if(a[0]< b[0]){
            System.out.println(toStr(a[0],a[1])+ " < "+toStr(b[0],b[1]));
        }else if(a[0]> b[0]){
            System.out.println(toStr(a[0],a[1])+ " > "+toStr(b[0],b[1]));
        }else{
            if(a[1]< b[1]){
                System.out.println(toStr(a[0],a[1])+ " < "+toStr(b[0],b[1]));
            }else if(a[1]> b[1]){
                System.out.println(toStr(a[0],a[1])+ " > "+toStr(b[0],b[1]));
            }else{
                System.out.println(toStr(a[0],a[1])+ " = "+toStr(b[0],b[1]));
            }
        }
    }

    public static void main(String[] args) {
        double[] z1_1 = {0,0};
        double[] z1_2 = {0,0};
        add(z1_1,z1_2);
        minus(z1_1,z1_2);
        cmp(z1_1,z1_2);
        System.out.println();

        double[] z2_1 = {4,0};
        double[] z2_2 = {0,3};
        add(z2_1,z2_2);
        minus(z2_1,z2_2);
        cmp(z2_1,z2_2);
        System.out.println();

        double[] z3_1 = {3,1.5};
        double[] z3_2 = {8,-1.5};
        add(z3_1,z3_2);
        minus(z3_1,z3_2);
        cmp(z3_1,z3_2);
        System.out.println();

        double[] z4_1 = {-4,3.4};
        double[] z4_2 = {-6,-8.1};
        add(z4_1,z4_2);
        minus(z4_1,z4_2);
        cmp(z4_1,z4_2);

        double[] z5_1 = {-5.4,1.2};
        double[] z5_2 = {5.4,3.2};
        add(z5_1,z5_2);
        minus(z5_1,z5_2);
        cmp(z5_1,z5_2);
    }
}
//单纯用print不能判断使用+还是-链接实部和虚部->toStr(int a,int b)