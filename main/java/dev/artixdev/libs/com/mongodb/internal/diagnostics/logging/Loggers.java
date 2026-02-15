package dev.artixdev.libs.com.mongodb.internal.diagnostics.logging;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public final class Loggers {
   private static final String PREFIX = "org.mongodb.driver";
   private static final boolean USE_SLF4J = shouldUseSLF4J();

   public static Logger getLogger(String suffix) {
      Assertions.notNull("suffix", suffix);
      if (!suffix.startsWith(".") && !suffix.endsWith(".")) {
         String name = "org.mongodb.driver." + suffix;
         return (Logger)(USE_SLF4J ? new SLF4JLogger(name) : new NoOpLogger(name));
      } else {
         throw new IllegalArgumentException("The suffix can not start or end with a '.'");
      }
   }

   private Loggers() {
   }

   private static boolean shouldUseSLF4J() {
      try {
         Class.forName("dev.artixdev.libs.org.slf4j.Logger");
         return true;
      } catch (ClassNotFoundException ignored) {
         java.util.logging.Logger.getLogger("org.mongodb.driver").warning(String.format("SLF4J not found on the classpath.  Logging is disabled for the '%s' component", "org.mongodb.driver"));
         return false;
      }
   }
}
