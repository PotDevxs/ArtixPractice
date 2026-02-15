package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoTimeoutException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ClusterSettings;
import dev.artixdev.libs.com.mongodb.connection.ClusterType;
import dev.artixdev.libs.com.mongodb.connection.ServerConnectionState;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.event.ClusterClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ClusterDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.event.ClusterListener;
import dev.artixdev.libs.com.mongodb.event.ClusterOpeningEvent;
import dev.artixdev.libs.com.mongodb.event.ServerDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.event.EventListenerHelper;
import dev.artixdev.libs.com.mongodb.internal.thread.InterruptionUtil;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.selector.ServerSelector;

@ThreadSafe
final class LoadBalancedCluster implements Cluster {
   private static final Logger LOGGER = Loggers.getLogger("cluster");
   private final ClusterId clusterId;
   private final ClusterSettings settings;
   private final ClusterClock clusterClock = new ClusterClock();
   private final ClusterListener clusterListener;
   private ClusterDescription description;
   @Nullable
   private ClusterableServer server;
   private final AtomicBoolean closed = new AtomicBoolean();
   private final DnsSrvRecordMonitor dnsSrvRecordMonitor;
   private volatile MongoException srvResolutionException;
   private boolean srvRecordResolvedToMultipleHosts;
   private volatile boolean initializationCompleted;
   private List<LoadBalancedCluster.ServerSelectionRequest> waitQueue = new LinkedList();
   private Thread waitQueueHandler;
   private final Lock lock = new ReentrantLock(true);
   private final Condition condition;

   LoadBalancedCluster(final ClusterId clusterId, ClusterSettings settings, final ClusterableServerFactory serverFactory, DnsSrvRecordMonitorFactory dnsSrvRecordMonitorFactory) {
      this.condition = this.lock.newCondition();
      Assertions.assertTrue(settings.getMode() == ClusterConnectionMode.LOAD_BALANCED);
      LOGGER.info(String.format("Cluster created with id %s and settings %s", clusterId, settings.getShortDescription()));
      this.clusterId = clusterId;
      this.settings = settings;
      this.clusterListener = EventListenerHelper.singleClusterListener(settings);
      this.description = new ClusterDescription(settings.getMode(), ClusterType.UNKNOWN, Collections.emptyList(), settings, serverFactory.getSettings());
      if (settings.getSrvHost() == null) {
         this.dnsSrvRecordMonitor = null;
         this.init(clusterId, serverFactory, (ServerAddress)settings.getHosts().get(0));
         this.initializationCompleted = true;
      } else {
         Assertions.notNull("dnsSrvRecordMonitorFactory", dnsSrvRecordMonitorFactory);
         this.dnsSrvRecordMonitor = dnsSrvRecordMonitorFactory.create((String)Assertions.assertNotNull(settings.getSrvHost()), settings.getSrvServiceName(), new DnsSrvRecordInitializer() {
            public void initialize(Collection<ServerAddress> hosts) {
               LoadBalancedCluster.LOGGER.info("SRV resolution completed with hosts: " + hosts);
               LoadBalancedCluster.this.lock.lock();

               List<LoadBalancedCluster.ServerSelectionRequest> localWaitQueue;
               try {
                  if (LoadBalancedCluster.this.isClosed()) {
                     return;
                  }

                  LoadBalancedCluster.this.srvResolutionException = null;
                  if (hosts.size() != 1) {
                     LoadBalancedCluster.this.srvRecordResolvedToMultipleHosts = true;
                  } else {
                     LoadBalancedCluster.this.init(clusterId, serverFactory, (ServerAddress)hosts.iterator().next());
                  }

                  LoadBalancedCluster.this.initializationCompleted = true;
                  localWaitQueue = LoadBalancedCluster.this.waitQueue;
                  LoadBalancedCluster.this.waitQueue = Collections.emptyList();
                  LoadBalancedCluster.this.condition.signalAll();
               } finally {
                  LoadBalancedCluster.this.lock.unlock();
               }

               localWaitQueue.forEach((request) -> {
                  LoadBalancedCluster.this.handleServerSelectionRequest(request);
               });
            }

            public void initialize(MongoException initializationException) {
               LoadBalancedCluster.this.srvResolutionException = initializationException;
            }

            public ClusterType getClusterType() {
               return LoadBalancedCluster.this.initializationCompleted ? ClusterType.LOAD_BALANCED : ClusterType.UNKNOWN;
            }
         });
         this.dnsSrvRecordMonitor.start();
      }

   }

   private void init(ClusterId clusterId, ClusterableServerFactory serverFactory, ServerAddress host) {
      this.clusterListener.clusterOpening(new ClusterOpeningEvent(clusterId));
      ClusterDescription initialDescription = new ClusterDescription(this.settings.getMode(), ClusterType.LOAD_BALANCED, Collections.singletonList(ServerDescription.builder().address((ServerAddress)this.settings.getHosts().get(0)).state(ServerConnectionState.CONNECTING).build()), this.settings, serverFactory.getSettings());
      this.clusterListener.clusterDescriptionChanged(new ClusterDescriptionChangedEvent(clusterId, initialDescription, this.description));
      this.description = new ClusterDescription(ClusterConnectionMode.LOAD_BALANCED, ClusterType.LOAD_BALANCED, Collections.singletonList(ServerDescription.builder().ok(true).state(ServerConnectionState.CONNECTED).type(ServerType.LOAD_BALANCER).address(host).build()), this.settings, serverFactory.getSettings());
      this.server = serverFactory.create(this, host);
      this.clusterListener.clusterDescriptionChanged(new ClusterDescriptionChangedEvent(clusterId, this.description, initialDescription));
   }

   public ClusterSettings getSettings() {
      Assertions.isTrue("open", !this.isClosed());
      return this.settings;
   }

   public ClusterId getClusterId() {
      return this.clusterId;
   }

   public ClusterableServer getServer(ServerAddress serverAddress) {
      Assertions.isTrue("open", !this.isClosed());
      this.waitForSrv();
      return (ClusterableServer)Assertions.assertNotNull(this.server);
   }

   public ClusterDescription getCurrentDescription() {
      Assertions.isTrue("open", !this.isClosed());
      return this.description;
   }

   public ClusterClock getClock() {
      Assertions.isTrue("open", !this.isClosed());
      return this.clusterClock;
   }

   public ServerTuple selectServer(ServerSelector serverSelector, OperationContext operationContext) {
      Assertions.isTrue("open", !this.isClosed());
      this.waitForSrv();
      if (this.srvRecordResolvedToMultipleHosts) {
         throw this.createResolvedToMultipleHostsException();
      } else {
         return new ServerTuple((Server)Assertions.assertNotNull(this.server), (ServerDescription)this.description.getServerDescriptions().get(0));
      }
   }

   private void waitForSrv() {
      if (!this.initializationCompleted) {
         Locks.withLock(this.lock, () -> {
            long remainingTimeNanos = this.getMaxWaitTimeNanos();

            while(!this.initializationCompleted) {
               if (this.isClosed()) {
                  throw this.createShutdownException();
               }

               if (remainingTimeNanos <= 0L) {
                  throw this.createTimeoutException();
               }

               try {
                  remainingTimeNanos = this.condition.awaitNanos(remainingTimeNanos);
               } catch (InterruptedException e) {
                  throw InterruptionUtil.interruptAndCreateMongoInterruptedException(String.format("Interrupted while resolving SRV records for %s", this.settings.getSrvHost()), e);
               }
            }

         });
      }
   }

   public void selectServerAsync(ServerSelector serverSelector, OperationContext operationContext, SingleResultCallback<ServerTuple> callback) {
      if (this.isClosed()) {
         callback.onResult(null, this.createShutdownException());
      } else {
         LoadBalancedCluster.ServerSelectionRequest serverSelectionRequest = new LoadBalancedCluster.ServerSelectionRequest(this.getMaxWaitTimeNanos(), callback);
         if (this.initializationCompleted) {
            this.handleServerSelectionRequest(serverSelectionRequest);
         } else {
            this.notifyWaitQueueHandler(serverSelectionRequest);
         }

      }
   }

   private MongoClientException createShutdownException() {
      return new MongoClientException("Shutdown in progress");
   }

   public void close() {
      if (!this.closed.getAndSet(true)) {
         LOGGER.info(String.format("Cluster closed with id %s", this.clusterId));
         if (this.dnsSrvRecordMonitor != null) {
            this.dnsSrvRecordMonitor.close();
         }

         ClusterableServer localServer = (ClusterableServer)Locks.withLock(this.lock, () -> {
            this.condition.signalAll();
            return this.server;
         });
         if (localServer != null) {
            localServer.close();
         }

         this.clusterListener.clusterClosed(new ClusterClosedEvent(this.clusterId));
      }

   }

   public boolean isClosed() {
      return this.closed.get();
   }

   public void withLock(Runnable action) {
      Assertions.fail();
   }

   public void onChange(ServerDescriptionChangedEvent event) {
      Assertions.fail();
   }

   private void handleServerSelectionRequest(LoadBalancedCluster.ServerSelectionRequest serverSelectionRequest) {
      Assertions.assertTrue(this.initializationCompleted);
      if (this.srvRecordResolvedToMultipleHosts) {
         serverSelectionRequest.onError(this.createResolvedToMultipleHostsException());
      } else {
         serverSelectionRequest.onSuccess(new ServerTuple((Server)Assertions.assertNotNull(this.server), (ServerDescription)this.description.getServerDescriptions().get(0)));
      }

   }

   private MongoClientException createResolvedToMultipleHostsException() {
      return new MongoClientException("In load balancing mode, the host must resolve to a single SRV record, but instead it resolved to multiple hosts");
   }

   private MongoTimeoutException createTimeoutException() {
      MongoException localSrvResolutionException = this.srvResolutionException;
      return localSrvResolutionException == null ? new MongoTimeoutException(String.format("Timed out after %d ms while waiting to resolve SRV records for %s.", this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS), this.settings.getSrvHost())) : new MongoTimeoutException(String.format("Timed out after %d ms while waiting to resolve SRV records for %s. Resolution exception was '%s'", this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS), this.settings.getSrvHost(), localSrvResolutionException));
   }

   private long getMaxWaitTimeNanos() {
      return this.settings.getServerSelectionTimeout(TimeUnit.NANOSECONDS) < 0L ? Long.MAX_VALUE : this.settings.getServerSelectionTimeout(TimeUnit.NANOSECONDS);
   }

   private void notifyWaitQueueHandler(LoadBalancedCluster.ServerSelectionRequest request) {
      Locks.withLock(this.lock, () -> {
         if (this.isClosed()) {
            request.onError(this.createShutdownException());
         } else if (this.initializationCompleted) {
            this.handleServerSelectionRequest(request);
         } else {
            this.waitQueue.add(request);
            if (this.waitQueueHandler == null) {
               this.waitQueueHandler = new Thread(new LoadBalancedCluster.WaitQueueHandler(), "cluster-" + this.clusterId.getValue());
               this.waitQueueHandler.setDaemon(true);
               this.waitQueueHandler.start();
            } else {
               this.condition.signalAll();
            }

         }
      });
   }

   private static final class ServerSelectionRequest {
      private final long maxWaitTimeNanos;
      private final long startTimeNanos;
      private final SingleResultCallback<ServerTuple> callback;

      private ServerSelectionRequest(long maxWaitTimeNanos, SingleResultCallback<ServerTuple> callback) {
         this.startTimeNanos = System.nanoTime();
         this.maxWaitTimeNanos = maxWaitTimeNanos;
         this.callback = callback;
      }

      long getRemainingTime(long curTimeNanos) {
         return this.startTimeNanos + this.maxWaitTimeNanos - curTimeNanos;
      }

      public void onSuccess(ServerTuple serverTuple) {
         try {
            this.callback.onResult(serverTuple, (Throwable)null);
         } catch (Exception exception) {
            LoadBalancedCluster.LOGGER.warn("Unanticipated exception thrown from callback", exception);
         }

      }

      public void onError(Throwable exception) {
         try {
            this.callback.onResult(null, exception);
         } catch (Exception callbackException) {
            LoadBalancedCluster.LOGGER.warn("Unanticipated exception thrown from callback", callbackException);
         }

      }

      // $FF: synthetic method
      ServerSelectionRequest(long x0, SingleResultCallback x1, Object x2) {
         this(x0, x1);
      }
   }

   private final class WaitQueueHandler implements Runnable {
      private WaitQueueHandler() {
      }

      public void run() {
         ArrayList<LoadBalancedCluster.ServerSelectionRequest> timeoutList = new ArrayList<>();

         while(!LoadBalancedCluster.this.isClosed() && !LoadBalancedCluster.this.initializationCompleted) {
            LoadBalancedCluster.this.lock.lock();

            try {
               if (LoadBalancedCluster.this.isClosed() || LoadBalancedCluster.this.initializationCompleted) {
                  break;
               }

               long waitTimeNanos = Long.MAX_VALUE;
               long curTimeNanos = System.nanoTime();
               Iterator iterator = LoadBalancedCluster.this.waitQueue.iterator();

               while(true) {
                  if (!iterator.hasNext()) {
                     if (timeoutList.isEmpty()) {
                        try {
                           LoadBalancedCluster.this.condition.await(waitTimeNanos, TimeUnit.NANOSECONDS);
                        } catch (InterruptedException e) {
                           Assertions.fail();
                        }
                     }
                     break;
                  }

                  LoadBalancedCluster.ServerSelectionRequest next = (LoadBalancedCluster.ServerSelectionRequest)iterator.next();
                  long remainingTime = next.getRemainingTime(curTimeNanos);
                  if (remainingTime <= 0L) {
                     timeoutList.add(next);
                     iterator.remove();
                  } else {
                     waitTimeNanos = Math.min(remainingTime, waitTimeNanos);
                  }
               }
            } finally {
               LoadBalancedCluster.this.lock.unlock();
            }

            timeoutList.forEach((request) -> {
               request.onError(LoadBalancedCluster.this.createTimeoutException());
            });
            timeoutList.clear();
         }

         List<LoadBalancedCluster.ServerSelectionRequest> shutdownList = (List)Locks.withLock(LoadBalancedCluster.this.lock, () -> {
            ArrayList<LoadBalancedCluster.ServerSelectionRequest> result = new ArrayList(LoadBalancedCluster.this.waitQueue);
            LoadBalancedCluster.this.waitQueue.clear();
            return result;
         });
         shutdownList.forEach((request) -> {
            request.onError(LoadBalancedCluster.this.createShutdownException());
         });
      }

      // $FF: synthetic method
      WaitQueueHandler(Object x1) {
         this();
      }
   }
}
