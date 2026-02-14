package net.daddel.novaBank.files;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class LanguageFile {

    private final JavaPlugin plugin;
    private FileConfiguration langConfig;
    private File langFile;

    public LanguageFile(JavaPlugin plugin) {
        this.plugin = plugin;
        reloadLanguage();
        createDefaultLanguages();
    }

    public void reloadLanguage() {
        File langDir = new File(plugin.getDataFolder(), "language");
        if (!langDir.exists()) {
            langDir.mkdirs();
        }

        langFile = new File(langDir, "en_US.yml");

        if (!langFile.exists()) {
            plugin.saveResource("language/en_US.yml", false);
        }

        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    private void createDefaultLanguages() {
        String[] defaultLanguages = {
                "en_US",
                "de_DE",
                "nl_NL"
        };

        File langDir = new File(plugin.getDataFolder(), "language");
        if (!langDir.exists()) {
            langDir.mkdirs();
        }

        for (String lang : defaultLanguages) {
            File file = new File(langDir, lang + ".yml");
            if (!file.exists()) {
                plugin.saveResource("language/" + lang + ".yml", false);
            }
        }
    }

    public String getCurrentLanguage() {
        if (langFile != null) {
            String fileName = langFile.getName();
            if (fileName.endsWith(".yml")) {
                return fileName.substring(0, fileName.length() - 4);
            }
        }
        return "unknown";
    }

    public boolean containsString(String string) {
        return langConfig.contains(string);
    }

    public String getString(String path) {
        return langConfig.contains(path) ? ChatColor.translateAlternateColorCodes('&', langConfig.getString(path)) : path;
    }

    public List<String> getStringList(String path) {
        return langConfig.contains(path) ? langConfig.getStringList(path) : List.of(path);
    }
}

