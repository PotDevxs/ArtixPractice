package dev.artixdev.api.practice.command.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Require;
import dev.artixdev.api.practice.command.exception.CommandRegistrationException;
import dev.artixdev.api.practice.command.exception.CommandStructureException;
import dev.artixdev.api.practice.command.exception.MissingProviderException;

public class CommandExtractor {
   private final DrinkCommandService commandService;

   public CommandExtractor(DrinkCommandService commandService) {
      this.commandService = commandService;
   }

   public Map<String, DrinkCommand> extractCommands(Object handler) throws MissingProviderException, CommandStructureException {
      Preconditions.checkNotNull(handler, "Handler object cannot be null");
      Map<String, DrinkCommand> commands = new HashMap();
      Method[] var3 = handler.getClass().getDeclaredMethods();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method method = var3[var5];
         Optional<DrinkCommand> o = this.extractCommand(handler, method);
         if (o.isPresent()) {
            DrinkCommand drinkCommand = (DrinkCommand)o.get();
            commands.put(this.commandService.getCommandKey(drinkCommand.getName()), drinkCommand);
         }
      }

      return commands;
   }

   private Optional<DrinkCommand> extractCommand(Object handler, Method method) throws MissingProviderException, CommandStructureException {
      Preconditions.checkNotNull(handler, "Handler object cannot be null");
      Preconditions.checkNotNull(method, "Method cannot be null");
      if (method.isAnnotationPresent(Command.class)) {
         try {
            method.setAccessible(true);
} catch (SecurityException e) {
         throw new CommandRegistrationException("Couldn't access method " + method.getName());
         }

         Command command = (Command)method.getAnnotation(Command.class);
         String perm = "";
         if (method.isAnnotationPresent(Require.class)) {
            Require require = (Require)method.getAnnotation(Require.class);
            perm = require.value();
         }

         DrinkCommand drinkCommand = new DrinkCommand(this.commandService, command.name(), Sets.newHashSet(command.aliases()), command.desc(), command.usage(), perm, handler, method);
         return Optional.of(drinkCommand);
      } else {
         return Optional.empty();
      }
   }
}
