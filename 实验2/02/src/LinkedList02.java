public class LinkedList02<T> implements Ilist<T>{
    private final Node<T> head;
    private int length;
    public LinkedList02(){
        head = new Node<>(null);
        length = 0;
    }


    @Override
    public void clear() {
        head.next = null;
        length = 0;
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public T get(int i) throws Exception {
        if(i<0 || i>this.length()-1){
            throw new Exception("Invalid index");
        }
        Node<T> temp = head.next;
        for(int j=0;j<i;j++){
            temp = temp.next;
        }
        return temp.data;
    }

    @Override
    public Node<T> getNodePointer(int i) throws Exception {
        if(i<0 || i>this.length()-1){
            throw new Exception("Invalid index");
        }
        Node<T> temp = head.next;
        for(int j=0;j<i;j++){
            temp = temp.next;
        }
        return temp;
    }

    @Override
    public void insert(int i, T x) throws Exception {
        if(i<0 || i>this.length()){
            throw new Exception("Invalid index");
        }
        Node<T> temp = head;
        Node<T> node = new Node<>(x);
        for(int j=0;j<i;j++){
            temp = temp.next;
        }
        node.next = temp.next;
        temp.next = node;
        length++;
    }

    @Override
    public void remove(int i) throws Exception {
        if(i<0 || i>this.length()-1){
            throw new Exception("Invalid index");
        }
        Node<T> temp = head;
        for(int j=0;j<i;j++){
            temp = temp.next;
        }
        temp.next = temp.next.next;
        length--;
    }

    @Override
    public int indexOf(T x) {
        Node<T> temp = head.next;
        int i = 0;
        while(temp!=null){
            if(temp.data.equals(x)){
                return i;
            }
            temp = temp.next;
            i++;
        }
        return -1;
    }

    @Override
    public void display() {
        if(isEmpty()){
            System.out.println("Empty");
            return;
        }
        Node<T> temp = head.next;
        while(temp!=null){
            System.out.print(temp.data);
            if(temp.next != null){
                System.out.print(",");
            }
            temp = temp.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try{
            LinkedList02<Integer> lst = new LinkedList02<>();
            int[] arr = {1,2,3,4,5,6,7,8,9,10};
            for(int val:arr){
                lst.insert(lst.length(),val);
            }
            lst.display();
            System.out.println();

            System.out.println(lst.isEmpty());
            System.out.println();

            System.out.println(lst.length());
            lst.insert(0,1);
            System.out.println(lst.length());
            lst.display();
//            lst.insert(lst.length()+1,2); //Invalid index
            System.out.println();

            lst.remove(0);
            System.out.println(lst.length());
            lst.display();
//            lst.remove(lst.length()); //Invalid index
            System.out.println();

            System.out.println(lst.indexOf(2));
            System.out.println(lst.indexOf(11));
            System.out.println();

            System.out.println(lst.get(0));
            System.out.println(lst.getNodePointer(0));
//            System.out.println(lst.get(lst.length())); //Invalid index
//            System.out.println(lst.get(lst.length())); //Invalid index
            System.out.println();

            lst.clear();
            System.out.println(lst.isEmpty());
            lst.display();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}