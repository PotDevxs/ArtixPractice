package dev.artixdev.api.practice.tablist.util;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BaseEvent extends Event implements Cancellable {
   private static final HandlerList handlers = new HandlerList();

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public boolean isCancelled() {
      return false;
   }

   public void setCancelled(boolean b) {
   }
}
