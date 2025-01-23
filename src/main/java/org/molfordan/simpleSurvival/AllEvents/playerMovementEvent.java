package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.molfordan.simpleSurvival.Commands.playerAFKcommand;

import java.util.HashMap;
import java.util.UUID;

import static org.molfordan.simpleSurvival.Main.color;

public class playerMovementEvent implements Listener {

    private final playerAFKcommand afkCommand;
    private final HashMap<UUID, Long> lastMovement = new HashMap<>();
    private static final long AFK_TIMEOUT = 5 * 60 * 1000; // 2 minutes in milliseconds

    public playerMovementEvent(playerAFKcommand afkCommand) {

        this.afkCommand = afkCommand;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Check if the player's movement is only rotation (not actual movement)
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        // Update last movement timestamp
        lastMovement.put(playerId, System.currentTimeMillis());

        // If the player is marked as AFK and moves, remove their AFK status
        if (afkCommand.isAfk(player)) {
            afkCommand.setAfk(player, false, null);
        }
    }

    // Method to check for inactive players
    public void checkInactivePlayers() {
        long currentTime = System.currentTimeMillis();
        for (UUID playerId : lastMovement.keySet()) {
            Player player = org.bukkit.Bukkit.getPlayer(playerId);
            if (player != null && !afkCommand.isAfk(player)) {
                long lastMove = lastMovement.get(playerId);
                if ((currentTime - lastMove) >= AFK_TIMEOUT) {
                    afkCommand.setAfk(player, true, null);
                    // Mark as AFK
                    player.sendMessage(color.GREEN+"You are now set to AFK after "+(AFK_TIMEOUT/60000)+" minutes idling");
                }
            }
        }
    }
}
