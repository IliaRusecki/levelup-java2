package org.levelup.trello.homework.homework3;

import org.levelup.trello.service.UserService;
import org.levelup.trello.service.jdbc.JdbcUserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppPerformAuthorization {
    public static void main(String[] args) throws IOException {

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите логин:");
        String login = consoleReader.readLine();

        System.out.println("Введите пароль:");
        String password = consoleReader.readLine();

        UserService userService = new JdbcUserService();
        boolean isUserExistInDB = userService.authorizeUser(login, password);

        if(isUserExistInDB) {
            System.out.printf("Вы вошли в систему под логином <%s>", login);
        } else {
            System.out.printf("Вы не прошли авторизацию", login);
        }
    }
}
