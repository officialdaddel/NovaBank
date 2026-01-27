package net.daddel.novaBank.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.utilities.Utilities;
import net.daddel.novaBank.utilities.manager.ConfigManager;
import net.daddel.novaBank.utilities.manager.DatabaseManager;
import net.daddel.novaBank.utilities.manager.EconomyManager;
import net.daddel.novaBank.utilities.manager.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BankCommand implements CommandExecutor {
   private final Utilities utilities = new Utilities();
   private final LanguageManager lang = new LanguageManager();
   private final DatabaseManager databaseManager = new DatabaseManager();
   private final ConfigManager configManager = new ConfigManager();
   private final EconomyManager economy = new EconomyManager();

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         if (args.length == 1) {
            if (args[0].equalsIgnoreCase("balance")) {
               if (!player.hasPermission(this.utilities.perms + "balance") && !player.hasPermission(this.utilities.admin)) {
                  this.utilities.noPerm(player);
               } else {
                  String currentBalance = PlaceholderAPI.setPlaceholders(player, this.lang.getMessage("current-balance"));
                  this.utilities.sendColoredMessage(player, currentBalance);
               }
            } else {
               this.utilities.wrongUsage(player);
            }
         } else if (args.length == 2) {
            int currentAmount;
            String depositedMoney;
            int removingAmount;
            if (args[0].equalsIgnoreCase("deposit")) {
               if (this.utilities.isInteger(args[1])) {
                  if (!player.hasPermission(this.utilities.perms + "deposit") && !player.hasPermission(this.utilities.admin)) {
                     this.utilities.noPerm(player);
                  } else {
                     removingAmount = Integer.parseInt(args[1]);
                     if (this.databaseManager.useDatabase()) {
                        currentAmount = this.databaseManager.getAmount(player.getUniqueId());
                     } else {
                        currentAmount = this.configManager.getAmount(player);
                     }

                     int newAmount = currentAmount + removingAmount;
                     if (this.economy.hasEnoughMoney(player, removingAmount)) {
                        if (this.databaseManager.useDatabase()) {
                           this.databaseManager.addAmount(player.getUniqueId(), newAmount);
                        } else {
                           this.configManager.addAmount(player, newAmount);
                        }

                        this.economy.removeMoney(player, removingAmount);
                        this.economy.setDepositAmount(player, removingAmount);
                        depositedMoney = PlaceholderAPI.setPlaceholders(player, this.lang.getMessage("deposit-money"));
                        this.utilities.sendColoredMessage(player, depositedMoney);
                        String newBalance = PlaceholderAPI.setPlaceholders(player, this.lang.getMessage("new-balance"));
                        this.utilities.sendColoredMessage(player, newBalance);
                     } else {
                        this.economy.notEnoughMoney(player);
                     }
                  }
               } else {
                  this.economy.invalidAmount(player);
               }
            } else if (args[0].equalsIgnoreCase("withdraw")) {
               if (this.utilities.isInteger(args[1])) {
                  if (!player.hasPermission(this.utilities.perms + "withdraw") && !player.hasPermission(this.utilities.admin)) {
                     this.utilities.noPerm(player);
                  } else {
                     removingAmount = Integer.parseInt(args[1]);
                     if (this.databaseManager.useDatabase()) {
                        currentAmount = this.databaseManager.getAmount(player.getUniqueId());
                     } else {
                        currentAmount = this.configManager.getAmount(player);
                     }

                     if (currentAmount != 0 && currentAmount >= removingAmount) {
                        if (this.databaseManager.useDatabase()) {
                           this.databaseManager.removeAmount(player.getUniqueId(), removingAmount);
                        } else {
                           this.configManager.removeAmount(player, removingAmount);
                        }

                        this.economy.addMoney(player, removingAmount);
                        this.economy.setWithdrawAmount(player, removingAmount);
                        String msg = PlaceholderAPI.setPlaceholders(player, this.lang.getMessage("withdraw-money"));
                        this.utilities.sendColoredMessage(player, msg);
                        depositedMoney = PlaceholderAPI.setPlaceholders(player, this.lang.getMessage("new-balance"));
                        this.utilities.sendColoredMessage(player, depositedMoney);
                     } else {
                        this.economy.notEnoughMoney(player);
                     }
                  }
               } else {
                  this.economy.invalidAmount(player);
               }
            } else {
               this.utilities.wrongUsage(player);
            }
         } else {
            this.utilities.wrongUsage(player);
         }
      } else {
         this.utilities.consoleError(sender);
      }

      return false;
   }
}
