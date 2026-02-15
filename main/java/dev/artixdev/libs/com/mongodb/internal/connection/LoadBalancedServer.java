package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.concurrent.atomic.AtomicBoolean;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoNodeIsRecoveringException;
import dev.artixdev.libs.com.mongodb.MongoNotPrimaryException;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.MongoSocketReadTimeoutException;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ServerConnectionState;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.event.ServerClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerListener;
import dev.artixdev.libs.com.mongodb.event.ServerOpeningEvent;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.types.ObjectId;

@ThreadSafe
public class LoadBalancedServer implements ClusterableServer {
   private static final Logger LOGGER = Loggers.getLogger("connection");
   private final AtomicBoolean closed = new AtomicBoolean();
   private final ServerId serverId;
   private final ConnectionPool connectionPool;
   private final ConnectionFactory connectionFactory;
   private final ServerListener serverListener;
   private final ClusterClock clusterClock;

   public LoadBalancedServer(ServerId serverId, ConnectionPool connectionPool, ConnectionFactory connectionFactory, ServerListener serverListener, ClusterClock clusterClock) {
      this.serverId = serverId;
      this.connectionPool = connectionPool;
      this.connectionFactory = connectionFactory;
      this.serverListener = serverListener;
      this.clusterClock = clusterClock;
      serverListener.serverOpening(new ServerOpeningEvent(serverId));
      serverListener.serverDescriptionChanged(new ServerDescriptionChangedEvent(serverId, ServerDescription.builder().ok(true).state(ServerConnectionState.CONNECTED).type(ServerType.LOAD_BALANCER).address(serverId.getAddress()).build(), ServerDescription.builder().address(serverId.getAddress()).state(ServerConnectionState.CONNECTING).build()));
   }

   public void resetToConnecting() {
   }

   public void invalidate() {
   }

   private void invalidate(Throwable t, @Nullable ObjectId serviceId, int generation) {
      if (!this.isClosed()) {
         if (t instanceof MongoSocketException && !(t instanceof MongoSocketReadTimeoutException)) {
            if (serviceId != null) {
               this.connectionPool.invalidate(serviceId, generation);
            }
         } else if ((t instanceof MongoNotPrimaryException || t instanceof MongoNodeIsRecoveringException) && SHUTDOWN_CODES.contains(((MongoCommandException)t).getErrorCode()) && serviceId != null) {
            this.connectionPool.invalidate(serviceId, generation);
         }
      }

   }

   public void close() {
      if (!this.closed.getAndSet(true)) {
         this.connectionPool.close();
         this.serverListener.serverClosed(new ServerClosedEvent(this.serverId));
      }

   }

   public boolean isClosed() {
      return this.closed.get();
   }

   public void connect() {
   }

   public Connection getConnection(OperationContext operationContext) {
      Assertions.isTrue("open", !this.isClosed());
      return this.connectionFactory.create(this.connectionPool.get(operationContext), new LoadBalancedServer.LoadBalancedServerProtocolExecutor(), ClusterConnectionMode.LOAD_BALANCED);
   }

   public void getConnectionAsync(OperationContext operationContext, SingleResultCallback<AsyncConnection> callback) {
      Assertions.isTrue("open", !this.isClosed());
      this.connectionPool.getAsync(operationContext, (result, t) -> {
         if (t != null) {
            callback.onResult(null, t);
         } else {
            callback.onResult(this.connectionFactory.createAsync(result, new LoadBalancedServer.LoadBalancedServerProtocolExecutor(), ClusterConnectionMode.LOAD_BALANCED), (Throwable)null);
         }

      });
   }

   public int operationCount() {
      return -1;
   }

   ConnectionPool getConnectionPool() {
      return this.connectionPool;
   }

   private class LoadBalancedServerProtocolExecutor implements ProtocolExecutor {
      private LoadBalancedServerProtocolExecutor() {
      }

      public <T> T execute(CommandProtocol<T> protocol, InternalConnection connection, SessionContext sessionContext) {
         try {
            protocol.sessionContext(new ClusterClockAdvancingSessionContext(sessionContext, LoadBalancedServer.this.clusterClock));
            return protocol.execute(connection);
         } catch (MongoWriteConcernWithResponseException e) {
            return (T) e.getResponse();
         } catch (MongoException e) {
            this.handleExecutionException(connection, sessionContext, e);
            throw e;
         }
      }

      public <T> void executeAsync(CommandProtocol<T> protocol, InternalConnection connection, SessionContext sessionContext, SingleResultCallback<T> callback) {
         protocol.sessionContext(new ClusterClockAdvancingSessionContext(sessionContext, LoadBalancedServer.this.clusterClock));
         protocol.executeAsync(connection, ErrorHandlingResultCallback.errorHandlingCallback((result, t) -> {
            if (t != null) {
               if (t instanceof MongoWriteConcernWithResponseException) {
                  callback.onResult((T) ((MongoWriteConcernWithResponseException)t).getResponse(), null);
               } else {
                  this.handleExecutionException(connection, sessionContext, t);
                  callback.onResult(null, t);
               }
            } else {
               callback.onResult(result, (Throwable)null);
            }

         }, LoadBalancedServer.LOGGER));
      }

      private void handleExecutionException(InternalConnection connection, SessionContext sessionContext, Throwable t) {
         LoadBalancedServer.this.invalidate(t, connection.getDescription().getServiceId(), connection.getGeneration());
         if (t instanceof MongoSocketException && sessionContext.hasSession()) {
            sessionContext.markSessionDirty();
         }

      }

      // $FF: synthetic method
      LoadBalancedServerProtocolExecutor(Object x1) {
         this();
      }
   }
}
