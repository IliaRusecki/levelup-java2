package org.levelup.trello.reflection;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Random;

public class RandomIntegerAnnotationProcessor {

    @SneakyThrows // lombok annotation
    public Person generatePerson() {
        Class<Person> personClass = Person.class;

        // Создадим объект

        // class SomeClass { SomeClass(Integer i, String s) {} SomeClass(int i, String s) {} }
        // c1 = someClass.getDeclaredConstructor(Integer.class, String.class) - first constructor
        // c2 = someClass.getDeclaredConstructor(int.class, String.class) - second constructor
        // c2.newInstance(123, "some string");

        Constructor<Person> defaultConstructor = personClass.getDeclaredConstructor();// ничего не передаем, так как параметров у конструктора нет
        Person object = defaultConstructor.newInstance();

        Field[] allFields = personClass.getDeclaredFields();
        for (Field field : allFields) {
            RandomInteger annotation = field.getAnnotation(RandomInteger.class); // RandomInteger.class -> Class<RandomInteger>
            if (annotation != null) {
                Random r = new Random();
                int randomInteger = r.nextInt(annotation.max() - annotation.min()) + annotation.min();

                // установка значения в поле объекта
                field.setAccessible(true);
                field.set(object, randomInteger); // значение randomInteger в поле field у объекта object
            }
        }

        return object;
    }

}
