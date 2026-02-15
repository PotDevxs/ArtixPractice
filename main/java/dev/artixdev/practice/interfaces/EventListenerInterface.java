package dev.artixdev.practice.interfaces;

import dev.artixdev.practice.enums.EventType;

/**
 * Event Listener Interface
 * Interface for event listeners with basic functionality
 */
public interface EventListenerInterface {
   
   /**
    * Get the listener priority
    * @return priority level
    */
   int getPriority();
   
   /**
    * Get the event type this listener handles
    * @return event type
    */
   EventType getEventType();
   
   /**
    * Get the listener name
    * @return listener name
    */
   String getName();
   
   /**
    * Get the listener ID
    * @return listener ID
    */
   int getId();
}
