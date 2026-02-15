package net.daddel.novaBank.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.files.ConfigFile;
import net.daddel.novaBank.files.LanguageFile;
import net.daddel.novaBank.gui.GuiManager;
import net.daddel.novaBank.manager.BankManager;
import net.daddel.novaBank.util.Placeholder;
import net.daddel.novaBank.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class BankCommand implements CommandExecutor {
    private final Utilities utilities;
    private final LanguageFile languageFile;
    private final BankManager bankManager;
    private Placeholder placeholder;
    private GuiManager guiManager;
    private ConfigFile configFile;

    public BankCommand(Utilities utilities, LanguageFile languageFile, BankManager bankManager, Placeholder placeholder, GuiManager guiManager, ConfigFile configFile) {
        this.utilities = utilities;
        this.languageFile = languageFile;
        this.bankManager = bankManager;
        this.placeholder = placeholder;
        this.guiManager = guiManager;
        this.configFile = configFile;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender,
                             @NonNull Command command,
                             @NonNull String s,
                             @NonNull String[] args) {
        if (!(sender instanceof Player)) {
            utilities.consoleError(sender);
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            handleDefaultCommand(player);
            return false;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "balance" -> handleBalanceCommand(player);
            case "deposit" -> handleDepositCommand(player, args);
            case "withdraw" -> handleWithdrawCommand(player, args);
            default -> utilities.wrongCommandUsage(player);
        }

        return false;
    }

    private void handleBalanceCommand(Player player){
        if (!hasBankPermission(player, "balance")) {
            placeholder.getPlayerPlaceholder().setMissingPermission(player, "novabank.balance");
            utilities.noPerms(player);
            return;
        }

        String message = PlaceholderAPI.setPlaceholders(player, languageFile.getString("current-balance"));
        utilities.sendColoredMessage(player, message);
    }

    private void handleDefaultCommand(Player player) {
        if (!hasBankPermission(player, "open")) {
            placeholder.getPlayerPlaceholder().setMissingPermission(player, "novabank.open");
            utilities.noPerms(player);
            return;
        }

        if (!isBankMenuEnabled()) {
            utilities.wrongCommandUsage(player);
            return;
        }

        guiManager.openBankInventory(player);
    }

    private void handleDepositCommand(Player player, String[] args) {
        if (args.length != 2) {
            utilities.wrongCommandUsage(player);
            return;
        }

        if (!hasBankPermission(player, "deposit")) {
            placeholder.getPlayerPlaceholder().setMissingPermission(player, "novabank.deposit");
            utilities.noPerms(player);
            return;
        }

        String amountString = args[1];

        if (amountString.equalsIgnoreCase("all")) {
            int currentBalance = bankManager.getEconomyBalance(player);

            placeholder.getMoneyPlaceholder().setDepositAmount(player, currentBalance);

            bankManager.depositMoney(player, currentBalance);
            return;
        }

        if (!utilities.isInteger(amountString)) {
            utilities.wrongCommandUsage(player);
            return;
        }

        int amount = Integer.parseInt(amountString);

        placeholder.getMoneyPlaceholder().setDepositAmount(player, amount);

        bankManager.depositMoney(player, amount);
    }

    private void handleWithdrawCommand(Player player, String[] args) {
        if (args.length != 2) {
            utilities.wrongCommandUsage(player);
            return;
        }

        if (!hasBankPermission(player, "withdraw")) {
            placeholder.getPlayerPlaceholder().setMissingPermission(player, "novabank.withdraw");
            utilities.noPerms(player);
            return;
        }

        String amountString = args[1];

        if (amountString.equalsIgnoreCase("all")) {
            int currentBalance = bankManager.getBankBalance(player.getUniqueId());

            placeholder.getMoneyPlaceholder().setWithdrawAmount(player, currentBalance);

            bankManager.withdrawMoney(player, currentBalance);
            return;
        }

        if (!utilities.isInteger(amountString)) {
            utilities.wrongCommandUsage(player);
            return;
        }

        int amount = Integer.parseInt(amountString);

        placeholder.getMoneyPlaceholder().setWithdrawAmount(player, amount);

        bankManager.withdrawMoney(player, amount);
    }

    private boolean hasBankPermission(Player player, String permissions) {
        return utilities.hasPermissions(player, permissions) || utilities.isAdmin(player);
    }

    private boolean isBankMenuEnabled() {
        return configFile.getBoolean("gui.enabled");
    }
}
