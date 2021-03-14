package org.levelup.trello.service.jdbc;

import lombok.SneakyThrows;
import org.levelup.trello.homework.homework6.ConnectionPool;
import org.levelup.trello.jdbc.JdbcConnectionService;
import org.levelup.trello.model.User;
import org.levelup.trello.service.TeamService;
import org.levelup.trello.service.UserService;
import org.postgresql.util.PSQLException;

import java.sql.*;


/**
 * Класс, ответсвенный за работу с таблицами пользователей (user, user_credentials)
 */


public class JdbcUserService implements UserService {

    private final JdbcConnectionService jdbcConnectionService;
    private final TeamService teamService;
    ConnectionPool connectionPool = ConnectionPool.create();


    public JdbcUserService() {
        this.jdbcConnectionService = new JdbcConnectionService();
        this.teamService = new JdbcTeamService();
    }

    @Override
    public User createUser(String login, String email, String name, String password, String team) {
        try  {
            Connection connection = connectionPool.getConnection();
            String sql = "insert into users ( login, name, email) values ( ?, ?, ?)";

            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, login);
            stmt.setString(2, name);
            stmt.setString(3, email);

            int rowsAffected = stmt.executeUpdate(); // количество строк, которые были изменены вашим запросом
            System.out.println("Количество строк, которое было изменено: " + rowsAffected);

            ResultSet keys = stmt.getGeneratedKeys(); // набор сгенерированных ID
            keys.next();

            int generateId = keys.getInt(1); // получить ID
            System.out.println("ID пользователя: " + generateId);

            saveUserCredentials(connection, generateId, password);
            addUserToTeam(connection, generateId, team);
            connectionPool.releaseConnection(connection);

            return new User(generateId, name, login, email);

        } catch (SQLException exc) {
            System.out.println("Ошибка при работе с базой: " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }

    @Override
    public boolean authorizeUser(String login, String password) {
        try {
            Connection connection = connectionPool.getConnection();
            String sql = "SELECT u.id, u.login, u.email, u.name, cr.password " +
                    "FROM users u JOIN user_credentials cr ON u.id = cr.user_id WHERE u.login = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            connectionPool.releaseConnection(connection);

            while (resultSet.next()) {
                String loginFromDB = resultSet.getString(2);
                String passwordFromDB = resultSet.getString(5);
                if (login.equals(loginFromDB)) {
                    if (password.equals(passwordFromDB)) {
                        return true;
                    } else {
                        System.out.println("Неверный пароль для данного логина");
                        return false;
                    }
                }
            }
            System.out.println("Пользователя с данным логином не существует");
            return false;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean checkUserInDB(String login) {
        try {
            Connection connection = connectionPool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select login from users");
            connectionPool.releaseConnection(connection);

            while (resultSet.next()) {
                String loginFromDB = resultSet.getString(1);
                if (login.equals(loginFromDB)) {
                    return true;
                }
            }
            return false;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public User getUserByLogin(String login) {
        try {
            Connection connection = connectionPool.getConnection();
            String sql = "SELECT u.id, u.login, u.email, u.name, cr.password " +
                    "FROM users u JOIN user_credentials cr ON u.id = cr.user_id WHERE u.login = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
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
            String password = resultSet.getString(5);
            connectionPool.releaseConnection(connection);
            return new User(userIdFromDB, nameFromDB, loginFromDB, password);

        } catch (SQLException exc) {
            System.out.println("Ошибка при работе с базой: " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }

    public void printUsers() {
        try {
            Connection connection = connectionPool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users");
            connectionPool.releaseConnection(connection);

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String login = resultSet.getString(2);
                String email = resultSet.getString("email");
                String name = resultSet.getString(4);

                System.out.println(String.join(" | ", Integer.toString(id), login, email, name));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
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
    private void addUserToTeam(Connection connection, Integer userId, String teamName) {
        String sql = "select id from team where name = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, teamName);
        ResultSet resultSet = statement.executeQuery();

        int teamId = -1;
        while (resultSet.next()) {
            teamId = resultSet.getInt(1);
        }

        if (teamId == -1) {
            teamId = teamService.createTeam(teamName);
        }

        PreparedStatement insertStmt = connection.prepareStatement("insert into team_member values (?, ?)");
        insertStmt.setInt(1, teamId);
        insertStmt.setInt(2, userId);
        insertStmt.executeUpdate();
    }

}
