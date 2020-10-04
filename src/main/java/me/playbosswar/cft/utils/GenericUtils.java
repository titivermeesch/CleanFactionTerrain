package me.playbosswar.cft.utils;

import me.playbosswar.cft.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class GenericUtils {
    public static void handleCleanupStart() {
        FileConfiguration conf = Main.getInstance().getConfig();

        if (conf.getBoolean("maintenance-on-cleanup")) {
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                Bukkit.getScheduler().callSyncMethod(Main.getInstance(), () -> {
                    player.kickPlayer("§cMap is resetting...");
                    return null;
                });
            });
        }
    }
}
