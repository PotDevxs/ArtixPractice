package dev.artixdev.libs.com.mongodb;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BSONObject;
import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonBinarySubType;
import dev.artixdev.libs.org.bson.BsonDbPointer;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWriter;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.codecs.BsonTypeClassMap;
import dev.artixdev.libs.org.bson.codecs.BsonTypeCodecMap;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.CollectibleCodec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.IdGenerator;
import dev.artixdev.libs.org.bson.codecs.ObjectIdGenerator;
import dev.artixdev.libs.org.bson.codecs.OverridableUuidRepresentationCodec;
import dev.artixdev.libs.org.bson.codecs.ValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.types.BSONTimestamp;
import dev.artixdev.libs.org.bson.types.Binary;
import dev.artixdev.libs.org.bson.types.CodeWScope;
import dev.artixdev.libs.org.bson.types.Symbol;

public class DBObjectCodec implements CollectibleCodec<DBObject>, OverridableUuidRepresentationCodec<DBObject> {
   private static final BsonTypeClassMap DEFAULT_BSON_TYPE_CLASS_MAP = createDefaultBsonTypeClassMap();
   private static final CodecRegistry DEFAULT_REGISTRY = CodecRegistries.fromProviders(Arrays.asList(new ValueCodecProvider(), new BsonValueCodecProvider(), new DBObjectCodecProvider()));
   private static final String ID_FIELD_NAME = "_id";
   private final CodecRegistry codecRegistry;
   private final BsonTypeCodecMap bsonTypeCodecMap;
   private final DBObjectFactory objectFactory;
   private final IdGenerator idGenerator;
   private final UuidRepresentation uuidRepresentation;

   private static BsonTypeClassMap createDefaultBsonTypeClassMap() {
      Map<BsonType, Class<?>> replacements = new HashMap();
      replacements.put(BsonType.REGULAR_EXPRESSION, Pattern.class);
      replacements.put(BsonType.SYMBOL, String.class);
      replacements.put(BsonType.TIMESTAMP, BSONTimestamp.class);
      replacements.put(BsonType.JAVASCRIPT_WITH_SCOPE, (Class<?>)null);
      replacements.put(BsonType.DOCUMENT, (Class<?>)null);
      return new BsonTypeClassMap(replacements);
   }

   static BsonTypeClassMap getDefaultBsonTypeClassMap() {
      return DEFAULT_BSON_TYPE_CLASS_MAP;
   }

   static CodecRegistry getDefaultRegistry() {
      return DEFAULT_REGISTRY;
   }

   public DBObjectCodec() {
      this(DEFAULT_REGISTRY);
   }

   public DBObjectCodec(CodecRegistry codecRegistry) {
      this(codecRegistry, DEFAULT_BSON_TYPE_CLASS_MAP);
   }

   public DBObjectCodec(CodecRegistry codecRegistry, BsonTypeClassMap bsonTypeClassMap) {
      this(codecRegistry, bsonTypeClassMap, new BasicDBObjectFactory());
   }

   public DBObjectCodec(CodecRegistry codecRegistry, BsonTypeClassMap bsonTypeClassMap, DBObjectFactory objectFactory) {
      this(codecRegistry, new BsonTypeCodecMap((BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap), codecRegistry), objectFactory, UuidRepresentation.UNSPECIFIED);
   }

   private DBObjectCodec(CodecRegistry codecRegistry, BsonTypeCodecMap bsonTypeCodecMap, DBObjectFactory objectFactory, UuidRepresentation uuidRepresentation) {
      this.idGenerator = new ObjectIdGenerator();
      this.objectFactory = (DBObjectFactory)Assertions.notNull("objectFactory", objectFactory);
      this.codecRegistry = (CodecRegistry)Assertions.notNull("codecRegistry", codecRegistry);
      this.uuidRepresentation = (UuidRepresentation)Assertions.notNull("uuidRepresentation", uuidRepresentation);
      this.bsonTypeCodecMap = bsonTypeCodecMap;
   }

   public void encode(BsonWriter writer, DBObject document, EncoderContext encoderContext) {
      writer.writeStartDocument();
      this.beforeFields(writer, encoderContext, document);
      Iterator var4 = document.keySet().iterator();

      while(var4.hasNext()) {
         String key = (String)var4.next();
         if (!this.skipField(encoderContext, key)) {
            writer.writeName(key);
            this.writeValue(writer, encoderContext, document.get(key));
         }
      }

      writer.writeEndDocument();
   }

   public DBObject decode(BsonReader reader, DecoderContext decoderContext) {
      List<String> path = new ArrayList(10);
      return this.readDocument(reader, decoderContext, path);
   }

   public Class<DBObject> getEncoderClass() {
      return DBObject.class;
   }

   public boolean documentHasId(DBObject document) {
      return document.containsField("_id");
   }

   public BsonValue getDocumentId(DBObject document) {
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

   public DBObject generateIdIfAbsentFromDocument(DBObject document) {
      if (!this.documentHasId(document)) {
         document.put("_id", this.idGenerator.generate());
      }

      return document;
   }

   public Codec<DBObject> withUuidRepresentation(UuidRepresentation uuidRepresentation) {
      return this.uuidRepresentation.equals(uuidRepresentation) ? this : new DBObjectCodec(this.codecRegistry, this.bsonTypeCodecMap, this.objectFactory, uuidRepresentation);
   }

   private void beforeFields(BsonWriter bsonWriter, EncoderContext encoderContext, DBObject document) {
      if (encoderContext.isEncodingCollectibleDocument() && document.containsField("_id")) {
         bsonWriter.writeName("_id");
         this.writeValue(bsonWriter, encoderContext, document.get("_id"));
      }

   }

   private boolean skipField(EncoderContext encoderContext, String key) {
      return encoderContext.isEncodingCollectibleDocument() && key.equals("_id");
   }

   private void writeValue(BsonWriter bsonWriter, EncoderContext encoderContext, @Nullable Object value) {
      if (value == null) {
         bsonWriter.writeNull();
      } else if (value instanceof DBRef) {
         this.encodeDBRef(bsonWriter, (DBRef)value, encoderContext);
      } else if (value instanceof Map) {
         this.encodeMap(bsonWriter, (Map)value, encoderContext);
      } else if (value instanceof Iterable) {
         this.encodeIterable(bsonWriter, (Iterable)value, encoderContext);
      } else if (value instanceof BSONObject) {
         this.encodeBsonObject(bsonWriter, (BSONObject)value, encoderContext);
      } else if (value instanceof CodeWScope) {
         this.encodeCodeWScope(bsonWriter, (CodeWScope)value, encoderContext);
      } else if (value instanceof byte[]) {
         this.encodeByteArray(bsonWriter, (byte[])value);
      } else if (value.getClass().isArray()) {
         this.encodeArray(bsonWriter, value, encoderContext);
      } else if (value instanceof Symbol) {
         bsonWriter.writeSymbol(((Symbol)value).getSymbol());
      } else {
         Codec codec = this.codecRegistry.get(value.getClass());
         encoderContext.encodeWithChildContext(codec, bsonWriter, value);
      }

   }

   private void encodeMap(BsonWriter bsonWriter, Map<String, Object> document, EncoderContext encoderContext) {
      bsonWriter.writeStartDocument();
      Iterator var4 = document.entrySet().iterator();

      while(var4.hasNext()) {
         Entry<String, Object> entry = (Entry)var4.next();
         bsonWriter.writeName((String)entry.getKey());
         this.writeValue(bsonWriter, encoderContext.getChildContext(), entry.getValue());
      }

      bsonWriter.writeEndDocument();
   }

   private void encodeBsonObject(BsonWriter bsonWriter, BSONObject document, EncoderContext encoderContext) {
      bsonWriter.writeStartDocument();
      Iterator var4 = document.keySet().iterator();

      while(var4.hasNext()) {
         String key = (String)var4.next();
         bsonWriter.writeName(key);
         this.writeValue(bsonWriter, encoderContext.getChildContext(), document.get(key));
      }

      bsonWriter.writeEndDocument();
   }

   private void encodeByteArray(BsonWriter bsonWriter, byte[] value) {
      bsonWriter.writeBinaryData(new BsonBinary(value));
   }

   private void encodeArray(BsonWriter bsonWriter, Object value, EncoderContext encoderContext) {
      bsonWriter.writeStartArray();
      int size = Array.getLength(value);

      for(int i = 0; i < size; ++i) {
         this.writeValue(bsonWriter, encoderContext.getChildContext(), Array.get(value, i));
      }

      bsonWriter.writeEndArray();
   }

   private void encodeDBRef(BsonWriter bsonWriter, DBRef dbRef, EncoderContext encoderContext) {
      bsonWriter.writeStartDocument();
      bsonWriter.writeString("$ref", dbRef.getCollectionName());
      bsonWriter.writeName("$id");
      this.writeValue(bsonWriter, encoderContext.getChildContext(), dbRef.getId());
      if (dbRef.getDatabaseName() != null) {
         bsonWriter.writeString("$db", dbRef.getDatabaseName());
      }

      bsonWriter.writeEndDocument();
   }

   private void encodeCodeWScope(BsonWriter bsonWriter, CodeWScope value, EncoderContext encoderContext) {
      bsonWriter.writeJavaScriptWithScope(value.getCode());
      this.encodeBsonObject(bsonWriter, value.getScope(), encoderContext.getChildContext());
   }

   private void encodeIterable(BsonWriter bsonWriter, Iterable iterable, EncoderContext encoderContext) {
      bsonWriter.writeStartArray();
      Iterator var4 = iterable.iterator();

      while(var4.hasNext()) {
         Object cur = var4.next();
         this.writeValue(bsonWriter, encoderContext.getChildContext(), cur);
      }

      bsonWriter.writeEndArray();
   }

   @Nullable
   private Object readValue(BsonReader reader, DecoderContext decoderContext, @Nullable String fieldName, List<String> path) {
      BsonType bsonType = reader.getCurrentBsonType();
      if (bsonType.isContainer() && fieldName != null) {
         path.add(fieldName);
      }

      Object initialRetVal;
      switch(bsonType) {
      case DOCUMENT:
         initialRetVal = this.verifyForDBRef(this.readDocument(reader, decoderContext, path));
         break;
      case ARRAY:
         initialRetVal = this.readArray(reader, decoderContext, path);
         break;
      case JAVASCRIPT_WITH_SCOPE:
         initialRetVal = this.readCodeWScope(reader, decoderContext, path);
         break;
      case DB_POINTER:
         BsonDbPointer dbPointer = reader.readDBPointer();
         initialRetVal = new DBRef(dbPointer.getNamespace(), dbPointer.getId());
         break;
      case BINARY:
         initialRetVal = this.readBinary(reader, decoderContext);
         break;
      case NULL:
         reader.readNull();
         initialRetVal = null;
         break;
      default:
         initialRetVal = this.bsonTypeCodecMap.get(bsonType).decode(reader, decoderContext);
      }

      if (bsonType.isContainer() && fieldName != null) {
         path.remove(fieldName);
      }

      return initialRetVal;
   }

   private Object readBinary(BsonReader reader, DecoderContext decoderContext) {
      byte bsonBinarySubType = reader.peekBinarySubType();
      Codec codec;
      if (BsonBinarySubType.isUuid(bsonBinarySubType) && reader.peekBinarySize() == 16) {
         codec = this.codecRegistry.get(Binary.class);
         switch(bsonBinarySubType) {
         case 3:
            if (this.uuidRepresentation == UuidRepresentation.JAVA_LEGACY || this.uuidRepresentation == UuidRepresentation.C_SHARP_LEGACY || this.uuidRepresentation == UuidRepresentation.PYTHON_LEGACY) {
               codec = this.codecRegistry.get(UUID.class);
            }
            break;
         case 4:
            if (this.uuidRepresentation == UuidRepresentation.STANDARD) {
               codec = this.codecRegistry.get(UUID.class);
            }
            break;
         default:
            throw new UnsupportedOperationException("Unknown UUID binary subtype " + bsonBinarySubType);
         }
      } else if (bsonBinarySubType != BsonBinarySubType.BINARY.getValue() && bsonBinarySubType != BsonBinarySubType.OLD_BINARY.getValue()) {
         codec = this.codecRegistry.get(Binary.class);
      } else {
         codec = this.codecRegistry.get(byte[].class);
      }

      return codec.decode(reader, decoderContext);
   }

   private List readArray(BsonReader reader, DecoderContext decoderContext, List<String> path) {
      reader.readStartArray();
      BasicDBList list = new BasicDBList();

      while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
         list.add(this.readValue(reader, decoderContext, (String)null, path));
      }

      reader.readEndArray();
      return list;
   }

   private DBObject readDocument(BsonReader reader, DecoderContext decoderContext, List<String> path) {
      DBObject document = this.objectFactory.getInstance(path);
      reader.readStartDocument();

      while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
         String fieldName = reader.readName();
         document.put(fieldName, this.readValue(reader, decoderContext, fieldName, path));
      }

      reader.readEndDocument();
      return document;
   }

   private CodeWScope readCodeWScope(BsonReader reader, DecoderContext decoderContext, List<String> path) {
      return new CodeWScope(reader.readJavaScriptWithScope(), this.readDocument(reader, decoderContext, path));
   }

   private Object verifyForDBRef(DBObject document) {
      return document.containsField("$ref") && document.containsField("$id") ? new DBRef((String)document.get("$db"), (String)document.get("$ref"), document.get("$id")) : document;
   }
}
