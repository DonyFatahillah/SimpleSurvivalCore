package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class playerColoredAnvilEvent implements Listener {

    @EventHandler
    public void onAnvilColored(PrepareAnvilEvent event){
        AnvilInventory anvilInventory = event.getInventory();

        // Get the item in the first slot (the item being renamed)
        ItemStack firstSlotItem = anvilInventory.getItem(0);

        if (firstSlotItem != null && firstSlotItem.hasItemMeta()) {
            ItemStack resultItem = event.getResult(); // Get the output item
            if (resultItem != null) {
                ItemMeta meta = resultItem.getItemMeta();
                if (meta != null) {
                    // Check if the rename text is present
                    String renameText = meta.getDisplayName();
                    if (renameText != null && !renameText.isEmpty()) {
                        // Translate color codes and set the display name
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', renameText));
                        resultItem.setItemMeta(meta);
                        event.setResult(resultItem); // Set the result item
                    }
                }
            }
        }
    }

}
