package dev.artixdev.practice.storage;

import java.util.List;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import dev.artixdev.practice.models.PlayerProfile;

/**
 * MongoDB Storage Interface
 * Defines the contract for MongoDB storage operations
 */
public interface MongoStorageInterface {
   
   /**
    * Load all player profiles from a collection
    * @param collectionName the collection name
    * @return list of player profiles
    */
   List<PlayerProfile> loadAllPlayerProfiles(String collectionName);

   /**
    * Get MongoDB database instance
    * @param databaseName the database name
    * @return MongoDatabase instance
    */
   MongoDatabase getDatabase(String databaseName);

   /**
    * Convert Document to PlayerProfile
    * @param document the BSON document
    * @return PlayerProfile object
    */
   PlayerProfile documentToPlayerProfile(Document document);
}
