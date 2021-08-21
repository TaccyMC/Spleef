package net.taccy.spleef.game;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpleefPlayer {

    private Player player;
    private boolean alive = true;
    private int kills = 0;
    private int deaths = 0;
    private List<Block> broken = new ArrayList<>();
    private SpleefPlayer lastHitBy;
    private SpleefPlayer lastFellThrough;

    public Player getPlayer() {
        return player;
    }
    public int getKills() {
        return kills;
    }
    public int getDeaths() {
        return deaths;
    }
    public List<Block> getBroken() {
        return broken;
    }
    public boolean isAlive() {
        return alive;
    }
    public SpleefPlayer getLastHitBy() {
        return lastHitBy;
    }
    public SpleefPlayer getLastFellThrough() {
        return lastFellThrough;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setKills(int kills) {
        this.kills = kills;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public void setLastHitBy(SpleefPlayer lastHitBy) {
        this.lastHitBy = lastHitBy;
    }
    public void setLastFellThrough(SpleefPlayer lastFellThrough) {
        this.lastFellThrough = lastFellThrough;
    }

}
