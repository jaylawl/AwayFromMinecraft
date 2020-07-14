package de.jaylawl.awayfromminecraft.cmd;

import de.jaylawl.awayfromminecraft.AwayFromMinecraft;
import de.jaylawl.awayfromminecraft.manager.AFKManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CmdList implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arguments) {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arguments) {
        AFKManager afkManager = AwayFromMinecraft.getAFKManager();
        Collection<String> afkPlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (afkManager.isAFK(player)) {
                afkPlayers.add(ChatColor.GOLD + player.getName() + ChatColor.RESET);
            }
        }
        if (afkPlayers.isEmpty()) {
            commandSender.sendMessage("There are currently no AFK players");
        } else {
            commandSender.sendMessage("AFK Players (" + afkPlayers.size() + "): " + Arrays.toString(afkPlayers.toArray()));
        }
        return true;
    }

}
