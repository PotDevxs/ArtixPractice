package dev.artixdev.libs.net.kyori.adventure.text.serializer.bungeecord;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.GsonBuilder;
import dev.artixdev.libs.com.google.gson.TypeAdapterFactory;
import dev.artixdev.libs.com.google.gson.internal.Excluder;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

final class GsonInjections {
   private GsonInjections() {
   }

   public static Field field(@NotNull Class<?> klass, @NotNull String name) throws NoSuchFieldException {
      Field field = klass.getDeclaredField(name);
      field.setAccessible(true);
      return field;
   }

   public static boolean injectGson(@NotNull Gson existing, @NotNull Consumer<GsonBuilder> accepter) {
      try {
         Field factoriesField = field(Gson.class, "factories");
         Field builderFactoriesField = field(GsonBuilder.class, "factories");
         Field builderHierarchyFactoriesField = field(GsonBuilder.class, "hierarchyFactories");
         GsonBuilder builder = new GsonBuilder();
         accepter.accept(builder);
         List<TypeAdapterFactory> existingFactories = (List)factoriesField.get(existing);
         List<TypeAdapterFactory> newFactories = new ArrayList();
         newFactories.addAll((List)builderFactoriesField.get(builder));
         Collections.reverse(newFactories);
         newFactories.addAll((List)builderHierarchyFactoriesField.get(builder));
         List<TypeAdapterFactory> modifiedFactories = new ArrayList(existingFactories);
         int index = findExcluderIndex(modifiedFactories);
         Collections.reverse(newFactories);
         Iterator var10 = newFactories.iterator();

         while(var10.hasNext()) {
            TypeAdapterFactory newFactory = (TypeAdapterFactory)var10.next();
            modifiedFactories.add(index, newFactory);
         }

         factoriesField.set(existing, modifiedFactories);
         return true;
      } catch (IllegalAccessException | NoSuchFieldException ignored) {
         return false;
      }
   }

   private static int findExcluderIndex(@NotNull List<TypeAdapterFactory> factories) {
      int i = 0;

      for(int size = factories.size(); i < size; ++i) {
         TypeAdapterFactory factory = (TypeAdapterFactory)factories.get(i);
         if (factory instanceof Excluder) {
            return i + 1;
         }
      }

      return 0;
   }
}
