/**
* Тестовый объект с полями имя (name) и фамилия. Используется при тестировании класса MyArrayList
 */
public class MyObject {
    /** Поле имя */
    private String name;
    /** Поле фамилия */
    private String surname;

    /** Конструктор класса со всеми аргументами */
    public MyObject(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    /** Конструктор класса без аргументов */
    public MyObject() {
    }

    /** Геттеры имени и фамилии */
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
