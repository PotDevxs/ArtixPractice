package dev.artixdev.practice.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import dev.artixdev.practice.managers.ProjectileManager;

/**
 * LightningStrikeListener
 * Currently kept as a hook for future lightning-related features.
 * Does not interact with projectile effects directly, since the
 * ProjectileManager only handles true projectiles (arrows, snowballs, etc.).
 */
public class LightningStrikeListener implements Listener {

   private final ProjectileManager projectileManager;

   public LightningStrikeListener(ProjectileManager projectileManager) {
      this.projectileManager = projectileManager;
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onLightningStrike(LightningStrikeEvent event) {
      // No-op for now: lightning strikes are not tied into ProjectileManager.
      // This listener remains as an extension point for future effects.
   }
}
