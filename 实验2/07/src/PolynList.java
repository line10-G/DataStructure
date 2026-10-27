import java.util.Scanner;

public class PolynList {

    public PolynData insert(double index, int point, PolynData head){
        PolynData polynData = new PolynData(index,point);
        if (head == null) {
            return polynData;
        }
        PolynData temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = polynData;
        return head;
    }

    public PolynData Add(PolynData head1, PolynData head2) {
        PolynData res = null;
        PolynData resTail = res;
        PolynData temp1 = head1;
        PolynData temp2 = head2;

        while(temp1 != null && temp2 != null){
            PolynData polynData;
            if(temp1.point>temp2.point){
                polynData = temp2;
                temp2 = temp2.next;
            }else if(temp1.point<temp2.point){
                polynData = temp1;
                temp1 = temp1.next;
            }else{
                double sum = temp1.index + temp2.index;
                int point = temp1.point;
                temp1 = temp1.next;
                temp2 = temp2.next;
//                if(sum == 0){} 浮点型有误差
                if(Math.abs(sum) < 1e-9){
                    continue;
                }
                polynData = new PolynData(sum,point);
            }
            if(res == null){
                res = polynData;
                resTail = res;
            }else{
                resTail.next = polynData;
                resTail = polynData;
            }
        }
        PolynData rest = (temp1 != null)? temp1 : temp2;
        if(resTail == null){
            res = rest;
        }else{
            resTail.next = rest;
        }
        return res;
    }

    public void display(PolynData head){
        if(head == null){
            System.out.println("Null");
            return;
        }
        PolynData temp = head;
        while(temp != null){
            System.out.println("系数为： "+temp.index+" 指数为： "+temp.point);
            temp = temp.next;
        }
    }

    public static void main(String[] args) {
        PolynList polynList = new PolynList();
        PolynData head1 = null;
        PolynData head2 = null;
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        for(int i=0;i<n;i++){
            double index = sc.nextDouble();
            int point = sc.nextInt();
            head1 = polynList.insert(index,point,head1);
        }

        int m = sc.nextInt();
        for(int i=0;i<m;i++){
            double index = sc.nextDouble();
            int point = sc.nextInt();
            head2 = polynList.insert(index,point,head2);
        }
        polynList.display(polynList.Add(head1,head2));

    }

}
