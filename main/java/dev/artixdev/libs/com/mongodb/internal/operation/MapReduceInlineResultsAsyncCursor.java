package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;

class MapReduceInlineResultsAsyncCursor<T> extends AsyncSingleBatchQueryCursor<T> implements MapReduceAsyncBatchCursor<T> {
   private final MapReduceStatistics statistics;

   MapReduceInlineResultsAsyncCursor(QueryResult<T> queryResult, MapReduceStatistics statistics) {
      super(queryResult);
      this.statistics = statistics;
   }

   public MapReduceStatistics getStatistics() {
      return this.statistics;
   }
}
