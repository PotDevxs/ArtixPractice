package dev.artixdev.api.practice.command.provider.spigot;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class PlayerSenderProvider extends DrinkProvider<Player> {
   public static final PlayerSenderProvider INSTANCE = new PlayerSenderProvider();

   public boolean doesConsumeArgument() {
      return false;
   }

   public boolean isAsync() {
      return false;
   }

   public Player provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      if (arg.isSenderPlayer()) {
         return arg.getSenderAsPlayer();
      } else {
         throw new CommandExitMessage("This is a player-only command.");
      }
   }

   public String argumentDescription() {
      return "player sender";
   }

   public List<String> getSuggestions(String prefix) {
      return Collections.emptyList();
   }
}
