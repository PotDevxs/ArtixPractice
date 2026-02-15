package dev.artixdev.api.practice.command.command;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import dev.artixdev.api.practice.command.exception.CommandStructureException;
import dev.artixdev.api.practice.command.exception.MissingProviderException;
import dev.artixdev.api.practice.command.parametric.CommandParameter;
import dev.artixdev.api.practice.command.parametric.CommandParameters;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class DrinkCommand {
   private final DrinkCommandService commandService;
   private final String name;
   private final Set<String> allAliases;
   private final Set<String> aliases;
   private final String description;
   private final String usage;
   private final String permission;
   private final Object handler;
   private final Method method;
   private final CommandParameters parameters;
   private final DrinkProvider<?>[] providers;
   private final DrinkProvider<?>[] consumingProviders;
   private final int consumingArgCount;
   private final int requiredArgCount;
   private final boolean requiresAsync;
   private final String generatedUsage;

   public DrinkCommand(DrinkCommandService commandService, String name, Set<String> aliases, String description, String usage, String permission, Object handler, Method method) throws MissingProviderException, CommandStructureException {
      this.commandService = commandService;
      this.name = name;
      this.aliases = aliases;
      this.description = description;
      this.usage = usage;
      this.permission = permission;
      this.handler = handler;
      this.method = method;
      this.parameters = new CommandParameters(method);
      this.providers = commandService.getProviderAssigner().assignProvidersFor(this);
      this.consumingArgCount = this.calculateConsumingArgCount();
      this.requiredArgCount = this.calculateRequiredArgCount();
      this.consumingProviders = this.calculateConsumingProviders();
      this.requiresAsync = this.calculateRequiresAsync();
      this.generatedUsage = this.generateUsage();
      this.allAliases = aliases;
      if (name.length() > 0 && !name.equals(DrinkCommandService.DEFAULT_KEY)) {
         this.allAliases.add(name);
      }

   }

   public String getMostApplicableUsage() {
      return this.usage.length() > 0 ? this.usage : this.generatedUsage;
   }

   public String getShortDescription() {
      return this.description.length() > 24 ? this.description.substring(0, 21) + "..." : this.description;
   }

   private String generateUsage() {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < this.parameters.getParameters().length; ++i) {
         CommandParameter parameter = this.parameters.getParameters()[i];
         DrinkProvider<?> provider = this.providers[i];
         if (parameter.isFlag()) {
            sb.append("-").append(parameter.getFlag().value()).append(" ");
         } else if (provider.doesConsumeArgument()) {
            if (parameter.isOptional()) {
               sb.append("[").append(provider.argumentDescription());
               if (parameter.isText()) {
                  sb.append("...");
               }

               if (parameter.getDefaultOptionalValue() != null && parameter.getDefaultOptionalValue().length() > 0) {
                  sb.append(" = ").append(parameter.getDefaultOptionalValue());
               }

               sb.append("]");
            } else {
               sb.append("§7<").append(provider.argumentDescription());
               if (parameter.isText()) {
                  sb.append("...");
               }

               sb.append("§7>");
            }

            sb.append(" ");
         }
      }

      return sb.toString();
   }

   private boolean calculateRequiresAsync() {
      DrinkProvider[] var1 = this.providers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         DrinkProvider<?> provider = var1[var3];
         if (provider.isAsync()) {
            return true;
         }
      }

      return false;
   }

   private DrinkProvider<?>[] calculateConsumingProviders() {
      DrinkProvider<?>[] consumingProviders = new DrinkProvider[this.consumingArgCount];
      int x = 0;
      DrinkProvider[] var3 = this.providers;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         DrinkProvider<?> provider = var3[var5];
         if (provider.doesConsumeArgument()) {
            consumingProviders[x] = provider;
            ++x;
         }
      }

      return consumingProviders;
   }

   private int calculateConsumingArgCount() {
      int count = 0;
      DrinkProvider[] var2 = this.providers;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         DrinkProvider<?> provider = var2[var4];
         if (provider.doesConsumeArgument()) {
            ++count;
         }
      }

      return count;
   }

   private int calculateRequiredArgCount() {
      int count = 0;

      for(int i = 0; i < this.parameters.getParameters().length; ++i) {
         CommandParameter parameter = this.parameters.getParameters()[i];
         if (!parameter.isFlag() && !parameter.isOptional()) {
            DrinkProvider<?> provider = this.providers[i];
            if (provider.doesConsumeArgument()) {
               ++count;
            }
         }
      }

      return count;
   }

   public List<String> getLoweredAliases() {
      return (List)this.aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
   }

   public DrinkCommandService getCommandService() {
      return this.commandService;
   }

   public String getName() {
      return this.name;
   }

   public Set<String> getAllAliases() {
      return this.allAliases;
   }

   public Set<String> getAliases() {
      return this.aliases;
   }

   public String getDescription() {
      return this.description;
   }

   public String getUsage() {
      return this.usage;
   }

   public String getPermission() {
      return this.permission;
   }

   public Object getHandler() {
      return this.handler;
   }

   public Method getMethod() {
      return this.method;
   }

   public CommandParameters getParameters() {
      return this.parameters;
   }

   public DrinkProvider<?>[] getProviders() {
      return this.providers;
   }

   public DrinkProvider<?>[] getConsumingProviders() {
      return this.consumingProviders;
   }

   public int getConsumingArgCount() {
      return this.consumingArgCount;
   }

   public int getRequiredArgCount() {
      return this.requiredArgCount;
   }

   public boolean isRequiresAsync() {
      return this.requiresAsync;
   }

   public String getGeneratedUsage() {
      return this.generatedUsage;
   }
}
