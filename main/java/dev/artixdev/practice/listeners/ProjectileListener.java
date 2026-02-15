package dev.artixdev.practice.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BlockIterator;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.configs.SettingsConfig;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.managers.MatchManager;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.events.MatchEndEvent;
import dev.artixdev.practice.events.PlayerCustomEvent;

import java.util.UUID;

public class ProjectileListener implements Listener {
    public static final int LISTENER_VERSION = 1;
    private static final String[] COMMAND_CONSTANTS = new String[14];

    private final Main plugin;
    private final MatchManager matchManager;

    public ProjectileListener() {
        this(Main.getInstance());
    }

    public ProjectileListener(Main plugin) {
        this.plugin = plugin;
        this.matchManager = plugin.getMatchManager();
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Match victimMatch = matchManager.getMatchByPlayer(victim);

        // Prevent match players from damaging non-match players (spectators/lobby)
        if (victimMatch == null) {
            cancelIfDamagedByMatchPlayer(event);
            return;
        }

        if (victimMatch.isEnded()) {
            event.setCancelled(true);
            return;
        }

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            Player damagerPlayer = extractPlayerShooter(damageEvent.getDamager());

            if (damagerPlayer != null) {
                Match damagerMatch = matchManager.getMatchByPlayer(damagerPlayer);

                if (damagerMatch == null || !sameMatch(victimMatch, damagerMatch)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        // Clean up lingering projectiles shot by players that were in the finished match
        int removedProjectiles = 0;
        for (UUID playerId : event.getPlayerResults().keySet()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player == null) {
                continue;
            }

            removedProjectiles += player.getWorld().getEntitiesByClass(Projectile.class).stream()
                .filter(projectile -> {
                    ProjectileSource source = projectile.getShooter();
                    return source instanceof Player && ((Player) source).getUniqueId().equals(playerId);
                })
                .mapToInt(projectile -> {
                    projectile.remove();
                    return 1;
                })
                .sum();
        }

        if (SettingsConfig.DEBUG_MODE && removedProjectiles > 0) {
            plugin.getLogger().info("[ProjectileListener] Cleaned up " + removedProjectiles + " projectiles after match end.");
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile.getShooter() instanceof Player) {
            Player shooter = (Player) projectile.getShooter();
            Match match = matchManager.getMatchByPlayer(shooter);

            if (match != null && match.isActive()) {
                if (projectile instanceof Arrow) {
                    handleArrowHit(match, shooter);
                    projectile.remove();
                } else if (projectile instanceof Snowball) {
                    handleSnowballSpleef(projectile);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for player respawn handling
    }

    @EventHandler(
        priority = EventPriority.LOW,
        ignoreCancelled = true
    )
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        // No special handling currently required
    }

    @EventHandler(
        priority = EventPriority.LOW,
        ignoreCancelled = true
    )
    public void onPotionSplash(PotionSplashEvent event) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for potion splash handling
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for food level change handling
    }

    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onPlayerCustomEvent(PlayerCustomEvent event) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for player custom event handling
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        
        if (projectile instanceof ThrownPotion && projectile.getShooter() instanceof Player) {
            Player shooter = (Player) projectile.getShooter();
            Match match = matchManager.getMatchByPlayer(shooter);
            
            if (match != null && match.isActive()) {
                // Handle potion launch
                handlePotionLaunch(match, shooter);
            }
        }
    }

    @EventHandler
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for entity combust by entity handling
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for player death handling
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onPlayerInteract(PlayerInteractEvent event) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for player interact handling
    }

    private void handleArrowHit(Match match, Player shooter) {
        // Handle arrow hit logic
    }

    private void handleSnowballSpleef(Projectile projectile) {
        BlockIterator blockIterator = new BlockIterator(
            projectile.getWorld(),
            projectile.getLocation().toVector(),
            projectile.getVelocity().normalize(),
            0.0D,
            4
        );

        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            
            if (block.getType() == XMaterial.SNOW_BLOCK.parseMaterial()) {
                block.setType(Material.AIR);
                
                if (!SettingsConfig.MATCH_SPLEEF_TRAJECTORY) {
                    return;
                }
            }
        }
    }

    private void handlePotionLaunch(Match match, Player shooter) {
        // Handle potion launch logic
    }

    private void cancelIfDamagedByMatchPlayer(EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return;
        }

        EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
        Player damager = extractPlayerShooter(damageEvent.getDamager());
        if (damager == null) {
            return;
        }

        if (matchManager.getMatchByPlayer(damager) != null) {
            event.setCancelled(true);
            if (damageEvent.getDamager() instanceof Projectile) {
                damageEvent.getDamager().remove();
            }
        }
    }

    private Player extractPlayerShooter(Entity entity) {
        if (entity instanceof Player) {
            return (Player) entity;
        }
        if (entity instanceof Projectile) {
            ProjectileSource source = ((Projectile) entity).getShooter();
            if (source instanceof Player) {
                return (Player) source;
            }
        }
        return null;
    }

    private boolean sameMatch(Match first, Match second) {
        if (first == null || second == null) {
            return false;
        }
        if (first == second) {
            return true;
        }
        if (first.getId() == null || second.getId() == null) {
            return false;
        }
        return first.getId().equals(second.getId());
    }

    static {
        COMMAND_CONSTANTS[0] = "G";
        COMMAND_CONSTANTS[1] = "XõÏ";
        COMMAND_CONSTANTS[2] = "°Îw(\"t£";
        COMMAND_CONSTANTS[3] = "➕";
        COMMAND_CONSTANTS[4] = "Ëïfeä";
        COMMAND_CONSTANTS[5] = "ñÉÿÐ";
        COMMAND_CONSTANTS[6] = "{ÔÊ£Ð";
        COMMAND_CONSTANTS[7] = ".Ü\"-M";
        COMMAND_CONSTANTS[8] = "V}$Às";
        COMMAND_CONSTANTS[9] = "ù¹Ù©Ä";
        COMMAND_CONSTANTS[10] = "êÃ/j±åû";
        COMMAND_CONSTANTS[11] = "ðBx¾";
        COMMAND_CONSTANTS[12] = "¸Ä!";
        COMMAND_CONSTANTS[13] = "ÜçÛß²*c8Ìa";
    }
}