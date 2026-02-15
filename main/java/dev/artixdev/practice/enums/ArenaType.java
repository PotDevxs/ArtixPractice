package dev.artixdev.practice.enums;

/**
 * Arena Type Enum
 * Defines different types of arenas
 */
public enum ArenaType {
   DUEL("Duel", "1v1 combat arena"),
   SUMO("Sumo", "Sumo wrestling arena"),
   BOXING("Boxing", "Boxing arena with fists only"),
   FFA("Free For All", "Free for all arena"),
   TOURNAMENT("Tournament", "Tournament arena"),
   TEAM("Team", "Team-based arena"),
   RANKED("Ranked", "Ranked competitive arena"),
   UNRANKED("Unranked", "Unranked casual arena"),
   BRIDGES("Bridges", "Bridges arena");
   
   private final String displayName;
   private final String description;
   
   ArenaType(String displayName, String description) {
      this.displayName = displayName;
      this.description = description;
   }
   
   public String getDisplayName() {
      return displayName;
   }
   
   public String getDescription() {
      return description;
   }
   
   /**
    * Check if arena type is competitive
    * @return true if competitive
    */
   public boolean isCompetitive() {
      return this == RANKED || this == TOURNAMENT || this == DUEL;
   }
   
   /**
    * Check if arena type is ranked
    * @return true if ranked
    */
   public boolean isRanked() {
      return this == RANKED || this == TOURNAMENT;
   }
   
   /**
    * Check if arena type is casual
    * @return true if casual
    */
   public boolean isCasual() {
      return this == UNRANKED || this == FFA;
   }
   
   /**
    * Check if arena type is team-based
    * @return true if team-based
    */
   public boolean isTeamBased() {
      return this == TEAM || this == TOURNAMENT;
   }
   
   /**
    * Check if arena type is solo
    * @return true if solo
    */
   public boolean isSolo() {
      return this == DUEL || this == SUMO || this == BOXING || this == FFA;
   }
   
   /**
    * Get arena type from string
    * @param name the name
    * @return arena type or null
    */
   public static ArenaType fromString(String name) {
      if (name == null) {
         return null;
      }
      
      try {
         return valueOf(name.toUpperCase());
      } catch (IllegalArgumentException e) {
         return null;
      }
   }
   
   /**
    * Get all competitive arena types
    * @return array of competitive arena types
    */
   public static ArenaType[] getCompetitiveTypes() {
      return new ArenaType[]{RANKED, TOURNAMENT, DUEL};
   }
   
   /**
    * Get all ranked arena types
    * @return array of ranked arena types
    */
   public static ArenaType[] getRankedTypes() {
      return new ArenaType[]{RANKED, TOURNAMENT};
   }
   
   /**
    * Get all casual arena types
    * @return array of casual arena types
    */
   public static ArenaType[] getCasualTypes() {
      return new ArenaType[]{UNRANKED, FFA};
   }
   
   /**
    * Get all team arena types
    * @return array of team arena types
    */
   public static ArenaType[] getTeamTypes() {
      return new ArenaType[]{TEAM, TOURNAMENT};
   }
   
   /**
    * Get all solo arena types
    * @return array of solo arena types
    */
   public static ArenaType[] getSoloTypes() {
      return new ArenaType[]{DUEL, SUMO, BOXING, FFA};
   }
   
   /**
    * Check if arena type is valid
    * @param type the type
    * @return true if valid
    */
   public static boolean isValid(ArenaType type) {
      return type != null;
   }
   
   /**
    * Get arena type priority
    * @return priority (higher = more important)
    */
   public int getPriority() {
      switch (this) {
         case TOURNAMENT:
            return 6;
         case RANKED:
            return 5;
         case TEAM:
            return 4;
         case DUEL:
            return 3;
         case SUMO:
         case BOXING:
            return 2;
         case FFA:
         case UNRANKED:
            return 1;
         default:
            return 0;
      }
   }
   
   /**
    * Get arena type icon material
    * @return material name
    */
   public String getIconMaterial() {
      switch (this) {
         case DUEL:
            return "DIAMOND_SWORD";
         case SUMO:
            return "SLIME_BALL";
         case BOXING:
            return "STICK";
         case FFA:
            return "BOW";
         case TOURNAMENT:
            return "GOLDEN_APPLE";
         case TEAM:
            return "NETHER_STAR";
         case RANKED:
            return "GOLDEN_SWORD";
         case UNRANKED:
            return "WOODEN_SWORD";
         default:
            return "DIAMOND_SWORD";
      }
   }
   
   /**
    * Get arena type color
    * @return color code
    */
   public String getColor() {
      switch (this) {
         case DUEL:
            return "&a";
         case SUMO:
            return "&e";
         case BOXING:
            return "&c";
         case FFA:
            return "&b";
         case TOURNAMENT:
            return "&6";
         case TEAM:
            return "&d";
         case RANKED:
            return "&5";
         case UNRANKED:
            return "&7";
         default:
            return "&f";
      }
   }
}