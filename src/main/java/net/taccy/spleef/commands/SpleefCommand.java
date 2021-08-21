package net.taccy.spleef.commands;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.arena.SpleefArena;
import net.taccy.spleef.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpleefCommand implements CommandExecutor, TabCompleter {

    private Spleef pl;

    public SpleefCommand(Spleef pl) {
        this.pl = pl;

        subcommands.add(new InfoSub(pl));
        subcommands.add(new ArenaSub(pl));
        subcommands.add(new GUISub(pl));
        subcommands.add(new GameSub(pl));
        subcommands.add(new PlaySub(pl));
        subcommands.add(new LeaveSub(pl));
    }

    private ArrayList<SubCommand> subcommands = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // if they dont give a subcommand
        if (args.length == 0) {
            // TODO: add handling for no arguments (maybe help command and also permissions)
            return true;
        }

        // get the subcommand they used
        SubCommand cmd = getSubCommandFromString(args[0]);

        if (cmd == null) {
            // TODO: handle not having the subcommand (use config here)
            return true;
        }

        // checks for permission from command
        if (!sender.hasPermission(cmd.getPermission())) {
            // TODO: add handling for no specific command permissions (config message?)
            return true;
        }


        // convert the args into arraylist and remove first argument
        // this converts "/sp gmc 1berry1" arguments to simply "1berry1"
        ArrayList betterArgs = new ArrayList<String>(Arrays.asList(args));
        betterArgs.remove(0);

        // now pass it over to the proper subcomand to handle
        cmd.handle(sender, betterArgs);

        return false;

    }

    public SubCommand getSubCommandFromString(String s) {
        for (SubCommand cmd : subcommands) {
            if (cmd.getName().equalsIgnoreCase(s)) {
                return cmd;
            }
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> suggestions = new ArrayList<>();

        if (!sender.hasPermission("launchify.admin")) {
            suggestions.add("play");
            return suggestions;
        }

        if (args.length == 1) {
            for (SubCommand cmd : subcommands) {
                suggestions.add(cmd.getName());
            }
        }

        SubCommand subCommand = getSubCommandFromString(args[0]);

        if (subCommand == null) {
            return suggestions;
        }

        if (args.length == 2) {
            List<String> subcommands = subCommand.getSubCommands();
            if (subcommands != null) {
                for (String subcommand : getSubCommandFromString(args[0]).getSubCommands()) {
                    suggestions.add(subcommand);
                }
            }
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("join") || args[1].equalsIgnoreCase("powervis")) {
                for (SpleefArena arena : Spleef.arenas) {
                    suggestions.add(arena.getName());
                }
            }
        }

        return suggestions;
    }

}
