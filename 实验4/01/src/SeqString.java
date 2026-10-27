import java.util.Arrays;

public class SeqString implements ADTString{

    private char[] data;
    private int len;

    private SeqString(char[] data, int len) {
        this.data = data;
        this.len = len;
    }

    public SeqString(String s) {
        if (s == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        this.data = s.toCharArray();
        this.len = s.length();
    }

    public SeqString() {
        this.data = new char[0];
        this.len = 0;
    }

    private static void checkPos(int pos,int max){
        if(pos<1 || pos>max){
            throw new IndexOutOfBoundsException("位置越界：pos = "+pos+"，允许范围 1..."+max);
        }
    }

    private static void checkRange(int pos,int len,int total){
        if(pos<1 || pos>total){
            throw new IndexOutOfBoundsException("起始位置越界：pos = "+pos+"，允许范围 1..."+(total+1));
        }
        if(len<0){
            throw new IndexOutOfBoundsException("长度不能为负：len = "+len);
        }
        if(pos+len-1>total){
            throw new IndexOutOfBoundsException("区间越界：pos = "+pos+"，len = "+len+"，串长 = "+total);
        }

    }

    @Override
    public void strAssign(String s) {
        if(s == null){
            //return; 直接使用return会增加查找bug的时间成本
            throw new IllegalArgumentException("参数不能为null");
        }
        this.data = s.toCharArray();
        this.len = s.length();
    }

    @Override
    public SeqString copy() {//子类可返回更具体的类型
        char[] a = Arrays.copyOf(data, len);//整个数组复制一份的Arrays.copyOf更加简洁
        return new SeqString(a,len);
    }

    @Override
    public int length() {
        return len;
    }

    @Override
    public boolean isEmpty() {
        return len == 0;
    }

    @Override
    public char charAt(int pos) {
        checkPos(pos,len);
        return data[pos-1];
    }

    @Override
    public SeqString setCharAt(int pos, char c) {
        checkPos(pos,len);
        char[] a = Arrays.copyOf(data,len);//整个数组复制一份的Arrays.copyOf更加简洁
        a[pos-1] = c;
        return new SeqString(a,len);
    }

    @Override
    public SeqString concat(ADTString t) {
        if(t==null){
            throw new IllegalArgumentException("参数不能为null");
        }
        int m = t.length();
        char[] a = new char[len+m];
        System.arraycopy(data,0,a,0,len);//分段拼装的用System.arraycopy更优
        for(int i=0;i<m;i++){
            a[len+i]=t.charAt(i+1);
        }
        return new SeqString(a,len+m);
    }

    @Override
    public SeqString substring(int pos, int len2) {
        checkRange(pos,len2,len);
        char[] a = Arrays.copyOfRange(data,pos-1,pos-1+len2);//从数组中截一段装进新数组用Arrays.copyOfRange更加简洁
        return new SeqString(a,len2);
    }

    @Override
    public SeqString insert(int pos, ADTString t) {
        if(t==null){
            throw new IllegalArgumentException("参数不为null");
        }
        int n = length();
        checkPos(pos,n+1);
        int m = t.length();
        char[] a = new char[n+m];
        int k = 0;
        for(int i=1;i<pos;i++){
            a[k++] = charAt(i);
        }
        for(int i=1;i<=m;i++){
            a[k++] = t.charAt(i);
        }
        for(int i=pos;i<=n;i++){
            a[k++] = charAt(i);
        }
        return new SeqString(a,k);
    }

    @Override
    public SeqString delete(int pos, int len) {
        int n = length();
        checkRange(pos,len,n);
        char[] a = new char[n-len];
        int k = 0;
        for(int i=1;i<pos;i++){
            a[k++] = charAt(i);
        }
        for(int i=pos+len;i<=n;i++){
            a[k++] = charAt(i);
        }
        return new SeqString(a,k);
    }

    @Override
    public SeqString replace(int pos, int len, ADTString t) {
        if(t==null){
            throw new IllegalArgumentException("参数不能为null");
        }
        int n = length();
        checkRange(pos,len,n);
        int m = t.length();
        char[] a = new char[n-len+m];
        int k = 0;
        for(int i=1;i<pos;i++){
            a[k++] = charAt(i);
        }
        for(int i=1;i<=m;i++){
            a[k++] = t.charAt(i);
        }
        for(int i=pos+len;i<=n;i++){
            a[k++] = charAt(i);
        }
        return new SeqString(a,k);
    }

    @Override
    public SeqString replaceAll(ADTString target, ADTString with) {
        if(target==null || with==null){
            throw new IllegalArgumentException("参数不能为null");
        }
        if(target.isEmpty()){
            throw new IllegalArgumentException("被替换的子串不能为空串，否则会无限匹配");
        }
        SeqString result = this; // 原代码：ADTString result = this; 改为具体类型以配合协变返回
        int from = 1;
        while(true){
            int p = result.index(target,from);
            if(p<0){
                break;
            }
            result = result.replace(p,target.length(),with);
            from = p + with.length();
        }
        return result;
    }

    @Override
    public int index(ADTString t, int pos) {
        if(t==null){
            throw new IllegalArgumentException("参数不能为null");
        }
        int n = len;
        int m = t.length();
        checkPos(pos,n+1);
        if(m==0){
            return pos;
        }
        for(int i=pos;i+m-1<=n;i++){
            int j=1;
            while(j<=m && charAt(i+j-1)==t.charAt(j)){
                j++;
            }
            if(j>m){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int compareTo(ADTString t) {
        if(t==null){
            throw new IllegalArgumentException("参数不能为null");
        }
        int n = Math.min(length(),t.length());
        for(int i=0;i<n;i++){
            char a = charAt(i + 1);
            char b = t.charAt(i + 1);
            if(a!=b){
                return a - b;
            }
        }
        return length() - t.length();
    }

    @Override
    public String toString() {
        int n = length();
        char[] a = new char[n];
        for(int i=0;i<n;i++){
            a[i] = charAt(i+1);
        }
        return new String(a);
    }
}
