package dev.artixdev.api.practice.command.command;

import java.lang.annotation.Annotation;
import dev.artixdev.api.practice.command.modifier.DrinkModifier;
import dev.artixdev.api.practice.command.parametric.binder.DrinkBinder;

public interface CommandService {
   DrinkCommandContainer register(Object var1, String var2, String... var3);

   DrinkCommandContainer registerSub(DrinkCommandContainer var1, Object var2);

   void registerCommands();

   void registerPermissions();

   void registerCommands(String var1) throws InstantiationException, IllegalAccessException;

   <T> DrinkBinder<T> bind(Class<T> var1);

   <T> void registerModifier(Class<? extends Annotation> var1, Class<T> var2, DrinkModifier<T> var3);

   DrinkCommandContainer get(String var1);

   void setAuthorizer(DrinkAuthorizer var1);
}
