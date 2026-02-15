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
import dev.artixdev.libs.com.mongodb.connection.ConnectionPoolSettings;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.connection.ServerSettings;
import dev.artixdev.libs.com.mongodb.connection.StreamFactory;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.event.ServerListener;
import dev.artixdev.libs.com.mongodb.internal.event.EventListenerHelper;
import dev.artixdev.libs.com.mongodb.internal.inject.SameObjectProvider;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.spi.dns.InetAddressResolver;

public class DefaultClusterableServerFactory implements ClusterableServerFactory {
   private final ServerSettings serverSettings;
   private final ConnectionPoolSettings connectionPoolSettings;
   private final InternalConnectionPoolSettings internalConnectionPoolSettings;
   private final StreamFactory streamFactory;
   private final MongoCredentialWithCache credential;
   private final StreamFactory heartbeatStreamFactory;
   private final LoggerSettings loggerSettings;
   private final CommandListener commandListener;
   private final String applicationName;
   private final MongoDriverInformation mongoDriverInformation;
   private final List<MongoCompressor> compressorList;
   @Nullable
   private final ServerApi serverApi;
   @Nullable
   private final InetAddressResolver inetAddressResolver;

   public DefaultClusterableServerFactory(ServerSettings serverSettings, ConnectionPoolSettings connectionPoolSettings, InternalConnectionPoolSettings internalConnectionPoolSettings, StreamFactory streamFactory, StreamFactory heartbeatStreamFactory, @Nullable MongoCredential credential, LoggerSettings loggerSettings, @Nullable CommandListener commandListener, @Nullable String applicationName, @Nullable MongoDriverInformation mongoDriverInformation, List<MongoCompressor> compressorList, @Nullable ServerApi serverApi, @Nullable InetAddressResolver inetAddressResolver) {
      this.serverSettings = serverSettings;
      this.connectionPoolSettings = connectionPoolSettings;
      this.internalConnectionPoolSettings = internalConnectionPoolSettings;
      this.streamFactory = streamFactory;
      this.credential = credential == null ? null : new MongoCredentialWithCache(credential);
      this.heartbeatStreamFactory = heartbeatStreamFactory;
      this.loggerSettings = loggerSettings;
      this.commandListener = commandListener;
      this.applicationName = applicationName;
      this.mongoDriverInformation = mongoDriverInformation;
      this.compressorList = compressorList;
      this.serverApi = serverApi;
      this.inetAddressResolver = inetAddressResolver;
   }

   public ClusterableServer create(Cluster cluster, ServerAddress serverAddress) {
      ServerId serverId = new ServerId(cluster.getClusterId(), serverAddress);
      ClusterConnectionMode clusterMode = cluster.getSettings().getMode();
      SameObjectProvider<SdamServerDescriptionManager> sdamProvider = SameObjectProvider.uninitialized();
      ServerMonitor serverMonitor = new DefaultServerMonitor(serverId, this.serverSettings, cluster.getClock(), new InternalStreamConnectionFactory(clusterMode, true, this.heartbeatStreamFactory, (MongoCredentialWithCache)null, this.applicationName, this.mongoDriverInformation, Collections.emptyList(), this.loggerSettings, (CommandListener)null, this.serverApi, this.inetAddressResolver), clusterMode, this.serverApi, sdamProvider);
      ConnectionPool connectionPool = new DefaultConnectionPool(serverId, new InternalStreamConnectionFactory(clusterMode, this.streamFactory, this.credential, this.applicationName, this.mongoDriverInformation, this.compressorList, this.loggerSettings, this.commandListener, this.serverApi, this.inetAddressResolver), this.connectionPoolSettings, this.internalConnectionPoolSettings, sdamProvider);
      ServerListener serverListener = EventListenerHelper.singleServerListener(this.serverSettings);
      SdamServerDescriptionManager sdam = new DefaultSdamServerDescriptionManager(cluster, serverId, serverListener, serverMonitor, connectionPool, clusterMode);
      sdamProvider.initialize(sdam);
      serverMonitor.start();
      return new DefaultServer(serverId, clusterMode, connectionPool, new DefaultConnectionFactory(), serverMonitor, sdam, serverListener, this.commandListener, cluster.getClock(), true);
   }

   public ServerSettings getSettings() {
      return this.serverSettings;
   }
}
