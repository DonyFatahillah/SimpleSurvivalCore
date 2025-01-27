package org.molfordan.simpleSurvival.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class enableConsoleLogCommand implements CommandExecutor {

    private final Set<UUID> consolePlayer = new HashSet<>();




    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("only players can use this command");
            return true;
        }



        Player player = (Player) sender;

        toggleConsole(player);





        return true;
    }


    private void toggleConsole(Player player){

        UUID playerUUID = player.getUniqueId();

        if (!consolePlayer.contains(playerUUID)) {
            consolePlayer.add(playerUUID);
            player.sendMessage(ChatColor.GREEN+"Console Log enabled!");

        } else {
            consolePlayer.remove(playerUUID);
            player.sendMessage(ChatColor.RED+"Console log disabled!");
        }
    }

    public boolean isToggled(Player player){
        return consolePlayer.contains(player.getUniqueId());
    }
}
