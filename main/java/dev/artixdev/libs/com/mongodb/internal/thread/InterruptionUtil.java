package dev.artixdev.libs.com.mongodb.internal.thread;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.ClosedByInterruptException;
import java.util.Optional;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class InterruptionUtil {
   public static MongoInterruptedException interruptAndCreateMongoInterruptedException(@Nullable String msg, @Nullable InterruptedException cause) {
      Thread.currentThread().interrupt();
      return new MongoInterruptedException(msg, cause);
   }

   public static Optional<MongoInterruptedException> translateInterruptedException(@Nullable Throwable e, @Nullable String message) {
      if (e instanceof InterruptedException) {
         return Optional.of(interruptAndCreateMongoInterruptedException(message, (InterruptedException)e));
      } else {
         return (!(e instanceof InterruptedIOException) || e instanceof SocketTimeoutException) && !(e instanceof ClosedByInterruptException) && (!(e instanceof SocketException) || !Thread.currentThread().isInterrupted()) ? Optional.empty() : Optional.of(new MongoInterruptedException(message, (Exception)e));
      }
   }

   private InterruptionUtil() {
   }
}
