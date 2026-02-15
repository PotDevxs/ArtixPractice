package dev.artixdev.api.practice.command.provider;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class LongProvider extends DrinkProvider<Long> {
   public static final LongProvider INSTANCE = new LongProvider();

   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   public boolean allowNullArgument() {
      return false;
   }

   public Long defaultNullValue() {
      return 0L;
   }

   public Long provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String s = arg.get();

      try {
         return Long.parseLong(s);
      } catch (NumberFormatException e) {
         throw new CommandExitMessage("Required: Long Number, Given: '" + s + "'");
      }
   }

   public String argumentDescription() {
      return "long number";
   }

   public List<String> getSuggestions(String prefix) {
      return Collections.emptyList();
   }
}
