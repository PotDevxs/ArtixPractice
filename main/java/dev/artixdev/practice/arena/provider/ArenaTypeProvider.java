package dev.artixdev.practice.arena.provider;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;
import dev.artixdev.practice.enums.ArenaType;

/**
 * Arena Type Provider
 * Provides ArenaType enum values for command arguments
 */
public class ArenaTypeProvider extends DrinkProvider<ArenaType> {
   
   @Override
   public boolean doesConsumeArgument() {
      return true;
   }

   @Override
   public boolean isAsync() {
      return false;
   }

   @Override
   public ArenaType provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String name = arg.get();

      try {
         return ArenaType.valueOf(name.toUpperCase());
      } catch (Exception e) {
         throw new CommandExitMessage("An ArenaType with that name does not exist!");
      }
   }

   @Override
   public String argumentDescription() {
      return "arena";
   }

   @Override
   public List<String> getSuggestions(String prefix) {
      return Arrays.stream(ArenaType.values())
         .map(Enum::name)
         .filter(s -> prefix.length() == 0 || s.startsWith(prefix))
         .collect(Collectors.toList());
   }
}