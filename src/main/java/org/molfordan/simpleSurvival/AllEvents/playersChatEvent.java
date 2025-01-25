package org.molfordan.simpleSurvival.AllEvents;

import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.Map;

import static org.molfordan.simpleSurvival.Main.color;

public class playersChatEvent implements Listener {

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        event.setFormat("<" + player.getDisplayName() + ChatColor.RESET + "> " + ChatColor.translateAlternateColorCodes('&', message));
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Check for [i] in the message
        if (message.contains("[i]")) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null && item.getType() != Material.AIR) {
                String itemName = getItemName(item);
                String hoverText = createHoverText(item, itemName);

                TextComponent itemMessage = new TextComponent(ChatColor.GRAY + "[" + ChatColor.RESET +
                        ChatColor.translateAlternateColorCodes('&', itemName) +
                        " x" + item.getAmount() + ChatColor.GRAY + "]");
                itemMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));

                // Replace [i] with the hoverable item message
                event.setMessage(message.replace("[i]", itemMessage.getText()));
            }
        }
    }

    private String getItemName(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return (meta != null && meta.hasDisplayName()) ? meta.getDisplayName() : ChatColor.WHITE + formatMaterialName(item.getType().name());
    }

    private String createHoverText(ItemStack item, String itemName) {
        ItemMeta meta = item.getItemMeta();
        StringBuilder hoverText = new StringBuilder();

        hoverText.append(ChatColor.YELLOW).append(itemName).append("\n");
        hoverText.append(ChatColor.GRAY).append("Amount: ").append(ChatColor.WHITE).append(item.getAmount()).append("\n");

        if (meta != null && meta.hasEnchants()) {
            hoverText.append(ChatColor.LIGHT_PURPLE).append("Enchantments:\n");
            for (Map.Entry<Enchantment, Integer> enchantment : meta.getEnchants().entrySet()) {
                hoverText.append(ChatColor.AQUA)
                        .append(formatMaterialName(enchantment.getKey().getKey().getKey()))
                        .append(" ")
                        .append(enchantment.getValue())
                        .append("\n");
            }
        } else {
            hoverText.append(ChatColor.RED).append("No Enchantments");
        }

        return hoverText.toString();
    }

    private String formatMaterialName(String materialName) {
        String[] words = materialName.toLowerCase().split("_");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            formatted.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return formatted.toString().trim();
    }
}
