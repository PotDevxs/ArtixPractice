package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.function.AsyncCallbackSupplier;
import dev.artixdev.libs.com.mongodb.internal.async.function.RetryState;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class ListCollectionsOperation<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>> {
   private final String databaseName;
   private final Decoder<T> decoder;
   private boolean retryReads;
   private BsonDocument filter;
   private int batchSize;
   private long maxTimeMS;
   private boolean nameOnly;
   private BsonValue comment;

   public ListCollectionsOperation(String databaseName, Decoder<T> decoder) {
      this.databaseName = (String)Assertions.notNull("databaseName", databaseName);
      this.decoder = (Decoder)Assertions.notNull("decoder", decoder);
   }

   public BsonDocument getFilter() {
      return this.filter;
   }

   public boolean isNameOnly() {
      return this.nameOnly;
   }

   public ListCollectionsOperation<T> filter(@Nullable BsonDocument filter) {
      this.filter = filter;
      return this;
   }

   public ListCollectionsOperation<T> nameOnly(boolean nameOnly) {
      this.nameOnly = nameOnly;
      return this;
   }

   public Integer getBatchSize() {
      return this.batchSize;
   }

   public ListCollectionsOperation<T> batchSize(int batchSize) {
      this.batchSize = batchSize;
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public ListCollectionsOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public ListCollectionsOperation<T> retryReads(boolean retryReads) {
      this.retryReads = retryReads;
      return this;
   }

   public boolean getRetryReads() {
      return this.retryReads;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public ListCollectionsOperation<T> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public BatchCursor<T> execute(ReadBinding binding) {
      RetryState retryState = CommandOperationHelper.initialRetryState(this.retryReads);
      Supplier<BatchCursor<T>> read = SyncOperationHelper.decorateReadWithRetries(retryState, binding.getOperationContext(), () -> {
         Objects.requireNonNull(binding);
         return (BatchCursor)SyncOperationHelper.withSourceAndConnection(binding::getReadConnectionSource, false, (source, connection) -> {
            retryState.breakAndThrowIfRetryAnd(() -> {
               return !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext());
            });

            try {
               return (BatchCursor)SyncOperationHelper.createReadCommandAndExecute(retryState, binding, source, this.databaseName, this.getCommandCreator(), this.createCommandDecoder(), this.commandTransformer(), connection);
            } catch (MongoCommandException e) {
               return (BatchCursor)CommandOperationHelper.rethrowIfNotNamespaceError(e, OperationHelper.createEmptyBatchCursor(this.createNamespace(), this.decoder, source.getServerDescription().getAddress(), this.batchSize));
            }
         });
      });
      return (BatchCursor)read.get();
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
      RetryState retryState = CommandOperationHelper.initialRetryState(this.retryReads);
      binding.retain();
      AsyncCallbackSupplier var10000 = AsyncOperationHelper.decorateReadWithRetriesAsync(retryState, binding.getOperationContext(), (funcCallback) -> {
         Objects.requireNonNull(binding);
         AsyncOperationHelper.withAsyncSourceAndConnection(binding::getReadConnectionSource, false, funcCallback, (source, connection, releasingCallback) -> {
            if (!retryState.breakAndCompleteIfRetryAnd(() -> {
               return !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext());
            }, releasingCallback)) {
               AsyncOperationHelper.createReadCommandAndExecuteAsync(retryState, binding, source, this.databaseName, this.getCommandCreator(), this.createCommandDecoder(), this.asyncTransformer(), connection, (result, t) -> {
                  if (t != null && !CommandOperationHelper.isNamespaceError(t)) {
                     releasingCallback.onResult(null, t);
                  } else {
                     releasingCallback.onResult(result != null ? result : this.emptyAsyncCursor(source), (Throwable)null);
                  }

               });
            }
         });
      });
      Objects.requireNonNull(binding);
      AsyncCallbackSupplier<AsyncBatchCursor<T>> asyncRead = var10000.whenComplete(binding::release);
      asyncRead.get(ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
   }

   private AsyncBatchCursor<T> emptyAsyncCursor(AsyncConnectionSource source) {
      return AsyncOperationHelper.createEmptyAsyncBatchCursor(this.createNamespace(), source.getServerDescription().getAddress());
   }

   private MongoNamespace createNamespace() {
      return new MongoNamespace(this.databaseName, "$cmd.listCollections");
   }

   private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
      return (result, source, connection) -> {
         return AsyncOperationHelper.cursorDocumentToAsyncBatchCursor(result.getDocument("cursor"), this.decoder, this.comment, source, connection, this.batchSize);
      };
   }

   private SyncOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>> commandTransformer() {
      return (result, source, connection) -> {
         return SyncOperationHelper.cursorDocumentToBatchCursor(result.getDocument("cursor"), this.decoder, this.comment, source, connection, this.batchSize);
      };
   }

   private CommandOperationHelper.CommandCreator getCommandCreator() {
      return (serverDescription, connectionDescription) -> {
         return this.getCommand();
      };
   }

   private BsonDocument getCommand() {
      BsonDocument command = (new BsonDocument("listCollections", new BsonInt32(1))).append("cursor", CursorHelper.getCursorDocumentFromBatchSize(this.batchSize == 0 ? null : this.batchSize));
      if (this.filter != null) {
         command.append("filter", this.filter);
      }

      if (this.nameOnly) {
         command.append("nameOnly", BsonBoolean.TRUE);
      }

      if (this.maxTimeMS > 0L) {
         command.put((String)"maxTimeMS", (BsonValue)(new BsonInt64(this.maxTimeMS)));
      }

      DocumentHelper.putIfNotNull(command, "comment", this.comment);
      return command;
   }

   private Codec<BsonDocument> createCommandDecoder() {
      return CommandResultDocumentCodec.create(this.decoder, "firstBatch");
   }
}
