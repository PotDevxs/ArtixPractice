package dev.artixdev.libs.com.mongodb.internal.event;

import dev.artixdev.libs.com.mongodb.event.ConnectionCheckOutFailedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionClosedEvent;

public final class EventReasonMessageResolver {
   private static final String MESSAGE_CONNECTION_POOL_WAS_CLOSED = "Connection pool was closed";
   private static final String EMPTY_REASON = "";

   public static String getMessage(ConnectionClosedEvent.Reason reason) {
      switch(reason) {
      case STALE:
         return "Connection became stale because the pool was cleared";
      case IDLE:
         return "Connection has been available but unused for longer than the configured max idle time";
      case ERROR:
         return "An error occurred while using the connection";
      case POOL_CLOSED:
         return "Connection pool was closed";
      default:
         return "";
      }
   }

   public static String getMessage(ConnectionCheckOutFailedEvent.Reason reason) {
      switch(reason) {
      case TIMEOUT:
         return "Wait queue timeout elapsed without a connection becoming available";
      case CONNECTION_ERROR:
         return "An error occurred while trying to establish a new connection";
      case POOL_CLOSED:
         return "Connection pool was closed";
      default:
         return "";
      }
   }

   private EventReasonMessageResolver() {
   }
}
