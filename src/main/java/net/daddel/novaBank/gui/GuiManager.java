package net.daddel.novaBank.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.Main;
import net.daddel.novaBank.utilities.manager.LanguageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiManager {
    private LanguageManager language;
    private String guiName = Main.configData.getConfig().getString("gui.title");
    private  int size = Main.configData.getConfig().getInt("gui.size");
    public GuiManager(LanguageManager language){
        this.language = language;
    }
    public void openInventory(Player player){
        Inventory inventory = Bukkit.createInventory(null, size, translateColorString(guiName));
        for (int i = 0; i < size; i++){
            if(i == 11){
                setWithdrawItem(inventory, i);
            }else if(i == 13) {
                setProfileHead(player, inventory, i);
            }else if(i == 15){
                setDepositItem(inventory, i);
            }else {
                setPlaceholderItem(inventory, i);
            }
        }
        player.openInventory(inventory);
    }
    private void setPlaceholderItem(Inventory inventory, int slot){
        Material material = Material.valueOf(Main.configData.getConfig().getString("gui.placeholder.material"));
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(translateColorString(Main.configData.getConfig().getString("gui.placeholder.name")));
        is.setItemMeta(im);
        inventory.setItem(slot, is);
    }
    private void setWithdrawItem(Inventory inventory, int slot){
        Material material = Material.valueOf(Main.configData.getConfig().getString("gui.withdraw.material"));
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(translateColorString(Main.configData.getConfig().getString("gui.withdraw.name")));
        im.setLore(translateColorStringList(Main.configData.getConfig().getStringList("gui.withdraw.description")));
        im.setItemName("withdraw");
        is.setItemMeta(im);
        inventory.setItem(slot, is);
    }
    private void setDepositItem(Inventory inventory, int slot){
        Material material = Material.valueOf(Main.configData.getConfig().getString("gui.deposit.material"));
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(translateColorString(Main.configData.getConfig().getString("gui.deposit.name")));
        im.setLore(translateColorStringList(Main.configData.getConfig().getStringList("gui.deposit.description")));
        im.setItemName("deposit");
        is.setItemMeta(im);
        inventory.setItem(slot, is);
    }
    private void setProfileHead(Player player, Inventory inventory, int slot){
        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) is.getItemMeta();
        meta.setOwnerProfile(player.getPlayerProfile());
        String profileName = PlaceholderAPI.setPlaceholders(player, Main.configData.getConfig().getString("gui.profile.name"));
        meta.setDisplayName(translateColorString(profileName));
        String balance = PlaceholderAPI.setPlaceholders(player, Main.configData.getConfig().getString("gui.profile.description"));
        meta.setLore(Arrays.asList(translateColorString(balance)));
        meta.setItemName("profile");
        is.setItemMeta(meta);
        inventory.setItem(slot, is);
    }
    private String translateColorString(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    private List<String> translateColorStringList(List<String> list){
        List<String> translated = new ArrayList<>();
        for(String s : list){
            translated.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return translated;
    }
}
