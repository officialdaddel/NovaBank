package net.daddel.novaBank.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.files.LanguageFile;
import net.daddel.novaBank.util.Utilities;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BankManager {
    private final LanguageFile languageFile;
    private final DatabaseManager databaseManager;
    private final EconomyManager economyManager;
    private final BankFileManager bankFileManager;
    private final Utilities utilities;

    public BankManager(LanguageFile languageFile, DatabaseManager databaseManager, EconomyManager economyManager,
                       BankFileManager bankFileManager, Utilities utilities) {
        this.languageFile = languageFile;
        this.databaseManager = databaseManager;
        this.economyManager = economyManager;
        this.bankFileManager = bankFileManager;
        this.utilities = utilities;
    }


    public void depositMoney(Player player, int amount) {
        UUID uuid = player.getUniqueId();

        int currentBalance = getBalance(uuid);

        if(!economyManager.hasEnoughMoney(player, amount)){
            utilities.notEnoughMoney(player);
            return;
        }

        int newBalance = currentBalance + amount;

        if (databaseManager.useDatabase()) {
            databaseManager.setAmount(uuid, newBalance);
        } else {
            bankFileManager.setAmount(uuid, newBalance);
        }

        economyManager.removeMoney(player, amount);

        utilities.sendPlaceholderMessage(player, languageFile.getString("deposit-money"));
        utilities.sendPlaceholderMessage(player, languageFile.getString("new-balance"));
    }

    public void withdrawMoney(Player player, int amount) {
        UUID uuid = player.getUniqueId();

        int currentBalance = getBalance(uuid);

        if(currentBalance < amount) {
            utilities.notEnoughMoney(player);
            return;
        }

        if (databaseManager.useDatabase()) {
            databaseManager.removeAmount(uuid, amount);
        } else {
            bankFileManager.removeAmount(uuid, amount);
        }

        economyManager.addMoney(player, amount);

        utilities.sendPlaceholderMessage(player, languageFile.getString("withdraw-money"));
        utilities.sendPlaceholderMessage(player, languageFile.getString("new-balance"));

    }

    public void resetBalance(Player player) {
        UUID uuid = player.getUniqueId();
        int amount = getBalance(uuid);

        if (databaseManager.useDatabase()) {
            databaseManager.removeAmount(uuid, amount);
        } else  {
            bankFileManager.removeAmount(uuid, amount);
        }

        utilities.sendPlaceholderMessage(player, languageFile.getString("reset-money-confirm"));
    }

    public void removeMoney(Player player, Player target, int amount) {
        UUID uuid = target.getUniqueId();

        if (databaseManager.useDatabase()) {
            databaseManager.removeAmount(uuid, amount);
        } else {
            bankFileManager.removeAmount(uuid, amount);
        }

        utilities.sendPlaceholderMessage(player, "remove-money-confirm");
    }

    public void addMoney(Player player, Player target, int amount) {
        UUID uuid = target.getUniqueId();

        int newBalance = amount + getBalance(uuid);

        if (databaseManager.useDatabase()) {
            databaseManager.setAmount(uuid, newBalance);
        } else {
            bankFileManager.setAmount(uuid, newBalance);
        }

        utilities.sendPlaceholderMessage(player, "add-money-confirm");
    }

    public void setMoney(Player player, Player target, int amount) {
        UUID uuid = target.getUniqueId();

        if (databaseManager.useDatabase()) {
            databaseManager.setAmount(uuid, amount);
        } else {
            bankFileManager.setAmount(uuid, amount);
        }


        utilities.sendPlaceholderMessage(player, "set-money-confirm");
    }

    public int getBalance(UUID uuid) {
        return (databaseManager.useDatabase() ? databaseManager.getAmount(uuid) : bankFileManager.getAmount(uuid));
    }
}
