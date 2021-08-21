package net.taccy.spleef.commands.subcommands;

import net.taccy.spleef.Spleef;
import net.taccy.spleef.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class InfoSub extends SubCommand {

    private Spleef pl;

    public InfoSub(Spleef pl) {
        this.pl = pl;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPermission() {
        return "spleef.command.info";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void handle(CommandSender sender, ArrayList<String> args) {
        sender.sendMessage("info command yey");
    }

}
