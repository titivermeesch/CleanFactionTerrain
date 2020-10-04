package me.playbosswar.cft.utils;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import org.bukkit.Location;

public class FactionUtils {
    public static boolean factionInRange(Location loc, int radius) {
        int chunks = Math.round(radius / 16);

        for(int x = -chunks; x < chunks; x++) {
            for(int z = -chunks; z < chunks;  z++) {
                FLocation location = new FLocation(loc);
                FLocation relLocation = location.getRelative(x, z);
                Faction faction = Board.getInstance().getFactionAt(relLocation);

                if(!faction.isWilderness() && !faction.isWarZone()) {
                    return true;
                }
            }
        }
        return  false;
    }
}
