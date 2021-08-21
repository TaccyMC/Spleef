package net.taccy.spleef.commands.subcommands;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.commands.SubCommand;
import net.taccy.spleef.utils.Colorize;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArenaSub extends SubCommand {

    private Spleef pl;

    public ArenaSub(Spleef pl) {
        this.pl = pl;
    }

    @Override
    public String getName() {
        return "arena";
    }

    @Override
    public String getPermission() {
        return "spleef.command.arena";
    }

    @Override
    public List<String> getSubCommands() {
        return Arrays.asList("create", "list", "delete", "powervis");
    }

    @Override
    public void handle(CommandSender sender, ArrayList<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("console cannot use this command!"); // todo: config
            return;
        }

        Player p = (Player) sender;

        if (args.size() == 0) {
            return;
        }

        String subcommand = args.get(0);

        if (subcommand.equalsIgnoreCase("create")) {
            pl.aswm.startWizard(p);
        }

        if (subcommand.equalsIgnoreCase("list")) {
            for (SpleefArena a : Spleef.arenas) {
                p.sendMessage(Colorize.color("&6Name: &f'" + a.getName() + "' &7| &f'" + a.getDisplayName() + "'"));
            }
        }

        if (subcommand.equalsIgnoreCase("delete")) {
            if (args.size() != 2) {
                p.sendMessage("/spleef arena delete <name>");
                pl.su.error(p.getLocation());
                return;
            }

            SpleefArena arena = pl.am.getArenaFromString(args.get(1));

            if (arena == null) {
                p.sendMessage(Colorize.color("&fAn arena with the name &7'" + args.get(1) + "' &cdoes not exist!"));
                pl.su.error(p.getLocation());
                return;
            }

            pl.am.deleteArena(arena);
            p.sendMessage(Colorize.color("The arena with the name &7'" + arena.getName() + "' &fhas been &cdeleted."));
            pl.su.success(p.getLocation());

        }

    }

}
