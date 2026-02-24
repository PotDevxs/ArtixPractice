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
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.managers.PlayerManager;

public class GameplayListener implements Listener {
    
    private final PlayerManager playerManager;

    public GameplayListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNPCSpawn(NPCSpawnEvent event) {
        // NPCs used for bots/arenas are configured via BotManager/ArenaManager
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player != null && playerManager != null) {
            PlayerProfile profile = playerManager.getPlayerProfile(player.getUniqueId());
            if (profile != null) {
                profile.setLastSeen(System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        // Projectile damage/effects are handled by EntityDamageByEntity and ProjectileHandler
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
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        caughtPlayer.setVelocity(velocity);
                    }, 1L);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        dev.artixdev.practice.models.Match matchV = Main.getInstance().getMatchManager().getMatchByPlayer(victim);
        dev.artixdev.practice.models.Match matchD = Main.getInstance().getMatchManager().getMatchByPlayer(damager);
        if (matchV != matchD) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNPCRemove(NPCRemoveEvent event) {
        // BotManager / match logic handles NPC cleanup when matches end
    }
}
