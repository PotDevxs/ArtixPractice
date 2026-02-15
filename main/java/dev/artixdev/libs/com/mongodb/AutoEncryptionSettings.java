package dev.artixdev.libs.com.mongodb;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.net.ssl.SSLContext;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

public final class AutoEncryptionSettings {
   private final MongoClientSettings keyVaultMongoClientSettings;
   private final String keyVaultNamespace;
   private final Map<String, Map<String, Object>> kmsProviders;
   private final Map<String, SSLContext> kmsProviderSslContextMap;
   private final Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers;
   private final Map<String, BsonDocument> schemaMap;
   private final Map<String, Object> extraOptions;
   private final boolean bypassAutoEncryption;
   private final Map<String, BsonDocument> encryptedFieldsMap;
   private final boolean bypassQueryAnalysis;

   public static AutoEncryptionSettings.Builder builder() {
      return new AutoEncryptionSettings.Builder();
   }

   @Nullable
   public MongoClientSettings getKeyVaultMongoClientSettings() {
      return this.keyVaultMongoClientSettings;
   }

   public String getKeyVaultNamespace() {
      return this.keyVaultNamespace;
   }

   public Map<String, Map<String, Object>> getKmsProviders() {
      return Collections.unmodifiableMap(this.kmsProviders);
   }

   public Map<String, Supplier<Map<String, Object>>> getKmsProviderPropertySuppliers() {
      return Collections.unmodifiableMap(this.kmsProviderPropertySuppliers);
   }

   public Map<String, SSLContext> getKmsProviderSslContextMap() {
      return Collections.unmodifiableMap(this.kmsProviderSslContextMap);
   }

   public Map<String, BsonDocument> getSchemaMap() {
      return this.schemaMap;
   }

   public Map<String, Object> getExtraOptions() {
      return this.extraOptions;
   }

   public boolean isBypassAutoEncryption() {
      return this.bypassAutoEncryption;
   }

   @Nullable
   public Map<String, BsonDocument> getEncryptedFieldsMap() {
      return this.encryptedFieldsMap;
   }

   public boolean isBypassQueryAnalysis() {
      return this.bypassQueryAnalysis;
   }

   private AutoEncryptionSettings(AutoEncryptionSettings.Builder builder) {
      this.keyVaultMongoClientSettings = builder.keyVaultMongoClientSettings;
      this.keyVaultNamespace = (String)Assertions.notNull("keyVaultNamespace", builder.keyVaultNamespace);
      this.kmsProviders = (Map)Assertions.notNull("kmsProviders", builder.kmsProviders);
      this.kmsProviderSslContextMap = (Map)Assertions.notNull("kmsProviderSslContextMap", builder.kmsProviderSslContextMap);
      this.kmsProviderPropertySuppliers = (Map)Assertions.notNull("kmsProviderPropertySuppliers", builder.kmsProviderPropertySuppliers);
      this.schemaMap = (Map)Assertions.notNull("schemaMap", builder.schemaMap);
      this.extraOptions = (Map)Assertions.notNull("extraOptions", builder.extraOptions);
      this.bypassAutoEncryption = builder.bypassAutoEncryption;
      this.encryptedFieldsMap = builder.encryptedFieldsMap;
      this.bypassQueryAnalysis = builder.bypassQueryAnalysis;
   }

   public String toString() {
      return "AutoEncryptionSettings{<hidden>}";
   }

   // $FF: synthetic method
   AutoEncryptionSettings(AutoEncryptionSettings.Builder x0, Object x1) {
      this(x0);
   }

   @NotThreadSafe
   public static final class Builder {
      private MongoClientSettings keyVaultMongoClientSettings;
      private String keyVaultNamespace;
      private Map<String, Map<String, Object>> kmsProviders;
      private Map<String, SSLContext> kmsProviderSslContextMap;
      private Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers;
      private Map<String, BsonDocument> schemaMap;
      private Map<String, Object> extraOptions;
      private boolean bypassAutoEncryption;
      private Map<String, BsonDocument> encryptedFieldsMap;
      private boolean bypassQueryAnalysis;

      public AutoEncryptionSettings.Builder keyVaultMongoClientSettings(MongoClientSettings keyVaultMongoClientSettings) {
         this.keyVaultMongoClientSettings = keyVaultMongoClientSettings;
         return this;
      }

      public AutoEncryptionSettings.Builder keyVaultNamespace(String keyVaultNamespace) {
         this.keyVaultNamespace = (String)Assertions.notNull("keyVaultNamespace", keyVaultNamespace);
         return this;
      }

      public AutoEncryptionSettings.Builder kmsProviders(Map<String, Map<String, Object>> kmsProviders) {
         this.kmsProviders = (Map)Assertions.notNull("kmsProviders", kmsProviders);
         return this;
      }

      public AutoEncryptionSettings.Builder kmsProviderPropertySuppliers(Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers) {
         this.kmsProviderPropertySuppliers = (Map)Assertions.notNull("kmsProviderPropertySuppliers", kmsProviderPropertySuppliers);
         return this;
      }

      public AutoEncryptionSettings.Builder kmsProviderSslContextMap(Map<String, SSLContext> kmsProviderSslContextMap) {
         this.kmsProviderSslContextMap = (Map)Assertions.notNull("kmsProviderSslContextMap", kmsProviderSslContextMap);
         return this;
      }

      public AutoEncryptionSettings.Builder schemaMap(Map<String, BsonDocument> schemaMap) {
         this.schemaMap = (Map)Assertions.notNull("schemaMap", schemaMap);
         return this;
      }

      public AutoEncryptionSettings.Builder extraOptions(Map<String, Object> extraOptions) {
         this.extraOptions = (Map)Assertions.notNull("extraOptions", extraOptions);
         return this;
      }

      public AutoEncryptionSettings.Builder bypassAutoEncryption(boolean bypassAutoEncryption) {
         this.bypassAutoEncryption = bypassAutoEncryption;
         return this;
      }

      public AutoEncryptionSettings.Builder encryptedFieldsMap(Map<String, BsonDocument> encryptedFieldsMap) {
         this.encryptedFieldsMap = (Map)Assertions.notNull("encryptedFieldsMap", encryptedFieldsMap);
         return this;
      }

      public AutoEncryptionSettings.Builder bypassQueryAnalysis(boolean bypassQueryAnalysis) {
         this.bypassQueryAnalysis = bypassQueryAnalysis;
         return this;
      }

      public AutoEncryptionSettings build() {
         return new AutoEncryptionSettings(this);
      }

      private Builder() {
         this.kmsProviderSslContextMap = new HashMap();
         this.kmsProviderPropertySuppliers = new HashMap();
         this.schemaMap = Collections.emptyMap();
         this.extraOptions = Collections.emptyMap();
         this.encryptedFieldsMap = Collections.emptyMap();
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
