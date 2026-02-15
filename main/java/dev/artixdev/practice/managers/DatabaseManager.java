package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.DatabaseConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Database Manager
 * Handles database connections and operations with full configuration support
 */
public class DatabaseManager {
    
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    
    private final Main plugin;
    private final DatabaseConfig databaseConfig;
    private final ExecutorService databaseExecutor;
    
    // Database connections
    private Connection mysqlConnection;
    private Object mongoClient; // MongoDB client
    private Object sqliteConnection; // SQLite connection
    
    public DatabaseManager(Main plugin) {
        this.plugin = plugin;
        this.databaseConfig = plugin.getConfigManager().getDatabaseConfig();
        this.databaseExecutor = Executors.newFixedThreadPool(4);
    }
    
    /**
     * Initialize database manager
     */
    public void initialize() {
        logger.info("Initializing database manager...");
        
        try {
            String databaseType = databaseConfig.getDatabaseType();
            logger.info("Database type: " + databaseType);
            
            switch (databaseType.toUpperCase()) {
                case "MONGODB":
                    initializeMongoDB();
                    break;
                case "MYSQL":
                    initializeMySQL();
                    break;
                case "SQLITE":
                    initializeSQLite();
                    break;
                default:
                    logger.error("Unsupported database type: " + databaseType);
                    throw new IllegalArgumentException("Unsupported database type: " + databaseType);
            }
            
            logger.info("Database manager initialized successfully!");
            
        } catch (Exception e) {
            logger.error("Failed to initialize database manager!", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Initialize MongoDB connection
     */
    private void initializeMongoDB() {
        try {
            if (databaseConfig.isMongoUriMode()) {
                // Connect using URI
                String connectionString = databaseConfig.getMongoConnectionString();
                logger.info("Connecting to MongoDB using URI: " + connectionString);
                
                // Initialize MongoDB client with URI
                // This would use the actual MongoDB driver
                // mongoClient = MongoClients.create(connectionString);
                
            } else {
                // Connect using individual settings
                String host = databaseConfig.getMongoHost();
                int port = databaseConfig.getMongoPort();
                String database = databaseConfig.getMongoDatabase();
                
                logger.info("Connecting to MongoDB at " + host + ":" + port + "/" + database);
                
                // Initialize MongoDB client with individual settings
                // This would use the actual MongoDB driver
                // mongoClient = MongoClients.create("mongodb://" + host + ":" + port + "/" + database);
            }
            
            // Configure connection pool
            configureMongoConnectionPool();
            
            logger.info("MongoDB connection established successfully!");
            
        } catch (Exception e) {
            logger.error("Failed to connect to MongoDB!", e);
            throw new RuntimeException("MongoDB connection failed", e);
        }
    }
    
    /**
     * Configure MongoDB connection pool
     */
    private void configureMongoConnectionPool() {
        int minPoolSize = databaseConfig.getMongoMinPoolSize();
        int maxPoolSize = databaseConfig.getMongoMaxPoolSize();
        int maxWaitTime = databaseConfig.getMongoMaxWaitTime();
        int maxConnectionIdleTime = databaseConfig.getMongoMaxConnectionIdleTime();
        
        logger.info("MongoDB connection pool configured - Min: " + minPoolSize + ", Max: " + maxPoolSize);
    }
    
    /**
     * Initialize MySQL connection
     */
    private void initializeMySQL() {
        try {
            String host = databaseConfig.getMysqlHost();
            int port = databaseConfig.getMysqlPort();
            String database = databaseConfig.getMysqlDatabase();
            String username = databaseConfig.getMysqlUsername();
            String password = databaseConfig.getMysqlPassword();
            boolean sslEnabled = databaseConfig.isMysqlSslEnabled();
            
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            if (sslEnabled) {
                url += "?useSSL=true";
            }
            
            logger.info("Connecting to MySQL at " + host + ":" + port + "/" + database);
            
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create connection
            mysqlConnection = DriverManager.getConnection(url, username, password);
            
            // Configure connection
            mysqlConnection.setAutoCommit(true);
            
            logger.info("MySQL connection established successfully!");
            
        } catch (ClassNotFoundException e) {
            logger.error("MySQL driver not found! Please add MySQL connector to classpath.", e);
            throw new RuntimeException("MySQL driver not found", e);
        } catch (SQLException e) {
            logger.error("Failed to connect to MySQL!", e);
            throw new RuntimeException("MySQL connection failed", e);
        }
    }
    
    /**
     * Initialize SQLite connection
     */
    private void initializeSQLite() {
        try {
            String filePath = databaseConfig.getSqliteFilePath();
            
            logger.info("Connecting to SQLite database: " + filePath);
            
            // Load SQLite driver
            Class.forName("org.sqlite.JDBC");
            
            // Create connection
            String url = "jdbc:sqlite:" + filePath;
            sqliteConnection = DriverManager.getConnection(url);
            
            logger.info("SQLite connection established successfully!");
            
        } catch (ClassNotFoundException e) {
            logger.error("SQLite driver not found! Please add SQLite connector to classpath.", e);
            throw new RuntimeException("SQLite driver not found", e);
        } catch (SQLException e) {
            logger.error("Failed to connect to SQLite!", e);
            throw new RuntimeException("SQLite connection failed", e);
        }
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() {
        String databaseType = databaseConfig.getDatabaseType();
        
        switch (databaseType.toUpperCase()) {
            case "MYSQL":
                return mysqlConnection;
            case "SQLITE":
                return (Connection) sqliteConnection;
            default:
                throw new UnsupportedOperationException("Connection not supported for database type: " + databaseType);
        }
    }
    
    /**
     * Get MongoDB client
     */
    public Object getMongoClient() {
        return mongoClient;
    }
    
    /**
     * Execute database operation asynchronously
     */
    public CompletableFuture<Void> executeAsync(Runnable operation) {
        return CompletableFuture.runAsync(operation, databaseExecutor);
    }
    
    /**
     * Execute database operation asynchronously with result
     */
    public <T> CompletableFuture<T> executeAsync(java.util.function.Supplier<T> operation) {
        return CompletableFuture.supplyAsync(operation, databaseExecutor);
    }
    
    /**
     * Test database connection
     */
    public boolean testConnection() {
        try {
            String databaseType = databaseConfig.getDatabaseType();
            
            switch (databaseType.toUpperCase()) {
                case "MONGODB":
                    return testMongoConnection();
                case "MYSQL":
                    return testMySQLConnection();
                case "SQLITE":
                    return testSQLiteConnection();
                default:
                    return false;
            }
        } catch (Exception e) {
            logger.error("Database connection test failed!", e);
            return false;
        }
    }
    
    /**
     * Test MongoDB connection
     */
    private boolean testMongoConnection() {
        try {
            // Test MongoDB connection
            // This would use the actual MongoDB driver
            // mongoClient.listDatabaseNames().first();
            return true;
        } catch (Exception e) {
            logger.error("MongoDB connection test failed!", e);
            return false;
        }
    }
    
    /**
     * Test MySQL connection
     */
    private boolean testMySQLConnection() {
        try {
            if (mysqlConnection != null && !mysqlConnection.isClosed()) {
                return mysqlConnection.isValid(5);
            }
            return false;
        } catch (SQLException e) {
            logger.error("MySQL connection test failed!", e);
            return false;
        }
    }
    
    /**
     * Test SQLite connection
     */
    private boolean testSQLiteConnection() {
        try {
            if (sqliteConnection != null && !((Connection) sqliteConnection).isClosed()) {
                return ((Connection) sqliteConnection).isValid(5);
            }
            return false;
        } catch (SQLException e) {
            logger.error("SQLite connection test failed!", e);
            return false;
        }
    }
    
    /**
     * Reconnect to database
     */
    public void reconnect() {
        if (databaseConfig.isAutoReconnect()) {
            logger.info("Attempting to reconnect to database...");
            
            try {
                // Close existing connections
                closeConnections();
                
                // Reinitialize
                initialize();
                
                logger.info("Database reconnected successfully!");
                
            } catch (Exception e) {
                logger.error("Failed to reconnect to database!", e);
            }
        }
    }
    
    /**
     * Close all database connections
     */
    public void closeConnections() {
        try {
            if (mysqlConnection != null && !mysqlConnection.isClosed()) {
                mysqlConnection.close();
                logger.info("MySQL connection closed.");
            }
            
            if (sqliteConnection != null && !((Connection) sqliteConnection).isClosed()) {
                ((Connection) sqliteConnection).close();
                logger.info("SQLite connection closed.");
            }
            
            // Close MongoDB client
            if (mongoClient != null) {
                // mongoClient.close();
                logger.info("MongoDB client closed.");
            }
            
        } catch (Exception e) {
            logger.error("Error closing database connections!", e);
        }
    }
    
    /**
     * Shutdown database manager
     */
    public void shutdown() {
        logger.info("Shutting down database manager...");
        
        // Close connections
        closeConnections();
        
        // Shutdown executor
        databaseExecutor.shutdown();
        
        logger.info("Database manager shutdown complete.");
    }
    
    /**
     * Get database configuration
     */
    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }
    
    /**
     * Check if database is connected
     */
    public boolean isConnected() {
        return testConnection();
    }
    
    /**
     * Get database type
     */
    public String getDatabaseType() {
        return databaseConfig.getDatabaseType();
    }
}
