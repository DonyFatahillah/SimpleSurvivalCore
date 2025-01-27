package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.molfordan.simpleSurvival.Commands.enableConsoleLogCommand;
import org.molfordan.simpleSurvival.Main;

import static org.molfordan.simpleSurvival.Main.color;

public class playerDoingCommands implements Listener {

    private final enableConsoleLogCommand commandLog;

    public playerDoingCommands(enableConsoleLogCommand commandLog){
        this.commandLog = commandLog;
    }


    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        String command = event.getMessage();
        Player player = event.getPlayer();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("sl.command.log")) {
                if(commandLog.isToggled(player)) {
                    if(!command.equalsIgnoreCase("/enablelog")) {
                        onlinePlayer.sendMessage(Main.console + color.GREEN + player.getName() + " is issuing command: " + command);
                    }
                }
            }
        }
    }
}
