package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.concurrent.atomic.AtomicInteger;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoServerUnavailableException;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.event.ServerClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerListener;
import dev.artixdev.libs.com.mongodb.event.ServerOpeningEvent;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.BindingContext;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.Decoder;

class DefaultServer implements ClusterableServer {
   private static final Logger LOGGER = Loggers.getLogger("connection");
   private final ServerId serverId;
   private final ConnectionPool connectionPool;
   private final ClusterConnectionMode clusterConnectionMode;
   private final ConnectionFactory connectionFactory;
   private final ServerMonitor serverMonitor;
   private final SdamServerDescriptionManager sdam;
   private final ServerListener serverListener;
   private final CommandListener commandListener;
   private final ClusterClock clusterClock;
   @Nullable
   private final AtomicInteger operationCount;
   private volatile boolean isClosed;

   DefaultServer(ServerId serverId, ClusterConnectionMode clusterConnectionMode, ConnectionPool connectionPool, ConnectionFactory connectionFactory, ServerMonitor serverMonitor, SdamServerDescriptionManager sdam, ServerListener serverListener, CommandListener commandListener, ClusterClock clusterClock, boolean trackOperationCount) {
      this.sdam = (SdamServerDescriptionManager)Assertions.assertNotNull(sdam);
      this.serverListener = (ServerListener)Assertions.notNull("serverListener", serverListener);
      this.commandListener = commandListener;
      this.clusterClock = (ClusterClock)Assertions.notNull("clusterClock", clusterClock);
      Assertions.notNull("serverAddress", serverId);
      this.clusterConnectionMode = (ClusterConnectionMode)Assertions.notNull("clusterConnectionMode", clusterConnectionMode);
      this.connectionFactory = (ConnectionFactory)Assertions.notNull("connectionFactory", connectionFactory);
      this.connectionPool = (ConnectionPool)Assertions.notNull("connectionPool", connectionPool);
      this.serverId = serverId;
      serverListener.serverOpening(new ServerOpeningEvent(this.serverId));
      this.serverMonitor = serverMonitor;
      this.operationCount = trackOperationCount ? new AtomicInteger() : null;
   }

   public Connection getConnection(OperationContext operationContext) {
      if (this.isClosed) {
         throw new MongoServerUnavailableException(String.format("The server at %s is no longer available", this.serverId.getAddress()));
      } else {
         SdamServerDescriptionManager.SdamIssue.Context exceptionContext = this.sdam.context();
         this.operationBegin();

         try {
            return DefaultServer.OperationCountTrackingConnection.decorate(this, this.connectionFactory.create(this.connectionPool.get(operationContext), new DefaultServer.DefaultServerProtocolExecutor(), this.clusterConnectionMode));
         } catch (Throwable throwable) {
            Throwable e = throwable;

            try {
               this.operationEnd();
               if (e instanceof MongoException) {
                  this.sdam.handleExceptionBeforeHandshake(SdamServerDescriptionManager.SdamIssue.specific(e, exceptionContext));
               }
            } catch (Exception suppressed) {
               throwable.addSuppressed(suppressed);
            }

            throw throwable;
         }
      }
   }

   public void getConnectionAsync(OperationContext operationContext, SingleResultCallback<AsyncConnection> callback) {
      if (this.isClosed) {
         callback.onResult(null, new MongoServerUnavailableException(String.format("The server at %s is no longer available", this.serverId.getAddress())));
      } else {
         SdamServerDescriptionManager.SdamIssue.Context exceptionContext = this.sdam.context();
         this.operationBegin();
         this.connectionPool.getAsync(operationContext, (result, t) -> {
            if (t != null) {
               try {
                  this.operationEnd();
                  this.sdam.handleExceptionBeforeHandshake(SdamServerDescriptionManager.SdamIssue.specific(t, exceptionContext));
               } catch (Exception suppressed) {
                  t.addSuppressed(suppressed);
               } finally {
                  callback.onResult(null, t);
               }
            } else {
               callback.onResult(DefaultServer.AsyncOperationCountTrackingConnection.decorate(this, this.connectionFactory.createAsync((InternalConnection)Assertions.assertNotNull(result), new DefaultServer.DefaultServerProtocolExecutor(), this.clusterConnectionMode)), (Throwable)null);
            }

         });
      }
   }

   public int operationCount() {
      return this.operationCount == null ? -1 : this.operationCount.get();
   }

   private void operationBegin() {
      if (this.operationCount != null) {
         this.operationCount.incrementAndGet();
      }

   }

   private void operationEnd() {
      if (this.operationCount != null) {
         Assertions.assertTrue(this.operationCount.decrementAndGet() >= 0);
      }

   }

   public void resetToConnecting() {
      this.sdam.update(ServerDescriptionHelper.unknownConnectingServerDescription(this.serverId, (Throwable)null));
   }

   public void invalidate() {
      if (!this.isClosed()) {
         this.sdam.handleExceptionAfterHandshake(SdamServerDescriptionManager.SdamIssue.unspecified(this.sdam.context()));
      }

   }

   public void close() {
      if (!this.isClosed()) {
         this.connectionPool.close();
         this.serverMonitor.close();
         this.isClosed = true;
         this.serverListener.serverClosed(new ServerClosedEvent(this.serverId));
      }

   }

   public boolean isClosed() {
      return this.isClosed;
   }

   public void connect() {
      this.serverMonitor.connect();
   }

   ConnectionPool getConnectionPool() {
      return this.connectionPool;
   }

   SdamServerDescriptionManager sdamServerDescriptionManager() {
      return this.sdam;
   }

   ServerId serverId() {
      return this.serverId;
   }

   private class DefaultServerProtocolExecutor implements ProtocolExecutor {
      private DefaultServerProtocolExecutor() {
      }

      public <T> T execute(CommandProtocol<T> protocol, InternalConnection connection, SessionContext sessionContext) {
         try {
            protocol.sessionContext(new ClusterClockAdvancingSessionContext(sessionContext, DefaultServer.this.clusterClock));
            return protocol.execute(connection);
         } catch (MongoException mongoException) {
            MongoException e = mongoException;

            try {
               DefaultServer.this.sdam.handleExceptionAfterHandshake(SdamServerDescriptionManager.SdamIssue.specific(e, DefaultServer.this.sdam.context(connection)));
            } catch (Exception suppressed) {
               mongoException.addSuppressed(suppressed);
            }

            if (mongoException instanceof MongoWriteConcernWithResponseException) {
               return (T) ((MongoWriteConcernWithResponseException)mongoException).getResponse();
            } else {
               if (mongoException instanceof MongoSocketException && sessionContext.hasSession()) {
                  sessionContext.markSessionDirty();
               }

               throw mongoException;
            }
         }
      }

      public <T> void executeAsync(CommandProtocol<T> protocol, InternalConnection connection, SessionContext sessionContext, SingleResultCallback<T> callback) {
         protocol.sessionContext(new ClusterClockAdvancingSessionContext(sessionContext, DefaultServer.this.clusterClock));
         protocol.executeAsync(connection, ErrorHandlingResultCallback.errorHandlingCallback((result, t) -> {
            if (t != null) {
               try {
                  DefaultServer.this.sdam.handleExceptionAfterHandshake(SdamServerDescriptionManager.SdamIssue.specific(t, DefaultServer.this.sdam.context(connection)));
               } catch (Exception suppressed) {
                  t.addSuppressed(suppressed);
               } finally {
                  if (t instanceof MongoWriteConcernWithResponseException) {
                     callback.onResult((T) ((MongoWriteConcernWithResponseException)t).getResponse(), null);
                  } else {
                     if (t instanceof MongoSocketException && sessionContext.hasSession()) {
                        sessionContext.markSessionDirty();
                     }

                     callback.onResult(null, t);
                  }

               }
            } else {
               callback.onResult(result, (Throwable)null);
            }

         }, DefaultServer.LOGGER));
      }

      // $FF: synthetic method
      DefaultServerProtocolExecutor(Object x1) {
         this();
      }
   }

   private static final class OperationCountTrackingConnection implements Connection {
      private final DefaultServer server;
      private final Connection wrapped;

      static Connection decorate(DefaultServer server, Connection connection) {
         return (Connection)(server.operationCount() < 0 ? connection : new DefaultServer.OperationCountTrackingConnection(server, connection));
      }

      private OperationCountTrackingConnection(DefaultServer server, Connection connection) {
         this.server = server;
         this.wrapped = connection;
      }

      public int getCount() {
         return this.wrapped.getCount();
      }

      public int release() {
         int count = this.wrapped.release();
         if (count == 0) {
            this.server.operationEnd();
         }

         return count;
      }

      public Connection retain() {
         this.wrapped.retain();
         return this;
      }

      public ConnectionDescription getDescription() {
         return this.wrapped.getDescription();
      }

      public <T> T command(String database, BsonDocument command, FieldNameValidator fieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, BindingContext context) {
         return this.wrapped.command(database, command, fieldNameValidator, readPreference, commandResultDecoder, context);
      }

      public <T> T command(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, BindingContext context, boolean responseExpected, @Nullable SplittablePayload payload, @Nullable FieldNameValidator payloadFieldNameValidator) {
         return this.wrapped.command(database, command, commandFieldNameValidator, readPreference, commandResultDecoder, context, responseExpected, payload, payloadFieldNameValidator);
      }

      public void markAsPinned(Connection.PinningMode pinningMode) {
         this.wrapped.markAsPinned(pinningMode);
      }
   }

   private static final class AsyncOperationCountTrackingConnection implements AsyncConnection {
      private final DefaultServer server;
      private final AsyncConnection wrapped;

      static AsyncConnection decorate(DefaultServer server, AsyncConnection connection) {
         return (AsyncConnection)(server.operationCount() < 0 ? connection : new DefaultServer.AsyncOperationCountTrackingConnection(server, connection));
      }

      AsyncOperationCountTrackingConnection(DefaultServer server, AsyncConnection connection) {
         this.server = server;
         this.wrapped = connection;
      }

      public int getCount() {
         return this.wrapped.getCount();
      }

      public int release() {
         int count = this.wrapped.release();
         if (count == 0) {
            this.server.operationEnd();
         }

         return count;
      }

      public AsyncConnection retain() {
         this.wrapped.retain();
         return this;
      }

      public ConnectionDescription getDescription() {
         return this.wrapped.getDescription();
      }

      public <T> void commandAsync(String database, BsonDocument command, FieldNameValidator fieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, BindingContext context, SingleResultCallback<T> callback) {
         this.wrapped.commandAsync(database, command, fieldNameValidator, readPreference, commandResultDecoder, context, callback);
      }

      public <T> void commandAsync(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, @Nullable ReadPreference readPreference, Decoder<T> commandResultDecoder, BindingContext context, boolean responseExpected, @Nullable SplittablePayload payload, @Nullable FieldNameValidator payloadFieldNameValidator, SingleResultCallback<T> callback) {
         this.wrapped.commandAsync(database, command, commandFieldNameValidator, readPreference, commandResultDecoder, context, responseExpected, payload, payloadFieldNameValidator, callback);
      }

      public void markAsPinned(Connection.PinningMode pinningMode) {
         this.wrapped.markAsPinned(pinningMode);
      }
   }
}
