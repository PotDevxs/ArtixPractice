package dev.artixdev.practice.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.Main;

/**
 * Abstract Task
 * Abstract class for scheduled tasks
 */
public abstract class AbstractTask implements Runnable {
   
   private BukkitTask task;
   private final Main plugin;
   private int ticks;
   private boolean running;
   private boolean cancelled;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public AbstractTask(Main plugin) {
      this.plugin = plugin;
      this.ticks = 0;
      this.running = false;
      this.cancelled = false;
   }
   
   /**
    * Get ticks
    * @return ticks
    */
   public int getTicks() {
      return this.ticks;
   }
   
   /**
    * Set ticks
    * @param ticks the ticks
    */
   public void setTicks(int ticks) {
      this.ticks = ticks;
   }
   
   /**
    * Increment ticks
    */
   public void incrementTicks() {
      this.ticks++;
   }
   
   /**
    * Reset ticks
    */
   public void resetTicks() {
      this.ticks = 0;
   }
   
   /**
    * Check if task is running
    * @return true if running
    */
   public boolean isRunning() {
      return this.running;
   }
   
   /**
    * Set running status
    * @param running the running status
    */
   public void setRunning(boolean running) {
      this.running = running;
   }
   
   /**
    * Start task
    * @param delay the delay in ticks
    * @param period the period in ticks
    */
   public void startTask(long delay, long period) {
      if (isRunning()) {
         stopTask();
      }
      
      this.cancelled = false;
      this.task = Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period);
      this.running = true;
   }
   
   /**
    * Start task
    * @param period the period in ticks
    */
   public void startTask(long period) {
      startTask(0L, period);
   }
   
   /**
    * Start task
    */
   public void startTask() {
      startTask(20L); // 1 second default
   }
   
   /**
    * Stop task
    */
   public void stopTask() {
      if (this.task != null) {
         this.task.cancel();
         this.cancelled = true;
         this.task = null;
      }
      this.running = false;
   }
   
   /**
    * Restart task
    * @param delay the delay in ticks
    * @param period the period in ticks
    */
   public void restartTask(long delay, long period) {
      stopTask();
      startTask(delay, period);
   }
   
   /**
    * Restart task
    * @param period the period in ticks
    */
   public void restartTask(long period) {
      restartTask(0L, period);
   }
   
   /**
    * Restart task
    */
   public void restartTask() {
      restartTask(20L); // 1 second default
   }
   
   /**
    * Check if task is cancelled
    * @return true if cancelled
    */
   public boolean isCancelled() {
      if (this.task == null || cancelled) {
         return true;
      }
      int taskId = this.task.getTaskId();
      return !Bukkit.getScheduler().isQueued(taskId) && !Bukkit.getScheduler().isCurrentlyRunning(taskId);
   }
   
   /**
    * Get task ID
    * @return task ID or -1 if not running
    */
   public int getTaskId() {
      return this.task != null ? this.task.getTaskId() : -1;
   }
   
   /**
    * Get plugin
    * @return plugin instance
    */
   public Main getPlugin() {
      return this.plugin;
   }
   
   /**
    * Run task
    */
   @Override
   public void run() {
      try {
         // Check if task should continue running
         if (!isRunning() || isCancelled()) {
            return;
         }
         
         // Increment ticks
         incrementTicks();
         
         // Execute task logic
         execute();
         
      } catch (Exception e) {
         plugin.getLogger().severe("Error in task execution: " + e.getMessage());
         e.printStackTrace();
         
         // Stop task on error
         stopTask();
      }
   }
   
   /**
    * Execute task logic
    * This method should be implemented by subclasses
    */
   protected abstract void execute();
   
   /**
    * On task start
    * This method is called when the task starts
    */
   protected void onStart() {
      // Override in subclasses if needed
   }
   
   /**
    * On task stop
    * This method is called when the task stops
    */
   protected void onStop() {
      // Override in subclasses if needed
   }
   
   /**
    * On task error
    * This method is called when an error occurs
    * @param error the error
    */
   protected void onError(Exception error) {
      // Override in subclasses if needed
      plugin.getLogger().warning("Task error: " + error.getMessage());
   }
   
   /**
    * Check if task should continue
    * @return true if should continue
    */
   protected boolean shouldContinue() {
      return isRunning() && !isCancelled();
   }
   
   /**
    * Get task name
    * @return task name
    */
   public String getTaskName() {
      return this.getClass().getSimpleName();
   }
   
   /**
    * Get task description
    * @return task description
    */
   public String getTaskDescription() {
      return "Abstract task implementation";
   }
   
   /**
    * Get task status
    * @return task status
    */
   public String getTaskStatus() {
      if (isRunning()) {
         return "Running";
      } else if (isCancelled()) {
         return "Cancelled";
      } else {
         return "Stopped";
      }
   }
   
   /**
    * Get task info
    * @return task info
    */
   public String getTaskInfo() {
      return String.format("Task: %s, Status: %s, Ticks: %d, ID: %d", 
         getTaskName(), getTaskStatus(), getTicks(), getTaskId());
   }
   
   /**
    * Log task info
    */
   public void logTaskInfo() {
      plugin.getLogger().info(getTaskInfo());
   }
   
   /**
    * Check if task is valid
    * @return true if valid
    */
   public boolean isValid() {
      return plugin != null && plugin.isEnabled();
   }
   
   /**
    * Validate task
    * @return true if valid
    */
   public boolean validate() {
      if (!isValid()) {
         plugin.getLogger().warning("Task validation failed: " + getTaskName());
         return false;
      }
      return true;
   }
   
   /**
    * Initialize task
    * This method is called when the task is initialized
    */
   public void initialize() {
      // Override in subclasses if needed
   }
   
   /**
    * Cleanup task
    * This method is called when the task is cleaned up
    */
   public void cleanup() {
      // Override in subclasses if needed
      stopTask();
   }
   
   /**
    * Get task priority
    * @return task priority
    */
   public int getPriority() {
      return 0; // Default priority
   }
   
   /**
    * Set task priority
    * @param priority the priority
    */
   public void setPriority(int priority) {
      // Override in subclasses if needed
   }
   
   /**
    * Check if task is high priority
    * @return true if high priority
    */
   public boolean isHighPriority() {
      return getPriority() > 5;
   }
   
   /**
    * Check if task is low priority
    * @return true if low priority
    */
   public boolean isLowPriority() {
      return getPriority() < 5;
   }
   
   /**
    * Get task execution time
    * @return execution time in milliseconds
    */
   public long getExecutionTime() {
      return System.currentTimeMillis() - (ticks * 50); // Approximate
   }
   
   /**
    * Get task performance
    * @return task performance info
    */
   public String getPerformance() {
      return String.format("Ticks: %d, Execution Time: %dms", 
         getTicks(), getExecutionTime());
   }
}
