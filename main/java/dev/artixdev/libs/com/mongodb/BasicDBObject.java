package dev.artixdev.libs.com.mongodb;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import dev.artixdev.libs.org.bson.BSONObject;
import dev.artixdev.libs.org.bson.BasicBSONObject;
import dev.artixdev.libs.org.bson.BsonBinaryWriter;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWrapper;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.Encoder;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;
import dev.artixdev.libs.org.bson.io.BasicOutputBuffer;
import dev.artixdev.libs.org.bson.io.OutputBuffer;
import dev.artixdev.libs.org.bson.json.JsonMode;
import dev.artixdev.libs.org.bson.json.JsonReader;
import dev.artixdev.libs.org.bson.json.JsonWriter;
import dev.artixdev.libs.org.bson.json.JsonWriterSettings;
import dev.artixdev.libs.org.bson.types.BasicBSONList;

public class BasicDBObject extends BasicBSONObject implements DBObject, Bson {
   private static final long serialVersionUID = -4415279469780082174L;
   private static final Codec<BasicDBObject> DEFAULT_CODEC;
   private boolean isPartialObject;

   public static BasicDBObject parse(String json) {
      return parse(json, DEFAULT_CODEC);
   }

   public static BasicDBObject parse(String json, Decoder<BasicDBObject> decoder) {
      return (BasicDBObject)decoder.decode(new JsonReader(json), DecoderContext.builder().build());
   }

   public BasicDBObject() {
   }

   public BasicDBObject(int size) {
      super(size);
   }

   public BasicDBObject(String key, Object value) {
      super(key, value);
   }

   public BasicDBObject(Map map) {
      super(map);
   }

   public BasicDBObject append(String key, Object val) {
      this.put(key, val);
      return this;
   }

   public boolean isPartialObject() {
      return this.isPartialObject;
   }

   public String toJson() {
      return this.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build());
   }

   public String toJson(JsonWriterSettings writerSettings) {
      return this.toJson(writerSettings, DEFAULT_CODEC);
   }

   public String toJson(Encoder<BasicDBObject> encoder) {
      return this.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build(), encoder);
   }

   public String toJson(JsonWriterSettings writerSettings, Encoder<BasicDBObject> encoder) {
      JsonWriter writer = new JsonWriter(new StringWriter(), writerSettings);
      encoder.encode(writer, this, EncoderContext.builder().build());
      return writer.getWriter().toString();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof BSONObject)) {
         return false;
      } else {
         BSONObject other = (BSONObject)o;
         return !this.keySet().equals(other.keySet()) ? false : Arrays.equals(toBson(canonicalizeBSONObject(this)), toBson(canonicalizeBSONObject(other)));
      }
   }

   public int hashCode() {
      return Arrays.hashCode(toBson(canonicalizeBSONObject(this)));
   }

   private static byte[] toBson(BasicDBObject dbObject) {
      OutputBuffer outputBuffer = new BasicOutputBuffer();
      DEFAULT_CODEC.encode(new BsonBinaryWriter(outputBuffer), dbObject, EncoderContext.builder().build());
      return outputBuffer.toByteArray();
   }

   public String toString() {
      return this.toJson();
   }

   public void markAsPartialObject() {
      this.isPartialObject = true;
   }

   public Object copy() {
      BasicDBObject newCopy = new BasicDBObject(this.toMap());
      Iterator var2 = this.keySet().iterator();

      while(var2.hasNext()) {
         String field = (String)var2.next();
         Object val = this.get(field);
         if (val instanceof BasicDBObject) {
            newCopy.put(field, ((BasicDBObject)val).copy());
         } else if (val instanceof BasicDBList) {
            newCopy.put(field, ((BasicDBList)val).copy());
         }
      }

      return newCopy;
   }

   public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
      return new BsonDocumentWrapper(this, codecRegistry.get(BasicDBObject.class));
   }

   private static Object canonicalize(Object from) {
      if (from instanceof BSONObject && !(from instanceof BasicBSONList)) {
         return canonicalizeBSONObject((BSONObject)from);
      } else if (from instanceof List) {
         return canonicalizeList((List)from);
      } else {
         return from instanceof Map ? canonicalizeMap((Map)from) : from;
      }
   }

   private static Map<String, Object> canonicalizeMap(Map<String, Object> from) {
      Map<String, Object> canonicalized = new LinkedHashMap(from.size());
      TreeSet<String> keysInOrder = new TreeSet(from.keySet());
      Iterator var3 = keysInOrder.iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         Object val = from.get(key);
         canonicalized.put(key, canonicalize(val));
      }

      return canonicalized;
   }

   private static BasicDBObject canonicalizeBSONObject(BSONObject from) {
      BasicDBObject canonicalized = new BasicDBObject();
      TreeSet<String> keysInOrder = new TreeSet(from.keySet());
      Iterator var3 = keysInOrder.iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         Object val = from.get(key);
         canonicalized.put(key, canonicalize(val));
      }

      return canonicalized;
   }

   private static List canonicalizeList(List<Object> list) {
      List<Object> canonicalized = new ArrayList(list.size());
      Iterator var2 = list.iterator();

      while(var2.hasNext()) {
         Object cur = var2.next();
         canonicalized.add(canonicalize(cur));
      }

      return canonicalized;
   }

   static {
      DEFAULT_CODEC = CodecRegistries.withUuidRepresentation(DBObjectCodec.getDefaultRegistry(), UuidRepresentation.STANDARD).get(BasicDBObject.class);
   }
}
