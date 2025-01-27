package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.molfordan.simpleSurvival.Main;

import java.util.*;

import static org.molfordan.simpleSurvival.Main.color;

public class playerAFKcommand implements TabExecutor {

    public final Main plugin;
    private final Set<UUID> afkPlayers = new HashSet<>();
    private final Map<UUID, String> afkReasons = new HashMap<>();
    private final Map<UUID, BukkitTask> afkTitleTasks = new HashMap<>();
    private final Map<Player, Long> lastActionTime = new HashMap<>();

    public playerAFKcommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            toggleAFK(player, null);
        } else {
            String reason = String.join(" ", args);
            toggleAFK(player, reason);
        }
        return true;
    }

    public void toggleAFK(Player player, String reason) {
        if (afkPlayers.contains(player.getUniqueId())) {
            setAFK(player, false, null);
        } else {
            setAFK(player, true, reason);
        }
    }

    public void setAFK(Player player, boolean isAFK, String reason) {
        UUID playerId = player.getUniqueId();

        if (isAFK) {
            afkPlayers.add(playerId);
            if (reason != null) {
                afkReasons.put(playerId, reason);
                Bukkit.broadcastMessage(color.GREEN + "* " + player.getName() + " is now AFK! Reason: " + ChatColor.translateAlternateColorCodes('&', reason));
            } else {
                Bukkit.broadcastMessage(color.GREEN + "* " + player.getName() + " is now AFK!");
            }

            // Schedule a repeating task to send titles while AFK
            BukkitTask afkTitleTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (afkPlayers.contains(playerId)) {
                    player.sendTitle(
                            color.YELLOW + "You are currently AFK",
                            color.GREEN+"move to remove this message!",
                            0, 70, 0
                    );
                }
            }, 0L, 20L); // Repeat every 5 seconds (100 ticks)

            afkTitleTasks.put(playerId, afkTitleTask);

        } else {
            afkPlayers.remove(playerId);
            afkReasons.remove(playerId);
            Bukkit.broadcastMessage(color.GREEN + "* " + player.getName() + " is no longer AFK!");

            // Cancel the repeating title task
            if (afkTitleTasks.containsKey(playerId)) {
                afkTitleTasks.get(playerId).cancel();
                afkTitleTasks.remove(playerId);
            }

            // Clear the title when player returns from AFK
            player.sendTitle("", "", 0, 0, 0);
        }
    }

    public void resetAFKTimer(Player player) {
        lastActionTime.put(player, System.currentTimeMillis());
    }

    public boolean isAFK(Player player) {
        return afkPlayers.contains(player.getUniqueId());
    }

    public String getAFKReason(Player player) {
        return afkReasons.getOrDefault(player.getUniqueId(), null);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
