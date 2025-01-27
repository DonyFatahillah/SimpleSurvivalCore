package org.molfordan.simpleSurvival.AllEvents;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.molfordan.simpleSurvival.Main.color;

public class milkPlayerEvent implements Listener {

    @EventHandler
    public void onPlayerMilk(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();

        if(event.getRightClicked() instanceof Player){
            Player clickedPlayer = (Player) event.getRightClicked();

            ItemStack itemStack = player.getInventory().getItemInMainHand();

            if (itemStack.getType() == Material.BUCKET){
                if (itemStack.getAmount() > 1) {
                    itemStack.setAmount(itemStack.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }

                ItemStack bucketMilk = new ItemStack(Material.MILK_BUCKET);

                ItemMeta meta = bucketMilk.getItemMeta();

                meta.setDisplayName(color.WHITE+clickedPlayer.getName() + "'s milk");

                bucketMilk.setItemMeta(meta);

                player.getInventory().setItemInMainHand(bucketMilk);


            }
        }
    }
    @EventHandler
    public void onEntityMilk(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (entity.getType() == EntityType.SHEEP){
            if (itemStack.getType() == Material.BUCKET){
                if (itemStack.getAmount() > 1) {
                    itemStack.setAmount(itemStack.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }

                ItemStack bucketMilk = new ItemStack(Material.MILK_BUCKET);

                ItemMeta meta = bucketMilk.getItemMeta();

                meta.setDisplayName(color.WHITE + "Sheep's milk");

                bucketMilk.setItemMeta(meta);

                player.getInventory().setItemInMainHand(bucketMilk);


            }
        }


    }
}
