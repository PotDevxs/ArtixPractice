package dev.artixdev.libs.com.mongodb.internal.async;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class ErrorHandlingResultCallback<T> implements SingleResultCallback<T> {
   private final SingleResultCallback<T> wrapped;
   private final Logger logger;

   public static <T> SingleResultCallback<T> errorHandlingCallback(SingleResultCallback<T> callback, Logger logger) {
      return (SingleResultCallback)(callback instanceof ErrorHandlingResultCallback ? callback : new ErrorHandlingResultCallback(callback, logger));
   }

   ErrorHandlingResultCallback(SingleResultCallback<T> wrapped, Logger logger) {
      this.wrapped = (SingleResultCallback)Assertions.notNull("wrapped", wrapped);
      this.logger = (Logger)Assertions.notNull("logger", logger);
   }

   public void onResult(@Nullable T result, @Nullable Throwable t) {
      try {
         this.wrapped.onResult(result, t);
      } catch (Throwable throwable) {
         this.logger.error("Callback onResult call produced an error", throwable);
      }

   }
}
