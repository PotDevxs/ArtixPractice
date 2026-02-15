package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerCursor;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class QueryResult<T> {
   private final MongoNamespace namespace;
   private final List<T> results;
   private final long cursorId;
   private final ServerAddress serverAddress;

   public QueryResult(@Nullable MongoNamespace namespace, List<T> results, long cursorId, ServerAddress serverAddress) {
      this.namespace = namespace;
      this.results = results;
      this.cursorId = cursorId;
      this.serverAddress = serverAddress;
   }

   @Nullable
   public MongoNamespace getNamespace() {
      return this.namespace;
   }

   @Nullable
   public ServerCursor getCursor() {
      return this.cursorId == 0L ? null : new ServerCursor(this.cursorId, this.serverAddress);
   }

   public List<T> getResults() {
      return this.results;
   }

   public ServerAddress getAddress() {
      return this.serverAddress;
   }

   public long getCursorId() {
      return this.cursorId;
   }
}
