package dev.artixdev.practice.utils;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Kit;
import dev.artixdev.practice.enums.KitType;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Kit Applier Utility
 * Handles applying kits to players with potion effects and items
 */
public class KitApplier {
   
   private static final float DEFAULT_HEALTH = 20.0F;
   private static final float DEFAULT_FOOD_LEVEL = 20.0F;
   private static final int TASK_DELAY = 20; // 1 second
   
   // Material to potion effect mappings
   private static final Map<Material, PotionEffectType> MATERIAL_EFFECTS = new EnumMap<>(Material.class);
   private static final Set<PotionEffectType> REMOVABLE_EFFECTS = ImmutableSet.of(
      PotionEffectType.SPEED,
      PotionEffectType.SLOW,
      PotionEffectType.FAST_DIGGING,
      PotionEffectType.SLOW_DIGGING,
      PotionEffectType.INCREASE_DAMAGE,
      PotionEffectType.HEAL,
      PotionEffectType.HARM,
      PotionEffectType.JUMP,
      PotionEffectType.CONFUSION,
      PotionEffectType.REGENERATION,
      PotionEffectType.DAMAGE_RESISTANCE,
      PotionEffectType.FIRE_RESISTANCE,
      PotionEffectType.WATER_BREATHING,
      PotionEffectType.INVISIBILITY,
      PotionEffectType.BLINDNESS,
      PotionEffectType.NIGHT_VISION,
      PotionEffectType.HUNGER,
      PotionEffectType.WEAKNESS,
      PotionEffectType.POISON,
      PotionEffectType.WITHER,
      PotionEffectType.HEALTH_BOOST,
      PotionEffectType.ABSORPTION,
      PotionEffectType.SATURATION
   );
   
   static {
      // Initialize material to effect mappings
      MATERIAL_EFFECTS.put(Material.SUGAR, PotionEffectType.SPEED);
      MATERIAL_EFFECTS.put(Material.SOUL_SAND, PotionEffectType.SLOW);
      MATERIAL_EFFECTS.put(Material.GOLD_PICKAXE, PotionEffectType.FAST_DIGGING); // Used as HASTE indicator
      MATERIAL_EFFECTS.put(Material.SLIME_BLOCK, PotionEffectType.SLOW_DIGGING);    // Used as MINING FATIGUE indicator
      MATERIAL_EFFECTS.put(Material.BLAZE_POWDER, PotionEffectType.INCREASE_DAMAGE); // Used as STRENGTH indicator
      MATERIAL_EFFECTS.put(Material.SPECKLED_MELON, PotionEffectType.HEAL);  // Used as INSTANT_HEAL indicator
      MATERIAL_EFFECTS.put(Material.FERMENTED_SPIDER_EYE, PotionEffectType.HARM);    // Used as INSTANT_DAMAGE indicator
      MATERIAL_EFFECTS.put(Material.RABBIT_FOOT, PotionEffectType.JUMP);
      // MATERIAL_EFFECTS.put(Material.CHORUS_FRUIT, PotionEffectType.CONFUSION); // Removed: CHORUS_FRUIT is not a valid Material in this version
      MATERIAL_EFFECTS.put(Material.GHAST_TEAR, PotionEffectType.REGENERATION);
      // MATERIAL_EFFECTS.put(Material.TURTLE_HELMET, PotionEffectType.DAMAGE_RESISTANCE); // Removed: TURTLE_HELMET is not a valid Material in this version
      MATERIAL_EFFECTS.put(Material.MAGMA_CREAM, PotionEffectType.FIRE_RESISTANCE);
      // MATERIAL_EFFECTS.put(Material.PUFFERFISH, PotionEffectType.WATER_BREATHING); // Removed: PUFFERFISH is not a valid Material in this version
      MATERIAL_EFFECTS.put(Material.GOLDEN_CARROT, PotionEffectType.NIGHT_VISION);
      // MATERIAL_EFFECTS.put(Material.INK_SAC, PotionEffectType.BLINDNESS); // Removed: INK_SAC is not a valid Material in this version
      MATERIAL_EFFECTS.put(Material.ROTTEN_FLESH, PotionEffectType.HUNGER);
      MATERIAL_EFFECTS.put(Material.MILK_BUCKET, PotionEffectType.WEAKNESS);
      MATERIAL_EFFECTS.put(Material.SPIDER_EYE, PotionEffectType.POISON);
      // MATERIAL_EFFECTS.put(Material.WITHER_SKELETON_SKULL, PotionEffectType.WITHER); // Removed: WITHER_SKELETON_SKULL is not a valid Material in this version
      MATERIAL_EFFECTS.put(Material.GOLDEN_APPLE, PotionEffectType.HEALTH_BOOST);
      MATERIAL_EFFECTS.put(Material.GOLDEN_APPLE, PotionEffectType.ABSORPTION);
      MATERIAL_EFFECTS.put(Material.COOKED_BEEF, PotionEffectType.SATURATION);
   }
   
   /**
    * Apply kit to player
    * @param player the player
    * @param kit the kit to apply
    * @param clearInventory whether to clear inventory first
    */
   public static void applyKit(Player player, Kit kit, boolean clearInventory) {
      if (player == null || kit == null) {
         return;
      }
      
      // Clear inventory if requested
      if (clearInventory) {
         player.getInventory().clear();
         player.getInventory().setArmorContents(new ItemStack[4]);
      }
      
      // Apply kit items
      applyKitItems(player, kit);
      
      // Apply potion effects
      applyPotionEffects(player, kit);
      
      // Reset health and food
      player.setHealth(DEFAULT_HEALTH);
      player.setFoodLevel((int) DEFAULT_FOOD_LEVEL);
      player.setSaturation(20.0F);
      
      // Clear any existing effects
      clearRemovableEffects(player);
      
      // Apply kit-specific effects
      applyKitSpecificEffects(player, kit);
   }
   
   /**
    * Apply kit items to player
    * @param player the player
    * @param kit the kit
    */
   private static void applyKitItems(Player player, Kit kit) {
      if (kit.getItems() != null) {
         ItemStack[] items = kit.getItems();
         for (ItemStack item : items) {
            if (item != null) {
               player.getInventory().addItem(item.clone());
            }
         }
      }
      
      if (kit.getArmor() != null) {
         ItemStack[] armor = (ItemStack[]) kit.getArmor();
         ItemStack[] armorClone = new ItemStack[armor.length];
         for (int i = 0; i < armor.length; i++) {
            armorClone[i] = armor[i] != null ? armor[i].clone() : null;
         }
         player.getInventory().setArmorContents(armorClone);
      }
   }
   
   /**
    * Apply potion effects based on kit
    * @param player the player
    * @param kit the kit
    */
   private static void applyPotionEffects(Player player, Kit kit) {
      if (kit.getPotionEffects() != null) {
         for (PotionEffect effect : kit.getPotionEffects()) {
            player.addPotionEffect(effect, true);
         }
      }
   }
   
   /**
    * Clear removable effects from player
    * @param player the player
    */
   private static void clearRemovableEffects(Player player) {
      for (PotionEffectType effectType : REMOVABLE_EFFECTS) {
         player.removePotionEffect(effectType);
      }
   }
   
   /**
    * Apply kit-specific effects
    * @param player the player
    * @param kit the kit
    */
   private static void applyKitSpecificEffects(Player player, Kit kit) {
      KitType kitType = kit.getKitType();
      
      switch (kitType) {
         case SUMO:
            // Sumo specific effects
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200, false, false));
            break;
            
         case BOXING:
            // Boxing specific effects
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            break;
            
         case CUSTOM:
            // Custom kit effects are handled by the kit itself
            break;
      }
   }
   
   /**
    * Apply kit to all players in a list
    * @param players the players
    * @param kit the kit
    * @param clearInventory whether to clear inventory
    */
   public static void applyKitToPlayers(List<Player> players, Kit kit, boolean clearInventory) {
      if (players == null || players.isEmpty()) {
         return;
      }
      
      for (Player player : players) {
         applyKit(player, kit, clearInventory);
      }
   }
   
   /**
    * Apply kit with delay
    * @param player the player
    * @param kit the kit
    * @param clearInventory whether to clear inventory
    * @param delayTicks the delay in ticks
    */
   public static void applyKitDelayed(Player player, Kit kit, boolean clearInventory, long delayTicks) {
      BukkitScheduler scheduler = Bukkit.getScheduler();
      scheduler.runTaskLater(Main.getInstance(), () -> {
         applyKit(player, kit, clearInventory);
      }, delayTicks);
   }
   
   /**
    * Get material effect type
    * @param material the material
    * @return effect type or null
    */
   public static PotionEffectType getMaterialEffect(Material material) {
      return MATERIAL_EFFECTS.get(material);
   }
   
   /**
    * Check if effect is removable
    * @param effectType the effect type
    * @return true if removable
    */
   public static boolean isRemovableEffect(PotionEffectType effectType) {
      return REMOVABLE_EFFECTS.contains(effectType);
   }
}
