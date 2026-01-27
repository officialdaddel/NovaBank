package net.daddel.novaBank.utilities.misc;

import net.daddel.novaBank.commands.BankAdminCommand;
import net.daddel.novaBank.commands.BankCommand;
import net.daddel.novaBank.tabcompleter.BankAdminTabcompleter;
import net.daddel.novaBank.tabcompleter.BankTabcompleter;
import org.bukkit.plugin.java.JavaPlugin;

public class Initializer {
   public void registerCommands(JavaPlugin plugin) {
      plugin.getCommand("bank").setExecutor(new BankCommand());
      plugin.getCommand("bankadmin").setExecutor(new BankAdminCommand());
   }

   public void registerListener(JavaPlugin plugin) {
   }

   public void registerTabcompleter(JavaPlugin plugin) {
      plugin.getCommand("bank").setTabCompleter(new BankTabcompleter());
      plugin.getCommand("bankadmin").setTabCompleter(new BankAdminTabcompleter());
   }

   public void checkPlugins(JavaPlugin plugin) {
      if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
         (new Placeholder()).register();
      } else {
         plugin.getLogger().severe("Error: Could not find plugin PlaceholderAPI! This plugin is required!");
      }

      if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
         plugin.getLogger().severe("Error: Could not find plugin Vault! This plugin is required!");
      }

   }
}
