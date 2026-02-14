package net.daddel.novaBank.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import net.daddel.novaBank.files.LanguageFile;
import net.daddel.novaBank.util.Utilities;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyManager {
    private Economy economy = null;;
    private final JavaPlugin plugin;

    public EconomyManager(JavaPlugin plugin){
        this.plugin = plugin;
        setupEconomy();
    }

    public void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if(economyProvider != null) economy = economyProvider.getProvider();
    }

    public void addMoney(OfflinePlayer player, double amount) {
        if(economy == null) return;
        economy.depositPlayer(player, amount);
    }

    public void setMoney(OfflinePlayer player, double amount) {
        if(economy == null) return;
        double current = economy.getBalance(player);
        economy.withdrawPlayer(player, current);
        economy.depositPlayer(player, amount);
    }

    public void removeMoney(OfflinePlayer player, double amount) {
        if(economy == null) return;
        if(economy.has(player, amount)) economy.withdrawPlayer(player, amount);
    }

    public boolean hasEnoughMoney(OfflinePlayer player, double amount) {
        return economy != null && economy.has(player, amount);
    }
}
