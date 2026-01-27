package net.daddel.novaBank.utilities.misc;

import java.util.HashMap;
import java.util.Map;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.daddel.novaBank.Main;
import net.daddel.novaBank.utilities.Utilities;
import net.daddel.novaBank.utilities.manager.ConfigManager;
import net.daddel.novaBank.utilities.manager.DatabaseManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholder extends PlaceholderExpansion {
   public static final Map<String, Integer> lastDeposit = new HashMap();
   public static final Map<String, Integer> lastWithdraw = new HashMap();
   public static final Map<String, String> invalidPlayer = new HashMap();
   public static final Map<String, String> targetPlayer = new HashMap();
   public static final Map<String, Integer> addedAmount = new HashMap();
   public static final Map<String, Integer> removedAmount = new HashMap();
   public static final Map<String, Integer> settingAmount = new HashMap();
   private final Utilities utilities = new Utilities();
   private final DatabaseManager dbM = new DatabaseManager();
   private final ConfigManager cfgM = new ConfigManager();

   @NotNull
   public String getIdentifier() {
      return "novabank";
   }

   @NotNull
   public String getAuthor() {
      return "Daddel";
   }

   @NotNull
   public String getVersion() {
      return this.utilities.version(Main.getPlugin());
   }

   public String onPlaceholderRequest(OfflinePlayer player, String identifier) {
      if (identifier.equalsIgnoreCase("balance")) {
         return this.dbM.useDatabase() ? String.valueOf(this.dbM.getAmount(player.getUniqueId())) : String.valueOf(this.cfgM.getAmount(player));
      } else if (identifier.equalsIgnoreCase("prefix")) {
         return this.utilities.prefix;
      } else if (identifier.equalsIgnoreCase("default_commands")) {
         return "§6/bank §7<§6deposit§7|§6withdraw§7|§6balance§7> §7[§6amount§7]";
      } else if (identifier.equalsIgnoreCase("deposit_amount")) {
         return String.valueOf(lastDeposit.getOrDefault(player.getName(), 0));
      } else if (identifier.equalsIgnoreCase("withdraw_amount")) {
         return String.valueOf(lastWithdraw.getOrDefault(player.getName(), 0));
      } else if (identifier.equalsIgnoreCase("invalid_player")) {
         return String.valueOf(invalidPlayer.getOrDefault(player.getName(), "XX"));
      } else if (identifier.equalsIgnoreCase("target")) {
         return String.valueOf(targetPlayer.getOrDefault(player.getName(), "XX"));
      } else if (identifier.equalsIgnoreCase("removed_amount")) {
         return String.valueOf(removedAmount.getOrDefault(player.getName(), 0));
      } else if (identifier.equalsIgnoreCase("added_amount")) {
         return String.valueOf(addedAmount.getOrDefault(player.getName(), 0));
      } else {
         return identifier.equalsIgnoreCase("setting_amount") ? String.valueOf(settingAmount.getOrDefault(player.getName(), 0)) : null;
      }
   }

}
