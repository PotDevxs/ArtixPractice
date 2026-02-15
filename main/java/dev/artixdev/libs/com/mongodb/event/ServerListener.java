package dev.artixdev.libs.com.mongodb.event;

import java.util.EventListener;

public interface ServerListener extends EventListener {
   default void serverOpening(ServerOpeningEvent event) {
   }

   default void serverClosed(ServerClosedEvent event) {
   }

   default void serverDescriptionChanged(ServerDescriptionChangedEvent event) {
   }
}
