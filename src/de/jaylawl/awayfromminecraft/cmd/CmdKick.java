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

public class CmdKick implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arguments) {
        AFKManager afkManager = AwayFromMinecraft.getAFKManager();
        int argNumber = TabHelper.getArgNumber(arguments);
        if (argNumber == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("*");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (afkManager.isAFK(player)) {
                    completions.add(player.getName());
                }
            }
            return TabHelper.sortedCompletions(arguments[argNumber - 1], completions);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (arguments.length < 1) {
            commandSender.sendMessage(ChatColor.GREEN + "/kickafk" + ChatColor.RED + " <player>" + ChatColor.GRAY + " [reason]");
            return true;
        }

        String kickReason = null;
        if (arguments.length > 1) {
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (String argument : arguments) {
                if (i > 0) {
                    builder.append(argument).append(" ");
                }
                i++;
            }
            kickReason = builder.toString();
            kickReason = ChatColor.translateAlternateColorCodes('&', kickReason);
        }

        AFKManager afkManager = AwayFromMinecraft.getAFKManager();
        Collection<Player> toBeKicked = new ArrayList<>();

        if (arguments[0].equals("*")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (afkManager.isAFK(player)) {
                    toBeKicked.add(player);
                }
            }
            if (toBeKicked.isEmpty()) {
                commandSender.sendMessage(ChatColor.RED + "Couldn't find any AFK players to kick");
                return true;
            }
        } else {
            Player player = Bukkit.getPlayer(arguments[0]);
            if (player == null) {
                commandSender.sendMessage(ChatColor.RED + "\"" + arguments[0] + "\" is not an online player");
                return true;
            } else if (!afkManager.isAFK(player)) {
                commandSender.sendMessage(ChatColor.RED + player.getName() + " is not currently AFK");
                return true;
            } else {
                toBeKicked.add(player);
            }
        }

        Collection<String> kicked = new ArrayList<>();
        for (Player kickee : toBeKicked) {
            kickee.kickPlayer(kickReason);
            kicked.add(ChatColor.GOLD + kickee.getName() + ChatColor.RESET);
        }
        commandSender.sendMessage("Kicked player(s) " + Arrays.toString(kicked.toArray()) + " for [" + (kickReason != null ? kickReason : "") + "]");
        return true;
    }

}
