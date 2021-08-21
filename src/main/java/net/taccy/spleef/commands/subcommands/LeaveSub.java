package net.taccy.spleef.commands.subcommands;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.commands.SubCommand;
import net.taccy.spleef.game.Game;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LeaveSub extends SubCommand {

    private Spleef pl;

    public LeaveSub(Spleef pl) {
        this.pl = pl;
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getPermission() {
        return "spleef.command.leave";
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
        Game game = pl.gm.getGameFromPlayer(p);

        if (game == null) {
            p.sendMessage("You aren't already in a game!");
            return;
        }

        game.removePlayer(p);
        p.sendMessage("You left game in arena " + game.getArena().getDisplayName());
    }
}
