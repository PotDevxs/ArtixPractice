package dev.artixdev.practice.managers;

/**
 * Base Cooldown Manager
 * Base class for cooldown management
 */
public abstract class BaseCooldownManager {
   
   protected final long defaultDuration;
   protected final String name;
   
   /**
    * Constructor
    * @param name the cooldown name
    * @param defaultDuration the default duration in milliseconds
    */
   public BaseCooldownManager(String name, long defaultDuration) {
      this.name = name;
      this.defaultDuration = defaultDuration;
   }
   
   /**
    * Get cooldown name
    * @return cooldown name
    */
   public String getName() {
      return name;
   }
   
   /**
    * Get default duration
    * @return default duration in milliseconds
    */
   public long getDefaultDuration() {
      return defaultDuration;
   }
   
   /**
    * Get default duration in seconds
    * @return default duration in seconds
    */
   public long getDefaultDurationSeconds() {
      return defaultDuration / 1000;
   }
   
   /**
    * Get default duration in minutes
    * @return default duration in minutes
    */
   public long getDefaultDurationMinutes() {
      return defaultDuration / (1000 * 60);
   }
   
   /**
    * Get default duration in hours
    * @return default duration in hours
    */
   public long getDefaultDurationHours() {
      return defaultDuration / (1000 * 60 * 60);
   }
   
   /**
    * Get default duration in days
    * @return default duration in days
    */
   public long getDefaultDurationDays() {
      return defaultDuration / (1000 * 60 * 60 * 24);
   }
   
   /**
    * Format duration to human readable string
    * @param duration the duration in milliseconds
    * @return formatted string
    */
   public String formatDuration(long duration) {
      if (duration < 1000) {
         return duration + "ms";
      } else if (duration < 60000) {
         return (duration / 1000) + "s";
      } else if (duration < 3600000) {
         return (duration / 60000) + "m " + ((duration % 60000) / 1000) + "s";
      } else if (duration < 86400000) {
         return (duration / 3600000) + "h " + ((duration % 3600000) / 60000) + "m";
      } else {
         return (duration / 86400000) + "d " + ((duration % 86400000) / 3600000) + "h";
      }
   }
   
   /**
    * Get formatted default duration
    * @return formatted default duration
    */
   public String getFormattedDefaultDuration() {
      return formatDuration(defaultDuration);
   }
   
   @Override
   public String toString() {
      return String.format("BaseCooldownManager{name='%s', defaultDuration=%dms}", name, defaultDuration);
   }
}
