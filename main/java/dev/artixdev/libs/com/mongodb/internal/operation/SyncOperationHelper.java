package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.function.LoopState;
import dev.artixdev.libs.com.mongodb.internal.async.function.RetryState;
import dev.artixdev.libs.com.mongodb.internal.async.function.RetryingSyncSupplier;
import dev.artixdev.libs.com.mongodb.internal.binding.ConnectionSource;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReferenceCounted;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.internal.connection.Connection;
import dev.artixdev.libs.com.mongodb.internal.connection.OperationContext;
import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;
import dev.artixdev.libs.com.mongodb.internal.operation.retry.AttachmentKeys;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.Decoder;

final class SyncOperationHelper {
   static <T> T withReadConnectionSource(ReadBinding binding, SyncOperationHelper.CallableWithSource<T> callable) {
      ConnectionSource source = binding.getReadConnectionSource();

      T result;
      try {
         result = callable.call(source);
      } finally {
         source.release();
      }

      return result;
   }

   static <T> T withConnection(WriteBinding binding, SyncOperationHelper.CallableWithConnection<T> callable) {
      ConnectionSource source = binding.getWriteConnectionSource();

      T result;
      try {
         result = withConnectionSource(source, callable);
      } finally {
         source.release();
      }

      return result;
   }

   static <R> R withSourceAndConnection(Supplier<ConnectionSource> sourceSupplier, boolean wrapConnectionSourceException, BiFunction<ConnectionSource, Connection, R> function) throws OperationHelper.ResourceSupplierInternalException {
      return withSuppliedResource(sourceSupplier, wrapConnectionSourceException, (source) -> {
         Objects.requireNonNull(source);
         return withSuppliedResource(source::getConnection, wrapConnectionSourceException, (connection) -> {
            return function.apply(source, connection);
         });
      });
   }

   static <R, T extends ReferenceCounted> R withSuppliedResource(Supplier<T> resourceSupplier, boolean wrapSupplierException, Function<T, R> function) throws OperationHelper.ResourceSupplierInternalException {
      T resource = null;

      R result;
      try {
         try {
            resource = resourceSupplier.get();
         } catch (Exception e) {
            if (wrapSupplierException) {
               throw new OperationHelper.ResourceSupplierInternalException(e);
            }

            throw e;
         }

         result = function.apply(resource);
      } finally {
         if (resource != null) {
            resource.release();
         }

      }

      return result;
   }

   private static <T> T withConnectionSource(ConnectionSource source, SyncOperationHelper.CallableWithConnection<T> callable) {
      Connection connection = source.getConnection();

      T result;
      try {
         result = callable.call(connection);
      } finally {
         connection.release();
      }

      return result;
   }

   static <D, T> T executeRetryableRead(ReadBinding binding, String database, CommandOperationHelper.CommandCreator commandCreator, Decoder<D> decoder, SyncOperationHelper.CommandReadTransformer<D, T> transformer, boolean retryReads) {
      Objects.requireNonNull(binding);
      return executeRetryableRead(binding, binding::getReadConnectionSource, database, commandCreator, decoder, transformer, retryReads);
   }

   static <D, T> T executeRetryableRead(ReadBinding binding, Supplier<ConnectionSource> readConnectionSourceSupplier, String database, CommandOperationHelper.CommandCreator commandCreator, Decoder<D> decoder, SyncOperationHelper.CommandReadTransformer<D, T> transformer, boolean retryReads) {
      RetryState retryState = CommandOperationHelper.initialRetryState(retryReads);
      Supplier<T> read = decorateReadWithRetries(retryState, binding.getOperationContext(), () -> {
         return withSourceAndConnection(readConnectionSourceSupplier, false, (source, connection) -> {
            retryState.breakAndThrowIfRetryAnd(() -> {
               return !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext());
            });
            return createReadCommandAndExecute(retryState, binding, source, database, commandCreator, decoder, transformer, connection);
         });
      });
      return read.get();
   }

   static <D, T> T executeCommand(WriteBinding binding, String database, BsonDocument command, Decoder<D> decoder, SyncOperationHelper.CommandWriteTransformer<D, T> transformer) {
      Objects.requireNonNull(binding);
      return withSourceAndConnection(binding::getWriteConnectionSource, false, (source, connection) -> {
         return transformer.apply(Assertions.assertNotNull(connection.command(database, command, new NoOpFieldNameValidator(), ReadPreference.primary(), decoder, binding)), connection);
      });
   }

   @Nullable
   static <T> T executeCommand(WriteBinding binding, String database, BsonDocument command, Connection connection, SyncOperationHelper.CommandWriteTransformer<BsonDocument, T> transformer) {
      Assertions.notNull("binding", binding);
      return transformer.apply((BsonDocument)Assertions.assertNotNull((BsonDocument)connection.command(database, command, new NoOpFieldNameValidator(), ReadPreference.primary(), new BsonDocumentCodec(), binding)), connection);
   }

   static <T, R> R executeRetryableWrite(WriteBinding binding, String database, @Nullable ReadPreference readPreference, FieldNameValidator fieldNameValidator, Decoder<T> commandResultDecoder, CommandOperationHelper.CommandCreator commandCreator, SyncOperationHelper.CommandWriteTransformer<T, R> transformer, dev.artixdev.libs.com.mongodb.Function<BsonDocument, BsonDocument> retryCommandModifier) {
      RetryState retryState = CommandOperationHelper.initialRetryState(true);
      Supplier<R> retryingWrite = decorateWriteWithRetries(retryState, binding.getOperationContext(), () -> {
         boolean firstAttempt = retryState.isFirstAttempt();
         if (!firstAttempt && binding.getSessionContext().hasActiveTransaction()) {
            binding.getSessionContext().clearTransactionContext();
         }

         Objects.requireNonNull(binding);
         return withSourceAndConnection(binding::getWriteConnectionSource, true, (source, connection) -> {
            int maxWireVersion = connection.getDescription().getMaxWireVersion();

            try {
               retryState.breakAndThrowIfRetryAnd(() -> {
                  return !OperationHelper.canRetryWrite(connection.getDescription(), binding.getSessionContext());
               });
               BsonDocument command = (BsonDocument)retryState.attachment(AttachmentKeys.command()).map((previousAttemptCommand) -> {
                  Assertions.assertFalse(firstAttempt);
                  return (BsonDocument)retryCommandModifier.apply(previousAttemptCommand);
               }).orElseGet(() -> {
                  return commandCreator.create(source.getServerDescription(), connection.getDescription());
               });
               RetryState var10000 = retryState.attach(AttachmentKeys.maxWireVersion(), maxWireVersion, true).attach(AttachmentKeys.retryableCommandFlag(), CommandOperationHelper.isRetryWritesEnabled(command), true);
               LoopState.AttachmentKey<Supplier<String>> commandDescriptionKey = AttachmentKeys.commandDescriptionSupplier();
               Objects.requireNonNull(command);
               var10000.attach(commandDescriptionKey, command::getFirstKey, false).attach(AttachmentKeys.command(), command, false);
               return transformer.apply(Assertions.assertNotNull(connection.command(database, command, fieldNameValidator, readPreference, commandResultDecoder, binding)), connection);
            } catch (MongoException e) {
               if (!firstAttempt) {
                  CommandOperationHelper.addRetryableWriteErrorLabel(e, maxWireVersion);
               }

               throw e;
            }
         });
      });

      try {
         return retryingWrite.get();
      } catch (MongoException e) {
         throw CommandOperationHelper.transformWriteException(e);
      }
   }

   @Nullable
   static <D, T> T createReadCommandAndExecute(RetryState retryState, ReadBinding binding, ConnectionSource source, String database, CommandOperationHelper.CommandCreator commandCreator, Decoder<D> decoder, SyncOperationHelper.CommandReadTransformer<D, T> transformer, Connection connection) {
      BsonDocument command = commandCreator.create(source.getServerDescription(), connection.getDescription());
      LoopState.AttachmentKey<Supplier<String>> commandDescriptionKey = AttachmentKeys.commandDescriptionSupplier();
      Objects.requireNonNull(command);
      retryState.attach(commandDescriptionKey, command::getFirstKey, false);
      return transformer.apply(Assertions.assertNotNull(connection.command(database, command, new NoOpFieldNameValidator(), source.getReadPreference(), decoder, binding)), source, connection);
   }

   static <R> Supplier<R> decorateWriteWithRetries(RetryState retryState, OperationContext operationContext, Supplier<R> writeFunction) {
      return new RetryingSyncSupplier<R>(retryState, CommandOperationHelper::chooseRetryableWriteException, CommandOperationHelper::shouldAttemptToRetryWrite, () -> {
         CommandOperationHelper.logRetryExecute(retryState, operationContext);
         return writeFunction.get();
      });
   }

   static <R> Supplier<R> decorateReadWithRetries(RetryState retryState, OperationContext operationContext, Supplier<R> readFunction) {
      return new RetryingSyncSupplier<R>(retryState, CommandOperationHelper::chooseRetryableReadException, CommandOperationHelper::shouldAttemptToRetryRead, () -> {
         CommandOperationHelper.logRetryExecute(retryState, operationContext);
         return readFunction.get();
      });
   }

   static SyncOperationHelper.CommandWriteTransformer<BsonDocument, Void> writeConcernErrorTransformer() {
      return (result, connection) -> {
         Assertions.assertNotNull(result);
         WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
         return null;
      };
   }

   static <T> BatchCursor<T> cursorDocumentToBatchCursor(BsonDocument cursorDocument, Decoder<T> decoder, BsonValue comment, ConnectionSource source, Connection connection, int batchSize) {
      return new QueryBatchCursor(OperationHelper.cursorDocumentToQueryResult(cursorDocument, source.getServerDescription().getAddress()), 0, batchSize, 0L, decoder, comment, source, connection);
   }

   static <T> QueryResult<T> getMoreCursorDocumentToQueryResult(BsonDocument cursorDocument, ServerAddress serverAddress) {
      return OperationHelper.cursorDocumentToQueryResult(cursorDocument, serverAddress, "nextBatch");
   }

   private SyncOperationHelper() {
   }

   interface CallableWithSource<T> {
      T call(ConnectionSource var1);
   }

   interface CallableWithConnection<T> {
      T call(Connection var1);
   }

   interface CommandReadTransformer<T, R> {
      @Nullable
      R apply(T var1, ConnectionSource var2, Connection var3);
   }

   interface CommandWriteTransformer<T, R> {
      @Nullable
      R apply(T var1, Connection var2);
   }
}
