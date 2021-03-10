package org.levelup.trello.reflection;

import lombok.Getter;

@Getter
public class Person {

    @RandomInteger(min = 0, max = 140)
    private int age; // значение будет генерировать автоматически

    private int weight;

    @RandomInteger(min = 20, max = 250)
    private int growth;

}
