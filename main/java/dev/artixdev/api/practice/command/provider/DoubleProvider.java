package dev.artixdev.api.practice.command.provider;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class DoubleProvider extends DrinkProvider<Double> {
   public static final DoubleProvider INSTANCE = new DoubleProvider();

   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   public boolean allowNullArgument() {
      return false;
   }

   public Double defaultNullValue() {
      return 0.0D;
   }

   public Double provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String s = arg.get();

      try {
         return Double.parseDouble(s);
      } catch (NumberFormatException e) {
         throw new CommandExitMessage("Required: Decimal Number, Given: '" + s + "'");
      }
   }

   public String argumentDescription() {
      return "decimal number";
   }

   public List<String> getSuggestions(String prefix) {
      return Collections.emptyList();
   }
}
