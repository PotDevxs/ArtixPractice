package dev.artixdev.api.practice.command.command;

import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DrinkCommandExecutor implements CommandExecutor {
   private final DrinkCommandService commandService;
   private final DrinkCommandContainer container;

   public DrinkCommandExecutor(DrinkCommandService commandService, DrinkCommandContainer container) {
      this.commandService = commandService;
      this.container = container;
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (command.getName().equalsIgnoreCase(this.container.getName())) {
         try {
            Entry<DrinkCommand, String[]> data = this.container.getCommand(args);
            if (data != null && data.getKey() != null) {
               DrinkCommand drinkCommand = (DrinkCommand)data.getKey();
               boolean helpRequired = !this.container.getCommands().containsKey("help") && !drinkCommand.getName().equalsIgnoreCase("help") && !drinkCommand.getAliases().contains("help");
               if (args.length > 0 && args[args.length - 1].equalsIgnoreCase("help") && helpRequired && this.commandService.getAuthorizer().isAuthorized(sender, drinkCommand)) {
                  this.commandService.getHelpService().sendHelpFor(sender, this.container);
                  return true;
               }

               this.commandService.executeCommand(sender, (DrinkCommand)data.getKey(), label, (String[])data.getValue());
            } else if (args.length > 0) {
               if (args[args.length - 1].equalsIgnoreCase("help") && this.commandService.getAuthorizer().isAuthorized(sender, this.container)) {
                  this.commandService.getHelpService().sendHelpFor(sender, this.container);
                  return true;
               }

               sender.sendMessage(ChatColor.RED + "Unknown sub-command: " + args[0] + ".  Use '/" + label + " help' for available cmds.");
            } else if (this.container.isDefaultCommandIsHelp()) {
               this.commandService.getHelpService().sendHelpFor(sender, this.container);
            } else {
               sender.sendMessage(ChatColor.RED + "Please choose a sub-command.  Use '/" + label + " help' for available cmds.");
            }

            return true;
         } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An exception occurred while performing this command.");
            e.printStackTrace();
         }
      }

      return false;
   }
}
