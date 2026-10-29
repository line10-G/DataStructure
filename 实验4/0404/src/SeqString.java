public class SeqString implements ADTString{
    char[] data;
    int len;

    @Override
    public void strAssign(String s) {
        if(s==null){
            throw new IllegalArgumentException("参数不能为null");
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
        if(pos<1||pos>len){
            throw new IndexOutOfBoundsException("位置越界：pos = "+pos+"，允许范围 0.."+len);
        }
        return data[pos-1];
    }
}
