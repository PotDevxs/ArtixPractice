package dev.artixdev.practice.enums;

import org.bukkit.entity.Player;

public enum KillEffectType {
    
    NONE("None", "practice.kill.effect.none"),
    FIREWORK("Firework", "practice.kill.effect.firework"),
    LIGHTNING("Lightning", "practice.kill.effect.lightning"),
    EXPLOSION("Explosion", "practice.kill.effect.explosion"),
    HEARTS("Hearts", "practice.kill.effect.hearts"),
    VILLAGER_HAPPY("Villager Happy", "practice.kill.effect.villager.happy"),
    VILLAGER_ANGRY("Villager Angry", "practice.kill.effect.villager.angry"),
    WITCH("Witch", "practice.kill.effect.witch"),
    DRAGON_BREATH("Dragon Breath", "practice.kill.effect.dragon.breath"),
    ENDER_CRYSTAL("Ender Crystal", "practice.kill.effect.ender.crystal"),
    TOTEM("Totem", "practice.kill.effect.totem"),
    ELDER_GUARDIAN("Elder Guardian", "practice.kill.effect.elder.guardian"),
    WITHER("Wither", "practice.kill.effect.wither"),
    ENDER_DRAGON("Ender Dragon", "practice.kill.effect.ender.dragon");

    private final String name;
    private final String permission;

    KillEffectType(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}
