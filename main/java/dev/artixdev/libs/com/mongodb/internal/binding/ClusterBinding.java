package dev.artixdev.libs.com.mongodb.internal.binding;

import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.connection.Cluster;
import dev.artixdev.libs.com.mongodb.internal.connection.Connection;
import dev.artixdev.libs.com.mongodb.internal.connection.OperationContext;
import dev.artixdev.libs.com.mongodb.internal.connection.ReadConcernAwareNoOpSessionContext;
import dev.artixdev.libs.com.mongodb.internal.connection.Server;
import dev.artixdev.libs.com.mongodb.internal.connection.ServerTuple;
import dev.artixdev.libs.com.mongodb.internal.selector.ReadPreferenceServerSelector;
import dev.artixdev.libs.com.mongodb.internal.selector.ReadPreferenceWithFallbackServerSelector;
import dev.artixdev.libs.com.mongodb.internal.selector.ServerAddressSelector;
import dev.artixdev.libs.com.mongodb.internal.selector.WritableServerSelector;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class ClusterBinding extends AbstractReferenceCounted implements ClusterAwareReadWriteBinding {
   private final Cluster cluster;
   private final ReadPreference readPreference;
   private final ReadConcern readConcern;
   @Nullable
   private final ServerApi serverApi;
   private final RequestContext requestContext;
   private final OperationContext operationContext;

   public ClusterBinding(Cluster cluster, ReadPreference readPreference, ReadConcern readConcern, @Nullable ServerApi serverApi, RequestContext requestContext) {
      this.cluster = (Cluster)Assertions.notNull("cluster", cluster);
      this.readPreference = (ReadPreference)Assertions.notNull("readPreference", readPreference);
      this.readConcern = (ReadConcern)Assertions.notNull("readConcern", readConcern);
      this.serverApi = serverApi;
      this.requestContext = (RequestContext)Assertions.notNull("requestContext", requestContext);
      this.operationContext = new OperationContext();
   }

   public Cluster getCluster() {
      return this.cluster;
   }

   public ReadWriteBinding retain() {
      super.retain();
      return this;
   }

   public ReadPreference getReadPreference() {
      return this.readPreference;
   }

   public SessionContext getSessionContext() {
      return new ReadConcernAwareNoOpSessionContext(this.readConcern);
   }

   @Nullable
   public ServerApi getServerApi() {
      return this.serverApi;
   }

   public RequestContext getRequestContext() {
      return this.requestContext;
   }

   public OperationContext getOperationContext() {
      return this.operationContext;
   }

   public ConnectionSource getReadConnectionSource() {
      return new ClusterBinding.ClusterBindingConnectionSource(this.cluster.selectServer(new ReadPreferenceServerSelector(this.readPreference), this.operationContext), this.readPreference);
   }

   public ConnectionSource getReadConnectionSource(int minWireVersion, ReadPreference fallbackReadPreference) {
      if (this.cluster.getSettings().getMode() == ClusterConnectionMode.LOAD_BALANCED) {
         return this.getReadConnectionSource();
      } else {
         ReadPreferenceWithFallbackServerSelector readPreferenceWithFallbackServerSelector = new ReadPreferenceWithFallbackServerSelector(this.readPreference, minWireVersion, fallbackReadPreference);
         ServerTuple serverTuple = this.cluster.selectServer(readPreferenceWithFallbackServerSelector, this.operationContext);
         return new ClusterBinding.ClusterBindingConnectionSource(serverTuple, readPreferenceWithFallbackServerSelector.getAppliedReadPreference());
      }
   }

   public ConnectionSource getWriteConnectionSource() {
      return new ClusterBinding.ClusterBindingConnectionSource(this.cluster.selectServer(new WritableServerSelector(), this.operationContext), this.readPreference);
   }

   public ConnectionSource getConnectionSource(ServerAddress serverAddress) {
      return new ClusterBinding.ClusterBindingConnectionSource(this.cluster.selectServer(new ServerAddressSelector(serverAddress), this.operationContext), this.readPreference);
   }

   private final class ClusterBindingConnectionSource extends AbstractReferenceCounted implements ConnectionSource {
      private final Server server;
      private final ServerDescription serverDescription;
      private final ReadPreference appliedReadPreference;

      private ClusterBindingConnectionSource(ServerTuple serverTuple, ReadPreference appliedReadPreference) {
         this.server = serverTuple.getServer();
         this.serverDescription = serverTuple.getServerDescription();
         this.appliedReadPreference = appliedReadPreference;
         ClusterBinding.this.retain();
      }

      public ServerDescription getServerDescription() {
         return this.serverDescription;
      }

      public SessionContext getSessionContext() {
         return new ReadConcernAwareNoOpSessionContext(ClusterBinding.this.readConcern);
      }

      public OperationContext getOperationContext() {
         return ClusterBinding.this.operationContext;
      }

      public ServerApi getServerApi() {
         return ClusterBinding.this.serverApi;
      }

      public RequestContext getRequestContext() {
         return ClusterBinding.this.requestContext;
      }

      public ReadPreference getReadPreference() {
         return this.appliedReadPreference;
      }

      public Connection getConnection() {
         return this.server.getConnection(ClusterBinding.this.operationContext);
      }

      public ConnectionSource retain() {
         super.retain();
         ClusterBinding.this.retain();
         return this;
      }

      public int release() {
         int count = super.release();
         ClusterBinding.this.release();
         return count;
      }

      // $FF: synthetic method
      ClusterBindingConnectionSource(ServerTuple x1, ReadPreference x2, Object x3) {
         this(x1, x2);
      }
   }
}
