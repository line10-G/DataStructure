public interface Ilist <T>{
    public void clear();
    public boolean isEmpty();
    public int length();
    public T get(int i) throws Exception;
    public void insert(int i,T x) throws Exception;
    public void remove(int i) throws Exception;
    public int indexOf(T x);
    public void reverse();
    public void display();
}
