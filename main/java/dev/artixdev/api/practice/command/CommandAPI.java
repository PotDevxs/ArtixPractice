package dev.artixdev.api.practice.command;

import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.command.command.CommandService;
import dev.artixdev.api.practice.command.command.DrinkCommandService;

public class CommandAPI {
   private static final ConcurrentMap<String, CommandService> services = new ConcurrentHashMap();

   public static CommandService get(JavaPlugin javaPlugin) {
      Preconditions.checkNotNull(javaPlugin, "JavaPlugin cannot be null");
      return (CommandService)services.computeIfAbsent(javaPlugin.getName(), (name) -> {
         return new DrinkCommandService(javaPlugin);
      });
   }
}
