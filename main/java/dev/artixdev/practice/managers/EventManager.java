package dev.artixdev.practice.managers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.EventType;

/**
 * Event Manager
 * Manages all events and listeners in the practice server
 */
public class EventManager {
   
   private static final Logger logger = LogManager.getLogger(EventManager.class);
   private final Main plugin;
   private final Map<UUID, EventListener> listeners;
   private boolean enabled = true;

   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public EventManager(Main plugin) {
      this.plugin = plugin;
      this.listeners = new ConcurrentHashMap<>();
   }

   /**
    * Register an event listener
    * @param listener the listener to register
    */
   public void registerListener(EventListener listener) {
      if (listener == null) {
         logger.warn("Cannot register null listener");
         return;
      }
      
      UUID listenerId = listener.getId();
      this.listeners.put(listenerId, listener);
      logger.info("Registered event listener: " + listener.getClass().getSimpleName());
   }

   /**
    * Unregister an event listener
    * @param listenerId the listener ID
    */
   public void unregisterListener(UUID listenerId) {
      EventListener listener = this.listeners.remove(listenerId);
      if (listener != null) {
         logger.info("Unregistered event listener: " + listener.getClass().getSimpleName());
      }
   }

   /**
    * Unload all listeners
    */
   public void unload() {
      logger.info("Unloading all event listeners...");
      this.listeners.clear();
      this.enabled = false;
      logger.info("All event listeners unloaded");
   }

   /**
    * Fire an event
    * @param eventType the event type
    * @param data the event data
    */
   public void fireEvent(EventType eventType, Object data) {
      if (!enabled) {
         return;
      }
      
      List<EventListener> relevantListeners = this.listeners.values().stream()
         .filter(listener -> listener.getEventTypes().contains(eventType))
         .collect(Collectors.toList());
      
      for (EventListener listener : relevantListeners) {
         try {
            listener.onEvent(eventType, data);
         } catch (Exception e) {
            logger.error("Error in event listener " + listener.getClass().getSimpleName(), e);
         }
      }
   }

   /**
    * Fire an event with delay
    * @param eventType the event type
    * @param data the event data
    * @param delayTicks the delay in ticks
    */
   public void fireEventDelayed(EventType eventType, Object data, long delayTicks) {
      plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
         fireEvent(eventType, data);
      }, delayTicks);
   }

   /**
    * Fire an event with delay and interval
    * @param eventType the event type
    * @param data the event data
    * @param delayTicks the delay in ticks
    * @param intervalTicks the interval in ticks
    */
   public void fireEventRepeating(EventType eventType, Object data, long delayTicks, long intervalTicks) {
      plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
         fireEvent(eventType, data);
      }, delayTicks, intervalTicks);
   }

   /**
    * Get all listeners
    * @return collection of listeners
    */
   public Collection<EventListener> getListeners() {
      return this.listeners.values();
   }

   /**
    * Get listeners by event type
    * @param eventType the event type
    * @return list of listeners
    */
   public List<EventListener> getListenersByEventType(EventType eventType) {
      return this.listeners.values().stream()
         .filter(listener -> listener.getEventTypes().contains(eventType))
         .collect(Collectors.toList());
   }

   /**
    * Get listener by ID
    * @param listenerId the listener ID
    * @return listener or null if not found
    */
   public EventListener getListener(UUID listenerId) {
      return this.listeners.get(listenerId);
   }

   /**
    * Check if listener exists
    * @param listenerId the listener ID
    * @return true if exists
    */
   public boolean hasListener(UUID listenerId) {
      return this.listeners.containsKey(listenerId);
   }

   /**
    * Get listener count
    * @return number of listeners
    */
   public int getListenerCount() {
      return this.listeners.size();
   }

   /**
    * Check if manager is enabled
    * @return true if enabled
    */
   public boolean isEnabled() {
      return this.enabled;
   }

   /**
    * Set enabled state
    * @param enabled the enabled state
    */
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   /**
    * Clear all listeners
    */
   public void clearListeners() {
      this.listeners.clear();
   }

   /**
    * Get listeners by class
    * @param clazz the listener class
    * @return list of listeners
    */
   public <T extends EventListener> List<T> getListenersByClass(Class<T> clazz) {
      return this.listeners.values().stream()
         .filter(clazz::isInstance)
         .map(clazz::cast)
         .collect(Collectors.toList());
   }
}
