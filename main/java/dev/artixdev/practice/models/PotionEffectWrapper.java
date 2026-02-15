package dev.artixdev.practice.models;

import org.bukkit.potion.PotionEffect;

public class PotionEffectWrapper {
    
    private final boolean isCustom;
    private final PotionEffect potionEffect;
    private final long timestamp;

    public PotionEffectWrapper(PotionEffect potionEffect, long timestamp, boolean isCustom) {
        this.potionEffect = potionEffect;
        this.timestamp = timestamp;
        this.isCustom = isCustom;
    }

    public boolean isCustom() {
        return this.isCustom;
    }

    public PotionEffect getPotionEffect() {
        return this.potionEffect;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public int hashCode() {
        int result = this.potionEffect.hashCode();
        result = 31 * result + (this.isCustom ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PotionEffectWrapper) {
            if (obj == this) {
                return true;
            } else {
                PotionEffectWrapper other = (PotionEffectWrapper) obj;
                if (other.getPotionEffect().equals(this.potionEffect)) {
                    return other.hashCode() == this.hashCode();
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public long getDuration() {
        return this.potionEffect.getDuration();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - this.timestamp > this.potionEffect.getDuration() * 50L; // Convert ticks to milliseconds
    }

    public long getTimeRemaining() {
        long elapsed = System.currentTimeMillis() - this.timestamp;
        long totalDuration = this.potionEffect.getDuration() * 50L; // Convert ticks to milliseconds
        return Math.max(0, totalDuration - elapsed);
    }
}
