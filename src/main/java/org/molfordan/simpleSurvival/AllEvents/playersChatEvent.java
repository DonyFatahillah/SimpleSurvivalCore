package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import static org.molfordan.simpleSurvival.Main.color;

public class playersChatEvent implements Listener {


    @EventHandler
    public void onChat(PlayerChatEvent event){

        Player player = event.getPlayer();

        String message = event.getMessage();

        event.setFormat("<"+player.getName()+">" + ChatColor.translateAlternateColorCodes('&', message));


    }
}
