package dev.artixdev.practice.commands;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.EventType;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.utils.Messages;

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
      player.sendMessage(Messages.get("PRACTICE.HELP_TITLE"));
      player.sendMessage(Messages.get("PRACTICE.HELP_USE"));
      player.sendMessage(Messages.get("PRACTICE.HELP_AVAILABLE"));
      player.sendMessage(Messages.get("PRACTICE.HELP_QUEUE"));
      player.sendMessage(Messages.get("PRACTICE.HELP_ARENA"));
      player.sendMessage(Messages.get("PRACTICE.HELP_KIT"));
      player.sendMessage(Messages.get("PRACTICE.HELP_STATS"));
      player.sendMessage(Messages.get("PRACTICE.HELP_HELP"));
   }
   
   @Command(
      name = "queue",
      usage = "<type> <kit>",
      desc = "Join a practice queue"
   )
   public void joinQueue(@Sender Player player, EventType eventType, KitType kitType) {
      if (player == null || eventType == null || kitType == null) {
         player.sendMessage(Messages.get("PRACTICE.INVALID_PARAMETERS"));
         return;
      }
      
      // Check if player is already in a queue
      if (plugin.getQueueManager().isPlayerInQueue(player)) {
         player.sendMessage(Messages.get("PRACTICE.ALREADY_IN_QUEUE"));
         return;
      }
      
      // Check if player is in a match
      if (plugin.getMatchManager().isPlayerInMatch(player)) {
         player.sendMessage(Messages.get("PRACTICE.CANNOT_QUEUE_IN_MATCH"));
         return;
      }
      
      // Join queue
      boolean success = plugin.getQueueManager().addPlayerToQueue(player, eventType, kitType);
      
      if (success) {
         player.sendMessage(Messages.get("PRACTICE.JOINED_QUEUE", "type", eventType.getDisplayName(), "kit", kitType.getName()));
         player.sendMessage(Messages.get("PRACTICE.PLAYERS_IN_QUEUE", "count", String.valueOf(plugin.getQueueManager().getQueueSize(eventType, kitType))));
      } else {
         player.sendMessage(Messages.get("PRACTICE.FAILED_JOIN_QUEUE"));
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
      player.sendMessage(Messages.get("PRACTICE.ARENA_MANAGEMENT_TITLE"));
      player.sendMessage(Messages.get("PRACTICE.ARENA_COUNT", "count", String.valueOf(plugin.getArenaManager().getArenas().size())));
      player.sendMessage(Messages.get("PRACTICE.ARENA_USE"));
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
      player.sendMessage(Messages.get("PRACTICE.KIT_MANAGEMENT_TITLE"));
      player.sendMessage(Messages.get("PRACTICE.KIT_COUNT", "count", String.valueOf(plugin.getKitManager().getKits().size())));
      player.sendMessage(Messages.get("PRACTICE.KIT_USE"));
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
         player.sendMessage(Messages.get("PRACTICE.PROFILE_LOAD_FAILED"));
         return;
      }
      
      // Display statistics
      player.sendMessage(Messages.get("PRACTICE.STATS_TITLE"));
      player.sendMessage(Messages.get("PRACTICE.STATS_WINS", "wins", String.valueOf(profile.getWins())));
      player.sendMessage(Messages.get("PRACTICE.STATS_LOSSES", "losses", String.valueOf(profile.getLosses())));
      player.sendMessage(Messages.get("PRACTICE.STATS_KILLS", "kills", String.valueOf(profile.getKills())));
      player.sendMessage(Messages.get("PRACTICE.STATS_DEATHS", "deaths", String.valueOf(profile.getDeaths())));
      player.sendMessage(Messages.get("PRACTICE.STATS_ELO", "elo", String.valueOf(profile.getElo())));
      player.sendMessage(Messages.get("PRACTICE.STATS_LEVEL", "level", String.valueOf(profile.getLevel())));
   }
   
   @Command(
      name = "help",
      desc = "Show help information"
   )
   public void showHelp(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      player.sendMessage(Messages.get("PRACTICE.HELP_TITLE"));
      player.sendMessage(Messages.get("PRACTICE.HELP_COMMANDS"));
      player.sendMessage(Messages.get("PRACTICE.HELP_PRACTICE"));
      player.sendMessage(Messages.get("PRACTICE.HELP_QUEUE_FULL"));
      player.sendMessage(Messages.get("PRACTICE.HELP_ARENA_CMD"));
      player.sendMessage(Messages.get("PRACTICE.HELP_KIT_CMD"));
      player.sendMessage(Messages.get("PRACTICE.HELP_STATS_CMD"));
      player.sendMessage(Messages.get("PRACTICE.HELP_HELP_CMD"));
      player.sendMessage(Messages.get("PRACTICE.HELP_BLANK"));
      player.sendMessage(Messages.get("PRACTICE.EVENT_TYPES"));
      for (EventType type : EventType.values()) {
         if (type != EventType.NONE) {
            player.sendMessage(Messages.get("PRACTICE.EVENT_TYPE_LINE", "name", type.getDisplayName(), "description", type.getDescription()));
         }
      }
      player.sendMessage(Messages.get("PRACTICE.HELP_BLANK"));
      player.sendMessage(Messages.get("PRACTICE.KIT_TYPES"));
      for (KitType type : KitType.values()) {
         if (type != KitType.CUSTOM) {
            player.sendMessage(Messages.get("PRACTICE.KIT_TYPE_LINE", "name", type.getDisplayName(), "description", type.getDescription()));
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
      if (!player.hasPermission("artix.admin")) {
         player.sendMessage(Messages.get("GENERAL.NO_PERMISSION_RELOAD"));
         return;
      }
      
      plugin.reloadConfig();
      plugin.onDisable();
      plugin.onEnable();
      
      player.sendMessage(Messages.get("GENERAL.RELOAD_SUCCESS"));
   }
   
   @Command(
      name = "version",
      desc = "Show plugin version"
   )
   public void showVersion(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      player.sendMessage(Messages.get("GENERAL.PLUGIN_INFO_TITLE"));
      player.sendMessage(Messages.get("GENERAL.PLUGIN_VERSION", "version", plugin.getDescription().getVersion()));
      player.sendMessage(Messages.get("GENERAL.PLUGIN_AUTHOR", "authors", String.valueOf(plugin.getDescription().getAuthors())));
      player.sendMessage(Messages.get("GENERAL.PLUGIN_DESCRIPTION", "description", plugin.getDescription().getDescription() != null ? plugin.getDescription().getDescription() : ""));
   }
}