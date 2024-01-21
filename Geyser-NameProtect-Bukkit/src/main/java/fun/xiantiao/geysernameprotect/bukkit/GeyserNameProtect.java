package fun.xiantiao.geysernameprotect.bukkit;

import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

/**
 * @author xiantiao
 * @date 2024/1/21
 * Geyser-NameProtect
 */
public class GeyserNameProtect extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        int pluginId = 20793;
        new Metrics(this, pluginId);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (uuid != null) {
            if (!FloodgateApi.getInstance().isFloodgatePlayer(uuid)) {
                String prefix = FloodgateApi.getInstance().getPlayerPrefix();
                if (player.getName().startsWith(prefix)) {
                    String var0001 = getConfig().getString("tickMsg").replaceAll("%player_id%", event.getPlayer().getName());
                    String var0002 = var0001.replaceAll("%player_uuid%", String.valueOf(event.getPlayer().getUniqueId()));
                    String var0003 = var0002.replaceAll("%geyser_prefix%", prefix);
                    String var0004 = var0003.replaceAll("%system_time%", String.valueOf(System.currentTimeMillis()));
                    event.getPlayer().kickPlayer(var0004);
                }
            }
        }
    }
}
