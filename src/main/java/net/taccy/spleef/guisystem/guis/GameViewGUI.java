package net.taccy.spleef.guisystem.guis;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.guisystem.GUI;
import net.taccy.spleef.guisystem.PlayerMenuUtility;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GameViewGUI extends GUI {

    private Spleef pl;

    public GameViewGUI(PlayerMenuUtility playerMenuUtility, Spleef pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Overview > Games";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setMenuItems() {
        int i = 10;
        for (SpleefArena a : Spleef.arenas) {
            Game game = a.getGame();

            int itemCount = 1;

            if (game.getPlayers().size() > 1) {
                itemCount = game.getPlayers().size();
            }

            inv.setItem(i, new ItemBuilder(Material.COMPASS, itemCount)
                    .setName(ChatColor.LIGHT_PURPLE + a.getDisplayName())
                    .setLore(Arrays.asList("&fLeft click &5edit.",
                            " ",
                            "&5Players: &7" + game.getPlayers().size(),
                            "&5Arena: &7" + a.getName(),
                            "&5State: &7" + game.getState().getType().getDisplayName()))
                    .toItemStack());

            i++;
            if ((i + 1) % 9 == 0)
                i = i + 2;
        }
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
            case COMPASS:
                String name = Colorize.stripColor(item.getItemMeta().getDisplayName());
                SpleefArena arena = pl.am.getArenaFromString(name);
                Game game = arena.getGame();

                playerMenuUtility.setGame(game);

                new GameEditGUI(playerMenuUtility, pl).open();
                break;
            case OAK_SIGN:
                new GeneralGUI(playerMenuUtility, pl).open();
        }
    }
}
