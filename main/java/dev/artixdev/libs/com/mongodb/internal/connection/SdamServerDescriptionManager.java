package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Optional;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoNodeIsRecoveringException;
import dev.artixdev.libs.com.mongodb.MongoNotPrimaryException;
import dev.artixdev.libs.com.mongodb.MongoSecurityException;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.MongoSocketReadTimeoutException;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@ThreadSafe
interface SdamServerDescriptionManager {
   void update(ServerDescription var1);

   void handleExceptionBeforeHandshake(SdamServerDescriptionManager.SdamIssue var1);

   void handleExceptionAfterHandshake(SdamServerDescriptionManager.SdamIssue var1);

   SdamServerDescriptionManager.SdamIssue.Context context();

   SdamServerDescriptionManager.SdamIssue.Context context(InternalConnection var1);

   @ThreadSafe
   public static final class SdamIssue {
      @Nullable
      private final Throwable exception;
      private final SdamServerDescriptionManager.SdamIssue.Context context;

      private SdamIssue(@Nullable Throwable exception, SdamServerDescriptionManager.SdamIssue.Context context) {
         this.exception = exception;
         this.context = (SdamServerDescriptionManager.SdamIssue.Context)Assertions.assertNotNull(context);
      }

      static SdamServerDescriptionManager.SdamIssue specific(Throwable exception, SdamServerDescriptionManager.SdamIssue.Context context) {
         return new SdamServerDescriptionManager.SdamIssue((Throwable)Assertions.assertNotNull(exception), (SdamServerDescriptionManager.SdamIssue.Context)Assertions.assertNotNull(context));
      }

      static SdamServerDescriptionManager.SdamIssue unspecified(SdamServerDescriptionManager.SdamIssue.Context context) {
         return new SdamServerDescriptionManager.SdamIssue((Throwable)null, (SdamServerDescriptionManager.SdamIssue.Context)Assertions.assertNotNull(context));
      }

      Optional<Throwable> exception() {
         return Optional.ofNullable(this.exception);
      }

      boolean specific() {
         return this.exception != null;
      }

      ServerDescription serverDescription() {
         return ServerDescriptionHelper.unknownConnectingServerDescription(this.context.serverId(), this.exception);
      }

      boolean serverIsLessThanVersionFourDotTwo() {
         return this.context.serverIsLessThanVersionFourDotTwo();
      }

      boolean stale(ConnectionPool connectionPool, ServerDescription currentServerDescription) {
         return this.context.stale(connectionPool) || stale(this.exception, currentServerDescription);
      }

      boolean relatedToStateChange() {
         return this.exception instanceof MongoNotPrimaryException || this.exception instanceof MongoNodeIsRecoveringException;
      }

      boolean relatedToShutdown() {
         Assertions.assertTrue(this.relatedToStateChange());
         return this.exception instanceof MongoCommandException ? ClusterableServer.SHUTDOWN_CODES.contains(((MongoCommandException)this.exception).getErrorCode()) : false;
      }

      boolean relatedToNetworkTimeout() {
         return this.exception instanceof MongoSocketReadTimeoutException;
      }

      boolean relatedToNetworkNotTimeout() {
         return this.exception instanceof MongoSocketException && !this.relatedToNetworkTimeout();
      }

      boolean relatedToAuth() {
         return this.exception instanceof MongoSecurityException;
      }

      boolean relatedToWriteConcern() {
         return this.exception instanceof MongoWriteConcernWithResponseException;
      }

      private static boolean stale(@Nullable Throwable t, ServerDescription currentServerDescription) {
         return (Boolean)TopologyVersionHelper.topologyVersion(t).map((candidateTopologyVersion) -> {
            return TopologyVersionHelper.newerOrEqual(currentServerDescription.getTopologyVersion(), candidateTopologyVersion);
         }).orElse(false);
      }

      @Immutable
      static final class Context {
         private final ServerId serverId;
         private final int connectionPoolGeneration;
         private final int serverMaxWireVersion;

         Context(ServerId serverId, int connectionPoolGeneration, int serverMaxWireVersion) {
            this.serverId = (ServerId)Assertions.assertNotNull(serverId);
            this.connectionPoolGeneration = connectionPoolGeneration;
            this.serverMaxWireVersion = serverMaxWireVersion;
         }

         private boolean serverIsLessThanVersionFourDotTwo() {
            return this.serverMaxWireVersion < 8;
         }

         private boolean stale(ConnectionPool connectionPool) {
            return this.connectionPoolGeneration < connectionPool.getGeneration();
         }

         private ServerId serverId() {
            return this.serverId;
         }
      }
   }
}
