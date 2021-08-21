package net.taccy.spleef.utils;

import net.taccy.spleef.Spleef;
import org.bukkit.Location;
import org.bukkit.Sound;

public class SoundUtil {

    private Spleef pl;

    public SoundUtil(Spleef pl) {
        this.pl = pl;
    }

    public void error(Location loc) {
        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_BIT, 1, -3);
    }

    public void success(Location loc) {
        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
    }
    public void success2(Location loc) {
        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_COW_BELL,1 , 1);
    }
    public void success3(Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP,1 , 1);
    }
    public void success4(Location loc) {
        loc.getWorld().playSound(loc, Sound.ITEM_LODESTONE_COMPASS_LOCK, 1, 1);
        loc.getWorld().playSound(loc, Sound.UI_LOOM_TAKE_RESULT, 1, 1);
    }

    public void selected(Location loc) {
        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 2);
    }

    public void joyful(Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    public void back(Location loc) {
        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_SNARE, 1, -3);
    }

    public void launch(Location loc) {
        loc.getWorld().playSound(loc, Sound.BLOCK_COMPOSTER_FILL, 1, 1);
    }

    public void count(Location loc) {
        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 5);
        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1);
    }

    public void release(Location loc) {
        loc.getWorld().playSound(loc, Sound.UI_BUTTON_CLICK, 1, 15);
    }

}
