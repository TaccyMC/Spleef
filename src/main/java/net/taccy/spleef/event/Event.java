package net.taccy.spleef.event;

import net.taccy.spleef.game.Game;
import org.bukkit.ChatColor;

public abstract class Event {

    public abstract String getName();
    public abstract ChatColor getColor();
    public abstract Game getGame();
    public abstract Boolean isActive();

    public abstract void runTick(Integer timeSinceExecution);

}
