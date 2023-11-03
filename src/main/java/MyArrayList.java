import java.util.Arrays;
import java.util.Comparator;
/**
 * Собственная реализация  <b>ArrayList</b> с использованием Generics.
 * Реализованы наиболее популярные методы ArrayList
 * Поиск реализован по алгоритму quickSort
 * @autor Михаил Тимченко
 * @version 1.0
 */
public class MyArrayList<E> {
    /** Массив, который находится "под капотом" у нашего ArrayList. Приватный, геттеров нет */
    private E[] array;

    /**
     * Количество фактических элементов, добавленных в ArrayList
     * Не равно размеру массива
     */
    private int size;

    /** Дефолтный размер массива под капотом. Задается при создании ArrayList */
    private static final int DEFAULT_ARRAY_SIZE = 10;


    /**
     * Конструктор ArrayList
     * создаем массив объектов E. Размер - значение по умолчанию
     * обнуляем фактическое количество элементов в списке (size)
     */
    public MyArrayList() {
        array = (E[]) new Object[DEFAULT_ARRAY_SIZE];
        size = 0;
    }

    /** Геттер количества фактически существующих в ArrayList элементов */
    public int getSize() {
        return size;
    }

    /**
     * Метод добавления в ArrayList нового элемента
     * @param element - добавляемый в список объект
     * Элемент добавляется в конец списка
     * Элементы будут находиться в списке в порядке их добавления (самый ранний - в ячейке 0).
     * При необходимости массив "под капотом" будет расширен
     */
    public void add(E element) {
        ensureArraySize();
        array[size] = element;
        size++;
    }

    /**
     * Перегруженный вариант метода добавления в ArrayList нового элемента
     * @param index - номер ячейки, в которую добавляем элемент
     * @param element - добавляемый в список объект
     * Элемент добавится в нужную ячейку, сместив все последующие элементы на 1 ячейку вперед
     * @throws IndexOutOfBoundsException в случае передачи некорректного номера ячейки
     * При необходимости массив "под капотом" будет расширен
     */
    public void add(int index, E element) throws IndexOutOfBoundsException {
        ensureArraySize();
        checkIndex(index);

        E[] newArray = (E[]) new Object[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        array = newArray;
        size++;
    }

    /**
     * Метод возвращает из ArrayList элемент по номеру индекса
     * @param index - номер ячейки, из которой получаем элемент
     * @return - возвращает искомый элемент из ячейки
     * @throws IndexOutOfBoundsException в случае передачи некорректного номера ячейки
     */
    public E get(int index) throws IndexOutOfBoundsException {
        checkIndex(index);
        return (E) array[index];
    }

    /**
     * Метод удаляет из ArrayList элемент по номеру индекса
     * @param index - номер ячейки, из которой удаляем элемент
     * @return - возвращает true в случае успеха
     * @throws IndexOutOfBoundsException в случае передачи некорректного номера ячейки
     * не уменьшает размер массива, в котором хранятся объекты
     * при удалении не оставляет null, сдвигая все последующие за удаленной ячейкой элементы влево
     */
    public boolean remove(int index) throws IndexOutOfBoundsException {
        checkIndex(index);
        E[] newArray = (E[]) new Object[array.length - 1];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);
        array = newArray;
        size--;
        return true;
    }

    /**
     * Перегруженный вариант метода удаления из ArrayList элемента
     * @param element - элемент, который необходимо удалить в списке
     * @return - возвращает true в случае успеха и false, если элемент не был найден в ArrayList
     * не уменьшает размер массива, в котором хранятся объекты
     * при удалении не оставляет null, сдвигая все последующие за удаленной ячейкой элементы влево
     */
    public boolean remove(E element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(array[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Метод очистки ArrayList. Не изменяет фактический размер массива, в котором хранятся объекты
     */
    public void clear() {
        array = (E[]) new Object[array.length];
        size = 0;
    }


    /**
     * Метод замены элемента в ArrayList.
     * @param index - номер ячейки, в которой заменяем элемент
     * @param element - элемент, который будет помещен в нужную ячейку
     * @return - возвращает ранее находящийся в этой ячейке списка элемент
     * @throws IndexOutOfBoundsException в случае передачи некорректного номера ячейки
     */
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = (E) array[index];
        array[index] = element;
        return oldElement;
    }


    /**
     * Метод для сортировки элементов в ArrayList.
     * Метод является перегруженным. Вариант метода sort() без параметров работает с Comparable объектами
     * Сортировка работает по принципу quicksort
     */
    public void sort() {
        E element = (E) array[0];
        if (!(element instanceof Comparable)) {
            throw new UnsupportedOperationException();
        }
        Comparable<?>[] newArray = new Comparable<?>[array.length];
        System.arraycopy(array, 0, newArray, 0, size);
        quickSort(newArray, 0, size - 1);

        for (int i = 0; i < array.length; i++) {
            array[i] = (E) newArray[i];
        }
    }

    /**
     * Метод для сортировки элементов в ArrayList.
     * @param comparator - компаратор, передаваемый в метод. Необходим для сравнения объектов
     * Сортировка работает по принципу quicksort
     */
    public void sort(Comparator<? super E> comparator) {
        quickSort(comparator, array, 0, size - 1);
    }


    public E[] toArray() {
        E[] newArray = (E[]) new Object[size];
        System.arraycopy(array, 0, newArray, 0, size);
        return newArray;
    }


    private void quickSort(Comparable<?>[] array, int low, int high) {
        if (array.length == 0)
            return;
        if (low >= high)
            return;
        int middle = low + (high - low) / 2;
        Comparable<?> opora = array[middle];
        int i = low;
        int j = high;

        while (i <= j) {
            while (((Comparable<E>) array[i]).compareTo((E) opora) < 0) {
                i++;
            }
            while ((((Comparable<E>) array[j]).compareTo((E) opora) > 0)) {
                j--;
            }

            if (i <= j) {
                swap(array, i, j);
                i++;
                j--;
            }
        }

        if (low < j)
            quickSort(array, low, j);

        if (high > i)
            quickSort(array, i, high);
    }


    private void quickSort(Comparator<? super E> comparator, E[] array, int low, int high) {
        if (array.length == 0)
            return;
        if (low >= high)
            return;
        int middle = low + (high - low) / 2;
        E opora = array[middle];
        int i = low;
        int j = high;

        while (i <= j) {
            while ((comparator.compare(array[i], opora)) < 0) {
                i++;
            }
            while ((comparator.compare(array[j], opora)) > 0) {
                j--;
            }

            if (i <= j) {
                swap(array, i, j);
                i++;
                j--;
            }
        }

        if (low < j)
            quickSort(comparator, array, low, j);

        if (high > i)
            quickSort(comparator, array, i, high);
    }


    private void swap(Object[] array, int i, int j) {
        Object temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void ensureArraySize() {
        if (size == array.length) {
            int newArraySize = ((int) (array.length * 1.5)) + 1;
            array = Arrays.copyOf(array, newArraySize);
        }
    }

    private void checkIndex(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
    }
}
