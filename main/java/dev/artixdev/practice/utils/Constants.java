package dev.artixdev.practice.utils;

public class Constants {
    
    public static final String PLUGIN_NAME = "BoltPractice";
    public static final String PLUGIN_VERSION = "1.0.0";
    public static final String PLUGIN_AUTHOR = "RefineDev";
    
    // Database
    public static final String DATABASE_NAME = "bolt_practice";
    public static final int DATABASE_PORT = 3306;
    
    // Arena
    public static final int MAX_ARENAS = 100;
    public static final int MIN_ARENA_SIZE = 10;
    public static final int MAX_ARENA_SIZE = 1000;
    
    // Kit
    public static final int MAX_KITS = 50;
    public static final int MAX_KIT_ITEMS = 36;
    
    // Match
    public static final int MAX_MATCH_DURATION = 1800; // 30 minutes
    public static final int MATCH_COUNTDOWN = 10;
    public static final int MATCH_END_COUNTDOWN = 5;
    
    // Queue
    public static final int MAX_QUEUE_SIZE = 1000;
    public static final int QUEUE_UPDATE_INTERVAL = 20; // 1 second
    
    // Bot
    public static final int MAX_BOTS = 100;
    public static final int BOT_UPDATE_INTERVAL = 20; // 1 second
    
    // Hologram
    public static final int MAX_HOLOGRAMS = 50;
    public static final double HOLOGRAM_UPDATE_INTERVAL = 1.0; // 1 second
    
    // Statistics
    public static final int STATS_SAVE_INTERVAL = 300; // 5 minutes
    public static final int LEADERBOARD_UPDATE_INTERVAL = 600; // 10 minutes
    
    // Cooldowns
    public static final int DEFAULT_COOLDOWN = 3; // 3 seconds
    public static final int MAX_COOLDOWN = 60; // 1 minute
    
    // Messages
    public static final String PREFIX = "&b[BoltPractice] &7";
    public static final String ERROR_PREFIX = "&c[Error] &7";
    public static final String SUCCESS_PREFIX = "&a[Success] &7";
    public static final String WARNING_PREFIX = "&e[Warning] &7";
    
    // Permissions
    public static final String ADMIN_PERMISSION = "bolt.admin";
    public static final String MODERATOR_PERMISSION = "bolt.moderator";
    public static final String VIP_PERMISSION = "bolt.vip";
    
    // Files
    public static final String CONFIG_FILE = "config.yml";
    public static final String MESSAGES_FILE = "messages.yml";
    public static final String ARENAS_FILE = "arenas.yml";
    public static final String KITS_FILE = "kits.yml";
    public static final String BOTS_FILE = "bots.yml";
    
    private Constants() {
        // Utility class
    }
}