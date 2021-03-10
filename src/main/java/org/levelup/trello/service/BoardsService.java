package org.levelup.trello.service;

import org.levelup.trello.model.Board;
import org.levelup.trello.model.User;

public interface BoardsService {

    void printBoards(User user);

    boolean createBoard(User user, String newBoard, boolean favourite, String team);

    boolean updateBoard(User user, String oldBoard, String newBoard, boolean favourite);

    boolean deleteBoard(User user, String boardName);

    Board getUserBoard(User user, String boardName);

}
