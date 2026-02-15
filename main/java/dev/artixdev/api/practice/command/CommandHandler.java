package dev.artixdev.api.practice.command;

import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.command.command.CommandService;
import dev.artixdev.api.practice.command.command.DrinkAuthorizer;
import dev.artixdev.api.practice.command.parametric.binder.DrinkBinder;

public class CommandHandler {
   private final JavaPlugin plugin;
   private final CommandService service;

   public CommandHandler(JavaPlugin plugin) {
      this.plugin = plugin;
      this.service = CommandAPI.get(plugin);
   }

   public void register(Object handler, String name, String... aliases) {
      this.service.register(handler, name, aliases);
   }

   public <T> DrinkBinder<T> bind(Class<T> type) {
      return this.service.bind(type);
   }

   public void registerCommands(String packageName) {
      try {
         this.service.registerCommands(packageName);
      } catch (Exception e) {
         this.plugin.getLogger().info("There was an error registering commands!");
         this.plugin.getLogger().info("Error: " + e.getMessage());
      }

   }

   public void registerCommands() {
      this.service.registerCommands();
   }

   public void registerPermissions() {
      this.service.registerPermissions();
   }

   public void setPermissionMessage(List<String> message) {
      DrinkAuthorizer authorizer = new DrinkAuthorizer();
      authorizer.setNoPermissionMessage(message);
      this.service.setAuthorizer(authorizer);
   }
}
