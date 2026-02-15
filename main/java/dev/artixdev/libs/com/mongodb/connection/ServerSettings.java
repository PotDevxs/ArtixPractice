package dev.artixdev.libs.com.mongodb.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ConnectionString;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.event.ServerListener;
import dev.artixdev.libs.com.mongodb.event.ServerMonitorListener;

@Immutable
public class ServerSettings {
   private final long heartbeatFrequencyMS;
   private final long minHeartbeatFrequencyMS;
   private final List<ServerListener> serverListeners;
   private final List<ServerMonitorListener> serverMonitorListeners;

   public static ServerSettings.Builder builder() {
      return new ServerSettings.Builder();
   }

   public static ServerSettings.Builder builder(ServerSettings serverSettings) {
      return builder().applySettings(serverSettings);
   }

   public long getHeartbeatFrequency(TimeUnit timeUnit) {
      return timeUnit.convert(this.heartbeatFrequencyMS, TimeUnit.MILLISECONDS);
   }

   public long getMinHeartbeatFrequency(TimeUnit timeUnit) {
      return timeUnit.convert(this.minHeartbeatFrequencyMS, TimeUnit.MILLISECONDS);
   }

   public List<ServerListener> getServerListeners() {
      return this.serverListeners;
   }

   public List<ServerMonitorListener> getServerMonitorListeners() {
      return this.serverMonitorListeners;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ServerSettings that = (ServerSettings)o;
         if (this.heartbeatFrequencyMS != that.heartbeatFrequencyMS) {
            return false;
         } else if (this.minHeartbeatFrequencyMS != that.minHeartbeatFrequencyMS) {
            return false;
         } else if (!this.serverListeners.equals(that.serverListeners)) {
            return false;
         } else {
            return this.serverMonitorListeners.equals(that.serverMonitorListeners);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = (int)(this.heartbeatFrequencyMS ^ this.heartbeatFrequencyMS >>> 32);
      result = 31 * result + (int)(this.minHeartbeatFrequencyMS ^ this.minHeartbeatFrequencyMS >>> 32);
      result = 31 * result + this.serverListeners.hashCode();
      result = 31 * result + this.serverMonitorListeners.hashCode();
      return result;
   }

   public String toString() {
      return "ServerSettings{heartbeatFrequencyMS=" + this.heartbeatFrequencyMS + ", minHeartbeatFrequencyMS=" + this.minHeartbeatFrequencyMS + ", serverListeners='" + this.serverListeners + '\'' + ", serverMonitorListeners='" + this.serverMonitorListeners + '\'' + '}';
   }

   ServerSettings(ServerSettings.Builder builder) {
      this.heartbeatFrequencyMS = builder.heartbeatFrequencyMS;
      this.minHeartbeatFrequencyMS = builder.minHeartbeatFrequencyMS;
      this.serverListeners = Collections.unmodifiableList(builder.serverListeners);
      this.serverMonitorListeners = Collections.unmodifiableList(builder.serverMonitorListeners);
   }

   @NotThreadSafe
   public static final class Builder {
      private long heartbeatFrequencyMS;
      private long minHeartbeatFrequencyMS;
      private List<ServerListener> serverListeners;
      private List<ServerMonitorListener> serverMonitorListeners;

      private Builder() {
         this.heartbeatFrequencyMS = 10000L;
         this.minHeartbeatFrequencyMS = 500L;
         this.serverListeners = new ArrayList();
         this.serverMonitorListeners = new ArrayList();
      }

      public ServerSettings.Builder applySettings(ServerSettings serverSettings) {
         Assertions.notNull("serverSettings", serverSettings);
         this.heartbeatFrequencyMS = serverSettings.heartbeatFrequencyMS;
         this.minHeartbeatFrequencyMS = serverSettings.minHeartbeatFrequencyMS;
         this.serverListeners = new ArrayList(serverSettings.serverListeners);
         this.serverMonitorListeners = new ArrayList(serverSettings.serverMonitorListeners);
         return this;
      }

      public ServerSettings.Builder heartbeatFrequency(long heartbeatFrequency, TimeUnit timeUnit) {
         this.heartbeatFrequencyMS = TimeUnit.MILLISECONDS.convert(heartbeatFrequency, timeUnit);
         return this;
      }

      public ServerSettings.Builder minHeartbeatFrequency(long minHeartbeatFrequency, TimeUnit timeUnit) {
         this.minHeartbeatFrequencyMS = TimeUnit.MILLISECONDS.convert(minHeartbeatFrequency, timeUnit);
         return this;
      }

      public ServerSettings.Builder addServerListener(ServerListener serverListener) {
         Assertions.notNull("serverListener", serverListener);
         this.serverListeners.add(serverListener);
         return this;
      }

      public ServerSettings.Builder serverListenerList(List<ServerListener> serverListeners) {
         Assertions.notNull("serverListeners", serverListeners);
         this.serverListeners = new ArrayList(serverListeners);
         return this;
      }

      public ServerSettings.Builder addServerMonitorListener(ServerMonitorListener serverMonitorListener) {
         Assertions.notNull("serverMonitorListener", serverMonitorListener);
         this.serverMonitorListeners.add(serverMonitorListener);
         return this;
      }

      public ServerSettings.Builder serverMonitorListenerList(List<ServerMonitorListener> serverMonitorListeners) {
         Assertions.notNull("serverMonitorListeners", serverMonitorListeners);
         this.serverMonitorListeners = new ArrayList(serverMonitorListeners);
         return this;
      }

      public ServerSettings.Builder applyConnectionString(ConnectionString connectionString) {
         Integer heartbeatFrequency = connectionString.getHeartbeatFrequency();
         if (heartbeatFrequency != null) {
            this.heartbeatFrequencyMS = (long)heartbeatFrequency;
         }

         return this;
      }

      public ServerSettings build() {
         return new ServerSettings(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
