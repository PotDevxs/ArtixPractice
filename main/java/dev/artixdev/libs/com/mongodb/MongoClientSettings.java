package dev.artixdev.libs.com.mongodb;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.gridfs.codecs.GridFSFileCodecProvider;
import dev.artixdev.libs.com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider;
import dev.artixdev.libs.com.mongodb.client.model.mql.ExpressionCodecProvider;
import dev.artixdev.libs.com.mongodb.connection.ClusterSettings;
import dev.artixdev.libs.com.mongodb.connection.ConnectionPoolSettings;
import dev.artixdev.libs.com.mongodb.connection.ServerSettings;
import dev.artixdev.libs.com.mongodb.connection.SocketSettings;
import dev.artixdev.libs.com.mongodb.connection.SslSettings;
import dev.artixdev.libs.com.mongodb.connection.StreamFactoryFactory;
import dev.artixdev.libs.com.mongodb.connection.TransportSettings;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.spi.dns.DnsClient;
import dev.artixdev.libs.com.mongodb.spi.dns.InetAddressResolver;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.codecs.BsonCodecProvider;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.CollectionCodecProvider;
import dev.artixdev.libs.org.bson.codecs.DocumentCodecProvider;
import dev.artixdev.libs.org.bson.codecs.EnumCodecProvider;
import dev.artixdev.libs.org.bson.codecs.IterableCodecProvider;
import dev.artixdev.libs.org.bson.codecs.JsonObjectCodecProvider;
import dev.artixdev.libs.org.bson.codecs.MapCodecProvider;
import dev.artixdev.libs.org.bson.codecs.ValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.codecs.jsr310.Jsr310CodecProvider;

@Immutable
public final class MongoClientSettings {
   private static final CodecRegistry DEFAULT_CODEC_REGISTRY = CodecRegistries.fromProviders(Arrays.asList(new ValueCodecProvider(), new BsonValueCodecProvider(), new DBRefCodecProvider(), new DBObjectCodecProvider(), new DocumentCodecProvider(new DocumentToDBRefTransformer()), new CollectionCodecProvider(new DocumentToDBRefTransformer()), new IterableCodecProvider(new DocumentToDBRefTransformer()), new MapCodecProvider(new DocumentToDBRefTransformer()), new GeoJsonCodecProvider(), new GridFSFileCodecProvider(), new Jsr310CodecProvider(), new JsonObjectCodecProvider(), new BsonCodecProvider(), new EnumCodecProvider(), new ExpressionCodecProvider(), new Jep395RecordCodecProvider(), new KotlinCodecProvider()));
   private final ReadPreference readPreference;
   private final WriteConcern writeConcern;
   private final boolean retryWrites;
   private final boolean retryReads;
   private final ReadConcern readConcern;
   private final MongoCredential credential;
   private final TransportSettings transportSettings;
   private final StreamFactoryFactory streamFactoryFactory;
   private final List<CommandListener> commandListeners;
   private final CodecRegistry codecRegistry;
   private final LoggerSettings loggerSettings;
   private final ClusterSettings clusterSettings;
   private final SocketSettings socketSettings;
   private final SocketSettings heartbeatSocketSettings;
   private final ConnectionPoolSettings connectionPoolSettings;
   private final ServerSettings serverSettings;
   private final SslSettings sslSettings;
   private final String applicationName;
   private final List<MongoCompressor> compressorList;
   private final UuidRepresentation uuidRepresentation;
   private final ServerApi serverApi;
   private final AutoEncryptionSettings autoEncryptionSettings;
   private final boolean heartbeatSocketTimeoutSetExplicitly;
   private final boolean heartbeatConnectTimeoutSetExplicitly;
   private final ContextProvider contextProvider;
   private final DnsClient dnsClient;
   private final InetAddressResolver inetAddressResolver;

   public static CodecRegistry getDefaultCodecRegistry() {
      return DEFAULT_CODEC_REGISTRY;
   }

   public static MongoClientSettings.Builder builder() {
      return new MongoClientSettings.Builder();
   }

   public static MongoClientSettings.Builder builder(MongoClientSettings settings) {
      return new MongoClientSettings.Builder(settings);
   }

   @Nullable
   public DnsClient getDnsClient() {
      return this.dnsClient;
   }

   @Nullable
   public InetAddressResolver getInetAddressResolver() {
      return this.inetAddressResolver;
   }

   public ReadPreference getReadPreference() {
      return this.readPreference;
   }

   @Nullable
   public MongoCredential getCredential() {
      return this.credential;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public boolean getRetryWrites() {
      return this.retryWrites;
   }

   public boolean getRetryReads() {
      return this.retryReads;
   }

   public ReadConcern getReadConcern() {
      return this.readConcern;
   }

   public CodecRegistry getCodecRegistry() {
      return this.codecRegistry;
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public StreamFactoryFactory getStreamFactoryFactory() {
      return this.streamFactoryFactory;
   }

   @Nullable
   public TransportSettings getTransportSettings() {
      return this.transportSettings;
   }

   public List<CommandListener> getCommandListeners() {
      return Collections.unmodifiableList(this.commandListeners);
   }

   @Nullable
   public String getApplicationName() {
      return this.applicationName;
   }

   public List<MongoCompressor> getCompressorList() {
      return Collections.unmodifiableList(this.compressorList);
   }

   public UuidRepresentation getUuidRepresentation() {
      return this.uuidRepresentation;
   }

   @Nullable
   public ServerApi getServerApi() {
      return this.serverApi;
   }

   @Nullable
   public AutoEncryptionSettings getAutoEncryptionSettings() {
      return this.autoEncryptionSettings;
   }

   public LoggerSettings getLoggerSettings() {
      return this.loggerSettings;
   }

   public ClusterSettings getClusterSettings() {
      return this.clusterSettings;
   }

   public SslSettings getSslSettings() {
      return this.sslSettings;
   }

   public SocketSettings getSocketSettings() {
      return this.socketSettings;
   }

   public SocketSettings getHeartbeatSocketSettings() {
      return this.heartbeatSocketSettings;
   }

   public ConnectionPoolSettings getConnectionPoolSettings() {
      return this.connectionPoolSettings;
   }

   public ServerSettings getServerSettings() {
      return this.serverSettings;
   }

   @Nullable
   public ContextProvider getContextProvider() {
      return this.contextProvider;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         MongoClientSettings that = (MongoClientSettings)o;
         return this.retryWrites == that.retryWrites && this.retryReads == that.retryReads && this.heartbeatSocketTimeoutSetExplicitly == that.heartbeatSocketTimeoutSetExplicitly && this.heartbeatConnectTimeoutSetExplicitly == that.heartbeatConnectTimeoutSetExplicitly && Objects.equals(this.readPreference, that.readPreference) && Objects.equals(this.writeConcern, that.writeConcern) && Objects.equals(this.readConcern, that.readConcern) && Objects.equals(this.credential, that.credential) && Objects.equals(this.transportSettings, that.transportSettings) && Objects.equals(this.streamFactoryFactory, that.streamFactoryFactory) && Objects.equals(this.commandListeners, that.commandListeners) && Objects.equals(this.codecRegistry, that.codecRegistry) && Objects.equals(this.loggerSettings, that.loggerSettings) && Objects.equals(this.clusterSettings, that.clusterSettings) && Objects.equals(this.socketSettings, that.socketSettings) && Objects.equals(this.heartbeatSocketSettings, that.heartbeatSocketSettings) && Objects.equals(this.connectionPoolSettings, that.connectionPoolSettings) && Objects.equals(this.serverSettings, that.serverSettings) && Objects.equals(this.sslSettings, that.sslSettings) && Objects.equals(this.applicationName, that.applicationName) && Objects.equals(this.compressorList, that.compressorList) && this.uuidRepresentation == that.uuidRepresentation && Objects.equals(this.serverApi, that.serverApi) && Objects.equals(this.autoEncryptionSettings, that.autoEncryptionSettings) && Objects.equals(this.dnsClient, that.dnsClient) && Objects.equals(this.inetAddressResolver, that.inetAddressResolver) && Objects.equals(this.contextProvider, that.contextProvider);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.credential, this.transportSettings, this.streamFactoryFactory, this.commandListeners, this.codecRegistry, this.loggerSettings, this.clusterSettings, this.socketSettings, this.heartbeatSocketSettings, this.connectionPoolSettings, this.serverSettings, this.sslSettings, this.applicationName, this.compressorList, this.uuidRepresentation, this.serverApi, this.autoEncryptionSettings, this.heartbeatSocketTimeoutSetExplicitly, this.heartbeatConnectTimeoutSetExplicitly, this.dnsClient, this.inetAddressResolver, this.contextProvider});
   }

   public String toString() {
      return "MongoClientSettings{readPreference=" + this.readPreference + ", writeConcern=" + this.writeConcern + ", retryWrites=" + this.retryWrites + ", retryReads=" + this.retryReads + ", readConcern=" + this.readConcern + ", credential=" + this.credential + ", transportSettings=" + this.transportSettings + ", streamFactoryFactory=" + this.streamFactoryFactory + ", commandListeners=" + this.commandListeners + ", codecRegistry=" + this.codecRegistry + ", loggerSettings=" + this.loggerSettings + ", clusterSettings=" + this.clusterSettings + ", socketSettings=" + this.socketSettings + ", heartbeatSocketSettings=" + this.heartbeatSocketSettings + ", connectionPoolSettings=" + this.connectionPoolSettings + ", serverSettings=" + this.serverSettings + ", sslSettings=" + this.sslSettings + ", applicationName='" + this.applicationName + '\'' + ", compressorList=" + this.compressorList + ", uuidRepresentation=" + this.uuidRepresentation + ", serverApi=" + this.serverApi + ", autoEncryptionSettings=" + this.autoEncryptionSettings + ", dnsClient=" + this.dnsClient + ", inetAddressResolver=" + this.inetAddressResolver + ", contextProvider=" + this.contextProvider + '}';
   }

   private MongoClientSettings(MongoClientSettings.Builder builder) {
      this.readPreference = builder.readPreference;
      this.writeConcern = builder.writeConcern;
      this.retryWrites = builder.retryWrites;
      this.retryReads = builder.retryReads;
      this.readConcern = builder.readConcern;
      this.credential = builder.credential;
      this.transportSettings = builder.transportSettings;
      this.streamFactoryFactory = builder.streamFactoryFactory;
      this.codecRegistry = builder.codecRegistry;
      this.commandListeners = builder.commandListeners;
      this.applicationName = builder.applicationName;
      this.loggerSettings = builder.loggerSettingsBuilder.build();
      this.clusterSettings = builder.clusterSettingsBuilder.build();
      this.serverSettings = builder.serverSettingsBuilder.build();
      this.socketSettings = builder.socketSettingsBuilder.build();
      this.connectionPoolSettings = builder.connectionPoolSettingsBuilder.build();
      this.sslSettings = builder.sslSettingsBuilder.build();
      this.compressorList = builder.compressorList;
      this.uuidRepresentation = builder.uuidRepresentation;
      this.serverApi = builder.serverApi;
      this.dnsClient = builder.dnsClient;
      this.inetAddressResolver = builder.inetAddressResolver;
      this.autoEncryptionSettings = builder.autoEncryptionSettings;
      this.heartbeatSocketSettings = SocketSettings.builder().readTimeout(builder.heartbeatSocketTimeoutMS == 0 ? this.socketSettings.getConnectTimeout(TimeUnit.MILLISECONDS) : builder.heartbeatSocketTimeoutMS, TimeUnit.MILLISECONDS).connectTimeout(builder.heartbeatConnectTimeoutMS == 0 ? this.socketSettings.getConnectTimeout(TimeUnit.MILLISECONDS) : builder.heartbeatConnectTimeoutMS, TimeUnit.MILLISECONDS).applyToProxySettings((proxyBuilder) -> {
         proxyBuilder.applySettings(this.socketSettings.getProxySettings());
      }).build();
      this.heartbeatSocketTimeoutSetExplicitly = builder.heartbeatSocketTimeoutMS != 0;
      this.heartbeatConnectTimeoutSetExplicitly = builder.heartbeatConnectTimeoutMS != 0;
      this.contextProvider = builder.contextProvider;
   }

   // $FF: synthetic method
   MongoClientSettings(MongoClientSettings.Builder x0, Object x1) {
      this(x0);
   }

   @NotThreadSafe
   public static final class Builder {
      private ReadPreference readPreference;
      private WriteConcern writeConcern;
      private boolean retryWrites;
      private boolean retryReads;
      private ReadConcern readConcern;
      private CodecRegistry codecRegistry;
      private TransportSettings transportSettings;
      private StreamFactoryFactory streamFactoryFactory;
      private List<CommandListener> commandListeners;
      private final LoggerSettings.Builder loggerSettingsBuilder;
      private final ClusterSettings.Builder clusterSettingsBuilder;
      private final SocketSettings.Builder socketSettingsBuilder;
      private final ConnectionPoolSettings.Builder connectionPoolSettingsBuilder;
      private final ServerSettings.Builder serverSettingsBuilder;
      private final SslSettings.Builder sslSettingsBuilder;
      private MongoCredential credential;
      private String applicationName;
      private List<MongoCompressor> compressorList;
      private UuidRepresentation uuidRepresentation;
      private ServerApi serverApi;
      private AutoEncryptionSettings autoEncryptionSettings;
      private int heartbeatConnectTimeoutMS;
      private int heartbeatSocketTimeoutMS;
      private ContextProvider contextProvider;
      private DnsClient dnsClient;
      private InetAddressResolver inetAddressResolver;

      private Builder() {
         this.readPreference = ReadPreference.primary();
         this.writeConcern = WriteConcern.ACKNOWLEDGED;
         this.retryWrites = true;
         this.retryReads = true;
         this.readConcern = ReadConcern.DEFAULT;
         this.codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
         this.commandListeners = new ArrayList();
         this.loggerSettingsBuilder = LoggerSettings.builder();
         this.clusterSettingsBuilder = ClusterSettings.builder();
         this.socketSettingsBuilder = SocketSettings.builder();
         this.connectionPoolSettingsBuilder = ConnectionPoolSettings.builder();
         this.serverSettingsBuilder = ServerSettings.builder();
         this.sslSettingsBuilder = SslSettings.builder();
         this.compressorList = Collections.emptyList();
         this.uuidRepresentation = UuidRepresentation.UNSPECIFIED;
      }

      private Builder(MongoClientSettings settings) {
         this.readPreference = ReadPreference.primary();
         this.writeConcern = WriteConcern.ACKNOWLEDGED;
         this.retryWrites = true;
         this.retryReads = true;
         this.readConcern = ReadConcern.DEFAULT;
         this.codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
         this.commandListeners = new ArrayList();
         this.loggerSettingsBuilder = LoggerSettings.builder();
         this.clusterSettingsBuilder = ClusterSettings.builder();
         this.socketSettingsBuilder = SocketSettings.builder();
         this.connectionPoolSettingsBuilder = ConnectionPoolSettings.builder();
         this.serverSettingsBuilder = ServerSettings.builder();
         this.sslSettingsBuilder = SslSettings.builder();
         this.compressorList = Collections.emptyList();
         this.uuidRepresentation = UuidRepresentation.UNSPECIFIED;
         Assertions.notNull("settings", settings);
         this.applicationName = settings.getApplicationName();
         this.commandListeners = new ArrayList(settings.getCommandListeners());
         this.compressorList = new ArrayList(settings.getCompressorList());
         this.codecRegistry = settings.getCodecRegistry();
         this.readPreference = settings.getReadPreference();
         this.writeConcern = settings.getWriteConcern();
         this.retryWrites = settings.getRetryWrites();
         this.retryReads = settings.getRetryReads();
         this.readConcern = settings.getReadConcern();
         this.credential = settings.getCredential();
         this.uuidRepresentation = settings.getUuidRepresentation();
         this.serverApi = settings.getServerApi();
         this.dnsClient = settings.getDnsClient();
         this.inetAddressResolver = settings.getInetAddressResolver();
         this.transportSettings = settings.getTransportSettings();
         this.streamFactoryFactory = settings.getStreamFactoryFactory();
         this.autoEncryptionSettings = settings.getAutoEncryptionSettings();
         this.contextProvider = settings.getContextProvider();
         this.loggerSettingsBuilder.applySettings(settings.getLoggerSettings());
         this.clusterSettingsBuilder.applySettings(settings.getClusterSettings());
         this.serverSettingsBuilder.applySettings(settings.getServerSettings());
         this.socketSettingsBuilder.applySettings(settings.getSocketSettings());
         this.connectionPoolSettingsBuilder.applySettings(settings.getConnectionPoolSettings());
         this.sslSettingsBuilder.applySettings(settings.getSslSettings());
         if (settings.heartbeatConnectTimeoutSetExplicitly) {
            this.heartbeatConnectTimeoutMS = settings.heartbeatSocketSettings.getConnectTimeout(TimeUnit.MILLISECONDS);
         }

         if (settings.heartbeatSocketTimeoutSetExplicitly) {
            this.heartbeatSocketTimeoutMS = settings.heartbeatSocketSettings.getReadTimeout(TimeUnit.MILLISECONDS);
         }

      }

      public MongoClientSettings.Builder applyConnectionString(ConnectionString connectionString) {
         if (connectionString.getApplicationName() != null) {
            this.applicationName = connectionString.getApplicationName();
         }

         this.clusterSettingsBuilder.applyConnectionString(connectionString);
         if (!connectionString.getCompressorList().isEmpty()) {
            this.compressorList = connectionString.getCompressorList();
         }

         this.connectionPoolSettingsBuilder.applyConnectionString(connectionString);
         if (connectionString.getCredential() != null) {
            this.credential = connectionString.getCredential();
         }

         if (connectionString.getReadConcern() != null) {
            this.readConcern = connectionString.getReadConcern();
         }

         if (connectionString.getReadPreference() != null) {
            this.readPreference = connectionString.getReadPreference();
         }

         Boolean retryWritesValue = connectionString.getRetryWritesValue();
         if (retryWritesValue != null) {
            this.retryWrites = retryWritesValue;
         }

         Boolean retryReadsValue = connectionString.getRetryReads();
         if (retryReadsValue != null) {
            this.retryReads = retryReadsValue;
         }

         if (connectionString.getUuidRepresentation() != null) {
            this.uuidRepresentation = connectionString.getUuidRepresentation();
         }

         this.serverSettingsBuilder.applyConnectionString(connectionString);
         this.socketSettingsBuilder.applyConnectionString(connectionString);
         this.sslSettingsBuilder.applyConnectionString(connectionString);
         if (connectionString.getWriteConcern() != null) {
            this.writeConcern = connectionString.getWriteConcern();
         }

         return this;
      }

      public MongoClientSettings.Builder applyToLoggerSettings(Block<LoggerSettings.Builder> block) {
         ((Block)Assertions.notNull("block", block)).apply(this.loggerSettingsBuilder);
         return this;
      }

      public MongoClientSettings.Builder applyToClusterSettings(Block<ClusterSettings.Builder> block) {
         ((Block)Assertions.notNull("block", block)).apply(this.clusterSettingsBuilder);
         return this;
      }

      public MongoClientSettings.Builder applyToSocketSettings(Block<SocketSettings.Builder> block) {
         ((Block)Assertions.notNull("block", block)).apply(this.socketSettingsBuilder);
         return this;
      }

      public MongoClientSettings.Builder applyToConnectionPoolSettings(Block<ConnectionPoolSettings.Builder> block) {
         ((Block)Assertions.notNull("block", block)).apply(this.connectionPoolSettingsBuilder);
         return this;
      }

      public MongoClientSettings.Builder applyToServerSettings(Block<ServerSettings.Builder> block) {
         ((Block)Assertions.notNull("block", block)).apply(this.serverSettingsBuilder);
         return this;
      }

      public MongoClientSettings.Builder applyToSslSettings(Block<SslSettings.Builder> block) {
         ((Block)Assertions.notNull("block", block)).apply(this.sslSettingsBuilder);
         return this;
      }

      public MongoClientSettings.Builder readPreference(ReadPreference readPreference) {
         this.readPreference = (ReadPreference)Assertions.notNull("readPreference", readPreference);
         return this;
      }

      public MongoClientSettings.Builder writeConcern(WriteConcern writeConcern) {
         this.writeConcern = (WriteConcern)Assertions.notNull("writeConcern", writeConcern);
         return this;
      }

      public MongoClientSettings.Builder retryWrites(boolean retryWrites) {
         this.retryWrites = retryWrites;
         return this;
      }

      public MongoClientSettings.Builder retryReads(boolean retryReads) {
         this.retryReads = retryReads;
         return this;
      }

      public MongoClientSettings.Builder readConcern(ReadConcern readConcern) {
         this.readConcern = (ReadConcern)Assertions.notNull("readConcern", readConcern);
         return this;
      }

      public MongoClientSettings.Builder credential(MongoCredential credential) {
         this.credential = (MongoCredential)Assertions.notNull("credential", credential);
         return this;
      }

      public MongoClientSettings.Builder codecRegistry(CodecRegistry codecRegistry) {
         this.codecRegistry = (CodecRegistry)Assertions.notNull("codecRegistry", codecRegistry);
         return this;
      }

      /** @deprecated */
      @Deprecated
      public MongoClientSettings.Builder streamFactoryFactory(StreamFactoryFactory streamFactoryFactory) {
         this.streamFactoryFactory = (StreamFactoryFactory)Assertions.notNull("streamFactoryFactory", streamFactoryFactory);
         return this;
      }

      public MongoClientSettings.Builder transportSettings(TransportSettings transportSettings) {
         this.transportSettings = (TransportSettings)Assertions.notNull("transportSettings", transportSettings);
         return this;
      }

      public MongoClientSettings.Builder addCommandListener(CommandListener commandListener) {
         Assertions.notNull("commandListener", commandListener);
         this.commandListeners.add(commandListener);
         return this;
      }

      public MongoClientSettings.Builder commandListenerList(List<CommandListener> commandListeners) {
         Assertions.notNull("commandListeners", commandListeners);
         this.commandListeners = new ArrayList(commandListeners);
         return this;
      }

      public MongoClientSettings.Builder applicationName(@Nullable String applicationName) {
         if (applicationName != null) {
            Assertions.isTrueArgument("applicationName UTF-8 encoding length <= 128", applicationName.getBytes(StandardCharsets.UTF_8).length <= 128);
         }

         this.applicationName = applicationName;
         return this;
      }

      public MongoClientSettings.Builder compressorList(List<MongoCompressor> compressorList) {
         Assertions.notNull("compressorList", compressorList);
         this.compressorList = new ArrayList(compressorList);
         return this;
      }

      public MongoClientSettings.Builder uuidRepresentation(UuidRepresentation uuidRepresentation) {
         this.uuidRepresentation = (UuidRepresentation)Assertions.notNull("uuidRepresentation", uuidRepresentation);
         return this;
      }

      public MongoClientSettings.Builder serverApi(ServerApi serverApi) {
         this.serverApi = (ServerApi)Assertions.notNull("serverApi", serverApi);
         return this;
      }

      public MongoClientSettings.Builder autoEncryptionSettings(@Nullable AutoEncryptionSettings autoEncryptionSettings) {
         this.autoEncryptionSettings = autoEncryptionSettings;
         return this;
      }

      public MongoClientSettings.Builder contextProvider(@Nullable ContextProvider contextProvider) {
         this.contextProvider = contextProvider;
         return this;
      }

      public MongoClientSettings.Builder dnsClient(@Nullable DnsClient dnsClient) {
         this.dnsClient = dnsClient;
         return this;
      }

      public MongoClientSettings.Builder inetAddressResolver(@Nullable InetAddressResolver inetAddressResolver) {
         this.inetAddressResolver = inetAddressResolver;
         return this;
      }

      MongoClientSettings.Builder heartbeatConnectTimeoutMS(int heartbeatConnectTimeoutMS) {
         this.heartbeatConnectTimeoutMS = heartbeatConnectTimeoutMS;
         return this;
      }

      MongoClientSettings.Builder heartbeatSocketTimeoutMS(int heartbeatSocketTimeoutMS) {
         this.heartbeatSocketTimeoutMS = heartbeatSocketTimeoutMS;
         return this;
      }

      public MongoClientSettings build() {
         return new MongoClientSettings(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }

      // $FF: synthetic method
      Builder(MongoClientSettings x0, Object x1) {
         this(x0);
      }
   }
}
