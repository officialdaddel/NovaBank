package net.daddel.novaBank.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.daddel.novaBank.manager.BankManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Placeholder extends PlaceholderExpansion {
    private final Utilities utilities;
    private final BankManager bankManager;
    private final MoneyPlaceholder moneyPlaceholder;
    private final PlayerPlaceholder playerPlaceholder;

    public Placeholder(Utilities utilities, BankManager bankManager) {
        this.utilities = utilities;
        this.bankManager = bankManager;
        this.moneyPlaceholder = new MoneyPlaceholder();
        this.playerPlaceholder = new PlayerPlaceholder();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "novabank";
    }

    @Override
    public @NotNull String getAuthor() {
        return utilities.getAuthor();
    }

    @Override
    public @NotNull String getVersion() {
        return utilities.getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        return switch (identifier.toLowerCase()) {
            case "prefix" -> utilities.getPrefix();
            case "missing_permission" -> playerPlaceholder.getMissingPermission(player);

            case "balance" -> String.valueOf(bankManager.getBankBalance(player.getUniqueId()));
            case "formatted_balance" -> utilities.formatString(bankManager.getBankBalance(player.getUniqueId()));

            case "default_commands" -> utilities.getDefaultCommands();
            case "default_admin_commands" -> utilities.getDefaultAdminCommands();

            case "deposit_amount" -> String.valueOf(moneyPlaceholder.getDepositAmount(player));
            case "withdraw_amount" -> String.valueOf(moneyPlaceholder.getWithdrawAmount(player));

            case "invalid_player" -> playerPlaceholder.getInvalidPlayer(player);
            case "target" -> playerPlaceholder.getTargetPlayer(player);
            case "player_name" -> player.getName();

            case "removed_amount" ->  String.valueOf(moneyPlaceholder.getRemovedAmount(player));
            case "added_amount" ->  String.valueOf(moneyPlaceholder.getAddedAmount(player));
            case "setting_amount" -> String.valueOf(moneyPlaceholder.getStatutoryAmount(player));

            default -> null;
        };
    }

    public static class MoneyPlaceholder {
        private final Map<UUID, Integer> depositAmount = new HashMap<>();
        private final Map<UUID, Integer> withdrawAmount = new HashMap<>();

        private final Map<UUID, Integer> removedAmount = new HashMap<>();
        private final Map<UUID, Integer> addedAmount = new HashMap<>();
        private final Map<UUID, Integer> statutoryAmount = new HashMap<>();

        public void setDepositAmount(Player player, int amount) {
            depositAmount.put(player.getUniqueId(), amount);
        }

        public void setWithdrawAmount(Player player, int amount) {
            withdrawAmount.put(player.getUniqueId(), amount);
        }

        public void setRemovedAmount(Player player, int amount) {
            removedAmount.put(player.getUniqueId(), amount);
        }

        public void setAddedAmount(Player player, int amount) {
            addedAmount.put(player.getUniqueId(), amount);
        }

        public void setStatutoryAmount(Player player, int amount) {
            statutoryAmount.put(player.getUniqueId(), amount);
        }

        private int getDepositAmount(Player player) {
            return depositAmount.getOrDefault(player.getUniqueId(), 0);
        }

        private int getWithdrawAmount(Player player) {
            return withdrawAmount.getOrDefault(player.getUniqueId(), 0);
        }

        private int getRemovedAmount(Player player) {
            return removedAmount.getOrDefault(player.getUniqueId(), 0);
        }

        private int getAddedAmount(Player player) {
            return addedAmount.getOrDefault(player.getUniqueId(), 0);
        }

        private int getStatutoryAmount(Player player) {
            return statutoryAmount.getOrDefault(player.getUniqueId(), 0);
        }
    }

    public static class PlayerPlaceholder {
        private final Map<UUID, String> invalidPlayer = new HashMap<>();
        private final Map<UUID, String> targetPlayer = new HashMap<>();
        private final Map<UUID, String> missingPermission = new HashMap<>();

        public void setInvalidPlayer(Player player, String playerName) {
            invalidPlayer.put(player.getUniqueId(), playerName);
        }

        public void setTargetPlayer(Player player, String playerName) {
            targetPlayer.put(player.getUniqueId(), playerName);
        }

        public void setMissingPermission(Player player, String permission) {
            missingPermission.put(player.getUniqueId(), permission);
        }

        private String getInvalidPlayer(Player player) {
            return invalidPlayer.getOrDefault(player.getUniqueId(), null);
        }

        private String getTargetPlayer(Player player) {
            return targetPlayer.getOrDefault(player.getUniqueId(), null);
        }

        private String getMissingPermission(Player player) {
            return missingPermission.getOrDefault(player.getUniqueId(), null);
        }

    }

    public MoneyPlaceholder getMoneyPlaceholder() {
        return moneyPlaceholder;
    }

    public PlayerPlaceholder getPlayerPlaceholder() {
        return playerPlaceholder;
    }
}
