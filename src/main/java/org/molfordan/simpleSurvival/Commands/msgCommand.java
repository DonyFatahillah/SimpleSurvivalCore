package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Main;
import org.molfordan.simpleSurvival.Manager.messageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import static org.molfordan.simpleSurvival.Main.color;

public class msgCommand implements TabExecutor {

    private final messageManager messageManager;

    private final Main plugin;

    public msgCommand(Main plugin, messageManager messageManager){
        this.plugin = plugin;
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        PluginCommand cmd = plugin.getCommand("message");

        if (cmd == null){
            return suggestions;
        }

        if (args.length == 0){
            List<String> aliases = cmd.getAliases();
            if (aliases != null && !aliases.isEmpty()) {
                suggestions.addAll(aliases);
            }

        } else if (args.length == 1){
            for (Player player : Bukkit.getOnlinePlayers()){
                suggestions.add(player.getName());
            }
        } else if (args.length > 1){
            suggestions.add("<message>");
        }


        return suggestions;
    }
}
