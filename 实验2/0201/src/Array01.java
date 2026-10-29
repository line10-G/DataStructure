class Array01<T> implements Ilist<T>{
    private Object[] arr;
    private int curLen;
    public static final int DEFAULT_SIZE = 10;
    public Array01(){
        this(DEFAULT_SIZE);
    }
    public Array01(int size){
        if(size < 1){
            throw new IllegalArgumentException("size must be greater than 0");
        }
        this.arr = new Object[size];
        this.curLen = 0;
    }

    @Override
    public void clear() {
        for(int i = 0; i < curLen; i++){
            arr[i] = null;
        }
        curLen = 0;
    }

    @Override
    public boolean isEmpty() {
        return curLen == 0;
    }

    @Override
    public int length() {
        return curLen;
    }

    @Override
    public T get(int i) throws Exception {
        if(i < 0 || i > curLen-1){
            throw new Exception("index out of range");
        }
        return (T)arr[i];
    }

    @Override
    public void insert(int i, T x) throws Exception {
        if(i < 0 || i > curLen){
            throw new Exception("index out of range");
        }
        if(curLen == arr.length){
            resize(arr.length*2);
        }
        for(int j = curLen; j > i; j--){
            arr[j] = arr[j-1];
        }
        arr[i] = x;
        curLen++;
    }

    private void resize(int i) {
        Object[] newArr = new Object[i];
        System.arraycopy(arr, 0, newArr, 0, curLen);
        arr = newArr;
    }

    @Override
    public void remove(int i) throws Exception {
        if(i < 0 || i > curLen-1){
            throw new Exception("index out of range");
        }
        if(curLen == arr.length/2){
            resize(arr.length/2);
        }
        for(int j = i; j <curLen-1; j++){
            arr[j] = arr[j+1];
        }
        arr[curLen-1] = null;
        curLen--;
    }

    @Override
    public int indexOf(T x) {
        for(int i = 0; i < curLen; i++){
            if(arr[i] == x){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void reverse() {
        if(curLen<=1)return;
        for(int i=0;i<curLen/2;i++){
            T temp = (T)arr[i];
            arr[i] = arr[curLen-i-1];
            arr[curLen-i-1] = temp;
        }
    }

    @Override
    public void display() {
        if(isEmpty()){
            System.out.println("empty");
        }
        for(int i = 0; i < curLen; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        try{
            Array01<Integer> arr1 = new Array01<>();

            int[] data1 = {10,20,30,40,50,60,70,80,90,10};
            for(int val:data1){
                arr1.insert(arr1.length(),val);
            }
            arr1.display();
            System.out.println(arr1.length());
            System.out.println();

            System.out.println(arr1.isEmpty());
            System.out.println();

            System.out.println(arr1.get(0));
            System.out.println(arr1.get(5));
//            System.out.println(arr.get(arr.length())); //index out of range
            System.out.println();

            arr1.insert(4,999);
            arr1.display();
            System.out.println(arr1.length());
//            arr.insert(-1,999); //index out of range
            System.out.println();

            arr1.remove(4);
            arr1.display();
            System.out.println(arr1.length());
//            arr.remove(-1); //index out of range
//            arr.remove(arr.curLen); //index out of range
            System.out.println();

            System.out.println(arr1.indexOf(10));
            System.out.println(arr1.indexOf(999));
            System.out.println();

            arr1.display();
            arr1.reverse();
            arr1.display();
            System.out.println();

            arr1.clear();
            System.out.println(arr1.isEmpty());
            arr1.display();
            System.out.println();

            Array01<Integer> arr2 = new Array01<>();
            int[] data2 = {10,20,30,40,50,60,70,80,90};
            for(int val:data2){
                arr2.insert(arr2.length(),val);
            }
            arr2.display();
            arr2.reverse();
            arr2.display();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}