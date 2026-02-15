package dev.artixdev.libs.com.mongodb.crypt.capi;

import java.util.List;
import java.util.Map;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

/**
 * Stub for MongoDB crypt options (configuration for MongoCrypt).
 * Full implementation is in mongodb-crypt dependency.
 */
public final class MongoCryptOptions {
   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private BsonDocument kmsProviderOptions;
      private boolean bypassQueryAnalysis;
      private List<String> searchPaths;
      private BsonDocument extraOptions;
      private Map<String, BsonDocument> localSchemaMap;
      private Map<String, BsonDocument> encryptedFieldsMap;
      private boolean needsKmsCredentialsStateEnabled;

      public Builder kmsProviderOptions(BsonDocument kmsProviderOptions) {
         this.kmsProviderOptions = kmsProviderOptions;
         return this;
      }

      public Builder bypassQueryAnalysis(boolean bypassQueryAnalysis) {
         this.bypassQueryAnalysis = bypassQueryAnalysis;
         return this;
      }

      public Builder searchPaths(List<String> searchPaths) {
         this.searchPaths = searchPaths;
         return this;
      }

      public Builder extraOptions(@Nullable BsonDocument extraOptions) {
         this.extraOptions = extraOptions;
         return this;
      }

      public Builder localSchemaMap(@Nullable Map<String, BsonDocument> localSchemaMap) {
         this.localSchemaMap = localSchemaMap;
         return this;
      }

      public Builder encryptedFieldsMap(@Nullable Map<String, BsonDocument> encryptedFieldsMap) {
         this.encryptedFieldsMap = encryptedFieldsMap;
         return this;
      }

      public Builder needsKmsCredentialsStateEnabled(boolean needsKmsCredentialsStateEnabled) {
         this.needsKmsCredentialsStateEnabled = needsKmsCredentialsStateEnabled;
         return this;
      }

      public MongoCryptOptions build() {
         return new MongoCryptOptions(kmsProviderOptions, bypassQueryAnalysis, searchPaths, extraOptions, localSchemaMap, encryptedFieldsMap, needsKmsCredentialsStateEnabled);
      }
   }

   private final BsonDocument kmsProviderOptions;
   private final boolean bypassQueryAnalysis;
   private final List<String> searchPaths;
   private final BsonDocument extraOptions;
   private final Map<String, BsonDocument> localSchemaMap;
   private final Map<String, BsonDocument> encryptedFieldsMap;
   private final boolean needsKmsCredentialsStateEnabled;

   private MongoCryptOptions(BsonDocument kmsProviderOptions, boolean bypassQueryAnalysis, List<String> searchPaths, BsonDocument extraOptions, Map<String, BsonDocument> localSchemaMap, Map<String, BsonDocument> encryptedFieldsMap, boolean needsKmsCredentialsStateEnabled) {
      this.kmsProviderOptions = kmsProviderOptions;
      this.bypassQueryAnalysis = bypassQueryAnalysis;
      this.searchPaths = searchPaths;
      this.extraOptions = extraOptions;
      this.localSchemaMap = localSchemaMap;
      this.encryptedFieldsMap = encryptedFieldsMap;
      this.needsKmsCredentialsStateEnabled = needsKmsCredentialsStateEnabled;
   }
}
