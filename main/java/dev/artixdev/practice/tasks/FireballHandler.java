package dev.artixdev.practice.tasks;

import org.bukkit.entity.Fireball;

/**
 * Handler interface for fireball-related operations
 */
public interface FireballHandler {
    
    /**
     * Update fireball physics and effects
     * @param fireball the fireball to update
     */
    void updateFireball(Fireball fireball);
    
    /**
     * Check if fireball should be removed
     * @param fireball the fireball to check
     * @return true if fireball should be removed
     */
    boolean shouldRemoveFireball(Fireball fireball);
}
