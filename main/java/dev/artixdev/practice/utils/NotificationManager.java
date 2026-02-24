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
   
   private static String getPrefix() { return Messages.get("NOTIFICATIONS.PREFIX"); }
   private static String getErrorPrefix() { return Messages.get("NOTIFICATIONS.ERROR_PREFIX"); }
   private static String getSuccessPrefix() { return Messages.get("NOTIFICATIONS.SUCCESS_PREFIX"); }
   private static String getWarningPrefix() { return Messages.get("NOTIFICATIONS.WARNING_PREFIX"); }
   
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
      
      player.sendMessage(getPrefix() + ChatUtils.colorize(message));
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
      
      player.sendMessage(getErrorPrefix() + ChatUtils.colorize(message));
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
      
      player.sendMessage(getSuccessPrefix() + ChatUtils.colorize(message));
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
      
      player.sendMessage(getWarningPrefix() + ChatUtils.colorize(message));
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
      
      player.sendMessage(Messages.get("NOTIFICATIONS.LIST_TITLE"));
      for (String notification : notifications) {
         player.sendMessage(Messages.get("NOTIFICATIONS.LIST_LINE", "message", notification));
      }
      
      clearNotifications(player);
   }
   
   /**
    * Send action bar message to player
    * @param player the player
    * @param message the message
    */
   @SuppressWarnings("unchecked")
   public void sendActionBar(Player player, String message) {
      if (player == null || message == null) return;
      String text = ChatUtils.colorize(message);
      try {
         Object spigotInstance = Player.class.getMethod("spigot").invoke(player);
         Class<?> chatMessageType = Class.forName("net.md_5.bungee.api.ChatMessageType");
         Object actionBar = java.lang.Enum.valueOf((Class<Enum>) chatMessageType, "ACTION_BAR");
         Class<?> textComponent = Class.forName("net.md_5.bungee.api.chat.TextComponent");
         Object[] comps = (Object[]) textComponent.getMethod("fromLegacyText", String.class).invoke(null, text);
         spigotInstance.getClass().getMethod("sendMessage", chatMessageType, comps.getClass()).invoke(spigotInstance, actionBar, comps);
      } catch (Throwable t) {
         player.sendMessage(Messages.get("NOTIFICATIONS.ACTION_BAR_FORMAT", "message", text));
      }
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
      if (player == null) return;
      String t = title != null ? ChatUtils.colorize(title) : "";
      String s = subtitle != null ? ChatUtils.colorize(subtitle) : "";
      try {
         java.lang.reflect.Method m = Player.class.getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
         m.invoke(player, t, s, fadeIn, stay, fadeOut);
      } catch (Throwable e) {
         player.sendMessage(Messages.get("NOTIFICATIONS.TITLE_FORMAT", "title", t));
         if (!s.isEmpty()) player.sendMessage(Messages.get("NOTIFICATIONS.SUBTITLE_FORMAT", "subtitle", s));
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
      
      String fullMessage = (prefix != null ? prefix : getPrefix()) + ChatUtils.colorize(message);
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
