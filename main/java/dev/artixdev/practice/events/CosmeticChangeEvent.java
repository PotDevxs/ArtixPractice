package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.CosmeticSettings;

/**
 * Cosmetic Change Event
 * Event fired when a player's cosmetic settings change
 */
public class CosmeticChangeEvent extends BaseEvent {
   
   private final CosmeticSettings oldSettings;
   private final CosmeticSettings newSettings;
   private final Player player;
   
   /**
    * Constructor
    * @param oldSettings the old settings
    * @param newSettings the new settings
    * @param player the player
    */
   public CosmeticChangeEvent(CosmeticSettings oldSettings, CosmeticSettings newSettings, Player player) {
      this.oldSettings = oldSettings;
      this.newSettings = newSettings;
      this.player = player;
   }
   
   /**
    * Get old settings
    * @return old settings
    */
   public CosmeticSettings getOldSettings() {
      return oldSettings;
   }
   
   /**
    * Get new settings
    * @return new settings
    */
   public CosmeticSettings getNewSettings() {
      return newSettings;
   }
   
   /**
    * Get player
    * @return player
    */
   public Player getPlayer() {
      return player;
   }
   
   /**
    * Check if kill effect changed
    * @return true if changed
    */
   public boolean isKillEffectChanged() {
      if (oldSettings == null || newSettings == null) {
         return true;
      }
      
      return oldSettings.getKillEffect() != newSettings.getKillEffect();
   }
   
   /**
    * Check if kill message changed
    * @return true if changed
    */
   public boolean isKillMessageChanged() {
      if (oldSettings == null || newSettings == null) {
         return true;
      }
      
      return oldSettings.getKillMessage() != newSettings.getKillMessage();
   }
   
   /**
    * Check if trail changed
    * @return true if changed
    */
   public boolean isTrailChanged() {
      if (oldSettings == null || newSettings == null) {
         return true;
      }
      
      return oldSettings.getTrail() != newSettings.getTrail();
   }
   
   /**
    * Check if any cosmetic changed
    * @return true if any changed
    */
   public boolean isAnyCosmeticChanged() {
      return isKillEffectChanged() || isKillMessageChanged() || isTrailChanged();
   }
   
   /**
    * Get change count
    * @return change count
    */
   public int getChangeCount() {
      int count = 0;
      if (isKillEffectChanged()) count++;
      if (isKillMessageChanged()) count++;
      if (isTrailChanged()) count++;
      return count;
   }
   
   /**
    * Get old kill effect
    * @return old kill effect
    */
   public CosmeticSettings.KillEffect getOldKillEffect() {
      return oldSettings != null ? oldSettings.getKillEffect() : null;
   }
   
   /**
    * Get new kill effect
    * @return new kill effect
    */
   public CosmeticSettings.KillEffect getNewKillEffect() {
      return newSettings != null ? newSettings.getKillEffect() : null;
   }
   
   /**
    * Get old kill message
    * @return old kill message
    */
   public CosmeticSettings.KillMessage getOldKillMessage() {
      return oldSettings != null ? oldSettings.getKillMessage() : null;
   }
   
   /**
    * Get new kill message
    * @return new kill message
    */
   public CosmeticSettings.KillMessage getNewKillMessage() {
      return newSettings != null ? newSettings.getKillMessage() : null;
   }
   
   /**
    * Get old trail
    * @return old trail
    */
   public CosmeticSettings.Trail getOldTrail() {
      return oldSettings != null ? oldSettings.getTrail() : null;
   }
   
   /**
    * Get new trail
    * @return new trail
    */
   public CosmeticSettings.Trail getNewTrail() {
      return newSettings != null ? newSettings.getTrail() : null;
   }
   
   /**
    * Get player name
    * @return player name
    */
   public String getPlayerName() {
      return player != null ? player.getName() : "Unknown";
   }
   
   /**
    * Get player UUID
    * @return player UUID
    */
   public java.util.UUID getPlayerUUID() {
      return player != null ? player.getUniqueId() : null;
   }
   
   /**
    * Check if player is online
    * @return true if online
    */
   public boolean isPlayerOnline() {
      return player != null && player.isOnline();
   }
   
   /**
    * Get change summary
    * @return change summary
    */
   public String getChangeSummary() {
      StringBuilder summary = new StringBuilder();
      
      if (isKillEffectChanged()) {
         summary.append("Kill Effect: ").append(getOldKillEffect()).append(" -> ").append(getNewKillEffect()).append(", ");
      }
      
      if (isKillMessageChanged()) {
         summary.append("Kill Message: ").append(getOldKillMessage()).append(" -> ").append(getNewKillMessage()).append(", ");
      }
      
      if (isTrailChanged()) {
         summary.append("Trail: ").append(getOldTrail()).append(" -> ").append(getNewTrail()).append(", ");
      }
      
      if (summary.length() > 0) {
         summary.setLength(summary.length() - 2); // Remove last ", "
      }
      
      return summary.toString();
   }
   
   /**
    * Get event summary
    * @return event summary
    */
   public String getEventSummary() {
      return String.format("CosmeticChangeEvent: %s, Changes: %s", 
         getPlayerName(), getChangeSummary());
   }
   
   @Override
   public String toString() {
      return getEventSummary();
   }
}
