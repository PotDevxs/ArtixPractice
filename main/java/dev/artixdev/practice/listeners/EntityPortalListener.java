package dev.artixdev.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import dev.artixdev.practice.Main;

public class EntityPortalListener implements Listener {
    public static final int LISTENER_VERSION = 1;
    private static final String[] PORTAL_CONSTANTS = new String[1];
    private static final String[] PORTAL_TYPES = {"NETHER_PORTAL", "END_PORTAL"};
    public static final boolean DEBUG_MODE = false;

    static {
        PORTAL_CONSTANTS[0] = "EntityPortalListener";
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        Main main = Main.getInstance();
        if (main != null && main.getMatchManager() != null && main.getMatchManager().getMatchByPlayer(player) != null) {
            event.setCancelled(true);
        }
    }
}
