package net.taccy.spleef.game.states;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.game.GameState;
import net.taccy.spleef.game.GameStateType;
import net.taccy.spleef.game.SpleefPlayer;
import net.taccy.spleef.powerup.PowerupBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class EndGameState extends GameState {

    private Spleef pl;
    private Game game;

    public EndGameState(Game game, Spleef pl) {
        this.game = game;
        this.pl = pl;
    }

    @Override
    public void onEnable(Spleef pl) {
        super.onEnable(pl);

        SpleefPlayer winner = game.getPlayerMostKills();

        for (SpleefPlayer bp : game.getPlayers()) {
            pl.su.success3(bp.getPlayer().getLocation());
            game.preparePlayer(bp.getPlayer());
            game.clearEffects(bp.getPlayer());
            if (!bp.isAlive()) {
                bp.getPlayer().teleport(game.getArena().getCenter());
            }
        }

        for (PowerupBlock pb : game.getPowerups()) {
            pb.despawn();
        }

        game.getPowerups().clear();

        if (winner != null) {
            game.broadcastTitle("&d&l" + winner.getPlayer().getName() + "&f has won!", "&7with &d" + winner.getKills() + (winner.getKills() == 1 ? "&7 kill!" : "&7 kills!"));
            game.broadcastMessage("&d⚐&7 " + winner.getPlayer().getName() + "&f has won with &d" + winner.getKills() + (winner.getKills() == 1 ? "&7 kill!" : "&7 kills!"));
        } else {
            game.broadcastTitle("&7&lNobody &fhas won!", "&7Nobody had any kills!");
            game.broadcastMessage("&d⚐&f Nobody has won as all players &dhad no kills!");
        }

        game.setTimeLeft(5);
    }

    @Override
    public void onDisable(Spleef pl) {
        super.onDisable(pl);
        game.resetData();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (pl.gm.getGameFromPlayer(p) == game) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL || e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (pl.gm.getGameFromPlayer(p) == game) {
            game.removePlayer(p);
            e.setQuitMessage("");
        }
    }

    @Override
    public void handleTick() {
        if (game.getTimeLeft() == 0) {
            List<SpleefPlayer> copyPlayers = new ArrayList<>();
            copyPlayers.addAll(game.getPlayers()); // todo: probably find a better solution to this mess.. but it works!

            game.broadcastMessage("&a⚐&f Game has &aconcluded!");

            Iterator<SpleefPlayer> iter = copyPlayers.iterator();
            while (iter.hasNext()) {
                SpleefPlayer bp = iter.next();
                game.removePlayer(bp.getPlayer());// should automatically detect no players and reset game and stuff
                iter.remove();
            }
        }
    }

    @Override
    public GameStateType getType() {
        return GameStateType.END;
    }
}
