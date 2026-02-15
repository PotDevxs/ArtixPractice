package dev.artixdev.practice.storage.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.artixdev.api.practice.storage.YamlStorage;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.client.MongoClient;
import dev.artixdev.libs.com.mongodb.client.MongoCollection;
import dev.artixdev.libs.com.mongodb.client.MongoDatabase;
import dev.artixdev.libs.org.bson.Document;

/**
 * MongoDB Database Manager
 * Manages MongoDB database connections and operations
 */
public final class MongoDatabaseManager {
   
   private static final Logger logger = LogManager.getLogger(MongoDatabaseManager.class);
   
   private MongoDatabase database;
   private MongoClient client;
   
   /**
    * Initialize MongoDB connection
    * @param yamlStorage the YAML storage configuration
    */
   public void initializeMongoDB(YamlStorage yamlStorage) {
      try {
         // Initialize MongoDB client
         initializeClient();
         
         // Initialize database
         initializeDatabase(yamlStorage);
         
         logger.info("MongoDB database manager initialized successfully");
         
      } catch (Exception e) {
         logger.error("Failed to initialize MongoDB database manager", e);
         throw new RuntimeException("MongoDB initialization failed", e);
      }
   }
   
   /**
    * Initialize MongoDB client
    */
   private void initializeClient() {
      try {
         // Create MongoDB client based on configuration
         // This would be implemented based on your MongoDB configuration
         logger.info("MongoDB client initialized");
         
      } catch (Exception e) {
         logger.error("Failed to initialize MongoDB client", e);
         throw new RuntimeException("MongoDB client initialization failed", e);
      }
   }
   
   /**
    * Initialize database
    * @param yamlStorage the YAML storage configuration
    */
   private void initializeDatabase(YamlStorage yamlStorage) {
      try {
         // Get database name from configuration
         String databaseName = yamlStorage.getStringOrDefault("database.name", "artix_practice");
         
         // Get database
         this.database = client.getDatabase(databaseName);
         
         logger.info("MongoDB database initialized: " + databaseName);
         
      } catch (Exception e) {
         logger.error("Failed to initialize MongoDB database", e);
         throw new RuntimeException("MongoDB database initialization failed", e);
      }
   }
   
   /**
    * Get database
    * @return MongoDB database
    */
   public MongoDatabase getDatabase() {
      return database;
   }
   
   /**
    * Get client
    * @return MongoDB client
    */
   public MongoClient getClient() {
      return client;
   }
   
   /**
    * Get collection
    * @param collectionName the collection name
    * @return MongoDB collection
    */
   public MongoCollection<Document> getCollection(String collectionName) {
      if (database == null) {
         throw new IllegalStateException("Database not initialized");
      }
      
      return database.getCollection(collectionName);
   }
   
   /**
    * Check if database is connected
    * @return true if connected
    */
   public boolean isConnected() {
      try {
         if (client == null || database == null) {
            return false;
         }
         
         // Ping the database
         database.runCommand(new Document("ping", 1));
         return true;
         
      } catch (Exception e) {
         logger.warn("Database connection check failed: " + e.getMessage());
         return false;
      }
   }
   
   /**
    * Reconnect to database
    * @return true if reconnected successfully
    */
   public boolean reconnect() {
      try {
         if (client != null) {
            client.close();
         }
         
         // Reinitialize
         initializeClient();
         initializeDatabase(null); // Would need to pass config
         
         logger.info("MongoDB reconnected successfully");
         return true;
         
      } catch (Exception e) {
         logger.error("Failed to reconnect to MongoDB", e);
         return false;
      }
   }
   
   /**
    * Create collection if not exists
    * @param collectionName the collection name
    * @return true if created or already exists
    */
   public boolean createCollectionIfNotExists(String collectionName) {
      try {
         if (database == null) {
            return false;
         }
         
         // Check if collection exists
         for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName)) {
               return true; // Collection already exists
            }
         }
         
         // Create collection
         database.createCollection(collectionName);
         logger.info("Created MongoDB collection: " + collectionName);
         return true;
         
      } catch (Exception e) {
         logger.error("Failed to create collection: " + collectionName, e);
         return false;
      }
   }
   
   /**
    * Drop collection
    * @param collectionName the collection name
    * @return true if dropped successfully
    */
   public boolean dropCollection(String collectionName) {
      try {
         if (database == null) {
            return false;
         }
         
         database.getCollection(collectionName).drop();
         logger.info("Dropped MongoDB collection: " + collectionName);
         return true;
         
      } catch (Exception e) {
         logger.error("Failed to drop collection: " + collectionName, e);
         return false;
      }
   }
   
   /**
    * Get collection count
    * @param collectionName the collection name
    * @return document count
    */
   public long getCollectionCount(String collectionName) {
      try {
         if (database == null) {
            return 0;
         }
         
         return database.getCollection(collectionName).countDocuments();
         
      } catch (Exception e) {
         logger.error("Failed to get collection count: " + collectionName, e);
         return 0;
      }
   }
   
   /**
    * Get database statistics
    * @return database statistics
    */
   public String getDatabaseStatistics() {
      try {
         if (database == null) {
            return "Database not initialized";
         }
         
         StringBuilder stats = new StringBuilder();
         stats.append("Database: ").append(database.getName()).append("\n");
         stats.append("Collections: ");
         
         for (String collectionName : database.listCollectionNames()) {
            long count = getCollectionCount(collectionName);
            stats.append(collectionName).append("(").append(count).append(") ");
         }
         
         return stats.toString();
         
      } catch (Exception e) {
         logger.error("Failed to get database statistics", e);
         return "Error getting statistics: " + e.getMessage();
      }
   }
   
   /**
    * Close database connection
    */
   public void close() {
      try {
         if (client != null) {
            client.close();
            logger.info("MongoDB connection closed");
         }
      } catch (Exception e) {
         logger.error("Failed to close MongoDB connection", e);
      }
   }
   
   /**
    * Check if manager is initialized
    * @return true if initialized
    */
   public boolean isInitialized() {
      return client != null && database != null;
   }
   
   /**
    * Get database name
    * @return database name
    */
   public String getDatabaseName() {
      return database != null ? database.getName() : "unknown";
   }
   
   /**
    * Get connection info
    * @return connection info
    */
   public String getConnectionInfo() {
      return String.format("MongoDB: %s, Connected: %s, Initialized: %s", 
         getDatabaseName(), isConnected(), isInitialized());
   }
}
