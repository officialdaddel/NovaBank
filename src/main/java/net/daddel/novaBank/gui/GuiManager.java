package net.daddel.novaBank.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.files.ConfigFile;
import net.daddel.novaBank.files.LanguageFile;
import net.daddel.novaBank.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiManager {

    private final LanguageFile languageFile;
    private final ConfigFile configFile;
    private final Utilities utilities;

    public GuiManager(LanguageFile languageFile, ConfigFile configFile, Utilities utilities) {
        this.languageFile = languageFile;
        this.configFile = configFile;
        this.utilities = utilities;
    }

    public void openBankInventory(Player player) {
        String title = utilities.translateColorString(configFile.getString("gui.title"));
        int size = configFile.getInt("gui.size");

        Inventory inventory = Bukkit.createInventory(null, size, title);

        for (int slot = 0; slot < size; slot++) {
            switch (slot) {
                case 11 -> setItem(inventory, slot, "gui.withdraw.",
                        Material.RED_CONCRETE, "§cWithdraw", "withdraw", null);

                case 13 -> setItem(inventory, slot, "gui.profile.",
                        Material.PLAYER_HEAD, "&e%novabank_player_name%'s bank account money", "profile", player);

                case 15 -> setItem(inventory, slot, "gui.deposit.",
                        Material.YELLOW_CONCRETE, "§aDeposit", "deposit", null);

                default -> setPlaceholder(inventory, slot);
            }
        }

        player.openInventory(inventory);
    }

    private void setPlaceholder(Inventory inventory, int slot) {
        String path = "gui.placeholder.";

        if (!configFile.getBoolean(path + "use")) return;

        setItem(inventory, slot, path,
                Material.GRAY_STAINED_GLASS_PANE, "§7", "placeholder", null);
    }

    private void setItem(Inventory inventory, int slot, String path, Material fallbackMaterial, String fallbackName,
                         String itemName, Player player) {

        Material material = getMaterial(path, fallbackMaterial);

        String displayName = getDisplayName(path);
        if (displayName == null) displayName = fallbackName;

        displayName = PlaceholderAPI.setPlaceholders(player, displayName);

        List<String> lore = getLore(path);
        if (lore == null) lore = new ArrayList<>();

        lore = PlaceholderAPI.setPlaceholders(player, lore);

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        if (meta instanceof SkullMeta skullMeta && player != null) {
            skullMeta.setOwnerProfile(player.getPlayerProfile());
            meta = skullMeta;
        }

        meta.setDisplayName(displayName);
        meta.setItemName(itemName);
        meta.setLore(lore);

        itemStack.setItemMeta(meta);
        inventory.setItem(slot, itemStack);
    }

    private Material getMaterial(String path, Material fallback) {
        String name = configFile.getString(path + "material");
        if (name == null) return fallback;

        try {
            return Material.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }

    private String getDisplayName(String path) {
        String name = configFile.getString(path + "name");
        if (name == null) return null;
        return utilities.translateColorString(name);
    }

    private List<String> getLore(String path) {
        List<String> lore = configFile.getStringList(path + "description");
        if (lore == null || lore.isEmpty()) return null;
        return utilities.translateColorStringList(lore);
    }
}