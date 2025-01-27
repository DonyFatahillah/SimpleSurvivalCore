package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.molfordan.simpleSurvival.Commands.playerAFKcommand;
import org.molfordan.simpleSurvival.Main;

import java.util.HashMap;
import java.util.UUID;

import static org.molfordan.simpleSurvival.Main.color;

public class playerMovementEvent implements Listener {

    private final playerAFKcommand afkCommand;
    private final HashMap<UUID, Long> lastMovement = new HashMap<>();
    private final long AFK_TIMEOUT;


    public playerMovementEvent(playerAFKcommand afkCommand, long afkTimeoutSeconds) {

        this.afkCommand = afkCommand;
        this.AFK_TIMEOUT = afkTimeoutSeconds * 1000; // Convert seconds to milliseconds

        // Schedule a repeating task to check inactivity
        Bukkit.getScheduler().runTaskTimer(afkCommand.plugin, this::checkAFKTimeout, 0L, 100L); // Every 5 seconds
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Ignore small movements
        if (event.getFrom().distanceSquared(event.getTo()) < 0.01) {
            return;
        }

        // Update last movement time
        lastMovement.put(playerId, System.currentTimeMillis());

        // If player is AFK, remove AFK status
        if (afkCommand.isAFK(player)) {
            afkCommand.setAFK(player, false, null);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove player from tracking when they leave the server
        UUID playerId = event.getPlayer().getUniqueId();
        lastMovement.remove(playerId);
    }

    /**
     * Periodically checks players for inactivity and sets them as AFK if necessary.
     */
    private void checkAFKTimeout() {
        long currentTime = System.currentTimeMillis();

        for (UUID playerId : lastMovement.keySet()) {
            Player player = Bukkit.getPlayer(playerId);

            if (player != null && !afkCommand.isAFK(player)) {
                long lastActiveTime = lastMovement.get(playerId);

                // If the player has been inactive for longer than the timeout
                if ((currentTime - lastActiveTime) >= AFK_TIMEOUT) {
                    afkCommand.setAFK(player, true, null);
                    player.sendMessage(color.GREEN + "You are now AFK due to " + (AFK_TIMEOUT / 60000) + " minutes of inactivity.");
                }
            }
        }
    }
}
