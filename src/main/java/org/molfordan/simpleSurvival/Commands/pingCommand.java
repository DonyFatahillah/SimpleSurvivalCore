package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.molfordan.simpleSurvival.Main.color;

public class pingCommand implements CommandExecutor {
    /**
     * @param sender
     * @param command
     * @param s
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage("you are not allowed to execute this command!");
            return true;
        }

        if (args.length == 0) {

            Player player = (Player) sender;
            int ping = player.getPing();
            player.sendMessage(color.GREEN + "Your ping is " + ping + "ms");
            return true;
        }



        if (args.length == 1){

            Player target = Bukkit.getPlayer(args[0]);
            if(args[0].equals(target.getName())) {

                if (target != null) {
                    int ping = target.getPing();
                    sender.sendMessage(color.GREEN + target.getName() + "'s ping is " + ping + "ms");
                    return true;
                } else {
                    sender.sendMessage(color.RED + "that player is not online!");

                }

                return true;
            } else {
                sender.sendMessage(color.GREEN+"Usage: /ping <playerName>");

            }

            return true;

        }


        return true;
    }
}
