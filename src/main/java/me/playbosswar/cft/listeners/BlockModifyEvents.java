package me.playbosswar.cft.listeners;

import me.playbosswar.cft.Main;
import me.playbosswar.cft.utils.BlockUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BlockModifyEvents implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        if (!BlockUtils.blockInBound(b)) {
            return;
        }

        PersistentDataContainer blockData = Main.getBlockApi().get(b);
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "placedBy");
        blockData.set(key, PersistentDataType.STRING, p.getDisplayName());
        Main.getBlockApi().set(b, blockData);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();

        if (!BlockUtils.blockInBound(b)) {
            return;
        }

        PersistentDataContainer blockData = Main.getBlockApi().get(b);
        NamespacedKey placedByKey = new NamespacedKey(Main.getInstance(), "placedBy");

        if (blockData.get(placedByKey, PersistentDataType.STRING) != null) {
            Main.getBlockApi().remove(b);
        } else {
            NamespacedKey key = new NamespacedKey(Main.getInstance(), "brokenType");
            blockData.set(key, PersistentDataType.STRING, b.getType().toString());
            Main.getBlockApi().set(b, blockData);
        }
    }

    @EventHandler
    public void onBlockDespawn(LeavesDecayEvent e) {
        Block b = e.getBlock();

        if (!BlockUtils.blockInBound(b)) {
            return;
        }

        PersistentDataContainer blockData = Main.getBlockApi().get(b);
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "brokenType");
        blockData.set(key, PersistentDataType.STRING, b.getType().toString());
        Main.getBlockApi().set(b, blockData);
    }

    @EventHandler
    public void onBlockGrow(StructureGrowEvent e) {
        Player p = e.getPlayer();
        List<BlockState> structureBlocks = e.getBlocks();

        for (BlockState bs : structureBlocks) {
            Block b = bs.getBlock();

            if (!BlockUtils.blockInBound(b)) {
                return;
            }

            PersistentDataContainer blockData = Main.getBlockApi().get(b);
            NamespacedKey key = new NamespacedKey(Main.getInstance(), "placedBy");
            blockData.set(key, PersistentDataType.STRING, "player");
            Main.getBlockApi().set(b, blockData);
        }
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent e) {
        List<Block> structureBlocks = e.blockList();

        for (Block b : structureBlocks) {
            if (!BlockUtils.blockInBound(b)) {
                return;
            }

            PersistentDataContainer blockData = Main.getBlockApi().get(b);
            NamespacedKey key = new NamespacedKey(Main.getInstance(), "brokenType");
            blockData.set(key, PersistentDataType.STRING, b.getType().toString());
            Main.getBlockApi().set(b, blockData);
        }
    }
}
