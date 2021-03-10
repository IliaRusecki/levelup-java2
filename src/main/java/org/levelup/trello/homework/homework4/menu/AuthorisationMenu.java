package org.levelup.trello.homework.homework4.menu;

import lombok.SneakyThrows;
import org.levelup.trello.model.User;
import org.levelup.trello.service.UserService;
import org.levelup.trello.service.jdbc.JdbcUserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class AuthorisationMenu {

   private final UserService userService;
   private final BufferedReader consoleReader;

    public AuthorisationMenu() {
        userService = new JdbcUserService();
        consoleReader = new BufferedReader(new InputStreamReader(System.in));

    }

    @SneakyThrows
    public User manageUserAuthorisation() {
        User user;
        System.out.println("Пожалуйста, выберите действие, нажав клавишу 1 или 2:");
        System.out.println("1. Регистрация\n2. Авторизация\n");
        System.out.println("Для завершения программы нажмите любую другую клавишу");
        String input = consoleReader.readLine();

        switch (input) {
            case "1": {
                System.out.println("Пройдите регистрацию");
                signUp();
            }
            case "2": {
                System.out.println("Пройдите авторизацию");
                user = signIn();
                break;
            }
            default: {
                System.out.println("Вы выбрали выйти из программы. Благодарим за использование нашего приложения");
                throw new RuntimeException();
            }
        }
        return user;
    }

    @SneakyThrows
    private void signUp() {
        String login = performLogin();
        System.out.println("Введите name:");
        String name = consoleReader.readLine();

        System.out.println("Введите email:");
        String email = consoleReader.readLine();

        System.out.println("Введите password:");
        String password = consoleReader.readLine();

        System.out.println("Введите team:");
        System.out.println("(команду, которой принадлежит пользователь (если команды не существует, то она будет создана)");
        String team = consoleReader.readLine();

        userService.createUser(login, email, name, password, team);

        System.out.println("Поздравляем, Вы успешно зарегестрировались!\n");
    }

    @SneakyThrows
    private User signIn() {
        while (true) {
            System.out.println("Введите login:");
            String login = consoleReader.readLine();

            System.out.println("Введите password:");
            String password = consoleReader.readLine();

            if (userService.authorizeUser(login, password)) {
                return userService.getUserByLogin(login);
            }
        }
    }


    @SneakyThrows
    private String performLogin() {
        String login;
        while (true) {
            System.out.println("Введите логин (не более 20 символов):");
            login = consoleReader.readLine();
            if (login.length() > 20) {
                System.out.println("Длина логина больше 20 символов");
                continue;
            }
            if (userService.checkUserInDB(login)) {
                System.out.printf("Пользователь с логином <%s> уже существует\n", login);
            } else {
                break;
            }
        }
        return login;
    }
}
