package net.taccy.spleef.game;

import net.taccy.spleef.Spleef;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class GameState implements Listener {

    public void onEnable(Spleef pl) {
        pl.getServer().getPluginManager().registerEvents(this, pl);
    }

    public void onDisable(Spleef pl) {
        HandlerList.unregisterAll(this);
    }

    public abstract void handleTick();
    public abstract GameStateType getType();

}
