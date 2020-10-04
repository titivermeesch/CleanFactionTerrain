package me.playbosswar.cft.commands;

import me.playbosswar.cft.Main;
import me.playbosswar.cft.utils.GenericUtils;
import me.playbosswar.cft.utils.JavaUtils;
import me.playbosswar.cft.utils.MapCleaner;
import me.playbosswar.cft.utils.ChunkUtils;
import me.wiefferink.bukkitdo.Do;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

public class MainCommandExecutor implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            // TODO: Send help message
            return true;
        }

        Player p = (Player) sender;

        if (args[0].equalsIgnoreCase("cleanup")) {
            MapCleaner cleaner = new MapCleaner(p, p.getWorld());
            cleaner.start();
            return true;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            YamlConfiguration conf = (YamlConfiguration) Main.getInstance().getConfig();
            LocalTime startTime = LocalTime.now();
            Main.setTaskRunning(true);
            p.sendMessage("§bWe are resetting all block change registrations, please wait...");

            GenericUtils.handleCleanupStart();

            Location loc1 = new Location(p.getWorld(), conf.getDouble("pos1.x"), conf.getDouble("pos1.y"), conf.getDouble("pos1.z"));
            Location loc2 = new Location(p.getWorld(), conf.getDouble("pos2.x"), conf.getDouble("pos2.y"), conf.getDouble("pos2.z"));
            Set<Chunk> chunks = ChunkUtils.getAllChunks(loc1, loc2);

            Do.forAll(conf.getInt("updates-per-tick"), chunks, chunk -> {
                        Set<Block> chunkData = Main.getBlockApi().getMetadataLocations(chunk);

                        int index = JavaUtils.getIndex(chunks, chunk);

                        if (conf.getBoolean("alert-cleanup-progress-in-console")) {
                            System.out.println("Processing chunk " + index + "/" + chunks.size());
                        }

                        if (chunkData == null || chunkData.size() == 0) {
                            return;
                        }

                        for (Block b : chunkData) {
                            Main.getBlockApi().remove(b);
                        }
                    },
                    () -> {
                        LocalTime stopTime = LocalTime.now();
                        Duration diff = Duration.between(startTime, stopTime);
                        p.sendMessage("§b§lReset done! " + chunks.size() + " chunk data has been removed. Process took " + diff.getSeconds() + " seconds");
                        Main.setTaskRunning(false);
                    });
        }
        return true;
    }
}
