package org.molfordan.simpleSurvival.Commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class seePlayerLocations implements TabExecutor {

    private final Main plugin;
    private final HashMap<String, String> locationMap;

    public seePlayerLocations(Main plugin, HashMap<String, String> locationMap) {
        this.plugin = plugin;
        this.locationMap = locationMap;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //sender.sendMessage("DEBUG: /" + label + " " + String.join(" ", args)); // Debug message

        if (!sender.hasPermission("simplesurvival.seeplayerlocations")){
            sender.sendMessage(notAllowed);
            return true;
        } else {

            if (args.length < 1) {
                sender.sendMessage(color.RED + "Usage: /seelocation <player> [locationName]");
                return true;
            }

            Player target = sender.getServer().getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(color.RED + "That player is not online!");
                return true;
            }

            FileConfiguration config = plugin.getConfig();
            String playerPath = target.getName() + ".Locations";

            if (!config.contains(playerPath)) {
                sender.sendMessage(color.RED + "The player does not have any saved locations!");
                return true;
            }

            if (args.length == 1) {
                // List all saved locations
                Set<String> locationNames = config.getConfigurationSection(playerPath).getKeys(false);
                if (locationNames == null || locationNames.isEmpty()) {
                    sender.sendMessage(color.RED + "No locations found for " + target.getName() + "!");
                    return true;
                }

                sender.sendMessage(color.GREEN + target.getDisplayName() + "'s saved locations:");
                for (String locationName : locationNames) {
                    // Create a clickable message
                    TextComponent message = new TextComponent("- " + locationName);
                    message.setColor(net.md_5.bungee.api.ChatColor.GREEN); // Set color
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/seelocation " + target.getName() + " " + locationName)); // Add click event

                    // Send the clickable message to the player
                    sender.spigot().sendMessage(message);
                    return true;
                }
                return true;
            }

            if (args.length == 2) {
                // Show details for a specific location
                String inputName = args[1].toLowerCase(Locale.ROOT);
                String matchedName = locationMap.getOrDefault(inputName, inputName); // Use input if no match found

                String locationPath = playerPath + "." + matchedName;

                if (!config.contains(locationPath)) {
                    sender.sendMessage(color.RED + "Location '" + args[1] + "' does not exist in " + target.getName() + "'s data!");
                    return true;
                }

                String worldName = config.getString(locationPath + ".world", "Unknown");
                String displayWorld;
                switch (worldName) {
                    case "world":
                        displayWorld = "Overworld";
                        break;
                    case "world_nether":
                        displayWorld = "Nether";
                        break;
                    case "world_the_end":
                        displayWorld = "End";
                        break;
                    default:
                        displayWorld = worldName;
                }

                double x = config.getDouble(locationPath + ".x", 0);
                double y = config.getDouble(locationPath + ".y", 0);
                double z = config.getDouble(locationPath + ".z", 0);

                sender.sendMessage(color.GREEN + "Details for " + target.getName() + "'s '" + matchedName + "':");
                sender.sendMessage(color.GREEN + "World: " + displayWorld);
                sender.sendMessage(color.GREEN + "X: " + x);
                sender.sendMessage(color.GREEN + "Y: " + y);
                sender.sendMessage(color.GREEN + "Z: " + z);
                return true;
            }

            sender.sendMessage(color.RED + "Usage: /seeplayerlocations <player> [locationName]");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (!sender.hasPermission("simplesurvival.seeplayerlocations")){
            sender.sendMessage(notAllowed);
            return Collections.emptyList();
        }

        if (args.length == 1){
            for (Player player : Bukkit.getOnlinePlayers()){
                suggestions.add(player.getName());
            }
        } else if (args.length == 2) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer != null) {
                FileConfiguration config = plugin.getConfig();
                String targetName = targetPlayer.getName();

                if (config.contains(targetName + ".Locations")) {
                    Set<String> locationNames = config.getConfigurationSection(targetName + ".Locations").getKeys(false);
                    suggestions.addAll(locationNames);
                }
            }
        }


        return suggestions;
    }
}
