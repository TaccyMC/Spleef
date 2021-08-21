package net.taccy.spleef.guisystem.guis;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.game.states.ActiveGameState;
import net.taccy.spleef.game.states.CountdownGameState;
import net.taccy.spleef.game.states.GracePeriodState;
import net.taccy.spleef.guisystem.GUI;
import net.taccy.spleef.guisystem.PlayerMenuUtility;
import net.taccy.spleef.utils.Colorize;
import net.taccy.spleef.utils.GenUtils;
import net.taccy.spleef.utils.ItemBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GameControlsGUI extends GUI {

    private Spleef pl;

    public GameControlsGUI(PlayerMenuUtility playerMenuUtility, Spleef pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return playerMenuUtility.getGame().getArena().getDisplayName() + " > Controls";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setMenuItems() {
        inv.setItem(11, new ItemBuilder(Material.LIGHT_BLUE_CONCRETE)
                .setName(Colorize.color("&bStart Countdowm"))
                .setLore(Arrays.asList("&fLeft click &bto start countdown."))
                .toItemStack());
        inv.setItem(12, new ItemBuilder(Material.PINK_CONCRETE)
                .setName(Colorize.color("&dStart Grace Period"))
                .setLore(Arrays.asList("&fLeft click &dto start the game."))
                .toItemStack());
        inv.setItem(13, new ItemBuilder(Material.BLUE_CONCRETE)
                .setName(Colorize.color("&9Start Game"))
                .setLore(Arrays.asList("&fLeft click &9to start the game"))
                .toItemStack());
        inv.setItem(14, (playerMenuUtility.getGame().isPaused() ?
                new ItemBuilder(Material.GREEN_CONCRETE)
                .setName(Colorize.color("&2Resume game"))
                .setLore(Arrays.asList("&fLeft click &2to resume the game."))
                .toItemStack() :
                new ItemBuilder(Material.YELLOW_CONCRETE)
                .setName(Colorize.color("&6Pause Game"))
                .setLore(Arrays.asList("&fLeft click &6to pause the game."))
                .toItemStack()));
        inv.setItem(15, new ItemBuilder(Material.LIME_CONCRETE)
                .setName(Colorize.color("&ASet Game Time"))
                .setLore(Arrays.asList("&fLeft click &ato set time remaining."))
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
            case LIGHT_BLUE_CONCRETE:
                playerMenuUtility.getGame().setGameState(new CountdownGameState(playerMenuUtility.getGame(), pl, 15, "due to &6admin controls."));
                break;
            case PINK_CONCRETE:
                playerMenuUtility.getGame().setGameState(new GracePeriodState(playerMenuUtility.getGame(), pl));
                break;
            case BLUE_CONCRETE:
                playerMenuUtility.getGame().setGameState(new ActiveGameState(playerMenuUtility.getGame(), pl));
                break;
            case GREEN_CONCRETE:
                playerMenuUtility.getGame().resume();
                break;
            case YELLOW_CONCRETE:
                playerMenuUtility.getGame().pause();
                break;
            case LIME_CONCRETE:
                new AnvilGUI.Builder()
                        .title("Set Time Remaining")
                        .item(new ItemStack(Material.PAPER))
                        .text(String.valueOf(playerMenuUtility.getGame().getTimeLeft()))
                        .plugin(pl)
                        .onComplete((completedPlayer, string) -> {
                            if (!GenUtils.isNumeric(string)) {
                                if (string.contains("m") && string.split("m").length == 2) {
                                    String[] split = string.split("m");
                                    String minutes = split[0].replace("m", "");
                                    minutes = minutes.replace(" ", "");

                                    String seconds = split[1].replace("s", "");
                                    seconds = seconds.replace(" ", "");

                                    if (GenUtils.isNumeric(minutes) && GenUtils.isNumeric(seconds)) {
                                        int mins = Integer.parseInt(minutes);
                                        int secs = Integer.parseInt(seconds);

                                        Integer total = (mins * 60) + secs;

                                        playerMenuUtility.getGame().setTimeLeft(total);
                                        pl.su.success(completedPlayer.getLocation());
                                        return AnvilGUI.Response.close();
                                    } else {
                                        return AnvilGUI.Response.text("That is not a valid input!");
                                    }
                                } else {
                                    return AnvilGUI.Response.text("That is not a valid input!");
                                }
                            }

                            playerMenuUtility.getGame().setTimeLeft(Integer.valueOf(string));
                            pl.su.success(completedPlayer.getLocation());
                            return AnvilGUI.Response.close();
                        }).open(p);
                break;
            case OAK_SIGN:
                new GameEditGUI(playerMenuUtility, pl).open();
                break;

        }
        setMenuItems();
    }

}
