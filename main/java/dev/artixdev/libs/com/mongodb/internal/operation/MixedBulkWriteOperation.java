package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteResult;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.function.AsyncCallbackLoop;
import dev.artixdev.libs.com.mongodb.internal.async.function.AsyncCallbackRunnable;
import dev.artixdev.libs.com.mongodb.internal.async.function.AsyncCallbackSupplier;
import dev.artixdev.libs.com.mongodb.internal.async.function.LoopState;
import dev.artixdev.libs.com.mongodb.internal.async.function.RetryState;
import dev.artixdev.libs.com.mongodb.internal.async.function.RetryingAsyncCallbackSupplier;
import dev.artixdev.libs.com.mongodb.internal.async.function.RetryingSyncSupplier;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.internal.bulk.WriteRequest;
import dev.artixdev.libs.com.mongodb.internal.connection.AsyncConnection;
import dev.artixdev.libs.com.mongodb.internal.connection.Connection;
import dev.artixdev.libs.com.mongodb.internal.connection.MongoWriteConcernWithResponseException;
import dev.artixdev.libs.com.mongodb.internal.connection.OperationContext;
import dev.artixdev.libs.com.mongodb.internal.connection.ProtocolHelper;
import dev.artixdev.libs.com.mongodb.internal.operation.retry.AttachmentKeys;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.FieldNameValidator;

public class MixedBulkWriteOperation implements AsyncWriteOperation<BulkWriteResult>, WriteOperation<BulkWriteResult> {
   private static final FieldNameValidator NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
   private final MongoNamespace namespace;
   private final List<? extends WriteRequest> writeRequests;
   private final boolean ordered;
   private final boolean retryWrites;
   private final WriteConcern writeConcern;
   private Boolean bypassDocumentValidation;
   private BsonValue comment;
   private BsonDocument variables;

   public MixedBulkWriteOperation(MongoNamespace namespace, List<? extends WriteRequest> writeRequests, boolean ordered, WriteConcern writeConcern, boolean retryWrites) {
      this.ordered = ordered;
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.writeRequests = (List)Assertions.notNull("writes", writeRequests);
      this.writeConcern = (WriteConcern)Assertions.notNull("writeConcern", writeConcern);
      this.retryWrites = retryWrites;
      Assertions.isTrueArgument("writes is not an empty list", !writeRequests.isEmpty());
   }

   public MongoNamespace getNamespace() {
      return this.namespace;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public boolean isOrdered() {
      return this.ordered;
   }

   public List<? extends WriteRequest> getWriteRequests() {
      return this.writeRequests;
   }

   public Boolean getBypassDocumentValidation() {
      return this.bypassDocumentValidation;
   }

   public MixedBulkWriteOperation bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   public BsonValue getComment() {
      return this.comment;
   }

   public MixedBulkWriteOperation comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public MixedBulkWriteOperation let(@Nullable BsonDocument variables) {
      this.variables = variables;
      return this;
   }

   public Boolean getRetryWrites() {
      return this.retryWrites;
   }

   private <R> Supplier<R> decorateWriteWithRetries(RetryState retryState, OperationContext operationContext, Supplier<R> writeFunction) {
      return new RetryingSyncSupplier<R>(retryState, CommandOperationHelper::chooseRetryableWriteException, this::shouldAttemptToRetryWrite, () -> {
         CommandOperationHelper.logRetryExecute(retryState, operationContext);
         return writeFunction.get();
      });
   }

   private <R> AsyncCallbackSupplier<R> decorateWriteWithRetries(RetryState retryState, OperationContext operationContext, AsyncCallbackSupplier<R> writeFunction) {
      return new RetryingAsyncCallbackSupplier<R>(retryState, CommandOperationHelper::chooseRetryableWriteException, this::shouldAttemptToRetryWrite, (callback) -> {
         CommandOperationHelper.logRetryExecute(retryState, operationContext);
         writeFunction.get(callback);
      });
   }

   private boolean shouldAttemptToRetryWrite(RetryState retryState, Throwable attemptFailure) {
      MixedBulkWriteOperation.BulkWriteTracker bulkWriteTracker = (MixedBulkWriteOperation.BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail);
      if (bulkWriteTracker.lastAttempt()) {
         return false;
      } else {
         boolean decision = CommandOperationHelper.shouldAttemptToRetryWrite(retryState, attemptFailure);
         if (decision) {
            bulkWriteTracker.advance();
         }

         return decision;
      }
   }

   public BulkWriteResult execute(WriteBinding binding) {
      RetryState retryState = new RetryState();
      MixedBulkWriteOperation.BulkWriteTracker.attachNew(retryState, this.retryWrites);
      Supplier retryingBulkWrite = this.decorateWriteWithRetries(retryState, binding.getOperationContext(), () -> {
         Objects.requireNonNull(binding);
         return (BulkWriteResult)SyncOperationHelper.withSourceAndConnection(binding::getWriteConnectionSource, true, (source, connection) -> {
            ConnectionDescription connectionDescription = connection.getDescription();
            retryState.attach(AttachmentKeys.maxWireVersion(), connectionDescription.getMaxWireVersion(), true);
            SessionContext sessionContext = binding.getSessionContext();
            WriteConcern writeConcern = this.getAppliedWriteConcern(sessionContext);
            if (!OperationHelper.isRetryableWrite(this.retryWrites, this.getAppliedWriteConcern(sessionContext), connectionDescription, sessionContext)) {
               this.handleMongoWriteConcernWithResponseException(retryState, true);
            }

            OperationHelper.validateWriteRequests(connectionDescription, this.bypassDocumentValidation, this.writeRequests, writeConcern);
            if (!((MixedBulkWriteOperation.BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail)).batch().isPresent()) {
               MixedBulkWriteOperation.BulkWriteTracker.attachNew(retryState, BulkWriteBatch.createBulkWriteBatch(this.namespace, connectionDescription, this.ordered, writeConcern, this.bypassDocumentValidation, this.retryWrites, this.writeRequests, sessionContext, this.comment, this.variables));
            }

            return this.executeBulkWriteBatch(retryState, binding, connection);
         });
      });

      try {
         return (BulkWriteResult)retryingBulkWrite.get();
      } catch (MongoException e) {
         throw CommandOperationHelper.transformWriteException(e);
      }
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<BulkWriteResult> callback) {
      RetryState retryState = new RetryState();
      MixedBulkWriteOperation.BulkWriteTracker.attachNew(retryState, this.retryWrites);
      binding.retain();
      AsyncCallbackSupplier<BulkWriteResult> var10000 = this.decorateWriteWithRetries(retryState, binding.getOperationContext(), (funcCallback) -> {
         Objects.requireNonNull(binding);
         AsyncOperationHelper.withAsyncSourceAndConnection(binding::getWriteConnectionSource, true, funcCallback, (source, connection, releasingCallback) -> {
            ConnectionDescription connectionDescription = connection.getDescription();
            retryState.attach(AttachmentKeys.maxWireVersion(), connectionDescription.getMaxWireVersion(), true);
            SessionContext sessionContext = binding.getSessionContext();
            WriteConcern writeConcern = this.getAppliedWriteConcern(sessionContext);
            if (OperationHelper.isRetryableWrite(this.retryWrites, this.getAppliedWriteConcern(sessionContext), connectionDescription, sessionContext) || !this.handleMongoWriteConcernWithResponseExceptionAsync(retryState, releasingCallback)) {
               if (!OperationHelper.validateWriteRequestsAndCompleteIfInvalid(connectionDescription, this.bypassDocumentValidation, this.writeRequests, writeConcern, releasingCallback)) {
                  try {
                     if (!((MixedBulkWriteOperation.BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail)).batch().isPresent()) {
                        MixedBulkWriteOperation.BulkWriteTracker.attachNew(retryState, BulkWriteBatch.createBulkWriteBatch(this.namespace, connectionDescription, this.ordered, writeConcern, this.bypassDocumentValidation, this.retryWrites, this.writeRequests, sessionContext, this.comment, this.variables));
                     }
                  } catch (Throwable e) {
                     releasingCallback.onResult(null, e);
                     return;
                  }

                  this.executeBulkWriteBatchAsync(retryState, binding, connection, releasingCallback);
               }
            }
         });
      });
      Objects.requireNonNull(binding);
      AsyncCallbackSupplier<BulkWriteResult> retryingBulkWrite = var10000.whenComplete(binding::release);
      retryingBulkWrite.get(AsyncOperationHelper.exceptionTransformingCallback(ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER)));
   }

   private BulkWriteResult executeBulkWriteBatch(RetryState retryState, WriteBinding binding, Connection connection) {
      MixedBulkWriteOperation.BulkWriteTracker currentBulkWriteTracker = (MixedBulkWriteOperation.BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail);
      BulkWriteBatch currentBatch = (BulkWriteBatch)currentBulkWriteTracker.batch().orElseThrow(Assertions::fail);
      int maxWireVersion = connection.getDescription().getMaxWireVersion();

      while(currentBatch.shouldProcessBatch()) {
         try {
            BsonDocument result = this.executeCommand(connection, currentBatch, binding);
            if (currentBatch.getRetryWrites() && !binding.getSessionContext().hasActiveTransaction()) {
               MongoException writeConcernBasedError = ProtocolHelper.createSpecialException(result, connection.getDescription().getServerAddress(), "errMsg");
               if (writeConcernBasedError != null) {
                  if (currentBulkWriteTracker.lastAttempt()) {
                     CommandOperationHelper.addRetryableWriteErrorLabel(writeConcernBasedError, maxWireVersion);
                     this.addErrorLabelsToWriteConcern(result.getDocument("writeConcernError"), writeConcernBasedError.getErrorLabels());
                  } else if (CommandOperationHelper.shouldAttemptToRetryWrite(retryState, writeConcernBasedError)) {
                     throw new MongoWriteConcernWithResponseException(writeConcernBasedError, result);
                  }
               }
            }

            currentBatch.addResult(result);
            currentBulkWriteTracker = MixedBulkWriteOperation.BulkWriteTracker.attachNext(retryState, currentBatch);
            currentBatch = (BulkWriteBatch)currentBulkWriteTracker.batch().orElseThrow(Assertions::fail);
         } catch (MongoException e) {
            if (!retryState.isFirstAttempt() && !(e instanceof MongoWriteConcernWithResponseException)) {
               CommandOperationHelper.addRetryableWriteErrorLabel(e, maxWireVersion);
            }

            this.handleMongoWriteConcernWithResponseException(retryState, false);
            throw e;
         }
      }

      try {
         return currentBatch.getResult();
      } catch (MongoException e) {
         retryState.markAsLastAttempt();
         throw e;
      }
   }

   private void executeBulkWriteBatchAsync(RetryState retryState, AsyncWriteBinding binding, AsyncConnection connection, SingleResultCallback<BulkWriteResult> callback) {
      LoopState loopState = new LoopState();
      AsyncCallbackRunnable loop = new AsyncCallbackLoop(loopState, (iterationCallback) -> {
         MixedBulkWriteOperation.BulkWriteTracker currentBulkWriteTracker = (MixedBulkWriteOperation.BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail);
         loopState.attach(AttachmentKeys.bulkWriteTracker(), currentBulkWriteTracker, true);
         BulkWriteBatch currentBatch = (BulkWriteBatch)currentBulkWriteTracker.batch().orElseThrow(Assertions::fail);
         int maxWireVersion = connection.getDescription().getMaxWireVersion();
         if (!loopState.breakAndCompleteIf(() -> {
            return !currentBatch.shouldProcessBatch();
         }, iterationCallback)) {
            this.executeCommandAsync(binding, connection, currentBatch, (result, t) -> {
               MongoException writeConcernBasedError;
               if (t == null) {
                  if (currentBatch.getRetryWrites() && !binding.getSessionContext().hasActiveTransaction()) {
                     writeConcernBasedError = ProtocolHelper.createSpecialException(result, connection.getDescription().getServerAddress(), "errMsg");
                     if (writeConcernBasedError != null) {
                        if (currentBulkWriteTracker.lastAttempt()) {
                           CommandOperationHelper.addRetryableWriteErrorLabel(writeConcernBasedError, maxWireVersion);
                           this.addErrorLabelsToWriteConcern(result.getDocument("writeConcernError"), writeConcernBasedError.getErrorLabels());
                        } else if (CommandOperationHelper.shouldAttemptToRetryWrite(retryState, writeConcernBasedError)) {
                           iterationCallback.onResult(null, new MongoWriteConcernWithResponseException(writeConcernBasedError, result));
                           return;
                        }
                     }
                  }

                  currentBatch.addResult(result);
                  MixedBulkWriteOperation.BulkWriteTracker.attachNext(retryState, currentBatch);
                  iterationCallback.onResult(null, (Throwable)null);
               } else {
                  if (t instanceof MongoException) {
                     writeConcernBasedError = (MongoException)t;
                     if (!retryState.isFirstAttempt() && !(writeConcernBasedError instanceof MongoWriteConcernWithResponseException)) {
                        CommandOperationHelper.addRetryableWriteErrorLabel(writeConcernBasedError, maxWireVersion);
                     }

                     if (this.handleMongoWriteConcernWithResponseExceptionAsync(retryState, (SingleResultCallback)null)) {
                        return;
                     }
                  }

                  iterationCallback.onResult(null, t);
               }

            });
         }
      });
      loop.run((voidResult, t) -> {
         if (t != null) {
            callback.onResult(null, t);
         } else {
            BulkWriteResult result;
            try {
               result = ((BulkWriteBatch)loopState.attachment(AttachmentKeys.bulkWriteTracker()).flatMap(MixedBulkWriteOperation.BulkWriteTracker::batch).orElseThrow(Assertions::fail)).getResult();
            } catch (Throwable e) {
               if (e instanceof MongoException) {
                  retryState.markAsLastAttempt();
               }

               callback.onResult(null, e);
               return;
            }

            callback.onResult(result, (Throwable)null);
         }

      });
   }

   private void handleMongoWriteConcernWithResponseException(RetryState retryState, boolean breakAndThrowIfDifferent) {
      if (!retryState.isFirstAttempt()) {
         RuntimeException prospectiveFailedResult = (RuntimeException)retryState.exception().orElse((Throwable)null);
         boolean prospectiveResultIsWriteConcernException = prospectiveFailedResult instanceof MongoWriteConcernWithResponseException;
         retryState.breakAndThrowIfRetryAnd(() -> {
            return breakAndThrowIfDifferent && !prospectiveResultIsWriteConcernException;
         });
         if (prospectiveResultIsWriteConcernException) {
            ((MixedBulkWriteOperation.BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail)).batch().ifPresent((bulkWriteBatch) -> {
               bulkWriteBatch.addResult((BsonDocument)((MongoWriteConcernWithResponseException)prospectiveFailedResult).getResponse());
               MixedBulkWriteOperation.BulkWriteTracker.attachNext(retryState, bulkWriteBatch);
            });
         }
      }

   }

   private boolean handleMongoWriteConcernWithResponseExceptionAsync(RetryState retryState, @Nullable SingleResultCallback<BulkWriteResult> callback) {
      if (!retryState.isFirstAttempt()) {
         RuntimeException prospectiveFailedResult = (RuntimeException)retryState.exception().orElse((Throwable)null);
         boolean prospectiveResultIsWriteConcernException = prospectiveFailedResult instanceof MongoWriteConcernWithResponseException;
         if (callback != null && retryState.breakAndCompleteIfRetryAnd(() -> {
            return !prospectiveResultIsWriteConcernException;
         }, callback)) {
            return true;
         }

         if (prospectiveResultIsWriteConcernException) {
            ((MixedBulkWriteOperation.BulkWriteTracker)retryState.attachment(AttachmentKeys.bulkWriteTracker()).orElseThrow(Assertions::fail)).batch().ifPresent((bulkWriteBatch) -> {
               bulkWriteBatch.addResult((BsonDocument)((MongoWriteConcernWithResponseException)prospectiveFailedResult).getResponse());
               MixedBulkWriteOperation.BulkWriteTracker.attachNext(retryState, bulkWriteBatch);
            });
         }
      }

      return false;
   }

   @Nullable
   private BsonDocument executeCommand(Connection connection, BulkWriteBatch batch, WriteBinding binding) {
      return (BsonDocument)connection.command(this.namespace.getDatabaseName(), batch.getCommand(), NO_OP_FIELD_NAME_VALIDATOR, (ReadPreference)null, batch.getDecoder(), binding, this.shouldAcknowledge(batch, binding.getSessionContext()), batch.getPayload(), batch.getFieldNameValidator());
   }

   private void executeCommandAsync(AsyncWriteBinding binding, AsyncConnection connection, BulkWriteBatch batch, SingleResultCallback<BsonDocument> callback) {
      connection.commandAsync(this.namespace.getDatabaseName(), batch.getCommand(), NO_OP_FIELD_NAME_VALIDATOR, (ReadPreference)null, batch.getDecoder(), binding, this.shouldAcknowledge(batch, binding.getSessionContext()), batch.getPayload(), batch.getFieldNameValidator(), callback);
   }

   private WriteConcern getAppliedWriteConcern(SessionContext sessionContext) {
      return sessionContext.hasActiveTransaction() ? WriteConcern.ACKNOWLEDGED : this.writeConcern;
   }

   private boolean shouldAcknowledge(BulkWriteBatch batch, SessionContext sessionContext) {
      return this.ordered ? batch.hasAnotherBatch() || this.getAppliedWriteConcern(sessionContext).isAcknowledged() : this.getAppliedWriteConcern(sessionContext).isAcknowledged();
   }

   private void addErrorLabelsToWriteConcern(BsonDocument result, Set<String> errorLabels) {
      if (!result.containsKey("errorLabels")) {
         result.put((String)"errorLabels", (BsonValue)(new BsonArray((List)errorLabels.stream().map(BsonString::new).collect(Collectors.toList()))));
      }

   }

   public static final class BulkWriteTracker {
      private int attempt = 0;
      private final int attempts;
      @Nullable
      private final BulkWriteBatch batch;

      static void attachNew(RetryState retryState, boolean retry) {
         retryState.attach(AttachmentKeys.bulkWriteTracker(), new MixedBulkWriteOperation.BulkWriteTracker(retry, (BulkWriteBatch)null), false);
      }

      static void attachNew(RetryState retryState, BulkWriteBatch batch) {
         attach(retryState, new MixedBulkWriteOperation.BulkWriteTracker(batch.getRetryWrites(), batch));
      }

      static MixedBulkWriteOperation.BulkWriteTracker attachNext(RetryState retryState, BulkWriteBatch batch) {
         BulkWriteBatch nextBatch = batch.getNextBatch();
         MixedBulkWriteOperation.BulkWriteTracker nextTracker = new MixedBulkWriteOperation.BulkWriteTracker(nextBatch.getRetryWrites(), nextBatch);
         attach(retryState, nextTracker);
         return nextTracker;
      }

      private static void attach(RetryState retryState, MixedBulkWriteOperation.BulkWriteTracker tracker) {
         retryState.attach(AttachmentKeys.bulkWriteTracker(), tracker, false);
         BulkWriteBatch batch = tracker.batch;
         if (batch != null) {
            retryState.attach(AttachmentKeys.retryableCommandFlag(), batch.getRetryWrites(), false).attach(AttachmentKeys.commandDescriptionSupplier(), () -> {
               return batch.getPayload().getPayloadType().toString();
            }, false);
         }

      }

      private BulkWriteTracker(boolean retry, @Nullable BulkWriteBatch batch) {
         this.attempts = retry ? 2 : 1;
         this.batch = batch;
      }

      boolean lastAttempt() {
         return this.attempt == this.attempts - 1;
      }

      void advance() {
         Assertions.assertTrue(!this.lastAttempt());
         ++this.attempt;
      }

      Optional<BulkWriteBatch> batch() {
         return Optional.ofNullable(this.batch);
      }
   }
}
