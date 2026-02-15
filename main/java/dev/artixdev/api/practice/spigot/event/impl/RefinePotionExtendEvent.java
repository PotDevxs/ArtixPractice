package dev.artixdev.api.practice.spigot.event.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

public class RefinePotionExtendEvent extends RefinePotionAddEvent {
   private final PotionEffect oldEffect;

   public RefinePotionExtendEvent(LivingEntity what, PotionEffect effect, RefinePotionAddEvent.EffectAddReason reason, PotionEffect oldEffect) {
      super(what, effect, reason);
      this.oldEffect = oldEffect;
   }

   public PotionEffect getOldEffect() {
      return this.oldEffect;
   }
}
