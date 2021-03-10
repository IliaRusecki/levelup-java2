package org.levelup.trello.reflection;

import org.levelup.trello.model.User;

import java.lang.reflect.Field;

public class ReflectionApp {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        User user = new User(1, "test user", "tuser", "t@user.com");

        // Через объект нашего класса
        Class<?> userClass = user.getClass(); // ? - любой тип
        // Через литерал .class
        Class<?> userClassLiteral = User.class;

        //
        System.out.println("Name: " + userClass.getName());

        Field[] fields = userClass.getDeclaredFields(); // вернет все поля вашего класса
        for (Field field : fields) {
            System.out.println(field.getType().getName() + " " + field.getName());
        }

        Field loginField = userClass.getDeclaredField("login");
        loginField.setAccessible(true);
        String login = (String) loginField.get(user);
        System.out.println("Login value: " + login);

        loginField.set(user, "newtestlogin");
        String newLogin = (String) loginField.get(user);
        System.out.println("New login value: " + newLogin);


//        Comparator<Integer> comp = new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return 0;
//            }
//        };
//        System.out.println(comp.getClass().getName());

    }

}
