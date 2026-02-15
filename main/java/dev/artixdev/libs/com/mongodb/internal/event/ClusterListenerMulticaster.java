package dev.artixdev.libs.com.mongodb.internal.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.event.ClusterClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ClusterDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.event.ClusterListener;
import dev.artixdev.libs.com.mongodb.event.ClusterOpeningEvent;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;

final class ClusterListenerMulticaster implements ClusterListener {
   private static final Logger LOGGER = Loggers.getLogger("cluster.event");
   private final List<ClusterListener> clusterListeners;

   ClusterListenerMulticaster(List<ClusterListener> clusterListeners) {
      Assertions.isTrue("All ClusterListener instances are non-null", !clusterListeners.contains((Object)null));
      this.clusterListeners = new ArrayList(clusterListeners);
   }

   public void clusterOpening(ClusterOpeningEvent event) {
      Iterator var2 = this.clusterListeners.iterator();

      while(var2.hasNext()) {
         ClusterListener cur = (ClusterListener)var2.next();

         try {
            cur.clusterOpening(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising cluster opening event to listener %s", cur), e);
            }
         }
      }

   }

   public void clusterClosed(ClusterClosedEvent event) {
      Iterator var2 = this.clusterListeners.iterator();

      while(var2.hasNext()) {
         ClusterListener cur = (ClusterListener)var2.next();

         try {
            cur.clusterClosed(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising cluster closed event to listener %s", cur), e);
            }
         }
      }

   }

   public void clusterDescriptionChanged(ClusterDescriptionChangedEvent event) {
      Iterator var2 = this.clusterListeners.iterator();

      while(var2.hasNext()) {
         ClusterListener cur = (ClusterListener)var2.next();

         try {
            cur.clusterDescriptionChanged(event);
         } catch (Exception e) {
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(String.format("Exception thrown raising cluster description changed event to listener %s", cur), e);
            }
         }
      }

   }
}
