package de.jaylawl.awayfromminecraft.cmd;

import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabHelper {

    public static int getArgNumber(@NotNull String[] args) {
        int i = 0;
        for (String arg : args) {
            if (!arg.equals("")) {
                i++;
            }
        }
        if (args[args.length - 1].equals("")) {
            i++;
        }
        return i;
    }

    public static @NotNull List<String> sortedCompletions(@NotNull String lastArg, @NotNull List<String> completions) {
        List<String> sortedCompletions = new ArrayList<>();
        StringUtil.copyPartialMatches(lastArg, completions, sortedCompletions);
        Collections.sort(sortedCompletions);
        return sortedCompletions;
    }

}
