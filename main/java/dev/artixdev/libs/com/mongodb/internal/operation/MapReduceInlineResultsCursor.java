package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.internal.binding.ConnectionSource;
import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.Decoder;

class MapReduceInlineResultsCursor<T> extends QueryBatchCursor<T> implements MapReduceBatchCursor<T> {
   private final MapReduceStatistics statistics;

   MapReduceInlineResultsCursor(QueryResult<T> queryResult, Decoder<T> decoder, ConnectionSource connectionSource, MapReduceStatistics statistics) {
      super(queryResult, 0, 0, decoder, (BsonValue)null, connectionSource);
      this.statistics = statistics;
   }

   public MapReduceStatistics getStatistics() {
      return this.statistics;
   }
}
