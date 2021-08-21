package net.taccy.spleef.guisystem.guis;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.guisystem.GUI;
import net.taccy.spleef.guisystem.PlayerMenuUtility;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GeneralGUI extends GUI {

    private Spleef pl;

    public GeneralGUI(PlayerMenuUtility playerMenuUtility, Spleef pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Overview";
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems() {
        inv.setItem(21, new ItemBuilder(Material.BOOKSHELF).setName(Colorize.color("&a&lArenas"))
                .setLore(Arrays.asList(Colorize.color("&fLeft click &ato view arenas!"))).toItemStack());
        inv.setItem(23, new ItemBuilder(Material.NOTE_BLOCK).setName(Colorize.color("&d&lGames"))
                .setLore(Arrays.asList(Colorize.color("&fLeft click &dto view games!"))).toItemStack());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        switch (item.getType()) {
            case BOOKSHELF:
                new ArenaViewGUI(playerMenuUtility, pl).open();
                break;
            case DISPENSER:
                new BowPlayerViewGUI(playerMenuUtility, pl).open();
                break;
            case NOTE_BLOCK:
                new GameViewGUI(playerMenuUtility, pl).open();
                break;
        }

    }
}
