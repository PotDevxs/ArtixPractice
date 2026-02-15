package dev.artixdev.practice;

import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.practice.managers.RankManager;

/**
 * Practice
 * Wrapper class for BoltPlugin to maintain compatibility
 */
public class Practice {
    
    private static Main plugin;
    private static RankManager rankManager;
    
    /**
     * Initialize Practice wrapper
     * @param boltPlugin the BoltPlugin instance
     */
    public static void initialize(Main boltPlugin) {
        plugin = boltPlugin;
        rankManager = new RankManager();
    }
    
    /**
     * Get plugin instance
     * @return BoltPlugin instance
     */
    public static Main getPlugin() {
        if (plugin == null) {
            plugin = Main.getInstance();
        }
        return plugin;
    }
    
    /**
     * Get RankManager
     * @return RankManager instance
     */
    public RankManager getRankManager() {
        if (rankManager == null) {
            rankManager = new RankManager();
        }
        return rankManager;
    }
    
    /**
     * Get Gson instance
     * @return Gson instance
     */
    public Gson getGson() {
        return Main.GSON;
    }

    public Object getServer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServer'");
    }
}
