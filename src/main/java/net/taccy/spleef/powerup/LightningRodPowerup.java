package net.taccy.spleef.powerup;

import net.taccy.spleef.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class LightningRodPowerup extends Powerup {

    @Override
    public String getName() {
        return "Lightning Rod";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public Material getItem() {
        return Material.END_ROD;
    }

    @Override
    public Material getBlock() {
        return Material.YELLOW_STAINED_GLASS;
    }

    @Override
    public void execute(Player p) {
        p.getInventory().addItem(new ItemBuilder(Material.END_ROD)
                .setName(ChatColor.YELLOW + "Lightning Rod")
                .addEnchant(Enchantment.DAMAGE_ALL, 1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .toItemStack());
    }

}
