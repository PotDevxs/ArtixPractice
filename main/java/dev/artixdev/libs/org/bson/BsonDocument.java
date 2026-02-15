package dev.artixdev.libs.org.bson;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;
import dev.artixdev.libs.org.bson.io.BasicOutputBuffer;
import dev.artixdev.libs.org.bson.json.JsonMode;
import dev.artixdev.libs.org.bson.json.JsonReader;
import dev.artixdev.libs.org.bson.json.JsonWriter;
import dev.artixdev.libs.org.bson.json.JsonWriterSettings;

public class BsonDocument extends BsonValue implements Serializable, Cloneable, Map<String, BsonValue>, Bson {
   private static final long serialVersionUID = 1L;
   private final Map<String, BsonValue> map;

   public static BsonDocument parse(String json) {
      return (new BsonDocumentCodec()).decode(new JsonReader(json), DecoderContext.builder().build());
   }

   public BsonDocument(List<BsonElement> bsonElements) {
      this(bsonElements.size());
      Iterator<BsonElement> iterator = bsonElements.iterator();

      while(iterator.hasNext()) {
         BsonElement cur = iterator.next();
         this.put(cur.getName(), cur.getValue());
      }

   }

   public BsonDocument(String key, BsonValue value) {
      this();
      this.put(key, value);
   }

   public BsonDocument(int initialCapacity) {
      this.map = new LinkedHashMap(initialCapacity);
   }

   public BsonDocument() {
      this.map = new LinkedHashMap();
   }

   public <C> BsonDocument toBsonDocument(Class<C> documentClass, CodecRegistry codecRegistry) {
      return this;
   }

   public BsonType getBsonType() {
      return BsonType.DOCUMENT;
   }

   public int size() {
      return this.map.size();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   public boolean containsKey(Object key) {
      return this.map.containsKey(key);
   }

   public boolean containsValue(Object value) {
      return this.map.containsValue(value);
   }

   public BsonValue get(Object key) {
      return (BsonValue)this.map.get(key);
   }

   public BsonDocument getDocument(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asDocument();
   }

   public BsonArray getArray(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asArray();
   }

   public BsonNumber getNumber(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asNumber();
   }

   public BsonInt32 getInt32(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asInt32();
   }

   public BsonInt64 getInt64(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asInt64();
   }

   public BsonDecimal128 getDecimal128(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asDecimal128();
   }

   public BsonDouble getDouble(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asDouble();
   }

   public BsonBoolean getBoolean(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asBoolean();
   }

   public BsonString getString(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asString();
   }

   public BsonDateTime getDateTime(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asDateTime();
   }

   public BsonTimestamp getTimestamp(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asTimestamp();
   }

   public BsonObjectId getObjectId(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asObjectId();
   }

   public BsonRegularExpression getRegularExpression(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asRegularExpression();
   }

   public BsonBinary getBinary(Object key) {
      this.throwIfKeyAbsent(key);
      return this.get(key).asBinary();
   }

   public boolean isNull(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isNull();
   }

   public boolean isDocument(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isDocument();
   }

   public boolean isArray(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isArray();
   }

   public boolean isNumber(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isNumber();
   }

   public boolean isInt32(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isInt32();
   }

   public boolean isInt64(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isInt64();
   }

   public boolean isDecimal128(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isDecimal128();
   }

   public boolean isDouble(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isDouble();
   }

   public boolean isBoolean(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isBoolean();
   }

   public boolean isString(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isString();
   }

   public boolean isDateTime(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isDateTime();
   }

   public boolean isTimestamp(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isTimestamp();
   }

   public boolean isObjectId(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isObjectId();
   }

   public boolean isBinary(Object key) {
      return !this.containsKey(key) ? false : this.get(key).isBinary();
   }

   public BsonValue get(Object key, BsonValue defaultValue) {
      BsonValue value = this.get(key);
      return value != null ? value : defaultValue;
   }

   public BsonDocument getDocument(Object key, BsonDocument defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asDocument();
   }

   public BsonArray getArray(Object key, BsonArray defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asArray();
   }

   public BsonNumber getNumber(Object key, BsonNumber defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asNumber();
   }

   public BsonInt32 getInt32(Object key, BsonInt32 defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asInt32();
   }

   public BsonInt64 getInt64(Object key, BsonInt64 defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asInt64();
   }

   public BsonDecimal128 getDecimal128(Object key, BsonDecimal128 defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asDecimal128();
   }

   public BsonDouble getDouble(Object key, BsonDouble defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asDouble();
   }

   public BsonBoolean getBoolean(Object key, BsonBoolean defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asBoolean();
   }

   public BsonString getString(Object key, BsonString defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asString();
   }

   public BsonDateTime getDateTime(Object key, BsonDateTime defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asDateTime();
   }

   public BsonTimestamp getTimestamp(Object key, BsonTimestamp defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asTimestamp();
   }

   public BsonObjectId getObjectId(Object key, BsonObjectId defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asObjectId();
   }

   public BsonBinary getBinary(Object key, BsonBinary defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asBinary();
   }

   public BsonRegularExpression getRegularExpression(Object key, BsonRegularExpression defaultValue) {
      return !this.containsKey(key) ? defaultValue : this.get(key).asRegularExpression();
   }

   public BsonValue put(String key, BsonValue value) {
      if (value == null) {
         throw new IllegalArgumentException(String.format("The value for key %s can not be null", key));
      } else {
         return (BsonValue)this.map.put(key, value);
      }
   }

   public BsonValue remove(Object key) {
      return (BsonValue)this.map.remove(key);
   }

   public void putAll(Map<? extends String, ? extends BsonValue> m) {
      for (Entry<? extends String, ? extends BsonValue> cur : m.entrySet()) {
         this.put((String) cur.getKey(), (BsonValue) cur.getValue());
      }
   }

   public void clear() {
      this.map.clear();
   }

   public Set<String> keySet() {
      return this.map.keySet();
   }

   public Collection<BsonValue> values() {
      return this.map.values();
   }

   public Set<Entry<String, BsonValue>> entrySet() {
      return this.map.entrySet();
   }

   public BsonDocument append(String key, BsonValue value) {
      this.put(key, value);
      return this;
   }

   public String getFirstKey() {
      return (String)this.keySet().iterator().next();
   }

   public BsonReader asBsonReader() {
      return new BsonDocumentReader(this);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof BsonDocument)) {
         return false;
      } else {
         BsonDocument that = (BsonDocument)o;
         return this.entrySet().equals(that.entrySet());
      }
   }

   public int hashCode() {
      return this.entrySet().hashCode();
   }

   public String toJson() {
      return this.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build());
   }

   public String toJson(JsonWriterSettings settings) {
      StringWriter writer = new StringWriter();
      (new BsonDocumentCodec()).encode(new JsonWriter(writer, settings), (BsonDocument)this, EncoderContext.builder().build());
      return writer.toString();
   }

   public String toString() {
      return this.toJson();
   }

   public BsonDocument clone() {
      BsonDocument to = new BsonDocument(this.size());
      Iterator<Entry<String, BsonValue>> iterator = this.entrySet().iterator();

      while(iterator.hasNext()) {
         Entry<String, BsonValue> cur = iterator.next();
         switch(((BsonValue)cur.getValue()).getBsonType()) {
         case DOCUMENT:
            to.put((String)((String)cur.getKey()), (BsonValue)((BsonValue)cur.getValue()).asDocument().clone());
            break;
         case ARRAY:
            to.put((String)((String)cur.getKey()), (BsonValue)((BsonValue)cur.getValue()).asArray().clone());
            break;
         case BINARY:
            to.put((String)((String)cur.getKey()), (BsonValue)BsonBinary.clone(((BsonValue)cur.getValue()).asBinary()));
            break;
         case JAVASCRIPT_WITH_SCOPE:
            to.put((String)((String)cur.getKey()), (BsonValue)BsonJavaScriptWithScope.clone(((BsonValue)cur.getValue()).asJavaScriptWithScope()));
            break;
         default:
            to.put((String)cur.getKey(), (BsonValue)cur.getValue());
         }
      }

      return to;
   }

   private void throwIfKeyAbsent(Object key) {
      if (!this.containsKey(key)) {
         throw new BsonInvalidOperationException("Document does not contain key " + key);
      }
   }

   private Object writeReplace() {
      return new BsonDocument.SerializationProxy(this);
   }

   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
      throw new InvalidObjectException("Proxy required");
   }

   private static class SerializationProxy implements Serializable {
      private static final long serialVersionUID = 1L;
      private final byte[] bytes;

      SerializationProxy(BsonDocument document) {
         BasicOutputBuffer buffer = new BasicOutputBuffer();
         (new BsonDocumentCodec()).encode(new BsonBinaryWriter(buffer), (BsonDocument)document, EncoderContext.builder().build());
         this.bytes = new byte[buffer.size()];
         int curPos = 0;

         ByteBuf cur;
         for(Iterator<ByteBuf> iterator = buffer.getByteBuffers().iterator(); iterator.hasNext(); curPos += cur.position()) {
            cur = iterator.next();
            System.arraycopy(cur.array(), cur.position(), this.bytes, curPos, cur.limit());
         }

      }

      private Object readResolve() {
         return (new BsonDocumentCodec()).decode(new BsonBinaryReader(ByteBuffer.wrap(this.bytes).order(ByteOrder.LITTLE_ENDIAN)), DecoderContext.builder().build());
      }
   }
}
