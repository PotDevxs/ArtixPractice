package dev.artixdev.practice.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import dev.artixdev.practice.Practice;
import dev.artixdev.practice.models.Match;

public class MatchUpdateTask implements Runnable {
    private final Match match;
    public static final int TASK_VERSION = 1;
    private final BukkitTask task;
    public static final boolean DEBUG_MODE = false;

    public BukkitTask getTask() {
        return this.task;
    }

    public Match getMatch() {
        return this.match;
    }

    public MatchUpdateTask(Match match) {
        this.match = match;
        BukkitScheduler scheduler = Bukkit.getScheduler();
        BukkitTask task = scheduler.runTaskTimer(Practice.getPlugin(), this, 0L, 1L);
        this.task = task;
    }

    private void updatePlayer(Player player) {
        if (player != null) {
            if (!this.match.isPaused()) {
                if (this.match.getTimeLeft() != 0) {
                    if (this.match.getTimeLeft() <= 0) {
                        // Handle match end
                        if (this.match.getTimeLeft() == 0) {
                            this.match.endMatch();
                            Bukkit.getScheduler().runTaskLater(Practice.getPlugin(), () -> {
                                // Handle match end logic
                            }, 1L);
                        }
                    } else {
                        this.match.decrementTimeLeft();
                        if (this.match.getTimeLeft() == 0) {
                            this.match.endMatch();
                            Bukkit.getScheduler().runTaskLater(Practice.getPlugin(), () -> {
                                // Handle match end logic
                            }, 1L);
                        }
                    }
                } else {
                    this.match.updateMatch();
                }
            }
        }
    }

    @Override
    public void run() {
        if (!this.match.isPaused()) {
            Player player = this.match.getPlayer();
            this.updatePlayer(player);
            this.match.incrementMatchTime();
            this.match.decrementTimeLeft();
        } else {
            this.task.cancel();
        }
    }
}
