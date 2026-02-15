package dev.artixdev.api.practice.spigot.event.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.potion.PotionEffect;

public abstract class PotionEffectEvent extends EntityEvent {
   private final PotionEffect effect;

   public PotionEffectEvent(LivingEntity entity, PotionEffect effect) {
      super(entity);
      this.effect = effect;
   }

   public LivingEntity getEntity() {
      return (LivingEntity)super.getEntity();
   }

   public PotionEffect getEffect() {
      return this.effect;
   }
}
