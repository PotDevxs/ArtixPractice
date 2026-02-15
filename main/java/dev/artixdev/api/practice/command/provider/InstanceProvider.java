package dev.artixdev.api.practice.command.provider;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;

public class InstanceProvider<T> extends DrinkProvider<T> {
   private final T instance;

   public InstanceProvider(T instance) {
      this.instance = instance;
   }

   public boolean doesConsumeArgument() {
      return false;
   }

   public boolean isAsync() {
      return false;
   }

   public T provide(CommandArg arg, List<? extends Annotation> annotations) throws CommandExitMessage {
      return this.instance;
   }

   public String argumentDescription() {
      return this.instance.getClass().getSimpleName() + " (provided)";
   }

   public List<String> getSuggestions(String prefix) {
      return Collections.emptyList();
   }
}
