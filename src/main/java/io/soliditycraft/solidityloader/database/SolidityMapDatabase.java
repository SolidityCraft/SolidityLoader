package io.soliditycraft.solidityloader.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A key-value database class that provides CRUD operations using HikariCP for connection pooling.
 *
 * <p>This class allows you to store, retrieve, update, and delete key-value pairs in a relational database.</p>
 *
 * <p>Ensure that the database is initialized and the necessary table is created before using this class.
 * The table structure should be similar to the following:</p>
 *
 * <pre>
 * CREATE TABLE IF NOT EXISTS key_value_store (
 *     `key` VARCHAR(255) PRIMARY KEY,
 *     `value` TEXT
 * );
 * </pre>
 */
public class SolidityMapDatabase {

    private final HikariDataSource dataSource;

    /**
     * Constructs a SolidityMapDatabase instance with the specified database configuration.
     *
     * @param jdbcUrl  The JDBC URL of the database.
     * @param username The username for the database connection.
     * @param password The password for the database connection.
     */
    public SolidityMapDatabase(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000); // 30 seconds
        config.setIdleTimeout(600000); // 10 minutes

        this.dataSource = new HikariDataSource(config);
    }

    /**
     * Inserts a key-value pair into the database.
     *
     * @param key   The key of the value to be inserted.
     * @param value The value to be associated with the key.
     * @throws SQLException if a database access error occurs.
     */
    public void put(String key, String value) throws SQLException {
        String sql = "INSERT INTO key_value_store (`key`, `value`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `value` = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, key);
            statement.setString(2, value);
            statement.setString(3, value);
            statement.executeUpdate();
        }
    }

    /**
     * Retrieves a String value associated with the given key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the specified key, or null if no value is found.
     * @throws SQLException if a database access error occurs.
     */
    public String getString(String key) throws SQLException {
        String sql = "SELECT `value` FROM key_value_store WHERE `key` = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, key);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getString("value") : null;
        }
    }

    /**
     * Retrieves a Float value associated with the given key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The Float value associated with the specified key, or null if no value is found.
     * @throws SQLException if a database access error occurs.
     */
    public Float getFloat(String key) throws SQLException {
        String value = getString(key);
        return value != null ? Float.valueOf(value) : null;
    }

    /**
     * Retrieves a Boolean value associated with the given key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The Boolean value associated with the specified key, or null if no value is found.
     * @throws SQLException if a database access error occurs.
     */
    public Boolean getBoolean(String key) throws SQLException {
        String value = getString(key);
        return value != null ? Boolean.valueOf(value) : null;
    }

    /**
     * Retrieves a Long value associated with the given key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The Long value associated with the specified key, or null if no value is found.
     * @throws SQLException if a database access error occurs.
     */
    public Long getLong(String key) throws SQLException {
        String value = getString(key);
        return value != null ? Long.valueOf(value) : null;
    }

    /**
     * Retrieves a Double value associated with the given key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The Double value associated with the specified key, or null if no value is found.
     * @throws SQLException if a database access error occurs.
     */
    public Double getDouble(String key) throws SQLException {
        String value = getString(key);
        return value != null ? Double.valueOf(value) : null;
    }

    /**
     * Checks if the specified key exists in the database.
     *
     * @param key The key to check for existence.
     * @return true if the key exists, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean contains(String key) throws SQLException {
        return getString(key) != null;
    }

    /**
     * Deletes the key-value pair associated with the specified key.
     *
     * @param key The key to be deleted.
     * @throws SQLException if a database access error occurs.
     */
    public void delete(String key) throws SQLException {
        String sql = "DELETE FROM key_value_store WHERE `key` = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, key);
            statement.executeUpdate();
        }
    }

    /**
     * Retrieves a JSONObject associated with the given key.
     *
     * @param key The key whose associated JSONObject is to be returned.
     * @return The JSONObject associated with the specified key, or null if no value is found.
     * @throws SQLException if a database access error occurs.
     */
    public JSONObject getObject(String key) throws SQLException {
        String value = getString(key);
        return value != null ? new JSONObject(value) : null;
    }

    /**
     * Retrieves a JSONArray associated with the given key.
     *
     * @param key The key whose associated JSONArray is to be returned.
     * @return The JSONArray associated with the specified key, or null if no value is found.
     * @throws SQLException if a database access error occurs.
     */
    public JSONArray getArray(String key) throws SQLException {
        String value = getString(key);
        return value != null ? new JSONArray(value) : null;
    }

    /**
     * Closes the database connection pool when done.
     */
    public void close() {
        dataSource.close();
    }
}
