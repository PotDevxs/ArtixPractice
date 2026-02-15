package dev.artixdev.practice.commands;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.OptArg;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.*;
import dev.artixdev.practice.models.Team;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Party Command
 * Command for managing parties
 */
@Register(name = "party", aliases = {"p"})
public class PartyCommand {
   
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public PartyCommand(Main plugin) {
      this.plugin = plugin;
   }
   
   @Command(
      name = "roster",
      aliases = {"classes"},
      desc = "View your party's roster for TeamFight"
   )
   public void viewRoster(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      try {
         // Get player's party
         Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
         if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
         }
         
         // Display party roster
         player.sendMessage(ChatUtils.colorize("&6&lParty Roster"));
         Player leader = party.getLeader();
         player.sendMessage(ChatUtils.colorize("&7Leader: &f" + (leader != null ? leader.getName() : "Unknown")));
         player.sendMessage(ChatUtils.colorize("&7Members: &f" + party.getMembers().size() + "/" + party.getMaxSize()));
         player.sendMessage(ChatUtils.colorize("&7"));
         
         for (UUID memberId : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null) {
               String role = memberId.equals(party.getLeader().getUniqueId()) ? "&cLeader" : "&7Member";
               player.sendMessage(ChatUtils.colorize("&7- " + role + " &f" + member.getName()));
            }
         }
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to view party roster!"));
         plugin.getLogger().warning("Failed to view party roster for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "create",
      desc = "Create a new party"
   )
   public void createParty(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      try {
         // Check if player is already in a party
         if (plugin.getPartyManager().getPlayerParty(player.getUniqueId()) != null) {
            player.sendMessage(ChatUtils.colorize("&cYou are already in a party!"));
            return;
         }
         
         // Create new party
         Team party = plugin.getPartyManager().createParty(player);
         if (party != null) {
            player.sendMessage(ChatUtils.colorize("&aParty created successfully!"));
            player.sendMessage(ChatUtils.colorize("&7Use &f/party invite <player> &7to invite members."));
         } else {
            player.sendMessage(ChatUtils.colorize("&cFailed to create party!"));
         }
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to create party!"));
         plugin.getLogger().warning("Failed to create party for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "invite",
      usage = "<player>",
      desc = "Invite a player to your party"
   )
   public void invitePlayer(@Sender Player player, Player target) {
      if (player == null || target == null) {
         player.sendMessage(ChatUtils.colorize("&cInvalid parameters!"));
         return;
      }
      
      try {
         // Get player's party
         Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
         if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
         }
         
         // Check if player is the leader
         if (!party.getLeader().getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatUtils.colorize("&cOnly the party leader can invite players!"));
            return;
         }
         
         // Check if target is already in a party
         if (plugin.getPartyManager().getPlayerParty(target.getUniqueId()) != null) {
            player.sendMessage(ChatUtils.colorize("&c" + target.getName() + " is already in a party!"));
            return;
         }
         
         // Check if party is full
         if (party.getMembers().size() >= party.getMaxSize()) {
            player.sendMessage(ChatUtils.colorize("&cYour party is full!"));
            return;
         }
         
         // Send invitation
         boolean success = plugin.getPartyManager().sendInvitation(party, target);
         if (success) {
            player.sendMessage(ChatUtils.colorize("&aInvitation sent to " + target.getName() + "!"));
            target.sendMessage(ChatUtils.colorize("&aYou have been invited to " + player.getName() + "'s party!"));
            target.sendMessage(ChatUtils.colorize("&7Use &f/party accept " + player.getName() + " &7to join."));
         } else {
            player.sendMessage(ChatUtils.colorize("&cFailed to send invitation!"));
         }
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to invite player!"));
         plugin.getLogger().warning("Failed to invite player for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "accept",
      usage = "<leader>",
      desc = "Accept a party invitation"
   )
   public void acceptInvitation(@Sender Player player, Player leader) {
      if (player == null || leader == null) {
         player.sendMessage(ChatUtils.colorize("&cInvalid parameters!"));
         return;
      }
      
      try {
         // Check if player is already in a party
         if (plugin.getPartyManager().getPlayerParty(player.getUniqueId()) != null) {
            player.sendMessage(ChatUtils.colorize("&cYou are already in a party!"));
            return;
         }
         
         // Accept invitation
         boolean success = plugin.getPartyManager().acceptInvitation(player, leader);
         if (success) {
            player.sendMessage(ChatUtils.colorize("&aYou joined " + leader.getName() + "'s party!"));
            leader.sendMessage(ChatUtils.colorize("&a" + player.getName() + " joined your party!"));
         } else {
            player.sendMessage(ChatUtils.colorize("&cNo invitation found from " + leader.getName() + "!"));
         }
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to accept invitation!"));
         plugin.getLogger().warning("Failed to accept invitation for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "leave",
      desc = "Leave your current party"
   )
   public void leaveParty(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      try {
         // Get player's party
         Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
         if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
         }
         
         // Check if player is the leader
         if (party.getLeader().getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatUtils.colorize("&cYou cannot leave your own party! Use &f/party disband &cto disband it."));
            return;
         }
         
         // Leave party
         boolean success = plugin.getPartyManager().removePlayerFromParty(player.getUniqueId());
         if (success) {
            player.sendMessage(ChatUtils.colorize("&aYou left the party!"));
            
            // Notify party members
            for (UUID memberId : party.getMembers()) {
               Player member = Bukkit.getPlayer(memberId);
               if (member != null && !member.equals(player)) {
                  member.sendMessage(ChatUtils.colorize("&c" + player.getName() + " left the party!"));
               }
            }
         } else {
            player.sendMessage(ChatUtils.colorize("&cFailed to leave party!"));
         }
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to leave party!"));
         plugin.getLogger().warning("Failed to leave party for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "disband",
      desc = "Disband your party"
   )
   public void disbandParty(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      try {
         // Get player's party
         Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
         if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
         }
         
         // Check if player is the leader
         if (!party.getLeader().getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatUtils.colorize("&cOnly the party leader can disband the party!"));
            return;
         }
         
         // Disband party
         boolean success = plugin.getPartyManager().disbandParty(party);
         if (success) {
            player.sendMessage(ChatUtils.colorize("&aParty disbanded successfully!"));
            
            // Notify party members
            for (UUID memberId : party.getMembers()) {
               Player member = Bukkit.getPlayer(memberId);
               if (member != null && !member.equals(player)) {
                  member.sendMessage(ChatUtils.colorize("&cThe party has been disbanded!"));
               }
            }
         } else {
            player.sendMessage(ChatUtils.colorize("&cFailed to disband party!"));
         }
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to disband party!"));
         plugin.getLogger().warning("Failed to disband party for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "kick",
      usage = "<player>",
      desc = "Kick a player from your party"
   )
   public void kickPlayer(@Sender Player player, Player target) {
      if (player == null || target == null) {
         player.sendMessage(ChatUtils.colorize("&cInvalid parameters!"));
         return;
      }
      
      try {
         // Get player's party
         Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
         if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
         }
         
         // Check if player is the leader
         if (!party.getLeader().getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatUtils.colorize("&cOnly the party leader can kick players!"));
            return;
         }
         
         // Check if target is in the party
         if (!party.getMembers().contains(target.getUniqueId())) {
            player.sendMessage(ChatUtils.colorize("&c" + target.getName() + " is not in your party!"));
            return;
         }
         
         // Check if trying to kick yourself
         if (target.equals(player)) {
            player.sendMessage(ChatUtils.colorize("&cYou cannot kick yourself! Use &f/party disband &cto disband the party."));
            return;
         }
         
         // Kick player
         boolean success = plugin.getPartyManager().removePlayerFromParty(target.getUniqueId());
         if (success) {
            player.sendMessage(ChatUtils.colorize("&a" + target.getName() + " has been kicked from the party!"));
            target.sendMessage(ChatUtils.colorize("&cYou have been kicked from the party!"));
         } else {
            player.sendMessage(ChatUtils.colorize("&cFailed to kick player!"));
         }
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to kick player!"));
         plugin.getLogger().warning("Failed to kick player for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   @Command(
      name = "info",
      desc = "View party information"
   )
   public void viewPartyInfo(@Sender Player player) {
      if (player == null) {
         return;
      }
      
      try {
         // Get player's party
         Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
         if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
         }
         
         // Display party information
         player.sendMessage(ChatUtils.colorize("&6&lParty Information"));
         player.sendMessage(ChatUtils.colorize("&7Leader: &f" + party.getLeader().getName()));
         player.sendMessage(ChatUtils.colorize("&7Members: &f" + party.getMembers().size() + "/" + party.getMaxSize()));
         player.sendMessage(ChatUtils.colorize("&7Created: &f" + getTimeAgo(party.getCreatedAt())));
         player.sendMessage(ChatUtils.colorize("&7"));
         
         // List members
         player.sendMessage(ChatUtils.colorize("&7Members:"));
         for (UUID memberId : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null) {
               String role = memberId.equals(party.getLeader().getUniqueId()) ? "&cLeader" : "&7Member";
               String status = member.isOnline() ? "&aOnline" : "&cOffline";
               player.sendMessage(ChatUtils.colorize("&7- " + role + " &f" + member.getName() + " &7(" + status + "&7)"));
            }
         }
         
      } catch (Exception e) {
         player.sendMessage(ChatUtils.colorize("&cFailed to view party information!"));
         plugin.getLogger().warning("Failed to view party info for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   /**
    * Get time ago string
    * @param timestamp the timestamp
    * @return time ago string
    */
   private String getTimeAgo(long timestamp) {
      long diff = System.currentTimeMillis() - timestamp;
      long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
      long hours = TimeUnit.MILLISECONDS.toHours(diff);
      long days = TimeUnit.MILLISECONDS.toDays(diff);
      
      if (days > 0) {
         return days + " day(s) ago";
      } else if (hours > 0) {
         return hours + " hour(s) ago";
      } else if (minutes > 0) {
         return minutes + " minute(s) ago";
      } else {
         return "Just now";
      }
   }
}
