package net.taccy.spleef.managers;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ArenaSetupWizardManager implements Listener {

    private Spleef pl;

    public ArenaSetupWizardManager(Spleef pl) {
        this.pl = pl;
    }

    private HashMap<Player, SpleefArena> inWizard = new HashMap<>();

    private final String SET_ARENA_LOBBY_ITEM_NAME = Colorize.color("&bSet Arena Lobby &7(Right click)");
    private final String SET_ARENA_NAME_ITEM_NAME = Colorize.color("&dSet Arena Name &7(Right click)");
    private final String SET_ARENA_CENTER_ITEM_NAME = Colorize.color("&eSet Arena Center &7(Right click)");
    private final String SAVE_ARENA_ITEM_NAME = Colorize.color("&aSave Arena &7(Right click)");
    private final String CANCEL_ARENA_CREATION_ITEM_NAME = Colorize.color("&cCancel Arena Creation &7(Right click)");

    public void startWizard(Player p) {
        SpleefArena arena = new SpleefArena();

        inWizard.put(p, arena);

        p.setGameMode(GameMode.CREATIVE);
        p.getInventory().clear(); // todo: restore inventory content
        p.sendMessage(Colorize.color("&7&lArena Setup Wizard &8» &fStarted &7wizard."));
        p.sendTitle(Colorize.color("&fSetup &7Wizard"), ChatColor.DARK_GRAY + "Use items to configure arena!", 10, 50, 10);

        pl.su.joyful(p.getLocation());

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(Colorize.color("&aArena Setup Wizard")));

        Inventory inv = p.getInventory();
        inv.setItem(2, new ItemBuilder(Material.LIGHT_BLUE_CONCRETE).setName(SET_ARENA_LOBBY_ITEM_NAME).toItemStack());
        inv.setItem(3, new ItemBuilder(Material.PINK_CONCRETE).setName(SET_ARENA_NAME_ITEM_NAME).toItemStack());
        inv.setItem(4, new ItemBuilder(Material.YELLOW_CONCRETE).setName(SET_ARENA_CENTER_ITEM_NAME).toItemStack());
        inv.setItem(5, new ItemBuilder(Material.LIME_CONCRETE).setName(SAVE_ARENA_ITEM_NAME).toItemStack());
        inv.setItem(6, new ItemBuilder(Material.RED_CONCRETE).setName(CANCEL_ARENA_CREATION_ITEM_NAME).toItemStack());

    }

    public void endWizard(Player p) {
        inWizard.remove(p);
        pl.gm.makeGames();
        p.getInventory().clear();
    }

    public boolean inWizard(Player p) {
        return inWizard.containsKey(p);
    }

    // todo: all config messages

    @EventHandler
    public void onItemInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!inWizard(p)) {
            return;
        }
        if (!e.hasItem()) {
            return;
        }
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!e.getItem().hasItemMeta()) {
            return;
        }

        String name = e.getItem().getItemMeta().getDisplayName();
        SpleefArena arena = inWizard.get(p);

        if (name.equals(SET_ARENA_LOBBY_ITEM_NAME)) {
            e.setCancelled(true);
            Location loc = p.getLocation();
            arena.setLobby(loc);
            p.sendMessage(Colorize.color("&b&lArena Setup Wizard &7» &fSet &blobby location!"));
            pl.su.success(p.getLocation());

        } else if (name.equals(SET_ARENA_NAME_ITEM_NAME)) {
            e.setCancelled(true);
            pl.su.selected(p.getLocation());
            new AnvilGUI.Builder()
                    .title("Set Arena Name")
                    .item(new ItemStack(Material.PAPER))
                    .plugin(pl)
                    .onComplete((completedPlayer, string) -> {
                if (pl.am.getArenaFromString(string) != null) {
                    return AnvilGUI.Response.text("This name is already taken!");
                }

                arena.setDisplayName(string);
                String convertedMessage = string.replace(" ", "_").toLowerCase();
                arena.setName(convertedMessage);
                pl.su.success(p.getLocation());
                p.sendMessage(Colorize.color("&d&lArena Setup Wizard &7» &fSet &darena name!"));
                return AnvilGUI.Response.close();
            }).open(p);
        } else if (name.equals(SET_ARENA_CENTER_ITEM_NAME)) {
            e.setCancelled(true);
            Location loc = p.getLocation();
            arena.setCenter(loc);
            p.sendMessage(Colorize.color("&e&lArena Setup Wizard &7» &fSet &ecenter location!"));
            pl.su.success(p.getLocation());

        } else if (name.equals(SAVE_ARENA_ITEM_NAME)) {
            e.setCancelled(true);
            if (arena.getName() == null) {
                p.sendMessage(Colorize.color("&c&lArena Setup Wizard &7» &fPlease &cset a name&f before saving!"));
                p.sendTitle(Colorize.color("&cIncomplete Setup!"), ChatColor.GRAY + "Please set a name!", 10, 30, 10);
                pl.su.error(p.getLocation());
                return;
            }
            if (arena.getLobby() == null) {
                p.sendMessage(Colorize.color("&c&lArena Setup Wizard &7» &fPlease &cset a lobby location e&f before saving!"));
                p.sendTitle(Colorize.color("&cIncomplete Setup!"), ChatColor.GRAY + "Please set a lobby location!", 10, 30, 10);
                pl.su.error(p.getLocation());
                return;
            }
            /*
            if (arena.getSpawns() == null || arena.getSpawns().isEmpty()) {
                p.sendMessage(Colorize.color("&c&lArena Setup Wizard &7» &fPlease &cadd spawn locations&f before saving!"));
                p.sendTitle(Colorize.color("&cIncomplete Setup!"), ChatColor.GRAY + "Please add one or more spawn locations!", 10, 30, 10);
                pl.su.error(p.getLocation());
                return;
            }
             */

            Spleef.arenas.add(arena);
            p.sendMessage(Colorize.color("&a&lArena Setup Wizard &7» &fArena &asaved!"));
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(Colorize.color("&a&lSaved Arena!")));
            pl.su.success2(p.getLocation());
            p.sendMessage(Colorize.color("&7&lArena Setup Wizard &8» &fCompleted &7wizard."));
            endWizard(p);

        } else if (name.equals(CANCEL_ARENA_CREATION_ITEM_NAME)) {
            e.setCancelled(true);
            p.sendMessage(Colorize.color("&c&lArena Setup Wizard &7» &fCancelled &carena creation!"));
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(Colorize.color("&c&lCancelled Arena Creation")));
            pl.su.back(p.getLocation());
            endWizard(p);
        }


    }

}
