package dev.artixdev.practice.cooldowns;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;

public class ComboCooldown extends BaseCooldown implements Listener {
    public static final int COOLDOWN_VERSION = 1;
    private static final String[] COMBO_CONSTANTS = {"ComboCooldown"};
    public static final boolean DEBUG_MODE = false;
    private static final PotionEffect SLOW_EFFECT = new PotionEffect(PotionEffectType.SLOW, 40, 2);
    private static final String[] COMBO_MESSAGES = new String[10];
    
    // Track combo data per player: UUID -> {lastHitTime, comboCount}
    private final Map<UUID, ComboData> playerCombos = new ConcurrentHashMap<>();
    
    // Combo data class
    private static class ComboData {
        private long lastHitTime;
        private int comboCount;
        
        public ComboData() {
            this.lastHitTime = System.currentTimeMillis();
            this.comboCount = 1;
        }
        
        public long getLastHitTime() {
            return lastHitTime;
        }
        
        public void setLastHitTime(long lastHitTime) {
            this.lastHitTime = lastHitTime;
        }
        
        public int getComboCount() {
            return comboCount;
        }
        
        public void incrementCombo() {
            this.comboCount++;
            this.lastHitTime = System.currentTimeMillis();
        }
        
        public void resetCombo() {
            this.comboCount = 1;
            this.lastHitTime = System.currentTimeMillis();
        }
    }

    static {
        COMBO_MESSAGES[0] = "Combo started!";
        COMBO_MESSAGES[1] = "Combo hit!";
        COMBO_MESSAGES[2] = "Combo combo!";
        COMBO_MESSAGES[3] = "Combo streak!";
        COMBO_MESSAGES[4] = "Combo break!";
        COMBO_MESSAGES[5] = "Combo end!";
        COMBO_MESSAGES[6] = "Combo reset!";
        COMBO_MESSAGES[7] = "Combo bonus!";
        COMBO_MESSAGES[8] = "Combo multiplier!";
        COMBO_MESSAGES[9] = "Combo finish!";
    }

    public ComboCooldown(long duration) {
        super("Combo Cooldown", duration);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Check if the damager is a player
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) {
            return;
        }
        
        Player attacker = (Player) damager;
        UUID attackerId = attacker.getUniqueId();
        
        // Check if the entity being damaged is a player
        Entity damaged = event.getEntity();
        if (!(damaged instanceof Player)) {
            return;
        }
        
        Player victim = (Player) damaged;
        
        // Get or create combo data for the attacker
        ComboData comboData = playerCombos.get(attackerId);
        long currentTime = System.currentTimeMillis();
        
        if (comboData == null) {
            // Start new combo
            comboData = new ComboData();
            playerCombos.put(attackerId, comboData);
            
            if (DEBUG_MODE) {
                attacker.sendMessage(COMBO_MESSAGES[0]); // "Combo started!"
            }
        } else {
            // Check if combo should continue or reset
            long timeSinceLastHit = currentTime - comboData.getLastHitTime();
            long comboTimeout = getDefaultDuration(); // Use cooldown duration as combo timeout
            
            if (timeSinceLastHit > comboTimeout) {
                // Combo expired, reset
                comboData.resetCombo();
                
                if (DEBUG_MODE) {
                    attacker.sendMessage(COMBO_MESSAGES[6]); // "Combo reset!"
                }
            } else {
                // Continue combo
                comboData.incrementCombo();
                int comboCount = comboData.getComboCount();
                
                // Apply slow effect to victim when combo reaches certain thresholds
                if (comboCount >= 3 && comboCount % 2 == 1) {
                    // Apply slow effect every 2 hits starting from 3
                    victim.addPotionEffect(SLOW_EFFECT);
                    
                    if (DEBUG_MODE) {
                        attacker.sendMessage(COMBO_MESSAGES[3] + " x" + comboCount); // "Combo streak!"
                        victim.sendMessage(COMBO_MESSAGES[4]); // "Combo break!" (from victim's perspective)
                    }
                }
                
                // Send combo messages at certain milestones
                if (DEBUG_MODE) {
                    if (comboCount == 2) {
                        attacker.sendMessage(COMBO_MESSAGES[1]); // "Combo hit!"
                    } else if (comboCount == 3) {
                        attacker.sendMessage(COMBO_MESSAGES[2]); // "Combo combo!"
                    } else if (comboCount >= 5) {
                        attacker.sendMessage(COMBO_MESSAGES[7] + " x" + comboCount); // "Combo bonus!"
                    }
                }
            }
        }
        
        // Update PlayerSnapshot if available
        try {
            Main plugin = Main.getInstance();
            if (plugin != null && plugin.getPlayerManager() != null) {
                PlayerProfile attackerProfile = plugin.getPlayerManager().getPlayerProfile(attackerId);
                if (attackerProfile != null && attackerProfile.getCurrentMatch() != null) {
                    // Combo display/effects are handled by match UI; no snapshot update needed here
                }
            }
        } catch (Exception e) {
            // Silently handle any errors accessing player profile
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Get combo count for a player
     * @param player the player
     * @return combo count or 0
     */
    public int getComboCount(Player player) {
        ComboData comboData = playerCombos.get(player.getUniqueId());
        if (comboData == null) {
            return 0;
        }
        
        // Check if combo expired
        long timeSinceLastHit = System.currentTimeMillis() - comboData.getLastHitTime();
        if (timeSinceLastHit > getDefaultDuration()) {
            playerCombos.remove(player.getUniqueId());
            return 0;
        }
        
        return comboData.getComboCount();
    }
    
    /**
     * Reset combo for a player
     * @param player the player
     */
    public void resetCombo(Player player) {
        playerCombos.remove(player.getUniqueId());
    }
    
    /**
     * Clear all combos (useful for cleanup)
     */
    public void clearAllCombos() {
        playerCombos.clear();
    }
}
