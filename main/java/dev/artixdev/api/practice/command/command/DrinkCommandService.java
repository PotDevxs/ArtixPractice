package dev.artixdev.api.practice.command.command;

import com.google.common.base.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.command.annotation.Duration;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.api.practice.command.annotation.Text;
import dev.artixdev.api.practice.command.argument.ArgumentParser;
import dev.artixdev.api.practice.command.argument.CommandArgs;
import dev.artixdev.api.practice.command.exception.CommandArgumentException;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.exception.CommandRegistrationException;
import dev.artixdev.api.practice.command.exception.CommandStructureException;
import dev.artixdev.api.practice.command.exception.DrinkException;
import dev.artixdev.api.practice.command.exception.MissingProviderException;
import dev.artixdev.api.practice.command.modifier.DrinkModifier;
import dev.artixdev.api.practice.command.modifier.ModifierService;
import dev.artixdev.api.practice.command.parametric.BindingContainer;
import dev.artixdev.api.practice.command.parametric.DrinkBinding;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;
import dev.artixdev.api.practice.command.parametric.ProviderAssigner;
import dev.artixdev.api.practice.command.parametric.binder.DrinkBinder;
import dev.artixdev.api.practice.command.provider.BooleanProvider;
import dev.artixdev.api.practice.command.provider.CommandArgsProvider;
import dev.artixdev.api.practice.command.provider.DateProvider;
import dev.artixdev.api.practice.command.provider.DoubleProvider;
import dev.artixdev.api.practice.command.provider.DurationProvider;
import dev.artixdev.api.practice.command.provider.IntegerProvider;
import dev.artixdev.api.practice.command.provider.LongProvider;
import dev.artixdev.api.practice.command.provider.StringProvider;
import dev.artixdev.api.practice.command.provider.TextProvider;
import dev.artixdev.api.practice.command.provider.spigot.CommandSenderProvider;
import dev.artixdev.api.practice.command.provider.spigot.PlayerProvider;
import dev.artixdev.api.practice.command.provider.spigot.PlayerSenderProvider;
import dev.artixdev.api.practice.command.util.ClassUtil;
import dev.artixdev.api.practice.command.util.CommandIgnore;

public class DrinkCommandService implements CommandService {
   public static String DEFAULT_KEY = "DRINK_DEFAULT";
   private final JavaPlugin plugin;
   private final CommandExtractor extractor;
   private final DrinkHelpService helpService;
   private final ProviderAssigner providerAssigner;
   private final ArgumentParser argumentParser;
   private final ModifierService modifierService;
   private final DrinkSpigotRegistry spigotRegistry;
   private final FlagExtractor flagExtractor;
   private DrinkAuthorizer authorizer;
   private final ConcurrentMap<String, DrinkCommandContainer> commands = new ConcurrentHashMap();
   private final ConcurrentMap<Class<?>, BindingContainer<?>> bindings = new ConcurrentHashMap();

   public DrinkCommandService(JavaPlugin plugin) {
      this.plugin = plugin;
      this.extractor = new CommandExtractor(this);
      this.helpService = new DrinkHelpService(this);
      this.providerAssigner = new ProviderAssigner(this);
      this.argumentParser = new ArgumentParser(this);
      this.modifierService = new ModifierService(this);
      this.spigotRegistry = new DrinkSpigotRegistry();
      this.flagExtractor = new FlagExtractor(this);
      this.authorizer = new DrinkAuthorizer();
      this.bindDefaults();
   }

   private void bindDefaults() {
      this.bind(Boolean.class).toProvider(BooleanProvider.INSTANCE);
      this.bind(Boolean.TYPE).toProvider(BooleanProvider.INSTANCE);
      this.bind(Double.class).toProvider(DoubleProvider.INSTANCE);
      this.bind(Double.TYPE).toProvider(DoubleProvider.INSTANCE);
      this.bind(Integer.class).toProvider(IntegerProvider.INSTANCE);
      this.bind(Integer.TYPE).toProvider(IntegerProvider.INSTANCE);
      this.bind(Long.class).toProvider(LongProvider.INSTANCE);
      this.bind(Long.TYPE).toProvider(LongProvider.INSTANCE);
      this.bind(String.class).toProvider(StringProvider.INSTANCE);
      this.bind(String.class).annotatedWith(Text.class).toProvider(TextProvider.INSTANCE);
      this.bind(Date.class).toProvider(DateProvider.INSTANCE);
      this.bind(Date.class).annotatedWith(Duration.class).toProvider(DurationProvider.INSTANCE);
      this.bind(CommandArgs.class).toProvider(CommandArgsProvider.INSTANCE);
      this.bind(CommandSender.class).annotatedWith(Sender.class).toProvider(CommandSenderProvider.INSTANCE);
      this.bind(Player.class).annotatedWith(Sender.class).toProvider(PlayerSenderProvider.INSTANCE);
      this.bind(Player.class).toProvider(new PlayerProvider(this.plugin));
   }

   public void setAuthorizer(DrinkAuthorizer authorizer) {
      Preconditions.checkNotNull(authorizer, "Authorizer cannot be null");
      this.authorizer = authorizer;
   }

   public void registerCommands() {
      this.commands.values().forEach((cmd) -> {
         this.spigotRegistry.register(cmd, cmd.isOverrideExistingCommands());
      });
   }

   public void registerPermissions() {
      Iterator var1 = this.commands.values().iterator();

      while(var1.hasNext()) {
         DrinkCommandContainer container = (DrinkCommandContainer)var1.next();
         container.getCommands().values().forEach((cmd) -> {
            if (cmd.getPermission() != null) {
               PluginManager pluginManager = this.plugin.getServer().getPluginManager();
               Permission permission = new Permission(cmd.getPermission(), "A permission registered by Refine CommandAPI", PermissionDefault.OP);
               if (pluginManager.getPermissions().stream().noneMatch((perm) -> {
                  return perm.getName().equalsIgnoreCase(permission.getName());
               })) {
                  pluginManager.addPermission(permission);
               }
            }

         });
      }

   }

   public void registerCommands(String packageName) throws InstantiationException, IllegalAccessException {
      Iterator var2 = ClassUtil.getClassesInPackage(this.plugin, packageName).iterator();

      while(var2.hasNext()) {
         Class<?> clazz = (Class)var2.next();
         if (!clazz.isInstance(DrinkProvider.class) && clazz.getAnnotation(Register.class) != null && clazz.getAnnotation(CommandIgnore.class) == null) {
            Register annotation = (Register)clazz.getAnnotation(Register.class);
            Object instance = clazz.newInstance();
            this.register(instance, annotation.name(), annotation.aliases());
         }
      }

   }

   public DrinkCommandContainer register(Object handler, String name, String... aliases) throws CommandRegistrationException {
      Preconditions.checkNotNull(handler, "Handler object cannot be null");
      Preconditions.checkNotNull(name, "Name cannot be null");
      Preconditions.checkState(name.length() > 0, "Name cannot be empty (must be > 0 characters in length)");
      Set<String> aliasesSet = new HashSet();
      if (aliases != null) {
         aliasesSet.addAll(Arrays.asList(aliases));
         aliasesSet.removeIf((s) -> {
            return s.length() == 0;
         });
      }

      try {
         Map<String, DrinkCommand> extractCommands = this.extractor.extractCommands(handler);
         if (extractCommands.isEmpty()) {
            throw new CommandRegistrationException("There were no cmds to init in the " + handler.getClass().getSimpleName() + " class");
         } else {
            DrinkCommandContainer container = new DrinkCommandContainer(this, handler, name, aliasesSet, extractCommands);
            this.commands.put(this.getCommandKey(name), container);
            return container;
         }
      } catch (CommandStructureException | MissingProviderException e) {
         throw new CommandRegistrationException("Could not init command '" + name + "': " + e.getMessage(), e);
      }
   }

   public DrinkCommandContainer registerSub(DrinkCommandContainer root, Object handler) {
      Preconditions.checkNotNull(root, "Root command container cannot be null");
      Preconditions.checkNotNull(handler, "Handler object cannot be null");

      try {
         Map<String, DrinkCommand> extractCommands = this.extractor.extractCommands(handler);
         extractCommands.forEach((s, d) -> {
            DrinkCommand var10000 = (DrinkCommand)root.getCommands().put(s, d);
         });
         return root;
      } catch (CommandStructureException | MissingProviderException e) {
         throw new CommandRegistrationException("Could not init sub-command in root '" + root + "' with handler '" + handler.getClass().getSimpleName() + "': " + e.getMessage(), e);
      }
   }

   public <T> void registerModifier(Class<? extends Annotation> annotation, Class<T> type, DrinkModifier<T> modifier) {
      this.modifierService.registerModifier(annotation, type, modifier);
   }

   void executeCommand(CommandSender sender, DrinkCommand command, String label, String[] args) {
      Preconditions.checkNotNull(sender, "Sender cannot be null");
      Preconditions.checkNotNull(command, "Command cannot be null");
      Preconditions.checkNotNull(label, "Label cannot be null");
      Preconditions.checkNotNull(args, "Args cannot be null");
      if (this.authorizer.isAuthorized(sender, command)) {
         if (command.isRequiresAsync()) {
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
               this.finishExecution(sender, command, label, args);
            });
         } else {
            this.finishExecution(sender, command, label, args);
         }
      }

   }

   private void finishExecution(CommandSender sender, DrinkCommand command, String label, String[] args) {
      ArrayList argList = new ArrayList(Arrays.asList(args));

      try {
         argList = new ArrayList(this.argumentParser.combineMultiWordArguments(argList));
         Map<Character, CommandFlag> flags = this.flagExtractor.extractFlags(argList);
         CommandArgs commandArgs = new CommandArgs(this, sender, label, argList, flags);
         CommandExecution execution = new CommandExecution(this, sender, argList, commandArgs, command);
         Object[] parsedArguments = this.argumentParser.parseArguments(execution, command, commandArgs);
         if (!execution.isCanExecute()) {
            return;
         }

         try {
            command.getMethod().invoke(command.getHandler(), parsedArguments);
         } catch (InvocationTargetException | IllegalAccessException e) {
            sender.sendMessage(ChatColor.RED + "Could not perform command.  Notify an administrator");
            throw new DrinkException("Failed to execute command '" + command.getName() + "' with arguments '" + StringUtils.join(Collections.singletonList(args), ' ') + " for sender " + sender.getName(), e);
         }
      } catch (CommandExitMessage e) {
         sender.sendMessage(ChatColor.RED + e.getMessage());
      } catch (CommandArgumentException e) {
         sender.sendMessage(ChatColor.RED + e.getMessage());
         this.helpService.sendUsageMessage(sender, this.getContainerFor(command), command);
      }

   }

   public DrinkCommandContainer getContainerFor(DrinkCommand command) {
      Preconditions.checkNotNull(command, "DrinkCommand cannot be null");
      Iterator var2 = this.commands.values().iterator();

      DrinkCommandContainer container;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         container = (DrinkCommandContainer)var2.next();
      } while(!container.getCommands().containsValue(command));

      return container;
   }

   public <T> BindingContainer<T> getBindingsFor(Class<T> type) {
      Preconditions.checkNotNull(type, "Type cannot be null");
      return this.bindings.containsKey(type) ? (BindingContainer)this.bindings.get(type) : null;
   }

   public DrinkCommandContainer get(String name) {
      Preconditions.checkNotNull(name, "Name cannot be null");
      return (DrinkCommandContainer)this.commands.get(this.getCommandKey(name));
   }

   public String getCommandKey(String name) {
      Preconditions.checkNotNull(name, "Name cannot be null");
      return name.length() == 0 ? DEFAULT_KEY : name.toLowerCase();
   }

   public <T> DrinkBinder<T> bind(Class<T> type) {
      Preconditions.checkNotNull(type, "Type cannot be null for bind");
      return new DrinkBinder(this, type);
   }

   public <T> void bindProvider(Class<T> type, Set<Class<? extends Annotation>> annotations, DrinkProvider<T> provider) {
      Preconditions.checkNotNull(type, "Type cannot be null");
      Preconditions.checkNotNull(annotations, "Annotations cannot be null");
      Preconditions.checkNotNull(provider, "Provider cannot be null");
      BindingContainer<T> container = this.getBindingsFor(type);
      if (container == null) {
         container = new BindingContainer(type);
         this.bindings.put(type, container);
      }

      DrinkBinding<T> binding = new DrinkBinding(type, annotations, provider);
      container.getBindings().add(binding);
   }

   public JavaPlugin getPlugin() {
      return this.plugin;
   }

   public CommandExtractor getExtractor() {
      return this.extractor;
   }

   public DrinkHelpService getHelpService() {
      return this.helpService;
   }

   public ProviderAssigner getProviderAssigner() {
      return this.providerAssigner;
   }

   public ArgumentParser getArgumentParser() {
      return this.argumentParser;
   }

   public ModifierService getModifierService() {
      return this.modifierService;
   }

   public DrinkSpigotRegistry getSpigotRegistry() {
      return this.spigotRegistry;
   }

   public FlagExtractor getFlagExtractor() {
      return this.flagExtractor;
   }

   public DrinkAuthorizer getAuthorizer() {
      return this.authorizer;
   }

   public ConcurrentMap<String, DrinkCommandContainer> getCommands() {
      return this.commands;
   }

   public ConcurrentMap<Class<?>, BindingContainer<?>> getBindings() {
      return this.bindings;
   }
}
