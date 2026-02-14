package net.daddel.novaBank.listener;

import net.daddel.novaBank.files.ConfigFile;
import net.daddel.novaBank.util.UpdateChecker;
import net.daddel.novaBank.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinListener implements Listener {
    private final JavaPlugin plugin;
    private final Utilities utilities;
    private final ConfigFile configFile;

    public JoinListener(JavaPlugin plugin, Utilities utilities, ConfigFile configFile) {
        this.plugin = plugin;
        this.utilities = utilities;
        this.configFile = configFile;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (!configFile.getBoolean("update-check-ingame")) return;

        Player player = event.getPlayer();

        if (!utilities.hasPermissions(player, "updatenotify") || !utilities.isAdmin(player)) return;

        new UpdateChecker(plugin, 116303).getVersion(latestVersion -> {

            String currentVersion = plugin.getDescription().getVersion();

            if (currentVersion.equals(latestVersion)) return;

            Bukkit.getScheduler().runTask(plugin, () -> {
                player.sendMessage(utilities.getPrefix() + " §cA new NovaBank update is available!");
                player.sendMessage(utilities.getPrefix() + " §eCurrent: §6v" + currentVersion + " §8• §aLatest: §2v" + latestVersion);
            });
        });
    }
}
