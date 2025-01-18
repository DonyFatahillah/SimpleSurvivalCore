package org.molfordan.simpleSurvival.Commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Main;

import java.util.*;

import static org.molfordan.simpleSurvival.Main.color;

public class LocationCommand implements TabExecutor {

    private final Main plugin;
    private final HashMap<String, String> locationMap; // Maps lowercase names to original names

    public LocationCommand(Main plugin, HashMap<String, String> locationMap) {
        this.plugin = plugin;
        this.locationMap = locationMap;
        reloadLocationMap();
    }

    public void reloadLocationMap() {
        locationMap.clear(); // Clear existing mappings
        FileConfiguration config = plugin.getConfig();
        for (String playerName : config.getKeys(false)) {
            if (config.isConfigurationSection(playerName + ".Locations")) {
                Set<String> locationNames = config.getConfigurationSection(playerName + ".Locations").getKeys(false);
                for (String locationName : locationNames) {
                    locationMap.put(locationName.toLowerCase(Locale.ROOT), locationName); // Map lowercase to original
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only!");
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();
        FileConfiguration config = plugin.getConfig();

        if (!config.contains(playerName + ".Locations")) {
            sender.sendMessage(color.RED + "You don't have any saved locations!");
            return true;
        }

        Set<String> locationNames = config.getConfigurationSection(playerName + ".Locations").getKeys(false);
        if (locationNames.isEmpty()) {
            sender.sendMessage(color.RED + "You don't have any saved locations!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(color.GREEN + "Your saved locations:");
            for (String locationName : locationNames) {
                TextComponent message = new TextComponent("- " + locationName);
                message.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/location " + locationName));
                player.spigot().sendMessage(message);
            }
            return true;
        }

        String inputName = args[0].toLowerCase(Locale.ROOT);
        reloadLocationMap(); // Ensure the map is up-to-date
        String matchedName = locationMap.get(inputName);

        if (matchedName == null) {
            sender.sendMessage(color.RED + "Location '" + args[0] + "' does not exist!");
            return true;
        }

        String path = playerName + ".Locations." + matchedName;
        String worldName = config.getString(path + ".world");
        String displayWorld = switch (worldName) {
            case "world" -> "Overworld";
            case "world_nether" -> "Nether";
            case "world_the_end" -> "End";
            default -> worldName;
        };

        int x = config.getInt(path + ".x");
        int y = config.getInt(path + ".y");
        int z = config.getInt(path + ".z");

        sender.sendMessage(color.GREEN + "Details for location '" + matchedName + "':");
        sender.sendMessage(color.GREEN + "World: " + displayWorld);
        sender.sendMessage(color.GREEN + "X: " + x);
        sender.sendMessage(color.GREEN + "Y: " + y);
        sender.sendMessage(color.GREEN + "Z: " + z);

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1){
            FileConfiguration config = plugin.getConfig();
            if (config.contains(sender.getName() + ".Locations")) {
                Set<String> locationNames = config.getConfigurationSection(sender.getName() + ".Locations").getKeys(false);
                suggestions.addAll(locationNames);
            }
        }

        return suggestions;
    }
}
