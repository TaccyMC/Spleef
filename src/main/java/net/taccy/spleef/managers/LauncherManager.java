package net.taccy.spleef.managers;

import net.taccy.spleef.Spleef;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class LauncherManager implements Listener {

    private Spleef pl;

    public LauncherManager(Spleef pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().getName().equalsIgnoreCase("ThatsFinn")
                || e.getPlayer().getName().equalsIgnoreCase("dragondomenic")) {
            handleLaunchers(e);
        }
    }

    public void handleLaunchers(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.LIME_CONCRETE) {
            p.setVelocity(p.getLocation().getDirection().multiply(0.75));
            p.setVelocity(new Vector(p.getVelocity().getX(), 1.75D, p.getVelocity().getZ()));
            pl.su.launch(p.getLocation());
        }
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GREEN_CONCRETE) {
            e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.8));
            e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 1.1D, e.getPlayer().getVelocity().getZ()));
            pl.su.launch(p.getLocation());
        }
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.YELLOW_CONCRETE) {
            e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(3.5));
            e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 1.D, e.getPlayer().getVelocity().getZ()));
            pl.su.launch(p.getLocation());
        }
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.RED_CONCRETE) {
            e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(0.75));
            e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 2.5D, e.getPlayer().getVelocity().getZ()));
            pl.su.launch(p.getLocation());
        }
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BLUE_CONCRETE) {
            e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(0.3));
            e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 0.8D, e.getPlayer().getVelocity().getZ()));
            pl.su.launch(p.getLocation());
        }
        if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.LIGHT_BLUE_CONCRETE) {
            e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(2D));
            e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 2D, e.getPlayer().getVelocity().getZ()));
            pl.su.launch(p.getLocation());
        }
    }

}
