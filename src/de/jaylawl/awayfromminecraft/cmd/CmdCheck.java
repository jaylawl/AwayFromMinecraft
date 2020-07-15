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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdCheck implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arguments) {
        int argNumber = TabHelper.getArgNumber(arguments);
        List<String> completions = new ArrayList<>();
        if (argNumber == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
            return TabHelper.sortedCompletions(arguments[argNumber - 1], completions);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arguments) {
        if (arguments.length < 1) {
            commandSender.sendMessage(ChatColor.GREEN + "/isafk " + ChatColor.RED + "<player>");
        } else {
            Player player = Bukkit.getPlayer(arguments[0]);
            if (player == null) {
                commandSender.sendMessage(ChatColor.RED + "\"" + arguments[0] + "\" is not an online player");
            } else {
                AFKManager afkManager = AwayFromMinecraft.getAFKManager();
                if (afkManager.isAFK(player)) {
                    int seconds = afkManager.getAFKSeconds(player);
                    int minutes = seconds / 60;
                    seconds %= 60;
                    commandSender.sendMessage(player.getName() + " is AFK since " + minutes + " minute(s) and " + seconds + " second(s)");
                } else {
                    commandSender.sendMessage(player.getName() + " is not currently AFK");
                }
            }
        }
        return true;
    }
}
