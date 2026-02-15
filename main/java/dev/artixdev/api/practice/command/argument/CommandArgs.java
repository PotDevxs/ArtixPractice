package dev.artixdev.api.practice.command.argument;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.command.CommandFlag;
import dev.artixdev.api.practice.command.command.DrinkCommandService;

public class CommandArgs {
   private final DrinkCommandService commandService;
   private final CommandSender sender;
   private final List<String> args;
   private final String label;
   private final Map<Character, CommandFlag> flags;
   private final ReentrantLock lock = new ReentrantLock();
   private int index = 0;

   public CommandArgs(DrinkCommandService commandService, CommandSender sender, String label, List<String> args, Map<Character, CommandFlag> flags) {
      Preconditions.checkNotNull(commandService, "CommandService cannot be null");
      Preconditions.checkNotNull(sender, "CommandSender cannot be null");
      Preconditions.checkNotNull(label, "Label cannot be null");
      Preconditions.checkNotNull(args, "Command args cannot be null");
      this.commandService = commandService;
      this.sender = sender;
      this.label = label;
      this.args = new ArrayList(args);
      this.flags = flags;
   }

   public boolean hasNext() {
      this.lock.lock();

      boolean var1;
      try {
         var1 = this.args.size() > this.index;
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public String next() {
      this.lock.lock();

      String var1;
      try {
         var1 = (String)this.args.get(this.index++);
      } finally {
         this.lock.unlock();
      }

      return var1;
   }

   public boolean isSenderPlayer() {
      return this.sender instanceof Player;
   }

   public Player getSenderAsPlayer() {
      return (Player)this.sender;
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

   public String getLabel() {
      return this.label;
   }

   public Map<Character, CommandFlag> getFlags() {
      return this.flags;
   }

   public ReentrantLock getLock() {
      return this.lock;
   }

   public int getIndex() {
      return this.index;
   }
}
