package dev.artixdev.practice.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.events.PlayerEffectEvent;

/**
 * Effect Manager
 * Manages potion effects and special effects for players
 */
public class EffectManager {
   
   private final Map<UUID, Long> effectCooldowns;
   private final Map<UUID, Set<Pair<UUID, Long>>> activeEffects;
   private final Main plugin;
   private final Map<Class<?>, EffectHandler> effectHandlers;
   private final Map<UUID, EffectHandler> playerEffectHandlers;
   private final Map<UUID, Float> effectIntensities;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public EffectManager(Main plugin) {
      this.plugin = plugin;
      this.effectCooldowns = new ConcurrentHashMap<>();
      this.activeEffects = new ConcurrentHashMap<>();
      this.effectHandlers = new HashMap<>();
      this.playerEffectHandlers = new ConcurrentHashMap<>();
      this.effectIntensities = new ConcurrentHashMap<>();
   }
   
   /**
    * Apply effect to player
    * @param player the player
    * @param effectType the effect type
    * @param duration the duration in ticks
    * @param amplifier the amplifier
    * @param showParticles whether to show particles
    * @param showIcon whether to show icon
    */
   public void applyEffect(Player player, PotionEffectType effectType, int duration, int amplifier, boolean showParticles, boolean showIcon) {
      try {
         // Check cooldown
         if (hasCooldown(player, effectType)) {
            return;
         }
         
         // Create effect
         PotionEffect effect = new PotionEffect(effectType, duration, amplifier, false, showParticles);
         
         // Apply effect
         player.addPotionEffect(effect);
         
         // Track effect
         trackEffect(player, effectType, duration);
         
         // Fire event
         fireEffectEvent(player, effectType, duration, amplifier);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to apply effect to " + player.getName() + ": " + e.getMessage());
      }
   }
   
   /**
    * Apply effect to player with default settings
    * @param player the player
    * @param effectType the effect type
    * @param duration the duration in ticks
    * @param amplifier the amplifier
    */
   public void applyEffect(Player player, PotionEffectType effectType, int duration, int amplifier) {
      applyEffect(player, effectType, duration, amplifier, true, true);
   }
   
   /**
    * Remove effect from player
    * @param player the player
    * @param effectType the effect type
    */
   public void removeEffect(Player player, PotionEffectType effectType) {
      try {
         // Remove effect
         player.removePotionEffect(effectType);
         
         // Untrack effect
         untrackEffect(player, effectType);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to remove effect from " + player.getName() + ": " + e.getMessage());
      }
   }
   
   /**
    * Remove all effects from player
    * @param player the player
    */
   public void removeAllEffects(Player player) {
      try {
         // Remove all effects
         for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
         }
         
         // Clear tracking
         clearPlayerEffects(player);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to remove all effects from " + player.getName() + ": " + e.getMessage());
      }
   }
   
   /**
    * Check if player has effect
    * @param player the player
    * @param effectType the effect type
    * @return true if has effect
    */
   public boolean hasEffect(Player player, PotionEffectType effectType) {
      return player.hasPotionEffect(effectType);
   }
   
   /**
    * Get effect level
    * @param player the player
    * @param effectType the effect type
    * @return effect level
    */
   public int getEffectLevel(Player player, PotionEffectType effectType) {
      // getPotionEffect() doesn't exist in 1.8.8, use getActivePotionEffects() instead
      for (PotionEffect effect : player.getActivePotionEffects()) {
         if (effect.getType().equals(effectType)) {
            return effect.getAmplifier();
         }
      }
      return 0;
   }
   
   /**
    * Get effect duration
    * @param player the player
    * @param effectType the effect type
    * @return effect duration
    */
   public int getEffectDuration(Player player, PotionEffectType effectType) {
      // getPotionEffect() doesn't exist in 1.8.8, use getActivePotionEffects() instead
      for (PotionEffect effect : player.getActivePotionEffects()) {
         if (effect.getType().equals(effectType)) {
            return effect.getDuration();
         }
      }
      return 0;
   }
   
   /**
    * Check if player has cooldown
    * @param player the player
    * @param effectType the effect type
    * @return true if has cooldown
    */
   public boolean hasCooldown(Player player, PotionEffectType effectType) {
      UUID playerId = player.getUniqueId();
      Long cooldown = effectCooldowns.get(playerId);
      return cooldown != null && cooldown > System.currentTimeMillis();
   }
   
   /**
    * Set cooldown for player
    * @param player the player
    * @param effectType the effect type
    * @param cooldownTicks the cooldown in ticks
    */
   public void setCooldown(Player player, PotionEffectType effectType, int cooldownTicks) {
      UUID playerId = player.getUniqueId();
      long cooldownTime = System.currentTimeMillis() + (cooldownTicks * 50L);
      effectCooldowns.put(playerId, cooldownTime);
   }
   
   /**
    * Get cooldown remaining
    * @param player the player
    * @param effectType the effect type
    * @return cooldown remaining in ticks
    */
   public int getCooldownRemaining(Player player, PotionEffectType effectType) {
      UUID playerId = player.getUniqueId();
      Long cooldown = effectCooldowns.get(playerId);
      if (cooldown == null) {
         return 0;
      }
      
      long remaining = cooldown - System.currentTimeMillis();
      return remaining > 0 ? (int) (remaining / 50L) : 0;
   }
   
   /**
    * Track effect
    * @param player the player
    * @param effectType the effect type
    * @param duration the duration
    */
   private void trackEffect(Player player, PotionEffectType effectType, int duration) {
      UUID playerId = player.getUniqueId();
      Set<Pair<UUID, Long>> effects = activeEffects.computeIfAbsent(playerId, k -> new HashSet<>());
      
      // Create effect ID (using effect type hash)
      UUID effectId = UUID.nameUUIDFromBytes(effectType.getName().getBytes());
      long endTime = System.currentTimeMillis() + (duration * 50L);
      
      effects.add(Pair.of(effectId, endTime));
   }
   
   /**
    * Untrack effect
    * @param player the player
    * @param effectType the effect type
    */
   private void untrackEffect(Player player, PotionEffectType effectType) {
      UUID playerId = player.getUniqueId();
      Set<Pair<UUID, Long>> effects = activeEffects.get(playerId);
      if (effects != null) {
         UUID effectId = UUID.nameUUIDFromBytes(effectType.getName().getBytes());
         effects.removeIf(pair -> pair.getLeft().equals(effectId));
      }
   }
   
   /**
    * Clear player effects
    * @param player the player
    */
   private void clearPlayerEffects(Player player) {
      UUID playerId = player.getUniqueId();
      activeEffects.remove(playerId);
      effectCooldowns.remove(playerId);
      playerEffectHandlers.remove(playerId);
      effectIntensities.remove(playerId);
   }
   
   /**
    * Fire effect event
    * @param player the player
    * @param effectType the effect type
    * @param duration the duration
    * @param amplifier the amplifier
    */
   private void fireEffectEvent(Player player, PotionEffectType effectType, int duration, int amplifier) {
      try {
         PlayerEffectEvent event = new PlayerEffectEvent(player, effectType, duration, amplifier);
         Bukkit.getPluginManager().callEvent(event);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to fire effect event: " + e.getMessage());
      }
   }
   
   /**
    * Get active effects count
    * @param player the player
    * @return active effects count
    */
   public int getActiveEffectsCount(Player player) {
      return player.getActivePotionEffects().size();
   }
   
   /**
    * Get tracked effects count
    * @param player the player
    * @return tracked effects count
    */
   public int getTrackedEffectsCount(Player player) {
      UUID playerId = player.getUniqueId();
      Set<Pair<UUID, Long>> effects = activeEffects.get(playerId);
      return effects != null ? effects.size() : 0;
   }
   
   /**
    * Clean up expired effects
    */
   public void cleanupExpiredEffects() {
      long currentTime = System.currentTimeMillis();
      
      for (Map.Entry<UUID, Set<Pair<UUID, Long>>> entry : activeEffects.entrySet()) {
         Set<Pair<UUID, Long>> effects = entry.getValue();
         effects.removeIf(pair -> pair.getRight() <= currentTime);
         
         if (effects.isEmpty()) {
            activeEffects.remove(entry.getKey());
         }
      }
   }
   
   /**
    * Get effect manager statistics
    * @return statistics
    */
   public String getStatistics() {
      return String.format("EffectManager: %d players with effects, %d total effects tracked", 
         activeEffects.size(), 
         activeEffects.values().stream().mapToInt(Set::size).sum());
   }
   
   /**
    * Effect Handler interface
    */
   public interface EffectHandler {
      void handleEffect(Player player, PotionEffectType effectType, int duration, int amplifier);
   }
}
