public interface IStack<T> {
    public void clear();
    public boolean isEmpty();
    public int length();
    public T peek();
    public T pop();
    public void push(T o) throws Exception;
    public void display();
}
