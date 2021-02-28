package org.levelup.trello.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private Integer id;
    private String name;
    private String login;
    private String password;

}