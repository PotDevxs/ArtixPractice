package dev.artixdev.libs.com.mongodb.internal.logging;

import java.util.concurrent.ConcurrentHashMap;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class StructuredLogger {
   private static final ConcurrentHashMap<String, LoggingInterceptor> INTERCEPTORS = new ConcurrentHashMap();
   private final Logger logger;

   public static void addInterceptor(String clusterDescription, LoggingInterceptor interceptor) {
      INTERCEPTORS.put(clusterDescription, interceptor);
   }

   public static void removeInterceptor(String clusterDescription) {
      INTERCEPTORS.remove(clusterDescription);
   }

   @Nullable
   private static LoggingInterceptor getInterceptor(@Nullable String clusterDescription) {
      return clusterDescription == null ? null : (LoggingInterceptor)INTERCEPTORS.get(clusterDescription);
   }

   public StructuredLogger(String suffix) {
      this(Loggers.getLogger(suffix));
   }

   public StructuredLogger(Logger logger) {
      this.logger = logger;
   }

   public boolean isRequired(LogMessage.Level level, ClusterId clusterId) {
      if (getInterceptor(clusterId.getDescription()) != null) {
         return true;
      } else {
         switch(level) {
         case DEBUG:
            return this.logger.isDebugEnabled();
         default:
            throw new UnsupportedOperationException();
         }
      }
   }

   public void log(LogMessage logMessage) {
      LoggingInterceptor interceptor = getInterceptor(logMessage.getClusterId().getDescription());
      if (interceptor != null) {
         interceptor.intercept(logMessage);
      }

      switch(logMessage.getLevel()) {
      case DEBUG:
         if (this.logger.isDebugEnabled()) {
            LogMessage.UnstructuredLogMessage unstructuredLogMessage = logMessage.toUnstructuredLogMessage();
            String message = unstructuredLogMessage.interpolate();
            Throwable exception = logMessage.getException();
            if (exception == null) {
               this.logger.debug(message);
            } else {
               this.logger.debug(message, exception);
            }
         }

         return;
      default:
         throw new UnsupportedOperationException();
      }
   }
}
