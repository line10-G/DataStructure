public class LinkedListMerge<T extends Comparable<T>> {
    private final static class Node<T>{
        T data;
        Node<T> next;
        Node(T data){
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> append(T i,Node<T> head){
        Node<T> node = new Node<>(i);
        if(head == null){
            return node;
        }
        Node<T> temp = head;
        while(temp.next!=null){
            temp = temp.next;
        }
        temp.next = node;
        return head;
    }

    private Node<T> merge(Node<T> head1, Node<T> head2){
        LinkedListMerge<T> temp = new LinkedListMerge<>();
        Node<T> res =  null;
        while(head1!=null && head2!=null){
            if(head1.data.compareTo(head2.data)<=0){
                res = temp.append(head1.data,res);
                head1 = head1.next;
            }else{
                res = temp.append(head2.data,res);
                head2 = head2.next;
            }
        }
        Node<T> tmp = res;
        while(tmp.next!=null){
            tmp = tmp.next;
        }
        if(head1!=null) tmp.next = head1;
        if(head2!=null) tmp.next = head2;
        return res;
    }

    private void display(Node<T> head){
        while(head!=null){
            System.out.print(head.data);
            if(head.next!=null){
                System.out.print(" ");
            }
            head = head.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try{
            LinkedListMerge<Integer> test1 = new LinkedListMerge<>();
            Node<Integer> head1 = null;
            Node<Integer> head2 = null;
            int[] arr1 = {1,3,5,7,9};
            int[] arr2 = {2,4,6,7};
            for(int i:arr1){
                head1 = test1.append(i,head1);
            }
            for(int i:arr2){
                head2 = test1.append(i,head2);
            }
            test1.display(head1);
            test1.display(head2);

            Node<Integer> head3 = test1.merge(head1,head2);
            test1.display(head3);


        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println();

        try{
            LinkedListMerge<String> test2 = new LinkedListMerge<>();
            Node<String> head1 = null;
            Node<String> head2 = null;
            String[] s1 = {"a","c","d"};
            String[] s2 = {"b","c"};
            for(String s:s1){
                head1 = test2.append(s,head1);
            }
            for(String s:s2){
                head2 = test2.append(s,head2);
            }
            test2.display(head1);
            test2.display(head2);

            Node<String> head3 = test2.merge(head1,head2);
            test2.display(head3);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
