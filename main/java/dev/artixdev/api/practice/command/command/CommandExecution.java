package dev.artixdev.api.practice.command.command;

import java.util.List;
import org.bukkit.command.CommandSender;
import dev.artixdev.api.practice.command.argument.CommandArgs;

public class CommandExecution {
   private final DrinkCommandService commandService;
   private final CommandSender sender;
   private final List<String> args;
   private final CommandArgs commandArgs;
   private final DrinkCommand command;
   private boolean canExecute = true;

   public CommandExecution(DrinkCommandService commandService, CommandSender sender, List<String> args, CommandArgs commandArgs, DrinkCommand command) {
      this.commandService = commandService;
      this.sender = sender;
      this.args = args;
      this.commandArgs = commandArgs;
      this.command = command;
   }

   public void preventExecution() {
      this.canExecute = false;
   }

   public DrinkCommandService getCommandService() {
      return this.commandService;
   }

   public CommandSender getSender() {
      return this.sender;
   }

   public List<String> getArgs() {
      return this.args;
   }

   public CommandArgs getCommandArgs() {
      return this.commandArgs;
   }

   public DrinkCommand getCommand() {
      return this.command;
   }

   public boolean isCanExecute() {
      return this.canExecute;
   }
}
