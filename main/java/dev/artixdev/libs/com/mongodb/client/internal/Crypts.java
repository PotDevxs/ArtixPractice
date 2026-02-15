package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.Map;
import javax.net.ssl.SSLContext;
import dev.artixdev.libs.com.mongodb.AutoEncryptionSettings;
import dev.artixdev.libs.com.mongodb.ClientEncryptionSettings;
import dev.artixdev.libs.com.mongodb.MongoClientSettings;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.client.MongoClient;
import dev.artixdev.libs.com.mongodb.client.MongoClients;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoCrypt;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoCrypts;
import dev.artixdev.libs.com.mongodb.internal.capi.MongoCryptHelper;

public final class Crypts {
   public static Crypt createCrypt(MongoClientImpl client, AutoEncryptionSettings settings) {
      MongoClient sharedInternalClient = null;
      MongoClientSettings keyVaultMongoClientSettings = settings.getKeyVaultMongoClientSettings();
      if (keyVaultMongoClientSettings == null || !settings.isBypassAutoEncryption()) {
         MongoClientSettings defaultInternalMongoClientSettings = MongoClientSettings.builder(client.getSettings()).applyToConnectionPoolSettings((builder) -> {
            builder.minSize(0);
         }).autoEncryptionSettings((AutoEncryptionSettings)null).build();
         sharedInternalClient = MongoClients.create(defaultInternalMongoClientSettings);
      }

      MongoClient keyVaultClient = keyVaultMongoClientSettings == null ? sharedInternalClient : MongoClients.create(keyVaultMongoClientSettings);
      MongoCrypt mongoCrypt = MongoCrypts.create(MongoCryptHelper.createMongoCryptOptions(settings));
      return new Crypt(mongoCrypt, createKeyRetriever(keyVaultClient, settings.getKeyVaultNamespace()), createKeyManagementService(settings.getKmsProviderSslContextMap()), settings.getKmsProviders(), settings.getKmsProviderPropertySuppliers(), settings.isBypassAutoEncryption(), settings.isBypassAutoEncryption() ? null : new CollectionInfoRetriever(sharedInternalClient), new CommandMarker(mongoCrypt, settings), sharedInternalClient, keyVaultClient);
   }

   static Crypt create(MongoClient keyVaultClient, ClientEncryptionSettings settings) {
      return new Crypt(MongoCrypts.create(MongoCryptHelper.createMongoCryptOptions(settings)), createKeyRetriever(keyVaultClient, settings.getKeyVaultNamespace()), createKeyManagementService(settings.getKmsProviderSslContextMap()), settings.getKmsProviders(), settings.getKmsProviderPropertySuppliers());
   }

   private static KeyRetriever createKeyRetriever(MongoClient keyVaultClient, String keyVaultNamespaceString) {
      return new KeyRetriever(keyVaultClient, new MongoNamespace(keyVaultNamespaceString));
   }

   private static KeyManagementService createKeyManagementService(Map<String, SSLContext> kmsProviderSslContextMap) {
      return new KeyManagementService(kmsProviderSslContextMap, 10000);
   }

   private Crypts() {
   }
}
