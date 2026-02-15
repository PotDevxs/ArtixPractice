package dev.artixdev.libs.org.bson.codecs;

import java.util.HashMap;
import java.util.Map;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDateTime;
import dev.artixdev.libs.org.bson.BsonDbPointer;
import dev.artixdev.libs.org.bson.BsonDecimal128;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWrapper;
import dev.artixdev.libs.org.bson.BsonDouble;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonJavaScript;
import dev.artixdev.libs.org.bson.BsonJavaScriptWithScope;
import dev.artixdev.libs.org.bson.BsonMaxKey;
import dev.artixdev.libs.org.bson.BsonMinKey;
import dev.artixdev.libs.org.bson.BsonNull;
import dev.artixdev.libs.org.bson.BsonObjectId;
import dev.artixdev.libs.org.bson.BsonRegularExpression;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonSymbol;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonUndefined;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class BsonValueCodecProvider implements CodecProvider {
   private static final BsonTypeClassMap DEFAULT_BSON_TYPE_CLASS_MAP;
   private final Map<Class<?>, Codec<?>> codecs = new HashMap();

   public BsonValueCodecProvider() {
      this.addCodecs();
   }

   public static Class<? extends BsonValue> getClassForBsonType(BsonType bsonType) {
      @SuppressWarnings("unchecked")
      Class<? extends BsonValue> clazz = (Class<? extends BsonValue>) DEFAULT_BSON_TYPE_CLASS_MAP.get(bsonType);
      return clazz;
   }

   public static BsonTypeClassMap getBsonTypeClassMap() {
      return DEFAULT_BSON_TYPE_CLASS_MAP;
   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      if (this.codecs.containsKey(clazz)) {
         @SuppressWarnings("unchecked")
         Codec<T> codec = (Codec<T>) this.codecs.get(clazz);
         return codec;
      } else if (clazz == BsonJavaScriptWithScope.class) {
         @SuppressWarnings("unchecked")
         Codec<T> codec = (Codec<T>) new BsonJavaScriptWithScopeCodec(registry.get(BsonDocument.class));
         return codec;
      } else if (clazz == BsonValue.class) {
         @SuppressWarnings("unchecked")
         Codec<T> codec = (Codec<T>) new BsonValueCodec(registry);
         return codec;
      } else if (clazz == BsonDocumentWrapper.class) {
         @SuppressWarnings("unchecked")
         Codec<T> codec = (Codec<T>) new BsonDocumentWrapperCodec(registry.get(BsonDocument.class));
         return codec;
      } else if (clazz == RawBsonDocument.class) {
         @SuppressWarnings("unchecked")
         Codec<T> codec = (Codec<T>) new RawBsonDocumentCodec();
         return codec;
      } else if (BsonDocument.class.isAssignableFrom(clazz)) {
         @SuppressWarnings("unchecked")
         Codec<T> codec = (Codec<T>) new BsonDocumentCodec(registry);
         return codec;
      } else {
         if (BsonArray.class.isAssignableFrom(clazz)) {
            @SuppressWarnings("unchecked")
            Codec<T> codec = (Codec<T>) new BsonArrayCodec(registry);
            return codec;
         }
         return null;
      }
   }

   private void addCodecs() {
      this.addCodec(new BsonNullCodec());
      this.addCodec(new BsonBinaryCodec());
      this.addCodec(new BsonBooleanCodec());
      this.addCodec(new BsonDateTimeCodec());
      this.addCodec(new BsonDBPointerCodec());
      this.addCodec(new BsonDoubleCodec());
      this.addCodec(new BsonInt32Codec());
      this.addCodec(new BsonInt64Codec());
      this.addCodec(new BsonDecimal128Codec());
      this.addCodec(new BsonMinKeyCodec());
      this.addCodec(new BsonMaxKeyCodec());
      this.addCodec(new BsonJavaScriptCodec());
      this.addCodec(new BsonObjectIdCodec());
      this.addCodec(new BsonRegularExpressionCodec());
      this.addCodec(new BsonStringCodec());
      this.addCodec(new BsonSymbolCodec());
      this.addCodec(new BsonTimestampCodec());
      this.addCodec(new BsonUndefinedCodec());
   }

   private <T extends BsonValue> void addCodec(Codec<T> codec) {
      this.codecs.put(codec.getEncoderClass(), codec);
   }

   public String toString() {
      return "BsonValueCodecProvider{}";
   }

   static {
      Map<BsonType, Class<?>> map = new HashMap();
      map.put(BsonType.NULL, BsonNull.class);
      map.put(BsonType.ARRAY, BsonArray.class);
      map.put(BsonType.BINARY, BsonBinary.class);
      map.put(BsonType.BOOLEAN, BsonBoolean.class);
      map.put(BsonType.DATE_TIME, BsonDateTime.class);
      map.put(BsonType.DB_POINTER, BsonDbPointer.class);
      map.put(BsonType.DOCUMENT, BsonDocument.class);
      map.put(BsonType.DOUBLE, BsonDouble.class);
      map.put(BsonType.INT32, BsonInt32.class);
      map.put(BsonType.INT64, BsonInt64.class);
      map.put(BsonType.DECIMAL128, BsonDecimal128.class);
      map.put(BsonType.MAX_KEY, BsonMaxKey.class);
      map.put(BsonType.MIN_KEY, BsonMinKey.class);
      map.put(BsonType.JAVASCRIPT, BsonJavaScript.class);
      map.put(BsonType.JAVASCRIPT_WITH_SCOPE, BsonJavaScriptWithScope.class);
      map.put(BsonType.OBJECT_ID, BsonObjectId.class);
      map.put(BsonType.REGULAR_EXPRESSION, BsonRegularExpression.class);
      map.put(BsonType.STRING, BsonString.class);
      map.put(BsonType.SYMBOL, BsonSymbol.class);
      map.put(BsonType.TIMESTAMP, BsonTimestamp.class);
      map.put(BsonType.UNDEFINED, BsonUndefined.class);
      DEFAULT_BSON_TYPE_CLASS_MAP = new BsonTypeClassMap(map);
   }
}
