package dev.artixdev.libs.net.kyori.adventure.util;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;
import dev.artixdev.libs.net.kyori.adventure.internal.properties.AdventureProperties;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public final class Services {
   private static final boolean SERVICE_LOAD_FAILURES_ARE_FATAL;

   private Services() {
   }

   @NotNull
   public static <P> Optional<P> service(@NotNull Class<P> type) {
      ServiceLoader<P> loader = Services0.loader(type);
      Iterator<P> it = loader.iterator();

      while(true) {
         if (it.hasNext()) {
            P instance;
            try {
               instance = it.next();
            } catch (Throwable e) {
               if (!SERVICE_LOAD_FAILURES_ARE_FATAL) {
                  continue;
               }

               throw new IllegalStateException("Encountered an exception loading service " + type, e);
            }

            if (it.hasNext()) {
               throw new IllegalStateException("Expected to find one service " + type + ", found multiple");
            }

            return Optional.of(instance);
         }

         return Optional.empty();
      }
   }

   @NotNull
   public static <P> Optional<P> serviceWithFallback(@NotNull Class<P> type) {
      ServiceLoader<P> loader = Services0.loader(type);
      Iterator<P> it = loader.iterator();
      P firstFallback = null;

      while(it.hasNext()) {
         P instance;
         try {
            instance = it.next();
         } catch (Throwable e) {
            if (!SERVICE_LOAD_FAILURES_ARE_FATAL) {
               continue;
            }

            throw new IllegalStateException("Encountered an exception loading service " + type, e);
         }

         if (!(instance instanceof Services.Fallback)) {
            return Optional.of(instance);
         }

         if (firstFallback == null) {
            firstFallback = instance;
         }
      }

      return Optional.ofNullable(firstFallback);
   }

   static {
      SERVICE_LOAD_FAILURES_ARE_FATAL = Boolean.TRUE.equals(AdventureProperties.SERVICE_LOAD_FAILURES_ARE_FATAL.value());
   }

   public interface Fallback {
   }
}
