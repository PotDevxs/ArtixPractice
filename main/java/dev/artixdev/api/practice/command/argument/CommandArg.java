package dev.artixdev.api.practice.command.argument;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandArg {
   private final CommandSender sender;
   private final String value;
   private final String label;
   private final CommandArgs args;

   public CommandArg(CommandSender sender, String value, CommandArgs args) {
      this.sender = sender;
      this.value = value;
      this.label = args.getLabel();
      this.args = args;
   }

   public CommandSender getSender() {
      return this.sender;
   }

   public String get() {
      return this.value;
   }

   public String getLabel() {
      return this.label;
   }

   public boolean isSenderPlayer() {
      return this.sender instanceof Player;
   }

   public Player getSenderAsPlayer() {
      return (Player)this.sender;
   }

   public CommandArgs getArgs() {
      return this.args;
   }
}
