package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Main;

import java.util.*;

import static org.molfordan.simpleSurvival.Main.color;
import static org.molfordan.simpleSurvival.Main.notAllowed;

public class setPlayerLocationCommand implements TabExecutor {

    private final Main plugin;
    private final HashMap<String, String> locationMap;
    public setPlayerLocationCommand(Main plugin, HashMap<String, String> locationMap){
        this.plugin = plugin;
        this.locationMap = locationMap;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        FileConfiguration config = plugin.getConfig();

        if(!sender.hasPermission("simplesurvival.setplayerlocation")){
            sender.sendMessage(notAllowed);
            return true;
        } else {

            if (args.length < 3) {
                sender.sendMessage(color.RED + "Usage : /setplayerlocation [playerName] [locationName] [x] [y] [z]");
                return true;
            }

            Player targetName = Bukkit.getPlayer(args[0]);
            String locationName = args[1];
            String lowerCaseName = locationName.toLowerCase(Locale.ROOT);
            String path = targetName.getName() + ".Locations." + locationName;

            if (args.length == 3) {
                if (!config.contains(targetName.getName() + ".Locations." + locationName)) {
                    Location location = targetName.getLocation();
                    config.set(path + ".world", location.getWorld().getName());
                    config.set(path + ".x", location.getBlockX());
                    config.set(path + ".y", location.getBlockY());
                    config.set(path + ".z", location.getBlockZ());
                } else {
                    sender.sendMessage(color.RED + "That location already exist!");
                    return true;
                }
            } else if (args.length == 5) {
                try {
                    int x = parseCoordinate(args[2], targetName.getLocation().getBlockX());
                    int y = parseCoordinate(args[3], targetName.getLocation().getBlockY());
                    int z = parseCoordinate(args[4], targetName.getLocation().getBlockZ());
                    config.set(path + ".world", targetName.getWorld().getName());
                    config.set(path + ".x", x);
                    config.set(path + ".y", y);
                    config.set(path + ".z", z);
                } catch (NumberFormatException e) {
                    sender.sendMessage(color.RED + "Coordinates must be numbers!");
                    return true;
                }
            } else {
                sender.sendMessage(color.RED + "Usage: /setlocation <name> [x] [y] [z]");
                return true;
            }

            locationMap.put(lowerCaseName, locationName);

            plugin.saveConfig();
            sender.sendMessage(color.GREEN + "Location '" + locationName + "' for " + targetName.getName() + " has been saved!");


            return true;
        }
    }

    private int parseCoordinate(String coord, int currentPos) {
        if (coord.contains("~")) {
            // If the coordinate contains "~", treat it as relative
            String relativeCoord = coord.replace("~", "").trim();
            int relativeValue = relativeCoord.isEmpty() ? 0 : Integer.parseInt(relativeCoord);
            return currentPos + relativeValue; // Add the relative value to the player's current position
        } else {
            // If it's not relative, parse it as an absolute coordinate
            return Integer.parseInt(coord);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (!sender.hasPermission("simplesurvival.setplayerlocations")){
            sender.sendMessage(notAllowed);
            return Collections.emptyList();
        }


        if (args.length == 1){
            for (Player player : Bukkit.getOnlinePlayers()){
                suggestions.add(player.getName());
            }
        } else if (args.length == 2){
            suggestions.add("locationName");
        } else if (args.length >= 3 && args.length <= 5){
            suggestions.add("x");
            suggestions.add("y");
            suggestions.add("z");
        }


        return suggestions;
    }
}
