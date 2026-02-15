package dev.artixdev.api.practice.command.parametric;

import java.lang.annotation.Annotation;
import java.util.List;
import dev.artixdev.api.practice.command.argument.CommandArg;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;

public abstract class DrinkProvider<T> {
   public abstract boolean doesConsumeArgument();

   public abstract boolean isAsync();

   public boolean allowNullArgument() {
      return true;
   }

   public T defaultNullValue() {
      return null;
   }

   public abstract T provide(CommandArg var1, List<? extends Annotation> var2) throws CommandExitMessage;

   public abstract String argumentDescription();

   public abstract List<String> getSuggestions(String var1);

   protected boolean hasAnnotation(List<? extends Annotation> list, Class<? extends Annotation> a) {
      return list.stream().anyMatch((annotation) -> {
         return annotation.annotationType().equals(a);
      });
   }
}
