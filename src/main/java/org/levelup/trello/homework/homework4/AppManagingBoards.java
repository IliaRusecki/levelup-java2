package org.levelup.trello.homework.homework4;

import org.levelup.trello.homework.homework4.menu.AuthorisationMenu;
import org.levelup.trello.homework.homework4.menu.BoardsMenu;
import org.levelup.trello.model.User;

public class AppManagingBoards {

    public static void main(String[] args) {
        AuthorisationMenu authorisationMenu = new AuthorisationMenu();
        BoardsMenu boardsMenu = new BoardsMenu();

        User authorizedUser = authorisationMenu.manageUserAuthorisation();
        boardsMenu.mainBoardsMenu(authorizedUser);
    }
}
