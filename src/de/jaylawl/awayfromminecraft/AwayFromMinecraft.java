package de.jaylawl.awayfromminecraft;

import de.jaylawl.awayfromminecraft.cmd.CmdCheck;
import de.jaylawl.awayfromminecraft.cmd.CmdList;
import de.jaylawl.awayfromminecraft.cmd.CmdMaster;
import de.jaylawl.awayfromminecraft.event.bukkit.PlayerEventListener;
import de.jaylawl.awayfromminecraft.manager.AFKManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AwayFromMinecraft extends JavaPlugin {

    private static AwayFromMinecraft INSTANCE;
    private AFKManager afkManager;

    @Override
    public void onEnable() {

        INSTANCE = this;
        this.afkManager = new AFKManager();

        this.saveDefaultConfig();
        reload();

        PluginManager pluginManager = Bukkit.getPluginManager();

        PluginCommand masterCommand = getCommand("awayfromminecraft");
        if (masterCommand != null) {
            CmdMaster cmdMaster = new CmdMaster();
            masterCommand.setExecutor(cmdMaster);
            masterCommand.setTabCompleter(cmdMaster);
        } else {
            getLogger().severe(ChatColor.RED + "Failed to register plugin's master command; disabling plugin...");
            pluginManager.disablePlugin(this);
            return;
        }

        PluginCommand isAFKCommand = getCommand("isafk");
        if (isAFKCommand != null) {
            CmdCheck cmdCheck = new CmdCheck();
            isAFKCommand.setExecutor(cmdCheck);
            isAFKCommand.setTabCompleter(cmdCheck);
        } else {
            getLogger().severe(ChatColor.RED + "Failed to register plugin's \"/isafk\" command");
            pluginManager.disablePlugin(this);
            return;
        }

        PluginCommand listAFK = getCommand("listafk");
        if (listAFK != null) {
            CmdList cmdList = new CmdList();
            listAFK.setExecutor(cmdList);
            listAFK.setTabCompleter(cmdList);
        } else {
            getLogger().severe(ChatColor.RED + "Failed to register plugin's \"/listafk\" command");
        }

        pluginManager.registerEvents(new PlayerEventListener(), this);

        if (Bukkit.getScheduler().scheduleSyncRepeatingTask(this, INSTANCE::clock, 1L, 1L) < 0) {
            getLogger().severe(ChatColor.RED + "Failed to initialize plugin's clock; disabling plugin...");
            pluginManager.disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
    }

    //

    private void clock() {
        INSTANCE.afkManager.evaluate();
        INSTANCE.afkManager.notifyAFKPlayers();
    }

    public void reload() {

        INSTANCE.reloadConfig();
        FileConfiguration config = getConfig();

        AFKManager.MAX_INACTIVITY_SECONDS = config.getInt("MaxInactivitySeconds", 30);

        String onAFKMessage = null;
        if (config.getBoolean("OnAFK.Broadcast")) {
            onAFKMessage = config.getString("OnAFK.ChatMessage");
            if (onAFKMessage != null) {
                onAFKMessage = ChatColor.translateAlternateColorCodes('&', onAFKMessage);
                onAFKMessage = ChatColor.translateAlternateColorCodes('§', onAFKMessage);
            }
        }
        AFKManager.ON_AFK_MESSAGE = onAFKMessage;

        String whileAFKMessage = null;
        if (config.getBoolean("WhileAFK.NotifyPlayer")) {
            whileAFKMessage = config.getString("WhileAFK.ActionbarMessage");
            if (whileAFKMessage != null) {
                whileAFKMessage = ChatColor.translateAlternateColorCodes('&', whileAFKMessage);
                whileAFKMessage = ChatColor.translateAlternateColorCodes('§', whileAFKMessage);
            }
        }
        AFKManager.WHILE_AFK_MESSAGE = whileAFKMessage;

        String onReturnMessage = null;
        if (config.getBoolean("OnReturn.Broadcast")) {
            onReturnMessage = config.getString("OnReturn.ChatMessage");
            if (onReturnMessage != null) {
                onReturnMessage = ChatColor.translateAlternateColorCodes('&', onReturnMessage);
                onReturnMessage = ChatColor.translateAlternateColorCodes('§', onReturnMessage);
            }
        }
        AFKManager.ON_RETURN_MESSAGE = onReturnMessage;

        AFKManager.IGNORE_PERMISSION = config.getString("IgnorePermission");

    }

    //

    public static AwayFromMinecraft inst() {
        return INSTANCE;
    }

    public static AFKManager getAFKManager() {
        return INSTANCE.afkManager;
    }

}
