public class LinkString extends AbstractADTString {

    private static class Node {
        final char c;
        Node next;
        Node(char c) {
            this.c = c;
        }
    }

    private Node head;
    private Node tail;
    private int len;

    public LinkString() {
        this.head = new Node('\0');
        this.tail = this.head;
        this.len = 0;
    }

    public LinkString(String s) {
        this();
        strAssign(s);
    }

    private void append(char c) {
        Node p = new Node(c);
        tail.next = p;
        tail = p;
        len++;
    }

    @Override
    public void strAssign(String s) {
        if (s == null) {
            throw new IllegalArgumentException("参数不能为 null");
        }
        this.head = new Node('\0');
        this.tail = this.head;
        this.len = 0;
        for (int i = 0; i < s.length(); i++) {
            append(s.charAt(i));
        }
    }

    @Override
    public int length() {
        return len;
    }

    @Override
    public char charAt(int pos) {
        checkPos(pos, len);
        Node p = head.next;
        for (int i = 1; i < pos; i++) {
            p = p.next;
        }
        return p.c;
    }

    @Override
    protected ADTString wrap(char[] arr, int n) {
        LinkString s = new LinkString();
        for (int i = 0; i < n; i++) {
            s.append(arr[i]);
        }
        return s;
    }
}