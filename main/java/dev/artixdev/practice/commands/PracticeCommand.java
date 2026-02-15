package dev.artixdev.practice.commands;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.EventType;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Practice Command
 * Main command for the practice plugin
 */
@Register(name = "practice")
public class PracticeCommand {
   
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public PracticeCommand(Main plugin) {
      this.plugin = plugin;
   }
   
   @Command(
      name = "",
      desc = "Open practice menu"
   )
   public void openMenu(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      // Open practice menu
      player.sendMessage(ChatUtils.colorize("&b&lPractice Menu"));
      player.sendMessage(ChatUtils.colorize("&7Use &e/practice <subcommand> &7for more options"));
      player.sendMessage(ChatUtils.colorize("&3Available commands:"));
      player.sendMessage(ChatUtils.colorize("&7- &e/practice queue &7- Join a queue"));
      player.sendMessage(ChatUtils.colorize("&7- &e/practice arena &7- Manage arenas"));
      player.sendMessage(ChatUtils.colorize("&7- &e/practice kit &7- Manage kits"));
      player.sendMessage(ChatUtils.colorize("&7- &e/practice stats &7- View statistics"));
      player.sendMessage(ChatUtils.colorize("&7- &e/practice help &7- Show help"));
   }
   
   @Command(
      name = "queue",
      usage = "<type> <kit>",
      desc = "Join a practice queue"
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
         player.sendMessage(ChatUtils.colorize("&aJoined " + eventType.getDisplayName() + " queue for " + kitType.getName() + "!"));
         player.sendMessage(ChatUtils.colorize("&7Players in queue: &f" + plugin.getQueueManager().getQueueSize(eventType, kitType)));
      } else {
         player.sendMessage(ChatUtils.colorize("&cFailed to join queue!"));
      }
   }
   
   @Command(
      name = "arena",
      desc = "Manage arenas"
   )
   public void manageArenas(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      // Open arena management menu
      player.sendMessage(ChatUtils.colorize("&6&lArena Management"));
      player.sendMessage(ChatUtils.colorize("&7Available arenas: &f" + plugin.getArenaManager().getArenas().size()));
      player.sendMessage(ChatUtils.colorize("&7Use &f/practice arena <name> &7to manage specific arena"));
   }
   
   @Command(
      name = "kit",
      desc = "Manage kits"
   )
   public void manageKits(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      // Open kit management menu
      player.sendMessage(ChatUtils.colorize("&6&lKit Management"));
      player.sendMessage(ChatUtils.colorize("&7Available kits: &f" + plugin.getKitManager().getKits().size()));
      player.sendMessage(ChatUtils.colorize("&7Use &f/practice kit <name> &7to manage specific kit"));
   }
   
   @Command(
      name = "stats",
      desc = "View your statistics"
   )
   public void viewStats(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      // Get player profile
      dev.artixdev.practice.models.PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
      if (profile == null) {
         player.sendMessage(ChatUtils.colorize("&cFailed to load your profile!"));
         return;
      }
      
      // Display statistics
      player.sendMessage(ChatUtils.colorize("&6&lYour Statistics"));
      player.sendMessage(ChatUtils.colorize("&7Wins: &a" + profile.getWins()));
      player.sendMessage(ChatUtils.colorize("&7Losses: &c" + profile.getLosses()));
      player.sendMessage(ChatUtils.colorize("&7Kills: &a" + profile.getKills()));
      player.sendMessage(ChatUtils.colorize("&7Deaths: &c" + profile.getDeaths()));
      player.sendMessage(ChatUtils.colorize("&7Elo: &6" + profile.getElo()));
      player.sendMessage(ChatUtils.colorize("&7Level: &b" + profile.getLevel()));
   }
   
   @Command(
      name = "help",
      desc = "Show help information"
   )
   public void showHelp(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      player.sendMessage(ChatUtils.colorize("&6&lBolt Practice Help"));
      player.sendMessage(ChatUtils.colorize("&7Commands:"));
      player.sendMessage(ChatUtils.colorize("&7- &f/practice &7- Open practice menu"));
      player.sendMessage(ChatUtils.colorize("&7- &f/practice queue <type> <kit> &7- Join a queue"));
      player.sendMessage(ChatUtils.colorize("&7- &f/practice arena &7- Manage arenas"));
      player.sendMessage(ChatUtils.colorize("&7- &f/practice kit &7- Manage kits"));
      player.sendMessage(ChatUtils.colorize("&7- &f/practice stats &7- View statistics"));
      player.sendMessage(ChatUtils.colorize("&7- &f/practice help &7- Show this help"));
      player.sendMessage(ChatUtils.colorize("&7"));
      player.sendMessage(ChatUtils.colorize("&7Event Types:"));
      for (EventType type : EventType.values()) {
         if (type != EventType.NONE) {
            player.sendMessage(ChatUtils.colorize("&7- &f" + type.getDisplayName() + " &7- " + type.getDescription()));
         }
      }
      player.sendMessage(ChatUtils.colorize("&7"));
      player.sendMessage(ChatUtils.colorize("&7Kit Types:"));
      for (KitType type : KitType.values()) {
         if (type != KitType.CUSTOM) {
            player.sendMessage(ChatUtils.colorize("&7- &f" + type.getDisplayName() + " &7- " + type.getDescription()));
         }
      }
   }
   
   @Command(
      name = "reload",
      desc = "Reload the plugin"
   )
   public void reloadPlugin(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      // Check permission
      if (!player.hasPermission("bolt.admin")) {
         player.sendMessage(ChatUtils.colorize("&cYou don't have permission to reload the plugin!"));
         return;
      }
      
      // Reload plugin
      plugin.reloadConfig();
      plugin.onDisable();
      plugin.onEnable();
      
      player.sendMessage(ChatUtils.colorize("&aPlugin reloaded successfully!"));
   }
   
   @Command(
      name = "version",
      desc = "Show plugin version"
   )
   public void showVersion(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      player.sendMessage(ChatUtils.colorize("&6&lBolt Practice"));
      player.sendMessage(ChatUtils.colorize("&7Version: &f" + plugin.getDescription().getVersion()));
      player.sendMessage(ChatUtils.colorize("&7Author: &f" + plugin.getDescription().getAuthors()));
      player.sendMessage(ChatUtils.colorize("&7Description: &f" + plugin.getDescription().getDescription()));
   }
}