package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import dev.artixdev.practice.utils.events.BaseEvent;

import java.util.Objects;

public class PlayerEffectEvent extends BaseEvent {
    private final Player player;
    private final PotionEffectType effectType;
    private final int duration;
    private final int amplifier;

    public PlayerEffectEvent(Player player, PotionEffectType effectType, int duration, int amplifier) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(effectType);
        this.player = player;
        this.effectType = effectType;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public Player getPlayer() {
        return player;
    }

    public PotionEffectType getEffectType() {
        return effectType;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}
