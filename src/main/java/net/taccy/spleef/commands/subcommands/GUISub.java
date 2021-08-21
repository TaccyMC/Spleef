package net.taccy.spleef.commands.subcommands;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.commands.SubCommand;
import net.taccy.spleef.game.Game;
import net.taccy.spleef.guisystem.guis.ArenaEditGUI;
import net.taccy.spleef.guisystem.guis.GameEditGUI;
import net.taccy.spleef.guisystem.guis.GeneralGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GUISub extends SubCommand {

    private Spleef pl;

    public GUISub(Spleef pl) {
        this.pl = pl;
    }

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public String getPermission() {
        return "spleef.gui";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void handle(CommandSender sender, ArrayList<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("console cannot use gui command");
            return;
        }

        Player p = (Player) sender;
        Game game = pl.gm.getGameFromPlayer(p);

        if (game != null) {
            Spleef.getPlayerMenuUtility(p).setGame(game);
            new GameEditGUI(Spleef.getPlayerMenuUtility(p), pl).open();
            return;
        }
        if (Spleef.getPlayerMenuUtility(p).getArena() == null) {
            new GeneralGUI(Spleef.getPlayerMenuUtility(p), pl).open();
        } else {
            new ArenaEditGUI(Spleef.getPlayerMenuUtility(p), pl).open();
        }

    }

}
