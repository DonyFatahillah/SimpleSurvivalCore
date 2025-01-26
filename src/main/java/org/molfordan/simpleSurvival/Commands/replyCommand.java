package org.molfordan.simpleSurvival.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Manager.messageManager;

import java.util.Arrays;

import static org.molfordan.simpleSurvival.Main.color;

public class replyCommand implements CommandExecutor {

    private final messageManager messageManager;


    public replyCommand(messageManager messageManager){
        this.messageManager = messageManager;
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage("only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Usage: /reply <message>");
            return true;
        }

        Player target = messageManager.getLastMessagedPlayer(player);
        if (target == null) {
            player.sendMessage("You have no one to reply to.");
            return false;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        target.sendMessage(color.GREEN + "[" + player.getName() + " -> You] "+color.GRAY+"» " +color.RESET+ ChatColor.translateAlternateColorCodes('&', message));
        player.sendMessage(color.GREEN + "[You -> " + target.getName() + "] "+color.GRAY+"» " +color.RESET+ ChatColor.translateAlternateColorCodes('&', message));

        return true;
    }
}
