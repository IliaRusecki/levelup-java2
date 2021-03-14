package org.levelup.trello.homework.homework6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ConnectionPool {
    private static List<Connection> connectionPool;
    private final static int INITIAL_POOL_SIZE = 10;

    public ConnectionPool() {
    }

    public static ConnectionPool create() {
        connectionPool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            try {
                connectionPool.add(createConnection());
            } catch (SQLException exc) {
                System.out.println("Exception has occurred by initializing connection pool" + exc.getMessage());
                throw new RuntimeException(exc);
            }
        }
        return new ConnectionPool();
    }


    public Connection getConnection() {
        if(connectionPool.size() == 0) {
            System.out.println("There's no connection in connection pool");
            throw new RuntimeException();
        }
        return connectionPool.remove(connectionPool.size() - 1);
    }


    public void releaseConnection(Connection connection) {
        connectionPool.add(connection);
    }


    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/trello",
                "postgres",
                "1234"
        );
    }

}
