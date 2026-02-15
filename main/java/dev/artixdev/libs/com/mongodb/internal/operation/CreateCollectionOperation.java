package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.ChangeStreamPreAndPostImagesOptions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.client.model.TimeSeriesGranularity;
import dev.artixdev.libs.com.mongodb.client.model.TimeSeriesOptions;
import dev.artixdev.libs.com.mongodb.client.model.ValidationAction;
import dev.artixdev.libs.com.mongodb.client.model.ValidationLevel;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.internal.connection.AsyncConnection;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public class CreateCollectionOperation implements AsyncWriteOperation<Void>, WriteOperation<Void> {
   private static final String ENCRYPT_PREFIX = "enxcol_.";
   private static final BsonDocument ENCRYPT_CLUSTERED_INDEX = BsonDocument.parse("{key: {_id: 1}, unique: true}");
   private static final BsonArray SAFE_CONTENT_ARRAY = new BsonArray(Collections.singletonList(BsonDocument.parse("{key: {__safeContent__: 1}, name: '__safeContent___1'}")));
   private final String databaseName;
   private final String collectionName;
   private final WriteConcern writeConcern;
   private boolean capped;
   private long sizeInBytes;
   private boolean autoIndex;
   private long maxDocuments;
   private BsonDocument storageEngineOptions;
   private BsonDocument indexOptionDefaults;
   private BsonDocument validator;
   private ValidationLevel validationLevel;
   private ValidationAction validationAction;
   private Collation collation;
   private long expireAfterSeconds;
   private TimeSeriesOptions timeSeriesOptions;
   private ChangeStreamPreAndPostImagesOptions changeStreamPreAndPostImagesOptions;
   private BsonDocument clusteredIndexKey;
   private boolean clusteredIndexUnique;
   private String clusteredIndexName;
   private BsonDocument encryptedFields;

   public CreateCollectionOperation(String databaseName, String collectionName) {
      this(databaseName, collectionName, (WriteConcern)null);
   }

   public CreateCollectionOperation(String databaseName, String collectionName, @Nullable WriteConcern writeConcern) {
      this.capped = false;
      this.sizeInBytes = 0L;
      this.autoIndex = true;
      this.maxDocuments = 0L;
      this.validationLevel = null;
      this.validationAction = null;
      this.collation = null;
      this.databaseName = (String)Assertions.notNull("databaseName", databaseName);
      this.collectionName = (String)Assertions.notNull("collectionName", collectionName);
      this.writeConcern = writeConcern;
   }

   public String getCollectionName() {
      return this.collectionName;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public boolean isAutoIndex() {
      return this.autoIndex;
   }

   public CreateCollectionOperation autoIndex(boolean autoIndex) {
      this.autoIndex = autoIndex;
      return this;
   }

   public long getMaxDocuments() {
      return this.maxDocuments;
   }

   public CreateCollectionOperation maxDocuments(long maxDocuments) {
      this.maxDocuments = maxDocuments;
      return this;
   }

   public boolean isCapped() {
      return this.capped;
   }

   public CreateCollectionOperation capped(boolean capped) {
      this.capped = capped;
      return this;
   }

   public long getSizeInBytes() {
      return this.sizeInBytes;
   }

   public CreateCollectionOperation sizeInBytes(long sizeInBytes) {
      this.sizeInBytes = sizeInBytes;
      return this;
   }

   public BsonDocument getStorageEngineOptions() {
      return this.storageEngineOptions;
   }

   public CreateCollectionOperation storageEngineOptions(@Nullable BsonDocument storageEngineOptions) {
      this.storageEngineOptions = storageEngineOptions;
      return this;
   }

   public BsonDocument getIndexOptionDefaults() {
      return this.indexOptionDefaults;
   }

   public CreateCollectionOperation indexOptionDefaults(@Nullable BsonDocument indexOptionDefaults) {
      this.indexOptionDefaults = indexOptionDefaults;
      return this;
   }

   public BsonDocument getValidator() {
      return this.validator;
   }

   public CreateCollectionOperation validator(@Nullable BsonDocument validator) {
      this.validator = validator;
      return this;
   }

   public ValidationLevel getValidationLevel() {
      return this.validationLevel;
   }

   public CreateCollectionOperation validationLevel(@Nullable ValidationLevel validationLevel) {
      this.validationLevel = validationLevel;
      return this;
   }

   public ValidationAction getValidationAction() {
      return this.validationAction;
   }

   public CreateCollectionOperation validationAction(@Nullable ValidationAction validationAction) {
      this.validationAction = validationAction;
      return this;
   }

   public Collation getCollation() {
      return this.collation;
   }

   public CreateCollectionOperation collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public CreateCollectionOperation expireAfter(long expireAfterSeconds) {
      this.expireAfterSeconds = expireAfterSeconds;
      return this;
   }

   public CreateCollectionOperation timeSeriesOptions(@Nullable TimeSeriesOptions timeSeriesOptions) {
      this.timeSeriesOptions = timeSeriesOptions;
      return this;
   }

   public CreateCollectionOperation changeStreamPreAndPostImagesOptions(@Nullable ChangeStreamPreAndPostImagesOptions changeStreamPreAndPostImagesOptions) {
      this.changeStreamPreAndPostImagesOptions = changeStreamPreAndPostImagesOptions;
      return this;
   }

   public CreateCollectionOperation clusteredIndexKey(@Nullable BsonDocument clusteredIndexKey) {
      this.clusteredIndexKey = clusteredIndexKey;
      return this;
   }

   public CreateCollectionOperation clusteredIndexUnique(boolean clusteredIndexUnique) {
      this.clusteredIndexUnique = clusteredIndexUnique;
      return this;
   }

   public CreateCollectionOperation clusteredIndexName(@Nullable String clusteredIndexName) {
      this.clusteredIndexName = clusteredIndexName;
      return this;
   }

   public CreateCollectionOperation encryptedFields(@Nullable BsonDocument encryptedFields) {
      this.encryptedFields = encryptedFields;
      return this;
   }

   public Void execute(WriteBinding binding) {
      return (Void)SyncOperationHelper.withConnection(binding, (connection) -> {
         this.checkEncryptedFieldsSupported(connection.getDescription());
         this.getCommandFunctions().forEach((commandCreator) -> {
            SyncOperationHelper.executeCommand(binding, this.databaseName, (BsonDocument)commandCreator.get(), connection, SyncOperationHelper.writeConcernErrorTransformer());
         });
         return null;
      });
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
      AsyncOperationHelper.withAsyncConnection(binding, (connection, t) -> {
         SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
         if (t != null) {
            errHandlingCallback.onResult(null, t);
         } else {
            SingleResultCallback<Void> releasingCallback = AsyncOperationHelper.releasingCallback(errHandlingCallback, connection);
            if (!this.checkEncryptedFieldsSupported(connection.getDescription(), releasingCallback)) {
               return;
            }

            (new CreateCollectionOperation.ProcessCommandsCallback(binding, connection, releasingCallback)).onResult((Void)null, (Throwable)null);
         }

      });
   }

   private String getGranularityAsString(TimeSeriesGranularity granularity) {
      switch(granularity) {
      case SECONDS:
         return "seconds";
      case MINUTES:
         return "minutes";
      case HOURS:
         return "hours";
      default:
         throw new AssertionError("Unexpected granularity " + granularity);
      }
   }

   private List<Supplier<BsonDocument>> getCommandFunctions() {
      return this.encryptedFields == null ? Collections.singletonList(this::getCreateCollectionCommand) : Arrays.asList(() -> {
         return this.getCreateEncryptedFieldsCollectionCommand("esc");
      }, () -> {
         return this.getCreateEncryptedFieldsCollectionCommand("ecoc");
      }, this::getCreateCollectionCommand, () -> {
         return (new BsonDocument("createIndexes", new BsonString(this.collectionName))).append("indexes", SAFE_CONTENT_ARRAY);
      });
   }

   private BsonDocument getCreateEncryptedFieldsCollectionCommand(String collectionSuffix) {
      return (new BsonDocument()).append("create", (BsonValue)this.encryptedFields.getOrDefault(collectionSuffix + "Collection", new BsonString("enxcol_." + this.collectionName + "." + collectionSuffix))).append("clusteredIndex", ENCRYPT_CLUSTERED_INDEX);
   }

   private BsonDocument getCreateCollectionCommand() {
      BsonDocument document = new BsonDocument("create", new BsonString(this.collectionName));
      DocumentHelper.putIfFalse(document, "autoIndexId", this.autoIndex);
      document.put((String)"capped", (BsonValue)BsonBoolean.valueOf(this.capped));
      if (this.capped) {
         DocumentHelper.putIfNotZero(document, "size", this.sizeInBytes);
         DocumentHelper.putIfNotZero(document, "max", this.maxDocuments);
      }

      DocumentHelper.putIfNotNull(document, "storageEngine", (BsonValue)this.storageEngineOptions);
      DocumentHelper.putIfNotNull(document, "indexOptionDefaults", (BsonValue)this.indexOptionDefaults);
      DocumentHelper.putIfNotNull(document, "validator", (BsonValue)this.validator);
      if (this.validationLevel != null) {
         document.put((String)"validationLevel", (BsonValue)(new BsonString(this.validationLevel.getValue())));
      }

      if (this.validationAction != null) {
         document.put((String)"validationAction", (BsonValue)(new BsonString(this.validationAction.getValue())));
      }

      WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, document);
      if (this.collation != null) {
         document.put((String)"collation", (BsonValue)this.collation.asDocument());
      }

      DocumentHelper.putIfNotZero(document, "expireAfterSeconds", this.expireAfterSeconds);
      BsonDocument timeSeriesDocument;
      if (this.timeSeriesOptions != null) {
         timeSeriesDocument = new BsonDocument("timeField", new BsonString(this.timeSeriesOptions.getTimeField()));
         String metaField = this.timeSeriesOptions.getMetaField();
         if (metaField != null) {
            timeSeriesDocument.put((String)"metaField", (BsonValue)(new BsonString(metaField)));
         }

         TimeSeriesGranularity granularity = this.timeSeriesOptions.getGranularity();
         if (granularity != null) {
            timeSeriesDocument.put((String)"granularity", (BsonValue)(new BsonString(this.getGranularityAsString(granularity))));
         }

         Long bucketMaxSpan = this.timeSeriesOptions.getBucketMaxSpan(TimeUnit.SECONDS);
         if (bucketMaxSpan != null) {
            timeSeriesDocument.put((String)"bucketMaxSpanSeconds", (BsonValue)(new BsonInt64(bucketMaxSpan)));
         }

         Long bucketRounding = this.timeSeriesOptions.getBucketRounding(TimeUnit.SECONDS);
         if (bucketRounding != null) {
            timeSeriesDocument.put((String)"bucketRoundingSeconds", (BsonValue)(new BsonInt64(bucketRounding)));
         }

         document.put((String)"timeseries", (BsonValue)timeSeriesDocument);
      }

      if (this.changeStreamPreAndPostImagesOptions != null) {
         document.put((String)"changeStreamPreAndPostImages", (BsonValue)(new BsonDocument("enabled", BsonBoolean.valueOf(this.changeStreamPreAndPostImagesOptions.isEnabled()))));
      }

      if (this.clusteredIndexKey != null) {
         timeSeriesDocument = (new BsonDocument()).append("key", this.clusteredIndexKey).append("unique", BsonBoolean.valueOf(this.clusteredIndexUnique));
         if (this.clusteredIndexName != null) {
            timeSeriesDocument.put((String)"name", (BsonValue)(new BsonString(this.clusteredIndexName)));
         }

         document.put((String)"clusteredIndex", (BsonValue)timeSeriesDocument);
      }

      DocumentHelper.putIfNotNull(document, "encryptedFields", (BsonValue)this.encryptedFields);
      return document;
   }

   private void checkEncryptedFieldsSupported(ConnectionDescription connectionDescription) throws MongoException {
      if (this.encryptedFields != null && ServerVersionHelper.serverIsLessThanVersionSevenDotZero(connectionDescription)) {
         throw new MongoClientException("Driver support of Queryable Encryption is incompatible with server. Upgrade server to use Queryable Encryption.");
      }
   }

   private boolean checkEncryptedFieldsSupported(ConnectionDescription connectionDescription, SingleResultCallback<Void> callback) {
      try {
         this.checkEncryptedFieldsSupported(connectionDescription);
         return true;
      } catch (Exception exception) {
         callback.onResult(null, exception);
         return false;
      }
   }

   class ProcessCommandsCallback implements SingleResultCallback<Void> {
      private final AsyncWriteBinding binding;
      private final AsyncConnection connection;
      private final SingleResultCallback<Void> finalCallback;
      private final Deque<Supplier<BsonDocument>> commands;

      ProcessCommandsCallback(AsyncWriteBinding binding, AsyncConnection connection, SingleResultCallback<Void> finalCallback) {
         this.binding = binding;
         this.connection = connection;
         this.finalCallback = finalCallback;
         this.commands = new ArrayDeque(CreateCollectionOperation.this.getCommandFunctions());
      }

      public void onResult(@Nullable Void result, @Nullable Throwable t) {
         if (t != null) {
            this.finalCallback.onResult(null, t);
         } else {
            Supplier<BsonDocument> nextCommandFunction = (Supplier)this.commands.poll();
            if (nextCommandFunction == null) {
               this.finalCallback.onResult(null, (Throwable)null);
            } else {
               AsyncOperationHelper.executeCommandAsync(this.binding, CreateCollectionOperation.this.databaseName, (BsonDocument)nextCommandFunction.get(), this.connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), this);
            }

         }
      }
   }
}
