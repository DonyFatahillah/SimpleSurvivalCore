package org.molfordan.simpleSurvival.Commands;

//import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

import static org.molfordan.simpleSurvival.Main.color;


public class playerAFKcommand implements TabExecutor {

    private final HashSet<UUID> afkPlayers = new HashSet<>();

    private final HashMap<UUID, String> afkReasons = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players are allowed to use this command!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {

            toggleAFK(player, null);

            return true;
        } else {

            //String reason = args[0];

            StringBuilder builder = new StringBuilder();

            for(int i = 0; i < args.length; i++){
                builder.append(args[i]);
                builder.append(" ");
            }

            String finalMessage = builder.toString();
            finalMessage = finalMessage.stripTrailing();

            toggleAFK(player, finalMessage);


        }
        return true;
    }

    public void toggleAFK(Player player, String reason) {
        if (!afkPlayers.contains(player.getUniqueId())) {
            setAfk(player, true, reason);
        } else {
            setAfk(player, false, reason);
        }
    }

    public void setAfk(Player player, boolean isAfk, String reason) {
        if (isAfk) {
            afkPlayers.add(player.getUniqueId());
            afkReasons.put(player.getUniqueId(), reason);
            if (reason != null) {

                Bukkit.broadcastMessage(color.GREEN + "* "+ player.getName() + " is now AFK! Reason: " + color.translateAlternateColorCodes('&',afkReasons.get(player.getUniqueId())));
                player.sendTitle(color.YELLOW+"You are now AFK!", color.GREEN+"move to remove this message!", 10,  6000, 20);
            } else {
                Bukkit.broadcastMessage(color.GREEN + "* "+ player.getName() + " is now AFK!");
                player.sendTitle(color.YELLOW+"You are now AFK!", color.GREEN+"move to remove this message!", 10,  6000, 20);
            }
        } else {
            afkPlayers.remove(player.getUniqueId());
            afkReasons.remove(player.getUniqueId());
            Bukkit.broadcastMessage(color.GREEN + player.getName() + " is no longer AFK!");
        }
    }

    public boolean isAfk(Player player) {
        return afkPlayers.contains(player.getUniqueId());
    }

    public String getAfkReason(Player player) {
        return afkReasons.getOrDefault(player.getUniqueId(), "No reason provided");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (args.length > 0){
            suggestions.add("reasonWord" + args.length);
        }


        return suggestions;
    }
}
