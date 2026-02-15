package dev.artixdev.libs.com.mongodb.event;

import java.util.EventListener;

public interface ClusterListener extends EventListener {
   default void clusterOpening(ClusterOpeningEvent event) {
   }

   default void clusterClosed(ClusterClosedEvent event) {
   }

   default void clusterDescriptionChanged(ClusterDescriptionChangedEvent event) {
   }
}
