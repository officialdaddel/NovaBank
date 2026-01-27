package net.daddel.novaBank.files.languages;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import net.daddel.novaBank.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class lang_nlData {
   private Main plugin;
   private FileConfiguration dataConfig = null;
   private File configFile = null;
   private String fileName = "languages/nl.yml";

   public lang_nlData(Main plugin) {
      this.plugin = plugin;
      this.saveDefaultConfig();
   }

   public void reloadConfig() {
      if (this.configFile == null) {
         this.configFile = new File(this.plugin.getDataFolder(), this.fileName);
      }

      this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
      InputStream defaultStream = this.plugin.getResource(this.fileName);
      if (defaultStream != null) {
         YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
         this.dataConfig.setDefaults(defaultConfig);
      }

   }

   public FileConfiguration getConfig() {
      if (this.dataConfig == null) {
         this.reloadConfig();
      }

      return this.dataConfig;
   }

   public void saveConfig() {
      if (this.dataConfig != null && this.configFile != null) {
         try {
            this.getConfig().save(this.configFile);
         } catch (IOException var2) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + String.valueOf(this.configFile), var2);
         }

      }
   }

   public void saveDefaultConfig() {
      if (this.configFile == null) {
         this.configFile = new File(this.plugin.getDataFolder(), this.fileName);
      }

      if (!this.configFile.exists()) {
         this.plugin.saveResource(this.fileName, false);
      }

   }
}
