package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.event.ClusterClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ClusterDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.event.ClusterListener;
import dev.artixdev.libs.com.mongodb.event.ClusterOpeningEvent;
import dev.artixdev.libs.com.mongodb.event.ServerClosedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerHeartbeatFailedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerHeartbeatStartedEvent;
import dev.artixdev.libs.com.mongodb.event.ServerHeartbeatSucceededEvent;
import dev.artixdev.libs.com.mongodb.event.ServerListener;
import dev.artixdev.libs.com.mongodb.event.ServerMonitorListener;
import dev.artixdev.libs.com.mongodb.event.ServerOpeningEvent;

@ThreadSafe
final class AsynchronousClusterEventListener implements ClusterListener, ServerListener, ServerMonitorListener {
   private final BlockingQueue<Supplier<Boolean>> eventPublishers = new LinkedBlockingQueue();
   private final ClusterListener clusterListener;
   private final ServerListener serverListener;
   private final ServerMonitorListener serverMonitorListener;
   private final Thread publishingThread;

   static AsynchronousClusterEventListener startNew(ClusterId clusterId, ClusterListener clusterListener, ServerListener serverListener, ServerMonitorListener serverMonitorListener) {
      AsynchronousClusterEventListener result = new AsynchronousClusterEventListener(clusterId, clusterListener, serverListener, serverMonitorListener);
      result.publishingThread.start();
      return result;
   }

   private AsynchronousClusterEventListener(ClusterId clusterId, ClusterListener clusterListener, ServerListener serverListener, ServerMonitorListener serverMonitorListener) {
      this.clusterListener = (ClusterListener)Assertions.notNull("clusterListener", clusterListener);
      this.serverListener = (ServerListener)Assertions.notNull("serverListener", serverListener);
      this.serverMonitorListener = (ServerMonitorListener)Assertions.notNull("serverMonitorListener", serverMonitorListener);
      this.publishingThread = new Thread(this::publishEvents, "cluster-event-publisher-" + clusterId.getValue());
      this.publishingThread.setDaemon(true);
   }

   Thread getPublishingThread() {
      return this.publishingThread;
   }

   public void clusterOpening(ClusterOpeningEvent event) {
      this.addClusterEventInvocation((clusterListener) -> {
         clusterListener.clusterOpening(event);
      }, false);
   }

   public void clusterClosed(ClusterClosedEvent event) {
      this.addClusterEventInvocation((clusterListener) -> {
         clusterListener.clusterClosed(event);
      }, true);
   }

   public void clusterDescriptionChanged(ClusterDescriptionChangedEvent event) {
      this.addClusterEventInvocation((clusterListener) -> {
         clusterListener.clusterDescriptionChanged(event);
      }, false);
   }

   public void serverOpening(ServerOpeningEvent event) {
      this.addServerEventInvocation((serverListener) -> {
         serverListener.serverOpening(event);
      });
   }

   public void serverClosed(ServerClosedEvent event) {
      this.addServerEventInvocation((serverListener) -> {
         serverListener.serverClosed(event);
      });
   }

   public void serverDescriptionChanged(ServerDescriptionChangedEvent event) {
      this.addServerEventInvocation((serverListener) -> {
         serverListener.serverDescriptionChanged(event);
      });
   }

   public void serverHearbeatStarted(ServerHeartbeatStartedEvent event) {
      this.addServerMonitorEventInvocation((serverMonitorListener) -> {
         serverMonitorListener.serverHearbeatStarted(event);
      });
   }

   public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent event) {
      this.addServerMonitorEventInvocation((serverMonitorListener) -> {
         serverMonitorListener.serverHeartbeatSucceeded(event);
      });
   }

   public void serverHeartbeatFailed(ServerHeartbeatFailedEvent event) {
      this.addServerMonitorEventInvocation((serverMonitorListener) -> {
         serverMonitorListener.serverHeartbeatFailed(event);
      });
   }

   private void addClusterEventInvocation(AsynchronousClusterEventListener.VoidFunction<ClusterListener> eventPublisher, boolean isLastEvent) {
      this.addEvent(() -> {
         eventPublisher.apply(this.clusterListener);
         return isLastEvent;
      });
   }

   private void addServerEventInvocation(AsynchronousClusterEventListener.VoidFunction<ServerListener> eventPublisher) {
      this.addEvent(() -> {
         eventPublisher.apply(this.serverListener);
         return false;
      });
   }

   private void addServerMonitorEventInvocation(AsynchronousClusterEventListener.VoidFunction<ServerMonitorListener> eventPublisher) {
      this.addEvent(() -> {
         eventPublisher.apply(this.serverMonitorListener);
         return false;
      });
   }

   private void addEvent(Supplier<Boolean> supplier) {
      if (this.publishingThread.isAlive()) {
         this.eventPublishers.add(supplier);
      }
   }

   private void publishEvents() {
      while(true) {
         try {
            Supplier<Boolean> eventPublisher = (Supplier)this.eventPublishers.take();
            boolean isLastEvent = (Boolean)eventPublisher.get();
            if (isLastEvent) {
               return;
            }
         } catch (Exception ignored) {
         }
      }
   }

   @FunctionalInterface
   private interface VoidFunction<T> {
      void apply(T var1);
   }
}
