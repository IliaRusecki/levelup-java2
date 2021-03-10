package org.levelup.trello.service;

public interface TeamService {
    int createTeam(String name);

    int getTeamIdByName(String name);
}
