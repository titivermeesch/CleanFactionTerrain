package me.playbosswar.cft;

import com.darkender.plugins.persistentblockmetadataapi.PersistentBlockMetadataAPI;
import me.playbosswar.cft.commands.MainCommandExecutor;
import me.playbosswar.cft.listeners.BlockModifyEvents;
import me.playbosswar.cft.listeners.JoinListener;
import me.wiefferink.bukkitdo.Do;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Plugin instance;
    private static PersistentBlockMetadataAPI blockApi;
    private static boolean blockJoin = false;
    private static boolean taskRunning = false;

    @Override
    public void onEnable() {
        instance = this;
        blockApi = new PersistentBlockMetadataAPI(this);
        Do.init(this);
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new BlockModifyEvents(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getCommand("cft").setExecutor(new MainCommandExecutor());
        blockJoin = getConfig().getBoolean("maintenance-on-cleanup");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static PersistentBlockMetadataAPI getBlockApi() {
        return blockApi;
    }

    public static boolean getBlockOnJoin() {
        return blockJoin;
    }

    public static boolean isTaskRunning() {
        return taskRunning;
    }

    public static void setTaskRunning(boolean taskRunning) {
        Main.taskRunning = taskRunning;
    }
}
