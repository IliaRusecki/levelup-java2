package org.levelup.trello.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Board {
    private Integer id;
    private String name;
    private boolean favourite;
    private Integer userId;

}
