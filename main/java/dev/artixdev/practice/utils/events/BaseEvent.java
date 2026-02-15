package dev.artixdev.practice.utils.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BaseEvent extends Event implements Cancellable {
   private static final HandlerList handlers = new HandlerList();

   public BaseEvent(boolean var1) {
      super(var1);
   }

   public void setCancelled(boolean var1) {
   }

   public boolean isCancelled() {
      return false;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public BaseEvent() {
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
