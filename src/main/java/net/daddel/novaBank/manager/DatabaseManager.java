package net.daddel.novaBank.manager;

import net.daddel.novaBank.files.ConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {

    private static Connection connection;
    private final ConfigFile configFile;
    private final JavaPlugin plugin;
    private int amount;

    // -- DATABASE PARAMS --
    private String host;
    private String database;
    private int port;
    private String username;
    private String password;

    public DatabaseManager(JavaPlugin plugin, ConfigFile configFile) {
        this.plugin = plugin;
        this.configFile = configFile;

        if (useDatabase()) {
            createParams();
            establishConnection();
            createPlayerDataTable();
        }
    }

    public void establishConnection() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database,
                    username,
                    password
            );
            plugin.getLogger().info("MySQL connection established");
        } catch (SQLException exception) {
            if (consoleLogging()) {
                plugin.getLogger().severe(
                        "Error: Could not establish MySQL connection: " + exception.getMessage()
                );
            }
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                if (consoleLogging()) {
                    plugin.getLogger().info("MySQL connection closed");
                }
            } catch (SQLException e) {
                if (consoleLogging()) {
                    plugin.getLogger().severe(
                            "Error: Could not close MySQL connection: " + e.getMessage()
                    );
                }
            }
        }
    }

    public void createPlayerDataTable() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS novabank_playerdata " +
                    "(uuid VARCHAR(36) PRIMARY KEY, amount INT)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            statement.close();

            if (consoleLogging()) {
                plugin.getLogger().info("Table 'novabank_playerdata' created or already exists");
            }
        } catch (SQLException exception) {
            if (consoleLogging()) {
                plugin.getLogger().severe(
                        "Error: Could not create table 'novabank_playerdata': " + exception.getMessage()
                );
            }
            exception.printStackTrace();
        }
    }

    public void setAmount(UUID uuid, int amount) {
        try {
            String query = "INSERT INTO novabank_playerdata (uuid, amount) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE amount = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            statement.setInt(2, amount);
            statement.setInt(3, amount);
            statement.executeUpdate();
            statement.close();

            if (consoleLogging()) {
                plugin.getLogger().info("Successfully updated player in 'novabank_playerdata'");
            }
        } catch (SQLException exception) {
            if (consoleLogging()) {
                plugin.getLogger().severe(
                        "Error: Could not update player in 'novabank_playerdata': " + exception.getMessage()
                );
            }
        }
    }

    public void removeAmount(UUID uuid, int amount) {
        int currentAmount = getAmount(uuid);
        int newAmount = currentAmount - amount;

        try {
            String query = "INSERT INTO novabank_playerdata (uuid, amount) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE amount = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            statement.setInt(2, newAmount);
            statement.setInt(3, newAmount);
            statement.executeUpdate();
            statement.close();

            if (consoleLogging()) {
                plugin.getLogger().info("Successfully updated player in 'novabank_playerdata'");
            }
        } catch (SQLException exception) {
            if (consoleLogging()) {
                plugin.getLogger().severe(
                        "Error: Could not update player in 'novabank_playerdata': " + exception.getMessage()
                );
            }
        }
    }

    public int getAmount(UUID uuid) {
        try {
            String query = "SELECT amount FROM novabank_playerdata WHERE uuid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                amount = result.getInt("amount");
            }
            statement.close();
        } catch (SQLException exception) {
            if (consoleLogging()) {
                plugin.getLogger().severe(
                        "Error: Could not check amount in 'novabank_playerdata': " + exception.getMessage()
                );
            }
        }
        return amount;
    }

    private void createParams() {
        host = configFile.getString("database.host");
        port = configFile.getInt("database.port");
        database = configFile.getString("database.database");
        username = configFile.getString("database.username");
        password = configFile.getString("database.password");
    }

    private boolean consoleLogging() {
        return configFile.getBoolean("console-logging");
    }

    public boolean useDatabase() {
        return configFile.getBoolean("database.use");
    }

    public Connection getConnection() {
        return connection;
    }
}