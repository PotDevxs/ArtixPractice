package dev.artixdev.libs.com.mongodb.internal.connection;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBinaryWriter;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonElement;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.io.BsonOutput;

public final class CommandMessage extends RequestMessage {
   private final MongoNamespace namespace;
   private final BsonDocument command;
   private final FieldNameValidator commandFieldNameValidator;
   private final ReadPreference readPreference;
   private final boolean exhaustAllowed;
   private final SplittablePayload payload;
   private final FieldNameValidator payloadFieldNameValidator;
   private final boolean responseExpected;
   private final ClusterConnectionMode clusterConnectionMode;
   private final ServerApi serverApi;

   CommandMessage(MongoNamespace namespace, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, MessageSettings settings, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      this(namespace, command, commandFieldNameValidator, readPreference, settings, true, (SplittablePayload)null, (FieldNameValidator)null, clusterConnectionMode, serverApi);
   }

   CommandMessage(MongoNamespace namespace, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, MessageSettings settings, boolean exhaustAllowed, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      this(namespace, command, commandFieldNameValidator, readPreference, settings, true, exhaustAllowed, (SplittablePayload)null, (FieldNameValidator)null, clusterConnectionMode, serverApi);
   }

   CommandMessage(MongoNamespace namespace, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, MessageSettings settings, boolean responseExpected, @Nullable SplittablePayload payload, @Nullable FieldNameValidator payloadFieldNameValidator, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      this(namespace, command, commandFieldNameValidator, readPreference, settings, responseExpected, false, payload, payloadFieldNameValidator, clusterConnectionMode, serverApi);
   }

   CommandMessage(MongoNamespace namespace, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, MessageSettings settings, boolean responseExpected, boolean exhaustAllowed, @Nullable SplittablePayload payload, @Nullable FieldNameValidator payloadFieldNameValidator, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      super(namespace.getFullName(), getOpCode(settings, clusterConnectionMode, serverApi), settings);
      this.namespace = namespace;
      this.command = command;
      this.commandFieldNameValidator = commandFieldNameValidator;
      this.readPreference = readPreference;
      this.responseExpected = responseExpected;
      this.exhaustAllowed = exhaustAllowed;
      this.payload = payload;
      this.payloadFieldNameValidator = payloadFieldNameValidator;
      this.clusterConnectionMode = (ClusterConnectionMode)Assertions.notNull("clusterConnectionMode", clusterConnectionMode);
      this.serverApi = serverApi;
   }

   BsonDocument getCommandDocument(ByteBufferBsonOutput bsonOutput) {
      ByteBufBsonDocument byteBufBsonDocument = ByteBufBsonDocument.createOne(bsonOutput, this.getEncodingMetadata().getFirstDocumentPosition());
      Object commandBsonDocument;
      if (this.containsPayload()) {
         commandBsonDocument = byteBufBsonDocument.toBaseBsonDocument();
         int payloadStartPosition = this.getEncodingMetadata().getFirstDocumentPosition() + byteBufBsonDocument.getSizeInBytes() + 1 + 4 + this.payload.getPayloadName().getBytes(StandardCharsets.UTF_8).length + 1;
         ((BsonDocument)commandBsonDocument).append(this.payload.getPayloadName(), new BsonArray(ByteBufBsonDocument.createList(bsonOutput, payloadStartPosition)));
      } else {
         commandBsonDocument = byteBufBsonDocument;
      }

      return (BsonDocument)commandBsonDocument;
   }

   boolean containsPayload() {
      return this.payload != null;
   }

   boolean isResponseExpected() {
      return !this.useOpMsg() || this.requireOpMsgResponse();
   }

   MongoNamespace getNamespace() {
      return this.namespace;
   }

   protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput, SessionContext sessionContext) {
      int messageStartPosition = bsonOutput.getPosition() - 16;
      int commandStartPosition;
      if (this.useOpMsg()) {
         int flagPosition = bsonOutput.getPosition();
         bsonOutput.writeInt32(0);
         bsonOutput.writeByte(0);
         commandStartPosition = bsonOutput.getPosition();
         this.addDocument(this.command, bsonOutput, this.commandFieldNameValidator, this.getExtraElements(sessionContext));
         if (this.payload != null) {
            bsonOutput.writeByte(1);
            int payloadBsonOutputStartPosition = bsonOutput.getPosition();
            bsonOutput.writeInt32(0);
            bsonOutput.writeCString(this.payload.getPayloadName());
            BsonWriterHelper.writePayload(new BsonBinaryWriter(bsonOutput, this.payloadFieldNameValidator), bsonOutput, this.getSettings(), messageStartPosition, this.payload, this.getSettings().getMaxDocumentSize());
            int payloadBsonOutputLength = bsonOutput.getPosition() - payloadBsonOutputStartPosition;
            bsonOutput.writeInt32(payloadBsonOutputStartPosition, payloadBsonOutputLength);
         }

         bsonOutput.writeInt32(flagPosition, this.getOpMsgFlagBits());
      } else {
         bsonOutput.writeInt32(0);
         bsonOutput.writeCString(this.namespace.getFullName());
         bsonOutput.writeInt32(0);
         bsonOutput.writeInt32(-1);
         commandStartPosition = bsonOutput.getPosition();
         List<BsonElement> elements = null;
         if (this.serverApi != null) {
            elements = new ArrayList(3);
            this.addServerApiElements(elements);
         }

         this.addDocument(this.command, bsonOutput, this.commandFieldNameValidator, elements);
      }

      return new RequestMessage.EncodingMetadata(commandStartPosition);
   }

   private int getOpMsgFlagBits() {
      int flagBits = 0;
      if (!this.requireOpMsgResponse()) {
         flagBits = 2;
      }

      if (this.exhaustAllowed) {
         flagBits |= 65536;
      }

      return flagBits;
   }

   private boolean requireOpMsgResponse() {
      if (this.responseExpected) {
         return true;
      } else {
         return this.payload != null && this.payload.hasAnotherSplit();
      }
   }

   private boolean isDirectConnectionToReplicaSetMember() {
      return this.clusterConnectionMode == ClusterConnectionMode.SINGLE && this.getSettings().getServerType() != ServerType.SHARD_ROUTER && this.getSettings().getServerType() != ServerType.STANDALONE;
   }

   private boolean useOpMsg() {
      return this.getOpCode().equals(OpCode.OP_MSG);
   }

   private List<BsonElement> getExtraElements(SessionContext sessionContext) {
      List<BsonElement> extraElements = new ArrayList();
      extraElements.add(new BsonElement("$db", new BsonString((new MongoNamespace(this.getCollectionName())).getDatabaseName())));
      if (sessionContext.getClusterTime() != null) {
         extraElements.add(new BsonElement("$clusterTime", sessionContext.getClusterTime()));
      }

      if (sessionContext.hasSession()) {
         if (!sessionContext.isImplicitSession() && !this.getSettings().isSessionSupported()) {
            throw new MongoClientException("Attempting to use a ClientSession while connected to a server that doesn't support sessions");
         }

         if (this.getSettings().isSessionSupported() && this.responseExpected) {
            extraElements.add(new BsonElement("lsid", sessionContext.getSessionId()));
         }
      }

      boolean firstMessageInTransaction = sessionContext.notifyMessageSent();
      Assertions.assertFalse(sessionContext.hasActiveTransaction() && sessionContext.isSnapshot());
      if (sessionContext.hasActiveTransaction()) {
         this.checkServerVersionForTransactionSupport();
         extraElements.add(new BsonElement("txnNumber", new BsonInt64(sessionContext.getTransactionNumber())));
         if (firstMessageInTransaction) {
            extraElements.add(new BsonElement("startTransaction", BsonBoolean.TRUE));
            this.addReadConcernDocument(extraElements, sessionContext);
         }

         extraElements.add(new BsonElement("autocommit", BsonBoolean.FALSE));
      } else if (sessionContext.isSnapshot()) {
         this.addReadConcernDocument(extraElements, sessionContext);
      }

      if (this.serverApi != null) {
         this.addServerApiElements(extraElements);
      }

      if (this.readPreference != null) {
         if (!this.readPreference.equals(ReadPreference.primary())) {
            extraElements.add(new BsonElement("$readPreference", this.readPreference.toDocument()));
         } else if (this.isDirectConnectionToReplicaSetMember()) {
            extraElements.add(new BsonElement("$readPreference", ReadPreference.primaryPreferred().toDocument()));
         }
      }

      return extraElements;
   }

   private void addServerApiElements(List<BsonElement> extraElements) {
      extraElements.add(new BsonElement("apiVersion", new BsonString(this.serverApi.getVersion().getValue())));
      if (this.serverApi.getStrict().isPresent()) {
         extraElements.add(new BsonElement("apiStrict", BsonBoolean.valueOf((Boolean)this.serverApi.getStrict().get())));
      }

      if (this.serverApi.getDeprecationErrors().isPresent()) {
         extraElements.add(new BsonElement("apiDeprecationErrors", BsonBoolean.valueOf((Boolean)this.serverApi.getDeprecationErrors().get())));
      }

   }

   private void checkServerVersionForTransactionSupport() {
      int wireVersion = this.getSettings().getMaxWireVersion();
      if (wireVersion < 7 || wireVersion < 8 && this.getSettings().getServerType() == ServerType.SHARD_ROUTER) {
         throw new MongoClientException("Transactions are not supported by the MongoDB cluster to which this client is connected.");
      }
   }

   private void addReadConcernDocument(List<BsonElement> extraElements, SessionContext sessionContext) {
      BsonDocument readConcernDocument = ReadConcernHelper.getReadConcernDocument(sessionContext, this.getSettings().getMaxWireVersion());
      if (!readConcernDocument.isEmpty()) {
         extraElements.add(new BsonElement("readConcern", readConcernDocument));
      }

   }

   private static OpCode getOpCode(MessageSettings settings, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      return !isServerVersionAtLeastThreeDotSix(settings) && clusterConnectionMode != ClusterConnectionMode.LOAD_BALANCED && serverApi == null ? OpCode.OP_QUERY : OpCode.OP_MSG;
   }

   private static boolean isServerVersionAtLeastThreeDotSix(MessageSettings settings) {
      return settings.getMaxWireVersion() >= 6;
   }
}
