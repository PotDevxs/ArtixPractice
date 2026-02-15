package dev.artixdev.libs.com.mongodb.event;

public interface CommandListener {
   default void commandStarted(CommandStartedEvent event) {
   }

   default void commandSucceeded(CommandSucceededEvent event) {
   }

   default void commandFailed(CommandFailedEvent event) {
   }
}
