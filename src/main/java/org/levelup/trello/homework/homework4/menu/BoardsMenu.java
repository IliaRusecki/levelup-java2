package org.levelup.trello.homework.homework4.menu;

import lombok.SneakyThrows;
import org.levelup.trello.model.User;
import org.levelup.trello.service.BoardsService;
import org.levelup.trello.service.jdbc.JdbcBoardsService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BoardsMenu {
    private final BoardsService boardsService;
    private final BufferedReader consoleReader;


    public BoardsMenu() {
        boardsService = new JdbcBoardsService();
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @SneakyThrows
    public void mainBoardsMenu(User user) {

        System.out.println("1. Управление досками\n");
        System.out.println("Пожалуйста, выберите пункт меню, нажав клавишу 1");
        System.out.println("Для завершения программы нажмите любую другую клавишу");

        String input = consoleReader.readLine();

        if ("1".equals(input)) {
            manageBoardServices(user);
        }
    }

    @SneakyThrows
    public void manageBoardServices(User user) {
        boolean repeatAction = true;

        while (repeatAction) {

            System.out.printf("[%s] 1. Вывести список досок trello\n", user.getLogin());
            System.out.printf("[%s] 2. Создать новую доску\n", user.getLogin());
            System.out.printf("[%s] 3. Редактировать доску\n", user.getLogin());
            System.out.printf("[%s] 4. Удалить доску\n", user.getLogin());
            System.out.println("\nПожалуйста, выберите пункт меню, нажав клавишу 1, 2, 3 или 4");
            System.out.println("Для завершения программы нажмите любую другую клавишу");

            switch (consoleReader.readLine()) {
                case ("1"): {
                    printAllBoardsBelongsToUser(user);
                    break;
                }
                case ("2"): {
                    createBoard(user);
                    break;
                }
                case ("3"): {
                    updateBoard(user);
                    break;
                }
                case ("4"): {
                    deleteBoard(user);
                    break;
                }
                default: {
                    System.out.println("Вы выбрали выйти из программы. Благодарим за использование нашего приложения");
                    repeatAction = false;
                    break;
                }
            }
        }
    }


    @SneakyThrows
    public void printAllBoardsBelongsToUser(User user) {
        boolean repeatAction = true;
        while (repeatAction) {
            boardsService.printBoards(user);
            repeatAction = checkIfUserWantsToProceed();
        }

    }

    @SneakyThrows
    public void deleteBoard(User user) {
        boolean repeatAction = true;
        while (repeatAction) {
            System.out.println("Введите название доски, которую хотите удалить");
            String userInput = consoleReader.readLine();
            boolean isBoardDeleted = boardsService.deleteBoard(user, userInput);
            if (isBoardDeleted) {
                System.out.println("Выбранная доска успешно удалена");

            } else {
                System.out.println("У вас нет доски с таким названием.");
            }
            repeatAction = checkIfUserWantsToProceed();
        }
    }

    @SneakyThrows
    public void createBoard(User user) {
        boolean repeatAction = true;

        while (repeatAction) {
            System.out.println("Введите название доски");
            String boardName = consoleReader.readLine();
            System.out.println("Задайте параметр favourite, введите \"true\" или \"false\"");
            String favourite = consoleReader.readLine();
            System.out.println("Введите название вашей команды, для которой вы хотите создать доску");
            String team = consoleReader.readLine();
            boolean isBoardCreated = boardsService.createBoard(user, boardName, Boolean.parseBoolean(favourite), team);
            if (isBoardCreated) {
                System.out.println("Выбранная доска успешно создана");

            } else {
                System.out.println("Доска не изменена. Попробуйте еще раз или нажмите 0 для возврата в меню");
            }
            repeatAction = checkIfUserWantsToProceed();
        }
    }

    @SneakyThrows
    public void updateBoard(User user) {
        boolean repeatAction = true;
        while (repeatAction) {
            System.out.println("Введите название доски, которую хотите изменить");
            String oldBoard = consoleReader.readLine();
            System.out.println("Введите новое название доски");
            String newBoard = consoleReader.readLine();
            System.out.println("Задайте параметр favourite Введите \"true\" или \"false\"");
            String favourite = consoleReader.readLine();

            boolean isBoardUpdated = boardsService.updateBoard(user, oldBoard, newBoard, Boolean.parseBoolean(favourite));
            if (isBoardUpdated) {
                System.out.println("Выбранная доска успешно изменена");

            } else {
                System.out.println("У вас нет доски с таким названием.");
            }
            repeatAction = checkIfUserWantsToProceed();
        }
    }

    @SneakyThrows
    private boolean checkIfUserWantsToProceed() {
        System.out.println("\nДля возврата в основное меню нажмите 0 или любую другую клавишу, если хотите продолжить в текущем подменю");
        String userInput = consoleReader.readLine();
        return !("0".equals(userInput));
    }
}
