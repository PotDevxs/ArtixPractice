package dev.artixdev.practice.utils;

import dev.artixdev.practice.Main;

/**
 * Update Checker
 * Checks for plugin updates (stub: no remote check; override or extend for real API).
 */
public class UpdateChecker {

    private final Main plugin;

    public UpdateChecker(Main plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdates() {
        String current = plugin.getDescription().getVersion();
        if (current == null) current = "0.0.0";
        plugin.getLogger().info("ArtixPractice v" + current + " - Update check skipped (no resource ID configured).");
    }

    /** Override to perform actual version check; return null if none or unknown. */
    public String getLatestVersion() {
        return null;
    }
}
