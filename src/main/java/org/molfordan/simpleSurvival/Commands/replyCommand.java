package org.molfordan.simpleSurvival.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Main;
import org.molfordan.simpleSurvival.Manager.messageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.molfordan.simpleSurvival.Main.color;

public class replyCommand implements TabExecutor {

    private final messageManager messageManager;

    private final Main plugin;


    public replyCommand(Main plugin, messageManager messageManager){
        this.plugin = plugin;
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
            player.sendMessage(color.GREEN+"You have no one to reply to.");
            return true;
        }

        String message = String.join(" ", args);

        target.sendMessage(color.GREEN + "[" + player.getName() + " -> You] "+color.GRAY+"» " +color.RESET+ ChatColor.translateAlternateColorCodes('&', message));
        player.sendMessage(color.GREEN + "[You -> " + target.getName() + "] "+color.GRAY+"» " +color.RESET+ ChatColor.translateAlternateColorCodes('&', message));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestion = new ArrayList<>();

        PluginCommand cmd = plugin.getCommand("reply");

        if (cmd == null){
            return suggestion;
        }

        if (args.length == 0){
            List<String> aliases = cmd.getAliases();
            if (aliases != null && !aliases.isEmpty()) {
                suggestion.addAll(aliases);
            }


        } else if (args.length > 1){
            suggestion.add("<message>");
        }


        return suggestion;
    }
}
