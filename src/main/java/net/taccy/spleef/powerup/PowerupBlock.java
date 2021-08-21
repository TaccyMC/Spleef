package net.taccy.spleef.powerup;

import net.taccy.spleef.game.Game;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class PowerupBlock {

    private Game game;
    private ArmorStand armorStand;
    private Powerup powerup;
    private Location location;
    private Integer createdAt;
    private Boolean despawnable = true;

    public PowerupBlock(Game game, Location location, Powerup powerup) {
        this.location = location;
        this.powerup = powerup;

        if (game != null) {
            createdAt = game.getTimeLeft();
        }

        ItemStack item = new ItemStack(powerup.getItem());
        Material block = powerup.getBlock();

        location.getBlock().setType(block);
        
        location = new Location(
                location.getWorld(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ());

        boolean isBlock = item.getType().isBlock();

        location.setX(location.getX() + (isBlock ? 1.17 : 1.57));
        location.setZ(location.getZ() + (isBlock ? 0.26 : 0.55));
        location.setY(location.getY() + (isBlock ? -0.35 : -0.9));


        armorStand = location.getWorld().spawn(location, ArmorStand.class);

        armorStand.setVisible(false);
        armorStand.getEquipment().setItemInMainHand(item);
        armorStand.setRightArmPose(isBlock ?
                new EulerAngle(-0.27, 0.78, 0) :
                new EulerAngle(-1.1, 1.57, 0));
        armorStand.setGravity(false);
        armorStand.setArms(true);
        armorStand.setMarker(true);


    }

    public void collect(Player p) {
        powerup.execute(p);
        armorStand.remove();
        location.getBlock().setType(Material.AIR);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 15, 0.6, 0.6, 0.6);

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(powerup.getColor() + powerup.getName()));
    }

    public void despawn() {
        armorStand.remove();
        location.getBlock().setType(Material.AIR);
        location.getWorld().spawnParticle(Particle.CLOUD, location, 30, 0.3, 0.3, 0.3);
        location.getWorld().playSound(location, Sound.UI_TOAST_OUT, 1, 1);
        location.getWorld().playSound(location, Sound.ITEM_TRIDENT_HIT, 1, 1);
    }

    public void setDespawnable(Boolean despawnable) {
        this.despawnable = despawnable;
    }

    public Game getGame() {
        return game;
    }
    public ArmorStand getArmorStand() {
        return armorStand;
    }
    public Integer getCreatedAt() {
        return createdAt;
    }
    public Location getLocation() {
        return location;
    }
    public Boolean isDespawnable() {
        return despawnable;
    }
    public Powerup getPowerup() {
        return powerup;
    }

}
