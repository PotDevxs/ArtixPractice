package dev.artixdev.api.practice.command.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class DrinkTabCompleter implements TabCompleter {
   private final DrinkCommandService commandService;
   private final DrinkCommandContainer container;

   public DrinkTabCompleter(DrinkCommandService commandService, DrinkCommandContainer container) {
      this.commandService = commandService;
      this.container = container;
   }

   public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
      if (command.getName().equalsIgnoreCase(this.container.getName())) {
         Entry<DrinkCommand, String[]> data = this.container.getCommand(args);
         String tC;
         if (data != null && data.getKey() != null) {
            tC = "";
            int tabCompletingIndex = 0;
            if (((String[])data.getValue()).length > 0) {
               tC = ((String[])data.getValue())[((String[])data.getValue()).length - 1];
               tabCompletingIndex = ((String[])data.getValue()).length - 1;
            }

            DrinkCommand drinkCommand = (DrinkCommand)data.getKey();
            boolean permitted = drinkCommand.getPermission() != null && !drinkCommand.getPermission().isEmpty() && sender.hasPermission(drinkCommand.getPermission());
            if (permitted && drinkCommand.getConsumingProviders().length > tabCompletingIndex) {
               List<String> s = drinkCommand.getConsumingProviders()[tabCompletingIndex].getSuggestions(tC);
               if (s != null) {
                  List<String> suggestions = new ArrayList(s);
                  if (args.length == 0 || args.length == 1) {
                     tC = "";
                     if (args.length > 0) {
                        tC = args[args.length - 1];
                     }

                     suggestions.addAll(this.container.getCommandSuggestions(sender, tC));
                  }

                  return suggestions;
               }

               if (args.length == 0 || args.length == 1) {
                  tC = "";
                  if (args.length > 0) {
                     tC = args[args.length - 1];
                  }

                  return this.container.getCommandSuggestions(sender, tC);
               }
            } else if (args.length == 0 || args.length == 1) {
               tC = "";
               if (args.length > 0) {
                  tC = args[args.length - 1];
               }

               return this.container.getCommandSuggestions(sender, tC);
            }
         } else if (args.length == 0 || args.length == 1) {
            tC = "";
            if (args.length > 0) {
               tC = args[args.length - 1];
            }

            return this.container.getCommandSuggestions(sender, tC);
         }
      }

      return Collections.emptyList();
   }
}
