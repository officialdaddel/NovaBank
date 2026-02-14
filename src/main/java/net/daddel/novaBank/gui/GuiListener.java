package net.daddel.novaBank.gui;

import net.daddel.novaBank.files.ConfigFile;
import net.daddel.novaBank.gui.chat.InputManager;
import net.daddel.novaBank.util.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {
    private final ConfigFile configFile;
    private Utilities utilities;
    private InputManager inputManager;
    public GuiListener(ConfigFile configFile, Utilities utilities, InputManager inputManager) {
        this.configFile = configFile;
        this.utilities = utilities;
        this.inputManager = inputManager;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = utilities.translateColorString(configFile.getString("gui.title"));

        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null) return;

        ItemStack item = event.getCurrentItem();

        if (item.getItemMeta() == null) return;
        if (!event.getView().getTitle().equalsIgnoreCase(title)) return;
        if (!item.getItemMeta().hasItemName()) return;

        event.setCancelled(true);

        switch (item.getItemMeta().getItemName()) {
            case "deposit" -> handleDepositItem(player);
            case "withdraw" -> handleWithdrawItem(player);
        }
    }

    private void handleDepositItem(Player player) {
        inputManager.addPlayerDeposit(player);

        utilities.sendPlaceholderMessage(player, "bank-menu.deposit");
        utilities.sendPlaceholderMessage(player, "bank-menu.cancel");
        player.closeInventory();
    }

    private void handleWithdrawItem(Player player) {
        inputManager.addPlayerWithdraw(player);

        utilities.sendPlaceholderMessage(player, "bank-menu.withdraw");
        utilities.sendPlaceholderMessage(player, "bank-menu.cancel");
        player.closeInventory();
    }
}
