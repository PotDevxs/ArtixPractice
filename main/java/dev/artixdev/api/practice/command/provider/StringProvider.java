package dev.artixdev.api.practice.command.provider;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class StringProvider extends DrinkProvider<String> {
   public static final StringProvider INSTANCE = new StringProvider();

   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   public boolean allowNullArgument() {
      return true;
   }

   public String defaultNullValue() {
      return null;
   }

   public String provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      return arg.get();
   }

   public String argumentDescription() {
      return "string";
   }

   public List<String> getSuggestions(String prefix) {
      return Collections.emptyList();
   }
}
