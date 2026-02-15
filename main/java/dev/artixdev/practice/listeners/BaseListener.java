package dev.artixdev.practice.listeners;

import org.bukkit.event.Listener;
import dev.artixdev.practice.Main;

/**
 * Base Listener Class
 * Base class for all event listeners
 */
public abstract class BaseListener implements Listener {
   
   protected final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public BaseListener(Main plugin) {
      this.plugin = plugin;
   }
   
   /**
    * Get the plugin instance
    * @return plugin instance
    */
   protected Main getPlugin() {
      return this.plugin;
   }
   
   /**
    * Called when the listener is registered
    */
   public void onRegister() {
      // Override in subclasses
   }
   
   /**
    * Called when the listener is unregistered
    */
   public void onUnregister() {
      // Override in subclasses
   }
}
