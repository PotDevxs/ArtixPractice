package dev.artixdev.practice.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    private static final String[] DAMAGE_CONSTANTS = {"EntityDamageListener"};
    public static final int LISTENER_VERSION = 1;
    private static final String[] DAMAGE_TYPES = new String[7];
    public static final boolean DEBUG_MODE = false;

    static {
        DAMAGE_TYPES[0] = "DAMAGE";
        DAMAGE_TYPES[1] = "FIRE";
        DAMAGE_TYPES[2] = "FALL";
        DAMAGE_TYPES[3] = "EXPLOSION";
        DAMAGE_TYPES[4] = "VOID";
        DAMAGE_TYPES[5] = "SUFFOCATION";
        DAMAGE_TYPES[6] = "DROWNING";
    }

    public EntityDamageListener() {
        super();
    }

    @EventHandler(
        ignoreCancelled = true,
        priority = EventPriority.HIGHEST
    )
    public void onEntityDamage(EntityDamageEvent event) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for entity damage handling
    }

    @EventHandler(
        priority = EventPriority.NORMAL
    )
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for entity damage by entity handling
    }
}
