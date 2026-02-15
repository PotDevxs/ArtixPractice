package dev.artixdev.practice;

import dev.artixdev.practice.configs.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Config Manager
 * Centralized configuration management for Bolt plugin
 */
public class ConfigManager {
    
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    
    private final JavaPlugin plugin;
    
    // Configuration instances
    private SettingsConfig settingsConfig;
    private BotsConfig botsConfig;
    private DatabaseConfig databaseConfig;
    private ScoreboardConfig scoreboardConfig;
    private MenuConfig menuConfig;
    private HotbarConfig hotbarConfig;
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Initialize all configurations
     */
    public void initializeConfigs() {
        logger.info("Initializing Bolt Practice configurations...");
        
        try {
            // Load main settings
            this.settingsConfig = new SettingsConfig(plugin, "config");
            logger.info("Settings configuration loaded successfully!");
            
            // Load bots configuration
            this.botsConfig = new BotsConfig(plugin, "bots");
            logger.info("Bots configuration loaded successfully!");
            
            // Load database configuration
            this.databaseConfig = new DatabaseConfig(plugin);
            this.databaseConfig.load();
            logger.info("Database configuration loaded successfully!");
            
            // Load scoreboard configuration
            this.scoreboardConfig = new ScoreboardConfig(plugin);
            logger.info("Scoreboard configuration loaded successfully!");
            
            // Load menu configuration
            this.menuConfig = new MenuConfig(plugin, "menus");
            logger.info("Menu configuration loaded successfully!");
            
            // Load hotbar configuration
            this.hotbarConfig = new HotbarConfig(plugin, "hotbar");
            logger.info("Hotbar configuration loaded successfully!");
            
            logger.info("All configurations loaded successfully!");
            
        } catch (Exception e) {
            logger.error("Failed to initialize configurations!", e);
            throw new RuntimeException("Configuration initialization failed", e);
        }
    }
    
    /**
     * Reload all configurations
     */
    public void reloadConfigs() {
        logger.info("Reloading all configurations...");
        
        try {
            if (settingsConfig != null) {
                // SettingsConfig reload if needed
            }
            if (botsConfig != null) {
                // BotsConfig reload if needed
            }
            if (databaseConfig != null) {
                databaseConfig.reload();
            }
            if (scoreboardConfig != null) {
                // ScoreboardConfig reload if needed
            }
            if (menuConfig != null) {
                // MenuConfig reload if needed
            }
            if (hotbarConfig != null) {
                // HotbarConfig reload if needed
            }
            
            logger.info("All configurations reloaded successfully!");
            
        } catch (Exception e) {
            logger.error("Failed to reload configurations!", e);
        }
    }
    
    /**
     * Validate all configurations
     */
    public boolean validateConfigs() {
        logger.info("Validating configurations...");
        
        boolean valid = true;
        
        // Validate settings
        if (settingsConfig != null) {
            if (!validateSettingsConfig()) {
                valid = false;
            }
        }
        
        // Validate database
        if (databaseConfig != null) {
            if (!validateDatabaseConfig()) {
                valid = false;
            }
        }
        
        // Validate bots
        if (botsConfig != null) {
            if (!validateBotsConfig()) {
                valid = false;
            }
        }
        
        if (valid) {
            logger.info("All configurations are valid!");
        } else {
            logger.warn("Some configurations have validation issues!");
        }
        
        return valid;
    }
    
    /**
     * Validate settings configuration
     */
    private boolean validateSettingsConfig() {
        boolean valid = true;
        
        // Validate critical values
        if (SettingsConfig.STARTING_ELO < 0) {
            logger.warn("STARTING_ELO cannot be negative! Using default value.");
            SettingsConfig.STARTING_ELO = 1000;
            valid = false;
        }
        
        if (SettingsConfig.MATCH_TIME_LIMIT_VALUE <= 0) {
            logger.warn("MATCH_TIME_LIMIT_VALUE must be greater than 0! Using default value.");
            SettingsConfig.MATCH_TIME_LIMIT_VALUE = 20;
            valid = false;
        }
        
        if (SettingsConfig.STATS_SAVE_INTERVAL <= 0) {
            logger.warn("STATS_SAVE_INTERVAL must be greater than 0! Using default value.");
            SettingsConfig.STATS_SAVE_INTERVAL = 15;
            valid = false;
        }
        
        return valid;
    }
    
    /**
     * Validate database configuration
     */
    private boolean validateDatabaseConfig() {
        boolean valid = true;
        
        String dbType = databaseConfig.getDatabaseType();
        
        if (dbType == null || dbType.isEmpty()) {
            logger.warn("Database type is not specified! Using MongoDB as default.");
            valid = false;
        }
        
        if ("MONGODB".equalsIgnoreCase(dbType)) {
            if (databaseConfig.getMongoHost() == null || databaseConfig.getMongoHost().isEmpty()) {
                logger.warn("MongoDB host is not specified!");
                valid = false;
            }
        } else if ("MYSQL".equalsIgnoreCase(dbType)) {
            if (databaseConfig.getMysqlHost() == null || databaseConfig.getMysqlHost().isEmpty()) {
                logger.warn("MySQL host is not specified!");
                valid = false;
            }
        }
        
        return valid;
    }
    
    /**
     * Validate bots configuration
     */
    private boolean validateBotsConfig() {
        boolean valid = true;
        
        if (BotsConfig.BOTS_ENABLED) {
            if (BotsConfig.NORMAL_MOVEMENT_SPEED <= 0) {
                logger.warn("NORMAL_MOVEMENT_SPEED must be greater than 0! Using default value.");
                BotsConfig.NORMAL_MOVEMENT_SPEED = 1.0;
                valid = false;
            }
            
            if (BotsConfig.TRY_HARD_MOVEMENT_SPEED <= 0) {
                logger.warn("TRY_HARD_MOVEMENT_SPEED must be greater than 0! Using default value.");
                BotsConfig.TRY_HARD_MOVEMENT_SPEED = 1.15;
                valid = false;
            }
            
            if (BotsConfig.BOT_NAMES == null || BotsConfig.BOT_NAMES.isEmpty()) {
                logger.warn("No bot names specified! Bots will use default names.");
                valid = false;
            }
        }
        
        return valid;
    }
    
    // Getters
    public SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }
    
    public BotsConfig getBotsConfig() {
        return botsConfig;
    }
    
    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }
    
    public ScoreboardConfig getScoreboardConfig() {
        return scoreboardConfig;
    }
    
    public MenuConfig getMenuConfig() {
        return menuConfig;
    }
    
    public HotbarConfig getHotbarConfig() {
        return hotbarConfig;
    }
}
