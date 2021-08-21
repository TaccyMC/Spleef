package net.taccy.spleef.listeners;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.game.GameStateType;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class RideablePearlListener implements Listener {

    private Spleef plugin;

    public RideablePearlListener(Spleef plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (isElligable(player)) {
            if (player.getInventory().getItemInMainHand().getType() == Material.ENDER_PEARL) {
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    event.setCancelled(true);
                    EnderPearl pearl = player.launchProjectile(EnderPearl.class);

                    pearl.setVelocity(player.getLocation().getDirection());
                    pearl.setPassenger(player);
                    player.getInventory().getItemInMainHand().setAmount(
                            player.getInventory().getItemInMainHand().getAmount() - 1);
                }
            }
        }
    }

    @EventHandler
    public void onTeleportEvent(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            if (isElligable(e.getPlayer())) {
                if (e.getTo() != null) {
                    e.setCancelled(true);
                    e.getPlayer().teleport(e.getTo().add(0, 0, 0));
                }
            }
        }
    }

    @EventHandler
    public void onDismount(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (p.getVehicle() != null) {
            if (p.getVehicle().getType() == EntityType.ENDER_PEARL && isElligable(p)) {
                e.setCancelled(true);
            }
        }
    }

    public boolean isElligable(Player p) {
        return plugin.gm.getGameFromPlayer(p) != null && plugin.gm.getGameFromPlayer(p).getState().getType() == GameStateType.ACTIVE;
    }

}
