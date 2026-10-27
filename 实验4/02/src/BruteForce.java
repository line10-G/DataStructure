public class BruteForce{
    public static int index (ADTString s,ADTString t, int pos) {
        if(s==null || t==null){
            throw new IllegalArgumentException("参数不能为null");
        }
        int n = s.length();
        int m = t.length();
        if(pos<1 || pos>n+1){
            throw new IllegalArgumentException("起始位置越界：pos = "+pos+"，允许范围 1.."+(n+1));
        }
        if(m==0){
            return pos;
        }

        int i = pos;
        int j = 1;
        while(i<=n && j<=m){
            if(s.charAt(i) == t.charAt(j)){
                i++;
                j++;
            }else{
                i = i-j+2;
                j = 1;
            }
        }
        if(j>m){
            return i-m;
        }
        return -1;
    }
}
