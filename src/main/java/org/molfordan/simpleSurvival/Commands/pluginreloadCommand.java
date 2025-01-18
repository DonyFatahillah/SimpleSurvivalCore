package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.molfordan.simpleSurvival.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.molfordan.simpleSurvival.Main.color;
import static org.molfordan.simpleSurvival.Main.notAllowed;

public class pluginreloadCommand implements TabExecutor {

    private final Main plugin;

    public pluginreloadCommand(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission("simplesurvival.reload")){
            sender.sendMessage(color.RED+"You don't have permissions to do that!");
            return true;
        }


        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

            plugin.reloadConfig();
            Bukkit.getConsoleSender().sendMessage(color.GREEN + "[SimpleSurvival] Plugin has been reloaded!");
            sender.sendMessage(color.GREEN + "[SimpleSurvival] Plugin has been reloaded!");
            return true;
        } else {
            sender.sendMessage(color.RED + "Invalid command!");


        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (!sender.hasPermission("simplesurvival.reload")){
            sender.sendMessage(notAllowed);
            return Collections.emptyList();
        }

        if (args.length == 1){
            suggestions.add("reload");
        }

        return suggestions;
    }
}
