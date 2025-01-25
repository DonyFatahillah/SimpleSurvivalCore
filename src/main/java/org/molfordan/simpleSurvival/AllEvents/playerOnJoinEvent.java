package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

import static org.molfordan.simpleSurvival.Main.color;






public class playerOnJoinEvent implements Listener {

    private final FileConfiguration mailConfig;

    public playerOnJoinEvent(FileConfiguration mailConfig){
        this.mailConfig = mailConfig;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        event.setJoinMessage(color.YELLOW+player.getName()+" telah memasuki server!");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);


        List<String> messages = mailConfig.getStringList(player.getName()+".messages." + uuid);
        if (!messages.isEmpty()){
            player.sendMessage(color.GREEN + "You have a new message!");
            player.sendMessage(color.GREEN+"/readmail to read the message!");
        }


    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.setQuitMessage(color.YELLOW+player.getName()+" telah meninggalkan server.");
    }
}
