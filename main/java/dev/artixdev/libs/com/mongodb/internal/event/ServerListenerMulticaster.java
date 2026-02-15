package dev.artixdev.libs.com.mongodb.internal.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.event.ServerClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerListener;
import dev.artixdev.libs.com.mongodb.event.ServerOpeningEvent;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;

final class ServerListenerMulticaster implements ServerListener {
   private static final Logger LOGGER = Loggers.getLogger("cluster.event");
   private final List<ServerListener> serverListeners;

   ServerListenerMulticaster(List<ServerListener> serverListeners) {
      Assertions.isTrue("All ServerListener instances are non-null", !serverListeners.contains((Object)null));
      this.serverListeners = new ArrayList(serverListeners);
   }

   public void serverOpening(ServerOpeningEvent event) {
      Iterator var2 = this.serverListeners.iterator();

      while(var2.hasNext()) {
         ServerListener cur = (ServerListener)var2.next();

         try {
            cur.serverOpening(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising server opening event to listener %s", cur), e);
            }
         }
      }

   }

   public void serverClosed(ServerClosedEvent event) {
      Iterator var2 = this.serverListeners.iterator();

      while(var2.hasNext()) {
         ServerListener cur = (ServerListener)var2.next();

         try {
            cur.serverClosed(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising server opening event to listener %s", cur), e);
            }
         }
      }

   }

   public void serverDescriptionChanged(ServerDescriptionChangedEvent event) {
      Iterator var2 = this.serverListeners.iterator();

      while(var2.hasNext()) {
         ServerListener cur = (ServerListener)var2.next();

         try {
            cur.serverDescriptionChanged(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising server description changed event to listener %s", cur), e);
            }
         }
      }

   }
}
