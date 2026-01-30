package net.daddel.novaBank.utilities.misc;

import net.daddel.novaBank.commands.BankAdminCommand;
import net.daddel.novaBank.commands.BankCommand;
import net.daddel.novaBank.gui.GuiListener;
import net.daddel.novaBank.gui.GuiManager;
import net.daddel.novaBank.gui.chat.ChatListener;
import net.daddel.novaBank.gui.chat.InputManager;
import net.daddel.novaBank.listener.JoinListener;
import net.daddel.novaBank.tabcompleter.BankAdminTabcompleter;
import net.daddel.novaBank.tabcompleter.BankTabcompleter;
import net.daddel.novaBank.utilities.Utilities;
import net.daddel.novaBank.utilities.manager.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Initializer {
   private GuiManager guiManager;
   private LanguageManager languageManager;
   private Utilities utilities;
   public Initializer(GuiManager guiManager, LanguageManager languageManager, Utilities utilities){
      this.languageManager = languageManager;
      this.guiManager = guiManager;
      this.utilities = utilities;
   }
   public void registerCommands(JavaPlugin plugin) {
      plugin.getCommand("bank").setExecutor(new BankCommand());
      plugin.getCommand("bankadmin").setExecutor(new BankAdminCommand());
   }

   public void registerListener(JavaPlugin plugin) {
      PluginManager plm = Bukkit.getPluginManager();
      plm.registerEvents(new ChatListener(guiManager, languageManager, utilities, plugin), plugin);
      plm.registerEvents(new GuiListener(languageManager), plugin);
      plm.registerEvents(new JoinListener(plugin, utilities), plugin);
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
