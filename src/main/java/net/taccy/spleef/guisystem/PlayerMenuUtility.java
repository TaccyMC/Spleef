package net.taccy.spleef.guisystem;

import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.game.SpleefPlayer;
import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private Player owner;
    private SpleefArena arena;
    private Game game;
    private SpleefPlayer spleefPlayerTarget;

    public PlayerMenuUtility(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
    public SpleefArena getArena() {
        return arena;
    }
    public Game getGame() {
        return game;
    }
    public SpleefPlayer getBPTarget() {
        return spleefPlayerTarget;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public void setArena(SpleefArena arena) {
        this.arena = arena;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public void setBPTarget(SpleefPlayer spleefPlayerTarget) {
        this.spleefPlayerTarget = spleefPlayerTarget;
    }
}
