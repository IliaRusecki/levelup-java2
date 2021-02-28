package org.levelup.trello;

import org.levelup.trello.service.UserService;
import org.levelup.trello.service.jdbc.JdbcUserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TrelloApplication {
    public static void main(String[] args) throws IOException {

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите логин:");
        String login = consoleReader.readLine();

        System.out.println("Введите имя:");
        String name = consoleReader.readLine();

        System.out.println("Введите email:");
        String email = consoleReader.readLine();

        System.out.println("Введите пароль:");
        String password = consoleReader.readLine();

        UserService userService = new JdbcUserService();

        userService.createUser(login, email, name, password);

    }
}
