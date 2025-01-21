package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.molfordan.simpleSurvival.Commands.playerAFKcommand;


import static org.molfordan.simpleSurvival.Main.color;

public class playerOnChatEvent implements Listener {

    private final playerAFKcommand afkCommand;

    public playerOnChatEvent(playerAFKcommand afkCommand) {
        this.afkCommand = afkCommand;
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage().toLowerCase(); // Convert to lowercase for case-insensitive comparison

        // Check for mentions of players in the format "@playerName" or just player names
        for (Player mentionedPlayer : sender.getServer().getOnlinePlayers()) {
            String playerName = mentionedPlayer.getName().toLowerCase();

            // Check if the message contains the player's name
            if (message.contains(playerName)) {
                // Check if the mentioned player is AFK
                if (afkCommand.isAfk(mentionedPlayer)) {
                    // Retrieve the reason for AFK
                    String reason = afkCommand.getAfkReason(mentionedPlayer);
                    if (reason != null) {

                        // Send a message to the sender that the mentioned player is AFK
                        sender.sendMessage(color.GREEN + "* " + mentionedPlayer.getName() + " is currently AFK! Reason: " + ChatColor.translateAlternateColorCodes('&', reason));
                    } else {
                        sender.sendMessage(color.GREEN + mentionedPlayer.getName() + " is currently AFK!");
                    }
                }
            }
        }
    }
}
