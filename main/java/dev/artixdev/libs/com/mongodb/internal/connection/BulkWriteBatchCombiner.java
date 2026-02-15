package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import dev.artixdev.libs.com.mongodb.MongoBulkWriteException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteError;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteInsert;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteResult;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteUpsert;
import dev.artixdev.libs.com.mongodb.bulk.WriteConcernError;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class BulkWriteBatchCombiner {
   private final ServerAddress serverAddress;
   private final boolean ordered;
   private final WriteConcern writeConcern;
   private int insertedCount;
   private int matchedCount;
   private int deletedCount;
   private int modifiedCount = 0;
   private final Set<BulkWriteUpsert> writeUpserts = new TreeSet(Comparator.comparingInt(BulkWriteUpsert::getIndex));
   private final Set<BulkWriteInsert> writeInserts = new TreeSet(Comparator.comparingInt(BulkWriteInsert::getIndex));
   private final Set<BulkWriteError> writeErrors = new TreeSet(Comparator.comparingInt(BulkWriteError::getIndex));
   private final Set<String> errorLabels = new HashSet();
   private final List<WriteConcernError> writeConcernErrors = new ArrayList();

   public BulkWriteBatchCombiner(ServerAddress serverAddress, boolean ordered, WriteConcern writeConcern) {
      this.writeConcern = (WriteConcern)Assertions.notNull("writeConcern", writeConcern);
      this.ordered = ordered;
      this.serverAddress = (ServerAddress)Assertions.notNull("serverAddress", serverAddress);
   }

   public void addResult(BulkWriteResult result) {
      this.insertedCount += result.getInsertedCount();
      this.matchedCount += result.getMatchedCount();
      this.deletedCount += result.getDeletedCount();
      this.modifiedCount += result.getModifiedCount();
      this.writeUpserts.addAll(result.getUpserts());
      this.writeInserts.addAll(result.getInserts());
   }

   public void addErrorResult(MongoBulkWriteException exception, IndexMap indexMap) {
      this.addResult(exception.getWriteResult());
      this.errorLabels.addAll(exception.getErrorLabels());
      this.mergeWriteErrors(exception.getWriteErrors(), indexMap);
      this.mergeWriteConcernError(exception.getWriteConcernError());
   }

   public void addWriteErrorResult(BulkWriteError writeError, IndexMap indexMap) {
      Assertions.notNull("writeError", writeError);
      this.mergeWriteErrors(Collections.singletonList(writeError), indexMap);
   }

   public void addWriteConcernErrorResult(WriteConcernError writeConcernError) {
      Assertions.notNull("writeConcernError", writeConcernError);
      this.mergeWriteConcernError(writeConcernError);
   }

   public void addErrorResult(List<BulkWriteError> writeErrors, WriteConcernError writeConcernError, IndexMap indexMap) {
      this.mergeWriteErrors(writeErrors, indexMap);
      this.mergeWriteConcernError(writeConcernError);
   }

   public BulkWriteResult getResult() {
      this.throwOnError();
      return this.createResult();
   }

   public boolean shouldStopSendingMoreBatches() {
      return this.ordered && this.hasWriteErrors();
   }

   public boolean hasErrors() {
      return this.hasWriteErrors() || this.hasWriteConcernErrors();
   }

   @Nullable
   public MongoBulkWriteException getError() {
      return !this.hasErrors() ? null : this.getErrorNonNullable();
   }

   private MongoBulkWriteException getErrorNonNullable() {
      return new MongoBulkWriteException(this.createResult(), new ArrayList(this.writeErrors), this.writeConcernErrors.isEmpty() ? null : (WriteConcernError)this.writeConcernErrors.get(this.writeConcernErrors.size() - 1), this.serverAddress, this.errorLabels);
   }

   private void mergeWriteConcernError(@Nullable WriteConcernError writeConcernError) {
      if (writeConcernError != null) {
         if (this.writeConcernErrors.isEmpty()) {
            this.writeConcernErrors.add(writeConcernError);
            this.errorLabels.addAll(writeConcernError.getErrorLabels());
         } else if (!writeConcernError.equals(this.writeConcernErrors.get(this.writeConcernErrors.size() - 1))) {
            this.writeConcernErrors.add(writeConcernError);
            this.errorLabels.addAll(writeConcernError.getErrorLabels());
         }
      }

   }

   private void mergeWriteErrors(List<BulkWriteError> newWriteErrors, IndexMap indexMap) {
      Iterator var3 = newWriteErrors.iterator();

      while(var3.hasNext()) {
         BulkWriteError cur = (BulkWriteError)var3.next();
         this.writeErrors.add(new BulkWriteError(cur.getCode(), cur.getMessage(), cur.getDetails(), indexMap.map(cur.getIndex())));
      }

   }

   private void throwOnError() {
      if (this.hasErrors()) {
         throw this.getErrorNonNullable();
      }
   }

   private BulkWriteResult createResult() {
      return this.writeConcern.isAcknowledged() ? BulkWriteResult.acknowledged(this.insertedCount, this.matchedCount, this.deletedCount, this.modifiedCount, new ArrayList(this.writeUpserts), new ArrayList(this.writeInserts)) : BulkWriteResult.unacknowledged();
   }

   private boolean hasWriteErrors() {
      return !this.writeErrors.isEmpty();
   }

   private boolean hasWriteConcernErrors() {
      return !this.writeConcernErrors.isEmpty();
   }
}
