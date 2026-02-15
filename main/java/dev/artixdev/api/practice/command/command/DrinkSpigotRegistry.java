package dev.artixdev.api.practice.command.command;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;

public class DrinkSpigotRegistry {
   private static final Logger log = LogManager.getLogger(DrinkSpigotRegistry.class);
   private static final Field COMMAND_MAP;
   private static final Field KNOWN_COMMANDS;

   private boolean doesBukkitCommandConflict(Command bukkitCommand, DrinkCommandContainer container) {
      if (!container.getName().equalsIgnoreCase(bukkitCommand.getName()) && !bukkitCommand.getAliases().stream().anyMatch((a) -> {
         return a.equalsIgnoreCase(container.getName());
      })) {
         Iterator var3 = container.getAliases().iterator();

         while(var3.hasNext()) {
            final String realAlias = (String)var3.next();
            if (bukkitCommand.getName().equalsIgnoreCase(realAlias) || bukkitCommand.getAliases().stream().anyMatch((a) -> {
               return a.equalsIgnoreCase(realAlias);
            })) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public void register(DrinkCommandContainer container, boolean override) {
      try {
         SimplePluginManager simplePluginManager = (SimplePluginManager)Bukkit.getServer().getPluginManager();
         SimpleCommandMap simpleCommandMap = (SimpleCommandMap)COMMAND_MAP.get(simplePluginManager);
         if (override) {
            Map<String, Command> knownCommands = (Map)KNOWN_COMMANDS.get(simpleCommandMap);
            Iterator iterator = knownCommands.entrySet().iterator();

            while(iterator.hasNext()) {
               Entry<String, Command> entry = (Entry)iterator.next();
               Command registeredCommand = (Command)entry.getValue();
               if (this.doesBukkitCommandConflict(registeredCommand, container)) {
                  log.debug("[" + container.getName() + "] {} command is conflicting with our {} command, overriding!", registeredCommand.getName(), container.getName());
                  registeredCommand.unregister(simpleCommandMap);
                  iterator.remove();
               }
            }
         }

         simpleCommandMap.register(container.getPlugin().getName(), container);
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }

   }

   static {
      Field mapField = null;
      Field commandsField = null;

      try {
         mapField = SimplePluginManager.class.getDeclaredField("commandMap");
         commandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
         mapField.setAccessible(true);
         commandsField.setAccessible(true);
      } catch (Exception e) {
         System.err.println("Failed to grab commandMap from the plugin manager.");
         e.printStackTrace();
      }

      COMMAND_MAP = mapField;
      KNOWN_COMMANDS = commandsField;
   }
}
