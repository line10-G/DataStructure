public class SeqString extends AbstractADTString {

    private char[] data;
    private int len;

    public SeqString() {
        this.data = new char[0];
        this.len = 0;
    }

    public SeqString(String s) {
        if (s == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        this.data = s.toCharArray();
        this.len = s.length();
    }

    protected SeqString(char[] arr, int len) {
        this.data = arr;
        this.len = len;
    }

    @Override
    public void strAssign(String s) {
        if (s == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        this.data = s.toCharArray();
        this.len = s.length();
    }

    @Override
    public int length() {
        return len;
    }

    @Override
    public char charAt(int pos) {
        checkPos(pos, len);
        return data[pos - 1];
    }

    @Override
    protected ADTString wrap(char[] arr, int n) {
        if (arr.length != n) {
            char[] a = new char[n];
            System.arraycopy(arr, 0, a, 0, n);
            return new SeqString(a, n);
        }
        return new SeqString(arr, n);
    }

    @Override
    public SeqString copy() {
        char[] a = new char[len];
        System.arraycopy(data, 0, a, 0, len);
        return new SeqString(a, len);
    }

    @Override
    public ADTString setCharAt(int pos, char c) {
        checkPos(pos, len);
        char[] a = new char[len];
        System.arraycopy(data, 0, a, 0, len);
        a[pos - 1] = c;
        return new SeqString(a, len);
    }

    @Override
    public ADTString concat(ADTString t) {
        if (t == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        int m = t.length();
        char[] a = new char[len + m];
        System.arraycopy(data, 0, a, 0, len);
        for (int i = 0; i < m; i++) {
            a[len + i] = t.charAt(i + 1);
        }
        return new SeqString(a, len + m);
    }

    @Override
    public ADTString substring(int pos, int len2) {
        checkRange(pos, len2, len);
        char[] a = new char[len2];
        System.arraycopy(data, pos - 1, a, 0, len2);
        return new SeqString(a, len2);
    }

    @Override
    public int index(ADTString t, int pos) {
        if (t == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        int n = len;
        int m = t.length();
        checkPos(pos, n + 1);
        if (m == 0) {
            return pos;
        }
        if (m > n - pos + 1) {
            return -1;
        }

        char[] p = new char[m];
        for (int i = 0; i < m; i++) {
            p[i] = t.charAt(i + 1);
        }

        int[] pi = new int[m];
        for (int i = 1, k = 0; i < m; i++) {
            while (k > 0 && p[i] != p[k]) {
                k = pi[k - 1];
            }
            if (p[i] == p[k]) {
                k++;
            }
            pi[i] = k;
        }

        int k = 0;
        for (int i = pos - 1; i < n; i++) {
            while (k > 0 && data[i] != p[k]) {
                k = pi[k - 1];
            }
            if (data[i] == p[k]) {
                k++;
            }
            if (k == m) {
                return i - m + 2; // 转成从 1 开始的位置
            }
        }
        return -1;
    }

    public int bfIndex(ADTString t, int pos) {
        return super.index(t, pos);
    }

    public int capacity() {
        return data.length;
    }
}
