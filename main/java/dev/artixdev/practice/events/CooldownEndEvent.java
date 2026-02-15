package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import dev.artixdev.practice.managers.CooldownManager;

/**
 * Cooldown End Event
 * Event fired when a cooldown ends
 */
public class CooldownEndEvent extends Event {
   
   private static final HandlerList handlers = new HandlerList();
   
   private final Player player;
   private final CooldownManager cooldownManager;
   
   /**
    * Constructor
    * @param player the player
    * @param cooldownManager the cooldown manager
    */
   public CooldownEndEvent(Player player, CooldownManager cooldownManager) {
      this.player = player;
      this.cooldownManager = cooldownManager;
   }
   
   /**
    * Get player
    * @return player
    */
   public Player getPlayer() {
      return player;
   }
   
   /**
    * Get cooldown manager
    * @return cooldown manager
    */
   public CooldownManager getCooldownManager() {
      return cooldownManager;
   }
   
   @Override
   public HandlerList getHandlers() {
      return handlers;
   }
   
   public static HandlerList getHandlerList() {
      return handlers;
   }
   
   @Override
   public String toString() {
      return String.format("CooldownEndEvent{player=%s, cooldownManager=%s}", 
         player != null ? player.getName() : "null", 
         cooldownManager != null ? cooldownManager.getName() : "null");
   }
}
