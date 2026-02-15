package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import dev.artixdev.practice.managers.CooldownManager;

/**
 * Cooldown Start Event
 * Event fired when a cooldown starts
 */
public class CooldownStartEvent extends Event implements Cancellable {
   
   private static final HandlerList handlers = new HandlerList();
   
   private final Player player;
   private final CooldownManager cooldownManager;
   private boolean cancelled = false;
   
   /**
    * Constructor
    * @param player the player
    * @param cooldownManager the cooldown manager
    */
   public CooldownStartEvent(Player player, CooldownManager cooldownManager) {
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
   public boolean isCancelled() {
      return cancelled;
   }
   
   @Override
   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
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
      return String.format("CooldownStartEvent{player=%s, cooldownManager=%s, cancelled=%s}", 
         player != null ? player.getName() : "null", 
         cooldownManager != null ? cooldownManager.getName() : "null", 
         cancelled);
   }
}
