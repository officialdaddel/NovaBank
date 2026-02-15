package net.daddel.novaBank.commands;

import net.daddel.novaBank.NovaBank;
import net.daddel.novaBank.manager.DatabaseManager;
import net.daddel.novaBank.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MigrateBankCommand implements CommandExecutor {

    private final NovaBank plugin;
    private final DatabaseManager databaseManager;
    private final Utilities utilities;

    public MigrateBankCommand(NovaBank plugin, DatabaseManager databaseManager, Utilities utilities) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.utilities = utilities;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (!utilities.isAdmin(player)) {
            utilities.noPerms(player);
            return true;
        }

        if (!databaseManager.useDatabase()) {
            utilities.sendColoredMessage(player, "§cYou're currently using files instead of database!");
            return true;
        }

        player.sendMessage(utilities.getPrefix() + " §eStarting migration...");

        Connection connection = databaseManager.getConnection();

        String sql =
                "INSERT IGNORE INTO novabank_playerdata (uuid, amount) " +
                        "SELECT uuid, amount FROM banksystem_playerdata";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int rows = ps.executeUpdate();
            player.sendMessage(utilities.getPrefix() + " §aMigration completed. §2" + rows + " entries moved...");
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            player.sendMessage(utilities.getPrefix() + " §cMigration failed: " + exception.getMessage());
        }
        return false;
    }
}