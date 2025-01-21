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



        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /nick [playerName] [newNick] or /nick [newNick]");
            return true;
        }

        if (args.length == 1) {
            String input = args[0];
            boolean isOnline = false;

            // Check if the input matches an online player's name
            Player targetPlayer = null;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getName().equalsIgnoreCase(input)) {
                    isOnline = true;
                    targetPlayer = onlinePlayer;
                    break;
                }
            }

            if (isOnline) {
                // The argument matches an online player's name
                player.sendMessage(ChatColor.RED + "Usage: /nick " + input + " [newNick] to set their nickname.");
            } else {
                // Change the sender's nickname
                player.setDisplayName(ChatColor.translateAlternateColorCodes('&', input));
                player.sendMessage(ChatColor.GREEN + "Your nickname has been set to " + ChatColor.translateAlternateColorCodes('&', input));
            }
            return true;
        }

        if (args.length == 2) {
            String targetName = args[0];
            String newNick = args[1];

            // Check if the target player is online
            Player targetPlayer = Bukkit.getPlayerExact(targetName);
            if (targetPlayer != null) {
                // Change the target player's nickname
                targetPlayer.setDisplayName(ChatColor.translateAlternateColorCodes('&', newNick));
                targetPlayer.sendMessage(ChatColor.GREEN + "Your nickname has been set to " + ChatColor.translateAlternateColorCodes('&', newNick));
                player.sendMessage(ChatColor.GREEN + "You have set " + targetName + "'s nickname to " + ChatColor.translateAlternateColorCodes('&', newNick));
            } else {
                player.sendMessage(ChatColor.RED + "Player " + targetName + " is not online.");
            }
            return true;
        }

        player.sendMessage(ChatColor.RED + "Invalid usage. Try: /nick [newNick] or /nick [playerName] [newNick]");
        return true;


    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1){
            for (Player player : Bukkit.getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
            suggestions.add("nickname");
        }


        return suggestions;
    }
}
