package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static org.molfordan.simpleSurvival.Main.color;

public class playerNickCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage("Only player can use this command!");
            return true;
        }

        Player player = (Player) sender;



        if (args.length == 0){
            player.sendMessage(color.RED + "usage : /nick [new nick]");
            return true;
        }

        if (args.length == 1){
            String playerName = args[0];

            player.setDisplayName(ChatColor.translateAlternateColorCodes('&', playerName));
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1){
            suggestions.add("nickname");
        }


        return suggestions;
    }
}
