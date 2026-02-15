package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import dev.artixdev.libs.com.mongodb.Function;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.MongoCursor;
import dev.artixdev.libs.com.mongodb.client.MongoIterable;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public abstract class MongoIterableImpl<TResult> implements MongoIterable<TResult> {
   private final ClientSession clientSession;
   private final ReadConcern readConcern;
   private final OperationExecutor executor;
   private final ReadPreference readPreference;
   private final boolean retryReads;
   private Integer batchSize;

   public MongoIterableImpl(@Nullable ClientSession clientSession, OperationExecutor executor, ReadConcern readConcern, ReadPreference readPreference, boolean retryReads) {
      this.clientSession = clientSession;
      this.executor = (OperationExecutor)Assertions.notNull("executor", executor);
      this.readConcern = (ReadConcern)Assertions.notNull("readConcern", readConcern);
      this.readPreference = (ReadPreference)Assertions.notNull("readPreference", readPreference);
      this.retryReads = (Boolean)Assertions.notNull("retryReads", retryReads);
   }

   public abstract ReadOperation<BatchCursor<TResult>> asReadOperation();

   @Nullable
   ClientSession getClientSession() {
      return this.clientSession;
   }

   OperationExecutor getExecutor() {
      return this.executor;
   }

   ReadPreference getReadPreference() {
      return this.readPreference;
   }

   protected ReadConcern getReadConcern() {
      return this.readConcern;
   }

   protected boolean getRetryReads() {
      return this.retryReads;
   }

   @Nullable
   public Integer getBatchSize() {
      return this.batchSize;
   }

   public MongoIterable<TResult> batchSize(int batchSize) {
      this.batchSize = batchSize;
      return this;
   }

   public MongoCursor<TResult> iterator() {
      return new MongoBatchCursorAdapter(this.execute());
   }

   public MongoCursor<TResult> cursor() {
      return this.iterator();
   }

   @Nullable
   public TResult first() {
      MongoCursor cursor = this.iterator();

      TResult var2;
      label43: {
         try {
            if (!cursor.hasNext()) {
               var2 = null;
               break label43;
            }

            var2 = (TResult) cursor.next();
         } catch (Throwable e) {
            if (cursor != null) {
               try {
                  cursor.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (cursor != null) {
            cursor.close();
         }

         return var2;
      }

      if (cursor != null) {
         cursor.close();
      }

      return var2;
   }

   public <U> MongoIterable<U> map(Function<TResult, U> mapper) {
      return new MappingIterable(this, mapper);
   }

   public void forEach(Consumer<? super TResult> action) {
      MongoCursor cursor = this.iterator();

      try {
         while(cursor.hasNext()) {
            action.accept((TResult) cursor.next());
         }
      } catch (Throwable e) {
         if (cursor != null) {
            try {
               cursor.close();
            } catch (Throwable suppressed) {
               e.addSuppressed(suppressed);
            }
         }

         throw e;
      }

      if (cursor != null) {
         cursor.close();
      }

   }

   public <A extends Collection<? super TResult>> A into(A target) {
      Objects.requireNonNull(target);
      this.forEach(target::add);
      return target;
   }

   private BatchCursor<TResult> execute() {
      return (BatchCursor)this.executor.execute(this.asReadOperation(), this.readPreference, this.readConcern, this.clientSession);
   }
}
