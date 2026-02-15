package net.daddel.novaBank.commands;

import net.daddel.novaBank.NovaBank;
import net.daddel.novaBank.files.LanguageFile;
import net.daddel.novaBank.manager.BankManager;
import net.daddel.novaBank.util.Placeholder;
import net.daddel.novaBank.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class BankAdminCommand implements CommandExecutor {
    private final NovaBank novaBank;
    private final Utilities utilities;
    private final LanguageFile languageFile;
    private final BankManager bankManager;
    private final Placeholder placeholder;

    public BankAdminCommand(NovaBank novaBank, Utilities utilities, LanguageFile languageFile, BankManager bankManager, Placeholder placeholder) {
        this.novaBank = novaBank;
        this.utilities = utilities;
        this.languageFile = languageFile;
        this.bankManager = bankManager;
        this.placeholder = placeholder;
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
            utilities.wrongAdminCommandUsage(player);
            return false;
        }

        String subCommand = args[0];

        switch (subCommand) {
            case "reload" -> handleReloadCommand(player, args);
            case "reset" -> handleResetCommand(player, args);
            case "remove" -> handleRemoveCommand(player, args);
            case "add" -> handleAddCommand(player, args);
            case "set" -> handleSetCommand(player, args);
        }

        return false;
    }

    private void handleReloadCommand(Player player, String[] args) {
        if (args.length != 1) {
            utilities.wrongAdminCommandUsage(player);
            return;
        }

        if (!hasPermission(player, "reload")) {
            placeholder.getPlayerPlaceholder().setMissingPermission(player, "novabank.admin.reload");
            utilities.noPerms(player);
            return;
        }

        novaBank.reloadAllConfigs();
        sendReloadMessages(player);
    }

    private void handleResetCommand(Player player, String[] args) {
        if (args.length != 2) {
            utilities.wrongAdminCommandUsage(player);
            return;
        }

        if(!hasPermission(player, "reset")) {
            placeholder.getPlayerPlaceholder().setMissingPermission(player, "novabank.admin.reset");
            utilities.noPerms(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            placeholder.getPlayerPlaceholder().setInvalidPlayer(player, args[1]);
            utilities.playerNotFound(player);
            return;
        }

        placeholder.getPlayerPlaceholder().setTargetPlayer(player, target.getName());
        bankManager.resetBalance(target);
    }

    private void handleRemoveCommand(Player player, String[] args) {
        if (args.length != 3) {
            utilities.wrongAdminCommandUsage(player);
            return;
        }

        if (!hasPermission(player, "remove")) {
            placeholder.getPlayerPlaceholder().setMissingPermission(player, "novabank.admin.remove");
            utilities.noPerms(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            placeholder.getPlayerPlaceholder().setInvalidPlayer(player, args[1]);
            utilities.playerNotFound(player);
            return;
        }

        String amount = args[2];

        if (!utilities.isInteger(amount)) {
            utilities.invalidAmount(player);
            return;
        }

        int removingAmount = Integer.parseInt(amount);

        placeholder.getMoneyPlaceholder().setRemovedAmount(player, removingAmount);

        placeholder.getPlayerPlaceholder().setTargetPlayer(player, target.getName());
        bankManager.removeMoney(player, target, removingAmount);
    }

    private void handleAddCommand(Player player, String[] args) {
        if (args.length != 3) {
            utilities.wrongAdminCommandUsage(player);
            return;
        }

        if (!hasPermission(player, "add")) {
            placeholder.getPlayerPlaceholder().setMissingPermission(player, "novabank.admin.add");
            utilities.noPerms(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            placeholder.getPlayerPlaceholder().setInvalidPlayer(player, args[1]);
            utilities.playerNotFound(player);
            return;
        }

        String amount = args[2];

        if (!utilities.isInteger(amount)) {
            utilities.invalidAmount(player);
            return;
        }

        int addedAmount = Integer.parseInt(amount);

        placeholder.getMoneyPlaceholder().setAddedAmount(player, addedAmount);

        placeholder.getPlayerPlaceholder().setTargetPlayer(player, target.getName());
        bankManager.addMoney(player, target, addedAmount);
    }

    private void handleSetCommand(Player player, String[] args) {
        if (args.length != 3) {
            utilities.wrongAdminCommandUsage(player);
            return;
        }

        if (!hasPermission(player, "set")) {
            placeholder.getPlayerPlaceholder().setMissingPermission(player, "novabank.admin.set");
            utilities.noPerms(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            placeholder.getPlayerPlaceholder().setInvalidPlayer(player, args[1]);
            utilities.playerNotFound(player);
            return;
        }

        String amount = args[2];

        if (!utilities.isInteger(amount)) {
            utilities.invalidAmount(player);
            return;
        }

        int removingAmount = Integer.parseInt(amount);

        placeholder.getMoneyPlaceholder().setStatutoryAmount(player, removingAmount);

        placeholder.getPlayerPlaceholder().setTargetPlayer(player, target.getName());
        bankManager.setMoney(player, target, removingAmount);
    }

    private boolean hasPermission(Player player, String permission) {
        return utilities.hasPermissions(player, "admin." + permission) || utilities.isAdmin(player);
    }

    private void sendReloadMessages(Player player) {
        String language = languageFile.getCurrentLanguage();

        player.sendMessage("§7");
        player.sendMessage("§eYou've successfully reloaded the following configurations:");
        player.sendMessage(" §8» §aconfig.yml");
        player.sendMessage(" §8» §7/data/§abank.yml");
        player.sendMessage(" §8» §7/languages/§a" + language + ".yml");
        player.sendMessage("§c§oIf the changes have not been applied, please restart the server!");
        player.sendMessage("§7");
    }
}
