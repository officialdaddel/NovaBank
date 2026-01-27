package net.daddel.novaBank.utilities.manager;

import net.daddel.novaBank.Main;
import net.daddel.novaBank.files.languages.lang_deData;
import net.daddel.novaBank.files.languages.lang_enData;
import net.daddel.novaBank.files.languages.lang_nlData;
import org.bukkit.configuration.file.FileConfiguration;

public class LanguageManager {
   private final FileConfiguration cfg;
   public static lang_deData lang_deData;
   public static lang_enData lang_enData;
   public static lang_nlData lang_nlData;

   public LanguageManager() {
      this.cfg = Main.configData.getConfig();
   }

   public void registerLanguages(Main plugin) {
      lang_enData = new lang_enData(plugin);
      lang_deData = new lang_deData(plugin);
      lang_nlData = new lang_nlData(plugin);
      plugin.getLogger().info("All available languages registered...");
      if (this.isValidLanguage()) {
         plugin.getLogger().info("Using locale " + String.valueOf(this.cfg.get("language")));
      } else {
         plugin.getLogger().severe("Invalid locale! Please change 'language' in config.yml");
      }

   }

   public String getMessage(String path) {
      if (Main.configData.getConfig().getString("language").equalsIgnoreCase("de")) {
         return lang_deData.getConfig().getString(path);
      } else if (Main.configData.getConfig().getString("language").equalsIgnoreCase("en")) {
         return lang_enData.getConfig().getString(path);
      } else {
         return Main.configData.getConfig().getString("language").equalsIgnoreCase("nl") ? lang_nlData.getConfig().getString(path) : lang_enData.getConfig().getString(path);
      }
   }

   private boolean isValidLanguage() {
      return this.cfg.getString("language").equalsIgnoreCase("de") || this.cfg.getString("language").equalsIgnoreCase("en") || this.cfg.getString("language").equalsIgnoreCase("nl");
   }
}
