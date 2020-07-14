package de.jaylawl.awayfromminecraft.event.bukkit;

import de.jaylawl.awayfromminecraft.AwayFromMinecraft;
import de.jaylawl.awayfromminecraft.manager.AFKManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

public class PlayerEventListener implements Listener {

    private final AFKManager afkManager = AwayFromMinecraft.getAFKManager();

    //

    @EventHandler(priority = EventPriority.MONITOR)
    public void join(@NotNull PlayerJoinEvent event) {
        this.afkManager.clearPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void quit(@NotNull PlayerQuitEvent event) {
        this.afkManager.clearPlayer(event.getPlayer());
    }

    //

    @EventHandler(priority = EventPriority.MONITOR)
    public void chat(@NotNull AsyncPlayerChatEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    // this one doesn't seem to work
    @EventHandler(priority = EventPriority.MONITOR)
    public void sendCommand(@NotNull PlayerCommandSendEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void dropItem(@NotNull PlayerDropItemEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void swapItems(@NotNull PlayerSwapHandItemsEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void editBook(@NotNull PlayerEditBookEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void interact(@NotNull PlayerInteractEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void consumeItem(@NotNull PlayerItemConsumeEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void hotbarChange(@NotNull PlayerItemHeldEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void move(@NotNull PlayerMoveEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void toggleSneak(@NotNull PlayerToggleSneakEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void toggleSprint(@NotNull PlayerToggleSprintEvent event) {
        this.afkManager.playerEvent(event.getPlayer());
    }

    //

    @EventHandler(priority = EventPriority.MONITOR)
    public void chat(@NotNull InventoryClickEvent event) {
        this.afkManager.playerEvent(((Player) event.getWhoClicked()));
    }

}
