package net.taccy.spleef.event.events;

import net.taccy.spleef.event.Event;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.game.states.ActiveGameState;
import net.taccy.spleef.powerup.PowerupBlock;
import org.bukkit.ChatColor;

public class PowerupFrenzy extends Event {

    private Game game;
    private Boolean active = false;

    public PowerupFrenzy(Game game) {
        this.game = game;
    }

    @Override
    public String getName() {
        return "Powerup Frenzy";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Boolean isActive() {
        return active;
    }

    @Override
    public void runTick(Integer timeSinceExecution) {
        if (timeSinceExecution < 30) {
            for (int i = 0; i < 15; i++) {
                game.getPlugin().pm.spawnPowerup(game);
            }
        }

        if (timeSinceExecution == 0) {
            active = true;
            if (game.getState() instanceof ActiveGameState) {
                for (PowerupBlock pb : game.getPowerups()) {
                    pb.setDespawnable(false);
                }
            }
        }

        if (timeSinceExecution >= 30) {
            active = false;
            if (game.getState() instanceof ActiveGameState) {
                for (PowerupBlock pb : game.getPowerups()) {
                    pb.setDespawnable(true);
                }
            }
        }
    }

}
