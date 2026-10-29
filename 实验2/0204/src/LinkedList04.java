public class LinkedList04<T>{
    public static class Node<T> {
        T data;
        Node<T> next;
        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    Node<T> head = null;

    private void append(T i){
        Node<T> node = new Node<>(i);
        if(head == null){
            head = node;
            return;
        }
        Node<T> temp = head;
        while(temp.next != null){
            temp = temp.next;
        }
        temp.next = node;
    }

    private void removeDuplicates(){
        Node<T> temp1 = head;
        while(temp1!=null){
            Node<T> temp2 = temp1;
            while(temp2.next!=null){
                if(temp1.data.equals(temp2.next.data)){
                    temp2.next = temp2.next.next;
                }else{
                    temp2 = temp2.next;
                }
//                temp2 = temp2.next;
            }
            temp1 = temp1.next;
        }
    }

    private void display(){
        Node<T> temp = head;
        while(temp!=null){
            System.out.print(temp.data);
            if(temp.next!=null){
                System.out.print(" ");
            }
            temp = temp.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try{
            String s = "ShangHai university ShangHai university of of electric electric power of";
            LinkedList04<String> test1 = new LinkedList04<>();
            for(String S:s.split(" ")){
                test1.append(S);
            }
            test1.display();

            test1.removeDuplicates();
            test1.display();

        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println();

        try{
            int[] arr = {123,123,456,45,67,67,89,90,90,90};
            LinkedList04<Integer> test2 = new LinkedList04<>();
            for(int val:arr){
                test2.append(val);
            }
            test2.display();

            test2.removeDuplicates();
            test2.display();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}