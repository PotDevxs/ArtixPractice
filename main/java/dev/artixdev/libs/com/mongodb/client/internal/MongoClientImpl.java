package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.AutoEncryptionSettings;
import dev.artixdev.libs.com.mongodb.ClientSessionOptions;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoClientSettings;
import dev.artixdev.libs.com.mongodb.MongoDriverInformation;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.TransactionOptions;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ChangeStreamIterable;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.ListDatabasesIterable;
import dev.artixdev.libs.com.mongodb.client.MongoClient;
import dev.artixdev.libs.com.mongodb.client.MongoDatabase;
import dev.artixdev.libs.com.mongodb.client.MongoIterable;
import dev.artixdev.libs.com.mongodb.client.SynchronousContextProvider;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.SocketSettings;
import dev.artixdev.libs.com.mongodb.connection.SocketStreamFactory;
import dev.artixdev.libs.com.mongodb.connection.StreamFactory;
import dev.artixdev.libs.com.mongodb.connection.StreamFactoryFactory;
import dev.artixdev.libs.com.mongodb.internal.client.model.changestream.ChangeStreamLevel;
import dev.artixdev.libs.com.mongodb.internal.connection.ClientMetadataHelper;
import dev.artixdev.libs.com.mongodb.internal.connection.Cluster;
import dev.artixdev.libs.com.mongodb.internal.connection.DefaultClusterFactory;
import dev.artixdev.libs.com.mongodb.internal.connection.InternalConnectionPoolSettings;
import dev.artixdev.libs.com.mongodb.internal.connection.StreamFactoryHelper;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.event.EventListenerHelper;
import dev.artixdev.libs.com.mongodb.internal.session.ServerSessionPool;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class MongoClientImpl implements MongoClient {
   private static final Logger LOGGER = Loggers.getLogger("client");
   private final MongoClientSettings settings;
   private final MongoDriverInformation mongoDriverInformation;
   private final MongoClientDelegate delegate;

   public MongoClientImpl(MongoClientSettings settings, MongoDriverInformation mongoDriverInformation) {
      this(createCluster(settings, mongoDriverInformation), mongoDriverInformation, settings, (OperationExecutor)null);
   }

   public MongoClientImpl(Cluster cluster, MongoDriverInformation mongoDriverInformation, MongoClientSettings settings, @Nullable OperationExecutor operationExecutor) {
      this.settings = (MongoClientSettings)Assertions.notNull("settings", settings);
      this.mongoDriverInformation = mongoDriverInformation;
      AutoEncryptionSettings autoEncryptionSettings = settings.getAutoEncryptionSettings();
      if (settings.getContextProvider() != null && !(settings.getContextProvider() instanceof SynchronousContextProvider)) {
         throw new IllegalArgumentException("The contextProvider must be an instance of " + SynchronousContextProvider.class.getName() + " when using the synchronous driver");
      } else {
         this.delegate = new MongoClientDelegate((Cluster)Assertions.notNull("cluster", cluster), CodecRegistries.withUuidRepresentation(settings.getCodecRegistry(), settings.getUuidRepresentation()), this, operationExecutor, autoEncryptionSettings == null ? null : Crypts.createCrypt(this, autoEncryptionSettings), settings.getServerApi(), (SynchronousContextProvider)settings.getContextProvider());
         BsonDocument clientMetadataDocument = ClientMetadataHelper.createClientMetadataDocument(settings.getApplicationName(), mongoDriverInformation);
         LOGGER.info(String.format("MongoClient with metadata %s created with settings %s", clientMetadataDocument.toJson(), settings));
      }
   }

   public MongoDatabase getDatabase(String databaseName) {
      return new MongoDatabaseImpl(databaseName, this.delegate.getCodecRegistry(), this.settings.getReadPreference(), this.settings.getWriteConcern(), this.settings.getRetryWrites(), this.settings.getRetryReads(), this.settings.getReadConcern(), this.settings.getUuidRepresentation(), this.settings.getAutoEncryptionSettings(), this.delegate.getOperationExecutor());
   }

   public MongoIterable<String> listDatabaseNames() {
      return this.createListDatabaseNamesIterable((ClientSession)null);
   }

   public MongoIterable<String> listDatabaseNames(ClientSession clientSession) {
      Assertions.notNull("clientSession", clientSession);
      return this.createListDatabaseNamesIterable(clientSession);
   }

   public ListDatabasesIterable<Document> listDatabases() {
      return this.listDatabases(Document.class);
   }

   public <T> ListDatabasesIterable<T> listDatabases(Class<T> clazz) {
      return this.createListDatabasesIterable((ClientSession)null, clazz);
   }

   public ListDatabasesIterable<Document> listDatabases(ClientSession clientSession) {
      return this.listDatabases(clientSession, Document.class);
   }

   public <T> ListDatabasesIterable<T> listDatabases(ClientSession clientSession, Class<T> clazz) {
      Assertions.notNull("clientSession", clientSession);
      return this.createListDatabasesIterable(clientSession, clazz);
   }

   public ClientSession startSession() {
      return this.startSession(ClientSessionOptions.builder().defaultTransactionOptions(TransactionOptions.builder().readConcern(this.settings.getReadConcern()).writeConcern(this.settings.getWriteConcern()).build()).build());
   }

   public ClientSession startSession(ClientSessionOptions options) {
      ClientSession clientSession = this.delegate.createClientSession((ClientSessionOptions)Assertions.notNull("options", options), this.settings.getReadConcern(), this.settings.getWriteConcern(), this.settings.getReadPreference());
      if (clientSession == null) {
         throw new MongoClientException("Sessions are not supported by the MongoDB cluster to which this client is connected");
      } else {
         return clientSession;
      }
   }

   public void close() {
      this.delegate.close();
   }

   public ChangeStreamIterable<Document> watch() {
      return this.watch(Collections.emptyList());
   }

   public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> resultClass) {
      return this.watch(Collections.emptyList(), resultClass);
   }

   public ChangeStreamIterable<Document> watch(List<? extends Bson> pipeline) {
      return this.watch(pipeline, Document.class);
   }

   public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> pipeline, Class<TResult> resultClass) {
      return this.createChangeStreamIterable((ClientSession)null, pipeline, resultClass);
   }

   public ChangeStreamIterable<Document> watch(ClientSession clientSession) {
      return this.watch(clientSession, Collections.emptyList(), Document.class);
   }

   public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> resultClass) {
      return this.watch(clientSession, Collections.emptyList(), resultClass);
   }

   public ChangeStreamIterable<Document> watch(ClientSession clientSession, List<? extends Bson> pipeline) {
      return this.watch(clientSession, pipeline, Document.class);
   }

   public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.createChangeStreamIterable(clientSession, pipeline, resultClass);
   }

   public ClusterDescription getClusterDescription() {
      return this.delegate.getCluster().getCurrentDescription();
   }

   private <TResult> ChangeStreamIterable<TResult> createChangeStreamIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
      return new ChangeStreamIterableImpl(clientSession, "admin", this.delegate.getCodecRegistry(), this.settings.getReadPreference(), this.settings.getReadConcern(), this.delegate.getOperationExecutor(), pipeline, resultClass, ChangeStreamLevel.CLIENT, this.settings.getRetryReads());
   }

   public Cluster getCluster() {
      return this.delegate.getCluster();
   }

   public CodecRegistry getCodecRegistry() {
      return this.delegate.getCodecRegistry();
   }

   private static Cluster createCluster(MongoClientSettings settings, @Nullable MongoDriverInformation mongoDriverInformation) {
      Assertions.notNull("settings", settings);
      return (new DefaultClusterFactory()).createCluster(settings.getClusterSettings(), settings.getServerSettings(), settings.getConnectionPoolSettings(), InternalConnectionPoolSettings.builder().build(), getStreamFactory(settings, false), getStreamFactory(settings, true), settings.getCredential(), settings.getLoggerSettings(), EventListenerHelper.getCommandListener(settings.getCommandListeners()), settings.getApplicationName(), mongoDriverInformation, settings.getCompressorList(), settings.getServerApi(), settings.getDnsClient(), settings.getInetAddressResolver());
   }

   private static StreamFactory getStreamFactory(MongoClientSettings settings, boolean isHeartbeat) {
      StreamFactoryFactory streamFactoryFactory = StreamFactoryHelper.getStreamFactoryFactoryFromSettings(settings);
      SocketSettings socketSettings = isHeartbeat ? settings.getHeartbeatSocketSettings() : settings.getSocketSettings();
      return (StreamFactory)(streamFactoryFactory == null ? new SocketStreamFactory(socketSettings, settings.getSslSettings()) : streamFactoryFactory.create(socketSettings, settings.getSslSettings()));
   }

   private <T> ListDatabasesIterable<T> createListDatabasesIterable(@Nullable ClientSession clientSession, Class<T> clazz) {
      return new ListDatabasesIterableImpl(clientSession, clazz, this.delegate.getCodecRegistry(), ReadPreference.primary(), this.delegate.getOperationExecutor(), this.settings.getRetryReads());
   }

   private MongoIterable<String> createListDatabaseNamesIterable(@Nullable ClientSession clientSession) {
      return this.createListDatabasesIterable(clientSession, BsonDocument.class).nameOnly(true).map((result) -> {
         return result.getString("name").getValue();
      });
   }

   public ServerSessionPool getServerSessionPool() {
      return this.delegate.getServerSessionPool();
   }

   public OperationExecutor getOperationExecutor() {
      return this.delegate.getOperationExecutor();
   }

   public MongoClientSettings getSettings() {
      return this.settings;
   }

   public MongoDriverInformation getMongoDriverInformation() {
      return this.mongoDriverInformation;
   }
}
