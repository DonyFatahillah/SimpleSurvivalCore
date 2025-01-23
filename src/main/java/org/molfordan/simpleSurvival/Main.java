package org.molfordan.simpleSurvival;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.molfordan.simpleSurvival.AllEvents.*;
import org.molfordan.simpleSurvival.Commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin {

    private final HashMap<String, String> locationMap = new HashMap<>();

    public static String console = ChatColor.GRAY +  "[" + ChatColor.GREEN + ChatColor.BOLD + "Console" + ChatColor.GRAY + "] ";

    public static ChatColor color = null;

    public static String notAllowed = color.RED + "You don't have permissions to do that!";

    //public List<String> suggestions = new ArrayList<>();



    @Override
    public void onEnable() {
        playerAFKcommand playerAfkCommand = new playerAFKcommand();



        // Register all commands with CommandManager
        getCommand("deleteplayerlocation").setExecutor(new deletePlayerLocationCommand(this));
        getCommand("setlocation").setExecutor(new setLocationCommand(this, locationMap));
        getCommand("location").setExecutor(new LocationCommand(this, locationMap));
        getCommand("seelocation").setExecutor(new seePlayerLocations(this, locationMap));
        getCommand("setplayerlocation").setExecutor(new setPlayerLocationCommand(this, locationMap));
        getCommand("deletelocation").setExecutor(new deleteLocationCommand(this));
        getCommand("simplesurvivalplugin").setExecutor(new pluginreloadCommand(this));
        getCommand("afk").setExecutor(playerAfkCommand);
        getCommand("tel").setExecutor(new playerTeleportCommand());
        getCommand("nick").setExecutor(new playerNickCommand());
        getCommand("ping").setExecutor(new pingCommand());
        //commandManager.registerCommand("setlocation", new setLocationCommand(this, locationMap));
        //commandManager.registerCommand("location", new LocationCommand(this, locationMap));
        //commandManager.registerCommand("seelocation", new seePlayerLocations(this, locationMap));
        //commandManager.registerCommand("setplayerlocation", new setPlayerLocationCommand(this, locationMap));
        //commandManager.registerCommand("deletelocation", new deleteLocationCommand(this));
        //commandManager.registerCommand("simplesurvivalplugin", new pluginreloadCommand(this));
        //commandManager.registerCommand("afk", playerAfkCommand);
        //commandManager.registerCommand("tel", new playerTeleportCommand());

        // Register events
        playerMovementEvent movement = new playerMovementEvent(playerAfkCommand);
        playerOnChatEvent chatEvent = new playerOnChatEvent(playerAfkCommand);

        getServer().getPluginManager().registerEvents(movement, this);
        getServer().getPluginManager().registerEvents(chatEvent, this);
        getServer().getPluginManager().registerEvents(new playerDoingCommands(), this);
        getServer().getPluginManager().registerEvents(new playerOnMentionEvent(), this);
        getServer().getPluginManager().registerEvents(new playerOnJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new playersChatEvent(), this);

        // Schedule tasks
        getServer().getScheduler().runTaskTimer(this, movement::checkInactivePlayers, 20L, 20L);

        // Save config


        getConfig().options().copyDefaults(true);


        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
