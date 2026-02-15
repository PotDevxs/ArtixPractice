package dev.artixdev.practice.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Main;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * Potion Effect Manager
 * Manages potion effects for players
 */
public class PotionEffectManager {
   
   private static final int DEFAULT_DURATION = 10; // 10 seconds
   private static final int INFINITE_DURATION = Integer.MAX_VALUE;
   
   // Materials that provide potion effects
   private static final Set<Material> EFFECT_MATERIALS = new HashSet<>();
   
   static {
      // Initialize materials with cross-version compatibility
      addIfNotNull(XMaterial.SUGAR);
      addIfNotNull(XMaterial.SOUL_SAND);
      addIfNotNull(XMaterial.POTION); // HASTE_POTION, MINING_FATIGUE_POTION, STRENGTH_POTION, INSTANT_HEAL, INSTANT_DAMAGE are all POTION with different metadata
      addIfNotNull(XMaterial.RABBIT_FOOT);
      addIfNotNull(XMaterial.CHORUS_FRUIT); // Exists in 1.9+, will be null on 1.8.8
      addIfNotNull(XMaterial.GHAST_TEAR);
      addIfNotNull(XMaterial.TURTLE_HELMET); // Exists in 1.13+, will be null on 1.8.8
      addIfNotNull(XMaterial.MAGMA_CREAM);
      addIfNotNull(XMaterial.PUFFERFISH);
      addIfNotNull(XMaterial.GOLDEN_CARROT);
      addIfNotNull(XMaterial.INK_SAC);
      addIfNotNull(XMaterial.ROTTEN_FLESH);
      addIfNotNull(XMaterial.MILK_BUCKET);
      addIfNotNull(XMaterial.SPIDER_EYE);
      addIfNotNull(XMaterial.WITHER_SKELETON_SKULL);
      addIfNotNull(XMaterial.GOLDEN_APPLE);
      addIfNotNull(XMaterial.COOKED_BEEF);
   }
   
   private static void addIfNotNull(XMaterial xMaterial) {
      Material material = xMaterial.parseMaterial();
      if (material != null) {
         EFFECT_MATERIALS.add(material);
      }
   }
   
   /**
    * Apply potion effect to player
    * @param player the player
    * @param effectType the effect type
    * @param duration the duration in ticks
    * @param amplifier the amplifier level
    * @param ambient whether the effect is ambient
    * @param particles whether to show particles
    */
   public static void applyEffect(Player player, PotionEffectType effectType, int duration, int amplifier, boolean ambient, boolean particles) {
      if (player == null || effectType == null) {
         return;
      }
      
      PotionEffect effect = new PotionEffect(effectType, duration, amplifier, ambient, particles);
      player.addPotionEffect(effect, true);
   }
   
   /**
    * Apply potion effect with default settings
    * @param player the player
    * @param effectType the effect type
    * @param amplifier the amplifier level
    */
   public static void applyEffect(Player player, PotionEffectType effectType, int amplifier) {
      applyEffect(player, effectType, DEFAULT_DURATION * 20, amplifier, false, true);
   }
   
   /**
    * Apply infinite potion effect
    * @param player the player
    * @param effectType the effect type
    * @param amplifier the amplifier level
    */
   public static void applyInfiniteEffect(Player player, PotionEffectType effectType, int amplifier) {
      applyEffect(player, effectType, INFINITE_DURATION, amplifier, false, true);
   }
   
   /**
    * Remove potion effect from player
    * @param player the player
    * @param effectType the effect type
    */
   public static void removeEffect(Player player, PotionEffectType effectType) {
      if (player == null || effectType == null) {
         return;
      }
      
      player.removePotionEffect(effectType);
   }
   
   /**
    * Clear all potion effects from player
    * @param player the player
    */
   public static void clearAllEffects(Player player) {
      if (player == null) {
         return;
      }
      
      for (PotionEffect effect : player.getActivePotionEffects()) {
         player.removePotionEffect(effect.getType());
      }
   }
   
   /**
    * Clear specific effects from player
    * @param player the player
    * @param effectTypes the effect types to remove
    */
   public static void clearEffects(Player player, PotionEffectType... effectTypes) {
      if (player == null || effectTypes == null) {
         return;
      }
      
      for (PotionEffectType effectType : effectTypes) {
         removeEffect(player, effectType);
      }
   }
   
   /**
    * Check if player has effect
    * @param player the player
    * @param effectType the effect type
    * @return true if has effect
    */
   public static boolean hasEffect(Player player, PotionEffectType effectType) {
      if (player == null || effectType == null) {
         return false;
      }
      
      return player.hasPotionEffect(effectType);
   }
   
   /**
    * Get effect amplifier
    * @param player the player
    * @param effectType the effect type
    * @return amplifier level or -1 if not found
    */
   public static int getEffectAmplifier(Player player, PotionEffectType effectType) {
      if (player == null || effectType == null) {
         return -1;
      }
      
      // getPotionEffect() doesn't exist in 1.8.8, use getActivePotionEffects() instead
      for (PotionEffect effect : player.getActivePotionEffects()) {
         if (effect.getType().equals(effectType)) {
            return effect.getAmplifier();
         }
      }
      return -1;
   }
   
   /**
    * Apply effects based on material
    * @param player the player
    * @param material the material
    * @param amplifier the amplifier level
    */
   public static void applyMaterialEffect(Player player, Material material, int amplifier) {
      if (player == null || material == null || !EFFECT_MATERIALS.contains(material)) {
         return;
      }
      
      PotionEffectType effectType = getEffectTypeFromMaterial(material);
      if (effectType != null) {
         applyEffect(player, effectType, amplifier);
      }
   }
   
   /**
    * Get effect type from material
    * @param material the material
    * @return effect type or null
    */
   private static PotionEffectType getEffectTypeFromMaterial(Material material) {
      if (material == null) {
         return null;
      }
      
      // Use XMaterial for cross-version compatibility
      XMaterial xMaterial;
      try {
         xMaterial = XMaterial.matchXMaterial(material);
      } catch (IllegalArgumentException e) {
         return null;
      }
      
      if (xMaterial == null) {
         return null;
      }
      
      switch (xMaterial) {
         case SUGAR:
            return PotionEffectType.SPEED;
         case SOUL_SAND:
            return PotionEffectType.SLOW;
         case POTION:
         case SPLASH_POTION:
         case LINGERING_POTION:
            // Potion types (HASTE_POTION, MINING_FATIGUE_POTION, STRENGTH_POTION, INSTANT_HEAL, INSTANT_DAMAGE)
            // are all represented by POTION with different metadata in 1.8.8
            // This would need to check PotionMeta to determine the actual effect type
            // For now, return null as we can't determine the effect without metadata
            return null;
         case RABBIT_FOOT:
            return PotionEffectType.JUMP;
         case CHORUS_FRUIT:
            return PotionEffectType.CONFUSION;
         case GHAST_TEAR:
            return PotionEffectType.REGENERATION;
         case TURTLE_HELMET:
            return PotionEffectType.DAMAGE_RESISTANCE;
         case MAGMA_CREAM:
            return PotionEffectType.FIRE_RESISTANCE;
         case PUFFERFISH:
            return PotionEffectType.WATER_BREATHING;
         case GOLDEN_CARROT:
            return PotionEffectType.NIGHT_VISION;
         case INK_SAC:
            return PotionEffectType.BLINDNESS;
         case ROTTEN_FLESH:
            return PotionEffectType.HUNGER;
         case MILK_BUCKET:
            return PotionEffectType.WEAKNESS;
         case SPIDER_EYE:
            return PotionEffectType.POISON;
         case WITHER_SKELETON_SKULL:
            return PotionEffectType.WITHER;
         case GOLDEN_APPLE:
            return PotionEffectType.HEALTH_BOOST;
         case COOKED_BEEF:
            return PotionEffectType.SATURATION;
         default:
            return null;
      }
   }
   
   /**
    * Apply effects to multiple players
    * @param players the players
    * @param effectType the effect type
    * @param amplifier the amplifier level
    */
   public static void applyEffectToPlayers(Collection<Player> players, PotionEffectType effectType, int amplifier) {
      if (players == null || players.isEmpty()) {
         return;
      }
      
      for (Player player : players) {
         applyEffect(player, effectType, amplifier);
      }
   }
   
   /**
    * Apply effects with delay
    * @param player the player
    * @param effectType the effect type
    * @param amplifier the amplifier level
    * @param delayTicks the delay in ticks
    */
   public static void applyEffectDelayed(Player player, PotionEffectType effectType, int amplifier, long delayTicks) {
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
         applyEffect(player, effectType, amplifier);
      }, delayTicks);
   }
   
   /**
    * Check if material provides effects
    * @param material the material
    * @return true if provides effects
    */
   public static boolean isEffectMaterial(Material material) {
      return EFFECT_MATERIALS.contains(material);
   }
}
