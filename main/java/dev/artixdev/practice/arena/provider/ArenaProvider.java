package dev.artixdev.practice.arena.provider;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.enums.ArenaType;
import dev.artixdev.practice.Main;

/**
 * Arena Provider
 * Provides Arena objects for command arguments
 */
public class ArenaProvider extends DrinkProvider<Arena> {
   
   @Override
   public boolean doesConsumeArgument() {
      return true;
   }

   @Override
   public boolean isAsync() {
      return false;
   }

   @Override
   public Arena provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String name = arg.get();
      Arena arena = Main.getInstance().getArenaManager().getArenaByName(name);
      if (arena == null) {
         throw new CommandExitMessage("An arena with that name does not exist!");
      } else {
         return arena;
      }
   }

   @Override
   public String argumentDescription() {
      return "arena";
   }

   @Override
   public List<String> getSuggestions(String prefix) {
      return Main.getInstance().getArenaManager().getArenas().values().stream()
         .map(Arena::getName)
         .filter(s -> prefix.length() == 0 || s.startsWith(prefix) || s.toLowerCase().startsWith(prefix.toLowerCase()))
         .collect(Collectors.toList());
   }
}