package dev.artixdev.libs.com.mongodb.client.internal;

import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.binding.ClusterAwareReadWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ConnectionSource;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.connection.Cluster;
import dev.artixdev.libs.com.mongodb.internal.connection.Connection;
import dev.artixdev.libs.com.mongodb.internal.connection.OperationContext;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

class CryptBinding implements ClusterAwareReadWriteBinding {
   private final ClusterAwareReadWriteBinding wrapped;
   private final Crypt crypt;

   CryptBinding(ClusterAwareReadWriteBinding wrapped, Crypt crypt) {
      this.crypt = crypt;
      this.wrapped = wrapped;
   }

   public ReadPreference getReadPreference() {
      return this.wrapped.getReadPreference();
   }

   public ConnectionSource getReadConnectionSource() {
      return new CryptBinding.CryptConnectionSource(this.wrapped.getReadConnectionSource());
   }

   public ConnectionSource getReadConnectionSource(int minWireVersion, ReadPreference fallbackReadPreference) {
      return new CryptBinding.CryptConnectionSource(this.wrapped.getReadConnectionSource(minWireVersion, fallbackReadPreference));
   }

   public ConnectionSource getWriteConnectionSource() {
      return new CryptBinding.CryptConnectionSource(this.wrapped.getWriteConnectionSource());
   }

   public ConnectionSource getConnectionSource(ServerAddress serverAddress) {
      return new CryptBinding.CryptConnectionSource(this.wrapped.getConnectionSource(serverAddress));
   }

   public SessionContext getSessionContext() {
      return this.wrapped.getSessionContext();
   }

   @Nullable
   public ServerApi getServerApi() {
      return this.wrapped.getServerApi();
   }

   public RequestContext getRequestContext() {
      return this.wrapped.getRequestContext();
   }

   public OperationContext getOperationContext() {
      return this.wrapped.getOperationContext();
   }

   public int getCount() {
      return this.wrapped.getCount();
   }

   public ReadWriteBinding retain() {
      this.wrapped.retain();
      return this;
   }

   public int release() {
      return this.wrapped.release();
   }

   public Cluster getCluster() {
      return this.wrapped.getCluster();
   }

   private class CryptConnectionSource implements ConnectionSource {
      private final ConnectionSource wrapped;

      CryptConnectionSource(ConnectionSource wrapped) {
         this.wrapped = wrapped;
      }

      public ServerDescription getServerDescription() {
         return this.wrapped.getServerDescription();
      }

      public SessionContext getSessionContext() {
         return this.wrapped.getSessionContext();
      }

      public OperationContext getOperationContext() {
         return this.wrapped.getOperationContext();
      }

      public ServerApi getServerApi() {
         return this.wrapped.getServerApi();
      }

      public RequestContext getRequestContext() {
         return this.wrapped.getRequestContext();
      }

      public ReadPreference getReadPreference() {
         return this.wrapped.getReadPreference();
      }

      public Connection getConnection() {
         return new CryptConnection(this.wrapped.getConnection(), CryptBinding.this.crypt);
      }

      public int getCount() {
         return this.wrapped.getCount();
      }

      public ConnectionSource retain() {
         this.wrapped.retain();
         return this;
      }

      public int release() {
         return this.wrapped.release();
      }
   }
}
