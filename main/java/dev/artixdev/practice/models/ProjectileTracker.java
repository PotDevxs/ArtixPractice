package dev.artixdev.practice.models;

import org.bukkit.entity.Player;
import dev.artixdev.practice.managers.ProjectileManager;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProjectileTracker {
    
    private int hits;
    private final Instant startTime;
    private boolean isActive;
    private final UUID playerId;
    private final Set<Integer> hitEntities;
    private double damage;
    private int maxHits;
    private final List<ProjectileEffect> effects;
    private final ProjectileManager manager;

    public ProjectileTracker(UUID playerId, ProjectileManager manager, Instant startTime, List<ProjectileEffect> effects) {
        this.playerId = playerId;
        this.manager = manager;
        this.startTime = startTime;
        this.effects = effects;
        this.hitEntities = new java.util.HashSet<>();
        this.isActive = true;
        this.maxHits = 10;
        this.damage = 1.0;
        this.hits = 0;
        this.initialize();
    }

    public ProjectileTracker(ProjectileManager manager, Player player) {
        this.playerId = player.getUniqueId();
        this.manager = manager;
        this.startTime = Instant.now();
        this.effects = new java.util.ArrayList<>();
        this.hitEntities = new java.util.HashSet<>();
        this.isActive = true;
        this.maxHits = 10;
        this.damage = 1.0;
        this.hits = 0;
        this.initialize();
    }

    private void initialize() {
        // Initialize projectile tracking
        this.isActive = true;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public ProjectileManager getManager() {
        return manager;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public List<ProjectileEffect> getEffects() {
        return effects;
    }

    public Set<Integer> getHitEntities() {
        return hitEntities;
    }

    public int getHits() {
        return hits;
    }

    public int getMaxHits() {
        return maxHits;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void addHit(int entityId) {
        if (hitEntities.add(entityId)) {
            hits++;
        }
    }

    public boolean canHit(int entityId) {
        return isActive && !hitEntities.contains(entityId) && hits < maxHits;
    }
}
