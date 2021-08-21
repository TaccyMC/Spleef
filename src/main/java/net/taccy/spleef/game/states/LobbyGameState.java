package net.taccy.spleef.game.states;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.game.GameState;
import net.taccy.spleef.game.GameStateType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyGameState extends GameState {

    private Spleef pl;
    private Game game;

    public LobbyGameState(Game game, Spleef pl) {
        this.pl = pl;
        this.game = game;
    }

    @Override
    public void onEnable(Spleef pl) {
        // generate event list
        game.getEvents().addAll(pl.em.getRandomEventSequence(game, 2));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (pl.gm.getGameFromPlayer(p) == game) {
            game.removePlayer(p);
            e.setQuitMessage("");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (pl.gm.getGameFromPlayer(p) == game) {
                e.setCancelled(true);
            }
        }
    }

    @Override
    public void handleTick() {
        // todo: idk
    }

    @Override
    public GameStateType getType() {
        return GameStateType.WAITING;
    }

}
