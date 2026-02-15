package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.List;
import dev.artixdev.libs.com.mongodb.LoggerSettings;
import dev.artixdev.libs.com.mongodb.MongoCompressor;
import dev.artixdev.libs.com.mongodb.MongoCredential;
import dev.artixdev.libs.com.mongodb.MongoDriverInformation;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ConnectionPoolSettings;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.connection.ServerSettings;
import dev.artixdev.libs.com.mongodb.connection.StreamFactory;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.internal.event.EventListenerHelper;
import dev.artixdev.libs.com.mongodb.internal.inject.EmptyProvider;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.spi.dns.InetAddressResolver;

@ThreadSafe
public class LoadBalancedClusterableServerFactory implements ClusterableServerFactory {
   private final ServerSettings serverSettings;
   private final ConnectionPoolSettings connectionPoolSettings;
   private final InternalConnectionPoolSettings internalConnectionPoolSettings;
   private final StreamFactory streamFactory;
   private final MongoCredentialWithCache credential;
   private final LoggerSettings loggerSettings;
   private final CommandListener commandListener;
   private final String applicationName;
   private final MongoDriverInformation mongoDriverInformation;
   private final List<MongoCompressor> compressorList;
   private final ServerApi serverApi;
   private final InetAddressResolver inetAddressResolver;

   public LoadBalancedClusterableServerFactory(ServerSettings serverSettings, ConnectionPoolSettings connectionPoolSettings, InternalConnectionPoolSettings internalConnectionPoolSettings, StreamFactory streamFactory, @Nullable MongoCredential credential, LoggerSettings loggerSettings, @Nullable CommandListener commandListener, @Nullable String applicationName, MongoDriverInformation mongoDriverInformation, List<MongoCompressor> compressorList, @Nullable ServerApi serverApi, @Nullable InetAddressResolver inetAddressResolver) {
      this.serverSettings = serverSettings;
      this.connectionPoolSettings = connectionPoolSettings;
      this.internalConnectionPoolSettings = internalConnectionPoolSettings;
      this.streamFactory = streamFactory;
      this.credential = credential == null ? null : new MongoCredentialWithCache(credential);
      this.loggerSettings = loggerSettings;
      this.commandListener = commandListener;
      this.applicationName = applicationName;
      this.mongoDriverInformation = mongoDriverInformation;
      this.compressorList = compressorList;
      this.serverApi = serverApi;
      this.inetAddressResolver = inetAddressResolver;
   }

   public ClusterableServer create(Cluster cluster, ServerAddress serverAddress) {
      ConnectionPool connectionPool = new DefaultConnectionPool(new ServerId(cluster.getClusterId(), serverAddress), new InternalStreamConnectionFactory(ClusterConnectionMode.LOAD_BALANCED, this.streamFactory, this.credential, this.applicationName, this.mongoDriverInformation, this.compressorList, this.loggerSettings, this.commandListener, this.serverApi, this.inetAddressResolver), this.connectionPoolSettings, this.internalConnectionPoolSettings, EmptyProvider.instance());
      connectionPool.ready();
      return new LoadBalancedServer(new ServerId(cluster.getClusterId(), serverAddress), connectionPool, new DefaultConnectionFactory(), EventListenerHelper.singleServerListener(this.serverSettings), cluster.getClock());
   }

   public ServerSettings getSettings() {
      return this.serverSettings;
   }
}
