package dev.artixdev.practice.enums;

import com.google.common.base.Preconditions;

/**
 * Relation Type
 * Enum for different types of player relations
 */
public enum RelationType {
   
   // Positive relations
   FRIEND("Friend", "§a", "You are friends with"),
   ALLY("Ally", "§b", "You are allied with"),
   TEAMMATE("Teammate", "§9", "You are teammates with"),
   PARTNER("Partner", "§d", "You are partners with"),
   MENTOR("Mentor", "§6", "You are mentored by"),
   STUDENT("Student", "§e", "You are a student of"),
   
   // Neutral relations
   NEUTRAL("Neutral", "§7", "You are neutral with"),
   ACQUAINTANCE("Acquaintance", "§f", "You are acquainted with"),
   RIVAL("Rival", "§c", "You are rivals with"),
   COMPETITOR("Competitor", "§5", "You are competitors with"),
   
   // Negative relations
   ENEMY("Enemy", "§4", "You are enemies with"),
   HOSTILE("Hostile", "§c", "You are hostile with"),
   BLOCKED("Blocked", "§8", "You have blocked"),
   IGNORED("Ignored", "§0", "You are ignoring"),
   BANNED("Banned", "§4", "You have banned"),
   MUTED("Muted", "§6", "You have muted"),
   
   // Special relations
   ADMIN("Admin", "§c", "You are an admin to"),
   MODERATOR("Moderator", "§6", "You are a moderator to"),
   HELPER("Helper", "§a", "You are a helper to"),
   VIP("VIP", "§b", "You are a VIP to"),
   PREMIUM("Premium", "§d", "You are premium to"),
   DONATOR("Donator", "§5", "You are a donator to"),
   YOUTUBER("YouTuber", "§c", "You are a YouTuber to"),
   STREAMER("Streamer", "§9", "You are a streamer to"),
   DEVELOPER("Developer", "§e", "You are a developer to"),
   OWNER("Owner", "§4", "You are the owner to");
   
   private final String name;
   private final String color;
   private final String description;
   
   /**
    * Constructor
    * @param name the relation name
    * @param color the color code
    * @param description the description
    */
   RelationType(String name, String color, String description) {
      this.name = name;
      this.color = color;
      this.description = description;
   }
   
   /**
    * Get relation name
    * @return relation name
    */
   public String getName() {
      return name;
   }
   
   /**
    * Get color code
    * @return color code
    */
   public String getColor() {
      return color;
   }
   
   /**
    * Get description
    * @return description
    */
   public String getDescription() {
      return description;
   }
   
   /**
    * Get colored name
    * @return colored name
    */
   public String getColoredName() {
      return color + name;
   }
   
   /**
    * Get full description
    * @return full description
    */
   public String getFullDescription() {
      return color + description;
   }
   
   /**
    * Check if relation is positive
    * @return true if positive
    */
   public boolean isPositive() {
      return this == FRIEND || 
             this == ALLY || 
             this == TEAMMATE || 
             this == PARTNER || 
             this == MENTOR || 
             this == STUDENT;
   }
   
   /**
    * Check if relation is negative
    * @return true if negative
    */
   public boolean isNegative() {
      return this == ENEMY || 
             this == HOSTILE || 
             this == BLOCKED || 
             this == IGNORED || 
             this == BANNED || 
             this == MUTED;
   }
   
   /**
    * Check if relation is neutral
    * @return true if neutral
    */
   public boolean isNeutral() {
      return this == NEUTRAL || 
             this == ACQUAINTANCE || 
             this == RIVAL || 
             this == COMPETITOR;
   }
   
   /**
    * Check if relation is special
    * @return true if special
    */
   public boolean isSpecial() {
      return this == ADMIN || 
             this == MODERATOR || 
             this == HELPER || 
             this == VIP || 
             this == PREMIUM || 
             this == DONATOR || 
             this == YOUTUBER || 
             this == STREAMER || 
             this == DEVELOPER || 
             this == OWNER;
   }
   
   /**
    * Check if relation is staff
    * @return true if staff
    */
   public boolean isStaff() {
      return this == ADMIN || 
             this == MODERATOR || 
             this == HELPER || 
             this == DEVELOPER || 
             this == OWNER;
   }
   
   /**
    * Check if relation is donator
    * @return true if donator
    */
   public boolean isDonator() {
      return this == VIP || 
             this == PREMIUM || 
             this == DONATOR || 
             this == YOUTUBER || 
             this == STREAMER;
   }
   
   /**
    * Get relation by name
    * @param name the relation name
    * @return relation type
    */
   public static RelationType getByName(String name) {
      Preconditions.checkNotNull(name, "Name cannot be null");
      
      for (RelationType type : values()) {
         if (type.getName().equalsIgnoreCase(name)) {
            return type;
         }
      }
      return null;
   }
   
   /**
    * Get relation by colored name
    * @param coloredName the colored name
    * @return relation type
    */
   public static RelationType getByColoredName(String coloredName) {
      Preconditions.checkNotNull(coloredName, "Colored name cannot be null");
      
      for (RelationType type : values()) {
         if (type.getColoredName().equals(coloredName)) {
            return type;
         }
      }
      return null;
   }
   
   /**
    * Get all positive relations
    * @return array of positive relations
    */
   public static RelationType[] getPositiveRelations() {
      return java.util.Arrays.stream(values())
         .filter(RelationType::isPositive)
         .toArray(RelationType[]::new);
   }
   
   /**
    * Get all negative relations
    * @return array of negative relations
    */
   public static RelationType[] getNegativeRelations() {
      return java.util.Arrays.stream(values())
         .filter(RelationType::isNegative)
         .toArray(RelationType[]::new);
   }
   
   /**
    * Get all neutral relations
    * @return array of neutral relations
    */
   public static RelationType[] getNeutralRelations() {
      return java.util.Arrays.stream(values())
         .filter(RelationType::isNeutral)
         .toArray(RelationType[]::new);
   }
   
   /**
    * Get all special relations
    * @return array of special relations
    */
   public static RelationType[] getSpecialRelations() {
      return java.util.Arrays.stream(values())
         .filter(RelationType::isSpecial)
         .toArray(RelationType[]::new);
   }
   
   /**
    * Get all staff relations
    * @return array of staff relations
    */
   public static RelationType[] getStaffRelations() {
      return java.util.Arrays.stream(values())
         .filter(RelationType::isStaff)
         .toArray(RelationType[]::new);
   }
   
   /**
    * Get all donator relations
    * @return array of donator relations
    */
   public static RelationType[] getDonatorRelations() {
      return java.util.Arrays.stream(values())
         .filter(RelationType::isDonator)
         .toArray(RelationType[]::new);
   }
   
   /**
    * Get relation priority
    * @return priority (higher = more important)
    */
   public int getPriority() {
      switch (this) {
         case OWNER: return 100;
         case DEVELOPER: return 90;
         case ADMIN: return 80;
         case MODERATOR: return 70;
         case HELPER: return 60;
         case YOUTUBER: return 50;
         case STREAMER: return 40;
         case DONATOR: return 30;
         case PREMIUM: return 20;
         case VIP: return 10;
         default: return 0;
      }
   }
   
   /**
    * Compare relation priorities
    * @param other the other relation type
    * @return comparison result
    */
   public int comparePriority(RelationType other) {
      return Integer.compare(this.getPriority(), other.getPriority());
   }
   
   @Override
   public String toString() {
      return getColoredName();
   }
}
