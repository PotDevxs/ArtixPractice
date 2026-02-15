package dev.artixdev.api.practice.command.parametric;

import java.util.Iterator;
import dev.artixdev.api.practice.command.command.DrinkCommand;
import dev.artixdev.api.practice.command.command.DrinkCommandService;
import dev.artixdev.api.practice.command.exception.CommandStructureException;
import dev.artixdev.api.practice.command.exception.MissingProviderException;

public class ProviderAssigner {
   private final DrinkCommandService commandService;

   public ProviderAssigner(DrinkCommandService commandService) {
      this.commandService = commandService;
   }

   public DrinkProvider<?>[] assignProvidersFor(DrinkCommand drinkCommand) throws MissingProviderException, CommandStructureException {
      CommandParameters parameters = drinkCommand.getParameters();
      DrinkProvider<?>[] providers = new DrinkProvider[parameters.getParameters().length];

      for(int i = 0; i < parameters.getParameters().length; ++i) {
         CommandParameter param = parameters.getParameters()[i];
         if (param.isRequireLastArg() && !parameters.isLastArgument(i)) {
            throw new CommandStructureException("Parameter " + param.getParameter().getName() + " [argument " + i + "] (" + param.getParameter().getType().getSimpleName() + ") in method '" + drinkCommand.getMethod().getName() + "' must be the last argument in the method.");
         }

         BindingContainer<?> bindings = this.commandService.getBindingsFor(param.getType());
         if (bindings == null) {
            throw new MissingProviderException("No provider bound for " + param.getType().getSimpleName());
         }

         DrinkProvider<?> provider = null;
         Iterator var8 = bindings.getBindings().iterator();

         while(var8.hasNext()) {
            DrinkBinding<?> binding = (DrinkBinding)var8.next();
            if (binding.canProvideFor(param)) {
               provider = binding.getProvider();
               break;
            }
         }

         if (provider == null) {
            throw new MissingProviderException("No provider bound for " + param.getType().getSimpleName() + " for parameter " + i + " for method " + drinkCommand.getMethod().getName());
         }

         providers[i] = provider;
      }

      return providers;
   }
}
