package dev.artixdev.practice.enums;

/**
 * Match Status Enum
 * Defines different states of a match
 */
public enum MatchStatus {
   WAITING("Waiting"),
   STARTING("Starting"),
   IN_PROGRESS("In Progress"),
   ENDING("Ending"),
   FINISHED("Finished");
   
   private final String displayName;
   
   MatchStatus(String displayName) {
      this.displayName = displayName;
   }
   
   public String getDisplayName() {
      return displayName;
   }
   
   /**
    * Check if match is active
    * @return true if active
    */
   public boolean isActive() {
      return this == WAITING || this == STARTING || this == IN_PROGRESS;
   }
   
   /**
    * Check if match is finished
    * @return true if finished
    */
   public boolean isFinished() {
      return this == ENDING || this == FINISHED;
   }
   
   /**
    * Check if match can be joined
    * @return true if can be joined
    */
   public boolean canJoin() {
      return this == WAITING;
   }
   
   /**
    * Check if match is in progress
    * @return true if in progress
    */
   public boolean isInProgress() {
      return this == IN_PROGRESS;
   }
   
   /**
    * Get next status
    * @return next status or null
    */
   public MatchStatus getNext() {
      switch (this) {
         case WAITING:
            return STARTING;
         case STARTING:
            return IN_PROGRESS;
         case IN_PROGRESS:
            return ENDING;
         case ENDING:
            return FINISHED;
         case FINISHED:
            return null;
         default:
            return null;
      }
   }
   
   /**
    * Get previous status
    * @return previous status or null
    */
   public MatchStatus getPrevious() {
      switch (this) {
         case WAITING:
            return null;
         case STARTING:
            return WAITING;
         case IN_PROGRESS:
            return STARTING;
         case ENDING:
            return IN_PROGRESS;
         case FINISHED:
            return ENDING;
         default:
            return null;
      }
   }
   
   /**
    * Get status from string
    * @param name the name
    * @return status or null
    */
   public static MatchStatus fromString(String name) {
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
    * Get all active statuses
    * @return array of active statuses
    */
   public static MatchStatus[] getActiveStatuses() {
      return new MatchStatus[]{WAITING, STARTING, IN_PROGRESS};
   }
   
   /**
    * Get all finished statuses
    * @return array of finished statuses
    */
   public static MatchStatus[] getFinishedStatuses() {
      return new MatchStatus[]{ENDING, FINISHED};
   }
}
