package otnose.tierra_gloriosa;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBconnector {
    private BlockingQueue<Connection> connectionPool;
    private ConfigManager cnf;
    private static final Logger LOGGER = Logger.getLogger(DBconnector.class.getName());


    public DBconnector(ConfigManager cnf, int poolSize) throws SQLException {
        this.cnf = cnf;
        this.connectionPool = new LinkedBlockingQueue<>(poolSize);
        initializeConnectionPool(poolSize);
    }

    private void initializeConnectionPool(int poolSize) throws SQLException {
        try {
            LOGGER.info("Con pool initialising");
            for (int i = 0; i < poolSize; i++) { // Используйте переданный размер пула
                Connection connection = createConnection();
                if (connection != null) {
                    connectionPool.add(connection);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error initializing connection pool: " + e.getMessage(), e);
            throw e;
        }
    }

    private Connection createConnection() throws SQLException {
        try {
            Class.forName(cnf.getDbDriver());
            return DriverManager.getConnection(
                    cnf.getDbUrl(),
                    cnf.getDbUsername(),
                    cnf.getDbPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() throws InterruptedException {
        LOGGER.log(Level.INFO, "Get pool connection. Pool size: "+connectionPool.size());
        return connectionPool.take();
    }

    public void releaseConnection(Connection connection) {
        connectionPool.offer(connection);
    }

    public void closeAllConnections() throws SQLException {
        for (Connection connection : connectionPool) {
            connection.close();
        }
    }

    public void createFactionTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS players_faction (" +
                "player_name VARCHAR(255) PRIMARY KEY," +
                "elo INT NOT NULL)";
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.execute(query);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    public void setPlayerFaction(String playerName, String factionName) throws SQLException {
        String query = "INSERT INTO players_faction (player_name, faction_name) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE faction_name = VALUES(faction_name)";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, playerName);
            statement.setString(2, factionName);
            statement.executeUpdate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                releaseConnection(connection);
            }
        }
    }

    public String getPlayerFaction(String playerName) throws SQLException {
        String query = "SELECT faction_name FROM players_faction WHERE player_name = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("faction_name");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                releaseConnection(connection);
            }
        }
        return "N/A";
    }

}
