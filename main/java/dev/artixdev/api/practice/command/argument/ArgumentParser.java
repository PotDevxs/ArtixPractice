package dev.artixdev.api.practice.command.argument;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import dev.artixdev.api.practice.command.annotation.Flag;
import dev.artixdev.api.practice.command.command.CommandExecution;
import dev.artixdev.api.practice.command.command.CommandFlag;
import dev.artixdev.api.practice.command.command.DrinkCommand;
import dev.artixdev.api.practice.command.command.DrinkCommandService;
import dev.artixdev.api.practice.command.exception.CommandArgumentException;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.CommandParameter;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class ArgumentParser {
   private final DrinkCommandService commandService;

   public ArgumentParser(DrinkCommandService commandService) {
      this.commandService = commandService;
   }

   public List<String> combineMultiWordArguments(List<String> args) {
      List<String> argList = new ArrayList(args.size());

      for(int i = 0; i < args.size(); ++i) {
         String arg = (String)args.get(i);
         if (!arg.isEmpty()) {
            char c = arg.charAt(0);
            if (c == '"' || c == '\'') {
               StringBuilder builder = new StringBuilder();

               int endIndex;
               for(endIndex = i; endIndex < args.size(); ++endIndex) {
                  String arg2 = (String)args.get(endIndex);
                  if (arg2.charAt(arg2.length() - 1) == c && arg2.length() > 1) {
                     if (endIndex != i) {
                        builder.append(' ');
                     }

                     builder.append(arg2.substring(endIndex == i ? 1 : 0, arg2.length() - 1));
                     break;
                  }

                  if (endIndex == i) {
                     builder.append(arg2.substring(1));
                  } else {
                     builder.append(' ').append(arg2);
                  }
               }

               if (endIndex < args.size()) {
                  arg = builder.toString();
                  i = endIndex;
               }
            }
         }

         if (!arg.isEmpty()) {
            argList.add(arg);
         }
      }

      return argList;
   }

   public Object[] parseArguments(CommandExecution execution, DrinkCommand command, CommandArgs args) throws CommandExitMessage, CommandArgumentException {
      Preconditions.checkNotNull(execution, "CommandExecution cannot be null");
      Preconditions.checkNotNull(command, "DrinkCommand cannot be null");
      Preconditions.checkNotNull(args, "CommandArgs cannot be null");
      Object[] arguments = new Object[command.getMethod().getParameterCount()];

      for(int i = 0; i < command.getParameters().getParameters().length; ++i) {
         CommandParameter param = command.getParameters().getParameters()[i];
         boolean skipOptional = false;
         DrinkProvider<?> provider = command.getProviders()[i];
         String value = null;
         if (param.isFlag()) {
            Flag flag = param.getFlag();
            CommandFlag commandFlag = (CommandFlag)args.getFlags().get(flag.value());
            if (commandFlag != null) {
               value = commandFlag.getValue();
            } else {
               value = null;
            }
         } else {
            if (!args.hasNext() && provider.doesConsumeArgument()) {
               if (!param.isOptional()) {
                  throw new CommandArgumentException("Missing argument for: " + provider.argumentDescription());
               }

               String defaultValue = param.getDefaultOptionalValue();
               if (defaultValue != null && defaultValue.length() > 0) {
                  value = defaultValue;
               } else {
                  skipOptional = true;
               }
            }

            if (provider.doesConsumeArgument() && value == null && args.hasNext()) {
               value = args.next();
            }

            if (provider.doesConsumeArgument() && value == null && !skipOptional) {
               throw new CommandArgumentException("Argument already consumed for next argument: " + provider.argumentDescription() + " (this is a provider error!)");
            }
         }

         if (param.isFlag() && !param.getType().isAssignableFrom(Boolean.class) && !param.getType().isAssignableFrom(Boolean.TYPE) && value == null && !provider.allowNullArgument()) {
            arguments[i] = provider.defaultNullValue();
         } else if (!skipOptional) {
            Object o = provider.provide(new CommandArg(args.getSender(), value, args), param.getAllAnnotations());
            o = this.commandService.getModifierService().executeModifiers(execution, param, o);
            arguments[i] = o;
         } else {
            arguments[i] = null;
         }
      }

      return arguments;
   }
}
