package net.taccy.spleef.guisystem.guis;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.guisystem.GUI;
import net.taccy.spleef.guisystem.PlayerMenuUtility;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.ItemBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GameEditGUI extends GUI {

    private Spleef pl;

    public GameEditGUI(PlayerMenuUtility playerMenuUtility, Spleef pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Games > " + playerMenuUtility.getGame().getArena().getDisplayName();
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setMenuItems() {
        inv.setItem(11, new ItemBuilder(Material.ARROW)
                .setName(Colorize.color("&a&lJoin Game"))
                .setLore(Arrays.asList("&fLeft click &ato join this game"))
                .toItemStack());
        inv.setItem(12, new ItemBuilder(Material.REDSTONE)
                .setName(Colorize.color("&c&lAdd A Player"))
                .setLore(Arrays.asList("&fLeft click &cto add a player to this game"))
                .toItemStack());
        inv.setItem(13, new ItemBuilder(Material.LEVER)
                .setName(Colorize.color("&6&lGame Controls"))
                .setLore(Arrays.asList("&fLeft click &6to open game controls"))
                .toItemStack());
        inv.setItem(14, new ItemBuilder(Material.DISPENSER)
                .setName(Colorize.color("&e&lView Players"))
                .setLore(Arrays.asList("&fLeft click &eto view players in this game"))
                .toItemStack());
        inv.setItem(15, new ItemBuilder(Material.COMPASS)
                .setName(Colorize.color("&9&lView Arena"))
                .setLore(Arrays.asList("&fLeft click &9to view games arena"))
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
            case ARROW:
                playerMenuUtility.getGame().addPlayer(p);
                break;
            case REDSTONE:
                new AnvilGUI.Builder()
                        .title("Type Player Name")
                        .item(new ItemStack(Material.PAPER))
                        .text(p.getName())
                        .plugin(pl)
                        .onComplete((completedPlayer, string) -> {
                            if (Bukkit.getPlayer(string) == null) {
                                return AnvilGUI.Response.text("Doesn't exist!");
                            }

                            playerMenuUtility.getGame().addPlayer(Bukkit.getPlayer(string));
                            pl.su.success(completedPlayer.getLocation());
                            return AnvilGUI.Response.close();
                        }).open(p);
                break;
            case LEVER:
                new GameControlsGUI(playerMenuUtility, pl).open();
                break;
            case DISPENSER:
                new BowPlayerViewGUI(playerMenuUtility, pl).open();
                break;
            case COMPASS:
                playerMenuUtility.setArena(playerMenuUtility.getGame().getArena());
                new ArenaEditGUI(playerMenuUtility, pl).open();
                break;
            case OAK_SIGN:
                new GameViewGUI(playerMenuUtility, pl).open();
                playerMenuUtility.setGame(null);
                break;
        }
    }

}
