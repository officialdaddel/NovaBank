package net.daddel.novaBank;

import net.daddel.novaBank.files.configData;
import net.daddel.novaBank.files.data.bankData;
import net.daddel.novaBank.utilities.Utilities;
import net.daddel.novaBank.utilities.manager.DatabaseManager;
import net.daddel.novaBank.utilities.manager.EconomyManager;
import net.daddel.novaBank.utilities.manager.LanguageManager;
import net.daddel.novaBank.utilities.misc.Initializer;
import net.daddel.novaBank.utilities.misc.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
   public static Main plugin;
   public static configData configData;
   public static bankData bankData;
   private Utilities utilities;
   private DatabaseManager dbmng;

   public void onEnable() {
      plugin = this;
      configData = new configData(this);
      bankData = new bankData(this);
      LanguageManager langMng = new LanguageManager();
      langMng.registerLanguages(this);
      this.utilities = new Utilities();
      Initializer initializer = new Initializer();
      EconomyManager economyManager = new EconomyManager();
      initializer.registerCommands(this);
      initializer.registerListener(this);
      initializer.registerTabcompleter(this);
      initializer.checkPlugins(this);
      economyManager.setupEconomy(this);
      this.dbmng = new DatabaseManager();
      this.dbmng.setupDatabase(this);
      (new UpdateChecker(this, 116303)).getVersion((version) -> {
         if (this.getDescription().getVersion().equals(version)) {
            this.getLogger().info("There is not a new update available.");
         } else {
            this.getLogger().warning("There is a new update available.");
            this.getLogger().warning("Your version: " + this.getDescription().getVersion());
            this.getLogger().warning("Newest version: " + version);
            this.getLogger().warning("Please update the plugin to avoid errors!");
         }

      });
      this.getLogger().info("NovaBank version " + this.utilities.version(this) + " enabled");
   }

   public void onDisable() {
      this.getLogger().info("NovaBank version " + this.utilities.version(this) + " disabled");
      this.dbmng = new DatabaseManager();
      this.dbmng.disconnectDatabase();
   }

   public static Main getPlugin() {
      return plugin;
   }
}
