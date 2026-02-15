package dev.artixdev.api.practice.command.command;

import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DrinkAuthorizer {
   private List<String> noPermissionMessage;

   public DrinkAuthorizer() {
      this.noPermissionMessage = Collections.singletonList(ChatColor.RED + "You do not have permission to perform this command.");
   }

   public boolean isAuthorized(CommandSender sender, DrinkCommand command) {
      if (command.getPermission() != null && command.getPermission().length() > 0 && !sender.hasPermission(command.getPermission())) {
         this.noPermissionMessage.forEach(sender::sendMessage);
         return false;
      } else {
         return true;
      }
   }

   public boolean isAuthorized(CommandSender sender, DrinkCommandContainer command) {
      if (command.getPermission() != null && command.getPermission().length() > 0 && !sender.hasPermission(command.getPermission())) {
         this.noPermissionMessage.forEach(sender::sendMessage);
         return false;
      } else {
         return true;
      }
   }

   public List<String> getNoPermissionMessage() {
      return this.noPermissionMessage;
   }

   public void setNoPermissionMessage(List<String> noPermissionMessage) {
      this.noPermissionMessage = noPermissionMessage;
   }
}
