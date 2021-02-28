package org.levelup.trello.jdbc;

public class App {
    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.printUsers();
    }
}
