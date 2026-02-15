package dev.artixdev.libs.com.mongodb.client;

import dev.artixdev.libs.com.mongodb.ConnectionString;
import dev.artixdev.libs.com.mongodb.MongoClientSettings;
import dev.artixdev.libs.com.mongodb.MongoDriverInformation;
import dev.artixdev.libs.com.mongodb.client.internal.MongoClientImpl;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class MongoClients {
   public static MongoClient create() {
      return create(new ConnectionString("mongodb://localhost"));
   }

   public static MongoClient create(MongoClientSettings settings) {
      return create((MongoClientSettings)settings, (MongoDriverInformation)null);
   }

   public static MongoClient create(String connectionString) {
      return create(new ConnectionString(connectionString));
   }

   public static MongoClient create(ConnectionString connectionString) {
      return create((ConnectionString)connectionString, (MongoDriverInformation)null);
   }

   public static MongoClient create(ConnectionString connectionString, @Nullable MongoDriverInformation mongoDriverInformation) {
      return create(MongoClientSettings.builder().applyConnectionString(connectionString).build(), mongoDriverInformation);
   }

   public static MongoClient create(MongoClientSettings settings, @Nullable MongoDriverInformation mongoDriverInformation) {
      MongoDriverInformation.Builder builder = mongoDriverInformation == null ? MongoDriverInformation.builder() : MongoDriverInformation.builder(mongoDriverInformation);
      return new MongoClientImpl(settings, builder.driverName("sync").build());
   }

   private MongoClients() {
   }
}
