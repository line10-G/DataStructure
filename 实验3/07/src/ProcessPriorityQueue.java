public class ProcessPriorityQueue implements IQueue{
    private SingleNode head = null;
    private int length = 0;

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public void insert(int index, int value) {
        SingleNode node = new SingleNode(index,value);
        length++;
        if(head==null){
            head=node;
            return;
        }
        if(value<head.value){
            node.next=head;
            head=node;
            return;
        }
        SingleNode prev=head;
        SingleNode cur=head.next;
        while(cur!=null && cur.value<=value){
            prev=cur;
            cur=cur.next;
        }
        prev.next=node;
        node.next=cur;
    }

    @Override
    public void display() {
        if(head==null){
            throw new RuntimeException("队列为空");
        }
        SingleNode temp = head;
        while(temp!=null){
            System.out.print("["+temp.index+","+temp.value+"]");
            temp = temp.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ProcessPriorityQueue ppq = new ProcessPriorityQueue();
        System.out.println(ppq.isEmpty());
        System.out.println(ppq.length());
        ppq.insert(1,5);
        ppq.insert(2,10);
        ppq.insert(3,20);
        ppq.insert(4,10);
        ppq.insert(5,40);
        ppq.insert(6,10);
        System.out.println(ppq.isEmpty());
        System.out.println(ppq.length());
        ppq.display();
    }
}
