package dev.artixdev.api.practice.spigot.event.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;

public class RefinePotionAddEvent extends PotionEffectEvent implements Cancellable {
   private boolean cancelled;
   private final RefinePotionAddEvent.EffectAddReason reason;
   private static final HandlerList handlers = new HandlerList();

   public RefinePotionAddEvent(LivingEntity what, PotionEffect effect, RefinePotionAddEvent.EffectAddReason reason) {
      super(what, effect);
      this.reason = reason;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancel) {
      this.cancelled = cancel;
   }

   public RefinePotionAddEvent.EffectAddReason getReason() {
      return this.reason;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public static enum EffectAddReason {
      COMMAND,
      GOLDEN_APPLE,
      BEACON,
      WITHER_SKULL,
      WITHER_SKELETON,
      VILLAGER_CURE,
      VILLAGER_LEVELUP,
      SPIDER_POWERUP,
      POTION_SPLASH,
      POTION_DRINK,
      CUSTOM,
      PLUGIN,
      UNKNOWN;
   }
}
