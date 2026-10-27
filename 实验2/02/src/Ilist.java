public interface Ilist <T>{
    public void clear();
    public boolean isEmpty();
    public int length();
    public T get(int index) throws Exception;
    public Node<T> getNodePointer(int i) throws Exception;
    public void insert(int i, T x) throws Exception;
    public void remove(int i) throws Exception;
    public int indexOf(T x);
    public void display();
}
