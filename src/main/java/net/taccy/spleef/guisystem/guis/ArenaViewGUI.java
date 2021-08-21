package net.taccy.spleef.guisystem.guis;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.guisystem.GUI;
import net.taccy.spleef.guisystem.PlayerMenuUtility;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ArenaViewGUI extends GUI {

    private Spleef pl;

    public ArenaViewGUI(PlayerMenuUtility playerMenuUtility, Spleef pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Overview > Arenas";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setMenuItems() {
        int i = 10;
        for (SpleefArena a : Spleef.arenas) {
            inv.setItem(i, new ItemBuilder(Material.BOOK)
                    .setName((a.isDisabled() ? Colorize.color("&c&m") : Colorize.color("&b")) + a.getDisplayName())
                    .setLore(Arrays.asList("&fLeft click &3edit.",
                            " ",
                            "&3Name: &7" + a.getName(),
                            "&3World: &7" + a.getLobby().getWorld().getName(),
                            "&3Status: &7" + (a.isDisabled() ? "disabled" : "enabled")))
                    .toItemStack());

            i++;
            if ((i + 1) % 9 == 0)
                i = i + 2;
        }
        inv.setItem(49, new ItemBuilder(Material.STICK)
                .setName(Colorize.color("&7&lAdd New Arena"))
                .setLore(Arrays.asList("&fLeft click &7to start wizard."))
                .toItemStack());
        inv.setItem(getSlots() - 1, new ItemBuilder(Material.OAK_SIGN)
                .setName(Colorize.color("&7Back"))
                .setLore(Arrays.asList("&fLeft click &7to go back."))
                .toItemStack());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        switch (item.getType()) {
            case BOOK:
                String name = Colorize.stripColor(item.getItemMeta().getDisplayName());
                SpleefArena arena = pl.am.getArenaFromString(name);

                if (arena == null) {
                    return;
                }

                playerMenuUtility.setArena(arena);
                new ArenaEditGUI(playerMenuUtility, pl).open();
                break;
            case STICK:
                p.closeInventory();
                pl.aswm.startWizard(p);
                break;
            case OAK_SIGN:
                new GeneralGUI(playerMenuUtility, pl).open();
        }
    }

}
