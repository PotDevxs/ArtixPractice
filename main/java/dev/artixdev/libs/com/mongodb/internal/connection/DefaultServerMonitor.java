package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.connection.ServerSettings;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.connection.TopologyVersion;
import dev.artixdev.libs.com.mongodb.event.ServerHeartbeatFailedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerHeartbeatStartedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerHeartbeatSucceededEvent;
import dev.artixdev.libs.com.mongodb.event.ServerMonitorListener;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.event.EventListenerHelper;
import dev.artixdev.libs.com.mongodb.internal.inject.Provider;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.types.ObjectId;

@ThreadSafe
class DefaultServerMonitor implements ServerMonitor {
   private static final Logger LOGGER = Loggers.getLogger("cluster");
   private final ServerId serverId;
   private final ServerMonitorListener serverMonitorListener;
   private final ClusterClock clusterClock;
   private final Provider<SdamServerDescriptionManager> sdamProvider;
   private final InternalConnectionFactory internalConnectionFactory;
   private final ClusterConnectionMode clusterConnectionMode;
   @Nullable
   private final ServerApi serverApi;
   private final ServerSettings serverSettings;
   private final DefaultServerMonitor.ServerMonitorRunnable monitor;
   private final Thread monitorThread;
   private final DefaultServerMonitor.RoundTripTimeRunnable roundTripTimeMonitor;
   private final ExponentiallyWeightedMovingAverage averageRoundTripTime = new ExponentiallyWeightedMovingAverage(0.2D);
   private final Thread roundTripTimeMonitorThread;
   private final Lock lock = new ReentrantLock();
   private final Condition condition;
   private volatile boolean isClosed;

   DefaultServerMonitor(ServerId serverId, ServerSettings serverSettings, ClusterClock clusterClock, InternalConnectionFactory internalConnectionFactory, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi, Provider<SdamServerDescriptionManager> sdamProvider) {
      this.condition = this.lock.newCondition();
      this.serverSettings = (ServerSettings)Assertions.notNull("serverSettings", serverSettings);
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
      this.serverMonitorListener = EventListenerHelper.singleServerMonitorListener(serverSettings);
      this.clusterClock = (ClusterClock)Assertions.notNull("clusterClock", clusterClock);
      this.internalConnectionFactory = (InternalConnectionFactory)Assertions.notNull("internalConnectionFactory", internalConnectionFactory);
      this.clusterConnectionMode = (ClusterConnectionMode)Assertions.notNull("clusterConnectionMode", clusterConnectionMode);
      this.serverApi = serverApi;
      this.sdamProvider = sdamProvider;
      this.monitor = new DefaultServerMonitor.ServerMonitorRunnable();
      this.monitorThread = new Thread(this.monitor, "cluster-" + this.serverId.getClusterId() + "-" + this.serverId.getAddress());
      this.monitorThread.setDaemon(true);
      this.roundTripTimeMonitor = new DefaultServerMonitor.RoundTripTimeRunnable();
      this.roundTripTimeMonitorThread = new Thread(this.roundTripTimeMonitor, "cluster-rtt-" + this.serverId.getClusterId() + "-" + this.serverId.getAddress());
      this.roundTripTimeMonitorThread.setDaemon(true);
      this.isClosed = false;
   }

   public void start() {
      this.monitorThread.start();
      this.roundTripTimeMonitorThread.start();
   }

   public void connect() {
      Lock var10000 = this.lock;
      Condition var10001 = this.condition;
      Objects.requireNonNull(var10001);
      Locks.withLock(var10000, var10001::signal);
   }

   public void close() {
      this.isClosed = true;
      this.monitor.close();
      this.monitorThread.interrupt();
      this.roundTripTimeMonitor.close();
      this.roundTripTimeMonitorThread.interrupt();
   }

   public void cancelCurrentCheck() {
      this.monitor.cancelCurrentCheck();
   }

   static boolean shouldLogStageChange(ServerDescription previous, ServerDescription current) {
      if (previous.isOk() != current.isOk()) {
         return true;
      } else if (!previous.getAddress().equals(current.getAddress())) {
         return true;
      } else {
         label124: {
            String previousCanonicalAddress = previous.getCanonicalAddress();
            if (previousCanonicalAddress != null) {
               if (previousCanonicalAddress.equals(current.getCanonicalAddress())) {
                  break label124;
               }
            } else if (current.getCanonicalAddress() == null) {
               break label124;
            }

            return true;
         }

         if (!previous.getHosts().equals(current.getHosts())) {
            return true;
         } else if (!previous.getArbiters().equals(current.getArbiters())) {
            return true;
         } else if (!previous.getPassives().equals(current.getPassives())) {
            return true;
         } else {
            String previousPrimary = previous.getPrimary();
            if (previousPrimary != null) {
               if (!previousPrimary.equals(current.getPrimary())) {
                  return true;
               }
            } else if (current.getPrimary() != null) {
               return true;
            }

            label106: {
               String previousSetName = previous.getSetName();
               if (previousSetName != null) {
                  if (previousSetName.equals(current.getSetName())) {
                     break label106;
                  }
               } else if (current.getSetName() == null) {
                  break label106;
               }

               return true;
            }

            if (previous.getState() != current.getState()) {
               return true;
            } else if (!previous.getTagSet().equals(current.getTagSet())) {
               return true;
            } else if (previous.getType() != current.getType()) {
               return true;
            } else if (previous.getMaxWireVersion() != current.getMaxWireVersion()) {
               return true;
            } else {
               ObjectId previousElectionId = previous.getElectionId();
               if (previousElectionId != null) {
                  if (!previousElectionId.equals(current.getElectionId())) {
                     return true;
                  }
               } else if (current.getElectionId() != null) {
                  return true;
               }

               label86: {
                  Integer setVersion = previous.getSetVersion();
                  if (setVersion != null) {
                     if (setVersion.equals(current.getSetVersion())) {
                        break label86;
                     }
                  } else if (current.getSetVersion() == null) {
                     break label86;
                  }

                  return true;
               }

               Throwable previousException = previous.getException();
               Throwable currentException = current.getException();
               Class<?> thisExceptionClass = previousException != null ? previousException.getClass() : null;
               Class<?> thatExceptionClass = currentException != null ? currentException.getClass() : null;
               if (!Objects.equals(thisExceptionClass, thatExceptionClass)) {
                  return true;
               } else {
                  String thisExceptionMessage = previousException != null ? previousException.getMessage() : null;
                  String thatExceptionMessage = currentException != null ? currentException.getMessage() : null;
                  if (!Objects.equals(thisExceptionMessage, thatExceptionMessage)) {
                     return true;
                  } else {
                     return false;
                  }
               }
            }
         }
      }
   }

   private void waitForNext() throws InterruptedException {
      Thread.sleep(this.serverSettings.getHeartbeatFrequency(TimeUnit.MILLISECONDS));
   }

   private String getHandshakeCommandName(ServerDescription serverDescription) {
      return serverDescription.isHelloOk() ? "hello" : "isMaster";
   }

   class ServerMonitorRunnable implements Runnable {
      private volatile InternalConnection connection = null;
      private volatile boolean currentCheckCancelled;

      void close() {
         InternalConnection connection = this.connection;
         if (connection != null) {
            connection.close();
         }

      }

      public void run() {
         ServerDescription currentServerDescription = ServerDescriptionHelper.unknownConnectingServerDescription(DefaultServerMonitor.this.serverId, (Throwable)null);

         try {
            while(!DefaultServerMonitor.this.isClosed) {
               ServerDescription previousServerDescription = currentServerDescription;
               currentServerDescription = this.lookupServerDescription(currentServerDescription);
               if (!DefaultServerMonitor.this.isClosed) {
                  if (this.currentCheckCancelled) {
                     this.waitForNext();
                     this.currentCheckCancelled = false;
                  } else {
                     this.logStateChange(previousServerDescription, currentServerDescription);
                     ((SdamServerDescriptionManager)DefaultServerMonitor.this.sdamProvider.get()).update(currentServerDescription);
                     if ((this.connection != null && !this.shouldStreamResponses(currentServerDescription) || currentServerDescription.getTopologyVersion() == null || currentServerDescription.getType() == ServerType.UNKNOWN) && (this.connection == null || !this.connection.hasMoreToCome()) && (!(currentServerDescription.getException() instanceof MongoSocketException) || previousServerDescription.getType() == ServerType.UNKNOWN)) {
                        this.waitForNext();
                     }
                  }
               }
            }
         } catch (MongoInterruptedException | InterruptedException ignored) {
         } catch (RuntimeException e) {
            DefaultServerMonitor.LOGGER.error(String.format("Server monitor for %s exiting with exception", DefaultServerMonitor.this.serverId), e);
         } finally {
            if (this.connection != null) {
               this.connection.close();
            }

         }

      }

      private ServerDescription lookupServerDescription(ServerDescription currentServerDescription) {
         try {
            if (this.connection != null && !this.connection.isClosed()) {
               if (DefaultServerMonitor.LOGGER.isDebugEnabled()) {
                  DefaultServerMonitor.LOGGER.debug(String.format("Checking status of %s", DefaultServerMonitor.this.serverId.getAddress()));
               }

               DefaultServerMonitor.this.serverMonitorListener.serverHearbeatStarted(new ServerHeartbeatStartedEvent(this.connection.getDescription().getConnectionId()));
               long start = System.nanoTime();

               try {
                  SessionContext sessionContext = new ClusterClockAdvancingSessionContext(NoOpSessionContext.INSTANCE, DefaultServerMonitor.this.clusterClock);
                  BsonDocument helloResult;
                  if (!this.connection.hasMoreToCome()) {
                     helloResult = (new BsonDocument(DefaultServerMonitor.this.getHandshakeCommandName(currentServerDescription), new BsonInt32(1))).append("helloOk", BsonBoolean.TRUE);
                     if (this.shouldStreamResponses(currentServerDescription)) {
                        helloResult.append("topologyVersion", ((TopologyVersion)Assertions.assertNotNull(currentServerDescription.getTopologyVersion())).asDocument());
                        helloResult.append("maxAwaitTimeMS", new BsonInt64(DefaultServerMonitor.this.serverSettings.getHeartbeatFrequency(TimeUnit.MILLISECONDS)));
                     }

                     this.connection.send(this.createCommandMessage(helloResult, this.connection, currentServerDescription), new BsonDocumentCodec(), sessionContext);
                  }

                  if (this.shouldStreamResponses(currentServerDescription)) {
                     helloResult = (BsonDocument)this.connection.receive(new BsonDocumentCodec(), sessionContext, Math.toIntExact(DefaultServerMonitor.this.serverSettings.getHeartbeatFrequency(TimeUnit.MILLISECONDS)));
                  } else {
                     helloResult = (BsonDocument)this.connection.receive(new BsonDocumentCodec(), sessionContext);
                  }

                  long elapsedTimeNanos = System.nanoTime() - start;
                  DefaultServerMonitor.this.serverMonitorListener.serverHeartbeatSucceeded(new ServerHeartbeatSucceededEvent(this.connection.getDescription().getConnectionId(), helloResult, elapsedTimeNanos, currentServerDescription.getTopologyVersion() != null));
                  return DescriptionHelper.createServerDescription(DefaultServerMonitor.this.serverId.getAddress(), helloResult, DefaultServerMonitor.this.averageRoundTripTime.getAverage());
               } catch (Exception e) {
                  DefaultServerMonitor.this.serverMonitorListener.serverHeartbeatFailed(new ServerHeartbeatFailedEvent(this.connection.getDescription().getConnectionId(), System.nanoTime() - start, currentServerDescription.getTopologyVersion() != null, e));
                  throw e;
               }
            } else {
               this.currentCheckCancelled = false;
               InternalConnection newConnection = DefaultServerMonitor.this.internalConnectionFactory.create(DefaultServerMonitor.this.serverId);
               newConnection.open();
               this.connection = newConnection;
               DefaultServerMonitor.this.averageRoundTripTime.addSample(this.connection.getInitialServerDescription().getRoundTripTimeNanos());
               return this.connection.getInitialServerDescription();
            }
         } catch (Throwable e) {
            DefaultServerMonitor.this.averageRoundTripTime.reset();
            InternalConnection localConnection = (InternalConnection)Locks.withLock(DefaultServerMonitor.this.lock, () -> {
               InternalConnection result = this.connection;
               this.connection = null;
               return result;
            });
            if (localConnection != null) {
               localConnection.close();
            }

            return ServerDescriptionHelper.unknownConnectingServerDescription(DefaultServerMonitor.this.serverId, e);
         }
      }

      private boolean shouldStreamResponses(ServerDescription currentServerDescription) {
         return currentServerDescription.getTopologyVersion() != null && this.connection.supportsAdditionalTimeout();
      }

      private CommandMessage createCommandMessage(BsonDocument command, InternalConnection connection, ServerDescription currentServerDescription) {
         return new CommandMessage(new MongoNamespace("admin", "$cmd"), command, new NoOpFieldNameValidator(), ReadPreference.primary(), MessageSettings.builder().maxWireVersion(connection.getDescription().getMaxWireVersion()).build(), this.shouldStreamResponses(currentServerDescription), DefaultServerMonitor.this.clusterConnectionMode, DefaultServerMonitor.this.serverApi);
      }

      private void logStateChange(ServerDescription previousServerDescription, ServerDescription currentServerDescription) {
         if (DefaultServerMonitor.shouldLogStageChange(previousServerDescription, currentServerDescription)) {
            if (currentServerDescription.getException() != null) {
               DefaultServerMonitor.LOGGER.info(String.format("Exception in monitor thread while connecting to server %s", DefaultServerMonitor.this.serverId.getAddress()), (Throwable)Assertions.assertNotNull(currentServerDescription.getException()));
            } else {
               DefaultServerMonitor.LOGGER.info(String.format("Monitor thread successfully connected to server with description %s", currentServerDescription));
            }
         }

      }

      private void waitForNext() throws InterruptedException {
         long timeRemaining = this.waitForSignalOrTimeout();
         if (timeRemaining > 0L) {
            long timeWaiting = DefaultServerMonitor.this.serverSettings.getHeartbeatFrequency(TimeUnit.NANOSECONDS) - timeRemaining;
            long minimumNanosToWait = DefaultServerMonitor.this.serverSettings.getMinHeartbeatFrequency(TimeUnit.NANOSECONDS);
            if (timeWaiting < minimumNanosToWait) {
               long millisToSleep = TimeUnit.MILLISECONDS.convert(minimumNanosToWait - timeWaiting, TimeUnit.NANOSECONDS);
               if (millisToSleep > 0L) {
                  Thread.sleep(millisToSleep);
               }
            }
         }

      }

      private long waitForSignalOrTimeout() throws InterruptedException {
         return (Long)Locks.checkedWithLock(DefaultServerMonitor.this.lock, () -> {
            return DefaultServerMonitor.this.condition.awaitNanos(DefaultServerMonitor.this.serverSettings.getHeartbeatFrequency(TimeUnit.NANOSECONDS));
         });
      }

      public void cancelCurrentCheck() {
         InternalConnection localConnection = (InternalConnection)Locks.withLock(DefaultServerMonitor.this.lock, () -> {
            if (this.connection != null && !this.currentCheckCancelled) {
               InternalConnection result = this.connection;
               this.currentCheckCancelled = true;
               return result;
            } else {
               return null;
            }
         });
         if (localConnection != null) {
            localConnection.close();
         }

      }
   }

   private class RoundTripTimeRunnable implements Runnable {
      private volatile InternalConnection connection;

      private RoundTripTimeRunnable() {
         this.connection = null;
      }

      void close() {
         InternalConnection connection = this.connection;
         if (connection != null) {
            connection.close();
         }

      }

      public void run() {
         while(true) {
            try {
               if (!DefaultServerMonitor.this.isClosed) {
                  try {
                     if (this.connection == null) {
                        this.initialize();
                     } else {
                        this.pingServer(this.connection);
                     }
                  } catch (Throwable ignored) {
                     if (this.connection != null) {
                        this.connection.close();
                        this.connection = null;
                     }
                  }

                  DefaultServerMonitor.this.waitForNext();
                  continue;
               }
            } catch (InterruptedException ignored) {
            } finally {
               if (this.connection != null) {
                  this.connection.close();
               }

            }

            return;
         }
      }

      private void initialize() {
         this.connection = null;
         this.connection = DefaultServerMonitor.this.internalConnectionFactory.create(DefaultServerMonitor.this.serverId);
         this.connection.open();
         DefaultServerMonitor.this.averageRoundTripTime.addSample(this.connection.getInitialServerDescription().getRoundTripTimeNanos());
      }

      private void pingServer(InternalConnection connection) {
         long start = System.nanoTime();
         CommandHelper.executeCommand("admin", new BsonDocument(DefaultServerMonitor.this.getHandshakeCommandName(connection.getInitialServerDescription()), new BsonInt32(1)), DefaultServerMonitor.this.clusterClock, DefaultServerMonitor.this.clusterConnectionMode, DefaultServerMonitor.this.serverApi, connection);
         long elapsedTimeNanos = System.nanoTime() - start;
         DefaultServerMonitor.this.averageRoundTripTime.addSample(elapsedTimeNanos);
      }

      // $FF: synthetic method
      RoundTripTimeRunnable(Object x1) {
         this();
      }
   }
}
