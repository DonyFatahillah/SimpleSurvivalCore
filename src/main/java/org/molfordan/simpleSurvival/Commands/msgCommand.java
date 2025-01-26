package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static org.molfordan.simpleSurvival.Main.color;

public class msgCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("only player can use this command!");
            return true;
        }


        Player playerSender = (Player) sender;

        Player target = Bukkit.getPlayer(args[0]);

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        if (args.length < 2){
            playerSender.sendMessage(color.GREEN+"Usage:/msg <Player> <Message>");
            return true;
        }


        if (target.isOnline()) {

            target.sendMessage(color.GREEN + "[" + playerSender.getName() + " -> You] "+color.GRAY+"» " +color.RESET+ ChatColor.translateAlternateColorCodes('&', message));
            playerSender.sendMessage(color.GREEN + "[You -> " + target.getName() + "] "+color.GRAY+"» " +color.RESET+ ChatColor.translateAlternateColorCodes('&', message));
            return true;
        } else {
            playerSender.sendMessage(color.RED+"That player is offline or doesn't exist!");
        }







        return true;
    }
}
