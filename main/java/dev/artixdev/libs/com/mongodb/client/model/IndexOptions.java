package dev.artixdev.libs.com.mongodb.client.model;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class IndexOptions {
   private boolean background;
   private boolean unique;
   private String name;
   private boolean sparse;
   private Long expireAfterSeconds;
   private Integer version;
   private Bson weights;
   private String defaultLanguage;
   private String languageOverride;
   private Integer textVersion;
   private Integer sphereVersion;
   private Integer bits;
   private Double min;
   private Double max;
   private Double bucketSize;
   private Bson storageEngine;
   private Bson partialFilterExpression;
   private Collation collation;
   private Bson wildcardProjection;
   private boolean hidden;

   public boolean isBackground() {
      return this.background;
   }

   public IndexOptions background(boolean background) {
      this.background = background;
      return this;
   }

   public boolean isUnique() {
      return this.unique;
   }

   public IndexOptions unique(boolean unique) {
      this.unique = unique;
      return this;
   }

   @Nullable
   public String getName() {
      return this.name;
   }

   public IndexOptions name(@Nullable String name) {
      this.name = name;
      return this;
   }

   public boolean isSparse() {
      return this.sparse;
   }

   public IndexOptions sparse(boolean sparse) {
      this.sparse = sparse;
      return this;
   }

   @Nullable
   public Long getExpireAfter(TimeUnit timeUnit) {
      return this.expireAfterSeconds == null ? null : timeUnit.convert(this.expireAfterSeconds, TimeUnit.SECONDS);
   }

   public IndexOptions expireAfter(@Nullable Long expireAfter, TimeUnit timeUnit) {
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

   public IndexOptions version(@Nullable Integer version) {
      this.version = version;
      return this;
   }

   @Nullable
   public Bson getWeights() {
      return this.weights;
   }

   public IndexOptions weights(@Nullable Bson weights) {
      this.weights = weights;
      return this;
   }

   @Nullable
   public String getDefaultLanguage() {
      return this.defaultLanguage;
   }

   public IndexOptions defaultLanguage(@Nullable String defaultLanguage) {
      this.defaultLanguage = defaultLanguage;
      return this;
   }

   @Nullable
   public String getLanguageOverride() {
      return this.languageOverride;
   }

   public IndexOptions languageOverride(@Nullable String languageOverride) {
      this.languageOverride = languageOverride;
      return this;
   }

   @Nullable
   public Integer getTextVersion() {
      return this.textVersion;
   }

   public IndexOptions textVersion(@Nullable Integer textVersion) {
      this.textVersion = textVersion;
      return this;
   }

   @Nullable
   public Integer getSphereVersion() {
      return this.sphereVersion;
   }

   public IndexOptions sphereVersion(@Nullable Integer sphereVersion) {
      this.sphereVersion = sphereVersion;
      return this;
   }

   @Nullable
   public Integer getBits() {
      return this.bits;
   }

   public IndexOptions bits(@Nullable Integer bits) {
      this.bits = bits;
      return this;
   }

   @Nullable
   public Double getMin() {
      return this.min;
   }

   public IndexOptions min(@Nullable Double min) {
      this.min = min;
      return this;
   }

   @Nullable
   public Double getMax() {
      return this.max;
   }

   public IndexOptions max(@Nullable Double max) {
      this.max = max;
      return this;
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   public Double getBucketSize() {
      return this.bucketSize;
   }

   /** @deprecated */
   @Deprecated
   public IndexOptions bucketSize(@Nullable Double bucketSize) {
      this.bucketSize = bucketSize;
      return this;
   }

   @Nullable
   public Bson getStorageEngine() {
      return this.storageEngine;
   }

   public IndexOptions storageEngine(@Nullable Bson storageEngine) {
      this.storageEngine = storageEngine;
      return this;
   }

   @Nullable
   public Bson getPartialFilterExpression() {
      return this.partialFilterExpression;
   }

   public IndexOptions partialFilterExpression(@Nullable Bson partialFilterExpression) {
      this.partialFilterExpression = partialFilterExpression;
      return this;
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public IndexOptions collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public Bson getWildcardProjection() {
      return this.wildcardProjection;
   }

   public IndexOptions wildcardProjection(Bson wildcardProjection) {
      this.wildcardProjection = wildcardProjection;
      return this;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public IndexOptions hidden(boolean hidden) {
      this.hidden = hidden;
      return this;
   }

   public String toString() {
      return "IndexOptions{background=" + this.background + ", unique=" + this.unique + ", name='" + this.name + '\'' + ", sparse=" + this.sparse + ", expireAfterSeconds=" + this.expireAfterSeconds + ", version=" + this.version + ", weights=" + this.weights + ", defaultLanguage='" + this.defaultLanguage + '\'' + ", languageOverride='" + this.languageOverride + '\'' + ", textVersion=" + this.textVersion + ", sphereVersion=" + this.sphereVersion + ", bits=" + this.bits + ", min=" + this.min + ", max=" + this.max + ", bucketSize=" + this.bucketSize + ", storageEngine=" + this.storageEngine + ", partialFilterExpression=" + this.partialFilterExpression + ", collation=" + this.collation + ", wildcardProjection=" + this.wildcardProjection + ", hidden=" + this.hidden + '}';
   }
}
