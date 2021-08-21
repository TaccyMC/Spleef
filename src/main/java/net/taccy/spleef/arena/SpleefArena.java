package net.taccy.spleef.arena;

import net.taccy.spleef.game.Game;
import org.bukkit.Location;

public class SpleefArena {

    private String name;
    private String displayName;
    private boolean disabled = false;

    private Location lobby;
    private Location center;

    private Game game;

    public String getName() {
        return name;
    }
    public Location getLobby() {
        return lobby;
    }
    public Location getCenter() {
        return center;
    }
    public Game getGame() {
        return game;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }
    public void setCenter(Location center) {
        this.center = center;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public boolean isDisabled() {
        return disabled;
    }

}
