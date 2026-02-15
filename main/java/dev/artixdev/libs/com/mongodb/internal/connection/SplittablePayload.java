package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.internal.bulk.DeleteRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.InsertRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.UpdateRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.WriteRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.WriteRequestWithIndex;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWrapper;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.Encoder;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class SplittablePayload {
   private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
   private final SplittablePayload.WriteRequestEncoder writeRequestEncoder = new SplittablePayload.WriteRequestEncoder();
   private final SplittablePayload.Type payloadType;
   private final List<WriteRequestWithIndex> writeRequestWithIndexes;
   private final Map<Integer, BsonValue> insertedIds = new HashMap();
   private int position = 0;

   public SplittablePayload(SplittablePayload.Type payloadType, List<WriteRequestWithIndex> writeRequestWithIndexes) {
      this.payloadType = (SplittablePayload.Type)Assertions.notNull("batchType", payloadType);
      this.writeRequestWithIndexes = (List)Assertions.notNull("writeRequests", writeRequestWithIndexes);
   }

   public SplittablePayload.Type getPayloadType() {
      return this.payloadType;
   }

   public String getPayloadName() {
      if (this.payloadType == SplittablePayload.Type.INSERT) {
         return "documents";
      } else {
         return this.payloadType != SplittablePayload.Type.UPDATE && this.payloadType != SplittablePayload.Type.REPLACE ? "deletes" : "updates";
      }
   }

   boolean hasPayload() {
      return this.writeRequestWithIndexes.size() > 0;
   }

   public int size() {
      return this.writeRequestWithIndexes.size();
   }

   public Map<Integer, BsonValue> getInsertedIds() {
      return this.insertedIds;
   }

   public List<BsonDocument> getPayload() {
      return (List)this.writeRequestWithIndexes.stream().map((wri) -> {
         return new BsonDocumentWrapper(wri, this.writeRequestEncoder);
      }).collect(Collectors.toList());
   }

   public List<WriteRequestWithIndex> getWriteRequestWithIndexes() {
      return this.writeRequestWithIndexes;
   }

   public int getPosition() {
      return this.position;
   }

   public void setPosition(int position) {
      this.position = position;
   }

   public boolean hasAnotherSplit() {
      return this.writeRequestWithIndexes.size() > this.position;
   }

   public SplittablePayload getNextSplit() {
      Assertions.isTrue("hasAnotherSplit", this.hasAnotherSplit());
      List<WriteRequestWithIndex> nextPayLoad = this.writeRequestWithIndexes.subList(this.position, this.writeRequestWithIndexes.size());
      return new SplittablePayload(this.payloadType, nextPayLoad);
   }

   public boolean isEmpty() {
      return this.writeRequestWithIndexes.isEmpty();
   }

   private static Codec<BsonDocument> getCodec(BsonDocument document) {
      return (Codec<BsonDocument>) REGISTRY.get(document.getClass());
   }

   class WriteRequestEncoder implements Encoder<WriteRequestWithIndex> {
      public void encode(BsonWriter writer, WriteRequestWithIndex writeRequestWithIndex, EncoderContext encoderContext) {
         BsonDocument hintx;
         if (writeRequestWithIndex.getType() == WriteRequest.Type.INSERT) {
            InsertRequest insertRequest = (InsertRequest)writeRequestWithIndex.getWriteRequest();
            hintx = insertRequest.getDocument();
            IdHoldingBsonWriter idHoldingBsonWriter = new IdHoldingBsonWriter(writer);
            SplittablePayload.getCodec(hintx).encode(idHoldingBsonWriter, hintx, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
            SplittablePayload.this.insertedIds.put(writeRequestWithIndex.getIndex(), idHoldingBsonWriter.getId());
         } else if (writeRequestWithIndex.getType() != WriteRequest.Type.UPDATE && writeRequestWithIndex.getType() != WriteRequest.Type.REPLACE) {
            DeleteRequest deleteRequest = (DeleteRequest)writeRequestWithIndex.getWriteRequest();
            writer.writeStartDocument();
            writer.writeName("q");
            SplittablePayload.getCodec(deleteRequest.getFilter()).encode(writer, deleteRequest.getFilter(), EncoderContext.builder().build());
            writer.writeInt32("limit", deleteRequest.isMulti() ? 0 : 1);
            if (deleteRequest.getCollation() != null) {
               writer.writeName("collation");
               hintx = ((Collation)Assertions.assertNotNull(deleteRequest.getCollation())).asDocument();
               SplittablePayload.getCodec(hintx).encode(writer, hintx, EncoderContext.builder().build());
            }

            if (deleteRequest.getHint() != null) {
               writer.writeName("hint");
               hintx = ((Bson)Assertions.assertNotNull(deleteRequest.getHint())).toBsonDocument(BsonDocument.class, (CodecRegistry)null);
               SplittablePayload.getCodec(hintx).encode(writer, hintx, EncoderContext.builder().build());
            } else if (deleteRequest.getHintString() != null) {
               writer.writeString("hint", deleteRequest.getHintString());
            }

            writer.writeEndDocument();
         } else {
            UpdateRequest update = (UpdateRequest)writeRequestWithIndex.getWriteRequest();
            writer.writeStartDocument();
            writer.writeName("q");
            SplittablePayload.getCodec(update.getFilter()).encode(writer, update.getFilter(), EncoderContext.builder().build());
            BsonValue updateValue = update.getUpdateValue();
            if (!updateValue.isDocument() && !updateValue.isArray()) {
               throw new IllegalArgumentException("Invalid BSON value for an update.");
            }

            if (updateValue.isArray() && updateValue.asArray().isEmpty()) {
               throw new IllegalArgumentException("Invalid pipeline for an update. The pipeline may not be empty.");
            }

            writer.writeName("u");
            Iterator<BsonValue> iterator;
            if (updateValue.isDocument()) {
               FieldTrackingBsonWriter fieldTrackingBsonWriter = new FieldTrackingBsonWriter(writer);
               SplittablePayload.getCodec(updateValue.asDocument()).encode(fieldTrackingBsonWriter, updateValue.asDocument(), EncoderContext.builder().build());
               if (writeRequestWithIndex.getType() == WriteRequest.Type.UPDATE && !fieldTrackingBsonWriter.hasWrittenField()) {
                  throw new IllegalArgumentException("Invalid BSON document for an update. The document may not be empty.");
               }
            } else if (update.getType() == WriteRequest.Type.UPDATE && updateValue.isArray()) {
               writer.writeStartArray();
               iterator = updateValue.asArray().iterator();

               while(iterator.hasNext()) {
                  BsonValue curx = iterator.next();
                  SplittablePayload.getCodec(curx.asDocument()).encode(writer, curx.asDocument(), EncoderContext.builder().build());
               }

               writer.writeEndArray();
            }

            if (update.isMulti()) {
               writer.writeBoolean("multi", true);
            }

            if (update.isUpsert()) {
               writer.writeBoolean("upsert", true);
            }

            BsonDocument hint;
            if (update.getCollation() != null) {
               writer.writeName("collation");
               hint = ((Collation)Assertions.assertNotNull(update.getCollation())).asDocument();
               SplittablePayload.getCodec(hint).encode(writer, hint, EncoderContext.builder().build());
            }

            if (update.getArrayFilters() != null) {
               writer.writeStartArray("arrayFilters");
               Iterator<BsonDocument> arrayFiltersIterator = ((List<BsonDocument>)Assertions.assertNotNull(update.getArrayFilters())).iterator();

               while(arrayFiltersIterator.hasNext()) {
                  BsonDocument cur = arrayFiltersIterator.next();
                  SplittablePayload.getCodec(cur).encode(writer, cur, EncoderContext.builder().build());
               }

               writer.writeEndArray();
            }

            if (update.getHint() != null) {
               writer.writeName("hint");
               hint = ((Bson)Assertions.assertNotNull(update.getHint())).toBsonDocument(BsonDocument.class, (CodecRegistry)null);
               SplittablePayload.getCodec(hint).encode(writer, hint, EncoderContext.builder().build());
            } else if (update.getHintString() != null) {
               writer.writeString("hint", update.getHintString());
            }

            writer.writeEndDocument();
         }

      }

      public Class<WriteRequestWithIndex> getEncoderClass() {
         return WriteRequestWithIndex.class;
      }
   }

   public static enum Type {
      INSERT,
      UPDATE,
      REPLACE,
      DELETE;

      // $FF: synthetic method
      private static SplittablePayload.Type[] $values() {
         return new SplittablePayload.Type[]{INSERT, UPDATE, REPLACE, DELETE};
      }
   }
}
