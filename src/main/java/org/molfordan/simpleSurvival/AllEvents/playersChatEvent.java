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
        if (itemStack != null) {
            String itemName;

            // Get the custom display name if available
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
                ItemMeta meta = itemStack.getItemMeta();
                itemName = ChatColor.translateAlternateColorCodes('&', meta.getDisplayName());
            } else {
                // Convert the material name to human-readable format
                itemName = formatMaterialName(itemStack.getType().name());
            }

            // Replace [i] in the message with the item name
            message = message.replace("[i]", color.GRAY+"["+color.RESET+itemName+color.GRAY+"]");
        }

        // Set the modified message
        event.setMessage(message);
    }

    // Helper method to format material names
    private String formatMaterialName(String materialName) {
        String[] words = materialName.toLowerCase().split("_");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            // Capitalize the first letter of each word
            formattedName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        // Remove the trailing space
        return formattedName.toString().trim();
    }
}
