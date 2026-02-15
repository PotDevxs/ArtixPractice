package dev.artixdev.api.practice.spigot.event.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

public class RefineRefinePotionExpireEvent extends RefinePotionRemoveEvent {
   private int duration = 0;

   public RefineRefinePotionExpireEvent(LivingEntity entity, PotionEffect effect) {
      super(entity, effect);
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = Math.max(0, duration);
   }

   public boolean isCancelled() {
      return this.duration > 0;
   }

   public void setCancelled(boolean cancel) {
      this.duration = cancel ? Integer.MAX_VALUE : 0;
   }
}
