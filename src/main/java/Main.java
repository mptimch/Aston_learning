public class Main {
    public static void main(String[] args) {
        MyArrayList<Object> objectMyArrayList = new MyArrayList<>();
        for (int i = 0; i < 100000; i++) {
            objectMyArrayList.add(0, new Object());
        }
    }
}