package net.taccy.spleef.commands.subcommands;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.commands.SubCommand;
import net.taccy.spleef.game.Game;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlaySub extends SubCommand {

    private Spleef pl;

    public PlaySub(Spleef pl) {
        this.pl = pl;
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getPermission() {
        return "spleef.command.play";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void handle(CommandSender sender, ArrayList<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("console cannot use this command!"); // todo: config
            return;
        }

        Player p = (Player) sender;

        if (pl.gm.getGameFromPlayer(p) != null) {
            p.sendMessage("You are already in a game! You need to leave before joining a new game.");
            return;
        }

        Game game = pl.gm.getBestGame();

        if (game == null) {
            p.sendMessage("There aren't any available games at the moment.");
            return;
        }

        game.addPlayer(p);
    }
}
