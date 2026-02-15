package net.daddel.novaBank;

import net.daddel.novaBank.commands.BankAdminCommand;
import net.daddel.novaBank.commands.BankCommand;
import net.daddel.novaBank.commands.MigrateBankCommand;
import net.daddel.novaBank.files.BankDataFile;
import net.daddel.novaBank.files.ConfigFile;
import net.daddel.novaBank.files.LanguageFile;
import net.daddel.novaBank.gui.GuiListener;
import net.daddel.novaBank.gui.GuiManager;
import net.daddel.novaBank.gui.chat.ChatListener;
import net.daddel.novaBank.gui.chat.InputManager;
import net.daddel.novaBank.listener.JoinListener;
import net.daddel.novaBank.manager.*;
import net.daddel.novaBank.tabcompleter.BankAdminTabCompleter;
import net.daddel.novaBank.tabcompleter.BankTabCompleter;
import net.daddel.novaBank.util.Placeholder;
import net.daddel.novaBank.util.UpdateChecker;
import net.daddel.novaBank.util.Utilities;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NovaBank extends JavaPlugin {

    private static NovaBank instance;

    private ConfigFile configFile;
    private LanguageFile languageFile;
    private BankDataFile bankDataFile;

    private Utilities utilities;
    private DatabaseManager databaseManager;
    private BankManager bankManager;
    private EconomyManager economyManager;
    private BankFileManager bankFileManager;
    private Placeholder placeholder;
    private GuiManager guiManager;
    private InputManager inputManager;

    @Override
    public void onEnable() {
        instance = this;

        loadFiles();
        loadManagers();
        registerCommands();
        registerTabCompleter();
        registerEvents();

        placeholder.register();
        checkVersion();

        getLogger().info("NovaBank v" + utilities.getVersion() + " enabled");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }

        getLogger().info("NovaBank v" + utilities.getVersion() + " disabled");
    }

    private void loadFiles() {
        configFile = new ConfigFile(this);
        languageFile = new LanguageFile(this);
        bankDataFile = new BankDataFile(this);
    }

    private void loadManagers() {
        inputManager = new InputManager();
        utilities = new Utilities(this, languageFile, configFile);

        economyManager = new EconomyManager(this);
        bankFileManager = new BankFileManager(bankDataFile);
        databaseManager = new DatabaseManager(this, configFile);

        bankManager = new BankManager(languageFile, databaseManager, economyManager, bankFileManager, utilities);

        placeholder = new Placeholder(utilities, bankManager);
        guiManager = new GuiManager(languageFile, configFile, utilities);
    }

    private void registerCommands() {
        getCommand("bank").setExecutor(
                new BankCommand(utilities, languageFile, bankManager, placeholder, guiManager, configFile));

        getCommand("bankadmin").setExecutor(
                new BankAdminCommand(this, utilities, languageFile, bankManager, placeholder));

        getCommand("migratebank").setExecutor(new MigrateBankCommand(this, databaseManager, utilities));
    }

    private void registerEvents() {
        PluginManager plm = getServer().getPluginManager();

        plm.registerEvents(new GuiListener(configFile, utilities, inputManager), this);
        plm.registerEvents(new ChatListener(this, utilities, inputManager), this);
        plm.registerEvents(new JoinListener(this, utilities, configFile), this);
    }

    private void registerTabCompleter() {
        getCommand("bank").setTabCompleter(new BankTabCompleter(configFile));
        getCommand("bankadmin").setTabCompleter(new BankAdminTabCompleter(utilities));
    }

    public void reloadAllConfigs() {
        configFile.reloadConfig();
        bankDataFile.reloadConfig();
        languageFile.reloadLanguage();
    }

    private void checkVersion() {
        new UpdateChecker(this, 116303).getVersion(version -> {
            if (utilities.getVersion().equals(version)) {
                getLogger().info("No new update available.");
            } else {
                getLogger().warning("New update available!");
                getLogger().warning("Current: v" + utilities.getVersion() + " | Latest: v" + version);
                getLogger().warning("Please update the plugin to avoid errors!");
            }
        });
    }

    public static NovaBank getInstance() {
        return instance;
    }
}