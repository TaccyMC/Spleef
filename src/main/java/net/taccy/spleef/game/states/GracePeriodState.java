package net.taccy.spleef.game.states;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.game.GameState;
import net.taccy.spleef.game.GameStateType;
import net.taccy.spleef.game.SpleefPlayer;
import net.taccy.spleef.utils.CustomRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Level;

public class GracePeriodState extends GameState {

    private Spleef pl;
    private Game game;
    Integer time = 3;
    CustomRunnable runnable;

    public GracePeriodState(Game game, Spleef pl) {
        this.pl = pl;
        this.game = game;
    }

    @Override
    public void onEnable(Spleef pl) {
        super.onEnable(pl);
        game.setTimeLeft(5);
        game.broadcastMessage("&6⛏ &fStarted &6grace period!");
        game.broadcastMessage("&b⚐ &fGrace period ends in &b3 &fseconds..");

        for (SpleefPlayer bp : game.getPlayers()) {
            pl.su.release(bp.getPlayer().getLocation());
            bp.getPlayer().addPotionEffect(
                    new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            bp.getPlayer().addPotionEffect(
                    new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
            bp.getPlayer().teleport(game.getArena().getCenter());
        }
    }

    @Override
    public void onDisable(Spleef pl) {
        super.onDisable(pl);

        for (SpleefPlayer bp : game.getPlayers()) {
            bp.getPlayer().removePotionEffect(PotionEffectType.SPEED);
            bp.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }

    @Override
    public void handleTick() {
        if (game.getTimeLeft() == 0) {
            Bukkit.getLogger().log(Level.INFO, "Started game in arena " + game.getArena().getName());
            game.setGameState(new ActiveGameState(game, pl));
        }
    }

    @Override
    public GameStateType getType() {
        return GameStateType.GRACE;
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

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (pl.gm.getGameFromPlayer(p) == game) {
            if (pl.gm.getLaunchPlayerFromPlayer(p) != null && pl.gm.getLaunchPlayerFromPlayer(p).isAlive()) {
                pl.lm.handleLaunchers(e);
            }
        }
    }

}
