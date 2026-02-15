package dev.artixdev.libs.org.bson.codecs;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWriter;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.Transformer;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class DocumentCodec implements CollectibleCodec<Document>, OverridableUuidRepresentationCodec<Document> {
   private static final String ID_FIELD_NAME = "_id";
   private static final CodecRegistry DEFAULT_REGISTRY = CodecRegistries.fromProviders(Arrays.asList(new ValueCodecProvider(), new CollectionCodecProvider(), new IterableCodecProvider(), new BsonValueCodecProvider(), new DocumentCodecProvider(), new MapCodecProvider()));
   private static final BsonTypeCodecMap DEFAULT_BSON_TYPE_CODEC_MAP;
   private static final IdGenerator DEFAULT_ID_GENERATOR;
   private final BsonTypeCodecMap bsonTypeCodecMap;
   private final CodecRegistry registry;
   private final IdGenerator idGenerator;
   private final Transformer valueTransformer;
   private final UuidRepresentation uuidRepresentation;

   public DocumentCodec() {
      this(DEFAULT_REGISTRY, (BsonTypeCodecMap)DEFAULT_BSON_TYPE_CODEC_MAP, (Transformer)null);
   }

   public DocumentCodec(CodecRegistry registry) {
      this(registry, BsonTypeClassMap.DEFAULT_BSON_TYPE_CLASS_MAP);
   }

   public DocumentCodec(CodecRegistry registry, BsonTypeClassMap bsonTypeClassMap) {
      this(registry, (BsonTypeClassMap)bsonTypeClassMap, (Transformer)null);
   }

   public DocumentCodec(CodecRegistry registry, BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer) {
      this(registry, new BsonTypeCodecMap((BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap), registry), valueTransformer);
   }

   private DocumentCodec(CodecRegistry registry, BsonTypeCodecMap bsonTypeCodecMap, Transformer valueTransformer) {
      this(registry, bsonTypeCodecMap, DEFAULT_ID_GENERATOR, valueTransformer, UuidRepresentation.UNSPECIFIED);
   }

   private DocumentCodec(CodecRegistry registry, BsonTypeCodecMap bsonTypeCodecMap, IdGenerator idGenerator, Transformer valueTransformer, UuidRepresentation uuidRepresentation) {
      this.registry = (CodecRegistry)Assertions.notNull("registry", registry);
      this.bsonTypeCodecMap = bsonTypeCodecMap;
      this.idGenerator = idGenerator;
      this.valueTransformer = valueTransformer != null ? valueTransformer : (value) -> {
         return value;
      };
      this.uuidRepresentation = uuidRepresentation;
   }

   public Codec<Document> withUuidRepresentation(UuidRepresentation uuidRepresentation) {
      return this.uuidRepresentation.equals(uuidRepresentation) ? this : new DocumentCodec(this.registry, this.bsonTypeCodecMap, this.idGenerator, this.valueTransformer, uuidRepresentation);
   }

   public boolean documentHasId(Document document) {
      return document.containsKey("_id");
   }

   public BsonValue getDocumentId(Document document) {
      if (!this.documentHasId(document)) {
         throw new IllegalStateException("The document does not contain an _id");
      } else {
         Object id = document.get("_id");
         if (id instanceof BsonValue) {
            return (BsonValue)id;
         } else {
            BsonDocument idHoldingDocument = new BsonDocument();
            BsonWriter writer = new BsonDocumentWriter(idHoldingDocument);
            writer.writeStartDocument();
            writer.writeName("_id");
            this.writeValue(writer, EncoderContext.builder().build(), id);
            writer.writeEndDocument();
            return idHoldingDocument.get("_id");
         }
      }
   }

   public Document generateIdIfAbsentFromDocument(Document document) {
      if (!this.documentHasId(document)) {
         document.put("_id", this.idGenerator.generate());
      }

      return document;
   }

   public void encode(BsonWriter writer, Document document, EncoderContext encoderContext) {
      writer.writeStartDocument();
      this.beforeFields(writer, encoderContext, document);
      Iterator<Entry<String, Object>> iterator = document.entrySet().iterator();

      while(iterator.hasNext()) {
         Entry<String, Object> entry = iterator.next();
         if (!this.skipField(encoderContext, (String)entry.getKey())) {
            writer.writeName((String)entry.getKey());
            this.writeValue(writer, encoderContext, entry.getValue());
         }
      }

      writer.writeEndDocument();
   }

   public Document decode(BsonReader reader, DecoderContext decoderContext) {
      Document document = new Document();
      reader.readStartDocument();

      while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
         String fieldName = reader.readName();
         document.put(fieldName, ContainerCodecHelper.readValue(reader, decoderContext, this.bsonTypeCodecMap, this.uuidRepresentation, this.registry, this.valueTransformer));
      }

      reader.readEndDocument();
      return document;
   }

   public Class<Document> getEncoderClass() {
      return Document.class;
   }

   private void beforeFields(BsonWriter bsonWriter, EncoderContext encoderContext, Map<String, Object> document) {
      if (encoderContext.isEncodingCollectibleDocument() && document.containsKey("_id")) {
         bsonWriter.writeName("_id");
         this.writeValue(bsonWriter, encoderContext, document.get("_id"));
      }

   }

   private boolean skipField(EncoderContext encoderContext, String key) {
      return encoderContext.isEncodingCollectibleDocument() && key.equals("_id");
   }

   private void writeValue(BsonWriter writer, EncoderContext encoderContext, Object value) {
      if (value == null) {
         writer.writeNull();
      } else {
         Codec codec = this.registry.get(value.getClass());
         encoderContext.encodeWithChildContext(codec, writer, value);
      }

   }

   static {
      DEFAULT_BSON_TYPE_CODEC_MAP = new BsonTypeCodecMap(BsonTypeClassMap.DEFAULT_BSON_TYPE_CLASS_MAP, DEFAULT_REGISTRY);
      DEFAULT_ID_GENERATOR = new ObjectIdGenerator();
   }
}
