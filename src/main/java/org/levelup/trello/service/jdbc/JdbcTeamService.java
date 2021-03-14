package org.levelup.trello.service.jdbc;

import org.levelup.trello.homework.homework6.ConnectionPool;
import org.levelup.trello.jdbc.JdbcConnectionService;
import org.levelup.trello.service.TeamService;

import java.sql.*;

public class JdbcTeamService implements TeamService {
    private final JdbcConnectionService jdbcConnectionService;
    ConnectionPool connectionPool = ConnectionPool.create();

    public JdbcTeamService() {
        this.jdbcConnectionService = new JdbcConnectionService();
    }

    @Override
    public int createTeam(String name) {
        try {
            Connection connection = connectionPool.getConnection();

            String sql = "INSERT into team (name) values (?)";

            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.execute();

            ResultSet keys = stmt.getGeneratedKeys(); // набор сгенерированных ID
            keys.next();
            connectionPool.releaseConnection(connection);

            return keys.getInt(1); // получить ID


        } catch (SQLException exc) {
            System.out.println("Ошибка при работе с базой: " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }

    /**
     * This method returns Team Id by Team name or -1 if there's no such team in DB
     */

    @Override
    public int getTeamIdByName(String name) {
        try {
            Connection connection = connectionPool.getConnection();

            String sql = "SELECT id FROM team WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            connectionPool.releaseConnection(connection);

            int teamId = -1;
            while (resultSet.next()) {
                teamId = resultSet.getInt(1);
            }

            return teamId;
        } catch (
                SQLException exc) {
            System.out.println("Ошибка при работе с базой: " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }
}