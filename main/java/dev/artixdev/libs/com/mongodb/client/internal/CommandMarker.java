package dev.artixdev.libs.com.mongodb.client.internal;

import java.io.Closeable;
import java.util.Map;
import dev.artixdev.libs.com.mongodb.AutoEncryptionSettings;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoTimeoutException;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.MongoClient;
import dev.artixdev.libs.com.mongodb.client.MongoClients;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoCrypt;
import dev.artixdev.libs.com.mongodb.internal.capi.MongoCryptHelper;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.conversions.Bson;

class CommandMarker implements Closeable {
   @Nullable
   private final MongoClient client;
   @Nullable
   private final ProcessBuilder processBuilder;

   CommandMarker(MongoCrypt mongoCrypt, AutoEncryptionSettings settings) {
      if (MongoCryptHelper.isMongocryptdSpawningDisabled(mongoCrypt.getCryptSharedLibVersionString(), settings)) {
         this.processBuilder = null;
         this.client = null;
      } else {
         Map<String, Object> extraOptions = settings.getExtraOptions();
         boolean mongocryptdBypassSpawn = (Boolean)extraOptions.getOrDefault("mongocryptdBypassSpawn", false);
         if (!mongocryptdBypassSpawn) {
            this.processBuilder = MongoCryptHelper.createProcessBuilder(extraOptions);
            MongoCryptHelper.startProcess(this.processBuilder);
         } else {
            this.processBuilder = null;
         }

         this.client = MongoClients.create(MongoCryptHelper.createMongocryptdClientSettings((String)extraOptions.get("mongocryptdURI")));
      }

   }

   RawBsonDocument mark(String databaseName, RawBsonDocument command) {
      if (this.client != null) {
         try {
            try {
               return this.executeCommand(databaseName, command);
            } catch (MongoTimeoutException e) {
               if (this.processBuilder == null) {
                  throw e;
               } else {
                  MongoCryptHelper.startProcess(this.processBuilder);
                  return this.executeCommand(databaseName, command);
               }
            }
         } catch (MongoException e) {
            throw this.wrapInClientException(e);
         }
      } else {
         return command;
      }
   }

   public void close() {
      if (this.client != null) {
         this.client.close();
      }

   }

   private RawBsonDocument executeCommand(String databaseName, RawBsonDocument markableCommand) {
      Assertions.assertNotNull(this.client);
      return (RawBsonDocument)this.client.getDatabase(databaseName).withReadConcern(ReadConcern.DEFAULT).withReadPreference(ReadPreference.primary()).runCommand((Bson)markableCommand, (Class)RawBsonDocument.class);
   }

   private MongoClientException wrapInClientException(MongoException e) {
      return new MongoClientException("Exception in encryption library: " + e.getMessage(), e);
   }
}
