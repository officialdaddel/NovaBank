package net.daddel.novaBank.gui.chat;

import net.daddel.novaBank.gui.GuiManager;
import net.daddel.novaBank.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatListener implements Listener {

    private final JavaPlugin plugin;
    private final Utilities utilities;
    private final InputManager inputManager;

    public ChatListener(JavaPlugin plugin, Utilities utilities, InputManager inputManager) {
        this.plugin = plugin;
        this.utilities = utilities;
        this.inputManager = inputManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        if (!inputManager.isInAnySet(player)) {
            return;
        }

        event.setCancelled(true);
        String input = event.getMessage();

        if (input.equalsIgnoreCase("cancel")) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                inputManager.removeFromSet(player);
                utilities.sendPlaceholderMessage(player, "bank-menu.cancel-confirm");
            });
            return;
        }

        if (!utilities.isInteger(input)) {
            Bukkit.getScheduler().runTask(plugin, () ->
                    utilities.sendPlaceholderMessage(player, "invalid-amount")
            );
            return;
        }

        int amount = Integer.parseInt(input);

        Bukkit.getScheduler().runTask(plugin, () -> {

            if (inputManager.isInWithdrawSet(player)) {
                Bukkit.dispatchCommand(player, "novabank:bank withdraw " + amount);
            }
            else if (inputManager.isInDepositSet(player)) {
                Bukkit.dispatchCommand(player, "novabank:bank deposit " + amount);
            }

            inputManager.removeFromSet(player);
        });
    }
}