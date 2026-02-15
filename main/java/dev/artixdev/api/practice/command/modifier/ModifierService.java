package dev.artixdev.api.practice.command.modifier;

import com.google.common.base.Preconditions;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import dev.artixdev.api.practice.command.annotation.Classifier;
import dev.artixdev.api.practice.command.annotation.Modifier;
import dev.artixdev.api.practice.command.command.CommandExecution;
import dev.artixdev.api.practice.command.command.DrinkCommandService;
import dev.artixdev.api.practice.command.exception.CommandExitMessage;
import dev.artixdev.api.practice.command.parametric.CommandParameter;

public class ModifierService {
   private final DrinkCommandService commandService;
   private final ConcurrentMap<Class<? extends Annotation>, ModifierContainer> modifiers = new ConcurrentHashMap();

   public ModifierService(DrinkCommandService commandService) {
      this.commandService = commandService;
   }

   public Object executeModifiers(CommandExecution execution, CommandParameter param, Object parsedArgument) throws CommandExitMessage {
      Preconditions.checkNotNull(execution, "CommandExecution cannot be null");
      Preconditions.checkNotNull(param, "CommandParameter cannot be null");
      Iterator var4 = param.getModifierAnnotations().iterator();

      while(true) {
         ModifierContainer container;
         do {
            if (!var4.hasNext()) {
               return parsedArgument;
            }

            Annotation annotation = (Annotation)var4.next();
            container = this.getModifiers(annotation.annotationType());
         } while(container == null);

         DrinkModifier modifier;
         for(Iterator var7 = ((Set)Objects.requireNonNull(container.getModifiersFor(param.getType()))).iterator(); var7.hasNext(); parsedArgument = modifier.modify(execution, param, parsedArgument)) {
            modifier = (DrinkModifier)var7.next();
         }
      }
   }

   public <T> void registerModifier(Class<? extends Annotation> annotation, Class<T> type, DrinkModifier<T> modifier) {
      Preconditions.checkNotNull(annotation, "Annotation cannot be null");
      Preconditions.checkNotNull(type, "Type cannot be null");
      Preconditions.checkNotNull(modifier, "Modifier cannot be null");
      ((Set)((ModifierContainer)this.modifiers.computeIfAbsent(annotation, (a) -> {
         return new ModifierContainer();
      })).getModifiers().computeIfAbsent(type, (t) -> {
         return new HashSet();
      })).add(modifier);
   }

   public ModifierContainer getModifiers(Class<? extends Annotation> annotation) {
      Preconditions.checkNotNull(annotation, "Annotation cannot be null");
      Preconditions.checkState(this.isModifier(annotation), "Annotation provided is not a modifier (annotate with @Modifier) for getModifier: " + annotation.getSimpleName());
      Preconditions.checkState(!this.isClassifier(annotation), "Annotation provided cannot be an @Classifier and an @Modifier: " + annotation.getSimpleName());
      return this.modifiers.containsKey(annotation) ? (ModifierContainer)this.modifiers.get(annotation) : null;
   }

   public boolean isModifier(Class<? extends Annotation> type) {
      return type.isAnnotationPresent(Modifier.class);
   }

   public boolean isClassifier(Class<? extends Annotation> type) {
      return type.isAnnotationPresent(Classifier.class);
   }
}
