package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.molfordan.simpleSurvival.Commands.playerAFKcommand;

import java.util.HashMap;
import java.util.UUID;

import static org.molfordan.simpleSurvival.Main.color;

public class playerMovementEvent implements Listener {

    private final playerAFKcommand afkCommand;
    private final HashMap<UUID, Long> lastMovement = new HashMap<>();
    private final long AFK_TIMEOUT;

    public playerMovementEvent(playerAFKcommand afkCommand, long afkTimeout) {
        this.afkCommand = afkCommand;
        this.AFK_TIMEOUT = afkTimeout * 1000; // Convert seconds to milliseconds
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (event.getFrom().distanceSquared(event.getTo()) < 0.01) {
            return; // Ignore small movements
        }

        // Update last movement timestamp
        lastMovement.put(playerId, System.currentTimeMillis());

        // If the player is marked as AFK and moves, remove their AFK status
        if (afkCommand.isAfk(player)) {
            afkCommand.setAfk(player, false, null);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        lastMovement.remove(playerId); // Remove player from lastMovement map
    }

    public void checkInactivePlayers() {
        long currentTime = System.currentTimeMillis();
        for (UUID playerId : lastMovement.keySet()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && !afkCommand.isAfk(player)) {
                long lastMove = lastMovement.get(playerId);
                if ((currentTime - lastMove) >= AFK_TIMEOUT) {
                    afkCommand.setAfk(player, true, null); // Mark as AFK
                    player.sendMessage(color.GREEN + "You are now set to AFK after " + (AFK_TIMEOUT / 60000) + " minutes idling.");
                }
            }
        }
    }
}
