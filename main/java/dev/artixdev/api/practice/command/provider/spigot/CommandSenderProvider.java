package dev.artixdev.api.practice.command.provider.spigot;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class CommandSenderProvider extends DrinkProvider<CommandSender> {
   public static final CommandSenderProvider INSTANCE = new CommandSenderProvider();

   public boolean doesConsumeArgument() {
      return false;
   }

   public boolean isAsync() {
      return false;
   }

   public boolean allowNullArgument() {
      return true;
   }

   public CommandSender defaultNullValue() {
      return null;
   }

   public CommandSender provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      return arg.getSender();
   }

   public String argumentDescription() {
      return "sender";
   }

   public List<String> getSuggestions(String prefix) {
      return Collections.emptyList();
   }
}
