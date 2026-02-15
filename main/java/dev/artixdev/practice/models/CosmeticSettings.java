package dev.artixdev.practice.models;

import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

/**
 * Cosmetic Settings
 * Model for player cosmetic settings
 */
public class CosmeticSettings {
   
   @SerializedName("killEffect")
   private KillEffect killEffect;
   
   @SerializedName("killMessage")
   private KillMessage killMessage;
   
   @SerializedName("trail")
   private Trail trail;
   
   /**
    * Constructor
    */
   public CosmeticSettings() {
      this.killEffect = KillEffect.NONE;
      this.killMessage = KillMessage.DEFAULT;
      this.trail = Trail.NONE;
   }
   
   /**
    * Get kill effect
    * @return kill effect
    */
   public KillEffect getKillEffect() {
      return killEffect;
   }
   
   /**
    * Set kill effect
    * @param killEffect the kill effect
    */
   public void setKillEffect(KillEffect killEffect) {
      this.killEffect = killEffect;
   }
   
   /**
    * Get kill message
    * @return kill message
    */
   public KillMessage getKillMessage() {
      return killMessage;
   }
   
   /**
    * Set kill message
    * @param killMessage the kill message
    */
   public void setKillMessage(KillMessage killMessage) {
      this.killMessage = killMessage;
   }
   
   /**
    * Get trail
    * @return trail
    */
   public Trail getTrail() {
      return trail;
   }
   
   /**
    * Set trail
    * @param trail the trail
    */
   public void setTrail(Trail trail) {
      this.trail = trail;
   }
   
   /**
    * Check if has kill effect
    * @return true if has kill effect
    */
   public boolean hasKillEffect() {
      return killEffect != null && killEffect != KillEffect.NONE;
   }
   
   /**
    * Check if has kill message
    * @return true if has kill message
    */
   public boolean hasKillMessage() {
      return killMessage != null && killMessage != KillMessage.DEFAULT;
   }
   
   /**
    * Check if has trail
    * @return true if has trail
    */
   public boolean hasTrail() {
      return trail != null && trail != Trail.NONE;
   }
   
   /**
    * Reset all cosmetics
    */
   public void resetAll() {
      this.killEffect = KillEffect.NONE;
      this.killMessage = KillMessage.DEFAULT;
      this.trail = Trail.NONE;
   }
   
   /**
    * Check if has any cosmetics
    * @return true if has any cosmetics
    */
   public boolean hasAnyCosmetics() {
      return hasKillEffect() || hasKillMessage() || hasTrail();
   }
   
   /**
    * Get cosmetic count
    * @return cosmetic count
    */
   public int getCosmeticCount() {
      int count = 0;
      if (hasKillEffect()) count++;
      if (hasKillMessage()) count++;
      if (hasTrail()) count++;
      return count;
   }
   
   /**
    * Get cosmetics info
    * @return cosmetics info
    */
   public String getCosmeticsInfo() {
      return String.format("Kill Effect: %s, Kill Message: %s, Trail: %s", 
         killEffect.name(), killMessage.name(), trail.name());
   }
   
   /**
    * Kill Effect enum
    */
   public enum KillEffect {
      NONE("None", "No kill effect"),
      EXPLOSION("Explosion", "Explosion effect on kill"),
      LIGHTNING("Lightning", "Lightning effect on kill"),
      FIREWORK("Firework", "Firework effect on kill"),
      HEARTS("Hearts", "Hearts effect on kill"),
      VILLAGER_HAPPY("Villager Happy", "Happy villager effect on kill"),
      ANGRY_VILLAGER("Angry Villager", "Angry villager effect on kill"),
      MAGIC_CRIT("Magic Crit", "Magic critical hit effect on kill"),
      SWEEP_ATTACK("Sweep Attack", "Sweep attack effect on kill"),
      ENCHANTMENT_TABLE("Enchantment Table", "Enchantment table effect on kill");
      
      private final String name;
      private final String description;
      
      KillEffect(String name, String description) {
         this.name = name;
         this.description = description;
      }
      
      public String getName() {
         return name;
      }
      
      public String getDescription() {
         return description;
      }
   }
   
   /**
    * Kill Message enum
    */
   public enum KillMessage {
      DEFAULT("Default", "Default kill message"),
      SIMPLE("Simple", "Simple kill message"),
      FANCY("Fancy", "Fancy kill message"),
      EPIC("Epic", "Epic kill message"),
      LEGENDARY("Legendary", "Legendary kill message"),
      MYTHICAL("Mythical", "Mythical kill message"),
      CUSTOM("Custom", "Custom kill message");
      
      private final String name;
      private final String description;
      
      KillMessage(String name, String description) {
         this.name = name;
         this.description = description;
      }
      
      public String getName() {
         return name;
      }
      
      public String getDescription() {
         return description;
      }
   }
   
   /**
    * Trail enum
    */
   public enum Trail {
      NONE("None", "No trail"),
      FIRE("Fire", "Fire trail"),
      WATER("Water", "Water trail"),
      LAVA("Lava", "Lava trail"),
      SMOKE("Smoke", "Smoke trail"),
      HEARTS("Hearts", "Hearts trail"),
      VILLAGER_HAPPY("Villager Happy", "Happy villager trail"),
      ANGRY_VILLAGER("Angry Villager", "Angry villager trail"),
      MAGIC_CRIT("Magic Crit", "Magic critical hit trail"),
      ENCHANTMENT_TABLE("Enchantment Table", "Enchantment table trail");
      
      private final String name;
      private final String description;
      
      Trail(String name, String description) {
         this.name = name;
         this.description = description;
      }
      
      public String getName() {
         return name;
      }
      
      public String getDescription() {
         return description;
      }
   }
}
