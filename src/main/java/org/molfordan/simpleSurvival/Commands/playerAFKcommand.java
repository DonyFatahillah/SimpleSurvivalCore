package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

import static org.molfordan.simpleSurvival.Main.color;

public class playerAFKcommand implements TabExecutor {

    private final HashSet<UUID> afkPlayers = new HashSet<>();
    private final HashMap<UUID, String> afkReasons = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players are allowed to use this command!");
            return true;
        }

        Player player = (Player) sender;

        // If no arguments are provided, toggle AFK status without a reason
        if (args.length < 1) {
            toggleAFK(player, null);
        } else {
            // Combine arguments into a single string for the reason
            String reason = String.join(" ", args);
            toggleAFK(player, reason);
        }
        return true;
    }

    /**
     * Toggles the AFK status of a player.
     *
     * @param player The player whose status to toggle.
     * @param reason The reason for AFK (can be null).
     */
    public void toggleAFK(Player player, String reason) {
        if (!afkPlayers.contains(player.getUniqueId())) {
            setAfk(player, true, reason);
        } else {
            setAfk(player, false, null); // Clear reason when toggling off
        }
    }

    /**
     * Sets the AFK status of a player.
     *
     * @param player The player whose status to set.
     * @param isAfk  Whether the player is AFK.
     * @param reason The reason for AFK (only used when setting AFK to true).
     */
    public void setAfk(Player player, boolean isAfk, String reason) {
        if (isAfk) {
            afkPlayers.add(player.getUniqueId());
            if (reason != null) {
                afkReasons.put(player.getUniqueId(), reason);
                Bukkit.broadcastMessage(color.GREEN + "* " + player.getName() + " is now AFK! Reason: " + color.translateAlternateColorCodes('&', reason));
            } else {
                Bukkit.broadcastMessage(color.GREEN + "* " + player.getName() + " is now AFK!");
            }
            player.sendTitle(color.YELLOW + "You are now AFK!", color.GREEN + "Move to remove this message!", 10, 6000, 20);
        } else {
            afkPlayers.remove(player.getUniqueId());
            afkReasons.remove(player.getUniqueId());
            Bukkit.broadcastMessage(color.GREEN + "* " + player.getName() + " is no longer AFK!");
            player.sendTitle("", "", 0, 0, 0); // Clear the AFK title
        }
    }

    /**
     * Checks if a player is AFK.
     *
     * @param player The player to check.
     * @return True if the player is AFK, false otherwise.
     */
    public boolean isAfk(Player player) {
        return afkPlayers.contains(player.getUniqueId());
    }

    /**
     * Gets the AFK reason for a player.
     *
     * @param player The player to check.
     * @return The AFK reason, or "No reason provided" if none exists.
     */
    public String getAfkReason(Player player) {
        return afkReasons.getOrDefault(player.getUniqueId(), "No reason provided");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        // Add suggestions dynamically based on arguments
        if (args.length > 0) {
            suggestions.add("reasonWord" + args.length);
        }
        return suggestions;
    }
}
