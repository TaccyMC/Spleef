package net.taccy.spleef.game.states;

import com.google.common.collect.Sets;
import net.taccy.spleef.Spleef;
import net.taccy.spleef.event.Event;
import net.taccy.spleef.game.*;
import net.taccy.spleef.powerup.PowerupBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ActiveGameState extends GameState {

    private Spleef pl;
    private Game game;
    private Integer spawnableSize = null;
    private Integer executionTimeOfLastEvent = 0;

    public ActiveGameState(Game game, Spleef pl) {
        this.game = game;
        this.pl = pl;
    }

    @Override
    public void onEnable(Spleef pl) {
        super.onEnable(pl);
        game.setTimeLeft(480);
        game.broadcastMessage("&a⚐&f Game has been &astarted!");

        List<Block> arenaSpawnable = new ArrayList<>();

        /*
        for (Location loc : game.getArena().getSpawns()) {
            arenaSpawnable.addAll(pl.pm.getSpawnable(loc, 15, 8, 100, false));
        }

        spawnableSize = arenaSpawnable.size();
         */

        for (SpleefPlayer bp : game.getPlayers()) {
            pl.su.success4(bp.getPlayer().getLocation());
            game.giveItems(bp.getPlayer());
        }
    }

    @Override
    public void handleTick() {
        if (game.getTimeLeft() == 0) {
            game.setGameState(new EndGameState(game, pl));
        }

        if (game.getTimeLeft() == 300) {
            game.broadcastActionbar("&a⚐ &f5 &aminutes &fremaining! &a⚐");
        }

        if (game.getTimeLeft() == 60) {
            game.broadcastActionbar("&6⚐ &f60 &6seconds &fremaining! &6⚐");
        }

        // effects when holding blaze rod
        for (SpleefPlayer lp : game.getPlayers()) {
            if (lp.getPlayer().getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
                for (PotionEffect pe : lp.getPlayer().getActivePotionEffects()) {
                    if (pe.getType() == PotionEffectType.SPEED) {
                        return;
                    }
                }

                lp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 25, 0, false, false));
            }
        }

        // respawning of players
        /*
        Iterator deathIrr = game.getDeathTimes().keySet().iterator();
        while (deathIrr.hasNext()) {
            SpleefPlayer p = (SpleefPlayer) deathIrr.next();
            Integer time = game.getDeathTimes().get(p);
            if (game.getTimeLeft() <= time - 5) { // respawn time
                game.respawn(p);
                deathIrr.remove();
            }
        }
         */

        /*
        if (game.getTimeLeft() % 6 == 0) { // todo: calculate rate of spawning from spawnable area
            if (spawnableSize == null) {
                spawnableSize = pl.pm.getArenaSpawnable(game.getArena()).size();
            }

            int spawnRate = spawnableSize/500;

            for (int i = 0; i < spawnRate; i++) {
                pl.pm.spawnPowerup(game);
            }
        }

        Iterator pwrItr = game.getPowerups().iterator();
        while (pwrItr.hasNext()) {
            PowerupBlock pb = (PowerupBlock) pwrItr.next();
            if (game.getTimeLeft() <= pb.getCreatedAt() - 15) { // here's the despawn time for powerups.
                if (pb.isDespawnable()) {
                    pb.despawn();
                    pwrItr.remove();
                }
            }
        }
         */


        // events initiation (using time elapsed into game here which isn't ideal but it works because i dont think backwards)
        Integer timeElapsed = 480 - game.getTimeLeft();

        // events dont REALLY have to last 30s. i'm building the system to work for really any amount of time up until
        // when the next event starts (which means 2 minutes REAL max). 30s is just a sorta guess and guideline because
        // i dont want them to take over the game just add spice & variety but if it would improve the event, more than
        // 30s is possible
        if (timeElapsed % 120 == 0 && timeElapsed != 0) {
            if (game.getEvents().size() > game.getEventIndex() + 1) {
                game.setEventIndex(game.getEventIndex() + 1);
                Event event = game.getEvents().get(game.getEventIndex());

                game.broadcastActionbar(event.getColor() + event.getName());
                game.broadcastTitle("&l" + event.getColor() + event.getName(), ChatColor.GRAY + "event started!");

                executionTimeOfLastEvent = timeElapsed;
            }
        }

        // running tick of events
        // basically just getting the instance of the current event from its index and running the 'runTick' function
        // with the relative time since execution
        if (game.getEvents() != null && game.getEventIndex() != -1 && game.getEvents().get(game.getEventIndex()) != null) {
            Event event = game.getEvents().get(game.getEventIndex());

            event.runTick(timeElapsed - executionTimeOfLastEvent);
        }

    }

    @Override
    public GameStateType getType() {
        return GameStateType.ACTIVE;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (pl.gm.getGameFromPlayer(p) == game) {
            game.removePlayer(p);
            e.setQuitMessage("");
        }
    }

    // cancel fall damage
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (pl.gm.getGameFromPlayer(p) == game) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);
                }
                p.setHealth(20);
                p.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public void onHitPlayer(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player target = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            Game targetsGame = pl.gm.getGameFromPlayer(target);
            Game damagersGame = pl.gm.getGameFromPlayer(damager);
            if (targetsGame == game && damagersGame == game) {
                targetsGame.getSpleefPlayerFromPlayer(target).setLastHitBy(targetsGame.getSpleefPlayerFromPlayer(damager));
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Game playersGame = pl.gm.getGameFromPlayer(e.getPlayer());
        Player p = e.getPlayer();
        SpleefPlayer bp = pl.gm.getLaunchPlayerFromPlayer(p);

        if (playersGame == game) {
            if (game.isPaused()) {
                if (!p.hasPermission("spleef.bypass.pause")) {
                    if (e.getTo().getBlockX() != e.getFrom().getBlockX() || e.getTo().getBlockZ() != e.getFrom().getBlockZ()) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }

            if (game.getSpleefPlayerFromPlayer(p).isAlive()) {
                Iterator itr = game.getPowerups().iterator();

                while (itr.hasNext()) {
                    PowerupBlock pb = (PowerupBlock) itr.next();
                    if (getBlocksAtLocation(p.getLocation()).contains(pb.getLocation().getBlock())) {
                        pb.collect(p);
                        itr.remove();
                    }
                }
            }

            // VOID DEATH
            if (e.getTo().getBlockY() < 40 && bp.isAlive()) {
                if (bp.getLastHitBy() != null) {
                    SpleefPlayer creditedPlayer = bp.getLastHitBy();
                    game.kill(bp, creditedPlayer, DeathReason.KNOCK_VOID);
                    return;
                }

                game.kill(bp, null, DeathReason.NONE);
            }

        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Game playersGame = pl.gm.getGameFromPlayer(e.getPlayer());
        Player p = e.getPlayer();
        SpleefPlayer bp = pl.gm.getLaunchPlayerFromPlayer(p);

        if (playersGame == game) {
            e.setCancelled(true);
        }
    }

    public static Set<Block> getBlocksAtLocation(Location location) {
        Set<Block> blocks = Sets.newHashSet();
        blocks.add(getRelativeBlock(location, 0.66, 0, -0.66));
        blocks.add(getRelativeBlock(location, 0.66, 0, 0.66));
        blocks.add(getRelativeBlock(location, -0.66, 0, 0.66));
        blocks.add(getRelativeBlock(location, -0.66, 0, -0.66));
        blocks.add(getRelativeBlock(location, 0, 0, -0.66));
        blocks.add(getRelativeBlock(location, 0, 0, 0.66));
        blocks.add(getRelativeBlock(location, -0.66, 0, 0));
        blocks.add(getRelativeBlock(location, 0.66, 0, 0));
        blocks.add(getRelativeBlock(location, 0.66, 1, -0.66));
        blocks.add(getRelativeBlock(location, 0.66, 1, 0.66));
        blocks.add(getRelativeBlock(location, -0.66, 1, 0.66));
        blocks.add(getRelativeBlock(location, -0.66, 1, -0.66));
        blocks.add(getRelativeBlock(location, 0, 1, -0.66));
        blocks.add(getRelativeBlock(location, 0, 1, 0.66));
        blocks.add(getRelativeBlock(location, -0.66, 1, 0));
        blocks.add(getRelativeBlock(location, 0.66, 1, 0));
        blocks.add(getRelativeBlock(location, 0, -1, 0));
        return blocks;
    }

    public static Block getRelativeBlock(Location location, double x, double y, double z) {
        return new Location(location.getWorld(), location.getX() + x, location.getY() + y, location.getZ() + z).getBlock();
    }

}
