package org.molfordan.simpleSurvival.Commands;

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

        Player player = (Player) sender;
        int ping  = player.getPing();
        player.sendMessage(color.GREEN + "Your ping is " + ping + "ms");



        return true;
    }
}
