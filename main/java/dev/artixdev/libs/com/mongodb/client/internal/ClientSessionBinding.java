package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.Objects;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.connection.ClusterType;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.binding.AbstractReferenceCounted;
import dev.artixdev.libs.com.mongodb.internal.binding.ClusterAwareReadWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ConnectionSource;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.TransactionContext;
import dev.artixdev.libs.com.mongodb.internal.connection.Connection;
import dev.artixdev.libs.com.mongodb.internal.connection.OperationContext;
import dev.artixdev.libs.com.mongodb.internal.session.ClientSessionContext;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.assertions.Assertions;

public class ClientSessionBinding extends AbstractReferenceCounted implements ReadWriteBinding {
   private final ClusterAwareReadWriteBinding wrapped;
   private final ClientSession session;
   private final boolean ownsSession;
   private final ClientSessionContext sessionContext;

   public ClientSessionBinding(ClientSession session, boolean ownsSession, ClusterAwareReadWriteBinding wrapped) {
      this.wrapped = wrapped;
      wrapped.retain();
      this.session = (ClientSession)Assertions.notNull("session", session);
      this.ownsSession = ownsSession;
      this.sessionContext = new ClientSessionBinding.SyncClientSessionContext(session);
   }

   public ReadPreference getReadPreference() {
      return this.wrapped.getReadPreference();
   }

   public int getCount() {
      return this.wrapped.getCount();
   }

   public ClientSessionBinding retain() {
      super.retain();
      return this;
   }

   public int release() {
      int count = super.release();
      if (count == 0) {
         this.wrapped.release();
         if (this.ownsSession) {
            this.session.close();
         }
      }

      return count;
   }

   public ConnectionSource getReadConnectionSource() {
      ClusterAwareReadWriteBinding var10004 = this.wrapped;
      Objects.requireNonNull(var10004);
      return new ClientSessionBinding.SessionBindingConnectionSource(this.getConnectionSource(var10004::getReadConnectionSource));
   }

   public ConnectionSource getReadConnectionSource(int minWireVersion, ReadPreference fallbackReadPreference) {
      return new ClientSessionBinding.SessionBindingConnectionSource(this.getConnectionSource(() -> {
         return this.wrapped.getReadConnectionSource(minWireVersion, fallbackReadPreference);
      }));
   }

   public ConnectionSource getWriteConnectionSource() {
      ClusterAwareReadWriteBinding var10004 = this.wrapped;
      Objects.requireNonNull(var10004);
      return new ClientSessionBinding.SessionBindingConnectionSource(this.getConnectionSource(var10004::getWriteConnectionSource));
   }

   public SessionContext getSessionContext() {
      return this.sessionContext;
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

   private ConnectionSource getConnectionSource(Supplier<ConnectionSource> wrappedConnectionSourceSupplier) {
      if (!this.session.hasActiveTransaction()) {
         return (ConnectionSource)wrappedConnectionSourceSupplier.get();
      } else if (TransactionContext.get(this.session) != null) {
         return this.wrapped.getConnectionSource((ServerAddress)Assertions.assertNotNull(this.session.getPinnedServerAddress()));
      } else {
         ConnectionSource source = (ConnectionSource)wrappedConnectionSourceSupplier.get();
         ClusterType clusterType = source.getServerDescription().getClusterType();
         if (clusterType == ClusterType.SHARDED || clusterType == ClusterType.LOAD_BALANCED) {
            TransactionContext<Connection> transactionContext = new TransactionContext(clusterType);
            this.session.setTransactionContext(source.getServerDescription().getAddress(), transactionContext);
            transactionContext.release();
         }

         return source;
      }
   }

   private final class SyncClientSessionContext extends ClientSessionContext {
      private final ClientSession clientSession;

      SyncClientSessionContext(ClientSession clientSession) {
         super(clientSession);
         this.clientSession = clientSession;
      }

      public boolean isImplicitSession() {
         return ClientSessionBinding.this.ownsSession;
      }

      public boolean notifyMessageSent() {
         return this.clientSession.notifyMessageSent();
      }

      public boolean hasActiveTransaction() {
         return this.clientSession.hasActiveTransaction();
      }

      public ReadConcern getReadConcern() {
         if (this.clientSession.hasActiveTransaction()) {
            return (ReadConcern)Assertions.assertNotNull(this.clientSession.getTransactionOptions().getReadConcern());
         } else {
            return this.isSnapshot() ? ReadConcern.SNAPSHOT : ClientSessionBinding.this.wrapped.getSessionContext().getReadConcern();
         }
      }
   }

   private class SessionBindingConnectionSource implements ConnectionSource {
      private ConnectionSource wrapped;

      SessionBindingConnectionSource(ConnectionSource wrapped) {
         this.wrapped = wrapped;
         ClientSessionBinding.this.retain();
      }

      public ServerDescription getServerDescription() {
         return this.wrapped.getServerDescription();
      }

      public SessionContext getSessionContext() {
         return ClientSessionBinding.this.sessionContext;
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
         TransactionContext<Connection> transactionContext = TransactionContext.get(ClientSessionBinding.this.session);
         if (transactionContext != null && transactionContext.isConnectionPinningRequired()) {
            Connection pinnedConnection = (Connection)transactionContext.getPinnedConnection();
            if (pinnedConnection == null) {
               Connection connection = this.wrapped.getConnection();
               transactionContext.pinConnection(connection, Connection::markAsPinned);
               return connection;
            } else {
               return pinnedConnection.retain();
            }
         } else {
            return this.wrapped.getConnection();
         }
      }

      public ConnectionSource retain() {
         this.wrapped = this.wrapped.retain();
         return this;
      }

      public int getCount() {
         return this.wrapped.getCount();
      }

      public int release() {
         int count = this.wrapped.release();
         if (count == 0) {
            ClientSessionBinding.this.release();
         }

         return count;
      }
   }
}
