import java.util.Arrays;

public class ArrayStack<T> implements IStack<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private int index = -1;
    private T[] stack;

    public ArrayStack() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayStack(int capacity) {
        if(capacity <= 0) throw new IllegalArgumentException("容量必须为正数");
        stack = (T[]) new Object[capacity];//不可以使用T[] stack = new T[int number]的方式建立泛型数列
    }

    @Override
    public void clear() {
        index = -1;
    }

    @Override
    public boolean isEmpty() {
        return index == -1;
    }

    @Override
    public int length() {
        return index +1;
    }

    @Override
    public T peek() {
        if(index == -1) {
            throw new RuntimeException("栈为空");
        }
        return stack[index];
    }

    @Override
    public T pop() {
        if(index == -1) {
            throw new RuntimeException("栈为空");
        }
        T tmp = stack[index];
        stack[index--] = null;
        shrinkCapacity();
        return tmp;
    }

    @Override
    public void push(T o){
        ensureCapacity();
        stack[++index] = o;
    }

    private void ensureCapacity() {
        if(index == stack.length) {
            stack = Arrays.copyOf(stack, stack.length*2);
        }
    }

    private void shrinkCapacity() {
        if(index > 0 && index == stack.length / 4) {
            stack = Arrays.copyOf(stack, stack.length / 2);
        }
    }

    @Override
    public void display() {
        if(index == -1) {
            throw new RuntimeException("栈为空");
        }
        for(int i = 0; i < index +1; i++){
            System.out.print(stack[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try{
            ArrayStack<Integer> stack = new ArrayStack<>();
            System.out.println(stack.isEmpty());
            stack.push(1);
            stack.push(2);
            stack.push(3);
            System.out.println(stack.isEmpty());
            stack.display();
            System.out.println(stack.peek());
            System.out.println(stack.length());
            stack.display();
            System.out.println(stack.pop());
            System.out.println(stack.length());
            stack.display();
            stack.clear();
            System.out.println(stack.length());
            System.out.println(stack.isEmpty());
            stack.display();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}