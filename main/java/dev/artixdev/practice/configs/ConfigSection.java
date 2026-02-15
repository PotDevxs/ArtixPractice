package dev.artixdev.practice.configs;

import java.util.ArrayList;
import java.util.List;

public class ConfigSection {
    
    private final ConfigOption<Double> damageMultiplier;
    private final ConfigOption<Boolean> enablePvP;
    private final ConfigOption<Double> knockbackMultiplier;
    private final ConfigOption<Boolean> enableKnockback;
    private final ConfigOption<Double> speedMultiplier;
    private final ConfigOption<Boolean> enableSpeed;
    private final ConfigOption<Boolean> enableRegen;
    private final ConfigOption<Double> regenMultiplier;
    private final ConfigOption<Boolean> enableHunger;
    private final ConfigOption<Boolean> enableFallDamage;
    private final ConfigOption<Double> fallDamageMultiplier;
    private final ConfigOption<Double> jumpMultiplier;
    private final ConfigOption<Double> gravityMultiplier;
    private final ConfigOption<Double> frictionMultiplier;
    private final ConfigOption<Double> airResistanceMultiplier;
    private final ConfigOption<Double> waterResistanceMultiplier;
    private final ConfigOption<Double> lavaResistanceMultiplier;
    private final ConfigOption<Double> fireResistanceMultiplier;
    private final ConfigOption<Double> explosionResistanceMultiplier;
    private final ConfigOption<Double> magicResistanceMultiplier;
    private final List<ConfigOption<?>> allOptions;

    public ConfigSection() {
        this.damageMultiplier = new ConfigOption<>("damage-multiplier", "Damage multiplier for PvP", "pvp.damage.multiplier", 1, "1.0", Double.class);
        this.enablePvP = new ConfigOption<>("enable-pvp", "Enable PvP", "pvp.enabled", 1, "true", Boolean.class);
        this.knockbackMultiplier = new ConfigOption<>("knockback-multiplier", "Knockback multiplier", "pvp.knockback.multiplier", 1, "1.0", Double.class);
        this.enableKnockback = new ConfigOption<>("enable-knockback", "Enable knockback", "pvp.knockback.enabled", 1, "true", Boolean.class);
        this.speedMultiplier = new ConfigOption<>("speed-multiplier", "Speed multiplier", "movement.speed.multiplier", 1, "1.0", Double.class);
        this.enableSpeed = new ConfigOption<>("enable-speed", "Enable speed modifications", "movement.speed.enabled", 1, "true", Boolean.class);
        this.enableRegen = new ConfigOption<>("enable-regen", "Enable health regeneration", "health.regen.enabled", 1, "true", Boolean.class);
        this.regenMultiplier = new ConfigOption<>("regen-multiplier", "Health regeneration multiplier", "health.regen.multiplier", 1, "1.0", Double.class);
        this.enableHunger = new ConfigOption<>("enable-hunger", "Enable hunger", "hunger.enabled", 1, "true", Boolean.class);
        this.enableFallDamage = new ConfigOption<>("enable-fall-damage", "Enable fall damage", "damage.fall.enabled", 1, "true", Boolean.class);
        this.fallDamageMultiplier = new ConfigOption<>("fall-damage-multiplier", "Fall damage multiplier", "damage.fall.multiplier", 1, "1.0", Double.class);
        this.jumpMultiplier = new ConfigOption<>("jump-multiplier", "Jump height multiplier", "movement.jump.multiplier", 1, "1.0", Double.class);
        this.gravityMultiplier = new ConfigOption<>("gravity-multiplier", "Gravity multiplier", "physics.gravity.multiplier", 1, "1.0", Double.class);
        this.frictionMultiplier = new ConfigOption<>("friction-multiplier", "Friction multiplier", "physics.friction.multiplier", 1, "1.0", Double.class);
        this.airResistanceMultiplier = new ConfigOption<>("air-resistance-multiplier", "Air resistance multiplier", "physics.air-resistance.multiplier", 1, "1.0", Double.class);
        this.waterResistanceMultiplier = new ConfigOption<>("water-resistance-multiplier", "Water resistance multiplier", "physics.water-resistance.multiplier", 1, "1.0", Double.class);
        this.lavaResistanceMultiplier = new ConfigOption<>("lava-resistance-multiplier", "Lava resistance multiplier", "physics.lava-resistance.multiplier", 1, "1.0", Double.class);
        this.fireResistanceMultiplier = new ConfigOption<>("fire-resistance-multiplier", "Fire resistance multiplier", "physics.fire-resistance.multiplier", 1, "1.0", Double.class);
        this.explosionResistanceMultiplier = new ConfigOption<>("explosion-resistance-multiplier", "Explosion resistance multiplier", "physics.explosion-resistance.multiplier", 1, "1.0", Double.class);
        this.magicResistanceMultiplier = new ConfigOption<>("magic-resistance-multiplier", "Magic resistance multiplier", "physics.magic-resistance.multiplier", 1, "1.0", Double.class);
        
        this.allOptions = new ArrayList<>();
        allOptions.add(damageMultiplier);
        allOptions.add(enablePvP);
        allOptions.add(knockbackMultiplier);
        allOptions.add(enableKnockback);
        allOptions.add(speedMultiplier);
        allOptions.add(enableSpeed);
        allOptions.add(enableRegen);
        allOptions.add(regenMultiplier);
        allOptions.add(enableHunger);
        allOptions.add(enableFallDamage);
        allOptions.add(fallDamageMultiplier);
        allOptions.add(jumpMultiplier);
        allOptions.add(gravityMultiplier);
        allOptions.add(frictionMultiplier);
        allOptions.add(airResistanceMultiplier);
        allOptions.add(waterResistanceMultiplier);
        allOptions.add(lavaResistanceMultiplier);
        allOptions.add(fireResistanceMultiplier);
        allOptions.add(explosionResistanceMultiplier);
        allOptions.add(magicResistanceMultiplier);
    }

    public ConfigOption<Double> getDamageMultiplier() {
        return damageMultiplier;
    }

    public ConfigOption<Boolean> getEnablePvP() {
        return enablePvP;
    }

    public ConfigOption<Double> getKnockbackMultiplier() {
        return knockbackMultiplier;
    }

    public ConfigOption<Boolean> getEnableKnockback() {
        return enableKnockback;
    }

    public ConfigOption<Double> getSpeedMultiplier() {
        return speedMultiplier;
    }

    public ConfigOption<Boolean> getEnableSpeed() {
        return enableSpeed;
    }

    public ConfigOption<Boolean> getEnableRegen() {
        return enableRegen;
    }

    public ConfigOption<Double> getRegenMultiplier() {
        return regenMultiplier;
    }

    public ConfigOption<Boolean> getEnableHunger() {
        return enableHunger;
    }

    public ConfigOption<Boolean> getEnableFallDamage() {
        return enableFallDamage;
    }

    public ConfigOption<Double> getFallDamageMultiplier() {
        return fallDamageMultiplier;
    }

    public ConfigOption<Double> getJumpMultiplier() {
        return jumpMultiplier;
    }

    public ConfigOption<Double> getGravityMultiplier() {
        return gravityMultiplier;
    }

    public ConfigOption<Double> getFrictionMultiplier() {
        return frictionMultiplier;
    }

    public ConfigOption<Double> getAirResistanceMultiplier() {
        return airResistanceMultiplier;
    }

    public ConfigOption<Double> getWaterResistanceMultiplier() {
        return waterResistanceMultiplier;
    }

    public ConfigOption<Double> getLavaResistanceMultiplier() {
        return lavaResistanceMultiplier;
    }

    public ConfigOption<Double> getFireResistanceMultiplier() {
        return fireResistanceMultiplier;
    }

    public ConfigOption<Double> getExplosionResistanceMultiplier() {
        return explosionResistanceMultiplier;
    }

    public ConfigOption<Double> getMagicResistanceMultiplier() {
        return magicResistanceMultiplier;
    }

    public List<ConfigOption<?>> getAllOptions() {
        return allOptions;
    }
}
