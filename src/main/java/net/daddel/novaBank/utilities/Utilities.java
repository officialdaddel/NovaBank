package net.daddel.novaBank.utilities;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.utilities.manager.LanguageManager;
import net.daddel.novaBank.utilities.misc.Placeholder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Utilities {
   private final LanguageManager langMng = new LanguageManager();
   public String prefix = String.valueOf(ChatColor.of("#8A2BE2")) + "NovaBank §8»§7";
   public String perms = "novabank.";
   public String admin;

   public Utilities() {
      this.admin = this.perms + "*";
   }

   public String version(JavaPlugin plugin) {
      return plugin.getDescription().getVersion();
   }

   public void consoleError(CommandSender sender) {
      sender.sendMessage("Error: The console can't execute this command!");
   }

   public void sendColoredMessage(Player player, String message) {
      player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
   }

   public void noPerm(Player player) {
      String message = PlaceholderAPI.setPlaceholders(player, this.langMng.getMessage("no-permissions"));
      this.sendColoredMessage(player, message);
   }

   public void wrongUsage(Player player) {
      String message = PlaceholderAPI.setPlaceholders(player, this.langMng.getMessage("wrong-usage"));
      this.sendColoredMessage(player, message);
   }

   public boolean isInteger(String value) {
      try {
         Integer.parseInt(value);
         return true;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   public void playerNotFound(Player player, String target) {
      this.setInvalidPlayer(player, target);
      String pnf = PlaceholderAPI.setPlaceholders(player, this.langMng.getMessage("player-not-found"));
      this.sendColoredMessage(player, pnf);
   }

   public void setInvalidPlayer(Player player, String target) {
      Placeholder.invalidPlayer.put(player.getName(), target);
   }

   public void setTargetPlayer(Player player, String target) {
      Placeholder.targetPlayer.put(player.getName(), target);
   }

   public void setAddedAmount(Player player, int amount) {
      Placeholder.addedAmount.put(player.getName(), amount);
   }

   public void setRemovedAmount(Player player, int amount) {
      Placeholder.removedAmount.put(player.getName(), amount);
   }

   public void setSettingAmount(Player player, int amount) {
      Placeholder.settingAmount.put(player.getName(), amount);
   }
}
