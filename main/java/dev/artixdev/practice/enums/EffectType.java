package dev.artixdev.practice.enums;

public enum EffectType {
    
    KILL_EFFECT("Kill Effect"),
    WIN_EFFECT("Win Effect"),
    DEATH_EFFECT("Death Effect"),
    HIT_EFFECT("Hit Effect"),
    BLOCK_BREAK_EFFECT("Block Break Effect"),
    PROJECTILE_EFFECT("Projectile Effect"),
    SOUND_EFFECT("Sound Effect"),
    PARTICLE_EFFECT("Particle Effect"),
    LIGHTNING_EFFECT("Lightning Effect");

    private final String displayName;

    EffectType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
