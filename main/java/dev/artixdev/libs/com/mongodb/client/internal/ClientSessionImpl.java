package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ClientSessionOptions;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoExecutionTimeoutException;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.TransactionOptions;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.TransactionBody;
import dev.artixdev.libs.com.mongodb.internal.operation.AbortTransactionOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.CommitTransactionOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.WriteOperation;
import dev.artixdev.libs.com.mongodb.internal.session.BaseClientSessionImpl;
import dev.artixdev.libs.com.mongodb.internal.session.ServerSessionPool;

final class ClientSessionImpl extends BaseClientSessionImpl implements ClientSession {
   private static final int MAX_RETRY_TIME_LIMIT_MS = 120000;
   private final MongoClientDelegate delegate;
   private ClientSessionImpl.TransactionState transactionState;
   private boolean messageSentInCurrentTransaction;
   private boolean commitInProgress;
   private TransactionOptions transactionOptions;

   ClientSessionImpl(ServerSessionPool serverSessionPool, Object originator, ClientSessionOptions options, MongoClientDelegate delegate) {
      super(serverSessionPool, originator, options);
      this.transactionState = ClientSessionImpl.TransactionState.NONE;
      this.delegate = delegate;
   }

   public boolean hasActiveTransaction() {
      return this.transactionState == ClientSessionImpl.TransactionState.IN || this.transactionState == ClientSessionImpl.TransactionState.COMMITTED && this.commitInProgress;
   }

   public boolean notifyMessageSent() {
      if (this.hasActiveTransaction()) {
         boolean firstMessageInCurrentTransaction = !this.messageSentInCurrentTransaction;
         this.messageSentInCurrentTransaction = true;
         return firstMessageInCurrentTransaction;
      } else {
         if (this.transactionState == ClientSessionImpl.TransactionState.COMMITTED || this.transactionState == ClientSessionImpl.TransactionState.ABORTED) {
            this.cleanupTransaction(ClientSessionImpl.TransactionState.NONE);
         }

         return false;
      }
   }

   public void notifyOperationInitiated(Object operation) {
      Assertions.assertTrue(operation instanceof ReadOperation || operation instanceof WriteOperation);
      if (!this.hasActiveTransaction() && !(operation instanceof CommitTransactionOperation)) {
         Assertions.assertTrue(this.getPinnedServerAddress() == null || this.transactionState != ClientSessionImpl.TransactionState.ABORTED && this.transactionState != ClientSessionImpl.TransactionState.NONE);
         this.clearTransactionContext();
      }

   }

   public TransactionOptions getTransactionOptions() {
      Assertions.isTrue("in transaction", this.transactionState == ClientSessionImpl.TransactionState.IN || this.transactionState == ClientSessionImpl.TransactionState.COMMITTED);
      return this.transactionOptions;
   }

   public void startTransaction() {
      this.startTransaction(TransactionOptions.builder().build());
   }

   public void startTransaction(TransactionOptions transactionOptions) {
      Boolean snapshot = this.getOptions().isSnapshot();
      if (snapshot != null && snapshot) {
         throw new IllegalArgumentException("Transactions are not supported in snapshot sessions");
      } else {
         Assertions.notNull("transactionOptions", transactionOptions);
         if (this.transactionState == ClientSessionImpl.TransactionState.IN) {
            throw new IllegalStateException("Transaction already in progress");
         } else {
            if (this.transactionState == ClientSessionImpl.TransactionState.COMMITTED) {
               this.cleanupTransaction(ClientSessionImpl.TransactionState.IN);
            } else {
               this.transactionState = ClientSessionImpl.TransactionState.IN;
            }

            this.getServerSession().advanceTransactionNumber();
            this.transactionOptions = TransactionOptions.merge(transactionOptions, this.getOptions().getDefaultTransactionOptions());
            WriteConcern writeConcern = this.transactionOptions.getWriteConcern();
            if (writeConcern == null) {
               throw new MongoInternalException("Invariant violated.  Transaction options write concern can not be null");
            } else if (!writeConcern.isAcknowledged()) {
               throw new MongoClientException("Transactions do not support unacknowledged write concern");
            } else {
               this.clearTransactionContext();
            }
         }
      }
   }

   public void commitTransaction() {
      if (this.transactionState == ClientSessionImpl.TransactionState.ABORTED) {
         throw new IllegalStateException("Cannot call commitTransaction after calling abortTransaction");
      } else if (this.transactionState == ClientSessionImpl.TransactionState.NONE) {
         throw new IllegalStateException("There is no transaction started");
      } else {
         try {
            if (this.messageSentInCurrentTransaction) {
               ReadConcern readConcern = this.transactionOptions.getReadConcern();
               if (readConcern == null) {
                  throw new MongoInternalException("Invariant violated.  Transaction options read concern can not be null");
               }

               this.commitInProgress = true;
               this.delegate.getOperationExecutor().execute((WriteOperation)(new CommitTransactionOperation((WriteConcern)Assertions.assertNotNull(this.transactionOptions.getWriteConcern()), this.transactionState == ClientSessionImpl.TransactionState.COMMITTED)).recoveryToken(this.getRecoveryToken()).maxCommitTime(this.transactionOptions.getMaxCommitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS), (ReadConcern)readConcern, (ClientSession)this);
            }
         } catch (MongoException e) {
            this.clearTransactionContextOnError(e);
            throw e;
         } finally {
            this.transactionState = ClientSessionImpl.TransactionState.COMMITTED;
            this.commitInProgress = false;
         }

      }
   }

   public void abortTransaction() {
      if (this.transactionState == ClientSessionImpl.TransactionState.ABORTED) {
         throw new IllegalStateException("Cannot call abortTransaction twice");
      } else if (this.transactionState == ClientSessionImpl.TransactionState.COMMITTED) {
         throw new IllegalStateException("Cannot call abortTransaction after calling commitTransaction");
      } else if (this.transactionState == ClientSessionImpl.TransactionState.NONE) {
         throw new IllegalStateException("There is no transaction started");
      } else {
         try {
            if (this.messageSentInCurrentTransaction) {
               ReadConcern readConcern = this.transactionOptions.getReadConcern();
               if (readConcern == null) {
                  throw new MongoInternalException("Invariant violated.  Transaction options read concern can not be null");
               }

               this.delegate.getOperationExecutor().execute((WriteOperation)(new AbortTransactionOperation((WriteConcern)Assertions.assertNotNull(this.transactionOptions.getWriteConcern()))).recoveryToken(this.getRecoveryToken()), (ReadConcern)readConcern, (ClientSession)this);
            }
         } catch (RuntimeException e) {
         } finally {
            this.clearTransactionContext();
            this.cleanupTransaction(ClientSessionImpl.TransactionState.ABORTED);
         }

      }
   }

   private void clearTransactionContextOnError(MongoException e) {
      if (e.hasErrorLabel("TransientTransactionError") || e.hasErrorLabel("UnknownTransactionCommitResult")) {
         this.clearTransactionContext();
      }

   }

   public <T> T withTransaction(TransactionBody<T> transactionBody) {
      return this.withTransaction(transactionBody, TransactionOptions.builder().build());
   }

   public <T> T withTransaction(TransactionBody<T> transactionBody, TransactionOptions options) {
      Assertions.notNull("transactionBody", transactionBody);
      long startTime = ClientSessionClock.INSTANCE.now();

      while(true) {
         T retVal;
         while(true) {
            try {
               this.startTransaction(options);
               retVal = transactionBody.execute();
               break;
            } catch (Throwable e) {
               if (this.transactionState == ClientSessionImpl.TransactionState.IN) {
                  this.abortTransaction();
               }

               if (!(e instanceof MongoException) || !((MongoException)e).hasErrorLabel("TransientTransactionError") || ClientSessionClock.INSTANCE.now() - startTime >= 120000L) {
                  throw e;
               }
            }
         }

         if (this.transactionState != ClientSessionImpl.TransactionState.IN) {
            return retVal;
         }

         while(true) {
            try {
               this.commitTransaction();
               return retVal;
            } catch (MongoException e) {
               this.clearTransactionContextOnError(e);
               if (ClientSessionClock.INSTANCE.now() - startTime < 120000L) {
                  this.applyMajorityWriteConcernToTransactionOptions();
                  if (!(e instanceof MongoExecutionTimeoutException) && e.hasErrorLabel("UnknownTransactionCommitResult")) {
                     continue;
                  }

                  if (e.hasErrorLabel("TransientTransactionError")) {
                     break;
                  }
               }

               throw e;
            }
         }
      }
   }

   private void applyMajorityWriteConcernToTransactionOptions() {
      if (this.transactionOptions != null) {
         WriteConcern writeConcern = this.transactionOptions.getWriteConcern();
         if (writeConcern != null) {
            this.transactionOptions = TransactionOptions.merge(TransactionOptions.builder().writeConcern(writeConcern.withW("majority")).build(), this.transactionOptions);
         } else {
            this.transactionOptions = TransactionOptions.merge(TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build(), this.transactionOptions);
         }
      } else {
         this.transactionOptions = TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build();
      }

   }

   public void close() {
      try {
         if (this.transactionState == ClientSessionImpl.TransactionState.IN) {
            this.abortTransaction();
         }
      } finally {
         this.clearTransactionContext();
         super.close();
      }

   }

   private void cleanupTransaction(ClientSessionImpl.TransactionState nextState) {
      this.messageSentInCurrentTransaction = false;
      this.transactionOptions = null;
      this.transactionState = nextState;
   }

   private static enum TransactionState {
      NONE,
      IN,
      COMMITTED,
      ABORTED;

      // $FF: synthetic method
      private static ClientSessionImpl.TransactionState[] $values() {
         return new ClientSessionImpl.TransactionState[]{NONE, IN, COMMITTED, ABORTED};
      }
   }
}
