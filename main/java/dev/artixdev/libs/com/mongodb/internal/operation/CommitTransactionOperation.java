package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.Function;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoExecutionTimeoutException;
import dev.artixdev.libs.com.mongodb.MongoNodeIsRecoveringException;
import dev.artixdev.libs.com.mongodb.MongoNotPrimaryException;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.MongoTimeoutException;
import dev.artixdev.libs.com.mongodb.MongoWriteConcernException;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonValue;

public class CommitTransactionOperation extends TransactionOperation {
   private final boolean alreadyCommitted;
   private BsonDocument recoveryToken;
   private Long maxCommitTimeMS;
   private static final List<Integer> NON_RETRYABLE_WRITE_CONCERN_ERROR_CODES = Arrays.asList(79, 100);

   public CommitTransactionOperation(WriteConcern writeConcern) {
      this(writeConcern, false);
   }

   public CommitTransactionOperation(WriteConcern writeConcern, boolean alreadyCommitted) {
      super(writeConcern);
      this.alreadyCommitted = alreadyCommitted;
   }

   public CommitTransactionOperation recoveryToken(@Nullable BsonDocument recoveryToken) {
      this.recoveryToken = recoveryToken;
      return this;
   }

   public CommitTransactionOperation maxCommitTime(@Nullable Long maxCommitTime, TimeUnit timeUnit) {
      if (maxCommitTime == null) {
         this.maxCommitTimeMS = null;
      } else {
         Assertions.notNull("timeUnit", timeUnit);
         Assertions.isTrueArgument("maxCommitTime > 0", maxCommitTime > 0L);
         this.maxCommitTimeMS = TimeUnit.MILLISECONDS.convert(maxCommitTime, timeUnit);
      }

      return this;
   }

   @Nullable
   public Long getMaxCommitTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return this.maxCommitTimeMS == null ? null : timeUnit.convert(this.maxCommitTimeMS, TimeUnit.MILLISECONDS);
   }

   public Void execute(WriteBinding binding) {
      try {
         return super.execute(binding);
      } catch (MongoException e) {
         this.addErrorLabels(e);
         throw e;
      }
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
      super.executeAsync(binding, (result, t) -> {
         if (t instanceof MongoException) {
            this.addErrorLabels((MongoException)t);
         }

         callback.onResult(result, t);
      });
   }

   private void addErrorLabels(MongoException e) {
      if (shouldAddUnknownTransactionCommitResultLabel(e)) {
         e.addLabel("UnknownTransactionCommitResult");
      }

   }

   private static boolean shouldAddUnknownTransactionCommitResultLabel(MongoException e) {
      if (!(e instanceof MongoSocketException) && !(e instanceof MongoTimeoutException) && !(e instanceof MongoNotPrimaryException) && !(e instanceof MongoNodeIsRecoveringException) && !(e instanceof MongoExecutionTimeoutException)) {
         if (e.hasErrorLabel("RetryableWriteError")) {
            return true;
         } else if (e instanceof MongoWriteConcernException) {
            return !NON_RETRYABLE_WRITE_CONCERN_ERROR_CODES.contains(e.getCode());
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   protected String getCommandName() {
      return "commitTransaction";
   }

   CommandOperationHelper.CommandCreator getCommandCreator() {
      CommandOperationHelper.CommandCreator creator = (serverDescription, connectionDescription) -> {
         BsonDocument command = access$001(this).create(serverDescription, connectionDescription);
         if (this.maxCommitTimeMS != null) {
            command.append("maxTimeMS", (BsonValue)(this.maxCommitTimeMS > 2147483647L ? new BsonInt64(this.maxCommitTimeMS) : new BsonInt32(this.maxCommitTimeMS.intValue())));
         }

         return command;
      };
      if (this.alreadyCommitted) {
         return (serverDescription, connectionDescription) -> {
            return (BsonDocument)this.getRetryCommandModifier().apply(creator.create(serverDescription, connectionDescription));
         };
      } else {
         return this.recoveryToken != null ? (serverDescription, connectionDescription) -> {
            return creator.create(serverDescription, connectionDescription).append("recoveryToken", this.recoveryToken);
         } : creator;
      }
   }

   protected Function<BsonDocument, BsonDocument> getRetryCommandModifier() {
      return (command) -> {
         WriteConcern retryWriteConcern = this.getWriteConcern().withW("majority");
         if (retryWriteConcern.getWTimeout(TimeUnit.MILLISECONDS) == null) {
            retryWriteConcern = retryWriteConcern.withWTimeout(10000L, TimeUnit.MILLISECONDS);
         }

         command.put((String)"writeConcern", (BsonValue)retryWriteConcern.asDocument());
         if (this.recoveryToken != null) {
            command.put((String)"recoveryToken", (BsonValue)this.recoveryToken);
         }

         return command;
      };
   }

   // $FF: synthetic method
   static CommandOperationHelper.CommandCreator access$001(CommitTransactionOperation x0) {
      return x0.getCommandCreator();
   }
}
