package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.client.model.AggregationLevel;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;

public class AggregateToCollectionOperation implements AsyncReadOperation<Void>, ReadOperation<Void> {
   private final MongoNamespace namespace;
   private final List<BsonDocument> pipeline;
   private final WriteConcern writeConcern;
   private final ReadConcern readConcern;
   private final AggregationLevel aggregationLevel;
   private Boolean allowDiskUse;
   private long maxTimeMS;
   private Boolean bypassDocumentValidation;
   private Collation collation;
   private BsonValue comment;
   private BsonValue hint;
   private BsonDocument variables;

   public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline) {
      this(namespace, pipeline, (ReadConcern)null, (WriteConcern)null, AggregationLevel.COLLECTION);
   }

   public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline, WriteConcern writeConcern) {
      this(namespace, pipeline, (ReadConcern)null, writeConcern, AggregationLevel.COLLECTION);
   }

   public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline, ReadConcern readConcern) {
      this(namespace, pipeline, readConcern, (WriteConcern)null, AggregationLevel.COLLECTION);
   }

   public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline, ReadConcern readConcern, WriteConcern writeConcern) {
      this(namespace, pipeline, readConcern, writeConcern, AggregationLevel.COLLECTION);
   }

   public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline, @Nullable ReadConcern readConcern, @Nullable WriteConcern writeConcern, AggregationLevel aggregationLevel) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.pipeline = (List)Assertions.notNull("pipeline", pipeline);
      this.writeConcern = writeConcern;
      this.readConcern = readConcern;
      this.aggregationLevel = (AggregationLevel)Assertions.notNull("aggregationLevel", aggregationLevel);
      Assertions.isTrueArgument("pipeline is not empty", !pipeline.isEmpty());
   }

   public List<BsonDocument> getPipeline() {
      return this.pipeline;
   }

   public ReadConcern getReadConcern() {
      return this.readConcern;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public Boolean getAllowDiskUse() {
      return this.allowDiskUse;
   }

   public AggregateToCollectionOperation allowDiskUse(@Nullable Boolean allowDiskUse) {
      this.allowDiskUse = allowDiskUse;
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public AggregateToCollectionOperation maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public AggregateToCollectionOperation bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   public Collation getCollation() {
      return this.collation;
   }

   public AggregateToCollectionOperation collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public BsonValue getComment() {
      return this.comment;
   }

   public AggregateToCollectionOperation let(@Nullable BsonDocument variables) {
      this.variables = variables;
      return this;
   }

   public AggregateToCollectionOperation comment(BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public BsonValue getHint() {
      return this.hint;
   }

   public AggregateToCollectionOperation hint(@Nullable BsonValue hint) {
      this.hint = hint;
      return this;
   }

   public Void execute(ReadBinding binding) {
      return (Void)SyncOperationHelper.executeRetryableRead(binding, () -> {
         return binding.getReadConnectionSource(12, ReadPreference.primary());
      }, this.namespace.getDatabaseName(), (serverDescription, connectionDescription) -> {
         return this.getCommand();
      }, new BsonDocumentCodec(), (result, source, connection) -> {
         WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
         return null;
      }, false);
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<Void> callback) {
      AsyncOperationHelper.executeRetryableReadAsync(binding, (connectionSourceCallback) -> {
         binding.getReadConnectionSource(12, ReadPreference.primary(), connectionSourceCallback);
      }, this.namespace.getDatabaseName(), (serverDescription, connectionDescription) -> {
         return this.getCommand();
      }, new BsonDocumentCodec(), (result, source, connection) -> {
         WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
         return null;
      }, false, callback);
   }

   private BsonDocument getCommand() {
      BsonValue aggregationTarget = this.aggregationLevel == AggregationLevel.DATABASE ? new BsonInt32(1) : new BsonString(this.namespace.getCollectionName());
      BsonDocument commandDocument = new BsonDocument("aggregate", (BsonValue)aggregationTarget);
      commandDocument.put((String)"pipeline", (BsonValue)(new BsonArray(this.pipeline)));
      if (this.maxTimeMS > 0L) {
         commandDocument.put((String)"maxTimeMS", (BsonValue)(new BsonInt64(this.maxTimeMS)));
      }

      if (this.allowDiskUse != null) {
         commandDocument.put((String)"allowDiskUse", (BsonValue)BsonBoolean.valueOf(this.allowDiskUse));
      }

      if (this.bypassDocumentValidation != null) {
         commandDocument.put((String)"bypassDocumentValidation", (BsonValue)BsonBoolean.valueOf(this.bypassDocumentValidation));
      }

      commandDocument.put((String)"cursor", (BsonValue)(new BsonDocument()));
      WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
      if (this.readConcern != null && !this.readConcern.isServerDefault()) {
         commandDocument.put((String)"readConcern", (BsonValue)this.readConcern.asDocument());
      }

      if (this.collation != null) {
         commandDocument.put((String)"collation", (BsonValue)this.collation.asDocument());
      }

      if (this.comment != null) {
         commandDocument.put("comment", this.comment);
      }

      if (this.hint != null) {
         commandDocument.put("hint", this.hint);
      }

      if (this.variables != null) {
         commandDocument.put((String)"let", (BsonValue)this.variables);
      }

      return commandDocument;
   }
}
