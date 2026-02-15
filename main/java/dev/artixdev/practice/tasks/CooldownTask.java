package dev.artixdev.practice.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;

public class CooldownTask implements Runnable {
    private final PlayerProfile playerProfile;
    private static final String[] MESSAGES = new String[0];
    private BukkitTask task;
    public static final int TASK_ID = 2;
    public static final boolean DEBUG = false;
    private static final String[] CONFIG_KEYS = new String[0];
    private int ticks = 5;
    private boolean cancelled;

    public CooldownTask(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public void start(Main plugin, long delay, long period) {
        if (task == null) {
            cancelled = false;
            task = Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period);
        }
    }

    public void run() {
        if (playerProfile == null) {
            cancel();
            return;
        }
        
        // Update cooldowns
        playerProfile.updateCooldowns();
        
        // TODO: Check if all cooldowns are finished when getActiveCooldowns() is implemented
        // The getActiveCooldowns() method doesn't exist on PlayerProfile yet
        
        ticks--;
        if (ticks <= 0) {
            ticks = 5; // Reset tick counter
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
        return Bukkit.getScheduler().isQueued(taskId) || Bukkit.getScheduler().isCurrentlyRunning(taskId);
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
}