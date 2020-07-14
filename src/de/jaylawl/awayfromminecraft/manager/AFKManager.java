package de.jaylawl.awayfromminecraft.manager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AFKManager {

    public static int MAX_INACTIVITY_SECONDS = 30;
    public static String IGNORE_PERMISSION = null;
    public static String ON_AFK_MESSAGE = null;
    public static String WHILE_AFK_MESSAGE = null;
    public static String ON_RETURN_MESSAGE = null;

    private static final Random RANDOM = new Random();

    private static final ConcurrentHashMap<OfflinePlayer, Long> LAST_ACTION_TIMESTAMP = new ConcurrentHashMap<>();
    private static final Set<OfflinePlayer> AFK_PLAYERS = new HashSet<>();

    private int notificationCooldown = 19;

    //

    public void evaluate() {
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collection<Player> filter = new ArrayList<>();
        for (Player player : onlinePlayers) {
            if (AFK_PLAYERS.contains(player) || player.hasPermission(IGNORE_PERMISSION)) {
                filter.add(player);
            }
        }
        if (!filter.isEmpty()) {
            onlinePlayers.removeAll(filter);
        }
        if (onlinePlayers.size() > 0) {
            Player player = onlinePlayers.get(RANDOM.nextInt(onlinePlayers.size()));
            if (LAST_ACTION_TIMESTAMP.containsKey(player)) {
                if ((System.currentTimeMillis() - LAST_ACTION_TIMESTAMP.get(player)) / 1000 > MAX_INACTIVITY_SECONDS) {
                    setAFK(player);
                }
            }
        }
    }

    public void notifyAFKPlayers() {
        if (WHILE_AFK_MESSAGE != null) {
            if (this.notificationCooldown > 0) {
                this.notificationCooldown--;
            } else {
                this.notificationCooldown = 19;
                Collection<OfflinePlayer> clearList = new ArrayList<>();
                for (OfflinePlayer offlinePlayer : AFK_PLAYERS) {
                    if (!offlinePlayer.isOnline()) {
                        clearList.add(offlinePlayer);
                    } else {
                        ((Player) offlinePlayer).sendActionBar(WHILE_AFK_MESSAGE);
                    }
                }
                if (!clearList.isEmpty()) {
                    AFK_PLAYERS.removeAll(clearList);
                }
            }
        }
    }

    public void clearAll() {
        LAST_ACTION_TIMESTAMP.clear();
        AFK_PLAYERS.clear();
    }

    public void clearPlayer(@NotNull OfflinePlayer player) {
        LAST_ACTION_TIMESTAMP.remove(player);
        AFK_PLAYERS.remove(player);
    }

    public void playerEvent(@NotNull OfflinePlayer player) {
        LAST_ACTION_TIMESTAMP.put(player, System.currentTimeMillis());
        setBack(player);
    }

    public void setAFK(@NotNull OfflinePlayer player) {
        if (!isAFK(player) && ON_AFK_MESSAGE != null) {
            Bukkit.broadcastMessage(ON_AFK_MESSAGE.replace("%player%", (player.getName() + "")));
        }
        AFK_PLAYERS.add(player);
    }

    public void setBack(@NotNull OfflinePlayer player) {
        if (isAFK(player) && ON_RETURN_MESSAGE != null) {
            Bukkit.broadcastMessage(ON_RETURN_MESSAGE.replace("%player%", (player.getName() + "")));
        }
        AFK_PLAYERS.remove(player);
    }

    public boolean isAFK(@NotNull OfflinePlayer player) {
        return AFK_PLAYERS.contains(player);
    }

    public int getAFKSeconds(@NotNull OfflinePlayer player) {
        long value = 0;
        if (AFK_PLAYERS.contains(player)) {
            value = System.currentTimeMillis() - LAST_ACTION_TIMESTAMP.getOrDefault(player, 0L);
        }
        return (int) (value / 1000);
    }

    public int getAFKMinutes(@NotNull OfflinePlayer player) {
        return getAFKSeconds(player) / 60;
    }

}
