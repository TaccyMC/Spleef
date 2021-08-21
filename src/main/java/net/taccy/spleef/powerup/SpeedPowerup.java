package net.taccy.spleef.powerup;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPowerup extends Powerup {

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.AQUA;
    }

    @Override
    public Material getItem() {
        return Material.DIAMOND_BOOTS;
    }

    @Override
    public Material getBlock() {
        return Material.CYAN_STAINED_GLASS;
    }

    @Override
    public void execute(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 2));
    }

}
