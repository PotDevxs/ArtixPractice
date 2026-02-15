package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.AutoEncryptionSettings;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.AggregateIterable;
import dev.artixdev.libs.com.mongodb.client.ChangeStreamIterable;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.ListCollectionsIterable;
import dev.artixdev.libs.com.mongodb.client.MongoCollection;
import dev.artixdev.libs.com.mongodb.client.MongoDatabase;
import dev.artixdev.libs.com.mongodb.client.MongoIterable;
import dev.artixdev.libs.com.mongodb.client.model.CreateCollectionOptions;
import dev.artixdev.libs.com.mongodb.client.model.CreateViewOptions;
import dev.artixdev.libs.com.mongodb.internal.client.model.AggregationLevel;
import dev.artixdev.libs.com.mongodb.internal.client.model.changestream.ChangeStreamLevel;
import dev.artixdev.libs.com.mongodb.internal.operation.SyncOperations;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class MongoDatabaseImpl implements MongoDatabase {
   private final String name;
   private final ReadPreference readPreference;
   private final CodecRegistry codecRegistry;
   private final WriteConcern writeConcern;
   private final boolean retryWrites;
   private final boolean retryReads;
   private final ReadConcern readConcern;
   @Nullable
   private final AutoEncryptionSettings autoEncryptionSettings;
   private final OperationExecutor executor;
   private final UuidRepresentation uuidRepresentation;
   private final SyncOperations<BsonDocument> operations;

   public MongoDatabaseImpl(String name, CodecRegistry codecRegistry, ReadPreference readPreference, WriteConcern writeConcern, boolean retryWrites, boolean retryReads, ReadConcern readConcern, UuidRepresentation uuidRepresentation, @Nullable AutoEncryptionSettings autoEncryptionSettings, OperationExecutor executor) {
      MongoNamespace.checkDatabaseNameValidity(name);
      this.name = (String)Assertions.notNull("name", name);
      this.codecRegistry = (CodecRegistry)Assertions.notNull("codecRegistry", codecRegistry);
      this.readPreference = (ReadPreference)Assertions.notNull("readPreference", readPreference);
      this.writeConcern = (WriteConcern)Assertions.notNull("writeConcern", writeConcern);
      this.retryWrites = retryWrites;
      this.retryReads = retryReads;
      this.readConcern = (ReadConcern)Assertions.notNull("readConcern", readConcern);
      this.uuidRepresentation = (UuidRepresentation)Assertions.notNull("uuidRepresentation", uuidRepresentation);
      this.autoEncryptionSettings = autoEncryptionSettings;
      this.executor = (OperationExecutor)Assertions.notNull("executor", executor);
      this.operations = new SyncOperations(new MongoNamespace(name, "$cmd"), BsonDocument.class, readPreference, codecRegistry, readConcern, writeConcern, retryWrites, retryReads);
   }

   public String getName() {
      return this.name;
   }

   public CodecRegistry getCodecRegistry() {
      return this.codecRegistry;
   }

   public ReadPreference getReadPreference() {
      return this.readPreference;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public ReadConcern getReadConcern() {
      return this.readConcern;
   }

   public MongoDatabase withCodecRegistry(CodecRegistry codecRegistry) {
      return new MongoDatabaseImpl(this.name, CodecRegistries.withUuidRepresentation(codecRegistry, this.uuidRepresentation), this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.autoEncryptionSettings, this.executor);
   }

   public MongoDatabase withReadPreference(ReadPreference readPreference) {
      return new MongoDatabaseImpl(this.name, this.codecRegistry, readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.autoEncryptionSettings, this.executor);
   }

   public MongoDatabase withWriteConcern(WriteConcern writeConcern) {
      return new MongoDatabaseImpl(this.name, this.codecRegistry, this.readPreference, writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.autoEncryptionSettings, this.executor);
   }

   public MongoDatabase withReadConcern(ReadConcern readConcern) {
      return new MongoDatabaseImpl(this.name, this.codecRegistry, this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, readConcern, this.uuidRepresentation, this.autoEncryptionSettings, this.executor);
   }

   public MongoCollection<Document> getCollection(String collectionName) {
      return this.getCollection(collectionName, Document.class);
   }

   public <TDocument> MongoCollection<TDocument> getCollection(String collectionName, Class<TDocument> documentClass) {
      return new MongoCollectionImpl(new MongoNamespace(this.name, collectionName), documentClass, this.codecRegistry, this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.autoEncryptionSettings, this.executor);
   }

   public Document runCommand(Bson command) {
      return (Document)this.runCommand(command, Document.class);
   }

   public Document runCommand(Bson command, ReadPreference readPreference) {
      return (Document)this.runCommand(command, readPreference, Document.class);
   }

   public <TResult> TResult runCommand(Bson command, Class<TResult> resultClass) {
      return this.runCommand(command, ReadPreference.primary(), resultClass);
   }

   public <TResult> TResult runCommand(Bson command, ReadPreference readPreference, Class<TResult> resultClass) {
      return this.executeCommand((ClientSession)null, command, readPreference, resultClass);
   }

   public Document runCommand(ClientSession clientSession, Bson command) {
      return (Document)this.runCommand(clientSession, command, ReadPreference.primary(), Document.class);
   }

   public Document runCommand(ClientSession clientSession, Bson command, ReadPreference readPreference) {
      return (Document)this.runCommand(clientSession, command, readPreference, Document.class);
   }

   public <TResult> TResult runCommand(ClientSession clientSession, Bson command, Class<TResult> resultClass) {
      return this.runCommand(clientSession, command, ReadPreference.primary(), resultClass);
   }

   public <TResult> TResult runCommand(ClientSession clientSession, Bson command, ReadPreference readPreference, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeCommand(clientSession, command, readPreference, resultClass);
   }

   private <TResult> TResult executeCommand(@Nullable ClientSession clientSession, Bson command, ReadPreference readPreference, Class<TResult> resultClass) {
      Assertions.notNull("readPreference", readPreference);
      if (clientSession != null && clientSession.hasActiveTransaction() && !readPreference.equals(ReadPreference.primary())) {
         throw new MongoClientException("Read preference in a transaction must be primary");
      } else {
         return this.executor.execute(this.operations.commandRead(command, resultClass), readPreference, this.readConcern, clientSession);
      }
   }

   public void drop() {
      this.executeDrop((ClientSession)null);
   }

   public void drop(ClientSession clientSession) {
      Assertions.notNull("clientSession", clientSession);
      this.executeDrop(clientSession);
   }

   private void executeDrop(@Nullable ClientSession clientSession) {
      this.executor.execute(this.operations.dropDatabase(), this.readConcern, clientSession);
   }

   public MongoIterable<String> listCollectionNames() {
      return this.createListCollectionNamesIterable((ClientSession)null);
   }

   public MongoIterable<String> listCollectionNames(ClientSession clientSession) {
      Assertions.notNull("clientSession", clientSession);
      return this.createListCollectionNamesIterable(clientSession);
   }

   private MongoIterable<String> createListCollectionNamesIterable(@Nullable ClientSession clientSession) {
      return this.createListCollectionsIterable(clientSession, BsonDocument.class, true).map((result) -> {
         return result.getString("name").getValue();
      });
   }

   public ListCollectionsIterable<Document> listCollections() {
      return this.listCollections(Document.class);
   }

   public <TResult> ListCollectionsIterable<TResult> listCollections(Class<TResult> resultClass) {
      return this.createListCollectionsIterable((ClientSession)null, resultClass, false);
   }

   public ListCollectionsIterable<Document> listCollections(ClientSession clientSession) {
      return this.listCollections(clientSession, Document.class);
   }

   public <TResult> ListCollectionsIterable<TResult> listCollections(ClientSession clientSession, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.createListCollectionsIterable(clientSession, resultClass, false);
   }

   private <TResult> ListCollectionsIterable<TResult> createListCollectionsIterable(@Nullable ClientSession clientSession, Class<TResult> resultClass, boolean collectionNamesOnly) {
      return new ListCollectionsIterableImpl(clientSession, this.name, collectionNamesOnly, resultClass, this.codecRegistry, ReadPreference.primary(), this.executor, this.retryReads);
   }

   public void createCollection(String collectionName) {
      this.createCollection(collectionName, new CreateCollectionOptions());
   }

   public void createCollection(String collectionName, CreateCollectionOptions createCollectionOptions) {
      this.executeCreateCollection((ClientSession)null, collectionName, createCollectionOptions);
   }

   public void createCollection(ClientSession clientSession, String collectionName) {
      this.createCollection(clientSession, collectionName, new CreateCollectionOptions());
   }

   public void createCollection(ClientSession clientSession, String collectionName, CreateCollectionOptions createCollectionOptions) {
      Assertions.notNull("clientSession", clientSession);
      this.executeCreateCollection(clientSession, collectionName, createCollectionOptions);
   }

   private void executeCreateCollection(@Nullable ClientSession clientSession, String collectionName, CreateCollectionOptions createCollectionOptions) {
      this.executor.execute(this.operations.createCollection(collectionName, createCollectionOptions, this.autoEncryptionSettings), this.readConcern, clientSession);
   }

   public void createView(String viewName, String viewOn, List<? extends Bson> pipeline) {
      this.createView(viewName, viewOn, pipeline, new CreateViewOptions());
   }

   public void createView(String viewName, String viewOn, List<? extends Bson> pipeline, CreateViewOptions createViewOptions) {
      this.executeCreateView((ClientSession)null, viewName, viewOn, pipeline, createViewOptions);
   }

   public void createView(ClientSession clientSession, String viewName, String viewOn, List<? extends Bson> pipeline) {
      this.createView(clientSession, viewName, viewOn, pipeline, new CreateViewOptions());
   }

   public void createView(ClientSession clientSession, String viewName, String viewOn, List<? extends Bson> pipeline, CreateViewOptions createViewOptions) {
      Assertions.notNull("clientSession", clientSession);
      this.executeCreateView(clientSession, viewName, viewOn, pipeline, createViewOptions);
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

   public AggregateIterable<Document> aggregate(List<? extends Bson> pipeline) {
      return this.aggregate(pipeline, Document.class);
   }

   public <TResult> AggregateIterable<TResult> aggregate(List<? extends Bson> pipeline, Class<TResult> resultClass) {
      return this.createAggregateIterable((ClientSession)null, pipeline, resultClass);
   }

   public AggregateIterable<Document> aggregate(ClientSession clientSession, List<? extends Bson> pipeline) {
      return this.aggregate(clientSession, pipeline, Document.class);
   }

   public <TResult> AggregateIterable<TResult> aggregate(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.createAggregateIterable(clientSession, pipeline, resultClass);
   }

   private <TResult> AggregateIterable<TResult> createAggregateIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
      return new AggregateIterableImpl(clientSession, this.name, Document.class, resultClass, this.codecRegistry, this.readPreference, this.readConcern, this.writeConcern, this.executor, pipeline, AggregationLevel.DATABASE, this.retryReads);
   }

   private <TResult> ChangeStreamIterable<TResult> createChangeStreamIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
      return new ChangeStreamIterableImpl(clientSession, this.name, this.codecRegistry, this.readPreference, this.readConcern, this.executor, pipeline, resultClass, ChangeStreamLevel.DATABASE, this.retryReads);
   }

   private void executeCreateView(@Nullable ClientSession clientSession, String viewName, String viewOn, List<? extends Bson> pipeline, CreateViewOptions createViewOptions) {
      Assertions.notNull("createViewOptions", createViewOptions);
      this.executor.execute(this.operations.createView(viewName, viewOn, pipeline, createViewOptions), this.readConcern, clientSession);
   }
}
