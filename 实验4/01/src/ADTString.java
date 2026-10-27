public interface ADTString {
    void strAssign(String s);
    ADTString copy();
    int length();
    boolean isEmpty();
    char charAt(int pos);
    ADTString setCharAt(int pos, char c);
    ADTString concat(ADTString t);
    ADTString substring(int pos, int len);
    ADTString insert(int pos, ADTString t);
    ADTString delete(int pos, int len);
    ADTString replace(int pos, int len, ADTString t);
    ADTString replaceAll(ADTString target, ADTString with);
    int index(ADTString t, int pos);
    int compareTo(ADTString t);
    String toString();
}