package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import static org.molfordan.simpleSurvival.Main.color;

public class playersChatEvent implements Listener {


    @EventHandler
    public void onChat(PlayerChatEvent event){

        Player player = event.getPlayer();

        String message = event.getMessage();

        event.setFormat("<"+player.getDisplayName()+ChatColor.RESET+"> " + ChatColor.translateAlternateColorCodes('&', message));


    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Check if the player is holding an item
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();

            // Get the display name or fallback to material name
            String itemName = meta.hasDisplayName()
                    ? ChatColor.translateAlternateColorCodes('&', meta.getDisplayName())
                    : itemStack.getType().name();

            // Replace [i] in the message with the item name
            message = message.replace("[i]", itemName);
        }

        // Set the modified message
        event.setMessage(message);
    }
}
