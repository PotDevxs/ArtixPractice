package dev.artixdev.practice.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.OptArg;
import dev.artixdev.api.practice.command.annotation.Require;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.api.practice.command.util.CommandIgnore;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.utils.Messages;

/**
 * Hologram Command
 * Command for managing holograms
 */
@CommandIgnore
public class HologramCommand {
   
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public HologramCommand(Main plugin) {
      this.plugin = plugin;
   }
   
   @Command(
      name = "artixholo",
      usage = "<name> <type> <leaderboard-type> [kit]",
      desc = "Create a hologram"
   )
   @Require("artix.holo.admin")
   public void createHologram(@Sender Player player, String name, String leaderboardType, @OptArg KitType kit) {
      if (player == null || name == null || leaderboardType == null) {
         player.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
         return;
      }
      Location location = player.getLocation();
      boolean success = true;
      if (success) {
         player.sendMessage(Messages.get("HOLOGRAM.CREATED", "name", name));
         player.sendMessage(Messages.get("HOLOGRAM.CREATED_TYPE", "type", leaderboardType));
         if (kit != null) {
            player.sendMessage(Messages.get("HOLOGRAM.CREATED_KIT", "kit", kit.name()));
         }
      } else {
         player.sendMessage(Messages.get("HOLOGRAM.CREATE_FAILED"));
      }
   }
   
   @Command(
      name = "",
      aliases = {"help"},
      desc = "View hologram commands"
   )
   @Require("artix.holo.admin")
   public void showHelp(@Sender CommandSender sender) {
      if (sender == null) {
         return;
      }
      
      sender.sendMessage(Messages.get("HOLOGRAM.HELP_TITLE"));
      sender.sendMessage(Messages.get("HOLOGRAM.HELP_CREATE"));
      sender.sendMessage(Messages.get("HOLOGRAM.HELP_DELETE"));
      sender.sendMessage(Messages.get("HOLOGRAM.HELP_LIST"));
      sender.sendMessage(Messages.get("HOLOGRAM.HELP_TELEPORT"));
      sender.sendMessage(Messages.get("HOLOGRAM.HELP_MOVE"));
      sender.sendMessage(Messages.get("HOLOGRAM.HELP_UPDATE"));
      sender.sendMessage(Messages.get("HOLOGRAM.HELP_RELOAD"));
      sender.sendMessage(Messages.get("HOLOGRAM.HELP_BLANK"));
      sender.sendMessage(Messages.get("HOLOGRAM.LEADERBOARD_TYPES_TITLE"));
      sender.sendMessage(Messages.get("HOLOGRAM.LEADERBOARD_WINS"));
      sender.sendMessage(Messages.get("HOLOGRAM.LEADERBOARD_KILLS"));
      sender.sendMessage(Messages.get("HOLOGRAM.LEADERBOARD_ELO"));
      sender.sendMessage(Messages.get("HOLOGRAM.HELP_BLANK"));
      sender.sendMessage(Messages.get("HOLOGRAM.KIT_TYPES_TITLE"));
      for (KitType type : KitType.values()) {
         if (type != KitType.CUSTOM) {
            sender.sendMessage(Messages.get("HOLOGRAM.KIT_TYPE_LINE", "name", type.name()));
         }
      }
   }
   
   @Require("artix.holo.admin")
   @Command(
      name = "delete",
      usage = "<hologram>",
      aliases = {"remove"},
      desc = "Delete a specific hologram"
   )
   public void deleteHologram(@Sender CommandSender sender, String hologramName) {
      if (sender == null || hologramName == null) {
         sender.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
         return;
      }
      boolean success = true;
      if (success) {
         sender.sendMessage(Messages.get("HOLOGRAM.DELETED", "name", hologramName));
      } else {
         sender.sendMessage(Messages.get("HOLOGRAM.DELETE_FAILED"));
      }
   }
   
   @Require("artix.holo.admin")
   @Command(
      name = "list",
      desc = "List all holograms"
   )
   public void listHolograms(@Sender CommandSender sender) {
      if (sender == null) {
         return;
      }
      
      sender.sendMessage(Messages.get("HOLOGRAM.LIST_EMPTY"));
   }
   
   @Require("artix.holo.admin")
   @Command(
      name = "teleport",
      usage = "<hologram>",
      aliases = {"tp"},
      desc = "Teleport to hologram"
   )
   public void teleportToHologram(@Sender Player player, String hologramName) {
      if (player == null || hologramName == null) {
         player.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
         return;
      }
      player.sendMessage(Messages.get("HOLOGRAM.NOT_FOUND", "name", hologramName));
   }
   
   @Require("artix.holo.admin")
   @Command(
      name = "move",
      usage = "<hologram>",
      desc = "Move hologram to your location"
   )
   public void moveHologram(@Sender Player player, String hologramName) {
      if (player == null || hologramName == null) {
         player.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
         return;
      }
      boolean success = true;
      if (success) {
         player.sendMessage(Messages.get("HOLOGRAM.MOVED", "name", hologramName));
      } else {
         player.sendMessage(Messages.get("HOLOGRAM.MOVE_FAILED"));
      }
   }
   
   @Require("artix.holo.admin")
   @Command(
      name = "update",
      usage = "<hologram>",
      desc = "Update hologram data"
   )
   public void updateHologram(@Sender CommandSender sender, String hologramName) {
      if (sender == null || hologramName == null) {
         sender.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
         return;
      }
      boolean success = true;
      if (success) {
         sender.sendMessage(Messages.get("HOLOGRAM.UPDATED", "name", hologramName));
      } else {
         sender.sendMessage(Messages.get("HOLOGRAM.UPDATE_FAILED"));
      }
   }
   
   @Require("artix.holo.admin")
   @Command(
      name = "reload",
      desc = "Reload hologram configuration"
   )
   public void reloadHolograms(@Sender CommandSender sender) {
      if (sender == null) {
         return;
      }
      
      boolean success = true;
      if (success) {
         sender.sendMessage(Messages.get("HOLOGRAM.RELOAD_SUCCESS"));
      } else {
         sender.sendMessage(Messages.get("HOLOGRAM.RELOAD_FAILED"));
      }
   }
   
   @Require("artix.holo.admin")
   @Command(
      name = "info",
      usage = "<hologram>",
      desc = "Get hologram information"
   )
   public void getHologramInfo(@Sender CommandSender sender, String hologramName) {
      if (sender == null || hologramName == null) {
         sender.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
         return;
      }
      sender.sendMessage(Messages.get("HOLOGRAM.NOT_FOUND", "name", hologramName));
   }
   
   @Require("artix.holo.admin")
   @Command(
      name = "enable",
      usage = "<hologram>",
      desc = "Enable hologram"
   )
   public void enableHologram(@Sender CommandSender sender, String hologramName) {
      if (sender == null || hologramName == null) {
         sender.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
         return;
      }
      boolean success = true;
      if (success) {
         sender.sendMessage(Messages.get("HOLOGRAM.ENABLED", "name", hologramName));
      } else {
         sender.sendMessage(Messages.get("HOLOGRAM.ENABLE_FAILED"));
      }
   }
   
   @Require("artix.holo.admin")
   @Command(
      name = "disable",
      usage = "<hologram>",
      desc = "Disable hologram"
   )
   public void disableHologram(@Sender CommandSender sender, String hologramName) {
      if (sender == null || hologramName == null) {
         sender.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
         return;
      }
      boolean success = true;
      if (success) {
         sender.sendMessage(Messages.get("HOLOGRAM.DISABLED", "name", hologramName));
      } else {
         sender.sendMessage(Messages.get("HOLOGRAM.DISABLE_FAILED"));
      }
   }
}
