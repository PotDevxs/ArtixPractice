package dev.artixdev.practice.handlers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.managers.PlayerManager;
import dev.artixdev.practice.models.PlayerProfile;

/**
 * UpdateHandler
 * 
 * Handles updates for player profiles.
 * This is a singleton class that manages periodic updates of player data.
 * 
 * @author RefineDev
 */
public class UpdateHandler {
    private static UpdateHandler instance;
    private final PlayerManager playerManager;

    private UpdateHandler() {
        Main plugin = Main.getInstance();
        if (plugin == null) {
            throw new IllegalStateException("ArtixPlugin instance is not available");
        }
        this.playerManager = plugin.getPlayerManager();
    }

    /**
     * Get the singleton instance of UpdateHandler.
     * 
     * @return the UpdateHandler instance
     */
    public static UpdateHandler getInstance() {
        if (instance == null) {
            synchronized (UpdateHandler.class) {
                if (instance == null) {
                    instance = new UpdateHandler();
                }
            }
        }
        return instance;
    }

    /**
     * Update a player profile.
     * This method saves the player profile to storage and updates any cached data.
     * 
     * @param profile the player profile to update
     */
    public void updatePlayer(PlayerProfile profile) {
        if (profile == null) {
            return;
        }

        try {
            // Save profile to storage
            if (playerManager != null) {
                playerManager.savePlayerProfile(profile);
            }
        } catch (Exception e) {
            Main plugin = Main.getInstance();
            if (plugin != null) {
                plugin.getLogger().warning("Failed to update player profile: " + 
                    (profile.getUniqueId() != null ? profile.getUniqueId().toString() : "unknown"));
            }
        }
    }

    /**
     * Get the PlayerManager instance.
     * 
     * @return the PlayerManager instance
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
