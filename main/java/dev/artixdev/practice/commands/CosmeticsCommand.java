package dev.artixdev.practice.commands;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.menus.CosmeticsMenu;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.CosmeticSettings;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Cosmetics Command
 * Command for managing player cosmetics
 */
@Register(name = "cosmetics")
public class CosmeticsCommand {
   
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public CosmeticsCommand(Main plugin) {
      this.plugin = plugin;
   }
   
   @Command(
      name = "",
      aliases = {"help"},
      desc = "View cosmetics menu"
   )
   public void openCosmeticsMenu(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      try {
         // Open cosmetics menu
         CosmeticsMenu menu = new CosmeticsMenu();
         MenuHandler.getInstance().openMenu(menu, player);
         
         player.sendMessage(ChatUtils.colorize("&aOpening cosmetics menu..."));
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to open cosmetics menu!"));
         plugin.getLogger().warning("Failed to open cosmetics menu for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "killEffect",
      usage = "<effect>",
      desc = "Set your kill effect"
   )
   public void setKillEffect(@Sender Player player, String effect) {
      if (player == null || effect == null) {
         player.sendMessage(ChatUtils.colorize("&cInvalid parameters!"));
         return;
      }
      
      try {
         // Parse kill effect
         dev.artixdev.practice.models.CosmeticSettings.KillEffect killEffect = 
            dev.artixdev.practice.models.CosmeticSettings.KillEffect.valueOf(effect.toUpperCase());
         
         if (killEffect == null) {
            player.sendMessage(ChatUtils.colorize("&cInvalid kill effect!"));
            return;
         }
         
         // Set kill effect for player
         PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
         if (profile != null) {
            CosmeticSettings cosmetics = profile.getCosmeticSettings();
            if (cosmetics == null) {
               cosmetics = new dev.artixdev.practice.models.CosmeticSettings();
            }
            
            cosmetics.setKillEffect(killEffect);
            profile.setCosmeticSettings(cosmetics);
            
            // Save profile
            plugin.getPlayerManager().savePlayerProfile(profile);
            
            player.sendMessage(ChatUtils.colorize("&aKill effect set to: &f" + killEffect.getName()));
         } else {
            player.sendMessage(ChatUtils.colorize("&cYour profile is not loaded!"));
         }
         
      } catch (IllegalArgumentException e) {
         player.sendMessage(ChatUtils.colorize("&cInvalid kill effect! Available effects:"));
         for (dev.artixdev.practice.models.CosmeticSettings.KillEffect killEffect : 
              dev.artixdev.practice.models.CosmeticSettings.KillEffect.values()) {
            player.sendMessage(ChatUtils.colorize("&7- &f" + killEffect.name()));
         }
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to set kill effect!"));
         plugin.getLogger().warning("Failed to set kill effect for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "killMessage",
      usage = "<message>",
      desc = "Set your kill message"
   )
   public void setKillMessage(@Sender Player player, String message) {
      if (player == null || message == null) {
         player.sendMessage(ChatUtils.colorize("&cInvalid parameters!"));
         return;
      }
      
      try {
         // Parse kill message
         dev.artixdev.practice.models.CosmeticSettings.KillMessage killMessage = 
            dev.artixdev.practice.models.CosmeticSettings.KillMessage.valueOf(message.toUpperCase());
         
         if (killMessage == null) {
            player.sendMessage(ChatUtils.colorize("&cInvalid kill message!"));
            return;
         }
         
         // Set kill message for player
         PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
         if (profile != null) {
            CosmeticSettings cosmetics = profile.getCosmeticSettings();
            if (cosmetics == null) {
               cosmetics = new dev.artixdev.practice.models.CosmeticSettings();
            }
            
            cosmetics.setKillMessage(killMessage);
            profile.setCosmeticSettings(cosmetics);
            
            // Save profile
            plugin.getPlayerManager().savePlayerProfile(profile);
            
            player.sendMessage(ChatUtils.colorize("&aKill message set to: &f" + killMessage.getName()));
         } else {
            player.sendMessage(ChatUtils.colorize("&cYour profile is not loaded!"));
         }
         
      } catch (IllegalArgumentException e) {
         player.sendMessage(ChatUtils.colorize("&cInvalid kill message! Available messages:"));
         for (dev.artixdev.practice.models.CosmeticSettings.KillMessage killMessage : 
              dev.artixdev.practice.models.CosmeticSettings.KillMessage.values()) {
            player.sendMessage(ChatUtils.colorize("&7- &f" + killMessage.name()));
         }
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to set kill message!"));
         plugin.getLogger().warning("Failed to set kill message for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "trail",
      usage = "<trail>",
      desc = "Set your trail"
   )
   public void setTrail(@Sender Player player, String trail) {
      if (player == null || trail == null) {
         player.sendMessage(ChatUtils.colorize("&cInvalid parameters!"));
         return;
      }
      
      try {
         // Parse trail
         dev.artixdev.practice.models.CosmeticSettings.Trail trailType = 
            dev.artixdev.practice.models.CosmeticSettings.Trail.valueOf(trail.toUpperCase());
         
         if (trailType == null) {
            player.sendMessage(ChatUtils.colorize("&cInvalid trail!"));
            return;
         }
         
         // Set trail for player
         PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
         if (profile != null) {
            CosmeticSettings cosmetics = profile.getCosmeticSettings();
            if (cosmetics == null) {
               cosmetics = new dev.artixdev.practice.models.CosmeticSettings();
            }
            
            cosmetics.setTrail(trailType);
            profile.setCosmeticSettings(cosmetics);
            
            // Save profile
            plugin.getPlayerManager().savePlayerProfile(profile);
            
            player.sendMessage(ChatUtils.colorize("&aTrail set to: &f" + trailType.getName()));
         } else {
            player.sendMessage(ChatUtils.colorize("&cYour profile is not loaded!"));
         }
         
      } catch (IllegalArgumentException e) {
         player.sendMessage(ChatUtils.colorize("&cInvalid trail! Available trails:"));
         for (dev.artixdev.practice.models.CosmeticSettings.Trail trailType : 
              dev.artixdev.practice.models.CosmeticSettings.Trail.values()) {
            player.sendMessage(ChatUtils.colorize("&7- &f" + trailType.name()));
         }
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to set trail!"));
         plugin.getLogger().warning("Failed to set trail for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "reset",
      desc = "Reset all your cosmetics"
   )
   public void resetCosmetics(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      try {
         // Reset cosmetics for player
         PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
         if (profile != null) {
            CosmeticSettings cosmetics = profile.getCosmeticSettings();
            if (cosmetics == null) {
               cosmetics = new dev.artixdev.practice.models.CosmeticSettings();
            }
            
            cosmetics.resetAll();
            profile.setCosmeticSettings(cosmetics);
            
            // Save profile
            plugin.getPlayerManager().savePlayerProfile(profile);
            
            player.sendMessage(ChatUtils.colorize("&aAll cosmetics have been reset!"));
         } else {
            player.sendMessage(ChatUtils.colorize("&cYour profile is not loaded!"));
         }
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to reset cosmetics!"));
         plugin.getLogger().warning("Failed to reset cosmetics for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "list",
      desc = "List all available cosmetics"
   )
   public void listCosmetics(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      try {
         player.sendMessage(ChatUtils.colorize("&6&lAvailable Cosmetics"));
         player.sendMessage(ChatUtils.colorize("&7"));
         
         // List kill effects
         player.sendMessage(ChatUtils.colorize("&cKill Effects:"));
         for (dev.artixdev.practice.models.CosmeticSettings.KillEffect effect : 
              dev.artixdev.practice.models.CosmeticSettings.KillEffect.values()) {
            player.sendMessage(ChatUtils.colorize("&7- &f" + effect.name() + " &7- " + effect.getDescription()));
         }
         
         player.sendMessage(ChatUtils.colorize("&7"));
         
         // List kill messages
         player.sendMessage(ChatUtils.colorize("&eKill Messages:"));
         for (dev.artixdev.practice.models.CosmeticSettings.KillMessage message : 
              dev.artixdev.practice.models.CosmeticSettings.KillMessage.values()) {
            player.sendMessage(ChatUtils.colorize("&7- &f" + message.name() + " &7- " + message.getDescription()));
         }
         
         player.sendMessage(ChatUtils.colorize("&7"));
         
         // List trails
         player.sendMessage(ChatUtils.colorize("&dTrails:"));
         for (dev.artixdev.practice.models.CosmeticSettings.Trail trail : 
              dev.artixdev.practice.models.CosmeticSettings.Trail.values()) {
            player.sendMessage(ChatUtils.colorize("&7- &f" + trail.name() + " &7- " + trail.getDescription()));
         }
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to list cosmetics!"));
         plugin.getLogger().warning("Failed to list cosmetics for " + player.getName() + ": " + e.getMessage());
      }
   }
}
