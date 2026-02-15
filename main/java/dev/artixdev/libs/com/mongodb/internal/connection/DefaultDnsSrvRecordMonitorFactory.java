package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ServerSettings;
import dev.artixdev.libs.com.mongodb.internal.dns.DefaultDnsResolver;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.spi.dns.DnsClient;

public class DefaultDnsSrvRecordMonitorFactory implements DnsSrvRecordMonitorFactory {
   private static final long DEFAULT_RESCAN_FREQUENCY_MILLIS = 60000L;
   private final ClusterId clusterId;
   private final long noRecordsRescanFrequency;
   private final DnsClient dnsClient;

   public DefaultDnsSrvRecordMonitorFactory(ClusterId clusterId, ServerSettings serverSettings, @Nullable DnsClient dnsClient) {
      this.clusterId = clusterId;
      this.noRecordsRescanFrequency = serverSettings.getHeartbeatFrequency(TimeUnit.MILLISECONDS);
      this.dnsClient = dnsClient;
   }

   public DnsSrvRecordMonitor create(String hostName, String srvServiceName, DnsSrvRecordInitializer dnsSrvRecordInitializer) {
      return new DefaultDnsSrvRecordMonitor(hostName, srvServiceName, 60000L, this.noRecordsRescanFrequency, dnsSrvRecordInitializer, this.clusterId, new DefaultDnsResolver(this.dnsClient));
   }
}
