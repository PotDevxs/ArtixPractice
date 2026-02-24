package dev.artixdev.practice.tasks;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.SettingsConfig;

/**
 * Announcement Task
 * Task for broadcasting announcements
 */
public class AnnouncementTask implements Runnable {
   
   private static final String[] ANNOUNCEMENT_MESSAGES = {
      "Welcome to Practice!",
      "Join our Discord server!",
      "Follow us on social media!",
      "Check out our website!",
      "Donate to support the server!",
      "Vote for us daily!",
      "Report bugs on our Discord!",
      "Suggest features on our Discord!",
      "Join our community!",
      "Have fun playing!"
   };
   
   private int countdown;
   private BukkitTask task;
   private boolean cancelled;
   
   /**
    * Constructor
    */
   public AnnouncementTask() {
      super();
      this.countdown = 60; // 60 seconds
   }
   
   /**
    * Start the task
    * @param plugin the plugin instance
    * @param delay the delay in ticks
    * @param period the period in ticks
    * @return the task
    */
   public BukkitTask startTask(Main plugin, long delay, long period) {
      cancelled = false;
      BukkitScheduler scheduler = Bukkit.getScheduler();
      this.task = scheduler.runTaskTimer(plugin, this, delay, period);
      return this.task;
   }
   
   @Override
   public void run() {
      // Get announcement manager
      dev.artixdev.practice.managers.AnnouncementManager announcementManager = 
         Main.getInstance().getAnnouncementManager();
      
      if (announcementManager == null) {
         // Cancel task if no announcement manager
         if (this.task != null) {
            this.task.cancel();
         }
         return;
      }
      
      // Get current announcement
      dev.artixdev.practice.models.Announcement currentAnnouncement = announcementManager.getCurrentAnnouncement();
      
      if (currentAnnouncement == null) {
         // Cancel task if no current announcement
         if (this.task != null) {
            this.task.cancel();
         }
         return;
      }
      
      // Handle countdown
      if (this.countdown > 0) {
         // Handle different countdown intervals
         if (this.countdown != 60) {
            if (this.countdown == 30) {
               // 30 second warning
               handleCountdownWarning(30);
            } else if (this.countdown == 10) {
               // 10 second warning
               handleCountdownWarning(10);
            } else if (this.countdown <= 5) {
               // Final countdown
               handleFinalCountdown();
            }
         }
         
         // Decrement countdown
         this.countdown--;
      } else {
         // Time to broadcast announcement
         broadcastAnnouncement(currentAnnouncement);
         
         // Reset countdown
         this.countdown = 60;
      }
   }
   
   /**
    * Handle countdown warning
    * @param seconds the seconds remaining
    */
   private void handleCountdownWarning(int seconds) {
      // Get online players
      Collection<? extends org.bukkit.entity.Player> onlinePlayers = Bukkit.getOnlinePlayers();
      
      if (onlinePlayers.size() >= 2) {
         // Broadcast countdown message
         String message = "§cAnnouncement in " + seconds + " seconds!";
         Bukkit.broadcastMessage(message);
      }
   }
   
   /**
    * Handle final countdown
    */
   private void handleFinalCountdown() {
      // Get online players
      Collection<? extends org.bukkit.entity.Player> onlinePlayers = Bukkit.getOnlinePlayers();
      
      if (onlinePlayers.size() >= 2) {
         // Broadcast final countdown
         String message = "§cAnnouncement in " + this.countdown + " seconds!";
         Bukkit.broadcastMessage(message);
      }
   }
   
   /**
    * Broadcast announcement
    * @param announcement the announcement
    */
   private void broadcastAnnouncement(dev.artixdev.practice.models.Announcement announcement) {
      // Get announcement messages
      List<String> messages = announcement.getMessages();
      
      if (messages == null || messages.isEmpty()) {
         return;
      }
      
      // Process messages
      Stream<String> messageStream = messages.stream();
      
      messageStream.map(this::processMessage).forEach(message -> {
         if (message != null && !message.isEmpty()) {
            if (message.contains("§")) {
               // Send as TextComponent for color support
               sendTextComponentMessage(message);
            } else {
               // Send as regular message
               Bukkit.broadcastMessage(message);
            }
         }
      });
   }
   
   /**
    * Process message
    * @param message the message
    * @return processed message
    */
   private String processMessage(String message) {
      if (message == null || message.isEmpty()) {
         return null;
      }
      
      // Replace placeholders
      String processedMessage = message
         .replace("{server}", Bukkit.getServerName())
         .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
         .replace("{max}", String.valueOf(Bukkit.getMaxPlayers()));
      
      return processedMessage;
   }
   
   /**
    * Send TextComponent message
    * @param message the message
    */
   private void sendTextComponentMessage(String message) {
      // Broadcast with legacy color codes; TextComponentBuilder could be added later for click/hover
      Bukkit.broadcastMessage(message);
   }
   
   /**
    * Cancel the task
    */
   public void cancel() {
      if (this.task != null) {
         this.task.cancel();
         cancelled = true;
         this.task = null;
      }
   }

   /**
    * Check if task is running
    * @return true if running
    */
   public boolean isRunning() {
      if (this.task == null || cancelled) {
         return false;
      }
      int taskId = this.task.getTaskId();
      return Bukkit.getScheduler().isQueued(taskId) || Bukkit.getScheduler().isCurrentlyRunning(taskId);
   }
   
   /**
    * Get countdown
    * @return countdown
    */
   public int getCountdown() {
      return countdown;
   }
   
   /**
    * Set countdown
    * @param countdown the countdown
    */
   public void setCountdown(int countdown) {
      this.countdown = countdown;
   }
}
