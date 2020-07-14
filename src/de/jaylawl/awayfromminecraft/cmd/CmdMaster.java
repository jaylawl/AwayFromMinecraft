package de.jaylawl.awayfromminecraft.cmd;

import de.jaylawl.awayfromminecraft.AwayFromMinecraft;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CmdMaster implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arguments) {
        return TabHelper.getArgNumber(arguments) == 1 ? Collections.singletonList("reload") : Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] arguments) {
        switch (arguments.length > 0 ? arguments[0] : "") {
            case "r":
            case "reload":
                AwayFromMinecraft.inst().reload();
                commandSender.sendMessage("Reloaded AwayFromMinecraft's config.yml");
                break;
            default:
                commandSender.sendMessage(ChatColor.RED + "Unknown/missing argument(s)");
                break;
        }
        return true;
    }

}