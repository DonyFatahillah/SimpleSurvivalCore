package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.molfordan.simpleSurvival.Main.notAllowed;

public class deletePlayerLocationCommand implements TabExecutor {

    private final Main plugin;

    public deletePlayerLocationCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("simplesurvival.deleteplayerlocation")){
            sender.sendMessage(notAllowed);
            return true;
        } else {


            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /deleteplayerlocation [playerName] [locationName]");
                return true;
            }

            Player targetName = Bukkit.getPlayer(args[0]);
            if (targetName == null) {
                sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' is not online or does not exist.");
                return true;
            }

            FileConfiguration config = plugin.getConfig();

            if (!config.contains(targetName.getName() + ".Locations")) {
                sender.sendMessage(ChatColor.RED + targetName.getName() + " doesn't have any locations saved.");
                return true;
            }

            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /deleteplayerlocation [playerName] [locationName]");
                Set<String> locationNames = config.getConfigurationSection(targetName.getName() + ".Locations").getKeys(false);
                if (locationNames.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + targetName.getName() + " doesn't have any locations saved.");
                    return true;
                }

                sender.sendMessage(ChatColor.GREEN + targetName.getName() + "'s saved locations:");
                for (String locationName : locationNames) {
                    sender.sendMessage(ChatColor.GREEN + "- " + locationName);
                }
                return true;
            }

            String locationName = args[1];
            String locationPath = targetName.getName() + ".Locations." + locationName;

            if (!config.contains(locationPath)) {
                sender.sendMessage(ChatColor.RED + "Location '" + locationName + "' does not exist!");
                return true;
            }

            config.set(locationPath, null);
            plugin.saveConfig();
            sender.sendMessage(ChatColor.GREEN + targetName.getName() + "'s location: '" + locationName + "' has been successfully deleted!");

            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (!sender.hasPermission("simplesurvival.deleteplayerlocation")) {
            sender.sendMessage(notAllowed);
            return Collections.emptyList();
        }

        if (args.length == 1){
            for (Player player : Bukkit.getOnlinePlayers()){
                suggestions.add(player.getName());
            }
        } else if (args.length == 2){
            Player targetName = Bukkit.getPlayer(args[0]);

            FileConfiguration config = plugin.getConfig();

            if (config.contains(targetName.getName() + ".Locations")) {
                Set<String> locationNames = config.getConfigurationSection(targetName.getName() + ".Locations").getKeys(false);
                suggestions.addAll(locationNames);
            }
        }


        return suggestions;
    }
}
