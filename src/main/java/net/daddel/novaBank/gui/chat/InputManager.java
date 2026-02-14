package net.daddel.novaBank.gui.chat;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class InputManager {
    private final Set<Player> deposit = new HashSet<>();
    private final Set<Player> withdraw = new HashSet<>();

    public void addPlayerDeposit(Player player) {
        deposit.add(player);
    }

    public void addPlayerWithdraw(Player player) {
        withdraw.add(player);
    }

    public boolean isInAnySet(Player player) {
        return deposit.contains(player) || withdraw.contains(player);
    }

    public void removeFromSet(Player player) {
        if (isInWithdrawSet(player)) {
            withdraw.remove(player);
        }  else {
            deposit.remove(player);
        }
    }

    public boolean isInDepositSet(Player player) {
        return deposit.contains(player);
    }
    public boolean isInWithdrawSet(Player player) {
        return withdraw.contains(player);
    }
}
