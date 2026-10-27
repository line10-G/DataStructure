import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();
        int[][] mat = new int[n][];
        for(int i=0;i<n;++i){
            mat[i] = new int[i+1];
            mat[i][0] = 1;
            mat[i][i] = 1;
            for(int j=1;j<i;++j){
                mat[i][j] = mat[i-1][j] + mat[i-1][j-1];
            }
        }
        for(int i=0;i<n;++i){
            for(int j=0;j<=i;++j){
                System.out.printf("%5d",mat[i][j]);
            }
            System.out.println();
        }
    }
}