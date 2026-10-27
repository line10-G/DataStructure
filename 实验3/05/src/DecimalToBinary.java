public class DecimalToBinary implements IStack{
    private final int size = 32;
    private final int[] stack = new int[size];
    private int index = -1;

    @Override
    public void push(int i) {
        stack[++index] = i;
    }

    @Override
    public int pop() {
        if(index == -1) throw new RuntimeException("栈为空");
        return stack[index--];
    }

    private void decimalToBinary(int data){
        boolean isNagative = data < 0;
        if( isNagative ){
            data = -data;
        }

        int carry = 0;
        for(int i=0;i<size;i++){
            int pushData = data % 2;
            if(isNagative){
                pushData ^= 1;
            }
            pushData += carry;
            if(isNagative && i==0){
                pushData += 1;
            }
            carry = 0;
            if(pushData > 1){
                pushData %= 2;
                carry = 1;
            }
            data /= 2;
            push(pushData);
        }

        for(int i=0;i<size;i++){
            System.out.print(pop());
        }
        System.out.println();
    }

    public static void main(String[] args) {
            DecimalToBinary d2b = new DecimalToBinary();
            d2b.decimalToBinary(1);
            d2b.decimalToBinary(0);
            d2b.decimalToBinary(-1);
            d2b.decimalToBinary(-99);
            d2b.decimalToBinary(99);
    }
}