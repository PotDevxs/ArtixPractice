package dev.artixdev.libs.com.mongodb.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ConnectionString;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolListener;

@Immutable
public class ConnectionPoolSettings {
   private final List<ConnectionPoolListener> connectionPoolListeners;
   private final int maxSize;
   private final int minSize;
   private final long maxWaitTimeMS;
   private final long maxConnectionLifeTimeMS;
   private final long maxConnectionIdleTimeMS;
   private final long maintenanceInitialDelayMS;
   private final long maintenanceFrequencyMS;
   private final int maxConnecting;

   public static ConnectionPoolSettings.Builder builder() {
      return new ConnectionPoolSettings.Builder();
   }

   public static ConnectionPoolSettings.Builder builder(ConnectionPoolSettings connectionPoolSettings) {
      return builder().applySettings(connectionPoolSettings);
   }

   public int getMaxSize() {
      return this.maxSize;
   }

   public int getMinSize() {
      return this.minSize;
   }

   public long getMaxWaitTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.maxWaitTimeMS, TimeUnit.MILLISECONDS);
   }

   public long getMaxConnectionLifeTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.maxConnectionLifeTimeMS, TimeUnit.MILLISECONDS);
   }

   public long getMaxConnectionIdleTime(TimeUnit timeUnit) {
      return timeUnit.convert(this.maxConnectionIdleTimeMS, TimeUnit.MILLISECONDS);
   }

   public long getMaintenanceInitialDelay(TimeUnit timeUnit) {
      return timeUnit.convert(this.maintenanceInitialDelayMS, TimeUnit.MILLISECONDS);
   }

   public long getMaintenanceFrequency(TimeUnit timeUnit) {
      return timeUnit.convert(this.maintenanceFrequencyMS, TimeUnit.MILLISECONDS);
   }

   public List<ConnectionPoolListener> getConnectionPoolListeners() {
      return this.connectionPoolListeners;
   }

   public int getMaxConnecting() {
      return this.maxConnecting;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ConnectionPoolSettings that = (ConnectionPoolSettings)o;
         if (this.maxConnectionIdleTimeMS != that.maxConnectionIdleTimeMS) {
            return false;
         } else if (this.maxConnectionLifeTimeMS != that.maxConnectionLifeTimeMS) {
            return false;
         } else if (this.maxSize != that.maxSize) {
            return false;
         } else if (this.minSize != that.minSize) {
            return false;
         } else if (this.maintenanceInitialDelayMS != that.maintenanceInitialDelayMS) {
            return false;
         } else if (this.maintenanceFrequencyMS != that.maintenanceFrequencyMS) {
            return false;
         } else if (this.maxWaitTimeMS != that.maxWaitTimeMS) {
            return false;
         } else if (!this.connectionPoolListeners.equals(that.connectionPoolListeners)) {
            return false;
         } else {
            return this.maxConnecting == that.maxConnecting;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.maxSize;
      result = 31 * result + this.minSize;
      result = 31 * result + (int)(this.maxWaitTimeMS ^ this.maxWaitTimeMS >>> 32);
      result = 31 * result + (int)(this.maxConnectionLifeTimeMS ^ this.maxConnectionLifeTimeMS >>> 32);
      result = 31 * result + (int)(this.maxConnectionIdleTimeMS ^ this.maxConnectionIdleTimeMS >>> 32);
      result = 31 * result + (int)(this.maintenanceInitialDelayMS ^ this.maintenanceInitialDelayMS >>> 32);
      result = 31 * result + (int)(this.maintenanceFrequencyMS ^ this.maintenanceFrequencyMS >>> 32);
      result = 31 * result + this.connectionPoolListeners.hashCode();
      result = 31 * result + this.maxConnecting;
      return result;
   }

   public String toString() {
      return "ConnectionPoolSettings{maxSize=" + this.maxSize + ", minSize=" + this.minSize + ", maxWaitTimeMS=" + this.maxWaitTimeMS + ", maxConnectionLifeTimeMS=" + this.maxConnectionLifeTimeMS + ", maxConnectionIdleTimeMS=" + this.maxConnectionIdleTimeMS + ", maintenanceInitialDelayMS=" + this.maintenanceInitialDelayMS + ", maintenanceFrequencyMS=" + this.maintenanceFrequencyMS + ", connectionPoolListeners=" + this.connectionPoolListeners + ", maxConnecting=" + this.maxConnecting + '}';
   }

   ConnectionPoolSettings(ConnectionPoolSettings.Builder builder) {
      Assertions.isTrue("maxSize >= 0", builder.maxSize >= 0);
      Assertions.isTrue("minSize >= 0", builder.minSize >= 0);
      Assertions.isTrue("maintenanceInitialDelayMS >= 0", builder.maintenanceInitialDelayMS >= 0L);
      Assertions.isTrue("maxConnectionLifeTime >= 0", builder.maxConnectionLifeTimeMS >= 0L);
      Assertions.isTrue("maxConnectionIdleTime >= 0", builder.maxConnectionIdleTimeMS >= 0L);
      Assertions.isTrue("sizeMaintenanceFrequency > 0", builder.maintenanceFrequencyMS > 0L);
      Assertions.isTrue("maxSize >= minSize", builder.maxSize >= builder.minSize);
      Assertions.isTrue("maxConnecting > 0", builder.maxConnecting > 0);
      this.maxSize = builder.maxSize;
      this.minSize = builder.minSize;
      this.maxWaitTimeMS = builder.maxWaitTimeMS;
      this.maxConnectionLifeTimeMS = builder.maxConnectionLifeTimeMS;
      this.maxConnectionIdleTimeMS = builder.maxConnectionIdleTimeMS;
      this.maintenanceInitialDelayMS = builder.maintenanceInitialDelayMS;
      this.maintenanceFrequencyMS = builder.maintenanceFrequencyMS;
      this.connectionPoolListeners = Collections.unmodifiableList(builder.connectionPoolListeners);
      this.maxConnecting = builder.maxConnecting;
   }

   @NotThreadSafe
   public static final class Builder {
      private List<ConnectionPoolListener> connectionPoolListeners = new ArrayList();
      private int maxSize = 100;
      private int minSize;
      private long maxWaitTimeMS = 120000L;
      private long maxConnectionLifeTimeMS;
      private long maxConnectionIdleTimeMS;
      private long maintenanceInitialDelayMS;
      private long maintenanceFrequencyMS;
      private int maxConnecting;

      Builder() {
         this.maintenanceFrequencyMS = TimeUnit.MILLISECONDS.convert(1L, TimeUnit.MINUTES);
         this.maxConnecting = 2;
      }

      public ConnectionPoolSettings.Builder applySettings(ConnectionPoolSettings connectionPoolSettings) {
         Assertions.notNull("connectionPoolSettings", connectionPoolSettings);
         this.connectionPoolListeners = new ArrayList(connectionPoolSettings.connectionPoolListeners);
         this.maxSize = connectionPoolSettings.maxSize;
         this.minSize = connectionPoolSettings.minSize;
         this.maxWaitTimeMS = connectionPoolSettings.maxWaitTimeMS;
         this.maxConnectionLifeTimeMS = connectionPoolSettings.maxConnectionLifeTimeMS;
         this.maxConnectionIdleTimeMS = connectionPoolSettings.maxConnectionIdleTimeMS;
         this.maintenanceInitialDelayMS = connectionPoolSettings.maintenanceInitialDelayMS;
         this.maintenanceFrequencyMS = connectionPoolSettings.maintenanceFrequencyMS;
         this.maxConnecting = connectionPoolSettings.maxConnecting;
         return this;
      }

      public ConnectionPoolSettings.Builder maxSize(int maxSize) {
         this.maxSize = maxSize;
         return this;
      }

      public ConnectionPoolSettings.Builder minSize(int minSize) {
         this.minSize = minSize;
         return this;
      }

      public ConnectionPoolSettings.Builder maxWaitTime(long maxWaitTime, TimeUnit timeUnit) {
         this.maxWaitTimeMS = TimeUnit.MILLISECONDS.convert(maxWaitTime, timeUnit);
         return this;
      }

      public ConnectionPoolSettings.Builder maxConnectionLifeTime(long maxConnectionLifeTime, TimeUnit timeUnit) {
         this.maxConnectionLifeTimeMS = TimeUnit.MILLISECONDS.convert(maxConnectionLifeTime, timeUnit);
         return this;
      }

      public ConnectionPoolSettings.Builder maxConnectionIdleTime(long maxConnectionIdleTime, TimeUnit timeUnit) {
         this.maxConnectionIdleTimeMS = TimeUnit.MILLISECONDS.convert(maxConnectionIdleTime, timeUnit);
         return this;
      }

      public ConnectionPoolSettings.Builder maintenanceInitialDelay(long maintenanceInitialDelay, TimeUnit timeUnit) {
         this.maintenanceInitialDelayMS = TimeUnit.MILLISECONDS.convert(maintenanceInitialDelay, timeUnit);
         return this;
      }

      public ConnectionPoolSettings.Builder maintenanceFrequency(long maintenanceFrequency, TimeUnit timeUnit) {
         this.maintenanceFrequencyMS = TimeUnit.MILLISECONDS.convert(maintenanceFrequency, timeUnit);
         return this;
      }

      public ConnectionPoolSettings.Builder addConnectionPoolListener(ConnectionPoolListener connectionPoolListener) {
         this.connectionPoolListeners.add((ConnectionPoolListener)Assertions.notNull("connectionPoolListener", connectionPoolListener));
         return this;
      }

      public ConnectionPoolSettings.Builder connectionPoolListenerList(List<ConnectionPoolListener> connectionPoolListeners) {
         Assertions.notNull("connectionPoolListeners", connectionPoolListeners);
         this.connectionPoolListeners = new ArrayList(connectionPoolListeners);
         return this;
      }

      public ConnectionPoolSettings.Builder maxConnecting(int maxConnecting) {
         this.maxConnecting = maxConnecting;
         return this;
      }

      public ConnectionPoolSettings build() {
         return new ConnectionPoolSettings(this);
      }

      public ConnectionPoolSettings.Builder applyConnectionString(ConnectionString connectionString) {
         Integer maxConnectionPoolSize = connectionString.getMaxConnectionPoolSize();
         if (maxConnectionPoolSize != null) {
            this.maxSize(maxConnectionPoolSize);
         }

         Integer minConnectionPoolSize = connectionString.getMinConnectionPoolSize();
         if (minConnectionPoolSize != null) {
            this.minSize(minConnectionPoolSize);
         }

         Integer maxWaitTime = connectionString.getMaxWaitTime();
         if (maxWaitTime != null) {
            this.maxWaitTime((long)maxWaitTime, TimeUnit.MILLISECONDS);
         }

         Integer maxConnectionIdleTime = connectionString.getMaxConnectionIdleTime();
         if (maxConnectionIdleTime != null) {
            this.maxConnectionIdleTime((long)maxConnectionIdleTime, TimeUnit.MILLISECONDS);
         }

         Integer maxConnectionLifeTime = connectionString.getMaxConnectionLifeTime();
         if (maxConnectionLifeTime != null) {
            this.maxConnectionLifeTime((long)maxConnectionLifeTime, TimeUnit.MILLISECONDS);
         }

         Integer maxConnecting = connectionString.getMaxConnecting();
         if (maxConnecting != null) {
            this.maxConnecting(maxConnecting);
         }

         return this;
      }
   }
}
