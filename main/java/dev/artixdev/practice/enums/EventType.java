package dev.artixdev.practice.enums;

/**
 * Event Type Enum
 * Defines different types of events
 */
public enum EventType {
   NONE("None", "No event"),
   DUEL("Duel", "1v1 combat event"),
   SUMO("Sumo", "Sumo wrestling event"),
   BOXING("Boxing", "Boxing event with fists only"),
   FFA("Free For All", "Free for all event"),
   TOURNAMENT("Tournament", "Tournament event"),
   TEAM("Team", "Team-based event"),
   RANKED("Ranked", "Ranked competitive event"),
   UNRANKED("Unranked", "Unranked casual event"),
   PRACTICE("Practice", "Practice event"),
   CUSTOM("Custom", "Custom event");
   
   private final String displayName;
   private final String description;
   
   EventType(String displayName, String description) {
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
    * Check if event type is competitive
    * @return true if competitive
    */
   public boolean isCompetitive() {
      return this == RANKED || this == TOURNAMENT || this == DUEL;
   }
   
   /**
    * Check if event type is ranked
    * @return true if ranked
    */
   public boolean isRanked() {
      return this == RANKED || this == TOURNAMENT;
   }
   
   /**
    * Check if event type is casual
    * @return true if casual
    */
   public boolean isCasual() {
      return this == UNRANKED || this == FFA || this == PRACTICE;
   }
   
   /**
    * Check if event type is team-based
    * @return true if team-based
    */
   public boolean isTeamBased() {
      return this == TEAM || this == TOURNAMENT;
   }
   
   /**
    * Check if event type is solo
    * @return true if solo
    */
   public boolean isSolo() {
      return this == DUEL || this == SUMO || this == BOXING || this == FFA;
   }
   
   /**
    * Check if event type is custom
    * @return true if custom
    */
   public boolean isCustom() {
      return this == CUSTOM;
   }
   
   /**
    * Check if event type is none
    * @return true if none
    */
   public boolean isNone() {
      return this == NONE;
   }
   
   /**
    * Get event type from string
    * @param name the name
    * @return event type or null
    */
   public static EventType fromString(String name) {
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
    * Get all competitive event types
    * @return array of competitive event types
    */
   public static EventType[] getCompetitiveTypes() {
      return new EventType[]{RANKED, TOURNAMENT, DUEL};
   }
   
   /**
    * Get all ranked event types
    * @return array of ranked event types
    */
   public static EventType[] getRankedTypes() {
      return new EventType[]{RANKED, TOURNAMENT};
   }
   
   /**
    * Get all casual event types
    * @return array of casual event types
    */
   public static EventType[] getCasualTypes() {
      return new EventType[]{UNRANKED, FFA, PRACTICE};
   }
   
   /**
    * Get all team event types
    * @return array of team event types
    */
   public static EventType[] getTeamTypes() {
      return new EventType[]{TEAM, TOURNAMENT};
   }
   
   /**
    * Get all solo event types
    * @return array of solo event types
    */
   public static EventType[] getSoloTypes() {
      return new EventType[]{DUEL, SUMO, BOXING, FFA};
   }
   
   /**
    * Get all custom event types
    * @return array of custom event types
    */
   public static EventType[] getCustomTypes() {
      return new EventType[]{CUSTOM};
   }
   
   /**
    * Get all non-none event types
    * @return array of non-none event types
    */
   public static EventType[] getNonNoneTypes() {
      return new EventType[]{DUEL, SUMO, BOXING, FFA, TOURNAMENT, TEAM, RANKED, UNRANKED, PRACTICE, CUSTOM};
   }
   
   /**
    * Check if event type is valid
    * @param type the type
    * @return true if valid
    */
   public static boolean isValid(EventType type) {
      return type != null && type != NONE;
   }
   
   /**
    * Get event type priority
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
         case PRACTICE:
            return 1;
         case CUSTOM:
            return 0;
         case NONE:
            return -1;
         default:
            return 0;
      }
   }
   
   /**
    * Get event type icon material
    * @return material name
    */
   public String getIconMaterial() {
      switch (this) {
         case NONE:
            return "BARRIER";
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
         case PRACTICE:
            return "BOOK";
         case CUSTOM:
            return "DIAMOND_SWORD";
         default:
            return "DIAMOND_SWORD";
      }
   }
   
   /**
    * Get event type color
    * @return color code
    */
   public String getColor() {
      switch (this) {
         case NONE:
            return "&7";
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
         case PRACTICE:
            return "&f";
         case CUSTOM:
            return "&9";
         default:
            return "&f";
      }
   }
}