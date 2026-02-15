package dev.artixdev.practice.tasks;

import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;

public class PlayerListTask implements Runnable {
    public static final int TASK_ID = 8;
    private final PlayerListHandler playerListHandler;
    private BukkitTask task;
    public static final boolean DEBUG = false;
    private boolean cancelled;
    private Main plugin;

    public void run() {
        if (playerListHandler == null) {
            cancel();
            return;
        }
        
        // Update player list
        updatePlayerList();
    }

    private void updatePlayerList() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        Iterator<? extends Player> iterator = players.iterator();
        
        while (iterator.hasNext()) {
            Player player = iterator.next();
            PlayerProfile profile = plugin != null
                ? plugin.getPlayerManager().getPlayerProfile(player.getUniqueId())
                : null;
            
            if (profile != null) {
                // Update player list entry
                playerListHandler.updatePlayerEntry(player, profile);
            }
        }
    }

    public void start(Main plugin, long delay, long period) {
        if (task == null) {
            cancelled = false;
            this.plugin = plugin;
            task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, delay, period);
        }
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
            cancelled = true;
            task = null;
        }
    }

    public boolean isRunning() {
        if (task == null || cancelled) {
            return false;
        }
        int taskId = task.getTaskId();
        return pluginScheduler().isQueued(taskId) || pluginScheduler().isCurrentlyRunning(taskId);
    }

    private org.bukkit.scheduler.BukkitScheduler pluginScheduler() {
        return Bukkit.getScheduler();
    }

    public PlayerListHandler getPlayerListHandler() {
        return playerListHandler;
    }

    public PlayerListTask(PlayerListHandler playerListHandler) {
        this.playerListHandler = playerListHandler;
    }
}
