package dev.artixdev.practice.tasks;

import java.awt.image.BufferedImage;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.utils.ImageUtils;

public class ImageDisplayTask implements Runnable {
    
    private final Player targetPlayer;
    private final Location displayLocation;
    private final BufferedImage image;
    private final ImageUtils imageUtils;
    private final List<Player> viewers;
    private int ticks;
    private final int maxTicks;
    private BukkitTask task;
    private boolean cancelled;

    public ImageDisplayTask(List<Player> viewers, Player targetPlayer, Location displayLocation, int maxTicks, BufferedImage image, ImageUtils imageUtils) {
        this.imageUtils = imageUtils;
        this.ticks = 0;
        this.viewers = viewers;
        this.targetPlayer = targetPlayer;
        this.displayLocation = displayLocation;
        this.maxTicks = maxTicks;
        this.image = image;
    }

    public BukkitTask startTask(long delay, long period) {
        cancelled = false;
        this.task = Bukkit.getScheduler().runTaskTimer(Bukkit.getPluginManager().getPlugin("Practice"), this, delay, period);
        return this.task;
    }

    @Override
    public void run() {
        // Display image logic
        if (ticks >= maxTicks) {
            if (task != null) {
                task.cancel();
            }
            return;
        }
        
        // Process image display
        ticks++;
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
}
