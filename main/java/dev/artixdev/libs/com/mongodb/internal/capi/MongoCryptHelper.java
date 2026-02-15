package dev.artixdev.libs.com.mongodb.internal.capi;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.AutoEncryptionSettings;
import dev.artixdev.libs.com.mongodb.AwsCredential;
import dev.artixdev.libs.com.mongodb.ClientEncryptionSettings;
import dev.artixdev.libs.com.mongodb.ConnectionString;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoClientSettings;
import dev.artixdev.libs.com.mongodb.MongoConfigurationException;
import dev.artixdev.libs.com.mongodb.client.model.vault.RewrapManyDataKeyOptions;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoCryptOptions;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoCryptOptions.Builder;
import dev.artixdev.libs.com.mongodb.internal.authentication.AwsCredentialHelper;
import dev.artixdev.libs.com.mongodb.internal.authentication.AzureCredentialHelper;
import dev.artixdev.libs.com.mongodb.internal.authentication.GcpCredentialHelper;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWrapper;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.codecs.DocumentCodec;

public final class MongoCryptHelper {
   public static MongoCryptOptions createMongoCryptOptions(ClientEncryptionSettings settings) {
      return createMongoCryptOptions(settings.getKmsProviders(), false, Collections.emptyList(), Collections.emptyMap(), (Map)null, (Map)null);
   }

   public static MongoCryptOptions createMongoCryptOptions(AutoEncryptionSettings settings) {
      return createMongoCryptOptions(settings.getKmsProviders(), settings.isBypassQueryAnalysis(), settings.isBypassAutoEncryption() ? Collections.emptyList() : Collections.singletonList("$SYSTEM"), settings.getExtraOptions(), settings.getSchemaMap(), settings.getEncryptedFieldsMap());
   }

   public static void validateRewrapManyDataKeyOptions(RewrapManyDataKeyOptions options) {
      if (options.getMasterKey() != null && options.getProvider() == null) {
         throw new MongoClientException("Missing the provider but supplied a master key in the RewrapManyDataKeyOptions");
      }
   }

   private static MongoCryptOptions createMongoCryptOptions(Map<String, Map<String, Object>> kmsProviders, boolean bypassQueryAnalysis, List<String> searchPaths, @Nullable Map<String, Object> extraOptions, @Nullable Map<String, BsonDocument> localSchemaMap, @Nullable Map<String, BsonDocument> encryptedFieldsMap) {
      Builder mongoCryptOptionsBuilder = MongoCryptOptions.builder();
      mongoCryptOptionsBuilder.kmsProviderOptions(getKmsProvidersAsBsonDocument(kmsProviders));
      mongoCryptOptionsBuilder.bypassQueryAnalysis(bypassQueryAnalysis);
      mongoCryptOptionsBuilder.searchPaths(searchPaths);
      mongoCryptOptionsBuilder.extraOptions(toBsonDocument(extraOptions));
      mongoCryptOptionsBuilder.localSchemaMap(localSchemaMap);
      mongoCryptOptionsBuilder.encryptedFieldsMap(encryptedFieldsMap);
      mongoCryptOptionsBuilder.needsKmsCredentialsStateEnabled(true);
      return mongoCryptOptionsBuilder.build();
   }

   public static BsonDocument fetchCredentials(Map<String, Map<String, Object>> kmsProviders, Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers) {
      BsonDocument kmsProvidersDocument = getKmsProvidersAsBsonDocument(kmsProviders);
      Iterator var3 = kmsProviderPropertySuppliers.entrySet().iterator();

      while(true) {
         Entry entry;
         String kmsProviderName;
         do {
            if (!var3.hasNext()) {
               if (kmsProvidersDocument.containsKey("aws") && kmsProvidersDocument.get("aws").asDocument().isEmpty()) {
                  AwsCredential awsCredential = AwsCredentialHelper.obtainFromEnvironment();
                  if (awsCredential != null) {
                     BsonDocument awsCredentialDocument = new BsonDocument();
                     awsCredentialDocument.put((String)"accessKeyId", (BsonValue)(new BsonString(awsCredential.getAccessKeyId())));
                     awsCredentialDocument.put((String)"secretAccessKey", (BsonValue)(new BsonString(awsCredential.getSecretAccessKey())));
                     if (awsCredential.getSessionToken() != null) {
                        awsCredentialDocument.put((String)"sessionToken", (BsonValue)(new BsonString(awsCredential.getSessionToken())));
                     }

                     kmsProvidersDocument.put((String)"aws", (BsonValue)awsCredentialDocument);
                  }
               }

               if (kmsProvidersDocument.containsKey("gcp") && kmsProvidersDocument.get("gcp").asDocument().isEmpty()) {
                  kmsProvidersDocument.put((String)"gcp", (BsonValue)GcpCredentialHelper.obtainFromEnvironment());
               }

               if (kmsProvidersDocument.containsKey("azure") && kmsProvidersDocument.get("azure").asDocument().isEmpty()) {
                  kmsProvidersDocument.put((String)"azure", (BsonValue)AzureCredentialHelper.obtainFromEnvironment());
               }

               return kmsProvidersDocument;
            }

            entry = (Entry)var3.next();
            kmsProviderName = (String)entry.getKey();
         } while(!kmsProvidersDocument.get(kmsProviderName).asDocument().isEmpty());

         Map kmsProviderCredential;
         try {
            kmsProviderCredential = (Map)((Supplier)entry.getValue()).get();
         } catch (Exception e) {
            throw new MongoConfigurationException(String.format("Exception getting credential for kms provider %s from configured Supplier.", kmsProviderName), e);
         }

         if (kmsProviderCredential == null || kmsProviderCredential.isEmpty()) {
            throw new MongoConfigurationException(String.format("Exception getting credential for kms provider %s from configured Supplier. The returned value is %s.", kmsProviderName, kmsProviderCredential == null ? "null" : "empty"));
         }

         kmsProvidersDocument.put((String)kmsProviderName, (BsonValue)toBsonDocument(kmsProviderCredential));
      }
   }

   private static BsonDocument getKmsProvidersAsBsonDocument(Map<String, Map<String, Object>> kmsProviders) {
      BsonDocument bsonKmsProviders = new BsonDocument();
      kmsProviders.forEach((k, v) -> {
         bsonKmsProviders.put((String)k, (BsonValue)toBsonDocument(v));
      });
      return bsonKmsProviders;
   }

   private static BsonDocument toBsonDocument(@Nullable Map<String, Object> optionsMap) {
      return (BsonDocument)(optionsMap == null ? new BsonDocument() : new BsonDocumentWrapper(new Document(optionsMap), new DocumentCodec()));
   }

   public static boolean isMongocryptdSpawningDisabled(@Nullable String cryptSharedLibVersion, AutoEncryptionSettings settings) {
      boolean cryptSharedLibIsAvailable = cryptSharedLibVersion != null && !cryptSharedLibVersion.isEmpty();
      boolean cryptSharedLibRequired = (Boolean)settings.getExtraOptions().getOrDefault("cryptSharedLibRequired", false);
      return settings.isBypassAutoEncryption() || settings.isBypassQueryAnalysis() || cryptSharedLibRequired || cryptSharedLibIsAvailable;
   }

   public static List<String> createMongocryptdSpawnArgs(Map<String, Object> options) {
      List<String> spawnArgs = new ArrayList();
      String path = options.containsKey("mongocryptdSpawnPath") ? (String)options.get("mongocryptdSpawnPath") : "mongocryptd";
      spawnArgs.add(path);
      if (options.containsKey("mongocryptdSpawnArgs")) {
         spawnArgs.addAll((List)options.get("mongocryptdSpawnArgs"));
      }

      if (!spawnArgs.contains("--idleShutdownTimeoutSecs")) {
         spawnArgs.add("--idleShutdownTimeoutSecs");
         spawnArgs.add("60");
      }

      return spawnArgs;
   }

   public static MongoClientSettings createMongocryptdClientSettings(@Nullable String connectionString) {
      return MongoClientSettings.builder().applyToClusterSettings((builder) -> {
         builder.serverSelectionTimeout(10L, TimeUnit.SECONDS);
      }).applyToSocketSettings((builder) -> {
         builder.readTimeout(10, TimeUnit.SECONDS);
         builder.connectTimeout(10, TimeUnit.SECONDS);
      }).applyConnectionString(new ConnectionString(connectionString != null ? connectionString : "mongodb://localhost:27020")).build();
   }

   public static ProcessBuilder createProcessBuilder(Map<String, Object> options) {
      return new ProcessBuilder(createMongocryptdSpawnArgs(options));
   }

   public static void startProcess(ProcessBuilder processBuilder) {
      try {
         processBuilder.redirectErrorStream(true);
         processBuilder.redirectOutput(new File(System.getProperty("os.name").startsWith("Windows") ? "NUL" : "/dev/null"));
         processBuilder.start();
      } catch (Throwable e) {
         throw new MongoClientException("Exception starting mongocryptd process. Is `mongocryptd` on the system path?", e);
      }
   }

   private MongoCryptHelper() {
   }
}
