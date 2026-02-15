package dev.artixdev.practice.tasks;

import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.SettingsConfig;
import dev.artixdev.practice.managers.LeaderboardManager;

/**
 * Leaderboard Update Task
 * Task that updates leaderboards periodically
 */
public class LeaderboardUpdateTask extends Thread {
   
   private static final Logger logger = LogManager.getLogger(LeaderboardUpdateTask.class);
   
   private final LeaderboardManager leaderboardManager;
   private volatile boolean running;
   private final long updateInterval;
   
   /**
    * Constructor
    * @param leaderboardManager the leaderboard manager
    * @param updateInterval the update interval in milliseconds
    */
   public LeaderboardUpdateTask(LeaderboardManager leaderboardManager, long updateInterval) {
      this.leaderboardManager = Preconditions.checkNotNull(leaderboardManager, "LeaderboardManager cannot be null");
      this.updateInterval = updateInterval;
      this.running = true;
      
      // Set thread name
      setName("LeaderboardUpdateTask");
      
      // Set daemon thread
      setDaemon(true);
   }
   
   @Override
   public void run() {
      logger.info("Leaderboard update task started with interval: " + updateInterval + "ms");
      
      while (running) {
         try {
            // Update leaderboards
            updateLeaderboards();
            
            // Wait for next update
            Thread.sleep(updateInterval);
            
         } catch (InterruptedException e) {
            logger.info("Leaderboard update task interrupted");
            break;
         } catch (Exception e) {
            logger.error("Error in leaderboard update task", e);
            
            // Wait a bit before retrying
            try {
               Thread.sleep(5000);
            } catch (InterruptedException ie) {
               logger.info("Leaderboard update task interrupted during error recovery");
               break;
            }
         }
      }
      
      logger.info("Leaderboard update task stopped");
   }
   
   /**
    * Update leaderboards
    */
   private void updateLeaderboards() {
      try {
         // Update all leaderboards and wait for completion
         leaderboardManager.updateAllLeaderboards().get();
         
         // Log update
         logger.debug("Leaderboards updated successfully");
         
      } catch (InterruptedException e) {
         logger.warn("Leaderboard update interrupted");
         Thread.currentThread().interrupt();
      } catch (ExecutionException e) {
         logger.error("Failed to update leaderboards", e);
      } catch (Exception e) {
         logger.error("Failed to update leaderboards", e);
      }
   }
   
   /**
    * Terminate the task
    */
   public void terminate() {
      this.running = false;
      logger.info("Leaderboard update task termination requested");
      
      // Interrupt the thread
      interrupt();
   }
   
   /**
    * Check if task is running
    * @return true if running
    */
   public boolean isRunning() {
      return running;
   }
   
   /**
    * Get update interval
    * @return update interval in milliseconds
    */
   public long getUpdateInterval() {
      return updateInterval;
   }
   
   /**
    * Get update interval in seconds
    * @return update interval in seconds
    */
   public long getUpdateIntervalSeconds() {
      return updateInterval / 1000;
   }
   
   /**
    * Get update interval in minutes
    * @return update interval in minutes
    */
   public long getUpdateIntervalMinutes() {
      return updateInterval / (1000 * 60);
   }
   
   /**
    * Get task statistics
    * @return task statistics
    */
   public String getTaskStatistics() {
      return String.format("LeaderboardUpdateTask: Running=%s, Interval=%dms, Thread=%s", 
         running, updateInterval, getName());
   }
   
   /**
    * Create task with default interval
    * @param leaderboardManager the leaderboard manager
    * @return leaderboard update task
    */
   public static LeaderboardUpdateTask createDefault(LeaderboardManager leaderboardManager) {
      // Default interval: 5 minutes
      long defaultInterval = TimeUnit.MINUTES.toMillis(5);
      return new LeaderboardUpdateTask(leaderboardManager, defaultInterval);
   }
   
   /**
    * Create task with custom interval
    * @param leaderboardManager the leaderboard manager
    * @param interval the interval
    * @param unit the time unit
    * @return leaderboard update task
    */
   public static LeaderboardUpdateTask createWithInterval(LeaderboardManager leaderboardManager, long interval, TimeUnit unit) {
      long intervalMs = unit.toMillis(interval);
      return new LeaderboardUpdateTask(leaderboardManager, intervalMs);
   }
   
   @Override
   public String toString() {
      return getTaskStatistics();
   }
}
