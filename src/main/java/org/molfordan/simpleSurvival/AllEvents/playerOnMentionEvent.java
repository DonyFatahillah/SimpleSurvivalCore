package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import static org.molfordan.simpleSurvival.Main.color;

public class playerOnMentionEvent implements Listener {
    @EventHandler
    public void onMention(PlayerChatEvent event){
        Player sender = event.getPlayer();

        String message = event.getMessage();
        for (Player mentionedPlayer : sender.getServer().getOnlinePlayers()) {
            String playerName = mentionedPlayer.getName();

            if (message.contains(playerName)){
                //String highlightedPlayerName = color.YELLOW + mentionedPlayer.getName();
                message = message.replace(playerName, color.YELLOW + mentionedPlayer.getName() + color.RESET);



                mentionedPlayer.getPlayer().playSound(sender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        }

        event.setMessage(message);
    }
}
