package dev.artixdev.practice.enums;

/**
 * Manager Status Enum
 * Defines different states of a manager
 */
public enum ManagerStatus {
   UNINITIALIZED("Uninitialized", "Manager has not been initialized"),
   INITIALIZING("Initializing", "Manager is currently being initialized"),
   INITIALIZED("Initialized", "Manager has been successfully initialized"),
   ENABLED("Enabled", "Manager is enabled and running"),
   DISABLED("Disabled", "Manager is disabled"),
   RELOADING("Reloading", "Manager is currently being reloaded"),
   ERROR("Error", "Manager encountered an error"),
   SHUTDOWN("Shutdown", "Manager has been shut down");
   
   private final String displayName;
   private final String description;
   
   ManagerStatus(String displayName, String description) {
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
    * Check if manager is active
    * @return true if active
    */
   public boolean isActive() {
      return this == ENABLED || this == INITIALIZED;
   }
   
   /**
    * Check if manager is inactive
    * @return true if inactive
    */
   public boolean isInactive() {
      return this == DISABLED || this == UNINITIALIZED || this == SHUTDOWN;
   }
   
   /**
    * Check if manager is in error state
    * @return true if in error
    */
   public boolean isError() {
      return this == ERROR;
   }
   
   /**
    * Check if manager is initializing
    * @return true if initializing
    */
   public boolean isInitializing() {
      return this == INITIALIZING || this == RELOADING;
   }
   
   /**
    * Check if manager is ready
    * @return true if ready
    */
   public boolean isReady() {
      return this == ENABLED;
   }
   
   /**
    * Get manager status from string
    * @param name the name
    * @return manager status or null
    */
   public static ManagerStatus fromString(String name) {
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
   public static ManagerStatus[] getActiveStatuses() {
      return new ManagerStatus[]{ENABLED, INITIALIZED};
   }
   
   /**
    * Get all inactive statuses
    * @return array of inactive statuses
    */
   public static ManagerStatus[] getInactiveStatuses() {
      return new ManagerStatus[]{DISABLED, UNINITIALIZED, SHUTDOWN};
   }
   
   /**
    * Get all error statuses
    * @return array of error statuses
    */
   public static ManagerStatus[] getErrorStatuses() {
      return new ManagerStatus[]{ERROR};
   }
   
   /**
    * Get all initializing statuses
    * @return array of initializing statuses
    */
   public static ManagerStatus[] getInitializingStatuses() {
      return new ManagerStatus[]{INITIALIZING, RELOADING};
   }
   
   /**
    * Check if status is valid
    * @param status the status
    * @return true if valid
    */
   public static boolean isValid(ManagerStatus status) {
      return status != null;
   }
   
   /**
    * Get status priority
    * @return priority (higher = more important)
    */
   public int getPriority() {
      switch (this) {
         case ERROR:
            return 5;
         case INITIALIZING:
         case RELOADING:
            return 4;
         case ENABLED:
            return 3;
         case INITIALIZED:
            return 2;
         case DISABLED:
            return 1;
         case UNINITIALIZED:
         case SHUTDOWN:
            return 0;
         default:
            return 0;
      }
   }
   
   /**
    * Get status color
    * @return color code
    */
   public String getColor() {
      switch (this) {
         case ENABLED:
            return "&a";
         case INITIALIZED:
            return "&e";
         case INITIALIZING:
         case RELOADING:
            return "&6";
         case DISABLED:
            return "&7";
         case ERROR:
            return "&c";
         case UNINITIALIZED:
         case SHUTDOWN:
            return "&8";
         default:
            return "&f";
      }
   }
   
   /**
    * Get status icon
    * @return icon name
    */
   public String getIcon() {
      switch (this) {
         case ENABLED:
            return "GREEN_WOOL";
         case INITIALIZED:
            return "YELLOW_WOOL";
         case INITIALIZING:
         case RELOADING:
            return "ORANGE_WOOL";
         case DISABLED:
            return "GRAY_WOOL";
         case ERROR:
            return "RED_WOOL";
         case UNINITIALIZED:
         case SHUTDOWN:
            return "BLACK_WOOL";
         default:
            return "WHITE_WOOL";
      }
   }
}
