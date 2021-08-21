package net.taccy.spleef.guisystem.guis;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.game.SpleefPlayer;
import net.taccy.spleef.game.DeathReason;
import net.taccy.spleef.guisystem.GUI;
import net.taccy.spleef.guisystem.PlayerMenuUtility;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.GenUtils;
import net.taccy.spleef.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GameEditPlayerGUI extends GUI {

    private Spleef pl;

    public GameEditPlayerGUI(PlayerMenuUtility playerMenuUtility, Spleef pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Players > " + playerMenuUtility.getBPTarget().getPlayer().getName();
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setMenuItems() {
        SpleefPlayer target = playerMenuUtility.getBPTarget();

        inv.setItem(11, new ItemBuilder(Material.GOLDEN_SWORD)
                .setName(Colorize.color("&e&lKills"))
                .setLore(Arrays.asList(
                        "&fLeft click &eto set the players kills",
                        "&fPress Q &eto kill the player",
                        " ",
                        "&7Currently: &e" + target.getKills()))
                .addFlags(Arrays.asList(ItemFlag.HIDE_ATTRIBUTES))
                .toItemStack());
        inv.setItem(12, new ItemBuilder(Material.WITHER_SKELETON_SKULL)
                .setName(Colorize.color("&c&lDeaths"))
                .setLore(Arrays.asList(
                        "&fLeft click &cto set the players deaths",
                        "&fPress Q &cto respawn the player",
                        " ",
                        "&7Currently: &c" + target.getDeaths()))
                .toItemStack());
        inv.setItem(13, new ItemBuilder(Material.ITEM_FRAME)
                .setName(Colorize.color("&b&lPlayer Managment"))
                .setLore(Arrays.asList(
                        "&fLeft click &bto prepare player",
                        "&fPress Q &bto set items"))
                .toItemStack());
        inv.setItem(14, new ItemBuilder(Material.FEATHER)
                .setName(Colorize.color("&a&lKick Player"))
                .setLore(Arrays.asList(
                        "&fLeft click &ato kick this player"))
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
        Game game = playerMenuUtility.getGame();
        SpleefPlayer target = playerMenuUtility.getBPTarget();

        if (e.getAction() == InventoryAction.DROP_ONE_SLOT) {
            switch (item.getType()) {
                case GOLDEN_SWORD:
                    game.kill(target, null, DeathReason.NONE);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(Colorize.color("&aPlayer has been killed.")));
                    pl.su.success(p.getLocation());
                    break;
                case WITHER_SKELETON_SKULL:
                    game.respawn(target);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(Colorize.color("&aPlayer has been respawned.")));
                    pl.su.success(p.getLocation());
                    break;
                case ITEM_FRAME:
                    game.giveItems(target.getPlayer());
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacyText(Colorize.color("&aItems set for player.")));
                    pl.su.success(p.getLocation());
                    break;
            }
            return;
        }

        switch (item.getType()) {
            case GOLDEN_SWORD:
                new AnvilGUI.Builder()
                        .title("Set Kills")
                        .item(new ItemStack(Material.PAPER))
                        .text(String.valueOf(target.getKills()))
                        .plugin(pl)
                        .onComplete((completedPlayer, string) -> {
                            if (!GenUtils.isNumeric(string)) {
                                return AnvilGUI.Response.text("Invalid!");
                            }

                            target.setKills(Integer.parseInt(string));
                            pl.su.success(completedPlayer.getLocation());
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(Colorize.color("&aSet kills for player.")));
                            game.updateScoreboards();
                            new GameEditPlayerGUI(playerMenuUtility, pl).open();
                            return AnvilGUI.Response.close();
                        }).open(p);
                break;
            case WITHER_SKELETON_SKULL:
                new AnvilGUI.Builder()
                        .title("Set Deaths")
                        .item(new ItemStack(Material.PAPER))
                        .text(String.valueOf(target.getDeaths()))
                        .plugin(pl)
                        .onComplete((completedPlayer, string) -> {
                            if (!GenUtils.isNumeric(string)) {
                                return AnvilGUI.Response.text("Invalid!");
                            }

                            target.setDeaths(Integer.parseInt(string));
                            pl.su.success(completedPlayer.getLocation());
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(Colorize.color("&aSet deaths for player.")));
                            game.updateScoreboards();
                            new GameEditPlayerGUI(playerMenuUtility, pl).open();
                            return AnvilGUI.Response.close();
                        }).open(p);
                break;
            case ITEM_FRAME:
                game.preparePlayer(target.getPlayer());
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(Colorize.color("&aPrepared player.")));
                pl.su.success(p.getLocation());
                break;
            case FEATHER:
                game.removePlayer(target.getPlayer());
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(Colorize.color("&aRemoved player from game.")));
                pl.su.success(p.getLocation());
                new BowPlayerViewGUI(playerMenuUtility, pl).open();
                break;
            case OAK_SIGN:
                new BowPlayerViewGUI(playerMenuUtility, pl).open();
                break;
        }
    }
}
