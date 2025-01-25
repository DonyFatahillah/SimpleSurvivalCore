package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.molfordan.simpleSurvival.Main;

import java.util.List;

import static org.molfordan.simpleSurvival.Main.color;

public class readMailCommand implements CommandExecutor {

    private final FileConfiguration mailConfig;

    public readMailCommand(FileConfiguration mailConfig) {
        this.mailConfig = mailConfig;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("you can't execute this command!");
            return true;
        }

        Player player = (Player) sender;

        String uuid = player.getUniqueId().toString();

        List<String> messages = mailConfig.getStringList(player.getName()+".messages."+uuid);

        if (args.length == 0){
            if (!messages.isEmpty()) {
                player.sendMessage("§aYou have new messages:");
                for (String message : messages) {
                    player.sendMessage("§7- §f" + message);
                }
                // Clear the messages after displaying them
                mailConfig.set(player.getName() + ".messages." + uuid, null);

                //Main plugin = (Main) Bukkit.getPluginManager().getPlugin("simpleSurvival");
                //if (plugin != null) {
                //    plugin.saveMessagesConfig();
                //} else {

                //}
                return true;
            } else {
                player.sendMessage(color.GREEN+"You have no new messages!");

            }
            return true;
        }


        return true;
    }
}
