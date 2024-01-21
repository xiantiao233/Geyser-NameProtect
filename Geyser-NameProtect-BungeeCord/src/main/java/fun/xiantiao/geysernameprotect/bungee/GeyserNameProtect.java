package fun.xiantiao.geysernameprotect.bungee;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import org.bstats.bungeecord.Metrics;
import org.geysermc.floodgate.api.FloodgateApi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author xiantiao
 * @date 2024/1/21
 * Geyser-NameProtect
 */
public class GeyserNameProtect extends Plugin implements Listener {
    private Configuration config;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        int pluginId = 1234;
        new Metrics(this, pluginId);

        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        String prefix = FloodgateApi.getInstance().getPlayerPrefix();
        if (event.getPlayer().getName().startsWith(prefix) && !FloodgateApi.getInstance().isFloodgatePlayer(event.getPlayer().getUniqueId())) {
            String var0001 = config.getString("tickMsg").replaceAll("%player_id%", event.getPlayer().getName());
            String var0002 = var0001.replaceAll("%player_uuid%", String.valueOf(event.getPlayer().getUniqueId()));
            String var0003 = var0002.replaceAll("%geyser_prefix%", prefix);
            String var0004 = var0003.replaceAll("%system_time%", String.valueOf(System.currentTimeMillis()));
            event.getPlayer().disconnect(var0004);
        }

    }

    private void saveDefaultConfig() {
        File dir = this.getDataFolder();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, "config.yml");
        if (!file.exists()) {
            try {
                InputStream in = this.getResourceAsStream("config.yml");

                try {
                    Files.copy(in, file.toPath());
                } catch (Throwable var7) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }

                    throw var7;
                }

                in.close();
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }

    }

    private void reloadConfig() {
        File file = new File(this.getDataFolder(), "config.yml");

        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }
}
