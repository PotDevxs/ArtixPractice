package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public abstract class BaseFindAndModifyOperation<T> implements AsyncWriteOperation<T>, WriteOperation<T> {
   private final MongoNamespace namespace;
   private final WriteConcern writeConcern;
   private final boolean retryWrites;
   private final Decoder<T> decoder;
   private BsonDocument filter;
   private BsonDocument projection;
   private BsonDocument sort;
   private long maxTimeMS;
   private Collation collation;
   private Bson hint;
   private String hintString;
   private BsonValue comment;
   private BsonDocument variables;

   protected BaseFindAndModifyOperation(MongoNamespace namespace, WriteConcern writeConcern, boolean retryWrites, Decoder<T> decoder) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.writeConcern = (WriteConcern)Assertions.notNull("writeConcern", writeConcern);
      this.retryWrites = retryWrites;
      this.decoder = (Decoder)Assertions.notNull("decoder", decoder);
   }

   public T execute(WriteBinding binding) {
      return SyncOperationHelper.executeRetryableWrite(binding, this.getDatabaseName(), (ReadPreference)null, this.getFieldNameValidator(), CommandResultDocumentCodec.create(this.getDecoder(), "value"), this.getCommandCreator(binding.getSessionContext()), FindAndModifyHelper.transformer(), (cmd) -> {
         return cmd;
      });
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<T> callback) {
      AsyncOperationHelper.executeRetryableWriteAsync(binding, this.getDatabaseName(), (ReadPreference)null, this.getFieldNameValidator(), CommandResultDocumentCodec.create(this.getDecoder(), "value"), this.getCommandCreator(binding.getSessionContext()), FindAndModifyHelper.asyncTransformer(), (cmd) -> {
         return cmd;
      }, callback);
   }

   public MongoNamespace getNamespace() {
      return this.namespace;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public Decoder<T> getDecoder() {
      return this.decoder;
   }

   public boolean isRetryWrites() {
      return this.retryWrites;
   }

   public BsonDocument getFilter() {
      return this.filter;
   }

   public BaseFindAndModifyOperation<T> filter(@Nullable BsonDocument filter) {
      this.filter = filter;
      return this;
   }

   public BsonDocument getProjection() {
      return this.projection;
   }

   public BaseFindAndModifyOperation<T> projection(@Nullable BsonDocument projection) {
      this.projection = projection;
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public BaseFindAndModifyOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public BsonDocument getSort() {
      return this.sort;
   }

   public BaseFindAndModifyOperation<T> sort(@Nullable BsonDocument sort) {
      this.sort = sort;
      return this;
   }

   @Nullable
   public Collation getCollation() {
      return this.collation;
   }

   @Nullable
   public Bson getHint() {
      return this.hint;
   }

   public BaseFindAndModifyOperation<T> hint(@Nullable Bson hint) {
      this.hint = hint;
      return this;
   }

   @Nullable
   public String getHintString() {
      return this.hintString;
   }

   public BaseFindAndModifyOperation<T> hintString(@Nullable String hint) {
      this.hintString = hint;
      return this;
   }

   public BaseFindAndModifyOperation<T> collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public BsonValue getComment() {
      return this.comment;
   }

   public BaseFindAndModifyOperation<T> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public BsonDocument getLet() {
      return this.variables;
   }

   public BaseFindAndModifyOperation<T> let(@Nullable BsonDocument variables) {
      this.variables = variables;
      return this;
   }

   protected abstract FieldNameValidator getFieldNameValidator();

   protected abstract void specializeCommand(BsonDocument var1, ConnectionDescription var2);

   private CommandOperationHelper.CommandCreator getCommandCreator(SessionContext sessionContext) {
      return (serverDescription, connectionDescription) -> {
         BsonDocument commandDocument = new BsonDocument("findAndModify", new BsonString(this.getNamespace().getCollectionName()));
         DocumentHelper.putIfNotNull(commandDocument, "query", (BsonValue)this.getFilter());
         DocumentHelper.putIfNotNull(commandDocument, "fields", (BsonValue)this.getProjection());
         DocumentHelper.putIfNotNull(commandDocument, "sort", (BsonValue)this.getSort());
         this.specializeCommand(commandDocument, connectionDescription);
         DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.getMaxTime(TimeUnit.MILLISECONDS));
         if (this.getWriteConcern().isAcknowledged() && !this.getWriteConcern().isServerDefault() && !sessionContext.hasActiveTransaction()) {
            commandDocument.put((String)"writeConcern", (BsonValue)this.getWriteConcern().asDocument());
         }

         if (this.getCollation() != null) {
            commandDocument.put((String)"collation", (BsonValue)this.getCollation().asDocument());
         }

         if (this.getHint() != null || this.getHintString() != null) {
            OperationHelper.validateHintForFindAndModify(connectionDescription, this.getWriteConcern());
            if (this.getHint() != null) {
               commandDocument.put((String)"hint", (BsonValue)this.getHint().toBsonDocument(BsonDocument.class, (CodecRegistry)null));
            } else {
               commandDocument.put((String)"hint", (BsonValue)(new BsonString(this.getHintString())));
            }
         }

         DocumentHelper.putIfNotNull(commandDocument, "comment", this.getComment());
         DocumentHelper.putIfNotNull(commandDocument, "let", (BsonValue)this.getLet());
         if (OperationHelper.isRetryableWrite(this.isRetryWrites(), this.getWriteConcern(), connectionDescription, sessionContext)) {
            commandDocument.put((String)"txnNumber", (BsonValue)(new BsonInt64(sessionContext.advanceTransactionNumber())));
         }

         return commandDocument;
      };
   }

   private String getDatabaseName() {
      return this.getNamespace().getDatabaseName();
   }
}
