package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class ListDatabasesOperation<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>> {
   private final Decoder<T> decoder;
   private boolean retryReads;
   private long maxTimeMS;
   private BsonDocument filter;
   private Boolean nameOnly;
   private Boolean authorizedDatabasesOnly;
   private BsonValue comment;

   public ListDatabasesOperation(Decoder<T> decoder) {
      this.decoder = (Decoder)Assertions.notNull("decoder", decoder);
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public ListDatabasesOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public ListDatabasesOperation<T> filter(@Nullable BsonDocument filter) {
      this.filter = filter;
      return this;
   }

   public BsonDocument getFilter() {
      return this.filter;
   }

   public ListDatabasesOperation<T> nameOnly(Boolean nameOnly) {
      this.nameOnly = nameOnly;
      return this;
   }

   public ListDatabasesOperation<T> authorizedDatabasesOnly(Boolean authorizedDatabasesOnly) {
      this.authorizedDatabasesOnly = authorizedDatabasesOnly;
      return this;
   }

   public ListDatabasesOperation<T> retryReads(boolean retryReads) {
      this.retryReads = retryReads;
      return this;
   }

   public boolean getRetryReads() {
      return this.retryReads;
   }

   public Boolean getNameOnly() {
      return this.nameOnly;
   }

   public Boolean getAuthorizedDatabasesOnly() {
      return this.authorizedDatabasesOnly;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public ListDatabasesOperation<T> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public BatchCursor<T> execute(ReadBinding binding) {
      return (BatchCursor)SyncOperationHelper.executeRetryableRead(binding, "admin", this.getCommandCreator(), CommandResultDocumentCodec.create(this.decoder, "databases"), this.transformer(), this.retryReads);
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
      AsyncOperationHelper.executeRetryableReadAsync(binding, "admin", this.getCommandCreator(), CommandResultDocumentCodec.create(this.decoder, "databases"), this.asyncTransformer(), this.retryReads, ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
   }

   private SyncOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>> transformer() {
      return (result, source, connection) -> {
         return new QueryBatchCursor(this.createQueryResult(result, connection.getDescription()), 0, 0, this.decoder, this.comment, source);
      };
   }

   private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
      return (result, source, connection) -> {
         return new AsyncQueryBatchCursor(this.createQueryResult(result, connection.getDescription()), 0, 0, 0L, this.decoder, this.comment, source, connection, result);
      };
   }

   private QueryResult<T> createQueryResult(BsonDocument result, ConnectionDescription description) {
      return new QueryResult((MongoNamespace)null, BsonDocumentWrapperHelper.toList(result, "databases"), 0L, description.getServerAddress());
   }

   private CommandOperationHelper.CommandCreator getCommandCreator() {
      return (serverDescription, connectionDescription) -> {
         return this.getCommand();
      };
   }

   private BsonDocument getCommand() {
      BsonDocument command = new BsonDocument("listDatabases", new BsonInt32(1));
      if (this.maxTimeMS > 0L) {
         command.put((String)"maxTimeMS", (BsonValue)(new BsonInt64(this.maxTimeMS)));
      }

      if (this.filter != null) {
         command.put((String)"filter", (BsonValue)this.filter);
      }

      if (this.nameOnly != null) {
         command.put((String)"nameOnly", (BsonValue)(new BsonBoolean(this.nameOnly)));
      }

      if (this.authorizedDatabasesOnly != null) {
         command.put((String)"authorizedDatabases", (BsonValue)(new BsonBoolean(this.authorizedDatabasesOnly)));
      }

      DocumentHelper.putIfNotNull(command, "comment", this.comment);
      return command;
   }
}
