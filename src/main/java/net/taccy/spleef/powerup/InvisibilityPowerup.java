package net.taccy.spleef.powerup;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InvisibilityPowerup extends Powerup {

    @Override
    public String getName() {
        return "Invisibility";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public Material getItem() {
        return Material.GLOWSTONE;
    }

    @Override
    public Material getBlock() {
        return Material.ORANGE_STAINED_GLASS;
    }

    @Override
    public void execute(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 0));
    }

}
