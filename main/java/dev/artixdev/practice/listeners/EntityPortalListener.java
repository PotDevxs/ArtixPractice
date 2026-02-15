package dev.artixdev.practice.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;

public class EntityPortalListener implements Listener {
    public static final int LISTENER_VERSION = 1;
    private static final String[] PORTAL_CONSTANTS = new String[1];
    private static final String[] PORTAL_TYPES = {"NETHER_PORTAL", "END_PORTAL"};
    public static final boolean DEBUG_MODE = false;

    static {
        PORTAL_CONSTANTS[0] = "EntityPortalListener";
    }

    @EventHandler(
        priority = EventPriority.HIGH,
        ignoreCancelled = true
    )
    public void onEntityPortalEnter(EntityPortalEnterEvent event) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for entity portal enter handling
        // Typically used to prevent players from entering portals during matches
    }
}
