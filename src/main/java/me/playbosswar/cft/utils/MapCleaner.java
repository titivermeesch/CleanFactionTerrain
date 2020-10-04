package me.playbosswar.cft.utils;

import me.playbosswar.cft.Main;
import me.wiefferink.bukkitdo.Do;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

public class MapCleaner extends Thread {
    YamlConfiguration conf = (YamlConfiguration) Main.getInstance().getConfig();
    private final World world;
    private final Player initializer;
    private int totalChunks = 0;
    private int totalBlocks = 0;
    private LocalTime startTime;

    public MapCleaner(Player p, World world) {
        this.world = world;
        this.initializer = p;
    }

    public void run() {
        this.startTime = LocalTime.now();
        initializer.sendMessage("§bWe are cleaning up the map, please wait...");
        Main.setTaskRunning(true);

        GenericUtils.handleCleanupStart();

        loadAllChunks(world);
    }

    private void loadAllChunks(World world) {
        Location loc1 = new Location(world, conf.getDouble("pos1.x"), conf.getDouble("pos1.y"), conf.getDouble("pos1.z"));
        Location loc2 = new Location(world, conf.getDouble("pos2.x"), conf.getDouble("pos2.y"), conf.getDouble("pos2.z"));
        Set<Chunk> chunks = ChunkUtils.getAllChunks(loc1, loc2);

        this.totalChunks = chunks.size();

        Do.forAll(conf.getInt("updates-per-tick"), chunks, chunk -> {
                    Set<Block> chunkData = Main.getBlockApi().getMetadataLocations(chunk);

                    int index = JavaUtils.getIndex(chunks, chunk);

                    if (conf.getBoolean("alert-cleanup-progress-in-console")) {
                        System.out.println("Processing chunk " + index + "/" + chunks.size());
                    }

                    if (chunkData == null || chunkData.size() == 0) {
                        return;
                    }

                    totalBlocks += chunkData.size();

                    for (Block b : chunkData) {
                        PersistentDataContainer data = Main.getBlockApi().get(b);
                        NamespacedKey placedBy = new NamespacedKey(Main.getInstance(), "placedBy");
                        NamespacedKey brokenType = new NamespacedKey(Main.getInstance(), "brokenType");

                        Bukkit.getScheduler().callSyncMethod(Main.getInstance(), () -> {

                            if (Objects.requireNonNull(conf.getList("whitelist-items")).contains(b.getType().toString())) {
                                return null;
                            }

                            if (b.getType().equals(Material.CHEST)) {
                                Chest chest = (Chest) b.getState();

                                for (Object itemString : Objects.requireNonNull(conf.getList("valuable-chest-items"))) {
                                    Material confMaterial = Material.valueOf(itemString.toString());

                                    if (chest.getBlockInventory().contains(confMaterial)) {
                                        return null;
                                    }
                                }
                            }

                            if (data.get(placedBy, PersistentDataType.STRING) != null) {
                                b.setType(Material.AIR);
                                Main.getBlockApi().remove(b);
                            } else {
                                b.setType(Material.valueOf(data.get(brokenType, PersistentDataType.STRING)));
                            }

                            Main.getBlockApi().remove(b);

                            return null;
                        });
                    }
                },
                () -> {
                    LocalTime stopTime = LocalTime.now();
                    Duration diff = Duration.between(startTime, stopTime);
                    initializer.sendMessage("§b§lCleanup done! Process took " + diff.getSeconds() + " seconds");
                    initializer.sendMessage("§6Details about cleanup:");
                    initializer.sendMessage("§6Chunks covered: §f" + totalChunks);
                    initializer.sendMessage("§6Blocks updated: §f" + totalBlocks);
                    Main.setTaskRunning(false);
                });
    }
}
