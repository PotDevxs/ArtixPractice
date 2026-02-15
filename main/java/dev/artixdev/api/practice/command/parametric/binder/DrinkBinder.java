package dev.artixdev.api.practice.command.parametric.binder;

import com.google.common.base.Preconditions;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import dev.artixdev.api.practice.command.command.DrinkCommandService;
import dev.artixdev.api.practice.command.parametric.DrinkProvider;
import dev.artixdev.api.practice.command.provider.InstanceProvider;

public class DrinkBinder<T> {
   private final DrinkCommandService commandService;
   private final Class<T> type;
   private final Set<Class<? extends Annotation>> classifiers = new HashSet();
   private DrinkProvider<T> provider;

   public DrinkBinder(DrinkCommandService commandService, Class<T> type) {
      this.commandService = commandService;
      this.type = type;
   }

   public DrinkBinder<T> annotatedWith(Class<? extends Annotation> annotation) {
      Preconditions.checkState(this.commandService.getModifierService().isClassifier(annotation), "Annotation " + annotation.getSimpleName() + " must have @Classifer to be bound");
      this.classifiers.add(annotation);
      return this;
   }

   public void toInstance(T instance) {
      Preconditions.checkNotNull(instance, "Instance cannot be null for toInstance during binding for " + this.type.getSimpleName());
      this.provider = new InstanceProvider(instance);
      this.finish();
   }

   public void toProvider(DrinkProvider<T> provider) {
      Preconditions.checkNotNull(provider, "Provider cannot be null for toProvider during binding for " + this.type.getSimpleName());
      this.provider = provider;
      this.finish();
   }

   private void finish() {
      this.commandService.bindProvider(this.type, this.classifiers, this.provider);
   }
}
