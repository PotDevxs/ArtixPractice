package dev.artixdev.libs.com.mongodb.client.model.changestream;

import java.util.Objects;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDateTime;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonCreator;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonExtraElements;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonId;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonIgnore;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonProperty;

public final class ChangeStreamDocument<TDocument> {
   @BsonId
   private final BsonDocument resumeToken;
   private final BsonDocument namespaceDocument;
   private final BsonDocument destinationNamespaceDocument;
   private final TDocument fullDocument;
   private final TDocument fullDocumentBeforeChange;
   private final BsonDocument documentKey;
   private final BsonTimestamp clusterTime;
   @BsonProperty("operationType")
   private final String operationTypeString;
   @BsonIgnore
   private final OperationType operationType;
   private final UpdateDescription updateDescription;
   private final BsonInt64 txnNumber;
   private final BsonDocument lsid;
   private final BsonDateTime wallTime;
   private final SplitEvent splitEvent;
   @BsonExtraElements
   private final BsonDocument extraElements;

   @BsonCreator
   public ChangeStreamDocument(@Nullable @BsonProperty("operationType") String operationTypeString, @BsonProperty("resumeToken") BsonDocument resumeToken, @Nullable @BsonProperty("ns") BsonDocument namespaceDocument, @Nullable @BsonProperty("to") BsonDocument destinationNamespaceDocument, @Nullable @BsonProperty("fullDocument") TDocument fullDocument, @Nullable @BsonProperty("fullDocumentBeforeChange") TDocument fullDocumentBeforeChange, @Nullable @BsonProperty("documentKey") BsonDocument documentKey, @Nullable @BsonProperty("clusterTime") BsonTimestamp clusterTime, @Nullable @BsonProperty("updateDescription") UpdateDescription updateDescription, @Nullable @BsonProperty("txnNumber") BsonInt64 txnNumber, @Nullable @BsonProperty("lsid") BsonDocument lsid, @Nullable @BsonProperty("wallTime") BsonDateTime wallTime, @Nullable @BsonProperty("splitEvent") SplitEvent splitEvent, @Nullable @BsonProperty BsonDocument extraElements) {
      this.resumeToken = resumeToken;
      this.namespaceDocument = namespaceDocument;
      this.destinationNamespaceDocument = destinationNamespaceDocument;
      this.fullDocumentBeforeChange = fullDocumentBeforeChange;
      this.documentKey = documentKey;
      this.fullDocument = fullDocument;
      this.clusterTime = clusterTime;
      this.operationTypeString = operationTypeString;
      this.operationType = operationTypeString == null ? null : OperationType.fromString(operationTypeString);
      this.updateDescription = updateDescription;
      this.txnNumber = txnNumber;
      this.lsid = lsid;
      this.wallTime = wallTime;
      this.splitEvent = splitEvent;
      this.extraElements = extraElements;
   }

   /** @deprecated */
   @Deprecated
   public ChangeStreamDocument(@BsonProperty("operationType") String operationTypeString, @BsonProperty("resumeToken") BsonDocument resumeToken, @Nullable @BsonProperty("ns") BsonDocument namespaceDocument, @Nullable @BsonProperty("to") BsonDocument destinationNamespaceDocument, @Nullable @BsonProperty("fullDocument") TDocument fullDocument, @Nullable @BsonProperty("fullDocumentBeforeChange") TDocument fullDocumentBeforeChange, @Nullable @BsonProperty("documentKey") BsonDocument documentKey, @Nullable @BsonProperty("clusterTime") BsonTimestamp clusterTime, @Nullable @BsonProperty("updateDescription") UpdateDescription updateDescription, @Nullable @BsonProperty("txnNumber") BsonInt64 txnNumber, @Nullable @BsonProperty("lsid") BsonDocument lsid, @Nullable @BsonProperty("wallTime") BsonDateTime wallTime, @Nullable @BsonProperty BsonDocument extraElements) {
      this(operationTypeString, resumeToken, namespaceDocument, destinationNamespaceDocument, fullDocument, fullDocumentBeforeChange, documentKey, clusterTime, updateDescription, txnNumber, lsid, wallTime, (SplitEvent)null, extraElements);
   }

   /** @deprecated */
   @Deprecated
   public ChangeStreamDocument(@BsonProperty("operationType") String operationTypeString, @BsonProperty("resumeToken") BsonDocument resumeToken, @Nullable @BsonProperty("ns") BsonDocument namespaceDocument, @Nullable @BsonProperty("to") BsonDocument destinationNamespaceDocument, @Nullable @BsonProperty("fullDocument") TDocument fullDocument, @Nullable @BsonProperty("documentKey") BsonDocument documentKey, @Nullable @BsonProperty("clusterTime") BsonTimestamp clusterTime, @Nullable @BsonProperty("updateDescription") UpdateDescription updateDescription, @Nullable @BsonProperty("txnNumber") BsonInt64 txnNumber, @Nullable @BsonProperty("lsid") BsonDocument lsid) {
      this(operationTypeString, resumeToken, namespaceDocument, destinationNamespaceDocument, fullDocument, null, documentKey, clusterTime, updateDescription, txnNumber, lsid, (BsonDateTime)null, (SplitEvent)null, (BsonDocument)null);
   }

   /** @deprecated */
   @Deprecated
   public ChangeStreamDocument(OperationType operationType, BsonDocument resumeToken, BsonDocument namespaceDocument, BsonDocument destinationNamespaceDocument, TDocument fullDocument, BsonDocument documentKey, BsonTimestamp clusterTime, UpdateDescription updateDescription, BsonInt64 txnNumber, BsonDocument lsid) {
      this(operationType.getValue(), resumeToken, namespaceDocument, destinationNamespaceDocument, fullDocument, null, documentKey, clusterTime, updateDescription, txnNumber, lsid, (BsonDateTime)null, (SplitEvent)null, (BsonDocument)null);
   }

   public BsonDocument getResumeToken() {
      return this.resumeToken;
   }

   @BsonIgnore
   @Nullable
   public MongoNamespace getNamespace() {
      if (this.namespaceDocument == null) {
         return null;
      } else {
         return this.namespaceDocument.containsKey("db") && this.namespaceDocument.containsKey("coll") ? new MongoNamespace(this.namespaceDocument.getString("db").getValue(), this.namespaceDocument.getString("coll").getValue()) : null;
      }
   }

   @BsonProperty("ns")
   @Nullable
   public BsonDocument getNamespaceDocument() {
      return this.namespaceDocument;
   }

   @BsonIgnore
   @Nullable
   public MongoNamespace getDestinationNamespace() {
      return this.destinationNamespaceDocument == null ? null : new MongoNamespace(this.destinationNamespaceDocument.getString("db").getValue(), this.destinationNamespaceDocument.getString("coll").getValue());
   }

   @BsonProperty("to")
   @Nullable
   public BsonDocument getDestinationNamespaceDocument() {
      return this.destinationNamespaceDocument;
   }

   @BsonIgnore
   @Nullable
   public String getDatabaseName() {
      if (this.namespaceDocument == null) {
         return null;
      } else {
         return !this.namespaceDocument.containsKey("db") ? null : this.namespaceDocument.getString("db").getValue();
      }
   }

   @Nullable
   public TDocument getFullDocument() {
      return this.fullDocument;
   }

   @Nullable
   public TDocument getFullDocumentBeforeChange() {
      return this.fullDocumentBeforeChange;
   }

   @Nullable
   public BsonDocument getDocumentKey() {
      return this.documentKey;
   }

   @Nullable
   public BsonTimestamp getClusterTime() {
      return this.clusterTime;
   }

   @Nullable
   public String getOperationTypeString() {
      return this.operationTypeString;
   }

   @Nullable
   public OperationType getOperationType() {
      return this.operationType;
   }

   @Nullable
   public UpdateDescription getUpdateDescription() {
      return this.updateDescription;
   }

   @Nullable
   public BsonInt64 getTxnNumber() {
      return this.txnNumber;
   }

   @Nullable
   public BsonDocument getLsid() {
      return this.lsid;
   }

   @Nullable
   public BsonDateTime getWallTime() {
      return this.wallTime;
   }

   @Nullable
   public SplitEvent getSplitEvent() {
      return this.splitEvent;
   }

   @Nullable
   public BsonDocument getExtraElements() {
      return this.extraElements;
   }

   public static <TFullDocument> Codec<ChangeStreamDocument<TFullDocument>> createCodec(Class<TFullDocument> fullDocumentClass, CodecRegistry codecRegistry) {
      return new ChangeStreamDocumentCodec(fullDocumentClass, codecRegistry);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ChangeStreamDocument<?> that = (ChangeStreamDocument)o;
         return Objects.equals(this.resumeToken, that.resumeToken) && Objects.equals(this.namespaceDocument, that.namespaceDocument) && Objects.equals(this.destinationNamespaceDocument, that.destinationNamespaceDocument) && Objects.equals(this.fullDocument, that.fullDocument) && Objects.equals(this.fullDocumentBeforeChange, that.fullDocumentBeforeChange) && Objects.equals(this.documentKey, that.documentKey) && Objects.equals(this.clusterTime, that.clusterTime) && Objects.equals(this.operationTypeString, that.operationTypeString) && Objects.equals(this.updateDescription, that.updateDescription) && Objects.equals(this.txnNumber, that.txnNumber) && Objects.equals(this.lsid, that.lsid) && Objects.equals(this.wallTime, that.wallTime) && Objects.equals(this.splitEvent, that.splitEvent) && Objects.equals(this.extraElements, that.extraElements);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.resumeToken, this.namespaceDocument, this.destinationNamespaceDocument, this.fullDocument, this.fullDocumentBeforeChange, this.documentKey, this.clusterTime, this.operationTypeString, this.updateDescription, this.txnNumber, this.lsid, this.wallTime, this.splitEvent, this.extraElements});
   }

   public String toString() {
      return "ChangeStreamDocument{ operationType=" + this.operationTypeString + ", resumeToken=" + this.resumeToken + ", namespace=" + this.getNamespace() + ", destinationNamespace=" + this.getDestinationNamespace() + ", fullDocument=" + this.fullDocument + ", fullDocumentBeforeChange=" + this.fullDocumentBeforeChange + ", documentKey=" + this.documentKey + ", clusterTime=" + this.clusterTime + ", updateDescription=" + this.updateDescription + ", txnNumber=" + this.txnNumber + ", lsid=" + this.lsid + ", splitEvent=" + this.splitEvent + ", wallTime=" + this.wallTime + "}";
   }
}
