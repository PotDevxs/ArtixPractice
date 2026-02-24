package dev.artixdev.practice.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Faz os itens da hotbar do practice funcionarem (clique abre menu / executa ação).
 */
public class HotbarInteractListener implements Listener {

    private final Main plugin;

    public HotbarInteractListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) return;

        String name = item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? ChatUtils.stripColor(item.getItemMeta().getDisplayName()) : "";
        if (name.isEmpty()) return;

        // Match por nome legado (sem cor) para não depender do config
        String lower = name.toLowerCase();
        if (lower.contains("queue")) {
            event.setCancelled(true);
            player.performCommand("practice queue");
            return;
        }
        if (lower.contains("party")) {
            event.setCancelled(true);
            player.performCommand("party");
            return;
        }
        if (lower.contains("kit") || lower.contains("kits")) {
            event.setCancelled(true);
            player.performCommand("practice kit");
            return;
        }
        if (lower.contains("stats")) {
            event.setCancelled(true);
            player.performCommand("practice stats");
            return;
        }
        if (lower.contains("arena")) {
            event.setCancelled(true);
            player.performCommand("practice arena");
            return;
        }
        if (lower.contains("leave queue")) {
            event.setCancelled(true);
            if (plugin.getQueueManager() != null && plugin.getQueueManager().isPlayerInQueue(player)) {
                plugin.getQueueManager().removePlayerFromQueue(player);
                player.sendMessage(dev.artixdev.practice.utils.Messages.getPrefix() + " §7Você saiu da fila.");
            }
            return;
        }
        if (lower.contains("disband") || lower.contains("leave party")) {
            event.setCancelled(true);
            player.performCommand("party leave");
            return;
        }
        if (lower.contains("stop spectating")) {
            event.setCancelled(true);
            if (plugin.getMatchManager().isSpectating(player)) {
                plugin.getMatchManager().stopSpectating(player);
            }
            return;
        }
    }
}
