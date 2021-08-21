package net.taccy.spleef.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {

    public abstract String getName();
    public abstract String getPermission();
    public abstract List<String> getSubCommands();
    public abstract void handle(CommandSender sender, ArrayList<String> args);

}
