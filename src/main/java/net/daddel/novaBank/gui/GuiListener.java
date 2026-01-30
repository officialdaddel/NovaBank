package net.daddel.novaBank.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.Main;
import net.daddel.novaBank.gui.chat.InputManager;
import net.daddel.novaBank.utilities.manager.LanguageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiListener implements Listener {
    private LanguageManager languageManager;
    public GuiListener(LanguageManager languageManager){
        this.languageManager = languageManager;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String guiName = Main.configData.getConfig().getString("gui.title");
        Player p = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;
        if (event.getView().getTitle().equalsIgnoreCase(translateColorString(guiName))) {
            if (event.getCurrentItem().getItemMeta().hasItemName()) {
                event.setCancelled(true);
                switch (event.getCurrentItem().getItemMeta().getItemName()) {
                    case "deposit":
                        InputManager.addPlayerDeposit(p);
                        String depositMessage = PlaceholderAPI.setPlaceholders(p, languageManager.getMessage("bank-menu.deposit"));
                        p.sendMessage(translateColorString(depositMessage));
                        p.closeInventory();
                        break;
                    case "withdraw":
                        InputManager.addPlayerWithdraw(p);
                        String withdrawMessage = PlaceholderAPI.setPlaceholders(p, languageManager.getMessage("bank-menu.withdraw"));
                        p.sendMessage(translateColorString(withdrawMessage));
                        p.closeInventory();
                        break;
                }
            }
        }
    }
    private String translateColorString(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
