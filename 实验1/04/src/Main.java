import java.util.Random;

public class Main{
    public static void main(String[] args) {
        Random rand = new Random();
        int[] arr = new int[10];
        for(int i = 0; i < arr.length; i++){
            arr[i] = rand.nextInt(1000);
        }
        for (int i = 0; i < arr.length/2; i++){
            int tmp = arr[i];
            arr[i] = arr[9-i];
            arr[9-i] = tmp;
        }
        for (int i = 0; i < arr.length; i++){
            System.out.println(arr[i]);
        }
    }
}