package dev.artixdev.libs.com.mongodb.internal.event;

import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterSettings;
import dev.artixdev.libs.com.mongodb.connection.ConnectionPoolSettings;
import dev.artixdev.libs.com.mongodb.connection.ServerSettings;
import dev.artixdev.libs.com.mongodb.event.ClusterListener;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.event.ConnectionPoolListener;
import dev.artixdev.libs.com.mongodb.event.ServerListener;
import dev.artixdev.libs.com.mongodb.event.ServerMonitorListener;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class EventListenerHelper {
   public static final ServerListener NO_OP_SERVER_LISTENER = new ServerListener() {
   };
   public static final ServerMonitorListener NO_OP_SERVER_MONITOR_LISTENER = new ServerMonitorListener() {
   };
   public static final ClusterListener NO_OP_CLUSTER_LISTENER = new ClusterListener() {
   };
   private static final ConnectionPoolListener NO_OP_CONNECTION_POOL_LISTENER = new ConnectionPoolListener() {
   };

   public static ClusterListener singleClusterListener(ClusterSettings clusterSettings) {
      Assertions.assertTrue(clusterSettings.getClusterListeners().size() <= 1);
      return clusterSettings.getClusterListeners().isEmpty() ? NO_OP_CLUSTER_LISTENER : (ClusterListener)clusterSettings.getClusterListeners().get(0);
   }

   public static ServerListener singleServerListener(ServerSettings serverSettings) {
      Assertions.assertTrue(serverSettings.getServerListeners().size() <= 1);
      return serverSettings.getServerListeners().isEmpty() ? NO_OP_SERVER_LISTENER : (ServerListener)serverSettings.getServerListeners().get(0);
   }

   public static ServerMonitorListener singleServerMonitorListener(ServerSettings serverSettings) {
      Assertions.assertTrue(serverSettings.getServerMonitorListeners().size() <= 1);
      return serverSettings.getServerMonitorListeners().isEmpty() ? NO_OP_SERVER_MONITOR_LISTENER : (ServerMonitorListener)serverSettings.getServerMonitorListeners().get(0);
   }

   public static ClusterListener clusterListenerMulticaster(List<ClusterListener> clusterListeners) {
      return new ClusterListenerMulticaster(clusterListeners);
   }

   public static ServerListener serverListenerMulticaster(List<ServerListener> serverListeners) {
      return new ServerListenerMulticaster(serverListeners);
   }

   public static ServerMonitorListener serverMonitorListenerMulticaster(List<ServerMonitorListener> serverMonitorListeners) {
      return new ServerMonitorListenerMulticaster(serverMonitorListeners);
   }

   @Nullable
   public static CommandListener getCommandListener(List<CommandListener> commandListeners) {
      switch(commandListeners.size()) {
      case 0:
         return null;
      case 1:
         return (CommandListener)commandListeners.get(0);
      default:
         return new CommandListenerMulticaster(commandListeners);
      }
   }

   public static ConnectionPoolListener getConnectionPoolListener(ConnectionPoolSettings connectionPoolSettings) {
      switch(connectionPoolSettings.getConnectionPoolListeners().size()) {
      case 0:
         return NO_OP_CONNECTION_POOL_LISTENER;
      case 1:
         return (ConnectionPoolListener)connectionPoolSettings.getConnectionPoolListeners().get(0);
      default:
         return new ConnectionPoolListenerMulticaster(connectionPoolSettings.getConnectionPoolListeners());
      }
   }

   private EventListenerHelper() {
   }
}
