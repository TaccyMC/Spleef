package net.taccy.spleef.event.events;

import net.taccy.spleef.event.Event;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.game.SpleefPlayer;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlowingEvent extends Event {

    private Game game;
    private Boolean active = false;

    public GlowingEvent(Game game) {
        this.game = game;
    }

    @Override
    public String getName() {
        return "Glowing";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.WHITE;
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
        if (timeSinceExecution == 0) {
            active = true;
            for (SpleefPlayer lp : game.getPlayers()) {
                lp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false, false));
            }
        }

        if (timeSinceExecution >= 30) {
            active = false;
            for (SpleefPlayer lp : game.getPlayers()) {
                lp.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
            }
        }
    }

}
