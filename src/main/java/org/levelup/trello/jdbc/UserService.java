package org.levelup.trello.jdbc;

import org.levelup.trello.jdbc.JdbcConnectionService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserService {

    public void printUsers() {
        JdbcConnectionService connectionService = new JdbcConnectionService();
        try (Connection connection = connectionService.openConnection()) {

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from users");

            while (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                String login = resultSet.getString(2);
                String email = resultSet.getString("email");
                String name = resultSet.getString(4);

                System.out.println(String.join(" | ", id.toString(), login, email, name));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
