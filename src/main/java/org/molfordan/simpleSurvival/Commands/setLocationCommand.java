package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static org.molfordan.simpleSurvival.Main.color;

public class setLocationCommand implements TabExecutor {

    private final HashMap<String, String> locationMap; // Maps lowercase names to original names
    private final Main plugin;

    public setLocationCommand(Main plugin, HashMap<String, String> locationMap) {
        this.plugin = plugin;
        this.locationMap = locationMap;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        FileConfiguration config = plugin.getConfig();
        Player player = (Player) sender;

        if (args.length < 1) {
            sender.sendMessage(color.RED + "Usage: /setlocation <name> [x] [y] [z]");
            return true;
        }

        String locationName = args[0];
        String lowerCaseName = locationName.toLowerCase(Locale.ROOT);



        // Save the location details
        String path = player.getName() + ".Locations." + locationName;




        if (args.length == 1) {
            if (!config.contains(player.getName() + ".Locations." + locationName)) {
                // Save the player's current location
                Location location = player.getLocation();
                config.set(path + ".world", location.getWorld().getName());
                config.set(path + ".x", location.getBlockX());
                config.set(path + ".y", location.getBlockY());
                config.set(path + ".z", location.getBlockZ());
            } else {
                player.sendMessage(color.RED + "That location already exist!");
                return true;
            }
        } else if (args.length == 4) {
            // Save specified coordinates
            try {
                int x = parseCoordinate(args[1], player.getLocation().getBlockX());
                int y = parseCoordinate(args[2], player.getLocation().getBlockY());
                int z = parseCoordinate(args[3], player.getLocation().getBlockZ());
                config.set(path + ".world", player.getWorld().getName());
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

        // Save to the HashMap for quick case-insensitive lookup
        locationMap.put(lowerCaseName, locationName);

        plugin.saveConfig();
        sender.sendMessage(color.GREEN + "Location '" + locationName + "' has been saved!");
        return true;
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

        if (args.length == 1){
            suggestions.add("locationName");
        } else if (args.length >= 2 && args.length <= 4){
            suggestions.add("x");
            suggestions.add("y");
            suggestions.add("z");
        }

        return suggestions;
    }
}
