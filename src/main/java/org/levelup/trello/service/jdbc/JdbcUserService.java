package org.levelup.trello.service.jdbc;

import lombok.SneakyThrows;
import org.levelup.trello.jdbc.JdbcConnectionService;
import org.levelup.trello.model.User;
import org.levelup.trello.service.UserService;
import org.postgresql.util.PSQLException;

import java.sql.*;


/**
 * Класс, ответсвенный за работу с таблица пользователей (user, user_credentials)
 */


public class JdbcUserService implements UserService {

    private final JdbcConnectionService jdbcConnectionService;

    public JdbcUserService() {
        this.jdbcConnectionService = new JdbcConnectionService();
    }

    @Override
    public User createUser(String login, String email, String name, String password) {
        try (Connection connection = jdbcConnectionService.openConnection()) {

            String sql = "insert into users ( login, name, email) values ( ?, ?, ?)";

            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, login);
            stmt.setString(2, name);
            stmt.setString(3, email);

            int rowsAffected = stmt.executeUpdate(); // количество строк, которые были изменены вашим запросом
            System.out.println("Количество строк, которое было изменено: " + rowsAffected);

            // if (hasResultSet) {
            //      ResultSet keys = stmt.getResultSet();
            //      keys.next();
            //      int generatedId = keys.getInt(1);
            // }
            ResultSet keys = stmt.getGeneratedKeys(); // набор сгенерированных ID
            keys.next();

            int generateId = keys.getInt(1); // получить ID
            System.out.println("ID пользователя: " + generateId);

            saveUserCredentials(connection, generateId, password);

            return new User(generateId, name, login, email);

        } catch (SQLException exc) {
            System.out.println("Ошибка при работе с базой: " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }

    @Override
    public boolean authorizeUser(String login, String password) {
        try (Connection connection = jdbcConnectionService.openConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id, login from users");

            while (resultSet.next()) {
                Integer userIdFromDB = resultSet.getInt(1);
                String loginFromDB = resultSet.getString(2);
                if (login.equals(loginFromDB)) {
                    String passwordFromDB = getUserPasswordById(connection, userIdFromDB);
                    if (password.equals(passwordFromDB)) {
                        return true;
                    } else {
                        System.out.println("Неверный пароль для данного логина.");
                        return false;
                    }
                }
            }
            System.out.println("Ваш логин неправильный.");
            return false;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public User getUser(String login) {
        try (Connection connection = jdbcConnectionService.openConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format("select * from users where login = '%s'", login));
            resultSet.next();
            String loginFromDB;
            try {
                loginFromDB = resultSet.getString(2);

            } catch (PSQLException exc) {
                System.out.printf("Юзера с логином <%s> нет в БД.%n", login);
                throw new RuntimeException(exc);
            }

            Integer userIdFromDB = resultSet.getInt(1);
            String nameFromDB = resultSet.getString(4);
            String password = getUserPasswordById(connection, userIdFromDB);

            return new User(userIdFromDB, nameFromDB, loginFromDB, password);

        } catch (SQLException exc) {
            System.out.println("Ошибка при работе с базой: " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }

    @SneakyThrows
    private void saveUserCredentials(Connection connection, Integer userId, String password) {
        PreparedStatement stmt = connection.prepareStatement("insert into user_credentials values (?, ?)");
        stmt.setInt(1, userId);
        stmt.setString(2, password);

        stmt.executeUpdate();
    }

    @SneakyThrows
    private String getUserPasswordById(Connection connection, Integer userId) {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format("select password from user_credentials where user_id = %s", userId));
        resultSet.next();
        return resultSet.getString(1);
    }

}
