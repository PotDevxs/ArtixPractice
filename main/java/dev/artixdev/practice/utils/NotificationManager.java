package dev.artixdev.practice.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * Notification Manager
 * Manages notifications and messages for players
 */
public class NotificationManager {
   
   private static final String PREFIX = ChatUtils.colorize("&6&lBolt &8» &7");
   private static final String ERROR_PREFIX = ChatUtils.colorize("&c&lError &8» &7");
   private static final String SUCCESS_PREFIX = ChatUtils.colorize("&a&lSuccess &8» &7");
   private static final String WARNING_PREFIX = ChatUtils.colorize("&e&lWarning &8» &7");
   
   private final Main plugin;
   private final Map<UUID, List<String>> playerNotifications;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public NotificationManager(Main plugin) {
      this.plugin = plugin;
      this.playerNotifications = new HashMap<>();
   }
   
   /**
    * Send message to player
    * @param player the player
    * @param message the message
    */
   public void sendMessage(Player player, String message) {
      if (player == null || message == null) {
         return;
      }
      
      player.sendMessage(PREFIX + ChatUtils.colorize(message));
   }
   
   /**
    * Send error message to player
    * @param player the player
    * @param message the error message
    */
   public void sendError(Player player, String message) {
      if (player == null || message == null) {
         return;
      }
      
      player.sendMessage(ERROR_PREFIX + ChatUtils.colorize(message));
   }
   
   /**
    * Send success message to player
    * @param player the player
    * @param message the success message
    */
   public void sendSuccess(Player player, String message) {
      if (player == null || message == null) {
         return;
      }
      
      player.sendMessage(SUCCESS_PREFIX + ChatUtils.colorize(message));
   }
   
   /**
    * Send warning message to player
    * @param player the player
    * @param message the warning message
    */
   public void sendWarning(Player player, String message) {
      if (player == null || message == null) {
         return;
      }
      
      player.sendMessage(WARNING_PREFIX + ChatUtils.colorize(message));
   }
   
   /**
    * Send message to multiple players
    * @param players the players
    * @param message the message
    */
   public void sendMessageToPlayers(List<Player> players, String message) {
      if (players == null || players.isEmpty() || message == null) {
         return;
      }
      
      for (Player player : players) {
         sendMessage(player, message);
      }
   }
   
   /**
    * Send message to all online players
    * @param message the message
    */
   public void sendMessageToAll(String message) {
      if (message == null) {
         return;
      }
      
      for (Player player : plugin.getServer().getOnlinePlayers()) {
         sendMessage(player, message);
      }
   }
   
   /**
    * Add notification to player
    * @param player the player
    * @param notification the notification
    */
   public void addNotification(Player player, String notification) {
      if (player == null || notification == null) {
         return;
      }
      
      UUID playerId = player.getUniqueId();
      List<String> notifications = playerNotifications.computeIfAbsent(playerId, k -> new ArrayList<>());
      notifications.add(notification);
   }
   
   /**
    * Get player notifications
    * @param player the player
    * @return list of notifications
    */
   public List<String> getNotifications(Player player) {
      if (player == null) {
         return new ArrayList<>();
      }
      
      return playerNotifications.getOrDefault(player.getUniqueId(), new ArrayList<>());
   }
   
   /**
    * Clear player notifications
    * @param player the player
    */
   public void clearNotifications(Player player) {
      if (player == null) {
         return;
      }
      
      playerNotifications.remove(player.getUniqueId());
   }
   
   /**
    * Send notifications to player
    * @param player the player
    */
   public void sendNotifications(Player player) {
      if (player == null) {
         return;
      }
      
      List<String> notifications = getNotifications(player);
      if (notifications.isEmpty()) {
         return;
      }
      
      player.sendMessage(ChatUtils.colorize("&6&lNotifications:"));
      for (String notification : notifications) {
         player.sendMessage(ChatUtils.colorize("&7- " + notification));
      }
      
      clearNotifications(player);
   }
   
   /**
    * Send action bar message to player
    * @param player the player
    * @param message the message
    */
   public void sendActionBar(Player player, String message) {
      if (player == null || message == null) {
         return;
      }
      
      // TODO: Implement action bar sending
      // This would require NMS or a library like Adventure API
      player.sendMessage(ChatUtils.colorize("&6&lAction Bar: &7" + message));
   }
   
   /**
    * Send title to player
    * @param player the player
    * @param title the title
    * @param subtitle the subtitle
    * @param fadeIn fade in time
    * @param stay stay time
    * @param fadeOut fade out time
    */
   public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      if (player == null) {
         return;
      }
      
      // TODO: Implement title sending
      // This would require NMS or a library like Adventure API
      player.sendMessage(ChatUtils.colorize("&6&lTitle: &7" + title));
      if (subtitle != null) {
         player.sendMessage(ChatUtils.colorize("&7" + subtitle));
      }
   }
   
   /**
    * Send title with default timing
    * @param player the player
    * @param title the title
    * @param subtitle the subtitle
    */
   public void sendTitle(Player player, String title, String subtitle) {
      sendTitle(player, title, subtitle, 10, 70, 20);
   }
   
   /**
    * Send title with only title
    * @param player the player
    * @param title the title
    */
   public void sendTitle(Player player, String title) {
      sendTitle(player, title, null);
   }
   
   /**
    * Send message with prefix
    * @param player the player
    * @param prefix the prefix
    * @param message the message
    */
   public void sendMessageWithPrefix(Player player, String prefix, String message) {
      if (player == null || message == null) {
         return;
      }
      
      String fullMessage = (prefix != null ? prefix : PREFIX) + ChatUtils.colorize(message);
      player.sendMessage(fullMessage);
   }
   
   /**
    * Send message without prefix
    * @param player the player
    * @param message the message
    */
   public void sendRawMessage(Player player, String message) {
      if (player == null || message == null) {
         return;
      }
      
      player.sendMessage(ChatUtils.colorize(message));
   }
   
   /**
    * Check if player has notifications
    * @param player the player
    * @return true if has notifications
    */
   public boolean hasNotifications(Player player) {
      if (player == null) {
         return false;
      }
      
      List<String> notifications = getNotifications(player);
      return !notifications.isEmpty();
   }
   
   /**
    * Get notification count for player
    * @param player the player
    * @return notification count
    */
   public int getNotificationCount(Player player) {
      if (player == null) {
         return 0;
      }
      
      List<String> notifications = getNotifications(player);
      return notifications.size();
   }
   
   /**
    * Cleanup player data
    * @param player the player
    */
   public void cleanupPlayer(Player player) {
      if (player == null) {
         return;
      }
      
      clearNotifications(player);
   }
}
