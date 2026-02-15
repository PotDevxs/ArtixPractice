package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.LoggerSettings;
import dev.artixdev.libs.com.mongodb.MongoCompressor;
import dev.artixdev.libs.com.mongodb.MongoCredential;
import dev.artixdev.libs.com.mongodb.MongoDriverInformation;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ClusterSettings;
import dev.artixdev.libs.com.mongodb.connection.ConnectionPoolSettings;
import dev.artixdev.libs.com.mongodb.connection.ServerSettings;
import dev.artixdev.libs.com.mongodb.connection.StreamFactory;
import dev.artixdev.libs.com.mongodb.event.ClusterListener;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.event.ServerListener;
import dev.artixdev.libs.com.mongodb.event.ServerMonitorListener;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.event.EventListenerHelper;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.spi.dns.DnsClient;
import dev.artixdev.libs.com.mongodb.spi.dns.InetAddressResolver;

public final class DefaultClusterFactory {
   private static final Logger LOGGER = Loggers.getLogger("DefaultClusterFactory");

   public Cluster createCluster(ClusterSettings originalClusterSettings, ServerSettings originalServerSettings, ConnectionPoolSettings connectionPoolSettings, InternalConnectionPoolSettings internalConnectionPoolSettings, StreamFactory streamFactory, StreamFactory heartbeatStreamFactory, @Nullable MongoCredential credential, LoggerSettings loggerSettings, @Nullable CommandListener commandListener, @Nullable String applicationName, @Nullable MongoDriverInformation mongoDriverInformation, List<MongoCompressor> compressorList, @Nullable ServerApi serverApi, @Nullable DnsClient dnsClient, @Nullable InetAddressResolver inetAddressResolver) {
      this.detectAndLogClusterEnvironment(originalClusterSettings);
      ClusterId clusterId = new ClusterId(applicationName);
      ClusterSettings clusterSettings;
      ServerSettings serverSettings;
      if (this.noClusterEventListeners(originalClusterSettings, originalServerSettings)) {
         clusterSettings = ClusterSettings.builder(originalClusterSettings).clusterListenerList(Collections.singletonList(EventListenerHelper.NO_OP_CLUSTER_LISTENER)).build();
         serverSettings = ServerSettings.builder(originalServerSettings).serverListenerList(Collections.singletonList(EventListenerHelper.NO_OP_SERVER_LISTENER)).serverMonitorListenerList(Collections.singletonList(EventListenerHelper.NO_OP_SERVER_MONITOR_LISTENER)).build();
      } else {
         AsynchronousClusterEventListener clusterEventListener = AsynchronousClusterEventListener.startNew(clusterId, getClusterListener(originalClusterSettings), getServerListener(originalServerSettings), getServerMonitorListener(originalServerSettings));
         clusterSettings = ClusterSettings.builder(originalClusterSettings).clusterListenerList(Collections.singletonList(clusterEventListener)).build();
         serverSettings = ServerSettings.builder(originalServerSettings).serverListenerList(Collections.singletonList(clusterEventListener)).serverMonitorListenerList(Collections.singletonList(clusterEventListener)).build();
      }

      DnsSrvRecordMonitorFactory dnsSrvRecordMonitorFactory = new DefaultDnsSrvRecordMonitorFactory(clusterId, serverSettings, dnsClient);
      if (clusterSettings.getMode() == ClusterConnectionMode.LOAD_BALANCED) {
         ClusterableServerFactory serverFactory = new LoadBalancedClusterableServerFactory(serverSettings, connectionPoolSettings, internalConnectionPoolSettings, streamFactory, credential, loggerSettings, commandListener, applicationName, mongoDriverInformation != null ? mongoDriverInformation : MongoDriverInformation.builder().build(), compressorList, serverApi, inetAddressResolver);
         return new LoadBalancedCluster(clusterId, clusterSettings, serverFactory, dnsSrvRecordMonitorFactory);
      } else {
         ClusterableServerFactory serverFactory = new DefaultClusterableServerFactory(serverSettings, connectionPoolSettings, internalConnectionPoolSettings, streamFactory, heartbeatStreamFactory, credential, loggerSettings, commandListener, applicationName, mongoDriverInformation != null ? mongoDriverInformation : MongoDriverInformation.builder().build(), compressorList, serverApi, inetAddressResolver);
         if (clusterSettings.getMode() == ClusterConnectionMode.SINGLE) {
            return new SingleServerCluster(clusterId, clusterSettings, serverFactory);
         } else if (clusterSettings.getMode() == ClusterConnectionMode.MULTIPLE) {
            return (Cluster)(clusterSettings.getSrvHost() == null ? new MultiServerCluster(clusterId, clusterSettings, serverFactory) : new DnsMultiServerCluster(clusterId, clusterSettings, serverFactory, dnsSrvRecordMonitorFactory));
         } else {
            throw new UnsupportedOperationException("Unsupported cluster mode: " + clusterSettings.getMode());
         }
      }
   }

   private boolean noClusterEventListeners(ClusterSettings clusterSettings, ServerSettings serverSettings) {
      return clusterSettings.getClusterListeners().isEmpty() && serverSettings.getServerListeners().isEmpty() && serverSettings.getServerMonitorListeners().isEmpty();
   }

   private static ClusterListener getClusterListener(ClusterSettings clusterSettings) {
      return clusterSettings.getClusterListeners().size() == 0 ? EventListenerHelper.NO_OP_CLUSTER_LISTENER : EventListenerHelper.clusterListenerMulticaster(clusterSettings.getClusterListeners());
   }

   private static ServerListener getServerListener(ServerSettings serverSettings) {
      return serverSettings.getServerListeners().size() == 0 ? EventListenerHelper.NO_OP_SERVER_LISTENER : EventListenerHelper.serverListenerMulticaster(serverSettings.getServerListeners());
   }

   private static ServerMonitorListener getServerMonitorListener(ServerSettings serverSettings) {
      return serverSettings.getServerMonitorListeners().size() == 0 ? EventListenerHelper.NO_OP_SERVER_MONITOR_LISTENER : EventListenerHelper.serverMonitorListenerMulticaster(serverSettings.getServerMonitorListeners());
   }

   public void detectAndLogClusterEnvironment(ClusterSettings clusterSettings) {
      String srvHost = clusterSettings.getSrvHost();
      DefaultClusterFactory.ClusterEnvironment clusterEnvironment;
      if (srvHost != null) {
         clusterEnvironment = DefaultClusterFactory.ClusterEnvironment.detectCluster(srvHost);
      } else {
         clusterEnvironment = DefaultClusterFactory.ClusterEnvironment.detectCluster((String[])clusterSettings.getHosts().stream().map(ServerAddress::getHost).toArray((x$0) -> {
            return new String[x$0];
         }));
      }

      if (clusterEnvironment != null) {
         LOGGER.info(String.format("You appear to be connected to a %s cluster. For more information regarding feature compatibility and support please visit %s", clusterEnvironment.clusterProductName, clusterEnvironment.documentationUrl));
      }

   }

   static enum ClusterEnvironment {
      AZURE("https://www.mongodb.com/supportability/cosmosdb", "CosmosDB", new String[]{".cosmos.azure.com"}),
      AWS("https://www.mongodb.com/supportability/documentdb", "DocumentDB", new String[]{".docdb.amazonaws.com", ".docdb-elastic.amazonaws.com"});

      private final String documentationUrl;
      private final String clusterProductName;
      private final String[] hostSuffixes;

      private ClusterEnvironment(String url, String name, String... hostSuffixes) {
         this.hostSuffixes = hostSuffixes;
         this.documentationUrl = url;
         this.clusterProductName = name;
      }

      @Nullable
      public static DefaultClusterFactory.ClusterEnvironment detectCluster(String... hosts) {
         String[] var1 = hosts;
         int var2 = hosts.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String host = var1[var3];
            DefaultClusterFactory.ClusterEnvironment[] var5 = values();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               DefaultClusterFactory.ClusterEnvironment clusterEnvironment = var5[var7];
               if (clusterEnvironment.isExternalClusterProvider(host)) {
                  return clusterEnvironment;
               }
            }
         }

         return null;
      }

      private boolean isExternalClusterProvider(String host) {
         String[] var2 = this.hostSuffixes;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String hostSuffix = var2[var4];
            String lowerCaseHost = host.toLowerCase();
            if (lowerCaseHost.endsWith(hostSuffix)) {
               return true;
            }
         }

         return false;
      }

      // $FF: synthetic method
      private static DefaultClusterFactory.ClusterEnvironment[] $values() {
         return new DefaultClusterFactory.ClusterEnvironment[]{AZURE, AWS};
      }
   }
}
