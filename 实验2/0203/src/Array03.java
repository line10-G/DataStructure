import java.util.Arrays;

public class Array03 {
    int[] arr = new int[10];
    int size = 0;

    private void resize(){
        int[] tmp = new int[size*2];
        System.arraycopy(arr, 0, tmp, 0, size);
        arr = tmp;
    }

    private void insert(int i){
        if(size == arr.length){
            resize();
        }
        arr[size++] = i;
    }

    private void sort(){
//        for(int i = 0; i < size; i++){
//            for(int j=i+1;j<size;j++){
//                if(arr[i]>arr[j]){
//                    int tmp = arr[i];
//                    arr[i] = arr[j];
//                    arr[j] = tmp;
//                }
//            }
//        }
        Arrays.sort(arr, 0, size);
    }

    private void deleteSame(){
        if(size == 0) return;
        int[] res = new int[size];
        res[0] = arr[0];
        int resSize = 1;
        for(int i = 1; i < size; i++){
            if(res[resSize-1] != arr[i]){
                res[resSize++] = arr[i];
            }
        }
        arr = res;
        size = resSize;
    }

    private void display(){
        for(int i = 0; i < size; i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr1 = {2,2,3,1,3,1,3,4,5,6};
        Array03 m = new Array03();

        for(int val:arr1){
            m.insert(val);
        }
        m.display();

        m.sort();
        m.deleteSame();
        m.display();

        System.out.println();

        int[] arr2 = {2,3,1,3,1,3,4,5,6,8,6,5,7,45};
        for(int val:arr2){
            m.insert(val);
        }
        m.display();

        m.sort();
        m.deleteSame();
        m.display();
    }
}