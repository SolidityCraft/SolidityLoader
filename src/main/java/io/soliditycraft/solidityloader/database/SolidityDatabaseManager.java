package io.soliditycraft.solidityloader.database;

/**
 * A manager class for creating and managing database instances.
 * <p>This class provides methods to create instances of SolidityDatabase and SolidityMapDatabase.</p>
 */
public class SolidityDatabaseManager {

    /**
     * Creates and returns a new instance of SolidityDatabase with the specified connection details.
     *
     * @param ip       The IP address of the database server.
     * @param port     The port number on which the database is running.
     * @param username The username for connecting to the database.
     * @param password The password for connecting to the database.
     * @param database The name of the database to connect to.
     * @return A new instance of SolidityDatabase.
     */
    public SolidityDatabase createDatabase(String ip, int port, String username, String password, String database) {
        String jdbcUrl = createJdbcUrl(ip, port, database); // Get JDBC URL
        return new SolidityDatabase(jdbcUrl, username, password);
    }

    /**
     * Creates and returns a new instance of SolidityMapDatabase with the specified connection details.
     *
     * @param ip       The IP address of the database server.
     * @param port     The port number on which the database is running.
     * @param username The username for connecting to the database.
     * @param password The password for connecting to the database.
     * @param database The name of the database to connect to.
     * @return A new instance of SolidityMapDatabase.
     */
    public SolidityMapDatabase createMapDatabase(String ip, int port, String username, String password, String database) {
        String mapDatabaseFilePath = String.format("%s:%d/%s_map_database.json", ip, port, database);
        return new SolidityMapDatabase(mapDatabaseFilePath, username, password);
    }

    /**
     * Constructs a JDBC URL based on the provided database parameters.
     *
     * @param ip       The IP address of the database server.
     * @param port     The port number on which the database is running.
     * @param database The name of the database to connect to.
     * @return A JDBC URL formatted string.
     */
    private String createJdbcUrl(String ip, int port, String database) {
        return String.format("jdbc:mysql://%s:%d/%s", ip, port, database); // MySQL example
        // Modify the JDBC URL format for different databases if necessary
    }
}
