package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class CountDocumentsOperation implements AsyncReadOperation<Long>, ReadOperation<Long> {
   private static final Decoder<BsonDocument> DECODER = new BsonDocumentCodec();
   private final MongoNamespace namespace;
   private boolean retryReads;
   private BsonDocument filter;
   private BsonValue hint;
   private BsonValue comment;
   private long skip;
   private long limit;
   private long maxTimeMS;
   private Collation collation;

   public CountDocumentsOperation(MongoNamespace namespace) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
   }

   public BsonDocument getFilter() {
      return this.filter;
   }

   public CountDocumentsOperation filter(@Nullable BsonDocument filter) {
      this.filter = filter;
      return this;
   }

   public CountDocumentsOperation retryReads(boolean retryReads) {
      this.retryReads = retryReads;
      return this;
   }

   public boolean getRetryReads() {
      return this.retryReads;
   }

   public BsonValue getHint() {
      return this.hint;
   }

   public CountDocumentsOperation hint(@Nullable BsonValue hint) {
      this.hint = hint;
      return this;
   }

   public long getLimit() {
      return this.limit;
   }

   public CountDocumentsOperation limit(long limit) {
      this.limit = limit;
      return this;
   }

   public long getSkip() {
      return this.skip;
   }

   public CountDocumentsOperation skip(long skip) {
      this.skip = skip;
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public CountDocumentsOperation maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public Collation getCollation() {
      return this.collation;
   }

   public CountDocumentsOperation collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.comment;
   }

   public CountDocumentsOperation comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public Long execute(ReadBinding binding) {
      BatchCursor<BsonDocument> cursor = this.getAggregateOperation().execute(binding);
      return cursor.hasNext() ? this.getCountFromAggregateResults(cursor.next()) : 0L;
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<Long> callback) {
      this.getAggregateOperation().executeAsync(binding, (result, t) -> {
         if (t != null) {
            callback.onResult(null, t);
         } else {
            result.next((result1, t1) -> {
               if (t1 != null) {
                  callback.onResult(null, t1);
               } else {
                  callback.onResult(this.getCountFromAggregateResults(result1), (Throwable)null);
               }

            });
         }

      });
   }

   private AggregateOperation<BsonDocument> getAggregateOperation() {
      return (new AggregateOperation(this.namespace, this.getPipeline(), DECODER)).retryReads(this.retryReads).collation(this.collation).comment(this.comment).hint(this.hint).maxTime(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   private List<BsonDocument> getPipeline() {
      ArrayList<BsonDocument> pipeline = new ArrayList();
      pipeline.add(new BsonDocument("$match", this.filter != null ? this.filter : new BsonDocument()));
      if (this.skip > 0L) {
         pipeline.add(new BsonDocument("$skip", new BsonInt64(this.skip)));
      }

      if (this.limit > 0L) {
         pipeline.add(new BsonDocument("$limit", new BsonInt64(this.limit)));
      }

      pipeline.add(new BsonDocument("$group", (new BsonDocument("_id", new BsonInt32(1))).append("n", new BsonDocument("$sum", new BsonInt32(1)))));
      return pipeline;
   }

   private Long getCountFromAggregateResults(List<BsonDocument> results) {
      return results != null && !results.isEmpty() ? ((BsonDocument)results.get(0)).getNumber("n").longValue() : 0L;
   }
}
