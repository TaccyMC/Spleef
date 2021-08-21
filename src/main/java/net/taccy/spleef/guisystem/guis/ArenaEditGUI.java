package net.taccy.spleef.guisystem.guis;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.guisystem.GUI;
import net.taccy.spleef.guisystem.PlayerMenuUtility;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ArenaEditGUI extends GUI {

    private Spleef pl;

    public ArenaEditGUI(PlayerMenuUtility playerMenuUtility, Spleef pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Arenas > " + playerMenuUtility.getArena().getDisplayName();
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setMenuItems() {
        SpleefArena arena = playerMenuUtility.getArena();

        inv.setItem(10, new ItemBuilder(Material.LIME_CONCRETE)
                .setName(Colorize.color("&a&lChange Display Name"))
                .setLore(Arrays.asList("&fLeft click &ato change display name."))
                .toItemStack());
        inv.setItem(11, new ItemBuilder(Material.LIGHT_BLUE_CONCRETE)
                .setName(Colorize.color("&b&lSet Lobby Location"))
                .setLore(Arrays.asList("&fLeft click &bto set lobby location.", "&fPress Q &bto teleport."))
                .toItemStack());
        inv.setItem(12, new ItemBuilder(Material.YELLOW_CONCRETE)
                .setName(Colorize.color("&e&l" + (arena.isDisabled() ? "Set to Enabled" : "Set to Disabled")))
                .setLore(Arrays.asList("&fLeft click &eto toggle arena disabled status.",
                        "&7Currently: " + (arena.isDisabled() ? "&cdisabled" : "&aenabled")))
                .toItemStack());
        inv.setItem(14, new ItemBuilder(Material.BLUE_CONCRETE)
                .setName(Colorize.color("&9&lManage Spawn Locations"))
                .setLore(Arrays.asList("&fLeft click &9to manage spawn locations."))
                .toItemStack());
        inv.setItem(15, new ItemBuilder(Material.PINK_CONCRETE)
                .setName(Colorize.color("&d&lRandom Spawn"))
                .setLore(Arrays.asList("&fLeft click &dto teleport to a random spawn."))
                .toItemStack());
        inv.setItem(16, new ItemBuilder(Material.RED_CONCRETE)
                .setName(Colorize.color("&c&lDelete Arena"))
                .setLore(Arrays.asList("&fLeft click &cto delete arena."))
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
        SpleefArena arena = playerMenuUtility.getArena();

        if (e.getAction() == InventoryAction.DROP_ONE_SLOT) {
            if (item.getType() == Material.LIGHT_BLUE_CONCRETE) {
                p.teleport(arena.getLobby());
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(Colorize.color("&bTeleported to lobby location.")));
                return;
            }
        }

        switch (item.getType()) {
            case LIME_CONCRETE:
                new AnvilGUI.Builder()
                        .title("Set Display Name")
                        .item(new ItemStack(Material.PAPER))
                        .text(arena.getDisplayName())
                        .plugin(pl)
                        .onComplete((completedPlayer, string) -> {
                            if (!arena.getName().equalsIgnoreCase(string)) {
                                if (pl.am.getArenaFromString(string) != null) {
                                    return AnvilGUI.Response.text("This name is already taken!");
                                }
                            }

                            arena.setDisplayName(string);
                            p.sendMessage(Colorize.color("&a&lArena Edit GUI &7» &fSet &adisplay name &fto &7'" + string + "'"));
                            new ArenaEditGUI(playerMenuUtility, pl).open();
                            return AnvilGUI.Response.close();
                        }).open(p);
                break;
            case LIGHT_BLUE_CONCRETE:
                arena.setLobby(p.getLocation());
                p.sendMessage(Colorize.color("&b&lArena Edit GUI &7» &fSet &blobby location &fto current location."));
                break;
            case YELLOW_CONCRETE:
                arena.setDisabled(!arena.isDisabled());
                setMenuItems();
                break;
            case BLUE_CONCRETE:
                //new SpawnLocationsEditGUI(playerMenuUtility, pl).open();
                break;
            case RED_CONCRETE:
                new ConfirmGUI(playerMenuUtility).onComplete(aBoolean -> {
                    if (aBoolean) {
                        pl.am.deleteArena(arena);
                        playerMenuUtility.setArena(null);
                        new ArenaViewGUI(playerMenuUtility, pl).open();
                    } else {
                        new ArenaEditGUI(playerMenuUtility, pl).open();
                    }
                }).open();
                break;
            case OAK_SIGN:
                new ArenaViewGUI(playerMenuUtility, pl).open();
                playerMenuUtility.setArena(null);
                break;
        }
    }
}
