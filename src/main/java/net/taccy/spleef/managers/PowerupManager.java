package net.taccy.spleef.managers;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.powerup.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PowerupManager {

    private Spleef pl;

    public Random rand = new Random();
    public List<Powerup> powerups = new ArrayList<>();

    public PowerupManager(Spleef pl) {
        this.pl = pl;
        powerups.add(new JumpPowerup());
        powerups.add(new SpeedPowerup());
        powerups.add(new InvisibilityPowerup());
        powerups.add(new PearlPowerup());
        powerups.add(new ThrowableTNTPowerup());
        powerups.add(new LightningRodPowerup());
    }

    public Powerup getRandomPowerup() {
        return powerups.get(rand.nextInt(powerups.size()));
    }

    public void spawnPowerup(Game game) {
        return;
        /*
        Location spawn = pl.am.getRandomSpawnLocation(game.getArena());
        List<Block> spawnable = getSpawnable(spawn, 15, 8, 100, false);
        Block b = spawnable.get(rand.nextInt(spawnable.size()));

        Location location = b.getLocation();
        location.setX(b.getX() + 0.5);
        location.setZ(b.getZ() + 0.5);

        PowerupBlock pb = new PowerupBlock(game, location, getRandomPowerup());
        game.getPowerups().add(pb);
         */
    }

    /*
    public List<Block> getArenaSpawnable(SpleefArena arena) {
        List<Block> blocks = new ArrayList<>();
        for (Location spawn : arena.getSpawns()) {
            blocks.addAll(getSpawnable(spawn, 15, 8, 100, false));
        }
        return blocks;
    }
    */

    public List<Block> getSpawnable(Location loc, Integer horizontal, Integer vertical, Integer heightChecks, boolean average) {
        List<Block> spawnable = new ArrayList<>();

        // go through all blocks
        for (Block b : getNearbyBlocks(loc, horizontal)) {
            // iterate through vertical range
            for (int offset = vertical * -1; offset < vertical + 1; offset++) {
                // if block below isn't air and current block is air
                if (b.getRelative(0, offset - 1, 0).getType() != Material.AIR && b.getRelative(0, offset, 0).getType() == Material.AIR) {
                    // if the block has clear sight to sky
                    if (!isUnderRoof(b.getRelative(0, offset, 0), heightChecks)) {
                        spawnable.add(b.getRelative(0, offset, 0));
                    }
                }
            }
        }

        if (average) {
            List<Integer> xValues = new ArrayList<>();
            List<Integer> yValues = new ArrayList<>();
            List<Integer> zValues = new ArrayList<>();

            // add all x, y and z values to lists
            for (Block b : spawnable) {
                xValues.add(b.getX());
                yValues.add(b.getY());
                zValues.add(b.getZ());
            }

            // solve averages
            Integer averageX = findAverage(xValues);
            Integer averageY = findAverage(yValues);
            Integer averageZ = findAverage(zValues);

            Iterator itr = spawnable.iterator();

            while (itr.hasNext()) {
                Block b = (Block) itr.next();
                if (b.getX() > averageX + 6 || b.getY() > averageY + 3 || b.getZ() > averageZ + 6) {
                    itr.remove();
                }
            }
        }

        // return final list
        return spawnable;
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        int y = location.getBlockY();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                blocks.add(location.getWorld().getBlockAt(x, y, z));
            }
        }
        return blocks;
    }

    public boolean isUnderRoof(Block b, Integer checks) {
        for (int verticalTest = 0; verticalTest < checks; verticalTest++) {
            if (b.getRelative(0, verticalTest, 0).getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }

    public int findAverage(List<Integer> integers) {
        int sum = 0;
        for (Integer integer : integers) {
            sum = sum + integer;
        }

        return sum / integers.size();
    }

}
