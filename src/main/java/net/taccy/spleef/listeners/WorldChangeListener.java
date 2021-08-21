package net.taccy.spleef.listeners;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {

    private Spleef pl;

    public WorldChangeListener(Spleef pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onChangeWorlds(PlayerChangedWorldEvent e) {
        Game game = pl.gm.getGameFromPlayer(e.getPlayer());
        if (game != null) {
            if (e.getFrom() == game.getArena().getLobby().getWorld()) {
                game.removePlayer(e.getPlayer());
            }
        }
    }

}
