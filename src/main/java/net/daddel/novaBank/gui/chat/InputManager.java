package net.daddel.novaBank.gui.chat;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;

public class InputManager {
    private static final Set<Player> deposit = new HashSet<>();
    private static final Set<Player> withdraw = new HashSet<>();

    public static void addPlayerDeposit(Player player) {
        deposit.add(player);
    }

    public static void removePlayerDeposit(Player player) {
        deposit.remove(player);
    }

    public static boolean isInDeposit(Player player) {
        return deposit.contains(player);
    }

    public static void addPlayerWithdraw(Player player) {
        withdraw.add(player);
    }

    public static void removePlayerWithdraw(Player player) {
        withdraw.remove(player);
    }

    public static boolean isInWithdraw(Player player) {
        return withdraw.contains(player);
    }
}

