package dev.artixdev.practice.commands;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Require;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.EventType;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Queue Command
 * Handles queue-related commands
 */
@Register(name = "queue")
public final class QueueCommand {
   
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public QueueCommand(Main plugin) {
      this.plugin = plugin;
   }
   
   @Command(
      name = "join",
      usage = "<type> <kit>",
      desc = "Join a specific queue"
   )
   public void joinQueue(@Sender Player player, EventType eventType, KitType kitType) {
      if (player == null || eventType == null || kitType == null) {
         player.sendMessage(ChatUtils.colorize("&cInvalid parameters!"));
         return;
      }
      
      // Check if player is already in a queue
      if (plugin.getQueueManager().isPlayerInQueue(player)) {
         player.sendMessage(ChatUtils.colorize("&cYou are already in a queue!"));
         return;
      }
      
      // Check if player is in a match
      if (plugin.getMatchManager().isPlayerInMatch(player)) {
         player.sendMessage(ChatUtils.colorize("&cYou cannot join a queue while in a match!"));
         return;
      }
      
      // Join queue
      boolean success = plugin.getQueueManager().addPlayerToQueue(player, eventType, kitType);
      
      if (success) {
         player.sendMessage(ChatUtils.colorize("&aJoined " + eventType.getDisplayName() + " queue for " + kitType.getDisplayName() + "!"));
         player.sendMessage(ChatUtils.colorize("&7Players in queue: &f" + plugin.getQueueManager().getQueueSize(eventType, kitType)));
      } else {
         player.sendMessage(ChatUtils.colorize("&cFailed to join queue!"));
      }
   }
   
   @Require("bolt.queue.admin")
   @Command(
      name = "forceQueue",
      usage = "<target> <type> <kit>",
      desc = "Force queue a target"
   )
   public void forceQueue(@Sender Player sender, Player target, EventType eventType, KitType kitType) {
      if (sender == null || target == null || eventType == null || kitType == null) {
         sender.sendMessage(ChatUtils.colorize("&cInvalid parameters!"));
         return;
      }
      
      // Check if target is already in a queue
      if (plugin.getQueueManager().isPlayerInQueue(target)) {
         sender.sendMessage(ChatUtils.colorize("&c" + target.getName() + " is already in a queue!"));
         return;
      }
      
      // Check if target is in a match
      if (plugin.getMatchManager().isPlayerInMatch(target)) {
         sender.sendMessage(ChatUtils.colorize("&c" + target.getName() + " is currently in a match!"));
         return;
      }
      
      // Force join queue
      boolean success = plugin.getQueueManager().addPlayerToQueue(target, eventType, kitType);
      
      if (success) {
         sender.sendMessage(ChatUtils.colorize("&aForced " + target.getName() + " into " + eventType.getDisplayName() + " queue for " + kitType.getDisplayName() + "!"));
         target.sendMessage(ChatUtils.colorize("&aYou were forced into " + eventType.getDisplayName() + " queue for " + kitType.getDisplayName() + "!"));
      } else {
         sender.sendMessage(ChatUtils.colorize("&cFailed to force " + target.getName() + " into queue!"));
      }
   }
   
   @Command(
      name = "leave",
      desc = "Leave your current queue"
   )
   public void leaveQueue(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      // Check if player is in a queue
      if (!plugin.getQueueManager().isPlayerInQueue(player)) {
         player.sendMessage(ChatUtils.colorize("&cYou are not in a queue!"));
         return;
      }
      
      // Leave queue
      boolean success = plugin.getQueueManager().removePlayerFromQueue(player);
      
      if (success) {
         player.sendMessage(ChatUtils.colorize("&aLeft the queue!"));
      } else {
         player.sendMessage(ChatUtils.colorize("&cFailed to leave queue!"));
      }
   }
   
   @Command(
      name = "list",
      desc = "List all available queues"
   )
   public void listQueues(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      player.sendMessage(ChatUtils.colorize("&6&lAvailable Queues:"));
      
      for (EventType eventType : EventType.values()) {
         if (eventType == EventType.NONE) continue;
         
         for (KitType kitType : KitType.values()) {
            if (kitType == KitType.CUSTOM) continue;
            
            int queueSize = plugin.getQueueManager().getQueueSize(eventType, kitType);
            String status = queueSize > 0 ? "&a" + queueSize + " players" : "&7Empty";
            
            player.sendMessage(ChatUtils.colorize("&7- " + eventType.getDisplayName() + " " + kitType.getDisplayName() + ": " + status));
         }
      }
   }
   
   @Require("bolt.queue.admin")
   @Command(
      name = "clear",
      desc = "Clear all queues"
   )
   public void clearQueues(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      int cleared = plugin.getQueueManager().clearAllQueues();
      player.sendMessage(ChatUtils.colorize("&aCleared " + cleared + " queues!"));
   }
   
   @Require("bolt.queue.admin")
   @Command(
      name = "info",
      usage = "<type> <kit>",
      desc = "Get queue information"
   )
   public void queueInfo(@Sender Player player, EventType eventType, KitType kitType) {
      if (player == null || eventType == null || kitType == null) {
         player.sendMessage(ChatUtils.colorize("&cInvalid parameters!"));
         return;
      }
      
      int queueSize = plugin.getQueueManager().getQueueSize(eventType, kitType);
      boolean isActive = plugin.getQueueManager().isQueueActive(eventType, kitType);
      
      player.sendMessage(ChatUtils.colorize("&6&lQueue Information:"));
      player.sendMessage(ChatUtils.colorize("&7Type: &f" + eventType.getDisplayName()));
      player.sendMessage(ChatUtils.colorize("&7Kit: &f" + kitType.getDisplayName()));
      player.sendMessage(ChatUtils.colorize("&7Players: &f" + queueSize));
      player.sendMessage(ChatUtils.colorize("&7Active: " + (isActive ? "&aYes" : "&cNo")));
   }
}
