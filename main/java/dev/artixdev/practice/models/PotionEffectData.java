package dev.artixdev.practice.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.potion.PotionEffect;

public class PotionEffectData {
    
    private final int duration;
    private final PotionEffect potionEffect;
    private final Map<UUID, Long> cooldowns;

    private PotionEffectData(PotionEffect potionEffect, int duration) {
        this.cooldowns = new HashMap<>();
        this.potionEffect = potionEffect;
        this.duration = duration;
    }

    public static PotionEffectData create(PotionEffect potionEffect) {
        return new PotionEffectData(potionEffect, -1);
    }

    public static PotionEffectData create(PotionEffect potionEffect, int duration) {
        return new PotionEffectData(potionEffect, duration);
    }

    public static PotionEffectData create(int duration) {
        // This method was obfuscated and its exact logic needs to be inferred or provided.
        // It seems to create a PotionEffectData with a specific duration.
        // For now, it's a placeholder.
        return null;
    }

    public int getDuration() {
        return this.duration;
    }

    public Map<UUID, Long> getCooldowns() {
        return this.cooldowns;
    }

    public PotionEffect getPotionEffect() {
        return this.potionEffect;
    }

    public void addCooldown(UUID playerId, long cooldownTime) {
        this.cooldowns.put(playerId, cooldownTime);
    }

    public void removeCooldown(UUID playerId) {
        this.cooldowns.remove(playerId);
    }

    public boolean hasCooldown(UUID playerId) {
        return this.cooldowns.containsKey(playerId);
    }

    public long getCooldownTime(UUID playerId) {
        return this.cooldowns.getOrDefault(playerId, 0L);
    }

    public boolean isCooldownExpired(UUID playerId) {
        if (!hasCooldown(playerId)) {
            return true;
        }
        
        long cooldownTime = getCooldownTime(playerId);
        return System.currentTimeMillis() >= cooldownTime;
    }

    public void clearAllCooldowns() {
        this.cooldowns.clear();
    }

    public int getActiveCooldowns() {
        return this.cooldowns.size();
    }
}
