package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class EstimatedDocumentCountOperation implements AsyncReadOperation<Long>, ReadOperation<Long> {
   private static final Decoder<BsonDocument> DECODER = new BsonDocumentCodec();
   private final MongoNamespace namespace;
   private boolean retryReads;
   private long maxTimeMS;
   private BsonValue comment;

   public EstimatedDocumentCountOperation(MongoNamespace namespace) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
   }

   public EstimatedDocumentCountOperation retryReads(boolean retryReads) {
      this.retryReads = retryReads;
      return this;
   }

   public EstimatedDocumentCountOperation maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public EstimatedDocumentCountOperation comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public Long execute(ReadBinding binding) {
      try {
         return (Long)SyncOperationHelper.executeRetryableRead(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(DECODER, Collections.singletonList("firstBatch")), this.transformer(), this.retryReads);
      } catch (MongoCommandException e) {
         return (Long)Assertions.assertNotNull((Long)CommandOperationHelper.rethrowIfNotNamespaceError(e, 0L));
      }
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<Long> callback) {
      AsyncOperationHelper.executeRetryableReadAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(DECODER, Collections.singletonList("firstBatch")), this.asyncTransformer(), this.retryReads, (result, t) -> {
         if (CommandOperationHelper.isNamespaceError(t)) {
            callback.onResult(0L, (Throwable)null);
         } else {
            callback.onResult(result, t);
         }

      });
   }

   private SyncOperationHelper.CommandReadTransformer<BsonDocument, Long> transformer() {
      return (result, source, connection) -> {
         return this.transformResult(result, connection.getDescription());
      };
   }

   private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, Long> asyncTransformer() {
      return (result, source, connection) -> {
         return this.transformResult(result, connection.getDescription());
      };
   }

   private long transformResult(BsonDocument result, ConnectionDescription connectionDescription) {
      return result.getNumber("n").longValue();
   }

   private CommandOperationHelper.CommandCreator getCommandCreator(SessionContext sessionContext) {
      return (serverDescription, connectionDescription) -> {
         BsonDocument document = new BsonDocument("count", new BsonString(this.namespace.getCollectionName()));
         OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, connectionDescription.getMaxWireVersion(), document);
         DocumentHelper.putIfNotZero(document, "maxTimeMS", this.maxTimeMS);
         if (this.comment != null) {
            document.put("comment", this.comment);
         }

         return document;
      };
   }
}
