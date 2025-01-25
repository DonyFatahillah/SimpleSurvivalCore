package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class mailCommand implements TabExecutor {


    private final FileConfiguration mailConfig;

    public mailCommand(FileConfiguration mailConfig) {
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
        String message = String.join(" ", args[1]);

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetPlayerName);

        if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
            playerSender.sendMessage("§cPlayer not found!");
            return true;
        }



        String uuid = targetPlayer.getUniqueId().toString();
        String path = targetPlayer.getName() + ".messages." + uuid;
        List<String> messages = mailConfig.getStringList(path);
        messages.add(message);
        mailConfig.set(path, messages);

        sender.sendMessage("§aMessage sent to " + targetPlayer.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {




        return List.of();
    }
}
