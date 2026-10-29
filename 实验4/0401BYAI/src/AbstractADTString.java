public abstract class AbstractADTString implements ADTString {
    protected abstract ADTString wrap(char[] arr,int n);

    protected final char[] dump(){
        int n = length();
        char[] a = new char[n];
        for(int i=0;i<n;i++){
            a[i] = charAt(i + 1);
        }
        return a;
    }

    protected static void checkPos(int pos, int max){
        if(pos<1 || pos>max){
            throw new IndexOutOfBoundsException("位置越界：pos = "+ pos+"，允许范围 1.."+max);
        }
    }

    protected static void checkRange(int pos,int len,int total){
        if(pos<1 || pos>total+1){
            throw new IndexOutOfBoundsException("起始位置越界：pos = " + pos + "，允许范围 1.." + (total+1));
        }
        if(len<0){
            throw new IndexOutOfBoundsException("长度不能为负：len = " + len);
        }
        if(pos + len - 1 > total){
            throw new IndexOutOfBoundsException("区间越界：pos = "+pos+"，len = "+len+"，串长 = "+total);
        }
    }

    @Override
    public boolean isEmpty() {
        return length() == 0;
    }

    @Override
    public ADTString copy(){
        return wrap(dump(),length());
    }

    @Override
    public ADTString setCharAt(int pos,char c){
        checkPos(pos,length());
        char[] a = dump();
        a[pos-1] = c;
        return wrap(a,a.length);
    }

    @Override
    public ADTString concat(ADTString t) {
        if (t == null) throw new IllegalArgumentException("参数不能为null");
        int n1 = length();
        int n2 = t.length();
        char[] a = new char[n1+n2];
        int k = 0;
        for(int i=1;i<=n1;i++){
            a[k++] = charAt(i);
        }
        for(int i=1;i<=n2;i++){
            a[k++] = t.charAt(i);
        }
        return wrap(a,k);
    }

    @Override
    public ADTString substring(int pos, int len) {
        checkRange(pos, len, length());
        char[] a = new char[len];
        for (int i = 0; i < len; i++) {
            a[i] = charAt(pos + i);
        }
        return wrap(a, len);
    }

    @Override
    public ADTString insert(int pos, ADTString t) {
        if (t == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        int n = length();
        checkPos(pos, n + 1);
        int m = t.length();
        char[] a = new char[n + m];
        int k = 0;
        for (int i = 1; i < pos; i++) {
            a[k++] = charAt(i);
        }
        for (int i = 1; i <= m; i++) {
            a[k++] = t.charAt(i);
        }
        for (int i = pos; i <= n; i++) {
            a[k++] = charAt(i);
        }
        return wrap(a, k);
    }

    @Override
    public ADTString delete(int pos, int len) {
        int n = length();
        checkRange(pos, len, n);
        char[] a = new char[n - len];
        int k = 0;
        for (int i = 1; i < pos; i++) {
            a[k++] = charAt(i);
        }
        for (int i = pos + len; i <= n; i++) {
            a[k++] = charAt(i);
        }
        return wrap(a, k);
    }

    @Override
    public ADTString replace(int pos, int len, ADTString t) {
        if (t == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        int n = length();
        checkRange(pos, len, n);
        int m = t.length();
        char[] a = new char[n - len + m];
        int k = 0;
        for (int i = 1; i < pos; i++) {
            a[k++] = charAt(i);
        }
        for (int i = 1; i <= m; i++) {
            a[k++] = t.charAt(i);
        }
        for (int i = pos + len; i <= n; i++) {
            a[k++] = charAt(i);
        }
        return wrap(a, k);
    }

    @Override
    public int index(ADTString t, int pos) {
        if (t == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        int n = length();
        int m = t.length();
        checkPos(pos, n + 1);
        if (m == 0) {
            return pos;
        }
        for (int i = pos; i + m - 1 <= n; i++) {
            int j = 1;
            while (j <= m && charAt(i + j - 1) == t.charAt(j)) {
                j++;
            }
            if (j > m) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ADTString replaceAll(ADTString target, ADTString with) {
        if (target == null || with == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        if (target.isEmpty()) {
            throw new IllegalArgumentException("被替换的子串不能为空串，否则会无限匹配");
        }
        ADTString result = this;
        int from = 1;
        while (true) {
            int p = result.index(target, from);
            if (p < 0) {
                break;
            }
            result = result.replace(p, target.length(), with);
            from = p + with.length();
        }
        return result;
    }

    @Override
    public int compareTo(ADTString t) {
        if (t == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        int n = Math.min(length(), t.length());
        for (int i = 1; i <= n; i++) {
            char a = charAt(i);
            char b = t.charAt(i);
            if (a != b) {
                return a - b;
            }
        }
        return length() - t.length();
    }

    @Override
    public String toString() {
        return new String(dump());
    }
}
