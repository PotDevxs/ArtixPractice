package dev.artixdev.libs.org.bson;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.CollectionCodecProvider;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.DocumentCodecProvider;
import dev.artixdev.libs.org.bson.codecs.Encoder;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.IterableCodecProvider;
import dev.artixdev.libs.org.bson.codecs.MapCodecProvider;
import dev.artixdev.libs.org.bson.codecs.ValueCodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;
import dev.artixdev.libs.org.bson.json.JsonMode;
import dev.artixdev.libs.org.bson.json.JsonReader;
import dev.artixdev.libs.org.bson.json.JsonWriter;
import dev.artixdev.libs.org.bson.json.JsonWriterSettings;
import dev.artixdev.libs.org.bson.types.ObjectId;

public class Document implements Serializable, Map<String, Object>, Bson {
   private static final Codec<Document> DEFAULT_CODEC;
   private static final long serialVersionUID = 6297731997167536582L;
   private final LinkedHashMap<String, Object> documentAsMap;

   public Document() {
      this.documentAsMap = new LinkedHashMap();
   }

   public Document(String key, Object value) {
      this.documentAsMap = new LinkedHashMap();
      this.documentAsMap.put(key, value);
   }

   public Document(Map<String, ?> map) {
      this.documentAsMap = new LinkedHashMap(map);
   }

   public static Document parse(String json) {
      return parse(json, DEFAULT_CODEC);
   }

   public static Document parse(String json, Decoder<Document> decoder) {
      Assertions.notNull("codec", decoder);
      JsonReader bsonReader = new JsonReader(json);
      return (Document)decoder.decode(bsonReader, DecoderContext.builder().build());
   }

   public <C> BsonDocument toBsonDocument(Class<C> documentClass, CodecRegistry codecRegistry) {
      return new BsonDocumentWrapper(this, codecRegistry.get(Document.class));
   }

   public Document append(String key, Object value) {
      this.documentAsMap.put(key, value);
      return this;
   }

   public <T> T get(Object key, Class<T> clazz) {
      Assertions.notNull("clazz", clazz);
      return clazz.cast(this.documentAsMap.get(key));
   }

   @SuppressWarnings("unchecked")
   public <T> T get(Object key, T defaultValue) {
      Assertions.notNull("defaultValue", defaultValue);
      Object value = this.documentAsMap.get(key);
      return value == null ? defaultValue : (T) value;
   }

   public <T> T getEmbedded(List<?> keys, Class<T> clazz) {
      Assertions.notNull("keys", keys);
      Assertions.isTrue("keys", !keys.isEmpty());
      Assertions.notNull("clazz", clazz);
      return this.getEmbeddedValue(keys, clazz, null);
   }

   @SuppressWarnings("unchecked")
   public <T> T getEmbedded(List<?> keys, T defaultValue) {
      Assertions.notNull("keys", keys);
      Assertions.isTrue("keys", !keys.isEmpty());
      Assertions.notNull("defaultValue", defaultValue);
      Class<T> clazz = (Class<T>) null;
      return this.getEmbeddedValue(keys, clazz, defaultValue);
   }

   private <T> T getEmbeddedValue(List<?> keys, Class<T> clazz, T defaultValue) {
      Object value = this;
      Iterator keyIterator = keys.iterator();

      while (keyIterator.hasNext()) {
         Object key = keyIterator.next();
         value = ((Document) value).get(key);
         if (!(value instanceof Document)) {
            if (value == null) {
               return defaultValue;
            }

            if (keyIterator.hasNext()) {
               throw new ClassCastException(String.format("At key %s, the value is not a Document (%s)", key, value.getClass().getName()));
            }
         }
      }

      if (clazz != null) {
         return clazz.cast(value);
      } else {
         @SuppressWarnings("unchecked")
         T result = (T) value;
         return result;
      }
   }

   public Integer getInteger(Object key) {
      return (Integer)this.get(key);
   }

   public int getInteger(Object key, int defaultValue) {
      return (Integer)this.get(key, (Object)defaultValue);
   }

   public Long getLong(Object key) {
      return (Long)this.get(key);
   }

   public Double getDouble(Object key) {
      return (Double)this.get(key);
   }

   public String getString(Object key) {
      return (String)this.get(key);
   }

   public Boolean getBoolean(Object key) {
      return (Boolean)this.get(key);
   }

   public boolean getBoolean(Object key, boolean defaultValue) {
      return (Boolean)this.get(key, (Object)defaultValue);
   }

   public ObjectId getObjectId(Object key) {
      return (ObjectId)this.get(key);
   }

   public Date getDate(Object key) {
      return (Date)this.get(key);
   }

   public <T> List<T> getList(Object key, Class<T> clazz) {
      Assertions.notNull("clazz", clazz);
      return this.constructValuesList(key, clazz, (List)null);
   }

   public <T> List<T> getList(Object key, Class<T> clazz, List<T> defaultValue) {
      Assertions.notNull("defaultValue", defaultValue);
      Assertions.notNull("clazz", clazz);
      return this.constructValuesList(key, clazz, defaultValue);
   }

   private <T> List<T> constructValuesList(Object key, Class<T> clazz, List<T> defaultValue) {
      List<T> value = (List)this.get(key, List.class);
      if (value == null) {
         return defaultValue;
      } else {
         Iterator<T> iterator = value.iterator();

         Object item;
         do {
            if (!iterator.hasNext()) {
               return value;
            }

            item = iterator.next();
         } while(item == null || clazz.isAssignableFrom(item.getClass()));

         throw new ClassCastException(String.format("List element cannot be cast to %s", clazz.getName()));
      }
   }

   public String toJson() {
      return this.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build());
   }

   public String toJson(JsonWriterSettings writerSettings) {
      return this.toJson(writerSettings, DEFAULT_CODEC);
   }

   public String toJson(Encoder<Document> encoder) {
      return this.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build(), encoder);
   }

   public String toJson(JsonWriterSettings writerSettings, Encoder<Document> encoder) {
      JsonWriter writer = new JsonWriter(new StringWriter(), writerSettings);
      encoder.encode(writer, this, EncoderContext.builder().build());
      return writer.getWriter().toString();
   }

   public int size() {
      return this.documentAsMap.size();
   }

   public boolean isEmpty() {
      return this.documentAsMap.isEmpty();
   }

   public boolean containsValue(Object value) {
      return this.documentAsMap.containsValue(value);
   }

   public boolean containsKey(Object key) {
      return this.documentAsMap.containsKey(key);
   }

   public Object get(Object key) {
      return this.documentAsMap.get(key);
   }

   public Object put(String key, Object value) {
      return this.documentAsMap.put(key, value);
   }

   public Object remove(Object key) {
      return this.documentAsMap.remove(key);
   }

   public void putAll(Map<? extends String, ?> map) {
      this.documentAsMap.putAll(map);
   }

   public void clear() {
      this.documentAsMap.clear();
   }

   public Set<String> keySet() {
      return this.documentAsMap.keySet();
   }

   public Collection<Object> values() {
      return this.documentAsMap.values();
   }

   public Set<Entry<String, Object>> entrySet() {
      return this.documentAsMap.entrySet();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Document document = (Document)o;
         return this.documentAsMap.equals(document.documentAsMap);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.documentAsMap.hashCode();
   }

   public String toString() {
      return "Document{" + this.documentAsMap + '}';
   }

   static {
      DEFAULT_CODEC = CodecRegistries.withUuidRepresentation(CodecRegistries.fromProviders(Arrays.asList(new ValueCodecProvider(), new CollectionCodecProvider(), new IterableCodecProvider(), new BsonValueCodecProvider(), new DocumentCodecProvider(), new MapCodecProvider())), UuidRepresentation.STANDARD).get(Document.class);
   }
}
