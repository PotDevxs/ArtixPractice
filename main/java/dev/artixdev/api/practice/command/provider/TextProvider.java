package dev.artixdev.api.practice.command.provider;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class TextProvider extends DrinkProvider<String> {
   public static final TextProvider INSTANCE = new TextProvider();

   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   public String provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      StringBuilder builder = new StringBuilder(arg.get());

      while(arg.getArgs().hasNext()) {
         builder.append(" ").append(arg.getArgs().next());
      }

      return builder.toString();
   }

   public String argumentDescription() {
      return "text";
   }

   public List<String> getSuggestions(String prefix) {
      return Collections.emptyList();
   }
}
