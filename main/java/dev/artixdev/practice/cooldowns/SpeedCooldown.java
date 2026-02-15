package dev.artixdev.practice.cooldowns;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedCooldown extends BaseCooldown implements Listener {
    public static final boolean DEBUG_MODE = false;
    private static final PotionEffect SPEED_EFFECT_1 = new PotionEffect(PotionEffectType.SPEED, 160, 3);
    public static final int COOLDOWN_VERSION = 1;
    private static final String[] SPEED_CONSTANTS = new String[7];
    private static final String[] SPEED_MESSAGES = new String[7];
    private static final PotionEffect SPEED_EFFECT_2 = new PotionEffect(PotionEffectType.SPEED, 200, 4);
    
    // Track last speed activation time per player: UUID -> lastSpeedTime
    private final Map<UUID, Long> lastSpeedTimes = new ConcurrentHashMap<>();
    
    // Track which effect to use next (alternate between effects)
    private final Map<UUID, Boolean> useEffect1 = new ConcurrentHashMap<>();

    static {
        SPEED_CONSTANTS[0] = "Speed Cooldown";
        SPEED_CONSTANTS[1] = "Speed Effect";
        SPEED_CONSTANTS[2] = "Speed Boost";
        SPEED_CONSTANTS[3] = "Speed Power";
        SPEED_CONSTANTS[4] = "Speed Duration";
        SPEED_CONSTANTS[5] = "Speed Amplifier";
        SPEED_CONSTANTS[6] = "Speed Type";
        
        SPEED_MESSAGES[0] = "Speed cooldown started!";
        SPEED_MESSAGES[1] = "Speed effect applied!";
        SPEED_MESSAGES[2] = "Speed boost active!";
        SPEED_MESSAGES[3] = "Speed power increased!";
        SPEED_MESSAGES[4] = "Speed duration extended!";
        SPEED_MESSAGES[5] = "Speed amplifier boosted!";
        SPEED_MESSAGES[6] = "Speed type changed!";
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // Check if player has used speed recently
        Long lastSpeedTime = lastSpeedTimes.get(playerId);
        
        if (lastSpeedTime != null) {
            long timeSinceLastSpeed = currentTime - lastSpeedTime;
            long cooldownDuration = getDefaultDuration();
            
            if (timeSinceLastSpeed < cooldownDuration) {
                // Player is still in cooldown, don't apply effect
                if (DEBUG_MODE) {
                    long remainingTime = cooldownDuration - timeSinceLastSpeed;
                    player.sendMessage(SPEED_MESSAGES[0] + " - Wait " + (remainingTime / 1000.0) + "s");
                }
                return;
            }
        }
        
        // Determine which effect to use (alternate between effects)
        Boolean useFirstObj = useEffect1.get(playerId);
        boolean useFirst = (useFirstObj == null) ? true : useFirstObj;
        PotionEffect effectToApply = useFirst ? SPEED_EFFECT_1 : SPEED_EFFECT_2;
        
        // Apply speed effect to player
        player.addPotionEffect(effectToApply);
        
        // Update last speed time
        lastSpeedTimes.put(playerId, currentTime);
        
        // Alternate effect for next time
        useEffect1.put(playerId, !useFirst);
        
        if (DEBUG_MODE) {
            if (useFirst) {
                player.sendMessage(SPEED_MESSAGES[1]); // "Speed effect applied!"
                player.sendMessage(SPEED_MESSAGES[2]); // "Speed boost active!"
            } else {
                player.sendMessage(SPEED_MESSAGES[3]); // "Speed power increased!"
                player.sendMessage(SPEED_MESSAGES[4]); // "Speed duration extended!"
            }
        }
    }

    @Override
    protected void onCooldownEnd(Player player, UUID playerUuid) {
        super.onCooldownEnd(player, playerUuid);
        
        // Clean up tracking data when cooldown ends
        lastSpeedTimes.remove(playerUuid);
        useEffect1.remove(playerUuid);
        
        if (DEBUG_MODE && player != null) {
            player.sendMessage(SPEED_MESSAGES[6]); // "Speed type changed!"
        }
    }

    public SpeedCooldown(long duration) {
        super("Speed Cooldown", TimeUnit.SECONDS.toMillis(duration));
    }
    
    /**
     * Get remaining cooldown time for a player
     * @param player the player
     * @return remaining cooldown in milliseconds, or 0 if no cooldown
     */
    public long getRemainingCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        Long lastSpeedTime = lastSpeedTimes.get(playerId);
        
        if (lastSpeedTime == null) {
            return 0;
        }
        
        long timeSinceLastSpeed = System.currentTimeMillis() - lastSpeedTime;
        long cooldownDuration = getDefaultDuration();
        
        if (timeSinceLastSpeed >= cooldownDuration) {
            return 0;
        }
        
        return cooldownDuration - timeSinceLastSpeed;
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
        UUID playerId = player.getUniqueId();
        lastSpeedTimes.remove(playerId);
        useEffect1.remove(playerId);
    }
    
    /**
     * Clear all cooldowns (useful for cleanup)
     */
    public void clearAllCooldowns() {
        lastSpeedTimes.clear();
        useEffect1.clear();
    }
}
