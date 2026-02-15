package dev.artixdev.libs.com.mongodb.connection;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Immutable
public final class ConnectionId {
   private static final AtomicInteger INCREMENTING_ID = new AtomicInteger();
   private final ServerId serverId;
   private final int localValue;
   private final Integer serverValue;
   private final String stringValue;

   public ConnectionId(ServerId serverId) {
      this(serverId, INCREMENTING_ID.incrementAndGet(), (Integer)null);
   }

   public ConnectionId(ServerId serverId, int localValue, @Nullable Integer serverValue) {
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
      this.localValue = localValue;
      this.serverValue = serverValue;
      if (serverValue == null) {
         this.stringValue = String.format("connectionId{localValue:%s}", localValue);
      } else {
         this.stringValue = String.format("connectionId{localValue:%s, serverValue:%s}", localValue, serverValue);
      }

   }

   public ConnectionId withServerValue(int serverValue) {
      Assertions.isTrue("server value is null", this.serverValue == null);
      return new ConnectionId(this.serverId, this.localValue, serverValue);
   }

   public ServerId getServerId() {
      return this.serverId;
   }

   public int getLocalValue() {
      return this.localValue;
   }

   @Nullable
   public Integer getServerValue() {
      return this.serverValue;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ConnectionId that = (ConnectionId)o;
         if (this.localValue != that.localValue) {
            return false;
         } else if (!this.serverId.equals(that.serverId)) {
            return false;
         } else {
            return Objects.equals(this.serverValue, that.serverValue);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.serverId.hashCode();
      result = 31 * result + this.localValue;
      result = 31 * result + (this.serverValue != null ? this.serverValue.hashCode() : 0);
      return result;
   }

   public String toString() {
      return this.stringValue;
   }
}
