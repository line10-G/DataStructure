public class KMP{
    public static int[] getNext(ADTString t){ //这个是直接求匹配时候j失配后j该回到的位置，调用时候在1基时要用j = next[j];
        int len = t.length();
        int[] next = new int[len+1];
        if(len == 0) return next;//空串没有next，直接返回
        int i=1,j=0;
        next[1] = 0;
        while(i<len){
            if(j==0 || t.charAt(j)==t.charAt(i)){
                i++;
                j++;
                next[i] = j;
            }else{
                j = next[j];
            }
        }
        return next;
    }

    /**另一种next数组求法，获取的是最长相等前后缀长度，调用时候在1基时候要用j = next[j-1] + 1;
     * public static int[] getNext(ADTString t){
     *     int len = t.length();
     *     int[] next = new int[len+1];
     *     if(len == 0) return next;
     *     next[1] = 0;
     *     int k = 0;
     *     for(int j=2;j<=len;j++){
     *         while(k>0 && t.charAt(j) != t.charAt(k+1)){
     *             k = next[k];
     *         }
     *         if(t.charAt(j) == t.charAt(k+1)){
     *             k++;
     *         }
     *         next[j] = k;
     *     }
     *     return next;
     * }
     */

    public static int index(ADTString s, ADTString t, int pos){
        if(t==null){
            throw new IllegalArgumentException("参数不能为null");
        }
        int n = s.length();
        int m = t.length();
        if(pos<1 || pos>n+1){
            throw new IndexOutOfBoundsException("起始位置越界：pos = "+pos+"，允许范围 1.."+(n+1));
        }
        if(m==0){
            return pos;
        }
        int[] next = getNext(t);
        int i = pos;
        int j = 1;
        while(j<=m && i<=n){
            if(j==0 || s.charAt(i)==t.charAt(j)){
                i++;
                j++;
            }else{
                j = next[j];
            }
        }
        if(j>m){
            return i-m;
        }
        return -1;
    }
}
