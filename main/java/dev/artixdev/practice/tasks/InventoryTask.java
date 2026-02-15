package dev.artixdev.practice.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;

public class InventoryTask implements Runnable {
    public static final boolean DEBUG = false;
    private BukkitTask task;
    private final PlayerProfile playerProfile;
    private static final String[] MESSAGES = new String[] {"Inventory task started"};
    private int ticks;
    private static final String[] CONFIG_KEYS = new String[] {"inventory.update_interval"};
    public static final int TASK_ID = 7;
    private boolean cancelled;

    private static String generateMessage(int param0, int param1) {
        // Generate message based on parameters
        return "Inventory task message";
    }

    public BukkitTask start(Main plugin, long delay, long period) {
        if (task == null) {
            cancelled = false;
            task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, delay, period);
        }
        return task;
    }

    public void run() {
        if (playerProfile == null) {
            cancel();
            return;
        }
        
        // Update player inventory
        updateInventory();
        
        ticks++;
        if (ticks >= 20) { // Every second
            ticks = 0;
            // Perform periodic inventory updates
            performPeriodicUpdate();
        }
    }

    private void updateInventory() {
        // Update inventory contents
        playerProfile.updateInventory();
        
        // Update inventory effects
        playerProfile.updateInventoryEffects();
        
        // Update inventory cooldowns
        playerProfile.updateInventoryCooldowns();
    }

    private void performPeriodicUpdate() {
        // Perform periodic inventory maintenance
        playerProfile.cleanupInventory();
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

    public InventoryTask(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
        this.ticks = 0;
    }
}
