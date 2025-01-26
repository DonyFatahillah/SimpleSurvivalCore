package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Manager.messageManager;

import java.util.Arrays;
import java.util.HashMap;

import static org.molfordan.simpleSurvival.Main.color;

public class msgCommand implements CommandExecutor {

    private final messageManager messageManager;

    public msgCommand(messageManager messageManager){
        this.messageManager = messageManager;
    }




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

        if(target == null){
            playerSender.sendMessage(color.RED+"That player isn't online or doesn't exist!");
            return true;
        }



            target.sendMessage(color.GREEN + "[" + playerSender.getName() + " -> You] "+color.GRAY+"» " +color.RESET+ ChatColor.translateAlternateColorCodes('&', message));
            playerSender.sendMessage(color.GREEN + "[You -> " + target.getName() + "] "+color.GRAY+"» " +color.RESET+ ChatColor.translateAlternateColorCodes('&', message));



            messageManager.setLastMessaged(target, playerSender);

            return true;









    }
}
