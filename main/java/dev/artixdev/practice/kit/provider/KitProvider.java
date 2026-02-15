package dev.artixdev.practice.kit.provider;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;
import dev.artixdev.practice.models.Kit;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.Main;

/**
 * Kit Provider
 * Provides Kit objects for command arguments
 */
public class KitProvider extends DrinkProvider<Kit> {
   
   @Override
   public boolean doesConsumeArgument() {
      return true;
   }

   @Override
   public boolean isAsync() {
      return false;
   }

   @Override
   public Kit provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String name = arg.get();
      Kit kit = Main.getInstance().getKitManager().getKitByName(name);
      if (kit == null) {
         throw new CommandExitMessage("A kit with that name does not exist!");
      } else {
         return kit;
      }
   }

   @Override
   public String argumentDescription() {
      return "kit";
   }

   @Override
   public List<String> getSuggestions(String prefix) {
      return Main.getInstance().getKitManager().getKits().stream()
         .map(KitType::name)
         .filter(s -> prefix.length() == 0 || s.startsWith(prefix) || s.toLowerCase().startsWith(prefix.toLowerCase()))
         .collect(Collectors.toList());
   }
}
