package dev.artixdev.practice.models;

import java.util.List;
import java.util.UUID;
import java.util.Objects;
import org.bukkit.entity.Player;

/**
 * Entity Data Model
 * Represents data for an entity with associated players
 */
public class EntityData {
   
   private final UUID entityUUID;
   private final int entityId;
   private final List<Player> viewers;
   
   /**
    * Constructor
    * @param entityId the entity ID
    * @param entityUUID the entity UUID
    * @param viewers the list of viewers
    */
   public EntityData(int entityId, UUID entityUUID, List<Player> viewers) {
      this.entityId = entityId;
      this.entityUUID = entityUUID;
      this.viewers = viewers;
   }
   
   /**
    * Get the entity UUID
    * @return entity UUID
    */
   public UUID getEntityUUID() {
      return this.entityUUID;
   }
   
   /**
    * Get the entity ID
    * @return entity ID
    */
   public int getEntityId() {
      return this.entityId;
   }
   
   /**
    * Get the list of viewers
    * @return list of viewers
    */
   public List<Player> getViewers() {
      return this.viewers;
   }
   
   /**
    * Add a viewer
    * @param player the player to add
    */
   public void addViewer(Player player) {
      if (player != null && !this.viewers.contains(player)) {
         this.viewers.add(player);
      }
   }
   
   /**
    * Remove a viewer
    * @param player the player to remove
    */
   public void removeViewer(Player player) {
      this.viewers.remove(player);
   }
   
   /**
    * Check if player is viewing
    * @param player the player to check
    * @return true if viewing
    */
   public boolean isViewing(Player player) {
      return this.viewers.contains(player);
   }
   
   /**
    * Get viewer count
    * @return number of viewers
    */
   public int getViewerCount() {
      return this.viewers.size();
   }
   
   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      
      EntityData that = (EntityData) obj;
      return entityId == that.entityId &&
             Objects.equals(entityUUID, that.entityUUID);
   }
   
   @Override
   public int hashCode() {
      return Objects.hash(entityId, entityUUID);
   }
   
   @Override
   public String toString() {
      return "EntityData{" +
             "entityId=" + entityId +
             ", entityUUID=" + entityUUID +
             ", viewers=" + viewers.size() +
             '}';
   }
}
