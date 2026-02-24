package dev.artixdev.practice.commands;

import java.util.UUID;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.NotificationManager;

/**
 * Post Match Command
 * Command to open post-match inventory for a specific player
 */
@Register(
   name = "postmatch",
   aliases = {"matchinv"}
)
public final class PostMatchCommand {
   
   private final Main plugin;
   private final NotificationManager notificationManager;
   
   /**
    * Constructor
    */
   public PostMatchCommand() {
      this.plugin = Main.getInstance();
      this.notificationManager = new NotificationManager(plugin);
   }
   
   /**
    * Open post-match inventory for a specific player
    * @param player the player executing the command
    * @param targetUUID the target player's UUID
    */
   @Command(
      name = "",
      usage = "<uuid>",
      aliases = {"help"},
      desc = "Open post-match inventory for a specific player"
   )
   public void openPostMatchInventory(@Sender Player player, UUID targetUUID) {
      if (targetUUID == null) {
         notificationManager.sendError(player, "Invalid player UUID provided.");
         return;
      }
      
      // Check if target player exists
      Player targetPlayer = plugin.getServer().getPlayer(targetUUID);
      if (targetPlayer == null) {
         notificationManager.sendError(player, "Target player is not online.");
         return;
      }
      
      // Check if player has permission
      if (!player.hasPermission("artix.command.postmatch")) {
         notificationManager.sendError(player, "You don't have permission to use this command.");
         return;
      }
      
      // Open post-match inventory
      try {
         openPostMatchInventory(player, targetPlayer);
         notificationManager.sendSuccess(player, "Opened post-match inventory for " + targetPlayer.getName() + ".");
      } catch (Exception e) {
         notificationManager.sendError(player, "Failed to open post-match inventory: " + e.getMessage());
         plugin.getLogger().severe("Error opening post-match inventory: " + e.getMessage());
      }
   }
   
   /**
    * Open post-match inventory
    * @param viewer the viewer
    * @param target the target player
    */
   private void openPostMatchInventory(Player viewer, Player target) {
      // Get target player's profile
      dev.artixdev.practice.models.PlayerProfile targetProfile = plugin.getPlayerManager().getPlayerProfile(target.getUniqueId());
      
      if (targetProfile == null) {
         notificationManager.sendError(viewer, "Target player's profile is not loaded.");
         return;
      }
      
      // Check if target player is in a match
      if (targetProfile.getState() != dev.artixdev.practice.enums.PlayerState.FIGHTING) {
         notificationManager.sendError(viewer, "Target player is not in a match.");
         return;
      }
      
      // Create and open post-match inventory
      dev.artixdev.practice.menus.PostMatchInventoryMenu menu = new dev.artixdev.practice.menus.PostMatchInventoryMenu(target);
      dev.artixdev.api.practice.menu.MenuHandler.getInstance().openMenu(menu, viewer);
   }
   
   /**
    * Get command usage
    * @return command usage
    */
   public String getUsage() {
      return "/postmatch <uuid>";
   }
   
   /**
    * Get command description
    * @return command description
    */
   public String getDescription() {
      return "Open post-match inventory for a specific player";
   }
   
   /**
    * Get command aliases
    * @return command aliases
    */
   public String[] getAliases() {
      return new String[]{"matchinv"};
   }
}
