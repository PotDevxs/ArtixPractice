package dev.artixdev.api.practice.command.provider;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.argument.CommandArgs;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class CommandArgsProvider extends DrinkProvider<CommandArgs> {
   public static final CommandArgsProvider INSTANCE = new CommandArgsProvider();

   public boolean doesConsumeArgument() {
      return false;
   }

   public boolean isAsync() {
      return false;
   }

   public CommandArgs provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      return arg.getArgs();
   }

   public String argumentDescription() {
      return "args";
   }

   public List<String> getSuggestions(String prefix) {
      return Collections.emptyList();
   }
}
