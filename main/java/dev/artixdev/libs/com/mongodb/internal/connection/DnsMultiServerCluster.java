package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ClusterSettings;
import dev.artixdev.libs.com.mongodb.connection.ClusterType;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class DnsMultiServerCluster extends AbstractMultiServerCluster {
   private final DnsSrvRecordMonitor dnsSrvRecordMonitor;
   private volatile MongoException srvResolutionException;

   public DnsMultiServerCluster(ClusterId clusterId, ClusterSettings settings, ClusterableServerFactory serverFactory, DnsSrvRecordMonitorFactory dnsSrvRecordMonitorFactory) {
      super(clusterId, settings, serverFactory);
      this.dnsSrvRecordMonitor = dnsSrvRecordMonitorFactory.create((String)Assertions.assertNotNull(settings.getSrvHost()), settings.getSrvServiceName(), new DnsSrvRecordInitializer() {
         private volatile boolean initialized;

         public void initialize(Collection<ServerAddress> hosts) {
            DnsMultiServerCluster.this.srvResolutionException = null;
            if (!this.initialized) {
               this.initialized = true;
               DnsMultiServerCluster.this.initialize(this.applySrvMaxHosts(hosts));
            } else {
               DnsMultiServerCluster.this.onChange(this.applySrvMaxHosts(hosts));
            }

         }

         private Collection<ServerAddress> applySrvMaxHosts(Collection<ServerAddress> hosts) {
            Collection<ServerAddress> newHosts = hosts;
            Integer srvMaxHosts = DnsMultiServerCluster.this.getSettings().getSrvMaxHosts();
            if (srvMaxHosts != null && srvMaxHosts > 0 && srvMaxHosts < hosts.size()) {
               List<ServerAddress> newHostsList = new ArrayList(hosts);
               Collections.shuffle(newHostsList, ThreadLocalRandom.current());
               newHosts = newHostsList.subList(0, srvMaxHosts);
            }

            return (Collection)newHosts;
         }

         public void initialize(MongoException initializationException) {
            if (!this.initialized) {
               DnsMultiServerCluster.this.srvResolutionException = initializationException;
               DnsMultiServerCluster.this.initialize(Collections.emptyList());
            }

         }

         public ClusterType getClusterType() {
            return DnsMultiServerCluster.this.getClusterType();
         }
      });
      this.dnsSrvRecordMonitor.start();
   }

   @Nullable
   protected MongoException getSrvResolutionException() {
      return this.srvResolutionException;
   }

   public void close() {
      if (this.dnsSrvRecordMonitor != null) {
         this.dnsSrvRecordMonitor.close();
      }

      super.close();
   }
}
