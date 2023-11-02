import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MyArrayListTest {

    private static MyObject[] objects;
    private static Integer[] integers;

    @BeforeAll
    public static void setUp() {
        objects = new MyObject[200];
        integers = new Integer[200];
        Random random = new Random();

        for (int i = 0; i < 200; i++) {
            String name = generatedString(random);
            String surname = generatedString(random);
            objects[i] = new MyObject(name, surname);
            integers[i] = (int) (Math.random() * 20000);
        }
    }


    // проверка размера ArrayList после вставки, а также сохранения порядка вставки. Проверка на примитивах
    @ParameterizedTest
    @CsvSource({ // параметры: количество элементов, номер элемента для проверки
            "10, 8",
            "60, 42",
            "90, 65"
    })
    void add(int size, int checkElement) {
        MyArrayList<Integer> list = new MyArrayList<>();
        Integer selectedValue = 0;

        for (int i = 0; i < size; i++) {
            list.add(integers[i]);

            if (i == checkElement) {
                selectedValue = integers[i];
            }
        }
        assertEquals(size, list.getSize());
        assertEquals(selectedValue, list.get(checkElement));

    }


    // проверка размера массива под капотом, а также корректно ли работает вставка по индексу
    @ParameterizedTest
    @CsvSource({ // параметры: количество элементов, индекс элемента для вставки
            "60, 8",
            "92, 42",
            "180, 65"
    })
    void AddWithIndex(int size, int elementIndex) throws Exception {
        MyArrayList<MyObject> list = new MyArrayList<>();
        MyObject myObject = objects[elementIndex];
        MyObject previousObject = objects[elementIndex - 1];
        MyObject nextObject = objects[elementIndex + 1];

        for (int i = 0; i < size; i++) {
            list.add(objects[i]);
        }

        // делаем массив под капотом доступным
        Field privateField = MyArrayList.class.getDeclaredField("array");
        privateField.setAccessible(true);
        Object array = privateField.get(list);
        int arraySize = ((Object[]) array).length;


        assertEquals(myObject, objects[elementIndex]);
        assertEquals(previousObject, objects[elementIndex - 1]);
        assertEquals(nextObject, objects[elementIndex + 1]);
        assertTrue(size <= arraySize);
    }

    @Test
    void getWithValidIndex() {
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(integers[i]);
        }
        assertEquals(integers[22], list.get(22));
    }

    @Test
    void getInvalidIndex() {
        MyArrayList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
    }


    @ParameterizedTest
    @CsvSource({ // параметры: количество элементов, индекс элемента для вставки
            "15, 4",
            "92, 0",
            "180, 175"
    })
    void removeWithIndex(int size, int index) {
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(integers[i]);
        }
        int deletedElement = integers[index];
        Integer nextElement = index + 1 < size ? integers[index + 1] : null;

        list.remove(index);

        assertNotEquals(deletedElement, list.get(index));

        if (nextElement != null) {
            assertEquals(nextElement, list.get(index));
        }
    }

    @Test
    void removeWithObject() {
        MyArrayList<MyObject> list = new MyArrayList<>();
        int index = 80;
        int size = 200;
        MyObject myObject = objects[index];
        MyObject previousObject = objects[index - 1];
        MyObject nextObject = objects[index + 1];

        for (int i = 0; i < size; i++) {
            list.add(objects[i]);
        }

        list.remove(myObject);
        assertEquals(nextObject, list.get(index));
        assertEquals(previousObject, list.get(index - 1));
        assertEquals(size - 1, list.getSize());
    }

    @Test
    void clear() {
        MyArrayList<Integer> list = new MyArrayList<>();
        int size = 50;
        for (int i = 0; i < size; i++) {
            list.add(integers[i]);
        }
        assertEquals(size, list.getSize());
        list.clear();
        assertEquals(list.getSize(), 0);
    }

    @Test
    void set() {
        MyArrayList<Integer> list = new MyArrayList<>();
        int size = 50;
        for (int i = 0; i < size; i++) {
            list.add(integers[i]);
        }

        int index = 15;
        Integer value = 123;
        list.set(index, value);
        assertEquals(value, list.get(index));
    }

    @Test
    void sortComparableClass() {
        MyArrayList<Integer> list = new MyArrayList<>();
        Integer[] unsortedArray = {5, 3, 1, 4, 2};
        Integer[] expectedArray = {1, 2, 3, 4, 5};

        for (Integer integer : unsortedArray) {
            list.add(integer);

        }
        list.sort();
        Object[] buffer = list.toArray();
        Integer[] sortedArray = Arrays.copyOf(buffer, buffer.length, Integer[].class);

        assertArrayEquals(sortedArray, expectedArray);
    }

    @Test
    void sortWithComparator() {
        MyArrayList<MyObject> list = new MyArrayList<>();
        for (int i = 0; i < 200; i++) {
            list.add(objects[i]);
        }
        list.add(new MyObject("Аааартем", "Петров"));
        Object[] buffer = list.toArray();
        MyObject[] unsortedArray = Arrays.copyOf(buffer, buffer.length, MyObject[].class);
        list.sort(Comparator.comparing(MyObject::getName));
        buffer = list.toArray();
        MyObject[] sortedArray = Arrays.copyOf(buffer, buffer.length, MyObject[].class);

        assertNotEquals(unsortedArray, sortedArray);
        assertNotEquals(sortedArray[0], unsortedArray[0]);
        assertEquals(sortedArray[0].getName(), "Аааартем");
    }


    @Test
    void toArray() {
        MyArrayList<Integer> list = new MyArrayList<>();
        Integer[] expectedArray = {1, 2, 3, 4, 5};
        for (Integer i : expectedArray) {
            list.add(i);
        }
        Object[] buffer = list.toArray();
        Integer[] realArray = Arrays.copyOf(buffer, buffer.length, Integer[].class);
        assertArrayEquals(realArray, expectedArray);
    }

    private static String generatedString(Random random) {
        int leftLimit = 1072; // буква 'а'
        int rightLimit = 1103; // буква 'я'
        int targetStringLength = 10;
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}