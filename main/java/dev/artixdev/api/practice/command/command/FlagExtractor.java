package dev.artixdev.api.practice.command.command;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import dev.artixdev.api.practice.command.exception.CommandArgumentException;

public class FlagExtractor {
   private final DrinkCommandService commandService;

   public FlagExtractor(DrinkCommandService commandService) {
      this.commandService = commandService;
   }

   public Map<Character, CommandFlag> extractFlags(List<String> args) throws CommandArgumentException {
      Preconditions.checkNotNull(args, "Args cannot be null");
      Map<Character, CommandFlag> flags = new HashMap();
      Iterator<String> it = args.iterator();
      Character currentFlag = null;

      while(it.hasNext()) {
         String arg = (String)it.next();
         if (currentFlag != null) {
            if (!this.isFlag(arg)) {
               flags.put(currentFlag, new CommandFlag(currentFlag, arg));
            } else {
               flags.put(currentFlag, new CommandFlag(currentFlag, "true"));
            }

            it.remove();
            currentFlag = null;
         } else if (this.isFlag(arg)) {
            char f = this.getFlag(arg);
            if (flags.containsKey(f)) {
               throw new CommandArgumentException("The flag '-" + f + "' has already been provided in this command.");
            }

            currentFlag = f;
            if (!it.hasNext()) {
               flags.put(currentFlag, new CommandFlag(currentFlag, "true"));
               currentFlag = null;
            }

            it.remove();
         }
      }

      return flags;
   }

   public char getFlag(String arg) {
      return arg.charAt(1);
   }

   public boolean isFlag(String arg) {
      return arg.length() == 2 && arg.charAt(0) == '-';
   }
}
