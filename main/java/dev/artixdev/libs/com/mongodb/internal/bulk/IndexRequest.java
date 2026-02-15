package dev.artixdev.libs.com.mongodb.internal.bulk;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

public class IndexRequest {
   private final BsonDocument keys;
   private static final List<Integer> VALID_TEXT_INDEX_VERSIONS = Arrays.asList(1, 2, 3);
   private static final List<Integer> VALID_SPHERE_INDEX_VERSIONS = Arrays.asList(1, 2, 3);
   private boolean background;
   private boolean unique;
   private String name;
   private boolean sparse;
   private Long expireAfterSeconds;
   private Integer version;
   private BsonDocument weights;
   private String defaultLanguage;
   private String languageOverride;
   private Integer textVersion;
   private Integer sphereVersion;
   private Integer bits;
   private Double min;
   private Double max;
   private Double bucketSize;
   private boolean dropDups;
   private BsonDocument storageEngine;
   private BsonDocument partialFilterExpression;
   private Collation collation;
   private BsonDocument wildcardProjection;
   private boolean hidden;

   public IndexRequest(BsonDocument keys) {
      this.keys = (BsonDocument)Assertions.notNull("keys", keys);
   }

   public BsonDocument getKeys() {
      return this.keys;
   }

   public boolean isBackground() {
      return this.background;
   }

   public IndexRequest background(boolean background) {
      this.background = background;
      return this;
   }

   public boolean isUnique() {
      return this.unique;
   }

   public IndexRequest unique(boolean unique) {
      this.unique = unique;
      return this;
   }

   @Nullable
   public String getName() {
      return this.name;
   }

   public IndexRequest name(@Nullable String name) {
      this.name = name;
      return this;
   }

   public boolean isSparse() {
      return this.sparse;
   }

   public IndexRequest sparse(boolean sparse) {
      this.sparse = sparse;
      return this;
   }

   @Nullable
   public Long getExpireAfter(TimeUnit timeUnit) {
      return this.expireAfterSeconds == null ? null : timeUnit.convert(this.expireAfterSeconds, TimeUnit.SECONDS);
   }

   public IndexRequest expireAfter(@Nullable Long expireAfter, TimeUnit timeUnit) {
      if (expireAfter == null) {
         this.expireAfterSeconds = null;
      } else {
         this.expireAfterSeconds = TimeUnit.SECONDS.convert(expireAfter, timeUnit);
      }

      return this;
   }

   @Nullable
   public Integer getVersion() {
      return this.version;
   }

   public IndexRequest version(@Nullable Integer version) {
      this.version = version;
      return this;
   }

   @Nullable
   public BsonDocument getWeights() {
      return this.weights;
   }

   public IndexRequest weights(@Nullable BsonDocument weights) {
      this.weights = weights;
      return this;
   }

   @Nullable
   public String getDefaultLanguage() {
      return this.defaultLanguage;
   }

   public IndexRequest defaultLanguage(@Nullable String defaultLanguage) {
      this.defaultLanguage = defaultLanguage;
      return this;
   }

   @Nullable
   public String getLanguageOverride() {
      return this.languageOverride;
   }

   public IndexRequest languageOverride(@Nullable String languageOverride) {
      this.languageOverride = languageOverride;
      return this;
   }

   @Nullable
   public Integer getTextVersion() {
      return this.textVersion;
   }

   public IndexRequest textVersion(@Nullable Integer textVersion) {
      if (textVersion != null) {
         Assertions.isTrueArgument("textVersion must be 1, 2 or 3", VALID_TEXT_INDEX_VERSIONS.contains(textVersion));
      }

      this.textVersion = textVersion;
      return this;
   }

   @Nullable
   public Integer getSphereVersion() {
      return this.sphereVersion;
   }

   public IndexRequest sphereVersion(@Nullable Integer sphereVersion) {
      if (sphereVersion != null) {
         Assertions.isTrueArgument("sphereIndexVersion must be 1, 2 or 3", VALID_SPHERE_INDEX_VERSIONS.contains(sphereVersion));
      }

      this.sphereVersion = sphereVersion;
      return this;
   }

   @Nullable
   public Integer getBits() {
      return this.bits;
   }

   public IndexRequest bits(@Nullable Integer bits) {
      this.bits = bits;
      return this;
   }

   @Nullable
   public Double getMin() {
      return this.min;
   }

   public IndexRequest min(@Nullable Double min) {
      this.min = min;
      return this;
   }

   @Nullable
   public Double getMax() {
      return this.max;
   }

   public IndexRequest max(@Nullable Double max) {
      this.max = max;
      return this;
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public Double getBucketSize() {
      return this.bucketSize;
   }

   /** @deprecated */
   @Deprecated
   public IndexRequest bucketSize(@Nullable Double bucketSize) {
      this.bucketSize = bucketSize;
      return this;
   }

   public boolean getDropDups() {
      return this.dropDups;
   }

   public IndexRequest dropDups(boolean dropDups) {
      this.dropDups = dropDups;
      return this;
   }

   @Nullable
   public BsonDocument getStorageEngine() {
      return this.storageEngine;
   }

   public IndexRequest storageEngine(@Nullable BsonDocument storageEngineOptions) {
      this.storageEngine = storageEngineOptions;
      return this;
   }

   @Nullable
   public BsonDocument getPartialFilterExpression() {
      return this.partialFilterExpression;
   }

   public IndexRequest partialFilterExpression(@Nullable BsonDocument partialFilterExpression) {
      this.partialFilterExpression = partialFilterExpression;
      return this;
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public IndexRequest collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   @Nullable
   public BsonDocument getWildcardProjection() {
      return this.wildcardProjection;
   }

   public IndexRequest wildcardProjection(@Nullable BsonDocument wildcardProjection) {
      this.wildcardProjection = wildcardProjection;
      return this;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public IndexRequest hidden(boolean hidden) {
      this.hidden = hidden;
      return this;
   }
}
