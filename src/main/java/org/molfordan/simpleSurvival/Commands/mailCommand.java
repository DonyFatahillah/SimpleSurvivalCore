package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class mailCommand implements TabExecutor {


    private final Main plugin;

    private final FileConfiguration mailConfig;

    public mailCommand(Main plugin, FileConfiguration mailConfig) {
        this.plugin = plugin;
        this.mailConfig = mailConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("you're not allowed to use this command on console!");
            return true;
        }

        Player playerSender = (Player) sender;

        if (args.length < 2) {
            playerSender.sendMessage("§cUsage: /mail <player> <message>");
            return true;
        }

        String targetPlayerName = args[0];
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetPlayerName);

        if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
            playerSender.sendMessage("§cPlayer not found!");
            return true;
        }



        String uuid = targetPlayer.getUniqueId().toString();
        String path = targetPlayer.getName() + ".messages." + uuid;
        List<String> messages = mailConfig.getStringList(path);
        messages.add("From "+playerSender.getName()+" : " +message);
        mailConfig.set(path, messages);

        playerSender.sendMessage("§aMessage sent to " + targetPlayer.getName());




        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        PluginCommand cmd = plugin.getCommand("mail");

        if (cmd == null){
            return suggestions;
        }

        if (args.length == 0){
            List<String> aliases = cmd.getAliases();
            if (aliases != null && !aliases.isEmpty()) {
                suggestions.addAll(aliases);
            }


        } else if (args.length == 1){
            for (Player player : Bukkit.getOnlinePlayers()){
                suggestions.add(player.getName());
            }
        } else if (args.length > 1){
            suggestions.add("<message>");
        }


        return suggestions;
    }
}
