package dev.artixdev.practice.models;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import java.util.UUID;

/**
 * Bot Profile
 * Represents a bot player profile
 */
public class BotProfile {
    
    private final UUID uuid;
    private final String name;
    private final String kit;
    private final String mode;
    private Location location;
    private boolean moving;
    private boolean fighting;
    private double movementSpeed;
    private boolean tryhard;
    private boolean fastPotions;
    private boolean alternateHitFactor;
    private boolean reduceRangeOnCombo;
    private boolean spigotBasedKB;
    private Vector velocity;
    private UUID targetPlayerId;
    private long lastAttackTimeMs;
    
    public BotProfile(String name, String kit, String mode) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.kit = kit;
        this.mode = mode;
        this.location = null;
        this.moving = false;
        this.fighting = false;
        this.movementSpeed = 1.0;
        this.tryhard = false;
        this.fastPotions = false;
        this.alternateHitFactor = false;
        this.reduceRangeOnCombo = true;
        this.spigotBasedKB = false;
        this.velocity = new Vector(0, 0, 0);
        this.targetPlayerId = null;
        this.lastAttackTimeMs = 0L;
    }
    
    // Getters and Setters
    public UUID getUuid() {
        return uuid;
    }
    
    public String getName() {
        return name;
    }
    
    public String getKit() {
        return kit;
    }
    
    public String getMode() {
        return mode;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public boolean isMoving() {
        return moving;
    }
    
    public void setMoving(boolean moving) {
        this.moving = moving;
    }
    
    public boolean isFighting() {
        return fighting;
    }
    
    public void setFighting(boolean fighting) {
        this.fighting = fighting;
    }
    
    public double getMovementSpeed() {
        return movementSpeed;
    }
    
    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }
    
    public boolean isTryhard() {
        return tryhard;
    }
    
    public void setTryhard(boolean tryhard) {
        this.tryhard = tryhard;
    }
    
    public boolean isFastPotions() {
        return fastPotions;
    }
    
    public void setFastPotions(boolean fastPotions) {
        this.fastPotions = fastPotions;
    }
    
    public boolean isAlternateHitFactor() {
        return alternateHitFactor;
    }
    
    public void setAlternateHitFactor(boolean alternateHitFactor) {
        this.alternateHitFactor = alternateHitFactor;
    }
    
    public boolean isReduceRangeOnCombo() {
        return reduceRangeOnCombo;
    }
    
    public void setReduceRangeOnCombo(boolean reduceRangeOnCombo) {
        this.reduceRangeOnCombo = reduceRangeOnCombo;
    }
    
    public boolean isSpigotBasedKB() {
        return spigotBasedKB;
    }
    
    public void setSpigotBasedKB(boolean spigotBasedKB) {
        this.spigotBasedKB = spigotBasedKB;
    }
    
    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity == null ? new Vector(0, 0, 0) : velocity;
    }

    public UUID getTargetPlayerId() {
        return targetPlayerId;
    }

    public void setTargetPlayerId(UUID targetPlayerId) {
        this.targetPlayerId = targetPlayerId;
    }

    public long getLastAttackTimeMs() {
        return lastAttackTimeMs;
    }

    public void setLastAttackTimeMs(long lastAttackTimeMs) {
        this.lastAttackTimeMs = lastAttackTimeMs;
    }
}
