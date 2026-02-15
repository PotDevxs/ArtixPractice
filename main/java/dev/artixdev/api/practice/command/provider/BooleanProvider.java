package dev.artixdev.api.practice.command.provider;

import com.google.common.collect.ImmutableList;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class BooleanProvider extends DrinkProvider<Boolean> {
   public static final BooleanProvider INSTANCE = new BooleanProvider();
   private static final List<String> SUGGEST = ImmutableList.of("true", "false");
   private static final List<String> SUGGEST_TRUE = ImmutableList.of("true");
   private static final List<String> SUGGEST_FALSE = ImmutableList.of("false");

   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   public boolean allowNullArgument() {
      return true;
   }

   public Boolean defaultNullValue() {
      return false;
   }

   public Boolean provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String s = arg.get();
      if (s == null) {
         return false;
      } else {
         try {
            return Boolean.parseBoolean(s);
} catch (NumberFormatException e) {
         throw new CommandExitMessage("Required: Boolean (true/false), Given: '" + s + "'");
         }
      }
   }

   public String argumentDescription() {
      return "true/false";
   }

   public List<String> getSuggestions(String prefix) {
      prefix = prefix.toLowerCase();
      if (prefix.length() == 0) {
         return SUGGEST;
      } else if ("true".startsWith(prefix)) {
         return SUGGEST_TRUE;
      } else {
         return "false".startsWith(prefix) ? SUGGEST_FALSE : Collections.emptyList();
      }
   }
}
