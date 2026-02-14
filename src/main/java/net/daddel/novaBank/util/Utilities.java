package net.daddel.novaBank.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.files.LanguageFile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Utilities {
    private final LanguageFile languageFile;
    private final JavaPlugin plugin;
    private final String prefix = ChatColor.of("#8A2BE5") + "NovaBank §8»§7";

    private final String perms = "novabank.";
    public Utilities(JavaPlugin plugin, LanguageFile languageFile) {
        this.languageFile = languageFile;
        this.plugin = plugin;
    }

    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    public boolean hasPermissions(Player player, String permission){
        return player.hasPermission(perms + permission);
    }

    public boolean isAdmin(Player player){
        return player.hasPermission(perms + "*");
    }

    public void consoleError(CommandSender sender){
        sender.sendMessage("Error: The console can't execute this command!");
    }

    public void sendColoredMessage(Player player, String message){
        player.sendMessage(translateColorString(message));
    }

    public void sendPlaceholderMessage(Player player, String path){
        String message = PlaceholderAPI.setPlaceholders(player, languageFile.getString(path));
        sendColoredMessage(player, message);
    }

    public void noPerms(Player player){
        sendPlaceholderMessage(player, "no-permissions");
    }

    public void wrongCommandUsage(Player player){
        sendPlaceholderMessage(player, "wrong-usage");
    }

    public void wrongAdminCommandUsage(Player player){
        sendPlaceholderMessage(player, "wrong-usage-admin");
    }

    public void playerNotFound(Player player) {
        sendPlaceholderMessage(player, "player-not-found");
    }

    public void invalidAmount(Player player) {
        sendPlaceholderMessage(player, "invalid-amount");
    }

    public void notEnoughMoney(Player player) {
        sendPlaceholderMessage(player, "not-enough-money");
    }

    public String translateColorString(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public List<String> translateColorStringList(List<String> list){
        List<String> translated = new ArrayList<>();
        for(String s : list){
            translated.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return translated;
    }

    public String getDefaultCommands() {
        return languageFile.containsString("placeholder.default-commands")
                ? languageFile.getString("placeholder.default-commands")
                : "§7/bank §7<§6deposit§7|§6withdraw§7|§6balance§7> §7[§6amount§7]";
    }

    public String getDefaultAdminCommands() {
        return languageFile.containsString("placeholder.default-admin-commands")
                ? languageFile.getString("placeholder.default-admin-commands")
                : "§7/bankadmin §7<§creload§7|§cset§7|§cadd§7|§cremove§7|§creset§7> §7[§6Player§7] [§6Amount§7]";
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
