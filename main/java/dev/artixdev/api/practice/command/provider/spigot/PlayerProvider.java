package dev.artixdev.api.practice.command.provider.spigot;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class PlayerProvider extends DrinkProvider<Player> {
   private final Plugin plugin;

   public PlayerProvider(Plugin plugin) {
      this.plugin = plugin;
   }

   public boolean doesConsumeArgument() {
      return true;
   }

   public boolean isAsync() {
      return false;
   }

   public boolean allowNullArgument() {
      return false;
   }

   public Player defaultNullValue() {
      return null;
   }

   public Player provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      String name = arg.get();
      Player p = this.plugin.getServer().getPlayer(name);
      if (p != null) {
         return p;
      } else {
         throw new CommandExitMessage("No player online with name '" + name + "'.");
      }
   }

   public String argumentDescription() {
      return "player";
   }

   public List<String> getSuggestions(String prefix) {
      return (List)this.plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).filter((s) -> {
         return prefix.length() == 0 || s.startsWith(prefix);
      }).collect(Collectors.toList());
   }
}
