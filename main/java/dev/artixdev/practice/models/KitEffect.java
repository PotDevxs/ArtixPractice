package dev.artixdev.practice.models;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Kit Effect Model
 * Represents a potion effect that can be applied to players
 */
public abstract class KitEffect {
   
   protected List<Material> materials;
   protected Set<PotionEffect> potionEffects;
   protected String name;
   public static long MAX_DURATION = TimeUnit.MINUTES.toMillis(8L);
   
   /**
    * Constructor
    * @param name the effect name
    * @param materials the materials
    */
   public KitEffect(String name, List<Material> materials) {
      this.name = name;
      this.materials = materials;
      this.potionEffects = new HashSet<>();
   }
   
   /**
    * Apply effect to player
    * @param player the player
    */
   public abstract void applyEffect(Player player);
   
   /**
    * Remove effect from player
    * @param player the player
    */
   public abstract void removeEffect(Player player);
   
   /**
    * Apply effect to player (implementation)
    * @param player the player
    */
   public void applyEffectToPlayer(Player player) {
      // Apply potion effects
      for (PotionEffect effect : potionEffects) {
         player.addPotionEffect(effect);
      }
   }
   
   /**
    * Get materials
    * @return materials list
    */
   public List<Material> getMaterials() {
      return materials;
   }
   
   /**
    * Get potion effects
    * @return potion effects set
    */
   public Set<PotionEffect> getPotionEffects() {
      return potionEffects;
   }
   
   /**
    * Get effect name
    * @return effect name
    */
   public String getName() {
      return name;
   }
   
   /**
    * Remove potion effects from player
    * @param player the player
    */
   public void removePotionEffects(Player player) {
      Iterator<PotionEffect> effectIterator = potionEffects.iterator();
      
      while (effectIterator.hasNext()) {
         PotionEffect effect = effectIterator.next();
         Iterator<PotionEffect> playerEffects = player.getActivePotionEffects().iterator();
         
         while (playerEffects.hasNext()) {
            PotionEffect playerEffect = playerEffects.next();
            
            // Check if effect duration is longer than max duration
            if (playerEffect.getDuration() > MAX_DURATION) {
               PotionEffectType effectType = playerEffect.getType();
               PotionEffectType targetType = effect.getType();
               
               if (effectType.equals(targetType)) {
                  if (playerEffect.getAmplifier() == effect.getAmplifier()) {
                     player.removePotionEffect(targetType);
                     break;
                  }
               }
            }
         }
      }
   }
   
   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      
      KitEffect kitEffect = (KitEffect) obj;
      
      if (potionEffects != null ? !potionEffects.equals(kitEffect.potionEffects) : kitEffect.potionEffects != null)
         return false;
      if (name != null ? !name.equals(kitEffect.name) : kitEffect.name != null)
         return false;
      return materials != null ? materials.equals(kitEffect.materials) : kitEffect.materials == null;
   }
   
   @Override
   public int hashCode() {
      int result = 1;
      result = 59 * result + (potionEffects != null ? potionEffects.hashCode() : 43);
      result = 59 * result + (name != null ? name.hashCode() : 43);
      result = 59 * result + (materials != null ? materials.hashCode() : 43);
      return result;
   }
   
   @Override
   public String toString() {
      return String.format("KitEffect{name='%s', materials=%s, potionEffects=%s}", 
         name, materials, potionEffects);
   }
}
