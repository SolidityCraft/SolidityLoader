package io.soliditycraft.solidityloader.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A database class that provides methods for executing SQL statements using HikariCP and SQLite.
 * <p>This class allows executing arbitrary SQL statements for CRUD operations and transactions.</p>
 */
public class SolidityDatabase {

    private final HikariDataSource dataSource;

    /**
     * Constructs a SolidityDatabase instance with the specified database configuration.
     *
     * @param databaseUrl The JDBC URL of the SQLite database (e.g., "jdbc:sqlite:database.db").
     */
    public SolidityDatabase(String databaseUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000); // 30 seconds
        config.setIdleTimeout(600000); // 10 minutes

        this.dataSource = new HikariDataSource(config);
    }

    /**
     * Executes a SQL update statement (INSERT, UPDATE, DELETE).
     *
     * @param sql The SQL statement to execute.
     * @throws SQLException if a database access error occurs.
     */
    public void executeUpdate(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    /**
     * Executes a SQL query statement and returns the result as a ResultSet.
     *
     * @param sql The SQL query to execute.
     * @return A ResultSet containing the results of the query.
     * @throws SQLException if a database access error occurs.
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement.executeQuery();
    }

    /**
     * Begins a new transaction.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void beginTransaction() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
    }

    /**
     * Commits the current transaction.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void commitTransaction() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.commit();
        connection.setAutoCommit(true);
    }

    /**
     * Rolls back the current transaction.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void rollbackTransaction() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.rollback();
        connection.setAutoCommit(true);
    }

    /**
     * Closes the database connection pool when done.
     */
    public void close() {
        dataSource.close();
    }
}
