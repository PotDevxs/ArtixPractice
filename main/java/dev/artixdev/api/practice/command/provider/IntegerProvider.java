package dev.artixdev.api.practice.command.provider;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class IntegerProvider extends DrinkProvider<Integer> {
   public static final IntegerProvider INSTANCE = new IntegerProvider();

   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   public boolean allowNullArgument() {
      return false;
   }

   public Integer defaultNullValue() {
      return 0;
   }

   public Integer provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String s = arg.get();

      try {
         return Integer.parseInt(s);
      } catch (NumberFormatException e) {
         throw new CommandExitMessage("Required: Integer, Given: '" + s + "'");
      }
   }

   public String argumentDescription() {
      return "integer";
   }

   public List<String> getSuggestions(String prefix) {
      return Collections.emptyList();
   }
}
