package dev.artixdev.api.practice.command.command;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

public class DrinkCommandContainer extends Command implements PluginIdentifiableCommand {
   private final DrinkCommandService commandService;
   private final Object object;
   private final String name;
   private final Set<String> aliases;
   private final Map<String, DrinkCommand> commands;
   private final DrinkCommand defaultCommand;
   private final DrinkCommandExecutor executor;
   private final DrinkTabCompleter tabCompleter;
   private boolean overrideExistingCommands = true;
   private boolean defaultCommandIsHelp = false;

   public DrinkCommandContainer(DrinkCommandService commandService, Object object, String name, Set<String> aliases, Map<String, DrinkCommand> commands) {
      super(name, "", "/" + name, new ArrayList(aliases));
      this.commandService = commandService;
      this.object = object;
      this.name = name;
      this.aliases = aliases;
      this.commands = commands;
      this.defaultCommand = this.calculateDefaultCommand();
      this.executor = new DrinkCommandExecutor(commandService, this);
      this.tabCompleter = new DrinkTabCompleter(commandService, this);
      if (this.defaultCommand != null) {
         this.setUsage("/" + name + " " + this.defaultCommand.getGeneratedUsage());
         this.setDescription(this.defaultCommand.getDescription());
         this.setPermission(this.defaultCommand.getPermission());
      }

   }

   public final DrinkCommandContainer registerSub(Object handler) {
      return this.commandService.registerSub(this, handler);
   }

   public List<String> getCommandSuggestions(CommandSender sender, String prefix) {
      Preconditions.checkNotNull(prefix, "Prefix cannot be null");
      String p = prefix.toLowerCase();
      List<String> suggestions = new ArrayList();
      Iterator var5 = this.commands.values().iterator();

      label44:
      while(true) {
         DrinkCommand c;
         do {
            if (!var5.hasNext()) {
               return suggestions;
            }

            c = (DrinkCommand)var5.next();
         } while(c.getPermission() != null && !c.getPermission().isEmpty() && !sender.hasPermission(c.getPermission()));

         Iterator var7 = c.getAllAliases().iterator();

         while(true) {
            String alias;
            do {
               do {
                  if (!var7.hasNext()) {
                     continue label44;
                  }

                  alias = (String)var7.next();
               } while(alias.length() <= 0);
            } while(p.length() != 0 && !alias.toLowerCase().startsWith(p));

            suggestions.add(alias);
         }
      }
   }

   private DrinkCommand calculateDefaultCommand() {
      Iterator var1 = this.commands.values().iterator();

      DrinkCommand dc;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         dc = (DrinkCommand)var1.next();
      } while(dc.getName().length() != 0 && !dc.getName().equals(DrinkCommandService.DEFAULT_KEY));

      return dc;
   }

   public DrinkCommand get(String name) {
      Preconditions.checkNotNull(name, "Name cannot be null");
      return (DrinkCommand)this.commands.get(this.commandService.getCommandKey(name));
   }

   public DrinkCommand getByKeyOrAlias(String key) {
      Preconditions.checkNotNull(key, "Key cannot be null");
      if (this.commands.containsKey(key)) {
         return (DrinkCommand)this.commands.get(key);
      } else {
         Iterator var2 = this.commands.values().iterator();

         DrinkCommand drinkCommand;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            drinkCommand = (DrinkCommand)var2.next();
         } while(!drinkCommand.getLoweredAliases().contains(key.toLowerCase()));

         return drinkCommand;
      }
   }

   public Entry<DrinkCommand, String[]> getCommand(String[] args) {
      for(int i = args.length - 1; i >= 0; --i) {
         String key = this.commandService.getCommandKey(StringUtils.join(Arrays.asList(Arrays.copyOfRange(args, 0, i + 1)), ' '));
         DrinkCommand drinkCommand = this.getByKeyOrAlias(key);
         if (drinkCommand != null) {
            return new SimpleEntry(drinkCommand, Arrays.copyOfRange(args, i + 1, args.length));
         }
      }

      return new SimpleEntry(this.getDefaultCommand(), args);
   }

   public DrinkCommand getDefaultCommand() {
      return this.defaultCommand;
   }

   public boolean execute(CommandSender commandSender, String s, String[] strings) {
      return this.executor.onCommand(commandSender, this, s, strings);
   }

   public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
      return this.tabCompleter.onTabComplete(sender, this, alias, args);
   }

   public Plugin getPlugin() {
      return this.commandService.getPlugin();
   }

   public DrinkCommandService getCommandService() {
      return this.commandService;
   }

   public Object getObject() {
      return this.object;
   }

   public String getName() {
      return this.name;
   }

   public Set<String> getDrinkAliases() {
      return this.aliases;
   }

   public Map<String, DrinkCommand> getCommands() {
      return this.commands;
   }

   public DrinkCommandExecutor getExecutor() {
      return this.executor;
   }

   public DrinkTabCompleter getTabCompleter() {
      return this.tabCompleter;
   }

   public boolean isOverrideExistingCommands() {
      return this.overrideExistingCommands;
   }

   public DrinkCommandContainer setOverrideExistingCommands(boolean overrideExistingCommands) {
      this.overrideExistingCommands = overrideExistingCommands;
      return this;
   }

   public boolean isDefaultCommandIsHelp() {
      return this.defaultCommandIsHelp;
   }

   public DrinkCommandContainer setDefaultCommandIsHelp(boolean defaultCommandIsHelp) {
      this.defaultCommandIsHelp = defaultCommandIsHelp;
      return this;
   }
}
