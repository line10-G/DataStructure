public class LinkListStack_V1<T> implements IStack<T> {
    private SingleNode<T> head = null;
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
    public void push(T o) {
        SingleNode<T> singleNode = new SingleNode<>(o);
        singleNode.next = head;
        head = singleNode;
        curLen++;
    }

    @Override
    public void display() { //重新建立一个栈进行反转操作
        if(isEmpty()) throw new RuntimeException("栈为空");
        LinkListStack_V1<T> temp = new LinkListStack_V1<>();
        SingleNode<T> tmp = head;
        while(tmp != null){
            temp.push(tmp.data);
            tmp = tmp.next;
        }
        SingleNode<T> t = temp.head;
        while(t != null){
            System.out.print(t.data + " ");
            t = t.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try{
            LinkListStack_V1<Integer> list = new LinkListStack_V1<>();
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

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}