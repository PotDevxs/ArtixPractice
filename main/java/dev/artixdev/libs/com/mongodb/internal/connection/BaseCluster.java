package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoIncompatibleDriverException;
import dev.artixdev.libs.com.mongodb.MongoTimeoutException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ClusterSettings;
import dev.artixdev.libs.com.mongodb.connection.ClusterType;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.event.ClusterClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ClusterDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.event.ClusterListener;
import dev.artixdev.libs.com.mongodb.event.ClusterOpeningEvent;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.event.EventListenerHelper;
import dev.artixdev.libs.com.mongodb.internal.selector.LatencyMinimizingServerSelector;
import dev.artixdev.libs.com.mongodb.internal.thread.InterruptionUtil;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.selector.CompositeServerSelector;
import dev.artixdev.libs.com.mongodb.selector.ServerSelector;

abstract class BaseCluster implements Cluster {
   private static final Logger LOGGER = Loggers.getLogger("cluster");
   private final ReentrantLock lock = new ReentrantLock();
   private final AtomicReference<CountDownLatch> phase = new AtomicReference(new CountDownLatch(1));
   private final ClusterableServerFactory serverFactory;
   private final ClusterId clusterId;
   private final ClusterSettings settings;
   private final ClusterListener clusterListener;
   private final Deque<BaseCluster.ServerSelectionRequest> waitQueue = new ConcurrentLinkedDeque();
   private final ClusterClock clusterClock = new ClusterClock();
   private Thread waitQueueHandler;
   private volatile boolean isClosed;
   private volatile ClusterDescription description;

   BaseCluster(ClusterId clusterId, ClusterSettings settings, ClusterableServerFactory serverFactory) {
      this.clusterId = (ClusterId)Assertions.notNull("clusterId", clusterId);
      this.settings = (ClusterSettings)Assertions.notNull("settings", settings);
      this.serverFactory = (ClusterableServerFactory)Assertions.notNull("serverFactory", serverFactory);
      this.clusterListener = EventListenerHelper.singleClusterListener(settings);
      this.clusterListener.clusterOpening(new ClusterOpeningEvent(clusterId));
      this.description = new ClusterDescription(settings.getMode(), ClusterType.UNKNOWN, Collections.emptyList(), settings, serverFactory.getSettings());
   }

   public ClusterClock getClock() {
      return this.clusterClock;
   }

   public ServerTuple selectServer(ServerSelector serverSelector, OperationContext operationContext) {
      Assertions.isTrue("open", !this.isClosed());

      try {
         CountDownLatch currentPhase = (CountDownLatch)this.phase.get();
         ClusterDescription curDescription = this.description;
         ServerSelector compositeServerSelector = this.getCompositeServerSelector(serverSelector);
         ServerTuple serverTuple = this.selectServer(compositeServerSelector, curDescription);
         boolean selectionFailureLogged = false;
         long startTimeNanos = System.nanoTime();
         long curTimeNanos = startTimeNanos;
         long maxWaitTimeNanos = this.getMaxWaitTimeNanos();

         while(true) {
            this.throwIfIncompatible(curDescription);
            if (serverTuple != null) {
               return serverTuple;
            }

            if (curTimeNanos - startTimeNanos > maxWaitTimeNanos) {
               throw this.createTimeoutException(serverSelector, curDescription);
            }

            if (!selectionFailureLogged) {
               this.logServerSelectionFailure(serverSelector, curDescription);
               selectionFailureLogged = true;
            }

            this.connect();
            currentPhase.await(Math.min(maxWaitTimeNanos - (curTimeNanos - startTimeNanos), this.getMinWaitTimeNanos()), TimeUnit.NANOSECONDS);
            curTimeNanos = System.nanoTime();
            currentPhase = (CountDownLatch)this.phase.get();
            curDescription = this.description;
            serverTuple = this.selectServer(compositeServerSelector, curDescription);
         }
      } catch (InterruptedException e) {
         throw InterruptionUtil.interruptAndCreateMongoInterruptedException(String.format("Interrupted while waiting for a server that matches %s", serverSelector), e);
      }
   }

   public void selectServerAsync(ServerSelector serverSelector, OperationContext operationContext, SingleResultCallback<ServerTuple> callback) {
      Assertions.isTrue("open", !this.isClosed());
      if (LOGGER.isTraceEnabled()) {
         LOGGER.trace(String.format("Asynchronously selecting server with selector %s", serverSelector));
      }

      BaseCluster.ServerSelectionRequest request = new BaseCluster.ServerSelectionRequest(serverSelector, this.getCompositeServerSelector(serverSelector), this.getMaxWaitTimeNanos(), callback);
      CountDownLatch currentPhase = (CountDownLatch)this.phase.get();
      ClusterDescription currentDescription = this.description;
      if (!this.handleServerSelectionRequest(request, currentPhase, currentDescription)) {
         this.notifyWaitQueueHandler(request);
      }

   }

   public ClusterId getClusterId() {
      return this.clusterId;
   }

   public ClusterSettings getSettings() {
      return this.settings;
   }

   public ClusterableServerFactory getServerFactory() {
      return this.serverFactory;
   }

   protected abstract void connect();

   public void close() {
      if (!this.isClosed()) {
         this.isClosed = true;
         ((CountDownLatch)this.phase.get()).countDown();
         this.clusterListener.clusterClosed(new ClusterClosedEvent(this.clusterId));
         this.stopWaitQueueHandler();
      }

   }

   public boolean isClosed() {
      return this.isClosed;
   }

   protected void updateDescription(ClusterDescription newDescription) {
      this.withLock(() -> {
         if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Updating cluster description to  %s", newDescription.getShortDescription()));
         }

         this.description = newDescription;
         this.updatePhase();
      });
   }

   protected void fireChangeEvent(ClusterDescription newDescription, ClusterDescription previousDescription) {
      if (!EventHelper.wouldDescriptionsGenerateEquivalentEvents(newDescription, previousDescription)) {
         this.clusterListener.clusterDescriptionChanged(new ClusterDescriptionChangedEvent(this.getClusterId(), newDescription, previousDescription));
      }

   }

   public ClusterDescription getCurrentDescription() {
      return this.description;
   }

   public void withLock(Runnable action) {
      Locks.withInterruptibleLock(this.lock, (Runnable)action);
   }

   private void updatePhase() {
      this.withLock(() -> {
         ((CountDownLatch)this.phase.getAndSet(new CountDownLatch(1))).countDown();
      });
   }

   private long getMaxWaitTimeNanos() {
      return this.settings.getServerSelectionTimeout(TimeUnit.NANOSECONDS) < 0L ? Long.MAX_VALUE : this.settings.getServerSelectionTimeout(TimeUnit.NANOSECONDS);
   }

   private long getMinWaitTimeNanos() {
      return this.serverFactory.getSettings().getMinHeartbeatFrequency(TimeUnit.NANOSECONDS);
   }

   private boolean handleServerSelectionRequest(BaseCluster.ServerSelectionRequest request, CountDownLatch currentPhase, ClusterDescription description) {
      try {
         if (currentPhase != request.phase) {
            CountDownLatch prevPhase = request.phase;
            request.phase = currentPhase;
            if (!description.isCompatibleWithDriver()) {
               if (LOGGER.isTraceEnabled()) {
                  LOGGER.trace("Asynchronously failed server selection due to driver incompatibility with server");
               }

               request.onResult((ServerTuple)null, this.createIncompatibleException(description));
               return true;
            }

            ServerTuple serverTuple = this.selectServer(request.compositeSelector, description);
            if (serverTuple != null) {
               if (LOGGER.isTraceEnabled()) {
                  LOGGER.trace(String.format("Asynchronously selected server %s", serverTuple.getServerDescription().getAddress()));
               }

               request.onResult(serverTuple, (Throwable)null);
               return true;
            }

            if (prevPhase == null) {
               this.logServerSelectionFailure(request.originalSelector, description);
            }
         }

         if (request.timedOut()) {
            if (LOGGER.isTraceEnabled()) {
               LOGGER.trace("Asynchronously failed server selection after timeout");
            }

            request.onResult((ServerTuple)null, this.createTimeoutException(request.originalSelector, description));
            return true;
         } else {
            return false;
         }
      } catch (Exception exception) {
         request.onResult((ServerTuple)null, exception);
         return true;
      }
   }

   private void logServerSelectionFailure(ServerSelector serverSelector, ClusterDescription curDescription) {
      if (LOGGER.isInfoEnabled()) {
         if (this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS) < 0L) {
            LOGGER.info(String.format("No server chosen by %s from cluster description %s. Waiting indefinitely.", serverSelector, curDescription));
         } else {
            LOGGER.info(String.format("No server chosen by %s from cluster description %s. Waiting for %d ms before timing out", serverSelector, curDescription, this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS)));
         }
      }

   }

   @Nullable
   private ServerTuple selectServer(ServerSelector serverSelector, ClusterDescription clusterDescription) {
      return selectServer(serverSelector, clusterDescription, this::getServer);
   }

   @Nullable
   static ServerTuple selectServer(ServerSelector serverSelector, ClusterDescription clusterDescription, Function<ServerAddress, Server> serverCatalog) {
      List<ServerTuple> candidates = atMostNRandom(new ArrayList<>(serverSelector.select(clusterDescription)), 2, (serverDescription) -> {
         Server server = (Server)serverCatalog.apply(serverDescription.getAddress());
         return server == null ? null : new ServerTuple(server, serverDescription);
      });
      return candidates.stream().min(Comparator.comparingInt((serverTuple) -> {
         return serverTuple.getServer().operationCount();
      })).orElse(null);
   }

   private static List<ServerTuple> atMostNRandom(ArrayList<ServerDescription> list, int n, Function<ServerDescription, ServerTuple> transformer) {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      List<ServerTuple> result = new ArrayList<>(n);

      for(int i = list.size() - 1; i >= 0 && result.size() < n; --i) {
         Collections.swap(list, i, random.nextInt(i + 1));
         ServerTuple serverTuple = (ServerTuple)transformer.apply((ServerDescription)list.get(i));
         if (serverTuple != null) {
            result.add(serverTuple);
         }
      }

      return result;
   }

   private ServerSelector getCompositeServerSelector(ServerSelector serverSelector) {
      ServerSelector latencyMinimizingServerSelector = new LatencyMinimizingServerSelector(this.settings.getLocalThreshold(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
      return this.settings.getServerSelector() == null ? new CompositeServerSelector(Arrays.asList(serverSelector, latencyMinimizingServerSelector)) : new CompositeServerSelector(Arrays.asList(serverSelector, this.settings.getServerSelector(), latencyMinimizingServerSelector));
   }

   protected ClusterableServer createServer(ServerAddress serverAddress) {
      return this.serverFactory.create(this, serverAddress);
   }

   private void throwIfIncompatible(ClusterDescription curDescription) {
      if (!curDescription.isCompatibleWithDriver()) {
         throw this.createIncompatibleException(curDescription);
      }
   }

   private MongoIncompatibleDriverException createIncompatibleException(ClusterDescription curDescription) {
      ServerDescription incompatibleServer = curDescription.findServerIncompatiblyOlderThanDriver();
      String message;
      if (incompatibleServer != null) {
         message = String.format("Server at %s reports wire version %d, but this version of the driver requires at least %d (MongoDB %s).", incompatibleServer.getAddress(), incompatibleServer.getMaxWireVersion(), 6, "3.6");
      } else {
         incompatibleServer = curDescription.findServerIncompatiblyNewerThanDriver();
         if (incompatibleServer == null) {
            throw new IllegalStateException("Server can't be both older than the driver and newer.");
         }

         message = String.format("Server at %s requires wire version %d, but this version of the driver only supports up to %d.", incompatibleServer.getAddress(), incompatibleServer.getMinWireVersion(), 21);
      }

      return new MongoIncompatibleDriverException(message, curDescription);
   }

   private MongoTimeoutException createTimeoutException(ServerSelector serverSelector, ClusterDescription curDescription) {
      return new MongoTimeoutException(String.format("Timed out after %d ms while waiting for a server that matches %s. Client view of cluster state is %s", this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS), serverSelector, curDescription.getShortDescription()));
   }

   private void notifyWaitQueueHandler(BaseCluster.ServerSelectionRequest request) {
      this.withLock(() -> {
         if (!this.isClosed) {
            this.waitQueue.add(request);
            if (this.waitQueueHandler == null) {
               this.waitQueueHandler = new Thread(new BaseCluster.WaitQueueHandler(), "cluster-" + this.clusterId.getValue());
               this.waitQueueHandler.setDaemon(true);
               this.waitQueueHandler.start();
            } else {
               this.updatePhase();
            }

         }
      });
   }

   private void stopWaitQueueHandler() {
      this.withLock(() -> {
         if (this.waitQueueHandler != null) {
            this.waitQueueHandler.interrupt();
         }

      });
   }

   private static final class ServerSelectionRequest {
      private final ServerSelector originalSelector;
      private final ServerSelector compositeSelector;
      private final long maxWaitTimeNanos;
      private final SingleResultCallback<ServerTuple> callback;
      private final long startTimeNanos = System.nanoTime();
      private CountDownLatch phase;

      ServerSelectionRequest(ServerSelector serverSelector, ServerSelector compositeSelector, long maxWaitTimeNanos, SingleResultCallback<ServerTuple> callback) {
         this.originalSelector = serverSelector;
         this.compositeSelector = compositeSelector;
         this.maxWaitTimeNanos = maxWaitTimeNanos;
         this.callback = callback;
      }

      void onResult(@Nullable ServerTuple serverTuple, @Nullable Throwable t) {
         try {
            this.callback.onResult(serverTuple, t);
         } catch (Throwable ignored) {
         }

      }

      boolean timedOut() {
         return System.nanoTime() - this.startTimeNanos > this.maxWaitTimeNanos;
      }

      long getRemainingTime() {
         return this.startTimeNanos + this.maxWaitTimeNanos - System.nanoTime();
      }
   }

   private final class WaitQueueHandler implements Runnable {
      private WaitQueueHandler() {
      }

      public void run() {
         while(!BaseCluster.this.isClosed) {
            CountDownLatch currentPhase = (CountDownLatch)BaseCluster.this.phase.get();
            ClusterDescription curDescription = BaseCluster.this.description;
            long waitTimeNanos = Long.MAX_VALUE;
            Iterator iterx = BaseCluster.this.waitQueue.iterator();

            while(iterx.hasNext()) {
               BaseCluster.ServerSelectionRequest nextRequest = (BaseCluster.ServerSelectionRequest)iterx.next();
               if (BaseCluster.this.handleServerSelectionRequest(nextRequest, currentPhase, curDescription)) {
                  iterx.remove();
               } else {
                  waitTimeNanos = Math.min(nextRequest.getRemainingTime(), Math.min(BaseCluster.this.getMinWaitTimeNanos(), waitTimeNanos));
               }
            }

            if (waitTimeNanos < Long.MAX_VALUE) {
               BaseCluster.this.connect();
            }

            try {
               currentPhase.await(waitTimeNanos, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
            }
         }

         Iterator iter = BaseCluster.this.waitQueue.iterator();

         while(iter.hasNext()) {
            ((BaseCluster.ServerSelectionRequest)iter.next()).onResult((ServerTuple)null, new MongoClientException("Shutdown in progress"));
            iter.remove();
         }

      }

      // $FF: synthetic method
      WaitQueueHandler(Object x1) {
         this();
      }
   }
}
