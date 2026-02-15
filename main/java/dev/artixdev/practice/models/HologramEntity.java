package dev.artixdev.practice.models;

import java.util.List;
import java.util.UUID;
import org.bukkit.Location;

/**
 * Hologram Entity
 * Represents a hologram entity in the world
 */
public class HologramEntity {
   
   private final Location location;
   private final List<UUID> viewers;
   private int entityId;
   private boolean visible;
   private String name;
   private List<String> lines;
   
   /**
    * Constructor
    * @param location the location
    * @param viewers the viewers
    */
   public HologramEntity(Location location, List<UUID> viewers) {
      this.location = location;
      this.viewers = viewers;
      this.entityId = -1;
      this.visible = true;
      this.name = "Hologram";
      this.lines = new java.util.ArrayList<>();
   }
   
   /**
    * Get location
    * @return location
    */
   public Location getLocation() {
      return location;
   }
   
   /**
    * Get viewers
    * @return viewers list
    */
   public List<UUID> getViewers() {
      return viewers;
   }
   
   /**
    * Get entity ID
    * @return entity ID
    */
   public int getEntityId() {
      return entityId;
   }
   
   /**
    * Set entity ID
    * @param entityId the entity ID
    */
   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }
   
   /**
    * Check if visible
    * @return true if visible
    */
   public boolean isVisible() {
      return visible;
   }
   
   /**
    * Set visible
    * @param visible the visible status
    */
   public void setVisible(boolean visible) {
      this.visible = visible;
   }
   
   /**
    * Get name
    * @return name
    */
   public String getName() {
      return name;
   }
   
   /**
    * Set name
    * @param name the name
    */
   public void setName(String name) {
      this.name = name;
   }
   
   /**
    * Get lines
    * @return lines list
    */
   public List<String> getLines() {
      return lines;
   }
   
   /**
    * Set lines
    * @param lines the lines
    */
   public void setLines(List<String> lines) {
      this.lines = lines;
   }
   
   /**
    * Add line
    * @param line the line to add
    */
   public void addLine(String line) {
      if (line != null) {
         this.lines.add(line);
      }
   }
   
   /**
    * Remove line
    * @param index the line index
    */
   public void removeLine(int index) {
      if (index >= 0 && index < this.lines.size()) {
         this.lines.remove(index);
      }
   }
   
   /**
    * Clear lines
    */
   public void clearLines() {
      this.lines.clear();
   }
   
   /**
    * Get line count
    * @return line count
    */
   public int getLineCount() {
      return this.lines.size();
   }
   
   /**
    * Add viewer
    * @param viewer the viewer UUID
    */
   public void addViewer(UUID viewer) {
      if (viewer != null && !this.viewers.contains(viewer)) {
         this.viewers.add(viewer);
      }
   }
   
   /**
    * Remove viewer
    * @param viewer the viewer UUID
    */
   public void removeViewer(UUID viewer) {
      if (viewer != null) {
         this.viewers.remove(viewer);
      }
   }
   
   /**
    * Check if viewer is viewing
    * @param viewer the viewer UUID
    * @return true if viewing
    */
   public boolean isViewing(UUID viewer) {
      return viewer != null && this.viewers.contains(viewer);
   }
   
   /**
    * Get viewer count
    * @return viewer count
    */
   public int getViewerCount() {
      return this.viewers.size();
   }
   
   /**
    * Clear viewers
    */
   public void clearViewers() {
      this.viewers.clear();
   }
   
   /**
    * Check if has viewers
    * @return true if has viewers
    */
   public boolean hasViewers() {
      return !this.viewers.isEmpty();
   }
   
   /**
    * Update hologram
    */
   public void update() {
      // Update hologram display
      // This would be implemented based on your hologram system
   }
   
   /**
    * Show hologram
    */
   public void show() {
      this.visible = true;
      update();
   }
   
   /**
    * Hide hologram
    */
   public void hide() {
      this.visible = false;
      update();
   }
   
   /**
    * Teleport hologram
    * @param newLocation the new location
    */
   public void teleport(Location newLocation) {
      if (newLocation != null) {
         // Update location and refresh display
         // This would be implemented based on your hologram system
      }
   }
   
   /**
    * Check if hologram is valid
    * @return true if valid
    */
   public boolean isValid() {
      return location != null && location.getWorld() != null;
   }
   
   /**
    * Get hologram info
    * @return hologram info
    */
   public String getInfo() {
      return String.format("Hologram: %s, Location: %s, Viewers: %d, Lines: %d", 
         name, location.toString(), getViewerCount(), getLineCount());
   }
   
   /**
    * Clone hologram
    * @return cloned hologram
    */
   public HologramEntity clone() {
      HologramEntity clone = new HologramEntity(location.clone(), new java.util.ArrayList<>(viewers));
      clone.setEntityId(entityId);
      clone.setVisible(visible);
      clone.setName(name);
      clone.setLines(new java.util.ArrayList<>(lines));
      return clone;
   }
   
   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      
      HologramEntity that = (HologramEntity) obj;
      return entityId == that.entityId && 
             java.util.Objects.equals(location, that.location) &&
             java.util.Objects.equals(name, that.name);
   }
   
   @Override
   public int hashCode() {
      return java.util.Objects.hash(location, entityId, name);
   }
   
   @Override
   public String toString() {
      return getInfo();
   }
}
