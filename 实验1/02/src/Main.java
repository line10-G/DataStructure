public class Main{
    public static void find(int[][] arr){
        int x = arr.length;
        int y = arr[0].length;
        int[] a = new int[x];
        int[] b = new int[y];
        for(int i=0;i<x;++i){
            a[i]=arr[i][0];
            for(int j=1;j<y;++j){
                a[i] = Math.max(arr[i][j],a[i]);
            }
        }
        for(int i=0;i<y;++i){
            b[i]=arr[0][i];
            for(int j=1;j<x;++j){
                b[i] = Math.min(arr[j][i],b[i]);
            }
        }
        for(int i=0;i<x;++i){
            for(int j=0;j<y;++j){
                if(arr[i][j]==a[i] && arr[i][j]==b[j]){
                    System.out.printf("(%d,%d)\n",i+1,j+1);
                    return;
                }
            }
        }
        System.out.println("No found");
    }

    public static void main(String[] args) {
        int[][] arr1 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        find(arr1);

        int[][] arr2 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 1}
        };
        find(arr2);

        int[][] arr3 = {
                {1, 2},
                {3, 4},
                {5, 6}
        };
        find(arr3);
    }
}