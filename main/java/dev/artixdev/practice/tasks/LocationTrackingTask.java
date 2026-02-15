package dev.artixdev.practice.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.managers.LocationTrackingManager;
import dev.artixdev.practice.models.Match;

public class LocationTrackingTask implements Runnable {
    public static final int TASK_VERSION = 1;
    private final Match match;
    public static final boolean DEBUG_MODE = false;
    private BukkitTask task;

    public LocationTrackingTask(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
        if (!this.match.isActive()) {
            if (this.match.getPlayer() != null) {
                Player player = this.match.getPlayer();
                if (player != null) {
                    Location location = player.getLocation();
                    this.trackLocation(location, player);
                }
            }
        } else {
            this.task.cancel();
        }
    }

    public void trackLocation(Location location, Player player) {
        Object trackerObj = this.match.getLocationTracker();
        if (trackerObj instanceof LocationTrackingManager) {
            try {
                LocationTrackingManager tracker = (LocationTrackingManager) trackerObj;
                tracker.trackLocation(player, location);
            } catch (Throwable e) {
                // Handle error
            }
        }
    }

    public void startTask(Main plugin, long delay, long period) {
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period);
    }
}
