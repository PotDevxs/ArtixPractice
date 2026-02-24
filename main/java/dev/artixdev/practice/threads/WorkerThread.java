package dev.artixdev.practice.threads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Worker Thread
 * Custom thread for background tasks
 */
public class WorkerThread extends Thread {
   
   private static final Logger logger = LogManager.getLogger(WorkerThread.class);
   private volatile boolean running;
   
   /**
    * Constructor
    */
   public WorkerThread() {
      super("Practice-Worker-Thread");
      this.running = true;
   }
   
   @Override
   public void run() {
      logger.info("Worker thread started");
      
      while (this.running) {
         try {
            // Perform background tasks
            this.tick();
            
            // Sleep for a short time to prevent excessive CPU usage
            Thread.sleep(50); // 50ms
         } catch (InterruptedException e) {
            logger.warn("Worker thread interrupted", e);
            break;
         } catch (Exception e) {
            logger.error("Error in worker thread", e);
         }
      }
      
      logger.info("Worker thread stopped");
   }
   
   /**
    * Perform tick operations
    */
   private void tick() {
      try {
         // Example: Process queued operations
         processQueuedOperations();
         
         // Example: Update statistics
         updateStatistics();
         
         // Example: Clean up resources
         cleanupResources();
         
      } catch (Exception e) {
         logger.error("Error during tick", e);
      }
   }
   
   /**
    * Process queued operations
    */
   private void processQueuedOperations() {
      // No queue configured; override or extend to process pending operations
   }
   
   /**
    * Update statistics
    */
   private void updateStatistics() {
      // Update various statistics
      // This could include updating player statistics, server statistics, etc.
   }
   
   /**
    * Clean up resources
    */
   private void cleanupResources() {
      // Clean up any resources that are no longer needed
      // This could include cleaning up expired data, unused objects, etc.
   }
   
   /**
    * Terminate the thread
    */
   public void terminate() {
      this.running = false;
      logger.info("Worker thread termination requested");
   }
   
   /**
    * Check if thread is running
    * @return true if running
    */
   public boolean isRunning() {
      return this.running;
   }
   
   /**
    * Get thread name
    * @return thread name
    */
   public String getThreadName() {
      return this.getName();
   }
   
   /**
    * Set thread name
    * @param name the name
    */
   public void setThreadName(String name) {
      this.setName(name);
   }
}
