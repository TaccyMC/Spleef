package net.taccy.spleef.powerup;

import net.taccy.spleef.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class PearlPowerup extends Powerup {
    
    @Override
    public String getName() {
        return "Rideable Pearl";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_AQUA;
    }

    @Override
    public Material getItem() {
        return Material.ENDER_PEARL;
    }

    @Override
    public Material getBlock() {
        return Material.CYAN_STAINED_GLASS;
    }

    @Override
    public void execute(Player p) {
        p.getInventory().addItem(new ItemBuilder(Material.ENDER_PEARL)
                .setName(ChatColor.DARK_AQUA + "Rideable Pearl")
                .addEnchant(Enchantment.DAMAGE_ALL, 1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .toItemStack());
    }

}
