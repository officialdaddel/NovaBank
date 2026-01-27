package net.daddel.novaBank.utilities.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.utilities.Utilities;
import net.daddel.novaBank.utilities.misc.Placeholder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyManager {
   public static Economy economy = null;
   private final LanguageManager languageManager = new LanguageManager();
   private final Utilities utilities = new Utilities();

   public boolean setupEconomy(JavaPlugin plugin) {
      RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
      if (economyProvider != null) {
         economy = (Economy)economyProvider.getProvider();
      }

      return economy != null;
   }

   public void addMoney(OfflinePlayer player, double amount) {
      if (economy != null && !(amount <= 0.0D)) {
         economy.depositPlayer(player, amount);
      }
   }

   public void setMoney(OfflinePlayer player, double amount) {
      if (economy != null && !(amount < 0.0D)) {
         double current = economy.getBalance(player);
         economy.withdrawPlayer(player, current);
         economy.depositPlayer(player, amount);
      }
   }

   public void removeMoney(OfflinePlayer player, double amount) {
      if (economy != null && !(amount <= 0.0D)) {
         if (economy.has(player, amount)) {
            economy.withdrawPlayer(player, amount);
         }

      }
   }

   public double getMoney(OfflinePlayer player) {
      return economy == null ? 0.0D : economy.getBalance(player);
   }

   public boolean hasEnoughMoney(OfflinePlayer player, double amount) {
      return economy != null && !(amount < 0.0D) ? economy.has(player, amount) : false;
   }

   public void notEnoughMoney(Player player) {
      String msg = PlaceholderAPI.setPlaceholders(player, this.languageManager.getMessage("not-enough-money"));
      this.utilities.sendColoredMessage(player, msg);
   }

   public void invalidAmount(Player player) {
      String msg = PlaceholderAPI.setPlaceholders(player, this.languageManager.getMessage("invalid-amount"));
      this.utilities.sendColoredMessage(player, msg);
   }

   public void setDepositAmount(Player player, int amount) {
      Placeholder.lastDeposit.put(player.getName(), amount);
   }

   public void setWithdrawAmount(Player player, int amount) {
      Placeholder.lastWithdraw.put(player.getName(), amount);
   }
}
