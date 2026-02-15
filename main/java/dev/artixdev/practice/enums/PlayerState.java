package dev.artixdev.practice.enums;

/**
 * Player State Enum
 * Defines different states a player can be in
 */
public enum PlayerState {
   LOBBY("Lobby"),
   QUEUE("Queue"),
   FIGHTING("Fighting"),
   SPECTATING("Spectating"),
   FREE("Free"),
   READY("Ready"),
   LEADER("Leader");
   
   private final String displayName;
   
   PlayerState(String displayName) {
      this.displayName = displayName;
   }
   
   public String getDisplayName() {
      return displayName;
   }
   
   /**
    * Check if player is in a match
    * @return true if in match
    */
   public boolean isInMatch() {
      return this == FIGHTING || this == SPECTATING;
   }
   
   /**
    * Check if player is in queue
    * @return true if in queue
    */
   public boolean isInQueue() {
      return this == QUEUE;
   }
   
   /**
    * Check if player is in lobby
    * @return true if in lobby
    */
   public boolean isInLobby() {
      return this == LOBBY;
   }
   
   /**
    * Check if player is free
    * @return true if free
    */
   public boolean isFree() {
      return this == FREE;
   }
   
   /**
    * Check if player is spectating
    * @return true if spectating
    */
   public boolean isSpectating() {
      return this == SPECTATING;
   }
   
   /**
    * Check if player is fighting
    * @return true if fighting
    */
   public boolean isFighting() {
      return this == FIGHTING;
   }
   
   /**
    * Get player state from string
    * @param name the name
    * @return player state or null
    */
   public static PlayerState fromString(String name) {
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
    * Get all match states
    * @return array of match states
    */
   public static PlayerState[] getMatchStates() {
      return new PlayerState[]{FIGHTING, SPECTATING};
   }
   
   /**
    * Get all non-match states
    * @return array of non-match states
    */
   public static PlayerState[] getNonMatchStates() {
      return new PlayerState[]{LOBBY, QUEUE, FREE};
   }
   
   /**
    * Get all active states
    * @return array of active states
    */
   public static PlayerState[] getActiveStates() {
      return new PlayerState[]{QUEUE, FIGHTING, SPECTATING};
   }
   
   /**
    * Get all inactive states
    * @return array of inactive states
    */
   public static PlayerState[] getInactiveStates() {
      return new PlayerState[]{LOBBY, FREE};
   }
   
   /**
    * Check if player state is valid
    * @param state the state
    * @return true if valid
    */
   public static boolean isValid(PlayerState state) {
      return state != null;
   }
   
   /**
    * Get player state priority
    * @return priority (higher = more important)
    */
   public int getPriority() {
      switch (this) {
         case FIGHTING:
            return 5;
         case SPECTATING:
            return 4;
         case QUEUE:
            return 3;
         case LOBBY:
            return 2;
         case FREE:
            return 1;
         default:
            return 0;
      }
   }
   
   /**
    * Get player state description
    * @return description
    */
   public String getDescription() {
      switch (this) {
         case LOBBY:
            return "Player is in the lobby";
         case QUEUE:
            return "Player is in a queue waiting for a match";
         case FIGHTING:
            return "Player is currently fighting in a match";
         case SPECTATING:
            return "Player is spectating a match";
         case FREE:
            return "Player is free to do anything";
         default:
            return "Unknown player state";
      }
   }
   
   /**
    * Get player state color
    * @return color code
    */
   public String getColor() {
      switch (this) {
         case LOBBY:
            return "&a";
         case QUEUE:
            return "&e";
         case FIGHTING:
            return "&c";
         case SPECTATING:
            return "&7";
         case FREE:
            return "&f";
         default:
            return "&f";
      }
   }
}