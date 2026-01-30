package net.daddel.novaBank.listener;

import net.daddel.novaBank.Main;
import net.daddel.novaBank.utilities.Utilities;
import net.daddel.novaBank.utilities.misc.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinListener implements Listener {
    private JavaPlugin plugin;
    private Utilities utilities;
    public JoinListener(JavaPlugin plugin, Utilities utilities){
        this.plugin = plugin;
        this.utilities = utilities;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(Main.configData.getConfig().getBoolean("update-check-ingame")){
            if(player.hasPermission(utilities.perms + "updatenotify") || player.hasPermission(utilities.admin)){
                new UpdateChecker(plugin, 116303).getVersion(version -> {
                    if(!plugin.getDescription().getVersion().equals(version)){
                        String currentVersion = plugin.getDescription().getVersion();
                        player.sendMessage(utilities.prefix + " §cThere is a new update for NovaBank available!");
                        player.sendMessage(utilities.prefix + " §cPlease update the plugin to prevent errors!");
                        player.sendMessage(utilities.prefix + " §eYour version: §6" + currentVersion + " §8• §aNewest version: §2" + version);
                    }
                });
            }
        }
    }
}
