package dev.artixdev.practice.listeners;

import net.citizensnpcs.api.event.NPCRemoveEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import dev.artixdev.practice.Practice;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.managers.PlayerManager;

import java.util.Iterator;
import java.util.UUID;

public class GameplayListener implements Listener {
    
    private final PlayerManager playerManager;

    public GameplayListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNPCSpawn(NPCSpawnEvent event) {
        // Handle NPC spawn events
        // This would typically involve setting up NPCs for practice matches
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        
        if (entity instanceof Player) {
            Player player = (Player) entity;
            
            // Check if player has special metadata for custom damage handling
            if (entity.hasMetadata("practice_player")) {
                PlayerProfile profile = playerManager.getPlayerProfile(player.getUniqueId());
                if (profile != null) {
                    // Placeholder for custom damage handling tied to PlayerProfile.
                    // Currently, we rely on Bukkit's built-in health system.
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Handle player quit events
        // This would typically involve cleaning up player data
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        // Handle projectile hit events
        // This would typically involve custom projectile mechanics
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getEntity();
        ItemStack item = potion.getItem();
        
        // Check for custom potion with specific durability
        if (item.getDurability() == 16421) {
            ProjectileSource shooter = potion.getShooter();
            
            if (shooter instanceof Player) {
                Player player = (Player) shooter;
                
                // Check if player has special metadata
                if (player.hasMetadata("practice_player")) {
                    PlayerProfile profile = playerManager.getPlayerProfile(player.getUniqueId());
                    if (profile != null) {
                        // Placeholder for custom potion usage tracking.
                        // Implement counters or effects here if needed.
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == State.CAUGHT_ENTITY) {
            Entity caught = event.getCaught();
            
            if (caught instanceof Player) {
                Player caughtPlayer = (Player) caught;
                
                // Check if caught player has special metadata
                if (caughtPlayer.hasMetadata("practice_player")) {
                    Vector velocity = caughtPlayer.getVelocity();
                    caughtPlayer.setNoDamageTicks(0);
                    
                    // Apply velocity after a short delay
                    Bukkit.getScheduler().runTaskLater(Practice.getPlugin(), () -> {
                        caughtPlayer.setVelocity(velocity);
                    }, 1L);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Handle entity damage by entity events
        // This would typically involve custom PvP mechanics
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNPCRemove(NPCRemoveEvent event) {
        // Handle NPC removal events
        // This would typically involve cleaning up NPC data
    }
}
