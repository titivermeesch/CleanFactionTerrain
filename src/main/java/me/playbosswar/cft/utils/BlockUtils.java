package me.playbosswar.cft.utils;

import com.massivecraft.factions.*;
import com.massivecraft.factions.shade.mkremins.fanciful.FancyMessage;
import me.playbosswar.cft.Main;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class BlockUtils {
    /**
     * Check if block is inside applied bound
     * @param b
     * @return
     */
    public static Boolean blockInBound(Block b) {
        YamlConfiguration conf = (YamlConfiguration) Main.getInstance().getConfig();

        FLocation loc = new FLocation(b.getLocation());
        Faction faction = Board.getInstance().getFactionAt(loc);

        if(FactionUtils.factionInRange(b.getLocation(), conf.getInt("no-rollback-around-claim-radius"))) {
            System.out.println("FACTION IN RANGE");
            return false;
        }

        if(!faction.isWilderness() && !faction.isWarZone()) {
            return false;
        }

        return b.getY() > conf.getDouble("pos1.y") && b.getY() < conf.getDouble("pos2.y");
    }
}
