package dev.artixdev.libs.com.mongodb;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.net.ssl.SSLContext;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public final class ClientEncryptionSettings {
   private final MongoClientSettings keyVaultMongoClientSettings;
   private final String keyVaultNamespace;
   private final Map<String, Map<String, Object>> kmsProviders;
   private final Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers;
   private final Map<String, SSLContext> kmsProviderSslContextMap;

   public static ClientEncryptionSettings.Builder builder() {
      return new ClientEncryptionSettings.Builder();
   }

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

   private ClientEncryptionSettings(ClientEncryptionSettings.Builder builder) {
      this.keyVaultMongoClientSettings = (MongoClientSettings)Assertions.notNull("keyVaultMongoClientSettings", builder.keyVaultMongoClientSettings);
      this.keyVaultNamespace = (String)Assertions.notNull("keyVaultNamespace", builder.keyVaultNamespace);
      this.kmsProviders = (Map)Assertions.notNull("kmsProviders", builder.kmsProviders);
      this.kmsProviderPropertySuppliers = (Map)Assertions.notNull("kmsProviderPropertySuppliers", builder.kmsProviderPropertySuppliers);
      this.kmsProviderSslContextMap = (Map)Assertions.notNull("kmsProviderSslContextMap", builder.kmsProviderSslContextMap);
   }

   // $FF: synthetic method
   ClientEncryptionSettings(ClientEncryptionSettings.Builder x0, Object x1) {
      this(x0);
   }

   @NotThreadSafe
   public static final class Builder {
      private MongoClientSettings keyVaultMongoClientSettings;
      private String keyVaultNamespace;
      private Map<String, Map<String, Object>> kmsProviders;
      private Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers;
      private Map<String, SSLContext> kmsProviderSslContextMap;

      public ClientEncryptionSettings.Builder keyVaultMongoClientSettings(MongoClientSettings keyVaultMongoClientSettings) {
         this.keyVaultMongoClientSettings = (MongoClientSettings)Assertions.notNull("keyVaultMongoClientSettings", keyVaultMongoClientSettings);
         return this;
      }

      public ClientEncryptionSettings.Builder keyVaultNamespace(String keyVaultNamespace) {
         this.keyVaultNamespace = (String)Assertions.notNull("keyVaultNamespace", keyVaultNamespace);
         return this;
      }

      public ClientEncryptionSettings.Builder kmsProviders(Map<String, Map<String, Object>> kmsProviders) {
         this.kmsProviders = (Map)Assertions.notNull("kmsProviders", kmsProviders);
         return this;
      }

      public ClientEncryptionSettings.Builder kmsProviderPropertySuppliers(Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers) {
         this.kmsProviderPropertySuppliers = (Map)Assertions.notNull("kmsProviderPropertySuppliers", kmsProviderPropertySuppliers);
         return this;
      }

      public ClientEncryptionSettings.Builder kmsProviderSslContextMap(Map<String, SSLContext> kmsProviderSslContextMap) {
         this.kmsProviderSslContextMap = (Map)Assertions.notNull("kmsProviderSslContextMap", kmsProviderSslContextMap);
         return this;
      }

      public ClientEncryptionSettings build() {
         return new ClientEncryptionSettings(this);
      }

      private Builder() {
         this.kmsProviderPropertySuppliers = new HashMap();
         this.kmsProviderSslContextMap = new HashMap();
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
