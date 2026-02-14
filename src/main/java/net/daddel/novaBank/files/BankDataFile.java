package net.daddel.novaBank.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BankDataFile {

    private final JavaPlugin plugin;

    private File configFile;
    private FileConfiguration config;

    public BankDataFile(JavaPlugin plugin) {
        this.plugin = plugin;
        createConfigs();
    }

    private void createConfigs() {
        configFile = new File(plugin.getDataFolder(), "data/bankData.yml");
        if(!configFile.exists()){
            plugin.saveResource("data/bankData.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        }catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private FileConfiguration getConfig() {
        return config;
    }

    public String getString(String path) {
        return getConfig().getString(path);
    }

    public boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public int getInt(String path) {
        return getConfig().getInt(path);
    }

    public void setString(String path, String value) {
        config.set(path, value);
        saveConfig();
    }

    public void setInteger(String path, int value) {
        config.set(path, value);
        saveConfig();
    }
}