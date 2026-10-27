public class ArrayQueue<T> implements IQueue<T> {
    private static final int  CAPACITY = 30;
    private int head = 0;
    private int tail = 0;
    private T[] lst;

    public ArrayQueue(){
        lst = (T[])new Object[CAPACITY];
    }

    @Override
    public void clear() {
        while (head != tail) {
            lst[head] = null;
            head = (head+1)%CAPACITY;
        }
    }

    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    @Override
    public int length() {
        return (tail-head+CAPACITY)%CAPACITY;
    }

    @Override
    public T peek() {
        if(isEmpty()){return null;}
        return lst[head];
    }

    @Override
    public T delete() {
        if(isEmpty())return null;
        T data = lst[head];
        lst[head] = null;
        head = (head+1)%CAPACITY;//在空时head与tail会不一样，队列状态破坏
        return data;
    }

    @Override
    public void insert(T o) throws Exception {
        if((tail+1)%CAPACITY==head){
            throw new Exception("队列已满");
        }
        lst[tail] = o;
        tail = (tail+1)%CAPACITY;
    }

    @Override
    public void display() {
        int tmp = head;
        while(tmp!=tail){
            System.out.print(lst[tmp]+" ");
            tmp = (tmp+1)%CAPACITY;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try{
            ArrayQueue<Integer> queue = new ArrayQueue<>();
            System.out.println(queue.isEmpty());
            queue.insert(1);
            queue.insert(2);
            queue.insert(3);
            queue.insert(4);
            System.out.println(queue.isEmpty());
            System.out.println(queue.length());
            System.out.println(queue.peek());
            queue.display();
            System.out.println(queue.delete());
            System.out.println(queue.length());
            queue.display();
            queue.clear();
            System.out.println(queue.isEmpty());
            System.out.println(queue.length());

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
