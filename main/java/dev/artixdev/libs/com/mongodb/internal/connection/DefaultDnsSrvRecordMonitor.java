package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ClusterType;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.dns.DnsResolver;

class DefaultDnsSrvRecordMonitor implements DnsSrvRecordMonitor {
   private static final Logger LOGGER = Loggers.getLogger("cluster");
   private final String hostName;
   private final String srvServiceName;
   private final long rescanFrequencyMillis;
   private final long noRecordsRescanFrequencyMillis;
   private final DnsSrvRecordInitializer dnsSrvRecordInitializer;
   private final DnsResolver dnsResolver;
   private final Thread monitorThread;
   private volatile boolean isClosed;

   DefaultDnsSrvRecordMonitor(String hostName, String srvServiceName, long rescanFrequencyMillis, long noRecordsRescanFrequencyMillis, DnsSrvRecordInitializer dnsSrvRecordInitializer, ClusterId clusterId, DnsResolver dnsResolver) {
      this.hostName = hostName;
      this.srvServiceName = srvServiceName;
      this.rescanFrequencyMillis = rescanFrequencyMillis;
      this.noRecordsRescanFrequencyMillis = noRecordsRescanFrequencyMillis;
      this.dnsSrvRecordInitializer = dnsSrvRecordInitializer;
      this.dnsResolver = dnsResolver;
      this.monitorThread = new Thread(new DefaultDnsSrvRecordMonitor.DnsSrvRecordMonitorRunnable(), "cluster-" + clusterId + "-srv-" + hostName);
      this.monitorThread.setDaemon(true);
   }

   public void start() {
      this.monitorThread.start();
   }

   public void close() {
      this.isClosed = true;
      this.monitorThread.interrupt();
   }

   private class DnsSrvRecordMonitorRunnable implements Runnable {
      private Set<ServerAddress> currentHosts;
      private ClusterType clusterType;

      private DnsSrvRecordMonitorRunnable() {
         this.currentHosts = Collections.emptySet();
         this.clusterType = ClusterType.UNKNOWN;
      }

      public void run() {
         for(; !DefaultDnsSrvRecordMonitor.this.isClosed && this.shouldContinueMonitoring(); this.clusterType = DefaultDnsSrvRecordMonitor.this.dnsSrvRecordInitializer.getClusterType()) {
            try {
               List<String> resolvedHostNames = DefaultDnsSrvRecordMonitor.this.dnsResolver.resolveHostFromSrvRecords(DefaultDnsSrvRecordMonitor.this.hostName, DefaultDnsSrvRecordMonitor.this.srvServiceName);
               Set<ServerAddress> hosts = this.createServerAddressSet(resolvedHostNames);
               if (DefaultDnsSrvRecordMonitor.this.isClosed) {
                  return;
               }

               if (!hosts.equals(this.currentHosts)) {
                  try {
                     DefaultDnsSrvRecordMonitor.this.dnsSrvRecordInitializer.initialize((Collection)Collections.unmodifiableSet(hosts));
                     this.currentHosts = hosts;
                  } catch (Exception e) {
                     DefaultDnsSrvRecordMonitor.LOGGER.warn("Exception in monitor thread during notification of DNS resolution state change", e);
                  }
               }
            } catch (MongoException e) {
               if (this.currentHosts.isEmpty()) {
                  DefaultDnsSrvRecordMonitor.this.dnsSrvRecordInitializer.initialize(e);
               }

               DefaultDnsSrvRecordMonitor.LOGGER.info("Exception while resolving SRV records", e);
            } catch (Exception e) {
               if (this.currentHosts.isEmpty()) {
                  DefaultDnsSrvRecordMonitor.this.dnsSrvRecordInitializer.initialize((MongoException)(new MongoInternalException("Unexpected runtime exception", e)));
               }

               DefaultDnsSrvRecordMonitor.LOGGER.info("Unexpected runtime exception while resolving SRV record", e);
            }

            try {
               Thread.sleep(this.getRescanFrequencyMillis());
            } catch (InterruptedException ignored) {
            }
         }

      }

      private boolean shouldContinueMonitoring() {
         return this.clusterType == ClusterType.UNKNOWN || this.clusterType == ClusterType.SHARDED;
      }

      private long getRescanFrequencyMillis() {
         return this.currentHosts.isEmpty() ? DefaultDnsSrvRecordMonitor.this.noRecordsRescanFrequencyMillis : DefaultDnsSrvRecordMonitor.this.rescanFrequencyMillis;
      }

      private Set<ServerAddress> createServerAddressSet(List<String> resolvedHostNames) {
         Set<ServerAddress> hosts = new HashSet(resolvedHostNames.size());
         Iterator var3 = resolvedHostNames.iterator();

         while(var3.hasNext()) {
            String host = (String)var3.next();
            hosts.add(ServerAddressHelper.createServerAddress(host));
         }

         return hosts;
      }

      // $FF: synthetic method
      DnsSrvRecordMonitorRunnable(Object x1) {
         this();
      }
   }
}
