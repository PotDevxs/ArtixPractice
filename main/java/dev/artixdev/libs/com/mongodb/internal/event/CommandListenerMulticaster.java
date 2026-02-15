package dev.artixdev.libs.com.mongodb.internal.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.event.CommandFailedEvent;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.event.CommandStartedEvent;
import dev.artixdev.libs.com.mongodb.event.CommandSucceededEvent;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;

final class CommandListenerMulticaster implements CommandListener {
   private static final Logger LOGGER = Loggers.getLogger("protocol.event");
   private final List<CommandListener> commandListeners;

   CommandListenerMulticaster(List<CommandListener> commandListeners) {
      Assertions.isTrue("All CommandListener instances are non-null", !commandListeners.contains((Object)null));
      this.commandListeners = new ArrayList(commandListeners);
   }

   public void commandStarted(CommandStartedEvent event) {
      Iterator var2 = this.commandListeners.iterator();

      while(var2.hasNext()) {
         CommandListener cur = (CommandListener)var2.next();

         try {
            cur.commandStarted(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising command started event to listener %s", cur), e);
            }
         }
      }

   }

   public void commandSucceeded(CommandSucceededEvent event) {
      Iterator var2 = this.commandListeners.iterator();

      while(var2.hasNext()) {
         CommandListener cur = (CommandListener)var2.next();

         try {
            cur.commandSucceeded(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising command succeeded event to listener %s", cur), e);
            }
         }
      }

   }

   public void commandFailed(CommandFailedEvent event) {
      Iterator var2 = this.commandListeners.iterator();

      while(var2.hasNext()) {
         CommandListener cur = (CommandListener)var2.next();

         try {
            cur.commandFailed(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising command failed event to listener %s", cur), e);
            }
         }
      }

   }
}
