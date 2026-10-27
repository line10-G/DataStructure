public class KMP{
    public static int[] getNext(ADTString t){
        return buildNext(t,new int[1]);
    }

    public static int[] buildNext(ADTString t, int[] counter){
        int len = t.length();
        int[] next = new int[len+1];
        if(len == 0) return next;
        int i = 1, j = 0;
        next[1] = 0;
        while(i<len){
            if(j==0) {
                i++;
                j++;
                next[i] = j;
                continue;
            }
            counter[0]++;
            if(t.charAt(i) == t.charAt(j)){
                i++;
                j++;
                next[i] = j;
            }else{
                j = next[j];
            }
        }
        return next;
    }

    public static int index(ADTString s, ADTString t, int pos){
        return match(s, t, pos).index;
    }

    public static MatchStat match(ADTString s, ADTString t, int pos) {

        if (t == null) {
            throw new IllegalArgumentException("参数不能为null");
        }
        int n = s.length();
        int m = t.length();
        if (pos < 1 || pos > n + 1) {
            throw new IndexOutOfBoundsException("起始位置越界：pos = " + pos + "，允许范围 1.." + (n + 1));
        }
        if (m == 0) {
            return new MatchStat(pos, 0, 0);
        }

        int[] preCmp = new int[1];
        int[] next = buildNext(t, preCmp);
        int i = pos, j = 1, cmp = 0;
        while (j <= m && i <= n) {
            if (j == 0) {
                i++;
                j++;
                continue;
            }
            cmp++;
            if (s.charAt(i) == t.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        int at = (j > m) ? i - m : -1;
        return new MatchStat(at, cmp, preCmp[0]);
    }
}
