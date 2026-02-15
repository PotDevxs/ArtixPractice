package dev.artixdev.practice.models;

import org.bukkit.Location;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;
import dev.artixdev.practice.enums.ArenaType;
import dev.artixdev.practice.enums.EventType;
import dev.artixdev.practice.utils.cuboid.Cuboid;

/**
 * Abstract Arena
 * Base class for arena implementations
 */
public abstract class AbstractArena {
   
   @SerializedName("bounds")
   protected Cuboid bounds;
   
   @SerializedName("eventArenaType")
   protected final ArenaType eventArenaType;
   
   @SerializedName("waitingLocation")
   protected Location waitingLocation;
   
   @SerializedName("deathHeight")
   private int deathHeight;
   
   @SerializedName("eventType")
   protected final EventType eventType;
   
   @SerializedName("buildHeight")
   private int buildHeight;
   
   @SerializedName("spectatorSpawn")
   protected Location spectatorSpawn;
   
   /**
    * Constructor
    * @param eventArenaType the event arena type
    * @param eventType the event type
    */
   public AbstractArena(ArenaType eventArenaType, EventType eventType) {
      this.eventArenaType = eventArenaType;
      this.eventType = eventType;
   }
   
   /**
    * Get event arena type
    * @return event arena type
    */
   public ArenaType getEventArenaType() {
      return eventArenaType;
   }
   
   /**
    * Set death height
    * @param deathHeight the death height
    */
   public void setDeathHeight(int deathHeight) {
      this.deathHeight = deathHeight;
   }
   
   /**
    * Get death height
    * @return death height
    */
   public int getDeathHeight() {
      return deathHeight;
   }
   
   /**
    * Set build height
    * @param buildHeight the build height
    */
   public void setBuildHeight(int buildHeight) {
      this.buildHeight = buildHeight;
   }
   
   /**
    * Get build height
    * @return build height
    */
   public int getBuildHeight() {
      return buildHeight;
   }
   
   /**
    * Get bounds
    * @return bounds
    */
   public Cuboid getBounds() {
      return bounds;
   }
   
   /**
    * Set bounds
    * @param bounds the bounds
    */
   public void setBounds(Cuboid bounds) {
      this.bounds = bounds;
   }
   
   /**
    * Get waiting location
    * @return waiting location
    */
   public Location getWaitingLocation() {
      return waitingLocation;
   }
   
   /**
    * Set waiting location
    * @param waitingLocation the waiting location
    */
   public void setWaitingLocation(Location waitingLocation) {
      this.waitingLocation = waitingLocation;
   }
   
   /**
    * Get spectator spawn
    * @return spectator spawn
    */
   public Location getSpectatorSpawn() {
      return spectatorSpawn;
   }
   
   /**
    * Set spectator spawn
    * @param spectatorSpawn the spectator spawn
    */
   public void setSpectatorSpawn(Location spectatorSpawn) {
      this.spectatorSpawn = spectatorSpawn;
   }
   
   /**
    * Get event type
    * @return event type
    */
   public EventType getEventType() {
      return eventType;
   }
   
   /**
    * Check if arena is valid
    * @return true if valid
    */
   public boolean isValid() {
      return bounds != null && 
             waitingLocation != null && 
             spectatorSpawn != null &&
             deathHeight > 0 &&
             buildHeight > 0;
   }
   
   /**
    * Get arena info
    * @return arena info
    */
   public String getArenaInfo() {
      return String.format("Arena: %s, Type: %s, Event: %s, Valid: %s", 
         getClass().getSimpleName(), eventArenaType, eventType, isValid());
   }
   
   /**
    * Get bounds info
    * @return bounds info
    */
   public String getBoundsInfo() {
      if (bounds == null) {
         return "No bounds set";
      }
      
      return String.format("Bounds: %dx%dx%d (%d blocks)", 
         bounds.getChunks().size(), bounds.getChunks().size(), bounds.getChunks().size(), bounds.getChunks().size());
   }
   
   /**
    * Get location info
    * @return location info
    */
   public String getLocationInfo() {
      StringBuilder info = new StringBuilder();
      
      if (waitingLocation != null) {
         info.append(String.format("Waiting: %.1f, %.1f, %.1f", 
            waitingLocation.getX(), waitingLocation.getY(), waitingLocation.getZ()));
      } else {
         info.append("Waiting: Not set");
      }
      
      if (spectatorSpawn != null) {
         info.append(String.format(", Spectator: %.1f, %.1f, %.1f", 
            spectatorSpawn.getX(), spectatorSpawn.getY(), spectatorSpawn.getZ()));
      } else {
         info.append(", Spectator: Not set");
      }
      
      return info.toString();
   }
   
   /**
    * Get height info
    * @return height info
    */
   public String getHeightInfo() {
      return String.format("Death Height: %d, Build Height: %d", deathHeight, buildHeight);
   }
   
   @Override
   public String toString() {
      return getArenaInfo();
   }
}
