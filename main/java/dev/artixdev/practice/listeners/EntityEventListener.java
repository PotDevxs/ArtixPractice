package dev.artixdev.practice.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class EntityEventListener implements Listener {

    @EventHandler(
        priority = EventPriority.LOW,
        ignoreCancelled = true
    )
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            RegainReason reason = event.getRegainReason();
            
            // Handle health regain logic
            if (reason == RegainReason.SATIATED) {
                // Handle natural health regeneration
            } else if (reason == RegainReason.REGEN) {
                // Handle potion regeneration
            }
        }
    }

    @EventHandler(
        priority = EventPriority.LOW,
        ignoreCancelled = true
    )
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            // Handle damage logic
            EntityDamageEvent.DamageCause cause = event.getCause();
            
            if (cause == EntityDamageEvent.DamageCause.FALL) {
                // Handle fall damage
            } else if (cause == EntityDamageEvent.DamageCause.PROJECTILE) {
                // Handle projectile damage
            }
        }
    }

    @EventHandler(
        priority = EventPriority.LOW,
        ignoreCancelled = true
    )
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            // Handle food level changes
        }
    }

    @EventHandler(
        priority = EventPriority.LOW,
        ignoreCancelled = true
    )
    public void onPotionSplash(PotionSplashEvent event) {
        // Handle potion splash effects
    }

    @EventHandler(
        priority = EventPriority.LOW,
        ignoreCancelled = true
    )
    public void onProjectileHit(ProjectileHitEvent event) {
        // Handle projectile hit effects
    }
}
