package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.molfordan.simpleSurvival.Main.color;

public class playerExpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        Player target = Bukkit.getPlayer(args[0]);

        if (target != null){
            sender.sendMessage(color.GREEN+target.getName()+"'s level is "+target.getLevel());
            return true;
        } else {
            sender.sendMessage(color.RED+"that player is not online or doesn't exist!");
        }




       return true;
    }
}
