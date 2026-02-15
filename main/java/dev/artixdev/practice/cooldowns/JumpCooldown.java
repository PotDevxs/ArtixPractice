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

public class JumpCooldown extends BaseCooldown implements Listener {
    public static final int COOLDOWN_VERSION = 1;
    public static final boolean DEBUG_MODE = false;
    private static final PotionEffect JUMP_EFFECT_1 = new PotionEffect(PotionEffectType.JUMP, 160, 7);
    private static final PotionEffect JUMP_EFFECT_2 = new PotionEffect(PotionEffectType.JUMP, 200, 6);
    private static final String[] JUMP_CONSTANTS = new String[7];
    private static final String[] JUMP_MESSAGES = new String[7];
    
    // Track last jump activation time per player: UUID -> lastJumpTime
    private final Map<UUID, Long> lastJumpTimes = new ConcurrentHashMap<>();
    
    // Track which effect to use next (alternate between effects)
    private final Map<UUID, Boolean> useEffect1 = new ConcurrentHashMap<>();

    public JumpCooldown(long duration) {
        super("Jump Cooldown", TimeUnit.SECONDS.toMillis(duration));
    }

    @Override
    protected void onCooldownEnd(Player player, UUID playerUuid) {
        super.onCooldownEnd(player, playerUuid);
        
        // Clean up tracking data when cooldown ends
        lastJumpTimes.remove(playerUuid);
        useEffect1.remove(playerUuid);
        
        if (DEBUG_MODE && player != null) {
            player.sendMessage(JUMP_MESSAGES[6]); // "Jump type changed!"
        }
    }
    
    /**
     * Get remaining cooldown time for a player
     * @param player the player
     * @return remaining cooldown in milliseconds, or 0 if no cooldown
     */
    public long getRemainingCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        Long lastJumpTime = lastJumpTimes.get(playerId);
        
        if (lastJumpTime == null) {
            return 0;
        }
        
        long timeSinceLastJump = System.currentTimeMillis() - lastJumpTime;
        long cooldownDuration = getDefaultDuration();
        
        if (timeSinceLastJump >= cooldownDuration) {
            return 0;
        }
        
        return cooldownDuration - timeSinceLastJump;
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
        lastJumpTimes.remove(playerId);
        useEffect1.remove(playerId);
    }
    
    /**
     * Clear all cooldowns (useful for cleanup)
     */
    public void clearAllCooldowns() {
        lastJumpTimes.clear();
        useEffect1.clear();
    }

    static {
        JUMP_CONSTANTS[0] = "Jump Cooldown";
        JUMP_CONSTANTS[1] = "Jump Effect";
        JUMP_CONSTANTS[2] = "Jump Boost";
        JUMP_CONSTANTS[3] = "Jump Power";
        JUMP_CONSTANTS[4] = "Jump Duration";
        JUMP_CONSTANTS[5] = "Jump Amplifier";
        JUMP_CONSTANTS[6] = "Jump Type";
        
        JUMP_MESSAGES[0] = "Jump cooldown started!";
        JUMP_MESSAGES[1] = "Jump effect applied!";
        JUMP_MESSAGES[2] = "Jump boost active!";
        JUMP_MESSAGES[3] = "Jump power increased!";
        JUMP_MESSAGES[4] = "Jump duration extended!";
        JUMP_MESSAGES[5] = "Jump amplifier boosted!";
        JUMP_MESSAGES[6] = "Jump type changed!";
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // Check if player has used jump recently
        Long lastJumpTime = lastJumpTimes.get(playerId);
        
        if (lastJumpTime != null) {
            long timeSinceLastJump = currentTime - lastJumpTime;
            long cooldownDuration = getDefaultDuration();
            
            if (timeSinceLastJump < cooldownDuration) {
                // Player is still in cooldown, don't apply effect
                if (DEBUG_MODE) {
                    long remainingTime = cooldownDuration - timeSinceLastJump;
                    player.sendMessage(JUMP_MESSAGES[0] + " - Wait " + (remainingTime / 1000.0) + "s");
                }
                return;
            }
        }
        
        // Determine which effect to use (alternate between effects)
        Boolean useFirstObj = useEffect1.get(playerId);
        boolean useFirst = (useFirstObj == null) ? true : useFirstObj;
        PotionEffect effectToApply = useFirst ? JUMP_EFFECT_1 : JUMP_EFFECT_2;
        
        // Apply jump effect to player
        player.addPotionEffect(effectToApply);
        
        // Update last jump time
        lastJumpTimes.put(playerId, currentTime);
        
        // Alternate effect for next time
        useEffect1.put(playerId, !useFirst);
        
        if (DEBUG_MODE) {
            if (useFirst) {
                player.sendMessage(JUMP_MESSAGES[1]); // "Jump effect applied!"
                player.sendMessage(JUMP_MESSAGES[2]); // "Jump boost active!"
            } else {
                player.sendMessage(JUMP_MESSAGES[3]); // "Jump power increased!"
                player.sendMessage(JUMP_MESSAGES[4]); // "Jump duration extended!"
            }
        }
    }
}
