package dev.artixdev.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.Messages;

/**
 * FFA: allow PvP in FFA world, respawn on death, track FFA kills/deaths.
 */
public class FFAListener implements Listener {

    private final Main plugin;

    public FFAListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        if (plugin.getMatchManager().isPlayerInMatch(victim) || plugin.getMatchManager().isPlayerInMatch(damager)) {
            return;
        }
        if (plugin.getFFAManager() != null && plugin.getFFAManager().isInFFA(victim.getLocation()) && plugin.getFFAManager().isInFFA(damager.getLocation())) {
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        if (plugin.getMatchManager().isPlayerInMatch(victim)) return;
        if (plugin.getFFAManager() == null || !plugin.getFFAManager().isInFFA(victim.getLocation())) return;
        Player killer = victim.getKiller();
        if (killer != null) {
            plugin.getFFAManager().onFFAKill(killer, victim);
            killer.sendMessage(Messages.get("FFA.KILL", "victim", victim.getName()));
        }
        event.setDeathMessage(null);
        event.getDrops().clear();
        event.setDroppedExp(0);
        org.bukkit.Bukkit.getScheduler().runTaskLater(plugin.getPlugin(), () -> {
            if (victim.isOnline()) {
                plugin.getFFAManager().respawnInFFA(victim);
                victim.sendMessage(Messages.get("FFA.RESPAWNED"));
            }
        }, 5L);
    }
}
