package dev.artixdev.libs.com.mongodb.event;

import java.util.EventListener;

public interface ServerMonitorListener extends EventListener {
   default void serverHearbeatStarted(ServerHeartbeatStartedEvent event) {
   }

   default void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent event) {
   }

   default void serverHeartbeatFailed(ServerHeartbeatFailedEvent event) {
   }
}
