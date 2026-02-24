package dev.artixdev.practice.tasks;

import org.bukkit.Bukkit;

public class TimerTask implements Runnable {
    public static final boolean DEBUG = false;
    public static final int TASK_ID = 3;
    private static final String[] MESSAGES = new String[] {"Timer task completed"};
    private static final String[] CONFIG_KEYS = new String[] {"timer.enabled"};

    @Override
    public void run() {
        if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
            // Optional: tick match timers, countdowns, etc. when plugin exposes them
        }
    }
}