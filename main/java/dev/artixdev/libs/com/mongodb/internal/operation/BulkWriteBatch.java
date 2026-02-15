package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import dev.artixdev.libs.com.mongodb.MongoBulkWriteException;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteError;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteInsert;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteResult;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteUpsert;
import dev.artixdev.libs.com.mongodb.bulk.WriteConcernError;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.bulk.DeleteRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.UpdateRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.WriteRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.WriteRequestWithIndex;
import dev.artixdev.libs.com.mongodb.internal.connection.BulkWriteBatchCombiner;
import dev.artixdev.libs.com.mongodb.internal.connection.IndexMap;
import dev.artixdev.libs.com.mongodb.internal.connection.SplittablePayload;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.internal.validator.MappedFieldNameValidator;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.com.mongodb.internal.validator.ReplacingDocumentFieldNameValidator;
import dev.artixdev.libs.com.mongodb.internal.validator.UpdateFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public final class BulkWriteBatch {
   private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
   private static final Decoder<BsonDocument> DECODER;
   private static final FieldNameValidator NO_OP_FIELD_NAME_VALIDATOR;
   private final MongoNamespace namespace;
   private final ConnectionDescription connectionDescription;
   private final boolean ordered;
   private final WriteConcern writeConcern;
   private final Boolean bypassDocumentValidation;
   private final boolean retryWrites;
   private final BulkWriteBatchCombiner bulkWriteBatchCombiner;
   private final IndexMap indexMap;
   private final WriteRequest.Type batchType;
   private final BsonDocument command;
   private final SplittablePayload payload;
   private final List<WriteRequestWithIndex> unprocessed;
   private final SessionContext sessionContext;
   private final BsonValue comment;
   private final BsonDocument variables;

   static BulkWriteBatch createBulkWriteBatch(MongoNamespace namespace, ConnectionDescription connectionDescription, boolean ordered, WriteConcern writeConcern, Boolean bypassDocumentValidation, boolean retryWrites, List<? extends WriteRequest> writeRequests, SessionContext sessionContext, @Nullable BsonValue comment, @Nullable BsonDocument variables) {
      if (sessionContext.hasSession() && !sessionContext.isImplicitSession() && !sessionContext.hasActiveTransaction() && !writeConcern.isAcknowledged()) {
         throw new MongoClientException("Unacknowledged writes are not supported when using an explicit session");
      } else {
         boolean canRetryWrites = OperationHelper.isRetryableWrite(retryWrites, writeConcern, connectionDescription, sessionContext);
         List<WriteRequestWithIndex> writeRequestsWithIndex = new ArrayList();
         boolean writeRequestsAreRetryable = true;

         for(int i = 0; i < writeRequests.size(); ++i) {
            WriteRequest writeRequest = (WriteRequest)writeRequests.get(i);
            writeRequestsAreRetryable = writeRequestsAreRetryable && isRetryable(writeRequest);
            writeRequestsWithIndex.add(new WriteRequestWithIndex(writeRequest, i));
         }

         if (canRetryWrites && !writeRequestsAreRetryable) {
            canRetryWrites = false;
            OperationHelper.LOGGER.debug("retryWrites set but one or more writeRequests do not support retryable writes");
         }

         return new BulkWriteBatch(namespace, connectionDescription, ordered, writeConcern, bypassDocumentValidation, canRetryWrites, new BulkWriteBatchCombiner(connectionDescription.getServerAddress(), ordered, writeConcern), writeRequestsWithIndex, sessionContext, comment, variables);
      }
   }

   private BulkWriteBatch(MongoNamespace namespace, ConnectionDescription connectionDescription, boolean ordered, WriteConcern writeConcern, @Nullable Boolean bypassDocumentValidation, boolean retryWrites, BulkWriteBatchCombiner bulkWriteBatchCombiner, List<WriteRequestWithIndex> writeRequestsWithIndices, SessionContext sessionContext, @Nullable BsonValue comment, @Nullable BsonDocument variables) {
      this.namespace = namespace;
      this.connectionDescription = connectionDescription;
      this.ordered = ordered;
      this.writeConcern = writeConcern;
      this.bypassDocumentValidation = bypassDocumentValidation;
      this.bulkWriteBatchCombiner = bulkWriteBatchCombiner;
      this.batchType = writeRequestsWithIndices.isEmpty() ? WriteRequest.Type.INSERT : ((WriteRequestWithIndex)writeRequestsWithIndices.get(0)).getType();
      this.retryWrites = retryWrites;
      List<WriteRequestWithIndex> payloadItems = new ArrayList();
      List<WriteRequestWithIndex> unprocessedItems = new ArrayList();
      IndexMap indexMap = IndexMap.create();

      for(int i = 0; i < writeRequestsWithIndices.size(); ++i) {
         WriteRequestWithIndex writeRequestWithIndex = (WriteRequestWithIndex)writeRequestsWithIndices.get(i);
         if (writeRequestWithIndex.getType() != this.batchType) {
            if (ordered) {
               unprocessedItems.addAll(writeRequestsWithIndices.subList(i, writeRequestsWithIndices.size()));
               break;
            }

            unprocessedItems.add(writeRequestWithIndex);
         } else {
            indexMap = indexMap.add(payloadItems.size(), writeRequestWithIndex.getIndex());
            payloadItems.add(writeRequestWithIndex);
         }
      }

      this.indexMap = indexMap;
      this.unprocessed = unprocessedItems;
      this.payload = new SplittablePayload(this.getPayloadType(this.batchType), payloadItems);
      this.sessionContext = sessionContext;
      this.comment = comment;
      this.variables = variables;
      this.command = new BsonDocument();
      if (!payloadItems.isEmpty()) {
         this.command.put((String)this.getCommandName(this.batchType), (BsonValue)(new BsonString(namespace.getCollectionName())));
         this.command.put((String)"ordered", (BsonValue)(new BsonBoolean(ordered)));
         if (!writeConcern.isServerDefault() && !sessionContext.hasActiveTransaction()) {
            this.command.put((String)"writeConcern", (BsonValue)writeConcern.asDocument());
         }

         if (bypassDocumentValidation != null) {
            this.command.put((String)"bypassDocumentValidation", (BsonValue)(new BsonBoolean(bypassDocumentValidation)));
         }

         DocumentHelper.putIfNotNull(this.command, "comment", comment);
         DocumentHelper.putIfNotNull(this.command, "let", (BsonValue)variables);
         if (retryWrites) {
            this.command.put((String)"txnNumber", (BsonValue)(new BsonInt64(sessionContext.advanceTransactionNumber())));
         }
      }

   }

   private BulkWriteBatch(MongoNamespace namespace, ConnectionDescription connectionDescription, boolean ordered, WriteConcern writeConcern, Boolean bypassDocumentValidation, boolean retryWrites, BulkWriteBatchCombiner bulkWriteBatchCombiner, IndexMap indexMap, WriteRequest.Type batchType, BsonDocument command, SplittablePayload payload, List<WriteRequestWithIndex> unprocessed, SessionContext sessionContext, @Nullable BsonValue comment, @Nullable BsonDocument variables) {
      this.namespace = namespace;
      this.connectionDescription = connectionDescription;
      this.ordered = ordered;
      this.writeConcern = writeConcern;
      this.bypassDocumentValidation = bypassDocumentValidation;
      this.bulkWriteBatchCombiner = bulkWriteBatchCombiner;
      this.indexMap = indexMap;
      this.batchType = batchType;
      this.payload = payload;
      this.unprocessed = unprocessed;
      this.retryWrites = retryWrites;
      this.sessionContext = sessionContext;
      this.comment = comment;
      this.variables = variables;
      if (retryWrites) {
         command.put((String)"txnNumber", (BsonValue)(new BsonInt64(sessionContext.advanceTransactionNumber())));
      }

      this.command = command;
   }

   void addResult(@Nullable BsonDocument result) {
      if (this.writeConcern.isAcknowledged()) {
         if (this.hasError((BsonDocument)Assertions.assertNotNull(result))) {
            MongoBulkWriteException bulkWriteException = this.getBulkWriteException(result);
            this.bulkWriteBatchCombiner.addErrorResult(bulkWriteException, this.indexMap);
         } else {
            this.bulkWriteBatchCombiner.addResult(this.getBulkWriteResult(result));
         }
      }

   }

   boolean getRetryWrites() {
      return this.retryWrites;
   }

   BsonDocument getCommand() {
      return this.command;
   }

   SplittablePayload getPayload() {
      return this.payload;
   }

   Decoder<BsonDocument> getDecoder() {
      return DECODER;
   }

   BulkWriteResult getResult() {
      return this.bulkWriteBatchCombiner.getResult();
   }

   boolean hasErrors() {
      return this.bulkWriteBatchCombiner.hasErrors();
   }

   @Nullable
   MongoBulkWriteException getError() {
      return this.bulkWriteBatchCombiner.getError();
   }

   boolean shouldProcessBatch() {
      return !this.bulkWriteBatchCombiner.shouldStopSendingMoreBatches() && !this.payload.isEmpty();
   }

   boolean hasAnotherBatch() {
      return !this.unprocessed.isEmpty();
   }

   BulkWriteBatch getNextBatch() {
      if (!this.payload.hasAnotherSplit()) {
         return new BulkWriteBatch(this.namespace, this.connectionDescription, this.ordered, this.writeConcern, this.bypassDocumentValidation, this.retryWrites, this.bulkWriteBatchCombiner, this.unprocessed, this.sessionContext, this.comment, this.variables);
      } else {
         IndexMap nextIndexMap = IndexMap.create();
         int newIndex = 0;

         for(int i = this.payload.getPosition(); i < this.payload.size(); ++i) {
            nextIndexMap = nextIndexMap.add(newIndex, this.indexMap.map(i));
            ++newIndex;
         }

         return new BulkWriteBatch(this.namespace, this.connectionDescription, this.ordered, this.writeConcern, this.bypassDocumentValidation, this.retryWrites, this.bulkWriteBatchCombiner, nextIndexMap, this.batchType, this.command, this.payload.getNextSplit(), this.unprocessed, this.sessionContext, this.comment, this.variables);
      }
   }

   FieldNameValidator getFieldNameValidator() {
      if (this.batchType != WriteRequest.Type.UPDATE && this.batchType != WriteRequest.Type.REPLACE) {
         return NO_OP_FIELD_NAME_VALIDATOR;
      } else {
         Map<String, FieldNameValidator> rootMap = new HashMap();
         if (this.batchType == WriteRequest.Type.REPLACE) {
            rootMap.put("u", new ReplacingDocumentFieldNameValidator());
         } else {
            rootMap.put("u", new UpdateFieldNameValidator());
         }

         return new MappedFieldNameValidator(NO_OP_FIELD_NAME_VALIDATOR, rootMap);
      }
   }

   private BulkWriteResult getBulkWriteResult(BsonDocument result) {
      int count = result.getNumber("n").intValue();
      List<BulkWriteInsert> insertedItems = this.getInsertedItems(result);
      List<BulkWriteUpsert> upsertedItems = this.getUpsertedItems(result);
      return BulkWriteResult.acknowledged(this.batchType, count - upsertedItems.size(), this.getModifiedCount(result), upsertedItems, insertedItems);
   }

   private List<BulkWriteInsert> getInsertedItems(BsonDocument result) {
      Set<Integer> writeErrors = (Set)this.getWriteErrors(result).stream().map(BulkWriteError::getIndex).collect(Collectors.toSet());
      return (List)this.payload.getInsertedIds().entrySet().stream().filter((entry) -> {
         return !writeErrors.contains(entry.getKey());
      }).map((entry) -> {
         return new BulkWriteInsert((Integer)entry.getKey(), (BsonValue)entry.getValue());
      }).collect(Collectors.toList());
   }

   private List<BulkWriteUpsert> getUpsertedItems(BsonDocument result) {
      BsonArray upsertedValue = result.getArray("upserted", new BsonArray());
      List<BulkWriteUpsert> bulkWriteUpsertList = new ArrayList();
      Iterator var4 = upsertedValue.iterator();

      while(var4.hasNext()) {
         BsonValue upsertedItem = (BsonValue)var4.next();
         BsonDocument upsertedItemDocument = (BsonDocument)upsertedItem;
         bulkWriteUpsertList.add(new BulkWriteUpsert(this.indexMap.map(upsertedItemDocument.getNumber("index").intValue()), upsertedItemDocument.get("_id")));
      }

      return bulkWriteUpsertList;
   }

   private int getModifiedCount(BsonDocument result) {
      return result.getNumber("nModified", new BsonInt32(0)).intValue();
   }

   private boolean hasError(BsonDocument result) {
      return result.get("writeErrors") != null || result.get("writeConcernError") != null;
   }

   private MongoBulkWriteException getBulkWriteException(BsonDocument result) {
      if (!this.hasError(result)) {
         throw new MongoInternalException("This method should not have been called");
      } else {
         return new MongoBulkWriteException(this.getBulkWriteResult(result), this.getWriteErrors(result), this.getWriteConcernError(result), this.connectionDescription.getServerAddress(), (Set)result.getArray("errorLabels", new BsonArray()).stream().map((i) -> {
            return i.asString().getValue();
         }).collect(Collectors.toSet()));
      }
   }

   private List<BulkWriteError> getWriteErrors(BsonDocument result) {
      List<BulkWriteError> writeErrors = new ArrayList();
      BsonArray writeErrorsDocuments = (BsonArray)result.get("writeErrors");
      if (writeErrorsDocuments != null) {
         Iterator var4 = writeErrorsDocuments.iterator();

         while(var4.hasNext()) {
            BsonValue cur = (BsonValue)var4.next();
            BsonDocument curDocument = (BsonDocument)cur;
            writeErrors.add(new BulkWriteError(curDocument.getNumber("code").intValue(), curDocument.getString("errmsg").getValue(), curDocument.getDocument("errInfo", new BsonDocument()), curDocument.getNumber("index").intValue()));
         }
      }

      return writeErrors;
   }

   @Nullable
   private WriteConcernError getWriteConcernError(BsonDocument result) {
      BsonDocument writeConcernErrorDocument = (BsonDocument)result.get("writeConcernError");
      return writeConcernErrorDocument == null ? null : WriteConcernHelper.createWriteConcernError(writeConcernErrorDocument);
   }

   private String getCommandName(WriteRequest.Type batchType) {
      if (batchType == WriteRequest.Type.INSERT) {
         return "insert";
      } else {
         return batchType != WriteRequest.Type.UPDATE && batchType != WriteRequest.Type.REPLACE ? "delete" : "update";
      }
   }

   private SplittablePayload.Type getPayloadType(WriteRequest.Type batchType) {
      if (batchType == WriteRequest.Type.INSERT) {
         return SplittablePayload.Type.INSERT;
      } else if (batchType == WriteRequest.Type.UPDATE) {
         return SplittablePayload.Type.UPDATE;
      } else {
         return batchType == WriteRequest.Type.REPLACE ? SplittablePayload.Type.REPLACE : SplittablePayload.Type.DELETE;
      }
   }

   private static boolean isRetryable(WriteRequest writeRequest) {
      if (writeRequest.getType() != WriteRequest.Type.UPDATE && writeRequest.getType() != WriteRequest.Type.REPLACE) {
         if (writeRequest.getType() == WriteRequest.Type.DELETE) {
            return !((DeleteRequest)writeRequest).isMulti();
         } else {
            return true;
         }
      } else {
         return !((UpdateRequest)writeRequest).isMulti();
      }
   }

   static {
      DECODER = REGISTRY.get(BsonDocument.class);
      NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
   }
}
