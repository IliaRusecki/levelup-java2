package org.levelup.trello.service.jdbc;

import org.levelup.trello.jdbc.JdbcConnectionService;
import org.levelup.trello.model.Board;
import org.levelup.trello.model.User;
import org.levelup.trello.service.BoardsService;
import org.levelup.trello.service.TeamService;

import java.sql.*;

public class JdbcBoardsService implements BoardsService {
    private final JdbcConnectionService jdbcConnectionService;
    private final TeamService teamService;


    public JdbcBoardsService() {
        this.jdbcConnectionService = new JdbcConnectionService();
        this.teamService = new JdbcTeamService();
    }

    @Override
    public boolean updateBoard(User user, String oldName, String newName, boolean favourite) {
        try (Connection connection = jdbcConnectionService.openConnection()) {

            String sql = "UPDATE boards SET name = ?, favourite = ? WHERE owner_id = ? AND name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newName);
            statement.setBoolean(2, favourite);
            statement.setInt(3, user.getId());
            statement.setString(4, oldName);

            int affectedRows = statement.executeUpdate();
            return affectedRows != 0;

        } catch (SQLException exc) {
            System.out.println("Ошибка при работе с базой: " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void printBoards(User user) {
        try (Connection connection = jdbcConnectionService.openConnection()) {

            String sql = "SELECT name, favourite FROM boards WHERE owner_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString(1);
                String favourite = resultSet.getString(2);
                System.out.println(String.join(" | ", name, favourite));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean createBoard(User user, String newBoard, boolean favourite, String team) {
        try (Connection connection = jdbcConnectionService.openConnection()) {

            String sql = "INSERT into boards (name, favourite, owner_id, team_id) values ( ?, ?, ?, ?)";
            int teamId = teamService.getTeamIdByName(team);
            if (teamId != -1) {
                PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, newBoard);
                stmt.setBoolean(2, favourite);
                stmt.setInt(3, user.getId());
                stmt.setInt(4, teamId);
                stmt.execute();
                return true;
            } else {
                System.out.println("Данной команды не существует");
                return false;
            }

        } catch (SQLException exc) {
            System.out.println("Ошибка при работе с базой: " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }

    @Override
    public boolean deleteBoard(User user, String boardName) {
        try (Connection connection = jdbcConnectionService.openConnection()) {

            String sql = "DELETE FROM boards WHERE name = ? AND owner_id in (SELECT id FROM users WHERE login = ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, boardName);
            statement.setString(2, user.getLogin());
            int affectedRows = statement.executeUpdate();
            return affectedRows != 0;
        } catch (SQLException exc) {
            System.out.println("Ошибка при работе с базой: " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }

    @Override
    public Board getUserBoard(User user, String boardName) {
        try (Connection connection = jdbcConnectionService.openConnection()) {

            String sql = "SELECT board_id, name, favourite FROM boards WHERE owner_id = ? AND name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getId());
            statement.setString(2, boardName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Integer boardId = resultSet.getInt(1);
                String name = resultSet.getString(2);
                boolean favourite = resultSet.getBoolean(3);

                if (boardName.equals(name)) {
                    return new Board(boardId, name, favourite, user.getId());
                }
            }
            return null;
        } catch (SQLException exc) {
            System.out.println("Ошибка при работе с базой: " + exc.getMessage());
            throw new RuntimeException(exc);
        }
    }
}
