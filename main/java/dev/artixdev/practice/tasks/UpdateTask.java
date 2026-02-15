package dev.artixdev.practice.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.handlers.UpdateHandler;
import dev.artixdev.practice.models.PlayerProfile;

public class UpdateTask implements Runnable {
    public static final int TASK_ID = 4;
    private final PlayerProfile playerProfile;
    private BukkitTask task;
    public static final boolean DEBUG = false;
    private final UpdateHandler updateHandler;

    public UpdateTask(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
        this.updateHandler = UpdateHandler.getInstance();
    }

    public void start(Main plugin, long delay, long period) {
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period);
    }

    public void run() {
        if (playerProfile == null) {
            cancel();
            return;
        }
        
        // Update player data
        updateHandler.updatePlayer(playerProfile);
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public boolean isRunning() {
        return task != null;
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public UpdateHandler getUpdateHandler() {
        return updateHandler;
    }
}
