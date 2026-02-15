package dev.artixdev.practice.cooldowns;

import java.util.UUID;
import org.bukkit.entity.Player;
import dev.artixdev.practice.managers.BaseCooldownManager;

/**
 * Base Cooldown
 * Base class for cooldown implementations
 */
public abstract class BaseCooldown extends BaseCooldownManager {
    
    /**
     * Constructor
     * @param name the cooldown name
     * @param duration the cooldown duration in milliseconds
     */
    public BaseCooldown(String name, long duration) {
        super(name, duration);
    }
    
    /**
     * Called when cooldown ends for a player
     * @param player the player
     * @param playerUuid the player UUID
     */
    protected void onCooldownEnd(Player player, UUID playerUuid) {
        // Override in subclasses to handle cooldown end events
    }
}
