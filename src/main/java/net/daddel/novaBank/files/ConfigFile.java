package net.daddel.novaBank.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigFile {

    private final JavaPlugin plugin;

    private File configFile;
    private FileConfiguration config;

    public ConfigFile(JavaPlugin plugin) {
        this.plugin = plugin;
        createConfigs();
    }

    private void createConfigs() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if(!configFile.exists()){
            plugin.saveResource("config.yml", false);
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

    public List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }
}