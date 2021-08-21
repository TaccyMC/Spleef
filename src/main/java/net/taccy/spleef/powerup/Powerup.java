package net.taccy.spleef.powerup;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class Powerup {

    public abstract String getName();
    public abstract ChatColor getColor();
    public abstract Material getItem();
    public abstract Material getBlock();

    public abstract void execute(Player p);

}
