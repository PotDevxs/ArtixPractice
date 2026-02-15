package dev.artixdev.practice.managers;

import java.util.List;
import java.util.UUID;
import dev.artixdev.practice.enums.EventType;

/**
 * Event Listener Interface
 * Interface for all event listeners
 */
public interface EventListener {
   
   /**
    * Get the listener ID
    * @return listener ID
    */
   UUID getId();
   
   /**
    * Get the event types this listener handles
    * @return list of event types
    */
   List<EventType> getEventTypes();
   
   /**
    * Handle an event
    * @param eventType the event type
    * @param data the event data
    */
   void onEvent(EventType eventType, Object data);
   
   /**
    * Get the listener priority
    * @return priority (higher = first)
    */
   default int getPriority() {
      return 0;
   }
   
   /**
    * Check if listener is enabled
    * @return true if enabled
    */
   default boolean isEnabled() {
      return true;
   }
   
   /**
    * Get listener name
    * @return listener name
    */
   default String getName() {
      return this.getClass().getSimpleName();
   }
}
