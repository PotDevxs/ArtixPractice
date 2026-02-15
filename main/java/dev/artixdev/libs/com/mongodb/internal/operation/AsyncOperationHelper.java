package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.Function;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.function.AsyncCallbackBiFunction;
import dev.artixdev.libs.com.mongodb.internal.async.function.AsyncCallbackFunction;
import dev.artixdev.libs.com.mongodb.internal.async.function.AsyncCallbackSupplier;
import dev.artixdev.libs.com.mongodb.internal.async.function.LoopState;
import dev.artixdev.libs.com.mongodb.internal.async.function.RetryState;
import dev.artixdev.libs.com.mongodb.internal.async.function.RetryingAsyncCallbackSupplier;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReferenceCounted;
import dev.artixdev.libs.com.mongodb.internal.connection.AsyncConnection;
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

final class AsyncOperationHelper {
   static void withAsyncReadConnectionSource(AsyncReadBinding binding, AsyncOperationHelper.AsyncCallableWithSource callable) {
      binding.getReadConnectionSource(ErrorHandlingResultCallback.errorHandlingCallback(new AsyncOperationHelper.AsyncCallableWithSourceCallback(callable), OperationHelper.LOGGER));
   }

   static void withAsyncConnection(AsyncWriteBinding binding, AsyncOperationHelper.AsyncCallableWithConnection callable) {
      binding.getWriteConnectionSource(ErrorHandlingResultCallback.errorHandlingCallback(new AsyncOperationHelper.AsyncCallableWithConnectionCallback(callable), OperationHelper.LOGGER));
   }

   static <R> void withAsyncSourceAndConnection(AsyncCallbackSupplier<AsyncConnectionSource> sourceSupplier, boolean wrapConnectionSourceException, SingleResultCallback<R> callback, AsyncCallbackBiFunction<AsyncConnectionSource, AsyncConnection, R> asyncFunction) throws OperationHelper.ResourceSupplierInternalException {
      SingleResultCallback<R> errorHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
      withAsyncSuppliedResource(sourceSupplier, wrapConnectionSourceException, errorHandlingCallback, (source, sourceReleasingCallback) -> {
         Objects.requireNonNull(source);
         withAsyncSuppliedResource(source::getConnection, wrapConnectionSourceException, sourceReleasingCallback, (connection, connectionAndSourceReleasingCallback) -> {
            asyncFunction.apply(source, connection, connectionAndSourceReleasingCallback);
         });
      });
   }

   static <R, T extends ReferenceCounted> void withAsyncSuppliedResource(AsyncCallbackSupplier<T> resourceSupplier, boolean wrapSourceConnectionException, SingleResultCallback<R> callback, AsyncCallbackFunction<T, R> function) throws OperationHelper.ResourceSupplierInternalException {
      SingleResultCallback<R> errorHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
      resourceSupplier.get((resource, supplierException) -> {
         if (supplierException != null) {
            if (wrapSourceConnectionException) {
               supplierException = new OperationHelper.ResourceSupplierInternalException((Throwable)supplierException);
            }

            errorHandlingCallback.onResult(null, (Throwable)supplierException);
         } else {
            Assertions.assertNotNull(resource);
            AsyncCallbackSupplier<R> curriedFunction = (c) -> {
               function.apply(resource, c);
            };
            Objects.requireNonNull(resource);
            curriedFunction.whenComplete(resource::release).get(errorHandlingCallback);
         }

      });
   }

   static void withAsyncConnectionSourceCallableConnection(AsyncConnectionSource source, AsyncOperationHelper.AsyncCallableWithConnection callable) {
      source.getConnection((connection, t) -> {
         source.release();
         if (t != null) {
            callable.call((AsyncConnection)null, t);
         } else {
            callable.call(connection, (Throwable)null);
         }

      });
   }

   static void withAsyncConnectionSource(AsyncConnectionSource source, AsyncOperationHelper.AsyncCallableWithSource callable) {
      callable.call(source, (Throwable)null);
   }

   static <D, T> void executeRetryableReadAsync(AsyncReadBinding binding, String database, CommandOperationHelper.CommandCreator commandCreator, Decoder<D> decoder, AsyncOperationHelper.CommandReadTransformerAsync<D, T> transformer, boolean retryReads, SingleResultCallback<T> callback) {
      Objects.requireNonNull(binding);
      executeRetryableReadAsync(binding, binding::getReadConnectionSource, database, commandCreator, decoder, transformer, retryReads, callback);
   }

   static <D, T> void executeRetryableReadAsync(AsyncReadBinding binding, AsyncCallbackSupplier<AsyncConnectionSource> sourceAsyncSupplier, String database, CommandOperationHelper.CommandCreator commandCreator, Decoder<D> decoder, AsyncOperationHelper.CommandReadTransformerAsync<D, T> transformer, boolean retryReads, SingleResultCallback<T> callback) {
      RetryState retryState = CommandOperationHelper.initialRetryState(retryReads);
      binding.retain();
      AsyncCallbackSupplier<T> decoratedRead = decorateReadWithRetriesAsync(retryState, binding.getOperationContext(), (funcCallback) -> {
         withAsyncSourceAndConnection(sourceAsyncSupplier, false, funcCallback, (source, connection, releasingCallback) -> {
            if (!retryState.breakAndCompleteIfRetryAnd(() -> {
               return !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext());
            }, releasingCallback)) {
               createReadCommandAndExecuteAsync(retryState, binding, source, database, commandCreator, decoder, transformer, connection, releasingCallback);
            }
         });
      });
      Objects.requireNonNull(binding);
      AsyncCallbackSupplier<T> asyncRead = decoratedRead.whenComplete(binding::release);
      asyncRead.get(ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
   }

   static <T> void executeCommandAsync(AsyncWriteBinding binding, String database, BsonDocument command, AsyncConnection connection, AsyncOperationHelper.CommandWriteTransformerAsync<BsonDocument, T> transformer, SingleResultCallback<T> callback) {
      Assertions.notNull("binding", binding);
      SingleResultCallback<T> addingRetryableLabelCallback = addingRetryableLabelCallback(callback, connection.getDescription().getMaxWireVersion());
      connection.commandAsync(database, command, new NoOpFieldNameValidator(), ReadPreference.primary(), new BsonDocumentCodec(), binding, transformingWriteCallback(transformer, connection, addingRetryableLabelCallback));
   }

   static <T, R> void executeRetryableWriteAsync(AsyncWriteBinding binding, String database, @Nullable ReadPreference readPreference, FieldNameValidator fieldNameValidator, Decoder<T> commandResultDecoder, CommandOperationHelper.CommandCreator commandCreator, AsyncOperationHelper.CommandWriteTransformerAsync<T, R> transformer, Function<BsonDocument, BsonDocument> retryCommandModifier, SingleResultCallback<R> callback) {
      RetryState retryState = CommandOperationHelper.initialRetryState(true);
      binding.retain();
      AsyncCallbackSupplier<R> decoratedWrite = decorateWriteWithRetriesAsync(retryState, binding.getOperationContext(), (funcCallback) -> {
         boolean firstAttempt = retryState.isFirstAttempt();
         if (!firstAttempt && binding.getSessionContext().hasActiveTransaction()) {
            binding.getSessionContext().clearTransactionContext();
         }

         Objects.requireNonNull(binding);
         withAsyncSourceAndConnection(binding::getWriteConnectionSource, true, funcCallback, (source, connection, releasingCallback) -> {
            int maxWireVersion = connection.getDescription().getMaxWireVersion();
            SingleResultCallback<R> addingRetryableLabelCallback = firstAttempt ? releasingCallback : addingRetryableLabelCallback(releasingCallback, maxWireVersion);
            if (!retryState.breakAndCompleteIfRetryAnd(() -> {
               return !OperationHelper.canRetryWrite(connection.getDescription(), binding.getSessionContext());
            }, addingRetryableLabelCallback)) {
               BsonDocument command;
               try {
                  command = (BsonDocument)retryState.attachment(AttachmentKeys.command()).map((previousAttemptCommand) -> {
                     Assertions.assertFalse(firstAttempt);
                     return (BsonDocument)retryCommandModifier.apply(previousAttemptCommand);
                  }).orElseGet(() -> {
                     return commandCreator.create(source.getServerDescription(), connection.getDescription());
                  });
                  RetryState stateWithAttachments = retryState.attach(AttachmentKeys.maxWireVersion(), maxWireVersion, true).attach(AttachmentKeys.retryableCommandFlag(), CommandOperationHelper.isRetryWritesEnabled(command), true);
                  Objects.requireNonNull(command);
                  stateWithAttachments.attach(AttachmentKeys.commandDescriptionSupplier(), command::getFirstKey, false).attach(AttachmentKeys.command(), command, false);
               } catch (Throwable throwable) {
                  addingRetryableLabelCallback.onResult(null, throwable);
                  return;
               }

               connection.commandAsync(database, command, fieldNameValidator, readPreference, commandResultDecoder, binding, transformingWriteCallback(transformer, connection, addingRetryableLabelCallback));
            }
         });
      });
      Objects.requireNonNull(binding);
      AsyncCallbackSupplier<R> asyncWrite = decoratedWrite.whenComplete(binding::release);
      asyncWrite.get(exceptionTransformingCallback(ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER)));
   }

   static <D, T> void createReadCommandAndExecuteAsync(RetryState retryState, AsyncReadBinding binding, AsyncConnectionSource source, String database, CommandOperationHelper.CommandCreator commandCreator, Decoder<D> decoder, AsyncOperationHelper.CommandReadTransformerAsync<D, T> transformer, AsyncConnection connection, SingleResultCallback<T> callback) {
      BsonDocument command;
      try {
         command = commandCreator.create(source.getServerDescription(), connection.getDescription());
         Objects.requireNonNull(command);
         retryState.attach(AttachmentKeys.commandDescriptionSupplier(), command::getFirstKey, false);
      } catch (IllegalArgumentException exception) {
         callback.onResult(null, exception);
         return;
      }

      connection.commandAsync(database, command, new NoOpFieldNameValidator(), source.getReadPreference(), decoder, binding, transformingReadCallback(transformer, source, connection, callback));
   }

   static <R> AsyncCallbackSupplier<R> decorateReadWithRetriesAsync(RetryState retryState, OperationContext operationContext, AsyncCallbackSupplier<R> asyncReadFunction) {
      return new RetryingAsyncCallbackSupplier<R>(retryState, CommandOperationHelper::chooseRetryableReadException, CommandOperationHelper::shouldAttemptToRetryRead, (callback) -> {
         CommandOperationHelper.logRetryExecute(retryState, operationContext);
         asyncReadFunction.get(callback);
      });
   }

   static <R> AsyncCallbackSupplier<R> decorateWriteWithRetriesAsync(RetryState retryState, OperationContext operationContext, AsyncCallbackSupplier<R> asyncWriteFunction) {
      return new RetryingAsyncCallbackSupplier<R>(retryState, CommandOperationHelper::chooseRetryableWriteException, CommandOperationHelper::shouldAttemptToRetryWrite, (callback) -> {
         CommandOperationHelper.logRetryExecute(retryState, operationContext);
         asyncWriteFunction.get(callback);
      });
   }

   static AsyncOperationHelper.CommandWriteTransformerAsync<BsonDocument, Void> writeConcernErrorTransformerAsync() {
      return (result, connection) -> {
         Assertions.assertNotNull(result);
         WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress(), connection.getDescription().getMaxWireVersion());
         return null;
      };
   }

   static <T> AsyncBatchCursor<T> createEmptyAsyncBatchCursor(MongoNamespace namespace, ServerAddress serverAddress) {
      return new AsyncSingleBatchQueryCursor(new QueryResult(namespace, Collections.emptyList(), 0L, serverAddress));
   }

   static <T> AsyncBatchCursor<T> cursorDocumentToAsyncBatchCursor(BsonDocument cursorDocument, Decoder<T> decoder, BsonValue comment, AsyncConnectionSource source, AsyncConnection connection, int batchSize) {
      return new AsyncQueryBatchCursor(OperationHelper.cursorDocumentToQueryResult(cursorDocument, source.getServerDescription().getAddress()), 0, batchSize, 0L, decoder, comment, source, connection, cursorDocument);
   }

   static <T> SingleResultCallback<T> releasingCallback(SingleResultCallback<T> wrapped, AsyncConnection connection) {
      return new AsyncOperationHelper.ReferenceCountedReleasingWrappedCallback(wrapped, Collections.singletonList(connection));
   }

   static <R> SingleResultCallback<R> exceptionTransformingCallback(SingleResultCallback<R> callback) {
      return (result, t) -> {
         if (t != null) {
            if (t instanceof MongoException) {
               callback.onResult(null, CommandOperationHelper.transformWriteException((MongoException)t));
            } else {
               callback.onResult(null, t);
            }
         } else {
            callback.onResult(result, (Throwable)null);
         }

      };
   }

   private static <T, R> SingleResultCallback<T> transformingWriteCallback(AsyncOperationHelper.CommandWriteTransformerAsync<T, R> transformer, AsyncConnection connection, SingleResultCallback<R> callback) {
      return (result, t) -> {
         if (t != null) {
            callback.onResult(null, t);
         } else {
            R transformedResult;
            try {
               transformedResult = transformer.apply(Assertions.assertNotNull(result), connection);
            } catch (Throwable throwable) {
               callback.onResult(null, throwable);
               return;
            }

            callback.onResult(transformedResult, (Throwable)null);
         }

      };
   }

   private static <R> SingleResultCallback<R> addingRetryableLabelCallback(SingleResultCallback<R> callback, int maxWireVersion) {
      return (result, t) -> {
         if (t != null) {
            if (t instanceof MongoException) {
               CommandOperationHelper.addRetryableWriteErrorLabel((MongoException)t, maxWireVersion);
            }

            callback.onResult(null, t);
         } else {
            callback.onResult(result, (Throwable)null);
         }

      };
   }

   private static <T, R> SingleResultCallback<T> transformingReadCallback(AsyncOperationHelper.CommandReadTransformerAsync<T, R> transformer, AsyncConnectionSource source, AsyncConnection connection, SingleResultCallback<R> callback) {
      return (result, t) -> {
         if (t != null) {
            callback.onResult(null, t);
         } else {
            R transformedResult;
            try {
               transformedResult = transformer.apply(Assertions.assertNotNull(result), source, connection);
            } catch (Throwable throwable) {
               callback.onResult(null, throwable);
               return;
            }

            callback.onResult(transformedResult, (Throwable)null);
         }

      };
   }

   private AsyncOperationHelper() {
   }

   private static class AsyncCallableWithSourceCallback implements SingleResultCallback<AsyncConnectionSource> {
      private final AsyncOperationHelper.AsyncCallableWithSource callable;

      AsyncCallableWithSourceCallback(AsyncOperationHelper.AsyncCallableWithSource callable) {
         this.callable = callable;
      }

      public void onResult(@Nullable AsyncConnectionSource source, @Nullable Throwable t) {
         if (t != null) {
            this.callable.call((AsyncConnectionSource)null, t);
         } else {
            AsyncOperationHelper.withAsyncConnectionSource((AsyncConnectionSource)Assertions.assertNotNull(source), this.callable);
         }

      }
   }

   interface AsyncCallableWithSource {
      void call(@Nullable AsyncConnectionSource var1, @Nullable Throwable var2);
   }

   private static class AsyncCallableWithConnectionCallback implements SingleResultCallback<AsyncConnectionSource> {
      private final AsyncOperationHelper.AsyncCallableWithConnection callable;

      AsyncCallableWithConnectionCallback(AsyncOperationHelper.AsyncCallableWithConnection callable) {
         this.callable = callable;
      }

      public void onResult(@Nullable AsyncConnectionSource source, @Nullable Throwable t) {
         if (t != null) {
            this.callable.call((AsyncConnection)null, t);
         } else {
            AsyncOperationHelper.withAsyncConnectionSourceCallableConnection((AsyncConnectionSource)Assertions.assertNotNull(source), this.callable);
         }

      }
   }

   interface AsyncCallableWithConnection {
      void call(@Nullable AsyncConnection var1, @Nullable Throwable var2);
   }

   interface CommandReadTransformerAsync<T, R> {
      @Nullable
      R apply(T var1, AsyncConnectionSource var2, AsyncConnection var3);
   }

   interface CommandWriteTransformerAsync<T, R> {
      @Nullable
      R apply(T var1, AsyncConnection var2);
   }

   private static class ReferenceCountedReleasingWrappedCallback<T> implements SingleResultCallback<T> {
      private final SingleResultCallback<T> wrapped;
      private final List<? extends ReferenceCounted> referenceCounted;

      ReferenceCountedReleasingWrappedCallback(SingleResultCallback<T> wrapped, List<? extends ReferenceCounted> referenceCounted) {
         this.wrapped = wrapped;
         this.referenceCounted = (List)Assertions.notNull("referenceCounted", referenceCounted);
      }

      public void onResult(@Nullable T result, @Nullable Throwable t) {
         Iterator<? extends ReferenceCounted> iterator = this.referenceCounted.iterator();

         while(iterator.hasNext()) {
            ReferenceCounted cur = iterator.next();
            if (cur != null) {
               cur.release();
            }
         }

         this.wrapped.onResult(result, t);
      }
   }
}
