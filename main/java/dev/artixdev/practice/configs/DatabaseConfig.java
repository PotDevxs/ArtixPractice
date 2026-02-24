package dev.artixdev.practice.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Database Configuration Manager
 * Handles all database settings and configuration
 */
public class DatabaseConfig {
    
    private static final Logger logger = LogManager.getLogger(DatabaseConfig.class);
    
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    
    // Database Type
    private String databaseType;
    
    // MongoDB Settings
    private boolean mongoUriMode;
    private boolean mongoUrlEncode;
    private String mongoHost;
    private int mongoPort;
    private String mongoDatabase;
    private boolean mongoAuthEnabled;
    private String mongoUsername;
    private String mongoPassword;
    private String mongoAuthDatabase;
    private String mongoConnectionString;
    private int mongoMinPoolSize;
    private int mongoMaxPoolSize;
    private int mongoMaxWaitTime;
    private int mongoMaxConnectionIdleTime;
    
    // MySQL Settings
    private String mysqlHost;
    private int mysqlPort;
    private String mysqlDatabase;
    private String mysqlUsername;
    private String mysqlPassword;
    private boolean mysqlSslEnabled;
    private int mysqlConnectionTimeout;
    private int mysqlMaxConnections;
    
    // SQLite Settings
    private String sqliteFilePath;
    private boolean sqliteBackupEnabled;
    private int sqliteBackupInterval;

    // Flatfile Settings (local JSON files)
    private String flatfileFolder;

    // General Settings
    private boolean autoReconnect;
    private int reconnectDelay;
    private int maxReconnectAttempts;
    private int queryTimeout;
    private boolean cacheEnabled;
    private int cacheSize;
    
    public DatabaseConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "database.yml");
    }
    
    /**
     * Load configuration from file
     */
    public void load() {
        try {
            if (!configFile.exists()) {
                plugin.saveResource("database.yml", false);
            }
            
            config = YamlConfiguration.loadConfiguration(configFile);
            
            // Database Type
            databaseType = config.getString("DATABASE_TYPE", "MONGODB");
            
            // MongoDB Settings
            mongoUriMode = config.getBoolean("MONGODB.URI_MODE", true);
            mongoUrlEncode = config.getBoolean("MONGODB.URL_ENCODE", false);
            mongoHost = config.getString("MONGODB.NORMAL.HOST", "127.0.0.1");
            mongoPort = config.getInt("MONGODB.NORMAL.PORT", 27017);
            mongoDatabase = config.getString("MONGODB.NORMAL.DATABASE", "artixpractice");
            mongoAuthEnabled = config.getBoolean("MONGODB.NORMAL.AUTHENTICATION.ENABLED", false);
            mongoUsername = config.getString("MONGODB.NORMAL.AUTHENTICATION.USERNAME", "");
            mongoPassword = config.getString("MONGODB.NORMAL.AUTHENTICATION.PASSWORD", "");
            mongoAuthDatabase = config.getString("MONGODB.NORMAL.AUTHENTICATION.AUTH_DATABASE", "admin");
            mongoConnectionString = config.getString("MONGODB.URI.CONNECTION_STRING", "mongodb://127.0.0.1:27017/artixpractice");
            mongoMinPoolSize = config.getInt("MONGODB.CONNECTION_POOL.MIN_SIZE", 2);
            mongoMaxPoolSize = config.getInt("MONGODB.CONNECTION_POOL.MAX_SIZE", 10);
            mongoMaxWaitTime = config.getInt("MONGODB.CONNECTION_POOL.MAX_WAIT_TIME", 30000);
            mongoMaxConnectionIdleTime = config.getInt("MONGODB.CONNECTION_POOL.MAX_CONNECTION_IDLE_TIME", 600000);
            
            // MySQL Settings
            mysqlHost = config.getString("MYSQL.HOST", "127.0.0.1");
            mysqlPort = config.getInt("MYSQL.PORT", 3306);
            mysqlDatabase = config.getString("MYSQL.DATABASE", "artixpractice");
            mysqlUsername = config.getString("MYSQL.USERNAME", "root");
            mysqlPassword = config.getString("MYSQL.PASSWORD", "");
            mysqlSslEnabled = config.getBoolean("MYSQL.SSL_ENABLED", false);
            mysqlConnectionTimeout = config.getInt("MYSQL.CONNECTION_TIMEOUT", 30000);
            mysqlMaxConnections = config.getInt("MYSQL.MAX_CONNECTIONS", 10);
            
            // SQLite Settings
            sqliteFilePath = config.getString("SQLITE.FILE_PATH", "plugins/Artix/database.db");
            sqliteBackupEnabled = config.getBoolean("SQLITE.BACKUP_ENABLED", true);
            sqliteBackupInterval = config.getInt("SQLITE.BACKUP_INTERVAL", 3600);

            // Flatfile Settings
            flatfileFolder = config.getString("FLATFILE.FOLDER", "data");

            // General Settings
            autoReconnect = config.getBoolean("SETTINGS.AUTO_RECONNECT", true);
            reconnectDelay = config.getInt("SETTINGS.RECONNECT_DELAY", 5000);
            maxReconnectAttempts = config.getInt("SETTINGS.MAX_RECONNECT_ATTEMPTS", 5);
            queryTimeout = config.getInt("SETTINGS.QUERY_TIMEOUT", 30000);
            cacheEnabled = config.getBoolean("SETTINGS.CACHE_ENABLED", true);
            cacheSize = config.getInt("SETTINGS.CACHE_SIZE", 1000);
            
            logger.info("Database configuration loaded successfully!");
            logger.info("Database Type: " + databaseType);
            
        } catch (Exception e) {
            logger.error("Failed to load database configuration!", e);
            throw new RuntimeException("Database configuration loading failed", e);
        }
    }
    
    /**
     * Save configuration to file
     */
    public void save() {
        try {
            config.save(configFile);
            logger.info("Database configuration saved successfully!");
        } catch (IOException e) {
            logger.error("Failed to save database configuration!", e);
        }
    }
    
    /**
     * Reload configuration from file
     */
    public void reload() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            load();
            logger.info("Database configuration reloaded successfully!");
        } catch (Exception e) {
            logger.error("Failed to reload database configuration!", e);
        }
    }
    
    // Getters
    public String getDatabaseType() {
        return databaseType;
    }
    
    public boolean isMongoUriMode() {
        return mongoUriMode;
    }
    
    public boolean isMongoUrlEncode() {
        return mongoUrlEncode;
    }
    
    public String getMongoHost() {
        return mongoHost;
    }
    
    public int getMongoPort() {
        return mongoPort;
    }
    
    public String getMongoDatabase() {
        return mongoDatabase;
    }
    
    public boolean isMongoAuthEnabled() {
        return mongoAuthEnabled;
    }
    
    public String getMongoUsername() {
        return mongoUsername;
    }
    
    public String getMongoPassword() {
        return mongoPassword;
    }
    
    public String getMongoAuthDatabase() {
        return mongoAuthDatabase;
    }
    
    public String getMongoConnectionString() {
        return mongoConnectionString;
    }
    
    public int getMongoMinPoolSize() {
        return mongoMinPoolSize;
    }
    
    public int getMongoMaxPoolSize() {
        return mongoMaxPoolSize;
    }
    
    public int getMongoMaxWaitTime() {
        return mongoMaxWaitTime;
    }
    
    public int getMongoMaxConnectionIdleTime() {
        return mongoMaxConnectionIdleTime;
    }
    
    public String getMysqlHost() {
        return mysqlHost;
    }
    
    public int getMysqlPort() {
        return mysqlPort;
    }
    
    public String getMysqlDatabase() {
        return mysqlDatabase;
    }
    
    public String getMysqlUsername() {
        return mysqlUsername;
    }
    
    public String getMysqlPassword() {
        return mysqlPassword;
    }
    
    public boolean isMysqlSslEnabled() {
        return mysqlSslEnabled;
    }
    
    public int getMysqlConnectionTimeout() {
        return mysqlConnectionTimeout;
    }
    
    public int getMysqlMaxConnections() {
        return mysqlMaxConnections;
    }
    
    public String getSqliteFilePath() {
        return sqliteFilePath;
    }
    
    public boolean isSqliteBackupEnabled() {
        return sqliteBackupEnabled;
    }
    
    public int getSqliteBackupInterval() {
        return sqliteBackupInterval;
    }

    public String getFlatfileFolder() {
        return flatfileFolder;
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }
    
    public int getReconnectDelay() {
        return reconnectDelay;
    }
    
    public int getMaxReconnectAttempts() {
        return maxReconnectAttempts;
    }
    
    public int getQueryTimeout() {
        return queryTimeout;
    }
    
    public boolean isCacheEnabled() {
        return cacheEnabled;
    }
    
    public int getCacheSize() {
        return cacheSize;
    }
}
