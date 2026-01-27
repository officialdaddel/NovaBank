package net.daddel.novaBank.utilities.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import net.daddel.novaBank.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseManager {
   private static Connection connection;
   private int amount;
   private String host;
   private String password;
   private String database;
   private String username;
   private int port;
   private final FileConfiguration maincfg;

   public DatabaseManager() {
      this.maincfg = Main.configData.getConfig();
   }

   public void connectDB() {
      try {
         connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
         Main.getPlugin().getLogger().info("MySQL connection successfully connected!");
      } catch (SQLException var2) {
         Main.getPlugin().getLogger().severe("Error: Could not connect MySQL: " + var2.getMessage());
      }

   }

   public void disconnectDatabase() {
      if (connection != null) {
         try {
            connection.close();
            Main.getPlugin().getLogger().info("MySQL connection closed!");
         } catch (SQLException var2) {
            Main.getPlugin().getLogger().severe("Error while closing MySQL connection: " + var2.getMessage());
         }
      }

   }

   public void createBankSystemTable() {
      try {
         String query = "CREATE TABLE IF NOT EXISTS novabank_playerdata (uuid VARCHAR(36) PRIMARY KEY, amount INT)";
         PreparedStatement statement = connection.prepareStatement(query);
         statement.executeUpdate();
         Main.getPlugin().getLogger().info("Table 'novabank_playerdata' created or already exists!");
      } catch (SQLException var3) {
         Main.getPlugin().getLogger().info("Error while creating 'novabank_playerdata': " + var3.getMessage());
         var3.printStackTrace();
      }

   }

   public void setAmount(UUID uuid, int amount) {
      try {
         String query = "INSERT INTO novabank_playerdata (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?";
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         preparedStatement.setString(1, uuid.toString());
         preparedStatement.setInt(2, amount);
         preparedStatement.setInt(3, amount);
         preparedStatement.executeUpdate();
         Main.getPlugin().getLogger().info("Successfully added/updated player in 'novabank_playerdata'!");
      } catch (SQLException var5) {
         Main.getPlugin().getLogger().severe("Error while adding/updating player in 'novabank_playerdata': " + var5.getMessage());
      }

   }

   public void addAmount(UUID uuid, int amount) {
      String query = "INSERT INTO novabank_playerdata (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?";

      try {
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         preparedStatement.setString(1, uuid.toString());
         preparedStatement.setInt(2, amount);
         preparedStatement.setInt(3, amount);
         preparedStatement.executeUpdate();
      } catch (SQLException var5) {
         Main.getPlugin().getLogger().severe("Error while updating player in 'novabank_playerdata': " + var5.getMessage());
      }

   }

   public void removeAmount(UUID uuid, int amount) {
      int currentAmount = this.getAmount(uuid);
      int newAmount = currentAmount - amount;

      try {
         String query = "INSERT INTO novabank_playerdata (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?";
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         preparedStatement.setString(1, uuid.toString());
         preparedStatement.setInt(2, newAmount);
         preparedStatement.setInt(3, newAmount);
         preparedStatement.executeUpdate();
         Main.getPlugin().getLogger().info("Successfully updated player in 'novabank_playerdata'!");
      } catch (SQLException var7) {
         Main.getPlugin().getLogger().severe("Error while updating player in 'novabank_playerdata': " + var7.getMessage());
      }

   }

   public int getAmount(UUID uuid) {
      try {
         String query = "SELECT amount FROM novabank_playerdata WHERE uuid = ?";
         PreparedStatement statement = connection.prepareStatement(query);
         statement.setString(1, uuid.toString());
         ResultSet result = statement.executeQuery();
         if (result.next()) {
            this.amount = result.getInt("amount");
         }
      } catch (SQLException var5) {
         Main.getPlugin().getLogger().severe("Error while checking amount in 'novabank_playerdata': " + var5.getMessage());
      }

      return this.amount;
   }

   public void setupDatabase(JavaPlugin plugin) {
      if (this.useDatabase()) {
         plugin.getLogger().info("Database is enabled... Using database...");
         this.host = this.maincfg.getString("database.host");
         this.port = this.maincfg.getInt("database.port");
         this.database = this.maincfg.getString("database.database");
         this.username = this.maincfg.getString("database.username");
         this.password = this.maincfg.getString("database.password");
         this.connectDB();
         this.createBankSystemTable();
      } else {
         plugin.getLogger().warning("Database is disabled... Using configs...");
      }

   }

   public boolean useDatabase() {
      return this.maincfg.getBoolean("database.use");
   }
}
