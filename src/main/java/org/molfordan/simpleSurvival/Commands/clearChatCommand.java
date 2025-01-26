package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class clearChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("simplesurvival.admin")){
            sender.sendMessage(ChatColor.RED+"You don't have any permission to do this");
            return true;
        }


        for (int x = 0; x < 150; x++){
            Bukkit.broadcastMessage("");
        }

        Bukkit.broadcastMessage(ChatColor.GREEN+"Chat has been cleared by "+sender.getName());

        return true;
    }
}
