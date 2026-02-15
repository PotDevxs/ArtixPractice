package dev.artixdev.libs.net.kyori.adventure.internal.properties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.VisibleForTesting;

final class AdventurePropertiesImpl {
   private static final String FILESYSTEM_DIRECTORY_NAME = "config";
   private static final String FILESYSTEM_FILE_NAME = "adventure.properties";
   private static final Properties PROPERTIES = new Properties();

   private static void print(Throwable ex) {
      ex.printStackTrace();
   }

   private AdventurePropertiesImpl() {
   }

   @VisibleForTesting
   @NotNull
   static String systemPropertyName(String name) {
      return String.join(".", "net", "kyori", "adventure", name);
   }

   @NotNull
   static <T> AdventureProperties.Property<T> property(@NotNull String name, @NotNull Function<String, T> parser, @Nullable T defaultValue) {
      return new AdventurePropertiesImpl.PropertyImpl(name, parser, defaultValue);
   }

   static {
      Path path = (Path)Optional.ofNullable(System.getProperty(systemPropertyName("config"))).map((x$0) -> {
         return Paths.get(x$0);
      }).orElseGet(() -> {
         return Paths.get("config", "adventure.properties");
      });
      if (Files.isRegularFile(path, new LinkOption[0])) {
         try {
            InputStream is = Files.newInputStream(path);

            try {
               PROPERTIES.load(is);
            } catch (Throwable e) {
               if (is != null) {
                  try {
                     is.close();
                  } catch (Throwable suppressed) {
                     e.addSuppressed(suppressed);
                  }
               }

               throw e;
            }

            if (is != null) {
               is.close();
            }
         } catch (IOException e) {
            print(e);
         }
      }

   }

   private static final class PropertyImpl<T> implements AdventureProperties.Property<T> {
      private final String name;
      private final Function<String, T> parser;
      @Nullable
      private final T defaultValue;
      private boolean valueCalculated;
      @Nullable
      private T value;

      PropertyImpl(@NotNull String name, @NotNull Function<String, T> parser, @Nullable T defaultValue) {
         this.name = name;
         this.parser = parser;
         this.defaultValue = defaultValue;
      }

      @Nullable
      public T value() {
         if (!this.valueCalculated) {
            String property = AdventurePropertiesImpl.systemPropertyName(this.name);
            String value = System.getProperty(property, AdventurePropertiesImpl.PROPERTIES.getProperty(this.name));
            if (value != null) {
               this.value = this.parser.apply(value);
            }

            if (this.value == null) {
               this.value = this.defaultValue;
            }

            this.valueCalculated = true;
         }

         return this.value;
      }

      public boolean equals(@Nullable Object that) {
         return this == that;
      }

      public int hashCode() {
         return this.name.hashCode();
      }
   }
}
