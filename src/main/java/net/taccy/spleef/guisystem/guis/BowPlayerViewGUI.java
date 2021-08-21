package net.taccy.spleef.guisystem.guis;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.game.SpleefPlayer;
import net.taccy.spleef.guisystem.GUI;
import net.taccy.spleef.guisystem.PlayerMenuUtility;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class BowPlayerViewGUI extends GUI {

    private Spleef pl;

    public BowPlayerViewGUI(PlayerMenuUtility playerMenuUtility, Spleef pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return playerMenuUtility.getGame().getArena().getDisplayName() + " > Players";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setMenuItems() {
        int i = 10;
        for (SpleefPlayer bp : playerMenuUtility.getGame().getPlayers()) {
            inv.setItem(i, getPlayerHead(bp.getPlayer()));

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
            case PLAYER_HEAD:
                String name = e.getCurrentItem().getItemMeta().getDisplayName();
                Player selected = Bukkit.getPlayer(ChatColor.stripColor(name));
                if (selected != null) {
                    playerMenuUtility.setBPTarget(playerMenuUtility.getGame().getSpleefPlayerFromPlayer(selected));
                    new GameEditPlayerGUI(playerMenuUtility, pl).open();
                }
                break;
            case OAK_SIGN:
                new GameEditGUI(playerMenuUtility, pl).open();
                break;
        }
    }

    @SuppressWarnings("deprication")
    public ItemStack getPlayerHead(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + player.getName());
        meta.setLore(Arrays.asList(Colorize.color("&eLeft click &fto view")));
        item.setItemMeta(meta);

        return item;
    }

}
