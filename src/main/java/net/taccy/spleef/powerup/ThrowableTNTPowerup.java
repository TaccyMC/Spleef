package net.taccy.spleef.powerup;

import net.taccy.spleef.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class ThrowableTNTPowerup extends Powerup {

    @Override
    public String getName() {
        return "Throwable TNT";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

    @Override
    public Material getItem() {
        return Material.TNT;
    }

    @Override
    public Material getBlock() {
        return Material.RED_STAINED_GLASS;
    }

    @Override
    public void execute(Player p) {
        p.getInventory().addItem(new ItemBuilder(Material.TNT)
                .setName(ChatColor.RED + "Throwable TNT")
                .addEnchant(Enchantment.DAMAGE_ALL, 1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .toItemStack());
    }

}
