package net.taccy.spleef.powerup;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JumpPowerup extends Powerup {

    @Override
    public String getName() {
        return "Jump";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GREEN;
    }

    @Override
    public Material getItem() {
        return Material.SLIME_BLOCK;
    }

    @Override
    public Material getBlock() {
        return Material.LIME_STAINED_GLASS;
    }

    @Override
    public void execute(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 400, 3));
    }

}
