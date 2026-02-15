package dev.artixdev.practice.utils;

import dev.artixdev.practice.Main;

/**
 * Update Checker
 * Checks for plugin updates
 */
public class UpdateChecker {
    
    private final Main plugin;
    
    public UpdateChecker(Main plugin) {
        this.plugin = plugin;
    }
    
    public void checkForUpdates() {
        // TODO: Implement update checking
        plugin.getLogger().info("Update checker started!");
    }
}
