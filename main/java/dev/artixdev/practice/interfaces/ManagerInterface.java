package dev.artixdev.practice.interfaces;

/**
 * Manager Interface
 * Base interface for all managers in the practice plugin
 */
public interface ManagerInterface {
   
   /**
    * Initialize the manager
    */
   void initialize();
   
   /**
    * Shutdown the manager
    */
   void shutdown();
   
   /**
    * Check if manager is initialized
    * @return true if initialized
    */
   boolean isInitialized();
   
   /**
    * Get manager name
    * @return manager name
    */
   String getManagerName();
   
   /**
    * Get manager version
    * @return manager version
    */
   String getManagerVersion();
   
   /**
    * Get manager description
    * @return manager description
    */
   String getManagerDescription();
   
   /**
    * Check if manager is enabled
    * @return true if enabled
    */
   boolean isEnabled();
   
   /**
    * Enable the manager
    */
   void enable();
   
   /**
    * Disable the manager
    */
   void disable();
   
   /**
    * Reload the manager
    */
   void reload();
   
   /**
    * Get manager status
    * @return status string
    */
   String getStatus();
   
   /**
    * Get manager statistics
    * @return statistics string
    */
   String getStatistics();
}
