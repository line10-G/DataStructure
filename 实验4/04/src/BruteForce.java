public class BruteForce {
    public static int index (ADTString s,ADTString t, int pos) {
        return match(s,t,pos).index;
    }

    public static MatchStat match(ADTString s,ADTString t,int pos){
        if(s==null || t==null){
            throw new IllegalArgumentException("参数不能为null");
        }
        int n = s.length();
        int m = t.length();
        if(pos<1 || pos>n+1){
            throw new IllegalArgumentException("起始位置越界：pos = "+pos+"，允许范围 1.."+(n+1));
        }
        if(m==0){
            return new MatchStat(pos,0,0);
        }

        int i = pos, j = 1, cmp = 0;
        while(i<=n && j<=m){
            cmp++;
            if(s.charAt(i)==t.charAt(j)){
                i++;
                j++;
            }else{
                i = i-j+2;
                j = 1;
            }
        }
        int at = (j>m)? i-m:-1;
        return new MatchStat(at,cmp,0);
    }
}
