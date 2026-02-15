package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoConnectionPoolClearedException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoNodeIsRecoveringException;
import dev.artixdev.libs.com.mongodb.MongoNotPrimaryException;
import dev.artixdev.libs.com.mongodb.MongoSecurityException;
import dev.artixdev.libs.com.mongodb.MongoServerException;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.async.function.RetryState;
import dev.artixdev.libs.com.mongodb.internal.connection.OperationContext;
import dev.artixdev.libs.com.mongodb.internal.operation.retry.AttachmentKeys;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

final class CommandOperationHelper {
   private static final List<Integer> RETRYABLE_ERROR_CODES = Arrays.asList(6, 7, 89, 91, 189, 262, 9001, 13436, 13435, 11602, 11600, 10107);
   static final String RETRYABLE_WRITE_ERROR_LABEL = "RetryableWriteError";
   private static final String NO_WRITES_PERFORMED_ERROR_LABEL = "NoWritesPerformed";

   static Throwable chooseRetryableReadException(@Nullable Throwable previouslyChosenException, Throwable mostRecentAttemptException) {
      Assertions.assertFalse(mostRecentAttemptException instanceof OperationHelper.ResourceSupplierInternalException);
      return previouslyChosenException != null && !(mostRecentAttemptException instanceof MongoSocketException) && !(mostRecentAttemptException instanceof MongoServerException) ? previouslyChosenException : mostRecentAttemptException;
   }

   static Throwable chooseRetryableWriteException(@Nullable Throwable previouslyChosenException, Throwable mostRecentAttemptException) {
      if (previouslyChosenException == null) {
         return mostRecentAttemptException instanceof OperationHelper.ResourceSupplierInternalException ? mostRecentAttemptException.getCause() : mostRecentAttemptException;
      } else {
         return !(mostRecentAttemptException instanceof OperationHelper.ResourceSupplierInternalException) && (!(mostRecentAttemptException instanceof MongoException) || !((MongoException)mostRecentAttemptException).hasErrorLabel("NoWritesPerformed")) ? mostRecentAttemptException : previouslyChosenException;
      }
   }

   static RetryState initialRetryState(boolean retry) {
      return new RetryState(retry ? 1 : 0);
   }

   static boolean isRetryableException(Throwable t) {
      if (!(t instanceof MongoException)) {
         return false;
      } else {
         return !(t instanceof MongoSocketException) && !(t instanceof MongoNotPrimaryException) && !(t instanceof MongoNodeIsRecoveringException) && !(t instanceof MongoConnectionPoolClearedException) ? RETRYABLE_ERROR_CODES.contains(((MongoException)t).getCode()) : true;
      }
   }

   static void rethrowIfNotNamespaceError(MongoCommandException e) {
      rethrowIfNotNamespaceError(e, (Object)null);
   }

   @Nullable
   static <T> T rethrowIfNotNamespaceError(MongoCommandException e, @Nullable T defaultValue) {
      if (!isNamespaceError(e)) {
         throw e;
      } else {
         return defaultValue;
      }
   }

   static boolean isNamespaceError(Throwable t) {
      if (!(t instanceof MongoCommandException)) {
         return false;
      } else {
         MongoCommandException e = (MongoCommandException)t;
         return e.getErrorMessage().contains("ns not found") || e.getErrorCode() == 26;
      }
   }

   static boolean shouldAttemptToRetryRead(RetryState retryState, Throwable attemptFailure) {
      Assertions.assertFalse(attemptFailure instanceof OperationHelper.ResourceSupplierInternalException);
      boolean decision = isRetryableException(attemptFailure) || attemptFailure instanceof MongoSecurityException && attemptFailure.getCause() != null && isRetryableException(attemptFailure.getCause());
      if (!decision) {
         logUnableToRetry(retryState.attachment(AttachmentKeys.commandDescriptionSupplier()).orElse(null), attemptFailure);
      }

      return decision;
   }

   static boolean shouldAttemptToRetryWrite(RetryState retryState, Throwable attemptFailure) {
      Throwable failure = attemptFailure instanceof OperationHelper.ResourceSupplierInternalException ? attemptFailure.getCause() : attemptFailure;
      boolean decision = false;
      MongoException exceptionRetryableRegardlessOfCommand = null;
      if (failure instanceof MongoConnectionPoolClearedException || failure instanceof MongoSecurityException && failure.getCause() != null && isRetryableException(failure.getCause())) {
         decision = true;
         exceptionRetryableRegardlessOfCommand = (MongoException)failure;
      }

      if ((Boolean)retryState.attachment(AttachmentKeys.retryableCommandFlag()).orElse(false)) {
         if (exceptionRetryableRegardlessOfCommand != null) {
            exceptionRetryableRegardlessOfCommand.addLabel("RetryableWriteError");
         } else if (decideRetryableAndAddRetryableWriteErrorLabel(failure, retryState.attachment(AttachmentKeys.maxWireVersion()).orElse(null))) {
            decision = true;
         } else {
            logUnableToRetry(retryState.attachment(AttachmentKeys.commandDescriptionSupplier()).orElse(null), failure);
         }
      }

      return decision;
   }

   static boolean isRetryWritesEnabled(@Nullable BsonDocument command) {
      return command != null && (command.containsKey("txnNumber") || command.getFirstKey().equals("commitTransaction") || command.getFirstKey().equals("abortTransaction"));
   }

   private static boolean decideRetryableAndAddRetryableWriteErrorLabel(Throwable t, @Nullable Integer maxWireVersion) {
      if (!(t instanceof MongoException)) {
         return false;
      } else {
         MongoException exception = (MongoException)t;
         if (maxWireVersion != null) {
            addRetryableWriteErrorLabel(exception, maxWireVersion);
         }

         return exception.hasErrorLabel("RetryableWriteError");
      }
   }

   static void addRetryableWriteErrorLabel(MongoException exception, int maxWireVersion) {
      if (maxWireVersion >= 9 && exception instanceof MongoSocketException) {
         exception.addLabel("RetryableWriteError");
      } else if (maxWireVersion < 9 && isRetryableException(exception)) {
         exception.addLabel("RetryableWriteError");
      }

   }

   static void logRetryExecute(RetryState retryState, OperationContext operationContext) {
      if (OperationHelper.LOGGER.isDebugEnabled() && !retryState.isFirstAttempt()) {
         String commandDescription = retryState.attachment(AttachmentKeys.commandDescriptionSupplier()).map(Supplier::get).orElse(null);
         Throwable exception = (Throwable)retryState.exception().orElseThrow(Assertions::fail);
         int oneBasedAttempt = retryState.attempt() + 1;
         long operationId = operationContext.getId();
         OperationHelper.LOGGER.debug(commandDescription == null ? String.format("Retrying the operation with operation ID %s due to the error \"%s\". Attempt number: #%d", operationId, exception, oneBasedAttempt) : String.format("Retrying the operation '%s' with operation ID %s due to the error \"%s\". Attempt number: #%d", commandDescription, operationId, exception, oneBasedAttempt));
      }

   }

   private static void logUnableToRetry(@Nullable Supplier<String> commandDescriptionSupplier, Throwable originalError) {
      if (OperationHelper.LOGGER.isDebugEnabled()) {
         String commandDescription = commandDescriptionSupplier == null ? null : (String)commandDescriptionSupplier.get();
         OperationHelper.LOGGER.debug(commandDescription == null ? String.format("Unable to retry an operation due to the error \"%s\"", originalError) : String.format("Unable to retry the operation %s due to the error \"%s\"", commandDescription, originalError));
      }

   }

   static MongoException transformWriteException(MongoException exception) {
      if (exception.getCode() == 20 && exception.getMessage().contains("Transaction numbers")) {
         MongoException clientException = new MongoClientException("This MongoDB deployment does not support retryable writes. Please add retryWrites=false to your connection string.", exception);
         Iterator var2 = exception.getErrorLabels().iterator();

         while(var2.hasNext()) {
            String errorLabel = (String)var2.next();
            clientException.addLabel(errorLabel);
         }

         return clientException;
      } else {
         return exception;
      }
   }

   private CommandOperationHelper() {
   }

   interface CommandCreator {
      BsonDocument create(ServerDescription var1, ConnectionDescription var2);
   }
}
