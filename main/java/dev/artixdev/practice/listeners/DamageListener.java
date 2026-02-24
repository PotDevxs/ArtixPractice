package dev.artixdev.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Match;

public class DamageListener implements Listener {

    private static Main plugin() {
        return Main.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Match match = plugin().getMatchManager().getMatchByPlayer((Player) event.getEntity());
        if (match != null && match.isInWarmup()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Match match = plugin().getMatchManager().getMatchByPlayer((Player) event.getEntity());
        if (match != null && match.isInWarmup()) {
            event.setCancelled(true);
        }
    }
}
