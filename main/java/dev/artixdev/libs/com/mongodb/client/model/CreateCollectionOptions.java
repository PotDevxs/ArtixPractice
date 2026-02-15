package dev.artixdev.libs.com.mongodb.client.model;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class CreateCollectionOptions {
   private long maxDocuments;
   private boolean capped;
   private long sizeInBytes;
   private Bson storageEngineOptions;
   private IndexOptionDefaults indexOptionDefaults = new IndexOptionDefaults();
   private ValidationOptions validationOptions = new ValidationOptions();
   private Collation collation;
   private long expireAfterSeconds;
   private TimeSeriesOptions timeSeriesOptions;
   private ChangeStreamPreAndPostImagesOptions changeStreamPreAndPostImagesOptions;
   private ClusteredIndexOptions clusteredIndexOptions;
   private Bson encryptedFields;

   public CreateCollectionOptions() {
   }

   public CreateCollectionOptions(CreateCollectionOptions options) {
      Assertions.notNull("options", options);
      this.maxDocuments = options.maxDocuments;
      this.capped = options.capped;
      this.sizeInBytes = options.sizeInBytes;
      this.storageEngineOptions = options.storageEngineOptions;
      this.indexOptionDefaults = options.indexOptionDefaults;
      this.validationOptions = options.validationOptions;
      this.collation = options.collation;
      this.expireAfterSeconds = options.expireAfterSeconds;
      this.timeSeriesOptions = options.timeSeriesOptions;
      this.changeStreamPreAndPostImagesOptions = options.changeStreamPreAndPostImagesOptions;
      this.clusteredIndexOptions = options.clusteredIndexOptions;
      this.encryptedFields = options.encryptedFields;
   }

   public long getMaxDocuments() {
      return this.maxDocuments;
   }

   public CreateCollectionOptions maxDocuments(long maxDocuments) {
      this.maxDocuments = maxDocuments;
      return this;
   }

   public boolean isCapped() {
      return this.capped;
   }

   public CreateCollectionOptions capped(boolean capped) {
      this.capped = capped;
      return this;
   }

   public long getSizeInBytes() {
      return this.sizeInBytes;
   }

   public CreateCollectionOptions sizeInBytes(long sizeInBytes) {
      this.sizeInBytes = sizeInBytes;
      return this;
   }

   @Nullable
   public Bson getStorageEngineOptions() {
      return this.storageEngineOptions;
   }

   public CreateCollectionOptions storageEngineOptions(@Nullable Bson storageEngineOptions) {
      this.storageEngineOptions = storageEngineOptions;
      return this;
   }

   public IndexOptionDefaults getIndexOptionDefaults() {
      return this.indexOptionDefaults;
   }

   public CreateCollectionOptions indexOptionDefaults(IndexOptionDefaults indexOptionDefaults) {
      this.indexOptionDefaults = (IndexOptionDefaults)Assertions.notNull("indexOptionDefaults", indexOptionDefaults);
      return this;
   }

   public ValidationOptions getValidationOptions() {
      return this.validationOptions;
   }

   public CreateCollectionOptions validationOptions(ValidationOptions validationOptions) {
      this.validationOptions = (ValidationOptions)Assertions.notNull("validationOptions", validationOptions);
      return this;
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   public CreateCollectionOptions collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public long getExpireAfter(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.expireAfterSeconds, TimeUnit.SECONDS);
   }

   public CreateCollectionOptions expireAfter(long expireAfter, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      long asSeconds = TimeUnit.SECONDS.convert(expireAfter, timeUnit);
      if (asSeconds < 0L) {
         throw new IllegalArgumentException("expireAfter, after conversion to seconds, must be >= 0");
      } else {
         this.expireAfterSeconds = asSeconds;
         return this;
      }
   }

   @Nullable
   public TimeSeriesOptions getTimeSeriesOptions() {
      return this.timeSeriesOptions;
   }

   public CreateCollectionOptions timeSeriesOptions(TimeSeriesOptions timeSeriesOptions) {
      this.timeSeriesOptions = timeSeriesOptions;
      return this;
   }

   @Nullable
   public ClusteredIndexOptions getClusteredIndexOptions() {
      return this.clusteredIndexOptions;
   }

   public CreateCollectionOptions clusteredIndexOptions(ClusteredIndexOptions clusteredIndexOptions) {
      this.clusteredIndexOptions = clusteredIndexOptions;
      return this;
   }

   @Nullable
   public ChangeStreamPreAndPostImagesOptions getChangeStreamPreAndPostImagesOptions() {
      return this.changeStreamPreAndPostImagesOptions;
   }

   public CreateCollectionOptions changeStreamPreAndPostImagesOptions(ChangeStreamPreAndPostImagesOptions changeStreamPreAndPostImagesOptions) {
      this.changeStreamPreAndPostImagesOptions = changeStreamPreAndPostImagesOptions;
      return this;
   }

   @Nullable
   @Beta({Beta.Reason.SERVER})
   public Bson getEncryptedFields() {
      return this.encryptedFields;
   }

   @Beta({Beta.Reason.SERVER})
   public CreateCollectionOptions encryptedFields(@Nullable Bson encryptedFields) {
      this.encryptedFields = encryptedFields;
      return this;
   }

   public String toString() {
      return "CreateCollectionOptions{, maxDocuments=" + this.maxDocuments + ", capped=" + this.capped + ", sizeInBytes=" + this.sizeInBytes + ", storageEngineOptions=" + this.storageEngineOptions + ", indexOptionDefaults=" + this.indexOptionDefaults + ", validationOptions=" + this.validationOptions + ", collation=" + this.collation + ", expireAfterSeconds=" + this.expireAfterSeconds + ", timeSeriesOptions=" + this.timeSeriesOptions + ", changeStreamPreAndPostImagesOptions=" + this.changeStreamPreAndPostImagesOptions + ", encryptedFields=" + this.encryptedFields + '}';
   }
}
