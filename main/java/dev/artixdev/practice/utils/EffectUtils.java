package dev.artixdev.practice.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.enums.KitType;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Effect Utils
 * Utility class for managing potion effects and materials
 */
public class EffectUtils {
   
   // Materials that provide special effects
   private static final Set<Material> SPECIAL_MATERIALS = new HashSet<>();
   
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
         SPECIAL_MATERIALS.add(material);
      }
   }
   
   /**
    * Apply kit-specific effects
    * @param player the player
    * @param kitType the kit type
    */
   public static void applyKitEffects(Player player, KitType kitType) {
      if (player == null || kitType == null) {
         return;
      }
      
      switch (kitType) {
         case SUMO:
            applySumoEffects(player);
            break;
         case BOXING:
            applyBoxingEffects(player);
            break;
         case CUSTOM:
            // Custom effects are handled by the kit itself
            break;
      }
   }
   
   /**
    * Apply sumo-specific effects
    * @param player the player
    */
   private static void applySumoEffects(Player player) {
      // Slow movement
      player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false));
      
      // High jump
      // JUMP_BOOST exists only in 1.9+, use JUMP for 1.8.8 compatibility
      PotionEffectType jumpType = PotionEffectType.JUMP;
      try {
         // Try to use JUMP_BOOST if available (1.9+)
         jumpType = PotionEffectType.getByName("JUMP_BOOST");
         if (jumpType == null) {
            jumpType = PotionEffectType.JUMP;
         }
      } catch (Exception e) {
         jumpType = PotionEffectType.JUMP;
      }
      player.addPotionEffect(new PotionEffect(jumpType, Integer.MAX_VALUE, 200, false, false));
      
      // Resistance to knockback
      player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
   }
   
   /**
    * Apply boxing-specific effects
    * @param player the player
    */
   private static void applyBoxingEffects(Player player) {
      // Speed boost
      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
      
      // Strength boost
      player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
      
      // Regeneration
      player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false));
   }
   
   /**
    * Apply material-based effects
    * @param player the player
    * @param material the material
    * @param amplifier the amplifier level
    */
   public static void applyMaterialEffect(Player player, Material material, int amplifier) {
      if (player == null || material == null || !SPECIAL_MATERIALS.contains(material)) {
         return;
      }
      
      PotionEffectType effectType = getEffectTypeFromMaterial(material);
      if (effectType != null) {
         player.addPotionEffect(new PotionEffect(effectType, Integer.MAX_VALUE, amplifier, false, true));
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
    * Clear all effects from player
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
         player.removePotionEffect(effectType);
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
    * Check if material provides effects
    * @param material the material
    * @return true if provides effects
    */
   public static boolean isSpecialMaterial(Material material) {
      return SPECIAL_MATERIALS.contains(material);
   }
   
   /**
    * Apply effects to multiple players
    * @param players the players
    * @param effectType the effect type
    * @param amplifier the amplifier level
    */
   public static void applyEffectToPlayers(List<Player> players, PotionEffectType effectType, int amplifier) {
      if (players == null || players.isEmpty()) {
         return;
      }
      
      for (Player player : players) {
         player.addPotionEffect(new PotionEffect(effectType, Integer.MAX_VALUE, amplifier, false, true));
      }
   }
   
   /**
    * Remove effects from multiple players
    * @param players the players
    * @param effectTypes the effect types to remove
    */
   public static void removeEffectsFromPlayers(List<Player> players, PotionEffectType... effectTypes) {
      if (players == null || players.isEmpty() || effectTypes == null) {
         return;
      }
      
      for (Player player : players) {
         clearEffects(player, effectTypes);
      }
   }
}
