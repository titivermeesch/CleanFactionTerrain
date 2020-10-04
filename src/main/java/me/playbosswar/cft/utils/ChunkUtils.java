package me.playbosswar.cft.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class ChunkUtils {
    public static Set<Chunk> getAllChunks(Location loc2, Location loc1) {
        final World world = loc1.getWorld();
        Set<Chunk> chunks = new HashSet<Chunk>();
        int xMin;
        int xMax;

        if (loc1.getChunk().getX() > loc2.getChunk().getX()) {
            xMin = loc2.getChunk().getX();
            xMax = loc1.getChunk().getX();
        } else {
            xMin = loc1.getChunk().getX();
            xMax = loc2.getChunk().getX();
        }

        int zMin;
        int zMax;
        if (loc1.getChunk().getZ() > loc2.getChunk().getZ()) {
            zMin = loc2.getChunk().getZ();
            zMax = loc1.getChunk().getZ();
        } else {
            zMin = loc2.getChunk().getZ();
            zMax = loc1.getChunk().getZ();
        }

        for (int x = xMin; x < xMax; x++) {
            for (int z = zMin; z < zMax; z++) {
                Chunk chunk = world.getChunkAt(x, z);
                chunks.add(chunk);
            }
        }

        return chunks;
    }
}
