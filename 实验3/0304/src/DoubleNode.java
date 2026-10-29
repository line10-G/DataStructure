public class DoubleNode <T>{
    T data;
    DoubleNode<T> next;
    DoubleNode<T> prev;
    public DoubleNode(T data){
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
