package dev.artixdev.api.practice.command.modifier;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ModifierContainer {
   private final ConcurrentMap<Class<?>, Set<DrinkModifier<?>>> modifiers = new ConcurrentHashMap();

   public Set<DrinkModifier<?>> getModifiersFor(Class<?> type) {
      if (this.modifiers.containsKey(type)) {
         return (Set)this.modifiers.get(type);
      } else {
         Iterator var2 = this.modifiers.keySet().iterator();

         Class modifierType;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            modifierType = (Class)var2.next();
         } while(!modifierType.isAssignableFrom(type) && !type.isAssignableFrom(modifierType));

         return (Set)this.modifiers.get(modifierType);
      }
   }

   public ConcurrentMap<Class<?>, Set<DrinkModifier<?>>> getModifiers() {
      return this.modifiers;
   }
}
