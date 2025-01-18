package org.molfordan.simpleSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

import static org.molfordan.simpleSurvival.Main.color;
import static org.molfordan.simpleSurvival.Main.notAllowed;

public class playerTeleportCommand implements TabExecutor {

    private final HashMap<UUID, Long> telCooldown;
    private final HashMap<UUID, Long> telHereCooldown;

    public playerTeleportCommand() {
        this.telCooldown = new HashMap<>();
        this.telHereCooldown = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player playerSender = (Player) sender;

        if (!playerSender.hasPermission("simplesurvival.tp")){
            playerSender.sendMessage(notAllowed);
            return true;
        } else {

            if (args.length == 0) {
                Bukkit.getLogger().log(Level.INFO, "DEBUG 1");
                playerSender.sendMessage(color.RED + "Usage: /tel [playerName] or /tel here [playerName]");
                return true;
            }

            if (args[0].equalsIgnoreCase("here")) {
                Bukkit.getLogger().log(Level.INFO, "DEBUG 2");
                if (args.length < 2) {
                    playerSender.sendMessage(color.RED + "Usage: /tel here [playerName]");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    Bukkit.getLogger().log(Level.INFO, "DEBUG 3");
                    playerSender.sendMessage(color.RED + "Player doesn't exist or is not online!");
                    return true;
                }

                if (!this.telHereCooldown.containsKey(playerSender.getUniqueId())) {
                    Bukkit.getLogger().log(Level.INFO, "DEBUG 4");
                    this.telHereCooldown.put(playerSender.getUniqueId(), System.currentTimeMillis());
                    target.teleport(playerSender.getLocation());
                    target.sendMessage(color.GREEN + "You have been teleported to " + playerSender.getName());
                    playerSender.sendMessage(color.GREEN + target.getName() + " teleported to you.");
                    return true;
                } else {
                    long timeElapsed = System.currentTimeMillis() - telHereCooldown.get(playerSender.getUniqueId());

                    if (timeElapsed >= 10000) {
                        Bukkit.getLogger().log(Level.INFO, "DEBUG 5");
                        this.telHereCooldown.put(playerSender.getUniqueId(), System.currentTimeMillis());
                        target.teleport(playerSender.getLocation());
                        target.sendMessage(color.GREEN + "You have been teleported to " + playerSender.getName());
                        playerSender.sendMessage(color.GREEN + target.getName() + " teleported to you.");
                        return true;
                    } else {
                        playerSender.sendMessage(color.RED + "Please wait for another " + ((10000 - timeElapsed) / 1000) + " seconds");
                        return true;
                    }
                }

            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null || !target.isOnline()) {
                    Bukkit.getLogger().log(Level.INFO, "DEBUG 6");
                    playerSender.sendMessage(color.RED + "Player doesn't exist or is not online!");
                    return true;
                }

                if (!this.telCooldown.containsKey(playerSender.getUniqueId())) {
                    Bukkit.getLogger().log(Level.INFO, "DEBUG 7");
                    this.telCooldown.put(playerSender.getUniqueId(), System.currentTimeMillis());
                    playerSender.teleport(target.getLocation());
                    playerSender.sendMessage(color.GREEN + "You have been teleported to " + target.getName());
                    target.sendMessage(color.GREEN + playerSender.getName() + " teleported to you.");
                    return true;
                } else {
                    long timeElapsed = System.currentTimeMillis() - telCooldown.get(playerSender.getUniqueId());

                    if (timeElapsed >= 10000) {
                        Bukkit.getLogger().log(Level.INFO, "DEBUG 8");
                        this.telCooldown.put(playerSender.getUniqueId(), System.currentTimeMillis());
                        playerSender.teleport(target.getLocation());
                        playerSender.sendMessage(color.GREEN + "You have been teleported to " + target.getName());
                        target.sendMessage(color.GREEN + playerSender.getName() + " teleported to you.");
                        return true;
                    } else {
                        playerSender.sendMessage(color.RED + "Please wait for another " + ((10000 - timeElapsed) / 1000) + " seconds");
                        return true;
                    }
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> suggestions = new ArrayList<>();

        if (!sender.hasPermission("simplesurvival.tp")) {
            sender.sendMessage(notAllowed);
            return Collections.emptyList();
        }


        if (args.length == 1) {
            suggestions.add("here");
            for (Player player : Bukkit.getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("here")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
        }

        return suggestions;
    }
}


