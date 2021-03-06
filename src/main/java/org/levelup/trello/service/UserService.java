package org.levelup.trello.service;

import org.levelup.trello.model.User;

public interface UserService {
    User createUser(String login, String email, String name, String password, String team);

    User getUserByLogin(String login);

    boolean authorizeUser(String login, String password);

    boolean checkUserInDB(String login);
}
