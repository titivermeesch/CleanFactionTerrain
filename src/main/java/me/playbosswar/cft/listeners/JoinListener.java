package me.playbosswar.cft.listeners;

import me.playbosswar.cft.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(Main.getBlockOnJoin() && Main.isTaskRunning()) {
            e.getPlayer().kickPlayer("§cMap is cleaning up... Please wait");
        }
    }
}
