package net.taccy.spleef.managers;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Random;

public class ArenaManager {

    private Spleef pl;
    Random rand = new Random();

    public ArenaManager(Spleef pl) {
        this.pl = pl;
    }

    public SpleefArena getArenaFromString(String name) {
        for (SpleefArena a : Spleef.arenas) {
            if (a.getName().equalsIgnoreCase(name) || a.getDisplayName().equalsIgnoreCase(name)) {
                return a;
            }
        }
        return null;
    }

    public void deleteArena(SpleefArena arena) {
        pl.cm.deleteArena(arena);
        arena.setGame(null);
        Spleef.arenas.remove(arena);
    }

    public SpleefArena getArena(World world) {
        for (SpleefArena a : Spleef.arenas) {
            if (a.getLobby().getWorld() == world) {
                return a;
            }
        }
        return null;
    }

}
