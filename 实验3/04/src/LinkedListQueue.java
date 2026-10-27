public class LinkedListQueue <T> implements IQueue<T> {
    private DoubleNode<T> head = null;
    private int curLen = 0;//不能用static会使得所有实例共享一个值

    @Override
    public void clear() {
        head = null;
        curLen = 0;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public int length() {
        return curLen;
    }

    @Override
    public T peek() {
        if(isEmpty())return null;
        return head.data;
    }

    @Override
    public T delete() {
        if(isEmpty())return null;
        T data = head.data;
        if(head.next == head){//只剩一个节点时候需要head置空，否则判空条件不满足
            head = null;
        }else {
            head.prev.next = head.next;
            head.next.prev = head.prev;
            head = head.next;
        }
        curLen--;
        return data;
    }

    @Override
    public void insert(T o){
        DoubleNode<T> node = new DoubleNode<>(o);
        if(isEmpty()){
            head = node;
            head.prev = head;
            head.next = head;
        }else{
            node.prev = head.prev;
            node.next = head;
            head.prev.next = node;
            head.prev = node;
        }
        curLen++;
    }

    @Override
    public void display() {
        if(isEmpty())return;
        DoubleNode<T> tmp = head;
        while(tmp != head.prev){
            System.out.print(tmp.data + " ");
            tmp = tmp.next;
        }
        System.out.println(tmp.data);
    }

    public static void main(String[] args) {
        try{
            LinkedListQueue<Integer> queue = new LinkedListQueue<>();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
