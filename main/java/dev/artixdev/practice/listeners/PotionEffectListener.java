package dev.artixdev.practice.listeners;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.UUID;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.artixdev.api.practice.spigot.event.impl.RefineRefinePotionExpireEvent;

public class PotionEffectListener implements Listener {
    
    private final Table<UUID, PotionEffectType, PotionEffect> activeEffects;

    public PotionEffectListener() {
        this.activeEffects = HashBasedTable.create();
    }

    public void addEffect(Player player, PotionEffect effect) {
        this.activeEffects.put(player.getUniqueId(), effect.getType(), effect);
    }

    public void removeEffect(Player player, PotionEffect effect) {
        this.activeEffects.remove(player.getUniqueId(), effect.getType());
    }

    public Table<UUID, PotionEffectType, PotionEffect> getActiveEffects() {
        return this.activeEffects;
    }

    @EventHandler(
        ignoreCancelled = true,
        priority = EventPriority.MONITOR
    )
    public void onPotionExpire(RefineRefinePotionExpireEvent event) {
        LivingEntity entity = event.getEntity();
        
        if (entity instanceof Player) {
            Player player = (Player) entity;
            
            // Check if player has custom metadata for this effect
            String metadataKey = "practice_custom_effect";
            if (!player.hasMetadata(metadataKey)) {
                return;
            }
            
            PotionEffectType effectType = event.getEffect().getType();
            PotionEffect storedEffect = this.activeEffects.remove(player.getUniqueId(), effectType);
            
            if (storedEffect != null) {
                int duration = storedEffect.getDuration();
                
                // If the stored effect has a reasonable duration (not infinite), reapply it
                if (duration < 1000000) {
                    event.setCancelled(true);
                    player.addPotionEffect(storedEffect, true);
                }
            }
        }
    }

    public void applyEffect(Player player, PotionEffect effect) {
        this.addEffect(player, effect);
    }

    public void clearPlayerEffects(Player player) {
        this.activeEffects.row(player.getUniqueId()).clear();
    }

    public boolean hasEffect(Player player, PotionEffectType effectType) {
        return this.activeEffects.contains(player.getUniqueId(), effectType);
    }

    public PotionEffect getEffect(Player player, PotionEffectType effectType) {
        return this.activeEffects.get(player.getUniqueId(), effectType);
    }
}
