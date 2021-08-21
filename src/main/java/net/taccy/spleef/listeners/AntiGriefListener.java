package net.taccy.spleef.listeners;

import net.taccy.spleef.Spleef;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class AntiGriefListener implements Listener {

    private Spleef pl;

    private List<Material> blacklistedInteract = new ArrayList<>();

    public AntiGriefListener(Spleef pl) {
        this.pl = pl;

        // add blacklisted blocks here for interaction
        blacklistedInteract.add(Material.CHEST);
        blacklistedInteract.add(Material.FURNACE);
        blacklistedInteract.add(Material.BARREL);
        blacklistedInteract.add(Material.CRAFTING_TABLE);
        blacklistedInteract.add(Material.HOPPER);
        blacklistedInteract.add(Material.ANVIL);
        blacklistedInteract.add(Material.LEVER);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if (pl.gm.getGameFromPlayer(p) != null) {
            if (p.hasPermission("bowlaunch.bypass.grief")) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if (pl.gm.getGameFromPlayer(p) != null) {
            if (p.hasPermission("bowlaunch.bypass.grief")) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (pl.gm.getGameFromPlayer(p) != null) {
            if (!p.hasPermission("bowlaunch.bypass.grief")) {
                if (e.getClickedBlock() != null) {
                    Block b = e.getClickedBlock();
                    if (blacklistedInteract.contains(b.getType())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}
