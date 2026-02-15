package dev.artixdev.practice.cooldowns;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractCooldown extends BaseCooldown implements Listener {
    private static final String[] INTERACT_CONSTANTS = {"InteractCooldown"};
    private static final int DEFAULT_DURATION = 10000;
    private static final String[] INTERACT_MESSAGES = {"InteractCooldown"};
    public static final boolean DEBUG_MODE = false;
    public static final int COOLDOWN_VERSION = 1;
    
    // Track last interaction time per player: UUID -> lastInteractionTime
    private final Map<UUID, Long> lastInteractionTimes = new ConcurrentHashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // Check if player has interacted recently
        Long lastInteractionTime = lastInteractionTimes.get(playerId);
        
        if (lastInteractionTime != null) {
            long timeSinceLastInteraction = currentTime - lastInteractionTime;
            long cooldownDuration = getDefaultDuration();
            
            if (timeSinceLastInteraction < cooldownDuration) {
                // Player is still in cooldown, cancel the event
                event.setCancelled(true);
                
                if (DEBUG_MODE) {
                    long remainingTime = cooldownDuration - timeSinceLastInteraction;
                    player.sendMessage(INTERACT_MESSAGES[0] + " - Wait " + (remainingTime / 1000.0) + "s");
                }
                return;
            }
        }
        
        // Update last interaction time
        lastInteractionTimes.put(playerId, currentTime);
        
        if (DEBUG_MODE) {
            player.sendMessage(INTERACT_MESSAGES[0] + " - Interaction allowed");
        }
    }

    static {
        INTERACT_CONSTANTS[0] = "InteractCooldown";
        INTERACT_MESSAGES[0] = "InteractCooldown";
    }

    public InteractCooldown() {
        super("Interact Cooldown", 10000L);
    }
    
    /**
     * Get remaining cooldown time for a player
     * @param player the player
     * @return remaining cooldown in milliseconds, or 0 if no cooldown
     */
    public long getRemainingCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        Long lastInteractionTime = lastInteractionTimes.get(playerId);
        
        if (lastInteractionTime == null) {
            return 0;
        }
        
        long timeSinceLastInteraction = System.currentTimeMillis() - lastInteractionTime;
        long cooldownDuration = getDefaultDuration();
        
        if (timeSinceLastInteraction >= cooldownDuration) {
            return 0;
        }
        
        return cooldownDuration - timeSinceLastInteraction;
    }
    
    /**
     * Check if player is in cooldown
     * @param player the player
     * @return true if player is in cooldown
     */
    public boolean isInCooldown(Player player) {
        return getRemainingCooldown(player) > 0;
    }
    
    /**
     * Reset cooldown for a player
     * @param player the player
     */
    public void resetCooldown(Player player) {
        lastInteractionTimes.remove(player.getUniqueId());
    }
    
    /**
     * Clear all cooldowns (useful for cleanup)
     */
    public void clearAllCooldowns() {
        lastInteractionTimes.clear();
    }
}
