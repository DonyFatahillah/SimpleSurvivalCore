package org.molfordan.simpleSurvival.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class testCommand implements CommandExecutor {
    /**
     * @param sender
     * @param command
     * @param s
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("TEST");
            return true;
        }

        Player player = (Player) sender;
        player.sendTitle("TEST", "test", 10,50, 10);
        player.sendMessage("DEBUG");



        return true;
    }
}
