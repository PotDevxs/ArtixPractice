package dev.artixdev.practice.expansions.hologram;

import dev.artixdev.practice.Main;
import org.bukkit.Location;

/**
 * Abstract Hologram Class
 * Base class for all hologram implementations.
 * Hologram instance is stored as Object to avoid hard dependency on HolographicDisplays API.
 */
public abstract class AbstractHologram {
   
   protected static final int DEFAULT_UPDATE_INTERVAL = 20; // 1 second
   protected int updateInterval = DEFAULT_UPDATE_INTERVAL;
   protected int taskId = -1;
   protected final Main plugin;
   /** External hologram instance (e.g. HolographicDisplays API); type erased to avoid API dependency. */
   protected Object hologram;
   protected Location location;
   protected String displayName;

   /**
    * Constructor (manager / no location)
    * @param plugin the plugin instance
    */
   public AbstractHologram(Main plugin) {
      this.plugin = plugin;
      this.location = null;
   }

   /**
    * Constructor
    * @param plugin the plugin instance
    * @param location the hologram location
    */
   public AbstractHologram(Main plugin, Location location) {
      this.plugin = plugin;
      this.location = location;
   }

   /**
    * Set the update interval for the hologram
    * @param interval the interval in ticks
    */
   public void setUpdateInterval(int interval) {
      this.updateInterval = interval;
   }

   /**
    * Abstract method to handle hologram updates
    * Called every update interval
    */
   public abstract void tick();

   /**
    * Set the hologram instance (e.g. from HolographicDisplays API)
    * @param hologram the hologram instance
    */
   public void setHologram(Object hologram) {
      this.hologram = hologram;
   }

   /**
    * Get the display name
    * @return display name
    */
   public String getDisplayName() {
      return this.displayName;
   }

   /**
    * Update the hologram
    */
   public void update() {
      if (hologram != null) {
         tick();
      }
   }

   /**
    * Get the plugin instance
    * @return plugin instance
    */
   public Main getPlugin() {
      return this.plugin;
   }

   /**
    * Get the update interval
    * @return update interval in ticks
    */
   public int getUpdateInterval() {
      return this.updateInterval;
   }

   /**
    * Set the display name
    * @param displayName the display name
    */
   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   /**
    * Set the task ID
    * @param taskId the task ID
    */
   public void setTaskId(int taskId) {
      this.taskId = taskId;
   }

   /**
    * Get the hologram instance (cast to API type where needed)
    * @return hologram instance
    */
   public Object getHologram() {
      return this.hologram;
   }

   /**
    * Get the task ID
    * @return task ID
    */
   public int getTaskId() {
      return this.taskId;
   }

   /**
    * Get the hologram location
    * @return location
    */
   public Location getLocation() {
      return this.location;
   }

   /**
    * Create the hologram
    * @return the created hologram instance (API-specific type)
    */
   public Object createHologram() {
      // This would be implemented by concrete classes
      return null;
   }
}
