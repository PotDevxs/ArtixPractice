package dev.artixdev.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Match;

public class EntityDamageListener implements Listener {
    private static final String[] DAMAGE_CONSTANTS = {"EntityDamageListener"};
    public static final int LISTENER_VERSION = 1;
    private static final String[] DAMAGE_TYPES = new String[7];
    public static final boolean DEBUG_MODE = false;

    static {
        DAMAGE_TYPES[0] = "DAMAGE";
        DAMAGE_TYPES[1] = "FIRE";
        DAMAGE_TYPES[2] = "FALL";
        DAMAGE_TYPES[3] = "EXPLOSION";
        DAMAGE_TYPES[4] = "VOID";
        DAMAGE_TYPES[5] = "SUFFOCATION";
        DAMAGE_TYPES[6] = "DROWNING";
    }

    public EntityDamageListener() {
        super();
    }

    @EventHandler(
        ignoreCancelled = true,
        priority = EventPriority.HIGHEST
    )
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        Main main = Main.getInstance();
        if (main == null || main.getMatchManager() == null) return;
        Match match = main.getMatchManager().getMatchByPlayer(victim);
        if (match != null && match.isInWarmup()) {
            event.setCancelled(true);
            return;
        }
        if (main.getMatchManager().isSpectating(victim)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(
        priority = EventPriority.NORMAL
    )
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        Player damager = null;
        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        }
        Main main = Main.getInstance();
        if (main == null || main.getMatchManager() == null) return;
        if (main.getMatchManager().isSpectating(victim) || (damager != null && main.getMatchManager().isSpectating(damager))) {
            event.setCancelled(true);
            return;
        }
        Match match = main.getMatchManager().getMatchByPlayer(victim);
        if (match != null && match.isInWarmup()) {
            event.setCancelled(true);
        }
    }
}
