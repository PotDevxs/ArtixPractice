package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.CosmeticSettings;

/**
 * Player Cosmetic Event
 * Event fired when a player's cosmetic settings change
 */
public class PlayerCosmeticEvent extends BaseEvent {
   
   private final Player player;
   private final CosmeticSettings cosmeticSettings;
   
   /**
    * Constructor
    * @param player the player
    * @param cosmeticSettings the cosmetic settings
    */
   public PlayerCosmeticEvent(Player player, CosmeticSettings cosmeticSettings) {
      this.player = player;
      this.cosmeticSettings = cosmeticSettings;
   }
   
   /**
    * Get player
    * @return player
    */
   public Player getPlayer() {
      return player;
   }
   
   /**
    * Get cosmetic settings
    * @return cosmetic settings
    */
   public CosmeticSettings getCosmeticSettings() {
      return cosmeticSettings;
   }
   
   /**
    * Get kill effect
    * @return kill effect
    */
   public CosmeticSettings.KillEffect getKillEffect() {
      return cosmeticSettings.getKillEffect();
   }
   
   /**
    * Get kill message
    * @return kill message
    */
   public CosmeticSettings.KillMessage getKillMessage() {
      return cosmeticSettings.getKillMessage();
   }
   
   /**
    * Get trail
    * @return trail
    */
   public CosmeticSettings.Trail getTrail() {
      return cosmeticSettings.getTrail();
   }
   
   /**
    * Check if has kill effect
    * @return true if has kill effect
    */
   public boolean hasKillEffect() {
      return cosmeticSettings.hasKillEffect();
   }
   
   /**
    * Check if has kill message
    * @return true if has kill message
    */
   public boolean hasKillMessage() {
      return cosmeticSettings.hasKillMessage();
   }
   
   /**
    * Check if has trail
    * @return true if has trail
    */
   public boolean hasTrail() {
      return cosmeticSettings.hasTrail();
   }
   
   /**
    * Check if has any cosmetics
    * @return true if has any cosmetics
    */
   public boolean hasAnyCosmetics() {
      return cosmeticSettings.hasAnyCosmetics();
   }
   
   /**
    * Get cosmetic count
    * @return cosmetic count
    */
   public int getCosmeticCount() {
      return cosmeticSettings.getCosmeticCount();
   }
   
   /**
    * Get cosmetics info
    * @return cosmetics info
    */
   public String getCosmeticsInfo() {
      return cosmeticSettings.getCosmeticsInfo();
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
    * Get event summary
    * @return event summary
    */
   public String getEventSummary() {
      return String.format("PlayerCosmeticEvent: %s, Cosmetics: %s", 
         getPlayerName(), getCosmeticsInfo());
   }
   
   @Override
   public String toString() {
      return getEventSummary();
   }
}
