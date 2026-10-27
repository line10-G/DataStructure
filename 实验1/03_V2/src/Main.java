public class Main {
    public static class Complex {
        double re;  // 实部
        double im;  // 虚部

        // 构造方法
        Complex(double re, double im) {
            this.re = re;
            this.im = im;
        }

        // 格式化输出
        public String toStr() {
            if (re == 0 && im == 0) return "0";
            if (re == 0) return String.format("%.1fi", im);
            if (im == 0) return String.format("%.1f", re);
            return String.format("%.1f%+.1fi", re, im);
        }
    }

    // 加法：传入两个 Complex 对象
    public static void add(Complex a, Complex b) {
        Complex res = new Complex(a.re + b.re, a.im + b.im);
        System.out.println(res.toStr());
    }

    // 减法
    public static void minus(Complex a, Complex b) {
        Complex res = new Complex(a.re - b.re, a.im - b.im);
        System.out.println(res.toStr());
    }

    // 比较：直接输出原始形式
    public static void cmp(Complex a, Complex b) {
        String left = a.toStr();
        String right = b.toStr();

        if (a.re < b.re) {
            System.out.println(left + " < " + right);
        } else if (a.re > b.re) {
            System.out.println(left + " > " + right);
        } else {
            if (a.im < b.im) {
                System.out.println(left + " < " + right);
            } else if (a.im > b.im) {
                System.out.println(left + " > " + right);
            } else {
                System.out.println(left + " = " + right);
            }
        }
    }

    public static void main(String[] args) {
        // 测试数据
        Complex z1_1 = new Complex(0, 0);
        Complex z1_2 = new Complex(0, 0);
        add(z1_1, z1_2);
        minus(z1_1, z1_2);
        cmp(z1_1, z1_2);
        System.out.println();

        Complex z2_1 = new Complex(4, 0);
        Complex z2_2 = new Complex(0, 3);
        add(z2_1, z2_2);
        minus(z2_1, z2_2);
        cmp(z2_1, z2_2);
        System.out.println();

        Complex z3_1 = new Complex(3, 1.5);
        Complex z3_2 = new Complex(8, -1.5);
        add(z3_1, z3_2);
        minus(z3_1, z3_2);
        cmp(z3_1, z3_2);
        System.out.println();

        Complex z4_1 = new Complex(-4, 3.4);
        Complex z4_2 = new Complex(-6, -8.1);
        add(z4_1, z4_2);
        minus(z4_1, z4_2);
        cmp(z4_1, z4_2);
        System.out.println();

        Complex z5_1 = new Complex(-5.4, 1.2);
        Complex z5_2 = new Complex(5.4, 3.2);
        add(z5_1, z5_2);
        minus(z5_1, z5_2);
        cmp(z5_1, z5_2);
    }
}