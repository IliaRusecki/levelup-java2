package org.levelup.trello.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC - Java Database Connectivity
 */
public class JdbcConnectionService {

    /**
     * Open new connection to DB
     *
     * @return connection
     */
    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/trello",
                "postgres",
                "1234"
        );
    }
}
