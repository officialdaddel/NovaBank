package net.daddel.novaBank.gui.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.gui.GuiManager;
import net.daddel.novaBank.utilities.Utilities;
import net.daddel.novaBank.utilities.manager.LanguageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatListener implements Listener {
    private GuiManager guiManager;
    private LanguageManager language;
    private Utilities utilities;
    private JavaPlugin plugin;
    public ChatListener(GuiManager guiManager, LanguageManager language, Utilities utilities, JavaPlugin plugin){
        this.guiManager = guiManager;
        this.language = language;
        this.utilities = utilities;
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String input = event.getMessage();
        if(InputManager.isInDeposit(player)){
            event.setCancelled(true);
            if(input.equalsIgnoreCase("CANCEL")){
                String message = PlaceholderAPI.setPlaceholders(player, language.getMessage("bank-menu.cancel-confirm"));
                player.sendMessage(translateColor(message));
                InputManager.removePlayerDeposit(player);
            }else if(utilities.isInteger(input)){
                int amount = Integer.parseInt(input);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    depositMoney(player, amount);
                });
                InputManager.removePlayerDeposit(player);
            }else {
                String message = PlaceholderAPI.setPlaceholders(player, language.getMessage("invalid-amount"));
                player.sendMessage(translateColor(message));
                InputManager.removePlayerDeposit(player);
            }
        }else if(InputManager.isInWithdraw(player)) {
            event.setCancelled(true);
            if (input.equalsIgnoreCase("CANCEL")) {
                String message = PlaceholderAPI.setPlaceholders(player, language.getMessage("bank-menu.cancel-confirm"));
                player.sendMessage(translateColor(message));
                InputManager.removePlayerWithdraw(player);
            } else if (utilities.isInteger(input)) {
                int amount = Integer.parseInt(input);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    withdrawMoney(player, amount);
                });
                InputManager.removePlayerWithdraw(player);
            } else {
                String message = PlaceholderAPI.setPlaceholders(player, language.getMessage("invalid-amount"));
                player.sendMessage(translateColor(message));
                InputManager.removePlayerWithdraw(player);
            }
        }
    }

    private void withdrawMoney(Player player, int amount){
        Bukkit.dispatchCommand(player, "bank withdraw " + amount);
    }
    private void depositMoney(Player player, int amount){
        Bukkit.dispatchCommand(player, "bank deposit " + amount);
    }
    private String translateColor(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
