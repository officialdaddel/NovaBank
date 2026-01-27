package net.daddel.novaBank.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.Main;
import net.daddel.novaBank.utilities.Utilities;
import net.daddel.novaBank.utilities.manager.ConfigManager;
import net.daddel.novaBank.utilities.manager.DatabaseManager;
import net.daddel.novaBank.utilities.manager.EconomyManager;
import net.daddel.novaBank.utilities.manager.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BankAdminCommand implements CommandExecutor {
   private final Utilities utilities = new Utilities();
   private final DatabaseManager databaseManager = new DatabaseManager();
   private final ConfigManager configManager = new ConfigManager();
   private final LanguageManager lang = new LanguageManager();
   private final EconomyManager economy = new EconomyManager();

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
               if (!player.hasPermission(this.utilities.perms + "admin.reload") && !player.hasPermission(this.utilities.admin)) {
                  this.utilities.noPerm(player);
               } else {
                  Main.configData.reloadConfig();
                  Main.bankData.reloadConfig();
                  this.reloadLangConfig();
                  player.sendMessage("§7");
                  player.sendMessage("§eYou've successfully reloaded follow configurations:");
                  player.sendMessage(" §8» §aconfig.yml");
                  player.sendMessage(" §8» §7/data/§abank.yml");
                  FileConfiguration var10001 = Main.configData.getConfig();
                  player.sendMessage(" §8» §7/languages/§a" + var10001.getString("language") + ".yml");
                  player.sendMessage("§c§oIf the changes have not been applied, please restart the server!");
                  player.sendMessage("§7");
               }
            } else {
               player.sendMessage(this.utilities.prefix + "§c/ba §7<§creload§7|§cset§7|§cadd§7|§cremove§7|§creset§7>");
            }
         } else {
            Player target;
            int amount;
            String setMessage;
            if (args.length == 2) {
               if (args[0].equalsIgnoreCase("reset")) {
                  target = Bukkit.getPlayer(args[1]);
                  if (target != null) {
                     if (!player.hasPermission(this.utilities.perms + "admin.reset") && !player.hasPermission(this.utilities.admin)) {
                        this.utilities.noPerm(player);
                     } else {
                        if (this.databaseManager.useDatabase()) {
                           amount = this.databaseManager.getAmount(target.getUniqueId());
                           this.databaseManager.removeAmount(target.getUniqueId(), amount);
                        } else {
                           this.configManager.resetMoney(target);
                        }

                        this.utilities.setTargetPlayer(player, target.getName());
                        setMessage = PlaceholderAPI.setPlaceholders(player, this.lang.getMessage("reset-money-confirm"));
                        this.utilities.sendColoredMessage(player, setMessage);
                     }
                  } else {
                     this.utilities.playerNotFound(player, args[1]);
                  }
               } else {
                  player.sendMessage(this.utilities.prefix + "§c/ba §7<§creload§7|§cset§7|§cadd§7|§cremove§7|§creset§7>");
               }
            } else if (args.length == 3) {
               int currentAmount;
               if (args[0].equalsIgnoreCase("remove")) {
                  target = Bukkit.getPlayer(args[1]);
                  if (target != null) {
                     if (this.utilities.isInteger(args[2])) {
                        if (!player.hasPermission(this.utilities.perms + "admin.remove") && !player.hasPermission(this.utilities.admin)) {
                           this.utilities.noPerm(player);
                        } else {
                           amount = Integer.parseInt(args[2]);
                           if (this.databaseManager.useDatabase()) {
                              currentAmount = this.databaseManager.getAmount(target.getUniqueId());
                           } else {
                              currentAmount = this.configManager.getAmount(target);
                           }

                           int newAmount = currentAmount - amount;
                           if (newAmount >= currentAmount && newAmount != 0) {
                              this.economy.invalidAmount(player);
                           } else if (this.databaseManager.useDatabase()) {
                              this.databaseManager.setAmount(target.getUniqueId(), newAmount);
                           } else {
                              this.configManager.setAmount(target, newAmount);
                           }

                           this.utilities.setRemovedAmount(player, amount);
                           String removeMessage = PlaceholderAPI.setPlaceholders(player, this.lang.getMessage("remove-money-confirm"));
                           this.utilities.sendColoredMessage(player, removeMessage);
                        }
                     } else {
                        this.economy.invalidAmount(player);
                     }
                  } else {
                     this.utilities.playerNotFound(player, args[1]);
                  }
               } else if (args[0].equalsIgnoreCase("add")) {
                  target = Bukkit.getPlayer(args[1]);
                  if (target != null) {
                     if (this.utilities.isInteger(args[2])) {
                        if (!player.hasPermission(this.utilities.perms + "admin.add") && !player.hasPermission(this.utilities.admin)) {
                           this.utilities.noPerm(player);
                        } else {
                           amount = Integer.parseInt(args[2]);
                           if (this.databaseManager.useDatabase()) {
                              currentAmount = this.databaseManager.getAmount(target.getUniqueId());
                              this.databaseManager.setAmount(target.getUniqueId(), amount + currentAmount);
                           } else {
                              currentAmount = this.configManager.getAmount(player);
                              this.configManager.setAmount(target, currentAmount + amount);
                           }

                           this.utilities.setTargetPlayer(player, target.getName());
                           this.utilities.setAddedAmount(player, amount);
                           String addingMessage = PlaceholderAPI.setPlaceholders(player, this.lang.getMessage("add-money-confirm"));
                           this.utilities.sendColoredMessage(player, addingMessage);
                        }
                     } else {
                        this.economy.invalidAmount(player);
                     }
                  } else {
                     this.utilities.playerNotFound(player, args[1]);
                  }
               } else if (args[0].equalsIgnoreCase("set")) {
                  target = Bukkit.getPlayer(args[1]);
                  if (target != null) {
                     if (this.utilities.isInteger(args[2])) {
                        if (!player.hasPermission(this.utilities.perms + "admin.set") && !player.hasPermission(this.utilities.admin)) {
                           this.utilities.noPerm(player);
                        } else {
                           amount = Integer.parseInt(args[2]);
                           if (this.databaseManager.useDatabase()) {
                              this.databaseManager.setAmount(target.getUniqueId(), amount);
                           } else {
                              this.configManager.setAmount(target, amount);
                           }

                           this.utilities.setTargetPlayer(player, target.getName());
                           this.utilities.setSettingAmount(player, amount);
                           setMessage = PlaceholderAPI.setPlaceholders(player, this.lang.getMessage("set-money-confirm"));
                           this.utilities.sendColoredMessage(player, setMessage);
                        }
                     } else {
                        this.economy.invalidAmount(player);
                     }
                  } else {
                     this.utilities.playerNotFound(player, args[1]);
                  }
               } else {
                  player.sendMessage(this.utilities.prefix + "§c/ba §7<§creload§7|§cset§7|§cadd§7|§cremove§7|§creset§7>");
               }
            } else {
               player.sendMessage(this.utilities.prefix + "§c/ba §7<§creload§7|§cset§7|§cadd§7|§cremove§7|§creset§7>");
            }
         }
      } else {
         this.utilities.consoleError(sender);
      }

      return false;
   }

   private void reloadLangConfig() {
      if (Main.configData.getConfig().getString("language").equalsIgnoreCase("de")) {
         LanguageManager.lang_deData.reloadConfig();
      } else if (Main.configData.getConfig().getString("language").equalsIgnoreCase("nl")) {
         LanguageManager.lang_nlData.reloadConfig();
      } else if (Main.configData.getConfig().getString("language").equalsIgnoreCase("en")) {
         LanguageManager.lang_enData.reloadConfig();
      }

   }
}
