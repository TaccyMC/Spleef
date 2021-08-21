package net.taccy.spleef.managers;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class TimingsManager extends BukkitRunnable {

    private Spleef pl;

    public TimingsManager(Spleef pl) {
        this.pl = pl;
    }

    @Override
    public void run() {
        for (SpleefArena arena : Spleef.arenas) {
            Game game = arena.getGame();
            if (game != null) {
                game.tickEvent();
            } else {
                Bukkit.getLogger().log(Level.SEVERE, "game was null from timings manager");
            }
        }
    }

}
