package dev.artixdev.libs.com.mongodb.event;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;
import dev.artixdev.libs.org.bson.BsonDocument;

public final class ServerHeartbeatSucceededEvent {
   private final ConnectionId connectionId;
   private final BsonDocument reply;
   private final long elapsedTimeNanos;
   private final boolean awaited;

   /** @deprecated */
   @Deprecated
   public ServerHeartbeatSucceededEvent(ConnectionId connectionId, BsonDocument reply, long elapsedTimeNanos) {
      this(connectionId, reply, elapsedTimeNanos, false);
   }

   public ServerHeartbeatSucceededEvent(ConnectionId connectionId, BsonDocument reply, long elapsedTimeNanos, boolean awaited) {
      this.connectionId = (ConnectionId)Assertions.notNull("connectionId", connectionId);
      this.reply = (BsonDocument)Assertions.notNull("reply", reply);
      Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
      this.elapsedTimeNanos = elapsedTimeNanos;
      this.awaited = awaited;
   }

   public ConnectionId getConnectionId() {
      return this.connectionId;
   }

   public BsonDocument getReply() {
      return this.reply;
   }

   public long getElapsedTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
   }

   public boolean isAwaited() {
      return this.awaited;
   }

   public String toString() {
      return "ServerHeartbeatSucceededEvent{connectionId=" + this.connectionId + ", server=" + this.connectionId.getServerId().getAddress() + ", clusterId=" + this.connectionId.getServerId().getClusterId() + ", reply=" + this.reply + ", elapsedTimeNanos=" + this.elapsedTimeNanos + ", awaited=" + this.awaited + "} ";
   }
}
