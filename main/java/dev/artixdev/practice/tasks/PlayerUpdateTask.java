package dev.artixdev.practice.tasks;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;

public class PlayerUpdateTask implements Runnable {
    public static final int TASK_ID = 6;
    private final PlayerProfile playerProfile;
    private BukkitTask task;
    public static final boolean DEBUG = false;
    private final UUID playerId;
    private boolean cancelled;

    public void run() {
        if (playerProfile == null) {
            cancel();
            return;
        }
        
        // Update player-specific data
        updatePlayerData();
    }

    public void start(Main plugin, long delay, long period) {
        this.cancelled = false;
        BukkitScheduler scheduler = Bukkit.getScheduler();
        task = scheduler.runTaskTimer(plugin, this, delay, period);
    }

    private void updatePlayerData() {
        // Update player statistics
        playerProfile.updateStatistics();
        
        // Update player cooldowns
        playerProfile.updateCooldowns();
        
        // Update player location
        playerProfile.updateLocation();
        
        // Update player inventory
        playerProfile.updateInventory();
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
        BukkitScheduler scheduler = Bukkit.getScheduler();
        int taskId = task.getTaskId();
        return scheduler.isQueued(taskId) || scheduler.isCurrentlyRunning(taskId);
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public PlayerUpdateTask(PlayerProfile playerProfile, UUID playerId) {
        this.playerProfile = playerProfile;
        this.playerId = playerId;
    }
}
