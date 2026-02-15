package dev.artixdev.libs.com.mongodb.internal.connection;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.MongoConnectionPoolClearedException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.MongoTimeoutException;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;
import dev.artixdev.libs.com.mongodb.connection.ConnectionPoolSettings;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.event.ConnectionAddedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionCheckOutFailedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionCheckOutStartedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionCheckedInEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionCheckedOutEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionCreatedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolClearedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolCreatedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolListener;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolOpenedEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolReadyEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionReadyEvent;
import dev.artixdev.libs.com.mongodb.event.ConnectionRemovedEvent;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.event.EventListenerHelper;
import dev.artixdev.libs.com.mongodb.internal.event.EventReasonMessageResolver;
import dev.artixdev.libs.com.mongodb.internal.inject.OptionalProvider;
import dev.artixdev.libs.com.mongodb.internal.logging.LogMessage;
import dev.artixdev.libs.com.mongodb.internal.logging.StructuredLogger;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.internal.thread.DaemonThreadFactory;
import dev.artixdev.libs.com.mongodb.internal.thread.InterruptionUtil;
import dev.artixdev.libs.com.mongodb.internal.time.TimePoint;
import dev.artixdev.libs.com.mongodb.internal.time.Timeout;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.types.ObjectId;

@ThreadSafe
final class DefaultConnectionPool implements ConnectionPool {
   private static final Logger LOGGER = Loggers.getLogger("connection");
   private static final StructuredLogger STRUCTURED_LOGGER = new StructuredLogger("connection");
   private final ConcurrentPool<UsageTrackingInternalConnection> pool;
   private final ConnectionPoolSettings settings;
   private final DefaultConnectionPool.BackgroundMaintenanceManager backgroundMaintenance;
   private final DefaultConnectionPool.AsyncWorkManager asyncWorkManager;
   private final ConnectionPoolListener connectionPoolListener;
   private final ServerId serverId;
   private final DefaultConnectionPool.PinnedStatsManager pinnedStatsManager;
   private final DefaultConnectionPool.ServiceStateManager serviceStateManager;
   private final ConnectionGenerationSupplier connectionGenerationSupplier;
   private final DefaultConnectionPool.OpenConcurrencyLimiter openConcurrencyLimiter;
   private final DefaultConnectionPool.StateAndGeneration stateAndGeneration;
   private final OptionalProvider<SdamServerDescriptionManager> sdamProvider;

   DefaultConnectionPool(ServerId serverId, InternalConnectionFactory internalConnectionFactory, ConnectionPoolSettings settings, OptionalProvider<SdamServerDescriptionManager> sdamProvider) {
      this(serverId, internalConnectionFactory, settings, InternalConnectionPoolSettings.builder().build(), sdamProvider);
   }

   DefaultConnectionPool(ServerId serverId, InternalConnectionFactory internalConnectionFactory, ConnectionPoolSettings settings, InternalConnectionPoolSettings internalSettings, OptionalProvider<SdamServerDescriptionManager> sdamProvider) {
      this.pinnedStatsManager = new DefaultConnectionPool.PinnedStatsManager();
      this.serviceStateManager = new DefaultConnectionPool.ServiceStateManager();
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
      this.settings = (ConnectionPoolSettings)Assertions.notNull("settings", settings);
      DefaultConnectionPool.UsageTrackingInternalConnectionItemFactory connectionItemFactory = new DefaultConnectionPool.UsageTrackingInternalConnectionItemFactory(internalConnectionFactory);
      this.pool = new ConcurrentPool(maxSize(settings), connectionItemFactory, String.format("The server at %s is no longer available", serverId.getAddress()));
      this.sdamProvider = (OptionalProvider)Assertions.assertNotNull(sdamProvider);
      this.connectionPoolListener = EventListenerHelper.getConnectionPoolListener(settings);
      this.backgroundMaintenance = new DefaultConnectionPool.BackgroundMaintenanceManager();
      this.connectionPoolCreated(this.connectionPoolListener, serverId, settings);
      this.openConcurrencyLimiter = new DefaultConnectionPool.OpenConcurrencyLimiter(settings.getMaxConnecting());
      this.asyncWorkManager = new DefaultConnectionPool.AsyncWorkManager(internalSettings.isPrestartAsyncWorkManager());
      this.stateAndGeneration = new DefaultConnectionPool.StateAndGeneration();
      this.connectionGenerationSupplier = new ConnectionGenerationSupplier() {
         public int getGeneration() {
            return DefaultConnectionPool.this.stateAndGeneration.generation();
         }

         public int getGeneration(@NonNull ObjectId serviceId) {
            return DefaultConnectionPool.this.serviceStateManager.getGeneration(serviceId);
         }
      };
   }

   public InternalConnection get(OperationContext operationContext) {
      return this.get(operationContext, this.settings.getMaxWaitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
   }

   public InternalConnection get(OperationContext operationContext, long timeoutValue, TimeUnit timeUnit) {
      TimePoint checkoutStart = this.connectionCheckoutStarted(operationContext);
      Timeout timeout = Timeout.started(timeoutValue, timeUnit, checkoutStart);

      try {
         this.stateAndGeneration.throwIfClosedOrPaused();
         DefaultConnectionPool.PooledConnection connection = this.getPooledConnection(timeout);
         if (!connection.opened()) {
            connection = this.openConcurrencyLimiter.openOrGetAvailable(connection, timeout);
         }

         connection.checkedOutForOperation(operationContext);
         this.connectionCheckedOut(operationContext, connection, checkoutStart);
         return connection;
      } catch (Exception exception) {
         throw (RuntimeException)this.checkOutFailed(exception, operationContext, checkoutStart);
      }
   }

   public void getAsync(OperationContext operationContext, SingleResultCallback<InternalConnection> callback) {
      TimePoint checkoutStart = this.connectionCheckoutStarted(operationContext);
      Timeout timeout = Timeout.started(this.settings.getMaxWaitTime(TimeUnit.NANOSECONDS), checkoutStart);
      SingleResultCallback<DefaultConnectionPool.PooledConnection> eventSendingCallback = (connection, failure) -> {
         SingleResultCallback<InternalConnection> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, LOGGER);
         if (failure == null) {
            connection.checkedOutForOperation(operationContext);
            this.connectionCheckedOut(operationContext, connection, checkoutStart);
            errHandlingCallback.onResult(connection, null);
         } else {
            errHandlingCallback.onResult(null, this.checkOutFailed(failure, operationContext, checkoutStart));
         }

      };

      try {
         this.stateAndGeneration.throwIfClosedOrPaused();
      } catch (Exception exception) {
         eventSendingCallback.onResult(null, exception);
         return;
      }

      this.asyncWorkManager.enqueue(new DefaultConnectionPool.Task(timeout, (t) -> {
         if (t != null) {
            eventSendingCallback.onResult(null, t);
         } else {
            DefaultConnectionPool.PooledConnection connection;
            try {
               connection = this.getPooledConnection(timeout);
            } catch (Exception exception) {
               eventSendingCallback.onResult(null, exception);
               return;
            }

            if (connection.opened()) {
               eventSendingCallback.onResult(connection, null);
            } else {
               this.openConcurrencyLimiter.openAsyncWithConcurrencyLimit(connection, timeout, eventSendingCallback);
            }
         }

      }));
   }

   private Throwable checkOutFailed(Throwable t, OperationContext operationContext, TimePoint checkoutStart) {
      Throwable result = t;
      ConnectionCheckOutFailedEvent.Reason reason;
      if (t instanceof MongoTimeoutException) {
         reason = ConnectionCheckOutFailedEvent.Reason.TIMEOUT;
      } else if (t instanceof DefaultConnectionPool.MongoOpenConnectionInternalException) {
         reason = ConnectionCheckOutFailedEvent.Reason.CONNECTION_ERROR;
         result = t.getCause();
      } else if (t instanceof MongoConnectionPoolClearedException) {
         reason = ConnectionCheckOutFailedEvent.Reason.CONNECTION_ERROR;
      } else if (ConcurrentPool.isPoolClosedException(t)) {
         reason = ConnectionCheckOutFailedEvent.Reason.POOL_CLOSED;
      } else {
         reason = ConnectionCheckOutFailedEvent.Reason.UNKNOWN;
      }

      Duration checkoutDuration = checkoutStart.elapsed();
      ClusterId clusterId = this.serverId.getClusterId();
      if (requiresLogging(clusterId)) {
         String message = "Checkout failed for connection to {}:{}. Reason: {}.[ Error: {}.] Duration: {} ms";
         List<LogMessage.Entry> entries = this.createBasicEntries();
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.REASON_DESCRIPTION, EventReasonMessageResolver.getMessage(reason)));
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.ERROR_DESCRIPTION, reason == ConnectionCheckOutFailedEvent.Reason.CONNECTION_ERROR ? result.toString() : null));
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DURATION_MS, checkoutDuration.toMillis()));
         logMessage("Connection checkout failed", clusterId, message, entries);
      }

      this.connectionPoolListener.connectionCheckOutFailed(new ConnectionCheckOutFailedEvent(this.serverId, operationContext.getId(), reason, checkoutDuration.toNanos()));
      return result;
   }

   public void invalidate(@Nullable Throwable cause) {
      Assertions.assertFalse(this.isLoadBalanced());
      if (this.stateAndGeneration.pauseAndIncrementGeneration(cause)) {
         this.openConcurrencyLimiter.signalClosedOrPaused();
      }

   }

   public void ready() {
      this.stateAndGeneration.ready();
   }

   public void invalidate(ObjectId serviceId, int generation) {
      Assertions.assertTrue(this.isLoadBalanced());
      if (generation != -1) {
         if (this.serviceStateManager.incrementGeneration(serviceId, generation)) {
            ClusterId clusterId = this.serverId.getClusterId();
            if (requiresLogging(clusterId)) {
               String message = "Connection pool for {}:{} cleared for serviceId {}";
               List<LogMessage.Entry> entries = this.createBasicEntries();
               entries.add(new LogMessage.Entry(LogMessage.Entry.Name.SERVICE_ID, serviceId.toHexString()));
               logMessage("Connection pool cleared", clusterId, message, entries);
            }

            this.connectionPoolListener.connectionPoolCleared(new ConnectionPoolClearedEvent(this.serverId, serviceId));
         }

      }
   }

   public void close() {
      if (this.stateAndGeneration.close()) {
         this.pool.close();
         this.backgroundMaintenance.close();
         this.asyncWorkManager.close();
         this.openConcurrencyLimiter.signalClosedOrPaused();
         this.logEventMessage("Connection pool closed", "Connection pool closed for {}:{}");
         this.connectionPoolListener.connectionPoolClosed(new ConnectionPoolClosedEvent(this.serverId));
      }

   }

   public int getGeneration() {
      return this.stateAndGeneration.generation();
   }

   private DefaultConnectionPool.PooledConnection getPooledConnection(Timeout timeout) throws MongoTimeoutException {
      try {
         UsageTrackingInternalConnection internalConnection;
         for(internalConnection = (UsageTrackingInternalConnection)this.pool.get(timeout.remainingOrInfinite(TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS); this.shouldPrune(internalConnection); internalConnection = (UsageTrackingInternalConnection)this.pool.get(timeout.remainingOrInfinite(TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS)) {
            this.pool.release(internalConnection, true);
         }

         return new DefaultConnectionPool.PooledConnection(internalConnection);
      } catch (MongoTimeoutException e) {
         throw this.createTimeoutException(timeout);
      }
   }

   @Nullable
   private DefaultConnectionPool.PooledConnection getPooledConnectionImmediateUnfair() {
      UsageTrackingInternalConnection internalConnection;
      for(internalConnection = (UsageTrackingInternalConnection)this.pool.getImmediateUnfair(); internalConnection != null && this.shouldPrune(internalConnection); internalConnection = (UsageTrackingInternalConnection)this.pool.getImmediateUnfair()) {
         this.pool.release(internalConnection, true);
      }

      return internalConnection == null ? null : new DefaultConnectionPool.PooledConnection(internalConnection);
   }

   private MongoTimeoutException createTimeoutException(Timeout timeout) {
      int numPinnedToCursor = this.pinnedStatsManager.getNumPinnedToCursor();
      int numPinnedToTransaction = this.pinnedStatsManager.getNumPinnedToTransaction();
      if (numPinnedToCursor == 0 && numPinnedToTransaction == 0) {
         return new MongoTimeoutException(String.format("Timed out after %s while waiting for a connection to server %s.", timeout.toUserString(), this.serverId.getAddress()));
      } else {
         int maxSize = this.pool.getMaxSize();
         int numInUse = this.pool.getInUseCount();
         if (numInUse == 0) {
            numInUse = Math.min(numPinnedToCursor + numPinnedToTransaction, maxSize);
         }

         numPinnedToCursor = Math.min(numPinnedToCursor, numInUse);
         numPinnedToTransaction = Math.min(numPinnedToTransaction, numInUse - numPinnedToCursor);
         int numOtherInUse = numInUse - numPinnedToCursor - numPinnedToTransaction;
         Assertions.assertTrue(numOtherInUse >= 0);
         Assertions.assertTrue(numPinnedToCursor + numPinnedToTransaction + numOtherInUse <= maxSize);
         return new MongoTimeoutException(String.format("Timed out after %s while waiting for a connection to server %s. Details: maxPoolSize: %s, connections in use by cursors: %d, connections in use by transactions: %d, connections in use by other operations: %d", timeout.toUserString(), this.serverId.getAddress(), ConcurrentPool.sizeToString(maxSize), numPinnedToCursor, numPinnedToTransaction, numOtherInUse));
      }
   }

   ConcurrentPool<UsageTrackingInternalConnection> getPool() {
      return this.pool;
   }

   void doMaintenance() {
      Predicate<Throwable> silentlyComplete = (e) -> {
         return e instanceof MongoInterruptedException || e instanceof MongoTimeoutException || e instanceof MongoConnectionPoolClearedException || ConcurrentPool.isPoolClosedException(e);
      };

      try {
         this.pool.prune();
         if (this.shouldEnsureMinSize()) {
            this.pool.ensureMinSize(this.settings.getMinSize(), (newConnection) -> {
               try {
                  this.openConcurrencyLimiter.openImmediatelyAndTryHandOverOrRelease(new DefaultConnectionPool.PooledConnection(newConnection));
               } catch (DefaultConnectionPool.MongoOpenConnectionInternalException | MongoException e) {
                  Throwable actualException = e instanceof DefaultConnectionPool.MongoOpenConnectionInternalException ? e.getCause() : e;

                  try {
                     this.sdamProvider.optional().ifPresent((sdam) -> {
                        if (!silentlyComplete.test(actualException)) {
                           sdam.handleExceptionBeforeHandshake(SdamServerDescriptionManager.SdamIssue.specific(actualException, sdam.context(newConnection)));
                        }

                     });
                  } catch (Exception suppressed) {
                     actualException.addSuppressed(suppressed);
                  }

                  throw actualException instanceof RuntimeException ? (RuntimeException) actualException : new MongoException(actualException.getMessage(), actualException);
               }
            });
         }
      } catch (Exception exception) {
         if (!silentlyComplete.test(exception)) {
            LOGGER.warn("Exception thrown during connection pool background maintenance task", exception);
            throw exception;
         }
      }

   }

   private boolean shouldEnsureMinSize() {
      return this.settings.getMinSize() > 0;
   }

   private boolean shouldPrune(UsageTrackingInternalConnection connection) {
      return this.fromPreviousGeneration(connection) || this.pastMaxLifeTime(connection) || this.pastMaxIdleTime(connection);
   }

   private boolean pastMaxIdleTime(UsageTrackingInternalConnection connection) {
      return this.expired(connection.getLastUsedAt(), System.currentTimeMillis(), this.settings.getMaxConnectionIdleTime(TimeUnit.MILLISECONDS));
   }

   private boolean pastMaxLifeTime(UsageTrackingInternalConnection connection) {
      return this.expired(connection.getOpenedAt(), System.currentTimeMillis(), this.settings.getMaxConnectionLifeTime(TimeUnit.MILLISECONDS));
   }

   private boolean fromPreviousGeneration(UsageTrackingInternalConnection connection) {
      int generation = connection.getGeneration();
      if (generation == -1) {
         return false;
      } else {
         ObjectId serviceId = connection.getDescription().getServiceId();
         if (serviceId != null) {
            return this.serviceStateManager.getGeneration(serviceId) > generation;
         } else {
            return this.stateAndGeneration.generation() > generation;
         }
      }
   }

   private boolean expired(long startTime, long curTime, long maxTime) {
      return maxTime != 0L && curTime - startTime > maxTime;
   }

   private void connectionPoolCreated(ConnectionPoolListener connectionPoolListener, ServerId serverId, ConnectionPoolSettings settings) {
      ClusterId clusterId = serverId.getClusterId();
      if (requiresLogging(clusterId)) {
         String message = "Connection pool created for {}:{} using options maxIdleTimeMS={}, minPoolSize={}, maxPoolSize={}, maxConnecting={}, waitQueueTimeoutMS={}";
         List<LogMessage.Entry> entries = this.createBasicEntries();
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.MAX_IDLE_TIME_MS, settings.getMaxConnectionIdleTime(TimeUnit.MILLISECONDS)));
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.MIN_POOL_SIZE, settings.getMinSize()));
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.MAX_POOL_SIZE, settings.getMaxSize()));
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.MAX_CONNECTING, settings.getMaxConnecting()));
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.WAIT_QUEUE_TIMEOUT_MS, settings.getMaxWaitTime(TimeUnit.MILLISECONDS)));
         logMessage("Connection pool created", clusterId, message, entries);
      }

      connectionPoolListener.connectionPoolCreated(new ConnectionPoolCreatedEvent(serverId, settings));
      connectionPoolListener.connectionPoolOpened(new ConnectionPoolOpenedEvent(serverId, settings));
   }

   private TimePoint connectionCreated(ConnectionPoolListener connectionPoolListener, ConnectionId connectionId) {
      this.logEventMessage("Connection created", "Connection created: address={}:{}, driver-generated ID={}", connectionId.getLocalValue());
      connectionPoolListener.connectionAdded(new ConnectionAddedEvent(connectionId));
      connectionPoolListener.connectionCreated(new ConnectionCreatedEvent(connectionId));
      return TimePoint.now();
   }

   private void connectionClosed(ConnectionPoolListener connectionPoolListener, ConnectionId connectionId, ConnectionClosedEvent.Reason reason) {
      ClusterId clusterId = this.serverId.getClusterId();
      if (requiresLogging(clusterId)) {
         String errorReason = "There was a socket exception raised by this connection";
         List<LogMessage.Entry> entries = this.createBasicEntries();
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DRIVER_CONNECTION_ID, connectionId.getLocalValue()));
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.REASON_DESCRIPTION, EventReasonMessageResolver.getMessage(reason)));
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.ERROR_DESCRIPTION, reason == ConnectionClosedEvent.Reason.ERROR ? errorReason : null));
         logMessage("Connection closed", clusterId, "Connection closed: address={}:{}, driver-generated ID={}. Reason: {}.[ Error: {}]", entries);
      }

      connectionPoolListener.connectionRemoved(new ConnectionRemovedEvent(connectionId, this.getReasonForRemoved(reason)));
      connectionPoolListener.connectionClosed(new ConnectionClosedEvent(connectionId, reason));
   }

   private void connectionCheckedOut(OperationContext operationContext, DefaultConnectionPool.PooledConnection connection, TimePoint checkoutStart) {
      Duration checkoutDuration = checkoutStart.elapsed();
      ConnectionId connectionId = this.getId(connection);
      ClusterId clusterId = this.serverId.getClusterId();
      if (requiresLogging(clusterId)) {
         List<LogMessage.Entry> entries = this.createBasicEntries();
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DRIVER_CONNECTION_ID, connectionId.getLocalValue()));
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DURATION_MS, checkoutDuration.toMillis()));
         logMessage("Connection checked out", clusterId, "Connection checked out: address={}:{}, driver-generated ID={}, duration={} ms", entries);
      }

      this.connectionPoolListener.connectionCheckedOut(new ConnectionCheckedOutEvent(connectionId, operationContext.getId(), checkoutDuration.toNanos()));
   }

   private TimePoint connectionCheckoutStarted(OperationContext operationContext) {
      this.logEventMessage("Connection checkout started", "Checkout started for connection to {}:{}");
      this.connectionPoolListener.connectionCheckOutStarted(new ConnectionCheckOutStartedEvent(this.serverId, operationContext.getId()));
      return TimePoint.now();
   }

   private ConnectionRemovedEvent.Reason getReasonForRemoved(ConnectionClosedEvent.Reason reason) {
      ConnectionRemovedEvent.Reason removedReason = ConnectionRemovedEvent.Reason.UNKNOWN;
      switch(reason) {
      case STALE:
         removedReason = ConnectionRemovedEvent.Reason.STALE;
         break;
      case IDLE:
         removedReason = ConnectionRemovedEvent.Reason.MAX_IDLE_TIME_EXCEEDED;
         break;
      case ERROR:
         removedReason = ConnectionRemovedEvent.Reason.ERROR;
         break;
      case POOL_CLOSED:
         removedReason = ConnectionRemovedEvent.Reason.POOL_CLOSED;
      }

      return removedReason;
   }

   private ConnectionId getId(InternalConnection internalConnection) {
      return internalConnection.getDescription().getConnectionId();
   }

   private boolean isLoadBalanced() {
      return !this.sdamProvider.optional().isPresent();
   }

   private static int maxSize(ConnectionPoolSettings settings) {
      return settings.getMaxSize() == 0 ? Integer.MAX_VALUE : settings.getMaxSize();
   }

   private void logEventMessage(String messageId, String format, int driverConnectionId) {
      ClusterId clusterId = this.serverId.getClusterId();
      if (requiresLogging(clusterId)) {
         List<LogMessage.Entry> entries = this.createBasicEntries();
         entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DRIVER_CONNECTION_ID, driverConnectionId));
         logMessage(messageId, clusterId, format, entries);
      }

   }

   private void logEventMessage(String messageId, String format) {
      ClusterId clusterId = this.serverId.getClusterId();
      if (requiresLogging(clusterId)) {
         List<LogMessage.Entry> entries = this.createBasicEntries();
         logMessage(messageId, clusterId, format, entries);
      }

   }

   private List<LogMessage.Entry> createBasicEntries() {
      List<LogMessage.Entry> entries = new ArrayList();
      entries.add(new LogMessage.Entry(LogMessage.Entry.Name.SERVER_HOST, this.serverId.getAddress().getHost()));
      entries.add(new LogMessage.Entry(LogMessage.Entry.Name.SERVER_PORT, this.serverId.getAddress().getPort()));
      return entries;
   }

   private static void logMessage(String messageId, ClusterId clusterId, String format, List<LogMessage.Entry> entries) {
      STRUCTURED_LOGGER.log(new LogMessage(LogMessage.Component.CONNECTION, LogMessage.Level.DEBUG, messageId, clusterId, entries, format));
   }

   private static boolean requiresLogging(ClusterId clusterId) {
      return STRUCTURED_LOGGER.isRequired(LogMessage.Level.DEBUG, clusterId);
   }

   @NotThreadSafe
   private final class BackgroundMaintenanceManager implements AutoCloseable {
      @Nullable
      private final ScheduledExecutorService maintainer;
      @Nullable
      private Future<?> cancellationHandle;
      private boolean initialStart;

      private BackgroundMaintenanceManager() {
         this.maintainer = DefaultConnectionPool.this.settings.getMaintenanceInitialDelay(TimeUnit.NANOSECONDS) < Long.MAX_VALUE ? Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory("MaintenanceTimer")) : null;
         this.cancellationHandle = null;
         this.initialStart = true;
      }

      void start() {
         if (this.maintainer != null) {
            Assertions.assertTrue(this.cancellationHandle == null);
            this.cancellationHandle = (Future)this.ignoreRejectedExectution(() -> {
               return this.maintainer.scheduleAtFixedRate(DefaultConnectionPool.this::doMaintenance, this.initialStart ? DefaultConnectionPool.this.settings.getMaintenanceInitialDelay(TimeUnit.MILLISECONDS) : 0L, DefaultConnectionPool.this.settings.getMaintenanceFrequency(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
            });
            this.initialStart = false;
         }

      }

      void runOnceAndStop() {
         if (this.maintainer != null) {
            if (this.cancellationHandle != null) {
               this.cancellationHandle.cancel(false);
               this.cancellationHandle = null;
            }

            this.ignoreRejectedExectution(() -> {
               this.maintainer.execute(DefaultConnectionPool.this::doMaintenance);
            });
         }

      }

      public void close() {
         if (this.maintainer != null) {
            this.maintainer.shutdownNow();
         }

      }

      private void ignoreRejectedExectution(Runnable action) {
         this.ignoreRejectedExectution(() -> {
            action.run();
            return null;
         });
      }

      @Nullable
      private <T> T ignoreRejectedExectution(Supplier<T> action) {
         try {
            return action.get();
         } catch (RejectedExecutionException e) {
            return null;
         }
      }

      // $FF: synthetic method
      BackgroundMaintenanceManager(Object x1) {
         this();
      }
   }

   private class PooledConnection implements InternalConnection {
      private final UsageTrackingInternalConnection wrapped;
      private final AtomicBoolean isClosed = new AtomicBoolean();
      private Connection.PinningMode pinningMode;
      private OperationContext operationContext;

      PooledConnection(UsageTrackingInternalConnection wrapped) {
         this.wrapped = (UsageTrackingInternalConnection)Assertions.notNull("wrapped", wrapped);
      }

      public int getGeneration() {
         return this.wrapped.getGeneration();
      }

      public void checkedOutForOperation(OperationContext operationContext) {
         this.operationContext = operationContext;
      }

      public void open() {
         Assertions.assertFalse(this.isClosed.get());

         TimePoint openStart;
         try {
            openStart = DefaultConnectionPool.this.connectionCreated(DefaultConnectionPool.this.connectionPoolListener, this.wrapped.getDescription().getConnectionId());
            this.wrapped.open();
         } catch (Exception exception) {
            this.closeAndHandleOpenFailure();
            throw new DefaultConnectionPool.MongoOpenConnectionInternalException(exception);
         }

         this.handleOpenSuccess(openStart);
      }

      public void openAsync(SingleResultCallback<Void> callback) {
         Assertions.assertFalse(this.isClosed.get());
         TimePoint openStart = DefaultConnectionPool.this.connectionCreated(DefaultConnectionPool.this.connectionPoolListener, this.wrapped.getDescription().getConnectionId());
         this.wrapped.openAsync((nullResult, failure) -> {
            if (failure != null) {
               this.closeAndHandleOpenFailure();
               callback.onResult(null, new DefaultConnectionPool.MongoOpenConnectionInternalException(failure));
            } else {
               this.handleOpenSuccess(openStart);
               callback.onResult(nullResult, (Throwable)null);
            }

         });
      }

      public void close() {
         if (!this.isClosed.getAndSet(true)) {
            this.unmarkAsPinned();
            this.connectionCheckedIn();
            if (!this.wrapped.isClosed() && !DefaultConnectionPool.this.shouldPrune(this.wrapped)) {
               DefaultConnectionPool.this.openConcurrencyLimiter.tryHandOverOrRelease(this.wrapped);
            } else {
               DefaultConnectionPool.this.pool.release(this.wrapped, true);
            }
         }

      }

      private void connectionCheckedIn() {
         ConnectionId connectionId = DefaultConnectionPool.this.getId(this.wrapped);
         DefaultConnectionPool.this.logEventMessage("Connection checked in", "Connection checked in: address={}:{}, driver-generated ID={}", connectionId.getLocalValue());
         DefaultConnectionPool.this.connectionPoolListener.connectionCheckedIn(new ConnectionCheckedInEvent(connectionId, this.operationContext.getId()));
      }

      void release() {
         if (!this.isClosed.getAndSet(true)) {
            DefaultConnectionPool.this.pool.release(this.wrapped);
         }

      }

      void closeSilently() {
         if (!this.isClosed.getAndSet(true)) {
            this.wrapped.setCloseSilently();
            DefaultConnectionPool.this.pool.release(this.wrapped, true);
         }

      }

      private void closeAndHandleOpenFailure() {
         if (!this.isClosed.getAndSet(true)) {
            if (this.wrapped.getDescription().getServiceId() != null) {
               DefaultConnectionPool.this.invalidate((ObjectId)Assertions.assertNotNull(this.wrapped.getDescription().getServiceId()), this.wrapped.getGeneration());
            }

            DefaultConnectionPool.this.pool.release(this.wrapped, true);
         }

      }

      private void handleOpenSuccess(TimePoint openStart) {
         Duration openDuration = openStart.elapsed();
         ConnectionId connectionId = DefaultConnectionPool.this.getId(this);
         ClusterId clusterId = DefaultConnectionPool.this.serverId.getClusterId();
         if (DefaultConnectionPool.requiresLogging(clusterId)) {
            List<LogMessage.Entry> entries = DefaultConnectionPool.this.createBasicEntries();
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DRIVER_CONNECTION_ID, connectionId.getLocalValue()));
            entries.add(new LogMessage.Entry(LogMessage.Entry.Name.DURATION_MS, openDuration.toMillis()));
            DefaultConnectionPool.logMessage("Connection ready", clusterId, "Connection ready: address={}:{}, driver-generated ID={}, established in={} ms", entries);
         }

         DefaultConnectionPool.this.connectionPoolListener.connectionReady(new ConnectionReadyEvent(connectionId, openDuration.toNanos()));
      }

      public boolean opened() {
         Assertions.isTrue("open", !this.isClosed.get());
         return this.wrapped.opened();
      }

      public boolean isClosed() {
         return this.isClosed.get() || this.wrapped.isClosed();
      }

      public ByteBuf getBuffer(int capacity) {
         return this.wrapped.getBuffer(capacity);
      }

      public void sendMessage(List<ByteBuf> byteBuffers, int lastRequestId) {
         Assertions.isTrue("open", !this.isClosed.get());
         this.wrapped.sendMessage(byteBuffers, lastRequestId);
      }

      public <T> T sendAndReceive(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext, RequestContext requestContext, OperationContext operationContext) {
         Assertions.isTrue("open", !this.isClosed.get());
         return this.wrapped.sendAndReceive(message, decoder, sessionContext, requestContext, operationContext);
      }

      public <T> void send(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext) {
         Assertions.isTrue("open", !this.isClosed.get());
         this.wrapped.send(message, decoder, sessionContext);
      }

      public <T> T receive(Decoder<T> decoder, SessionContext sessionContext) {
         Assertions.isTrue("open", !this.isClosed.get());
         return this.wrapped.receive(decoder, sessionContext);
      }

      public boolean supportsAdditionalTimeout() {
         Assertions.isTrue("open", !this.isClosed.get());
         return this.wrapped.supportsAdditionalTimeout();
      }

      public <T> T receive(Decoder<T> decoder, SessionContext sessionContext, int additionalTimeout) {
         Assertions.isTrue("open", !this.isClosed.get());
         return this.wrapped.receive(decoder, sessionContext, additionalTimeout);
      }

      public boolean hasMoreToCome() {
         Assertions.isTrue("open", !this.isClosed.get());
         return this.wrapped.hasMoreToCome();
      }

      public <T> void sendAndReceiveAsync(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext, RequestContext requestContext, OperationContext operationContext, SingleResultCallback<T> callback) {
         Assertions.isTrue("open", !this.isClosed.get());
         this.wrapped.sendAndReceiveAsync(message, decoder, sessionContext, requestContext, operationContext, (result, t) -> {
            callback.onResult(result, t);
         });
      }

      public ResponseBuffers receiveMessage(int responseTo) {
         Assertions.isTrue("open", !this.isClosed.get());
         return this.wrapped.receiveMessage(responseTo);
      }

      public void sendMessageAsync(List<ByteBuf> byteBuffers, int lastRequestId, SingleResultCallback<Void> callback) {
         Assertions.isTrue("open", !this.isClosed.get());
         this.wrapped.sendMessageAsync(byteBuffers, lastRequestId, (result, t) -> {
            callback.onResult(null, t);
         });
      }

      public void receiveMessageAsync(int responseTo, SingleResultCallback<ResponseBuffers> callback) {
         Assertions.isTrue("open", !this.isClosed.get());
         this.wrapped.receiveMessageAsync(responseTo, (result, t) -> {
            callback.onResult(result, t);
         });
      }

      public void markAsPinned(Connection.PinningMode pinningMode) {
         Assertions.assertNotNull(pinningMode);
         if (this.pinningMode == null) {
            this.pinningMode = pinningMode;
            DefaultConnectionPool.this.pinnedStatsManager.increment(pinningMode);
         }

      }

      void unmarkAsPinned() {
         if (this.pinningMode != null) {
            DefaultConnectionPool.this.pinnedStatsManager.decrement(this.pinningMode);
         }

      }

      public ConnectionDescription getDescription() {
         return this.wrapped.getDescription();
      }

      public ServerDescription getInitialServerDescription() {
         Assertions.isTrue("open", !this.isClosed.get());
         return this.wrapped.getInitialServerDescription();
      }
   }

   private static final class PinnedStatsManager {
      private final LongAdder numPinnedToCursor;
      private final LongAdder numPinnedToTransaction;

      private PinnedStatsManager() {
         this.numPinnedToCursor = new LongAdder();
         this.numPinnedToTransaction = new LongAdder();
      }

      void increment(Connection.PinningMode pinningMode) {
         switch(pinningMode) {
         case CURSOR:
            this.numPinnedToCursor.increment();
            break;
         case TRANSACTION:
            this.numPinnedToTransaction.increment();
            break;
         default:
            Assertions.fail();
         }

      }

      void decrement(Connection.PinningMode pinningMode) {
         switch(pinningMode) {
         case CURSOR:
            this.numPinnedToCursor.decrement();
            break;
         case TRANSACTION:
            this.numPinnedToTransaction.decrement();
            break;
         default:
            Assertions.fail();
         }

      }

      int getNumPinnedToCursor() {
         return this.numPinnedToCursor.intValue();
      }

      int getNumPinnedToTransaction() {
         return this.numPinnedToTransaction.intValue();
      }

      // $FF: synthetic method
      PinnedStatsManager(Object x0) {
         this();
      }
   }

   @ThreadSafe
   private final class OpenConcurrencyLimiter {
      private final ReentrantLock lock = new ReentrantLock(true);
      private final Condition permitAvailableOrHandedOverOrClosedOrPausedCondition;
      private final int maxPermits;
      private int permits;
      private final Deque<DefaultConnectionPool.MutableReference<DefaultConnectionPool.PooledConnection>> desiredConnectionSlots;

      OpenConcurrencyLimiter(int maxConnecting) {
         this.permitAvailableOrHandedOverOrClosedOrPausedCondition = this.lock.newCondition();
         this.maxPermits = maxConnecting;
         this.permits = this.maxPermits;
         this.desiredConnectionSlots = new LinkedList();
      }

      DefaultConnectionPool.PooledConnection openOrGetAvailable(DefaultConnectionPool.PooledConnection connection, Timeout timeout) throws MongoTimeoutException {
         DefaultConnectionPool.PooledConnection result = this.openWithConcurrencyLimit(connection, DefaultConnectionPool.OpenWithConcurrencyLimitMode.TRY_GET_AVAILABLE, timeout);
         return (DefaultConnectionPool.PooledConnection)Assertions.assertNotNull(result);
      }

      void openImmediatelyAndTryHandOverOrRelease(DefaultConnectionPool.PooledConnection connection) throws MongoTimeoutException {
         Assertions.assertNull(this.openWithConcurrencyLimit(connection, DefaultConnectionPool.OpenWithConcurrencyLimitMode.TRY_HAND_OVER_OR_RELEASE, Timeout.immediate()));
      }

      @Nullable
      private DefaultConnectionPool.PooledConnection openWithConcurrencyLimit(DefaultConnectionPool.PooledConnection connection, DefaultConnectionPool.OpenWithConcurrencyLimitMode mode, Timeout timeout) throws MongoTimeoutException {
         DefaultConnectionPool.PooledConnection availableConnection;
         try {
            availableConnection = this.acquirePermitOrGetAvailableOpenedConnection(mode == DefaultConnectionPool.OpenWithConcurrencyLimitMode.TRY_GET_AVAILABLE, timeout);
         } catch (Exception exception) {
            connection.closeSilently();
            throw exception;
         }

         if (availableConnection != null) {
            connection.closeSilently();
            return availableConnection;
         } else {
            DefaultConnectionPool.PooledConnection var5;
            try {
               connection.open();
               if (mode == DefaultConnectionPool.OpenWithConcurrencyLimitMode.TRY_HAND_OVER_OR_RELEASE) {
                  this.tryHandOverOrRelease(connection.wrapped);
                  var5 = null;
                  return var5;
               }

               var5 = connection;
            } finally {
               this.releasePermit();
            }

            return var5;
         }
      }

      void openAsyncWithConcurrencyLimit(DefaultConnectionPool.PooledConnection connection, Timeout timeout, SingleResultCallback<DefaultConnectionPool.PooledConnection> callback) {
         DefaultConnectionPool.PooledConnection availableConnection;
         try {
            availableConnection = this.acquirePermitOrGetAvailableOpenedConnection(true, timeout);
         } catch (Exception exception) {
            connection.closeSilently();
            callback.onResult(null, exception);
            return;
         }

         if (availableConnection != null) {
            connection.closeSilently();
            callback.onResult(availableConnection, (Throwable)null);
         } else {
            connection.openAsync((nullResult, failure) -> {
               this.releasePermit();
               if (failure != null) {
                  callback.onResult(null, failure);
               } else {
                  callback.onResult(connection, (Throwable)null);
               }

            });
         }

      }

      @Nullable
      private DefaultConnectionPool.PooledConnection acquirePermitOrGetAvailableOpenedConnection(boolean tryGetAvailable, Timeout timeout) throws MongoTimeoutException, MongoInterruptedException {
         DefaultConnectionPool.PooledConnection availableConnection = null;
         boolean expressedDesireToGetAvailableConnection = false;
         Locks.lockInterruptibly(this.lock);

         try {
            if (tryGetAvailable) {
               availableConnection = DefaultConnectionPool.this.getPooledConnectionImmediateUnfair();
               if (availableConnection != null) {
                  DefaultConnectionPool.PooledConnection var29 = availableConnection;
                  return var29;
               }

               this.expressDesireToGetAvailableConnection();
               expressedDesireToGetAvailableConnection = true;
            }

            long remainingNanos = timeout.remainingOrInfinite(TimeUnit.NANOSECONDS);

            while(true) {
               boolean var10000 = this.permits == 0 & !DefaultConnectionPool.this.stateAndGeneration.throwIfClosedOrPaused();
               DefaultConnectionPool.PooledConnection var10001 = tryGetAvailable ? this.tryGetAvailableConnection() : null;
               availableConnection = var10001;
               if (!(var10000 & var10001 == null)) {
                  if (availableConnection == null) {
                     Assertions.assertTrue(this.permits > 0);
                     --this.permits;
                  }

                  DefaultConnectionPool.PooledConnection var7 = availableConnection;
                  return var7;
               }

               if (Timeout.expired(remainingNanos)) {
                  throw DefaultConnectionPool.this.createTimeoutException(timeout);
               }

               remainingNanos = this.awaitNanos(this.permitAvailableOrHandedOverOrClosedOrPausedCondition, remainingNanos);
            }
         } finally {
            try {
               if (expressedDesireToGetAvailableConnection && availableConnection == null) {
                  this.giveUpOnTryingToGetAvailableConnection();
               }
            } finally {
               this.lock.unlock();
            }

         }
      }

      private void releasePermit() {
         Locks.withUnfairLock(this.lock, () -> {
            Assertions.assertTrue(this.permits < this.maxPermits);
            ++this.permits;
            this.permitAvailableOrHandedOverOrClosedOrPausedCondition.signal();
         });
      }

      private void expressDesireToGetAvailableConnection() {
         this.desiredConnectionSlots.addLast(new DefaultConnectionPool.MutableReference());
      }

      @Nullable
      private DefaultConnectionPool.PooledConnection tryGetAvailableConnection() {
         Assertions.assertFalse(this.desiredConnectionSlots.isEmpty());
         DefaultConnectionPool.PooledConnection result = (DefaultConnectionPool.PooledConnection)((DefaultConnectionPool.MutableReference)this.desiredConnectionSlots.peekFirst()).reference;
         if (result != null) {
            this.desiredConnectionSlots.removeFirst();
            Assertions.assertTrue(result.opened());
         }

         return result;
      }

      private void giveUpOnTryingToGetAvailableConnection() {
         Assertions.assertFalse(this.desiredConnectionSlots.isEmpty());
         DefaultConnectionPool.PooledConnection connection = (DefaultConnectionPool.PooledConnection)((DefaultConnectionPool.MutableReference)this.desiredConnectionSlots.removeLast()).reference;
         if (connection != null) {
            connection.release();
         }

      }

      void tryHandOverOrRelease(UsageTrackingInternalConnection openConnection) {
         boolean handedOver = (Boolean)Locks.withUnfairLock(this.lock, () -> {
            Iterator<DefaultConnectionPool.MutableReference<DefaultConnectionPool.PooledConnection>> iterator = this.desiredConnectionSlots.iterator();

            DefaultConnectionPool.MutableReference<DefaultConnectionPool.PooledConnection> desiredConnectionSlot;
            do {
               if (!iterator.hasNext()) {
                  return false;
               }

               desiredConnectionSlot = iterator.next();
            } while(desiredConnectionSlot.reference != null);

            desiredConnectionSlot.reference = DefaultConnectionPool.this.new PooledConnection(openConnection);
            this.permitAvailableOrHandedOverOrClosedOrPausedCondition.signal();
            return true;
         });
         if (!handedOver) {
            DefaultConnectionPool.this.pool.release(openConnection);
         }

      }

      void signalClosedOrPaused() {
         ReentrantLock var10000 = this.lock;
         Condition var10001 = this.permitAvailableOrHandedOverOrClosedOrPausedCondition;
         Objects.requireNonNull(var10001);
         Locks.withUnfairLock(var10000, var10001::signalAll);
      }

      private long awaitNanos(Condition condition, long timeoutNanos) throws MongoInterruptedException {
         try {
            if (timeoutNanos >= 0L && timeoutNanos != Long.MAX_VALUE) {
               return Math.max(0L, condition.awaitNanos(timeoutNanos));
            } else {
               condition.await();
               return -1L;
            }
         } catch (InterruptedException e) {
            throw InterruptionUtil.interruptAndCreateMongoInterruptedException((String)null, e);
         }
      }
   }

   @ThreadSafe
   static final class ServiceStateManager {
      private final ConcurrentHashMap<ObjectId, DefaultConnectionPool.ServiceStateManager.ServiceState> stateByServiceId = new ConcurrentHashMap();

      void addConnection(ObjectId serviceId) {
         this.stateByServiceId.compute(serviceId, (k, v) -> {
            if (v == null) {
               v = new DefaultConnectionPool.ServiceStateManager.ServiceState();
            }

            v.incrementConnectionCount();
            return v;
         });
      }

      void removeConnection(ObjectId serviceId) {
         this.stateByServiceId.compute(serviceId, (k, v) -> {
            Assertions.assertNotNull(v);
            return v.decrementAndGetConnectionCount() == 0 ? null : v;
         });
      }

      boolean incrementGeneration(ObjectId serviceId, int expectedGeneration) {
         DefaultConnectionPool.ServiceStateManager.ServiceState state = (DefaultConnectionPool.ServiceStateManager.ServiceState)this.stateByServiceId.get(serviceId);
         return state == null || state.incrementGeneration(expectedGeneration);
      }

      int getGeneration(ObjectId serviceId) {
         DefaultConnectionPool.ServiceStateManager.ServiceState state = (DefaultConnectionPool.ServiceStateManager.ServiceState)this.stateByServiceId.get(serviceId);
         return state == null ? 0 : state.getGeneration();
      }

      private static final class ServiceState {
         private final AtomicInteger generation;
         private final AtomicInteger connectionCount;

         private ServiceState() {
            this.generation = new AtomicInteger();
            this.connectionCount = new AtomicInteger();
         }

         void incrementConnectionCount() {
            this.connectionCount.incrementAndGet();
         }

         int decrementAndGetConnectionCount() {
            return this.connectionCount.decrementAndGet();
         }

         boolean incrementGeneration(int expectedGeneration) {
            return this.generation.compareAndSet(expectedGeneration, expectedGeneration + 1);
         }

         public int getGeneration() {
            return this.generation.get();
         }

         // $FF: synthetic method
         ServiceState(Object x0) {
            this();
         }
      }
   }

   @ThreadSafe
   private final class StateAndGeneration {
      private final ReadWriteLock lock = (new StampedLock()).asReadWriteLock();
      private volatile boolean paused = true;
      private final AtomicBoolean closed = new AtomicBoolean();
      private volatile int generation = 0;
      @Nullable
      private Throwable cause = null;

      StateAndGeneration() {
      }

      int generation() {
         return this.generation;
      }

      boolean pauseAndIncrementGeneration(@Nullable Throwable cause) {
         return (Boolean)Locks.withLock(this.lock.writeLock(), () -> {
            boolean result = false;
            if (!this.paused) {
               this.paused = true;
               DefaultConnectionPool.this.pool.pause(() -> {
                  return new MongoConnectionPoolClearedException(DefaultConnectionPool.this.serverId, cause);
               });
               result = true;
            }

            this.cause = cause;
            ++this.generation;
            if (result) {
               DefaultConnectionPool.this.logEventMessage("Connection pool cleared", "Connection pool for {}:{} cleared");
               DefaultConnectionPool.this.connectionPoolListener.connectionPoolCleared(new ConnectionPoolClearedEvent(DefaultConnectionPool.this.serverId));
               DefaultConnectionPool.this.backgroundMaintenance.runOnceAndStop();
            }

            return result;
         });
      }

      boolean ready() {
         boolean result = false;
         if (this.paused) {
            result = (Boolean)Locks.withLock(this.lock.writeLock(), () -> {
               if (this.paused) {
                  this.paused = false;
                  this.cause = null;
                  DefaultConnectionPool.this.pool.ready();
                  DefaultConnectionPool.this.logEventMessage("Connection pool ready", "Connection pool ready for {}:{}");
                  DefaultConnectionPool.this.connectionPoolListener.connectionPoolReady(new ConnectionPoolReadyEvent(DefaultConnectionPool.this.serverId));
                  DefaultConnectionPool.this.backgroundMaintenance.start();
                  return true;
               } else {
                  return false;
               }
            });
         }

         return result;
      }

      boolean close() {
         return this.closed.compareAndSet(false, true);
      }

      boolean throwIfClosedOrPaused() {
         if (this.closed.get()) {
            throw DefaultConnectionPool.this.pool.poolClosedException();
         } else {
            if (this.paused) {
               Locks.withLock(this.lock.readLock(), () -> {
                  if (this.paused) {
                     throw new MongoConnectionPoolClearedException(DefaultConnectionPool.this.serverId, this.cause);
                  }
               });
            }

            return false;
         }
      }
   }

   private class UsageTrackingInternalConnectionItemFactory implements ConcurrentPool.ItemFactory<UsageTrackingInternalConnection> {
      private final InternalConnectionFactory internalConnectionFactory;

      UsageTrackingInternalConnectionItemFactory(InternalConnectionFactory internalConnectionFactory) {
         this.internalConnectionFactory = internalConnectionFactory;
      }

      public UsageTrackingInternalConnection create() {
         return new UsageTrackingInternalConnection(this.internalConnectionFactory.create(DefaultConnectionPool.this.serverId, DefaultConnectionPool.this.connectionGenerationSupplier), DefaultConnectionPool.this.serviceStateManager);
      }

      public void close(UsageTrackingInternalConnection connection) {
         if (!connection.isCloseSilently()) {
            DefaultConnectionPool.this.connectionClosed(DefaultConnectionPool.this.connectionPoolListener, DefaultConnectionPool.this.getId(connection), this.getReasonForClosing(connection));
         }

         connection.close();
      }

      private ConnectionClosedEvent.Reason getReasonForClosing(UsageTrackingInternalConnection connection) {
         ConnectionClosedEvent.Reason reason;
         if (connection.isClosed()) {
            reason = ConnectionClosedEvent.Reason.ERROR;
         } else if (DefaultConnectionPool.this.fromPreviousGeneration(connection)) {
            reason = ConnectionClosedEvent.Reason.STALE;
         } else if (DefaultConnectionPool.this.pastMaxIdleTime(connection)) {
            reason = ConnectionClosedEvent.Reason.IDLE;
         } else {
            reason = ConnectionClosedEvent.Reason.POOL_CLOSED;
         }

         return reason;
      }

      public boolean shouldPrune(UsageTrackingInternalConnection usageTrackingConnection) {
         return DefaultConnectionPool.this.shouldPrune(usageTrackingConnection);
      }
   }

   @ThreadSafe
   private static class AsyncWorkManager implements AutoCloseable {
      private volatile DefaultConnectionPool.AsyncWorkManager.State state;
      private volatile BlockingQueue<DefaultConnectionPool.Task> tasks;
      private final Lock lock;
      @Nullable
      private ExecutorService worker;

      AsyncWorkManager(boolean prestart) {
         this.state = DefaultConnectionPool.AsyncWorkManager.State.NEW;
         this.tasks = new LinkedBlockingQueue();
         this.lock = (new StampedLock()).asWriteLock();
         if (prestart) {
            Assertions.assertTrue(this.initUnlessClosed());
         }

      }

      void enqueue(DefaultConnectionPool.Task task) {
         boolean closed = (Boolean)Locks.withLock(this.lock, () -> {
            if (this.initUnlessClosed()) {
               this.tasks.add(task);
               return false;
            } else {
               return true;
            }
         });
         if (closed) {
            task.failAsClosed();
         }

      }

      private boolean initUnlessClosed() {
         boolean result = true;
         if (this.state == DefaultConnectionPool.AsyncWorkManager.State.NEW) {
            this.worker = Executors.newSingleThreadExecutor(new DaemonThreadFactory("AsyncGetter"));
            this.worker.submit(() -> {
               this.runAndLogUncaught(this::workerRun);
            });
            this.state = DefaultConnectionPool.AsyncWorkManager.State.INITIALIZED;
         } else if (this.state == DefaultConnectionPool.AsyncWorkManager.State.CLOSED) {
            result = false;
         }

         return result;
      }

      public void close() {
         Locks.withLock(this.lock, () -> {
            if (this.state != DefaultConnectionPool.AsyncWorkManager.State.CLOSED) {
               this.state = DefaultConnectionPool.AsyncWorkManager.State.CLOSED;
               if (this.worker != null) {
                  this.worker.shutdownNow();
               }
            }

         });
      }

      private void workerRun() {
         while(this.state != DefaultConnectionPool.AsyncWorkManager.State.CLOSED) {
            try {
               DefaultConnectionPool.Task task = (DefaultConnectionPool.Task)this.tasks.take();
               if (task.timeout().expired()) {
                  task.failAsTimedOut();
               } else {
                  task.execute();
               }
            } catch (InterruptedException e) {
            } catch (Exception exception) {
               DefaultConnectionPool.LOGGER.error((String)null, exception);
            }
         }

         this.failAllTasksAfterClosing();
      }

      private void failAllTasksAfterClosing() {
         Queue<DefaultConnectionPool.Task> localGets = (Queue)Locks.withLock(this.lock, () -> {
            Assertions.assertTrue(this.state == DefaultConnectionPool.AsyncWorkManager.State.CLOSED);
            Queue<DefaultConnectionPool.Task> result = this.tasks;
            if (!this.tasks.isEmpty()) {
               this.tasks = new LinkedBlockingQueue();
            }

            return result;
         });
         localGets.forEach(DefaultConnectionPool.Task::failAsClosed);
         localGets.clear();
      }

      private void runAndLogUncaught(Runnable runnable) {
         try {
            runnable.run();
         } catch (Throwable throwable) {
            DefaultConnectionPool.LOGGER.error("The pool is not going to work correctly from now on. You may want to recreate the MongoClient", throwable);
            throw throwable;
         }
      }

      private static enum State {
         NEW,
         INITIALIZED,
         CLOSED;

         // $FF: synthetic method
         private static DefaultConnectionPool.AsyncWorkManager.State[] $values() {
            return new DefaultConnectionPool.AsyncWorkManager.State[]{NEW, INITIALIZED, CLOSED};
         }
      }
   }

   @NotThreadSafe
   final class Task {
      private final Timeout timeout;
      private final Consumer<RuntimeException> action;
      private boolean completed;

      Task(Timeout timeout, Consumer<RuntimeException> action) {
         this.timeout = timeout;
         this.action = action;
      }

      void execute() {
         this.doComplete(() -> {
            return null;
         });
      }

      void failAsClosed() {
         ConcurrentPool var10001 = DefaultConnectionPool.this.pool;
         Objects.requireNonNull(var10001);
         this.doComplete(var10001::poolClosedException);
      }

      void failAsTimedOut() {
         this.doComplete(() -> {
            return DefaultConnectionPool.this.createTimeoutException(this.timeout);
         });
      }

      private void doComplete(Supplier<RuntimeException> failureSupplier) {
         Assertions.assertFalse(this.completed);
         this.completed = true;
         this.action.accept((RuntimeException)failureSupplier.get());
      }

      Timeout timeout() {
         return this.timeout;
      }
   }

   private static final class MongoOpenConnectionInternalException extends RuntimeException {
      private static final long serialVersionUID = 1L;

      MongoOpenConnectionInternalException(@NonNull Throwable cause) {
         super(cause);
      }

      @NonNull
      public Throwable getCause() {
         return (Throwable)Assertions.assertNotNull(super.getCause());
      }
   }

   @NotThreadSafe
   private static final class MutableReference<T> {
      @Nullable
      private T reference;

      private MutableReference() {
      }

      // $FF: synthetic method
      MutableReference(Object x0) {
         this();
      }
   }

   private static enum OpenWithConcurrencyLimitMode {
      TRY_GET_AVAILABLE,
      TRY_HAND_OVER_OR_RELEASE;

      // $FF: synthetic method
      private static DefaultConnectionPool.OpenWithConcurrencyLimitMode[] $values() {
         return new DefaultConnectionPool.OpenWithConcurrencyLimitMode[]{TRY_GET_AVAILABLE, TRY_HAND_OVER_OR_RELEASE};
      }
   }
}
