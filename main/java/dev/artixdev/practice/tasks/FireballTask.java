package dev.artixdev.practice.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Fireball;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;

public class FireballTask implements Runnable {
    private final Fireball fireball;
    private BukkitTask task;
    private final PlayerProfile playerProfile;
    public static final boolean DEBUG = false;
    public static final int TASK_ID = 1;
    private final FireballHandler fireballHandler;

    public void run() {
        if (fireball == null || fireball.isDead()) {
            cancel();
            return;
        }
        
        // Update fireball physics and effects
        fireballHandler.updateFireball(fireball);
        
        // Check if fireball should be removed
        if (fireballHandler.shouldRemoveFireball(fireball)) {
            fireball.remove();
            cancel();
        }
    }

    public void start(Main plugin) {
        if (task == null) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            task = scheduler.runTaskTimer(plugin, this, 0L, 1L);
        }
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public boolean isRunning() {
        if (task == null) {
            return false;
        }
        int taskId = task.getTaskId();
        return Bukkit.getScheduler().isQueued(taskId) || Bukkit.getScheduler().isCurrentlyRunning(taskId);
    }

    public Fireball getFireball() {
        return fireball;
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public FireballHandler getFireballHandler() {
        return fireballHandler;
    }

    public FireballTask(PlayerProfile playerProfile, FireballHandler fireballHandler, Fireball fireball) {
        this.playerProfile = playerProfile;
        this.fireballHandler = fireballHandler;
        this.fireball = fireball;
    }
}
