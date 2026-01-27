package net.daddel.novaBank.utilities.manager;

import net.daddel.novaBank.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigManager {
   private final FileConfiguration cfg;

   public ConfigManager() {
      this.cfg = Main.bankData.getConfig();
   }

   public void setAmount(OfflinePlayer player, int amount) {
      this.cfg.set(player.getUniqueId().toString(), amount);
      Main.bankData.saveConfig();
   }

   public void addAmount(OfflinePlayer player, int newAmount) {
      this.cfg.set(player.getUniqueId().toString(), newAmount);
      Main.bankData.saveConfig();
   }

   public void removeAmount(OfflinePlayer player, int amount) {
      int oldAmount = this.cfg.getInt(player.getUniqueId().toString());
      int newAmount = oldAmount - amount;
      this.cfg.set(player.getUniqueId().toString(), newAmount);
      Main.bankData.saveConfig();
   }

   public int getAmount(OfflinePlayer player) {
      return this.cfg.contains(player.getUniqueId().toString()) ? this.cfg.getInt(player.getUniqueId().toString()) : 0;
   }

   public void resetMoney(OfflinePlayer player) {
      this.cfg.set(player.getUniqueId().toString(), 0);
      Main.bankData.saveConfig();
   }
}
