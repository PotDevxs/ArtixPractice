package dev.artixdev.libs.com.mongodb.internal.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.event.ServerHeartbeatFailedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerHeartbeatStartedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerHeartbeatSucceededEvent;
import dev.artixdev.libs.com.mongodb.event.ServerMonitorListener;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;

final class ServerMonitorListenerMulticaster implements ServerMonitorListener {
   private static final Logger LOGGER = Loggers.getLogger("cluster.event");
   private final List<ServerMonitorListener> serverMonitorListeners;

   ServerMonitorListenerMulticaster(List<ServerMonitorListener> serverMonitorListeners) {
      Assertions.isTrue("All ServerMonitorListener instances are non-null", !serverMonitorListeners.contains((Object)null));
      this.serverMonitorListeners = new ArrayList(serverMonitorListeners);
   }

   public void serverHearbeatStarted(ServerHeartbeatStartedEvent event) {
      Iterator var2 = this.serverMonitorListeners.iterator();

      while(var2.hasNext()) {
         ServerMonitorListener cur = (ServerMonitorListener)var2.next();

         try {
            cur.serverHearbeatStarted(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising server heartbeat started event to listener %s", cur), e);
            }
         }
      }

   }

   public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent event) {
      Iterator var2 = this.serverMonitorListeners.iterator();

      while(var2.hasNext()) {
         ServerMonitorListener cur = (ServerMonitorListener)var2.next();

         try {
            cur.serverHeartbeatSucceeded(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising server heartbeat succeeded event to listener %s", cur), e);
            }
         }
      }

   }

   public void serverHeartbeatFailed(ServerHeartbeatFailedEvent event) {
      Iterator var2 = this.serverMonitorListeners.iterator();

      while(var2.hasNext()) {
         ServerMonitorListener cur = (ServerMonitorListener)var2.next();

         try {
            cur.serverHeartbeatFailed(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising server heartbeat failed event to listener %s", cur), e);
            }
         }
      }

   }
}
