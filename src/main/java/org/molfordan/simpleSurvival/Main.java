package org.molfordan.simpleSurvival;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.molfordan.simpleSurvival.AllEvents.*;
import org.molfordan.simpleSurvival.Commands.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin {

    private final HashMap<String, String> locationMap = new HashMap<>();

    public static String console = ChatColor.GRAY +  "[" + ChatColor.GREEN + ChatColor.BOLD + "Console" + ChatColor.GRAY + "] ";

    public static ChatColor color = null;

    public static String notAllowed = color.RED + "You don't have permissions to do that!";

    //public List<String> suggestions = new ArrayList<>();
    private File mailFile;

    private FileConfiguration mailConfig;



    @Override
    public void onEnable() {


        createMessagesFile();
        saveMessagesConfig();
        //reloadMessagesConfig();

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
        getCommand("test").setExecutor(new testCommand());
        getCommand("playerlevel").setExecutor(new playerExpCommand());
        getCommand("mail").setExecutor(new mailCommand(mailConfig));
        getCommand("readmail").setExecutor(new readMailCommand(mailConfig));
        //commandManager.registerCommand("setlocation", new setLocationCommand(this, locationMap));
        //commandManager.registerCommand("location", new LocationCommand(this, locationMap));
        //commandManager.registerCommand("seelocation", new seePlayerLocations(this, locationMap));
        //commandManager.registerCommand("setplayerlocation", new setPlayerLocationCommand(this, locationMap));
        //commandManager.registerCommand("deletelocation", new deleteLocationCommand(this));
        //commandManager.registerCommand("simplesurvivalplugin", new pluginreloadCommand(this));
        //commandManager.registerCommand("afk", playerAfkCommand);
        //commandManager.registerCommand("tel", new playerTeleportCommand());

        // Register events
        playerMovementEvent movement = new playerMovementEvent(playerAfkCommand, 5 * 60);
        playerOnChatEvent chatEvent = new playerOnChatEvent(playerAfkCommand);

        getServer().getPluginManager().registerEvents(movement, this);
        getServer().getPluginManager().registerEvents(chatEvent, this);
        getServer().getPluginManager().registerEvents(new playerDoingCommands(), this);
        getServer().getPluginManager().registerEvents(new playerOnMentionEvent(), this);
        getServer().getPluginManager().registerEvents(new playerOnJoinEvent(mailConfig), this);
        getServer().getPluginManager().registerEvents(new playersChatEvent(), this);
        getServer().getPluginManager().registerEvents(new playerColoredAnvilEvent(), this);


        // Schedule tasks
        getServer().getScheduler().runTaskTimer(this, movement::checkInactivePlayers, 20L, 20L);

        // Save config


        getConfig().options().copyDefaults(true);


        saveConfig();
    }

    private void createMessagesFile() {
        mailFile = new File(getDataFolder(), "messages.yml");

        if (!mailFile.exists()) {
            mailFile.getParentFile().mkdirs();
            saveResource("messages.yml", false); // Save default if bundled
        }

        mailConfig = YamlConfiguration.loadConfiguration(mailFile);
    }

    public FileConfiguration getMessagesConfig() {
        return mailConfig;
    }

    public void saveMessagesConfig() {
        try {
            mailConfig.save(mailFile);
        } catch (IOException e) {
            getLogger().severe("Could not save messages.yml!");
            e.printStackTrace();
        }
    }

    public void reloadMessagesConfig() {
        mailConfig   = YamlConfiguration.loadConfiguration(mailFile);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
