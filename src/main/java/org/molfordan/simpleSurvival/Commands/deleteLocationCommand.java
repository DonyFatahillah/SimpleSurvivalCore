package org.molfordan.simpleSurvival.Commands;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.molfordan.simpleSurvival.Main.color;

public class deleteLocationCommand implements TabExecutor {

    private final Main plugin;

    public deleteLocationCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure only players can use this command
        if (!(sender instanceof Player)) {
            sender.sendMessage(color.RED + "This command is for players only!");
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();
        FileConfiguration config = plugin.getConfig();



        // Ensure the player has saved locations
        if (!config.contains(playerName + ".Locations")) {
            sender.sendMessage(color.RED + "You don't have any saved locations!");
            return true;
        }

        // If no arguments are provided, list the player's saved locations
        if (args.length == 0) {
            Set<String> locationNames = config.getConfigurationSection(playerName + ".Locations").getKeys(false);
            if (locationNames.isEmpty()) {
                sender.sendMessage(color.RED + "You don't have any saved locations!");
                return true;
            }

            sender.sendMessage(color.GREEN + "Your saved locations:");
            for (String locationName : locationNames) {
                sender.sendMessage(color.GREEN+ "- " + locationName);
            }
            return true;
        }

        // Handle deletion of a specific location
        String locationName = args[0];
        String locationPath = playerName + ".Locations." + locationName;

        // Check if the location exists
        if (!config.contains(locationPath)) {
            sender.sendMessage(color.RED + "Location '" + locationName + "' does not exist!");
            return true;
        }

        // Remove the location from the configuration
        config.set(locationPath, null);

        // Save the configuration
        plugin.saveConfig();

        // Notify the player of the deletion
        sender.sendMessage(color.GREEN + "Location '" + locationName + "' has been successfully deleted!");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1){
            FileConfiguration config = plugin.getConfig();
            if(config.contains(sender.getName() + ".Locations")){
                Set<String> locationName = config.getConfigurationSection(sender.getName() + ".Locations").getKeys(false);
                suggestions.addAll(locationName);
            }
        }


        return suggestions;
    }
}
