package dev.artixdev.practice.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ProjectileEffect {
    
    private final String name;
    private final EffectType type;
    private final double radius;
    private final int duration;
    private final int amplifier;

    public ProjectileEffect(String name, EffectType type, double radius, int duration, int amplifier) {
        this.name = name;
        this.type = type;
        this.radius = radius;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public String getName() {
        return name;
    }

    public EffectType getType() {
        return type;
    }

    public double getRadius() {
        return radius;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public void apply(Player player, Location location) {
        switch (type) {
            case DAMAGE:
                // Apply damage effect
                break;
            case HEAL:
                // Apply heal effect
                break;
            case POISON:
                // Apply poison effect
                break;
            case SLOW:
                // Apply slow effect
                break;
            case SPEED:
                // Apply speed effect
                break;
            case FIRE:
                // Apply fire effect
                break;
            case EXPLOSION:
                // Apply explosion effect
                break;
        }
    }

    public enum EffectType {
        DAMAGE, HEAL, POISON, SLOW, SPEED, FIRE, EXPLOSION
    }
}
