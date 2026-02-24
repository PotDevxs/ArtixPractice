package dev.artixdev.api.practice.command.parametric;

import com.google.common.base.Preconditions;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Set;

public class DrinkBinding<T> {
   private final Class<T> type;
   private final Set<Class<? extends Annotation>> annotations;
   private final DrinkProvider<T> provider;

   public DrinkBinding(Class<T> type, Set<Class<? extends Annotation>> annotations, DrinkProvider<T> provider) {
      this.type = type;
      this.annotations = annotations;
      this.provider = provider;
   }

   public boolean canProvideFor(CommandParameter parameter) {
      Preconditions.checkNotNull(parameter, "Parameter cannot be null");
      Iterator var2 = parameter.getClassifierAnnotations().iterator();

      Annotation c;
      do {
         if (!var2.hasNext()) {
            var2 = this.annotations.iterator();

            while(var2.hasNext()) {
               final Class<? extends Annotation> annotation = (Class<? extends Annotation>)var2.next();
               if (!parameter.getClassifierAnnotations().stream().anyMatch((a) -> {
                  return a.annotationType().equals(annotation);
               })) {
                  return false;
               }
            }

            return true;
         }

         c = (Annotation)var2.next();
      } while(this.annotations.contains(c.annotationType()));

      return false;
   }

   public Class<T> getType() {
      return this.type;
   }

   public Set<Class<? extends Annotation>> getAnnotations() {
      return this.annotations;
   }

   public DrinkProvider<T> getProvider() {
      return this.provider;
   }
}
