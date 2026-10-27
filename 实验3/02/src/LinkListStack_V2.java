public class LinkListStack_V2<T> implements IStack<T>{
    private DoubleNode<T> head = null;
    private int curLen = 0;

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
        if(isEmpty()) throw new RuntimeException("栈为空");
        return head.data;
    }

    @Override
    public T pop() {
        if(isEmpty()) throw new RuntimeException("栈为空");
        T data = head.data;
        head = head.next;
        curLen--;
        return data;
    }

    @Override
    public void push(T o){
        DoubleNode<T> node = new DoubleNode<>(o);
        if (head == null) {
            head = node;
        }else{
            head.prev = node;
            node.next = head;
            head = node;
        }
        curLen++;
    }

    @Override
    public void display() { //使用双向链表可以不用开新空间
        if(isEmpty()) throw new RuntimeException("栈为空");
        DoubleNode<T> tmp = head;
        while (tmp.next != null){
            tmp = tmp.next;
        }
        while(tmp != null){
            System.out.print(tmp.data+" ");
            tmp = tmp.prev;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try{
            LinkListStack_V2<Integer> list = new LinkListStack_V2<>();
            System.out.println(list.isEmpty());
            list.push(1);
            list.push(2);
            list.push(3);
            list.push(4);
            list.push(5);
            list.display();
            System.out.println(list.isEmpty());
            System.out.println(list.peek());
            System.out.println(list.length());
            list.display();
            System.out.println(list.pop());
            System.out.println(list.length());
            list.display();
            list.clear();
            System.out.println(list.length());
            System.out.println(list.isEmpty());
            list.display();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
