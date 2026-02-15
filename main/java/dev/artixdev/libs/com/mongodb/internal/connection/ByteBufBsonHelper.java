package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.org.bson.BsonBinaryReader;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDateTime;
import dev.artixdev.libs.org.bson.BsonDecimal128;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDouble;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonJavaScript;
import dev.artixdev.libs.org.bson.BsonJavaScriptWithScope;
import dev.artixdev.libs.org.bson.BsonMaxKey;
import dev.artixdev.libs.org.bson.BsonMinKey;
import dev.artixdev.libs.org.bson.BsonNull;
import dev.artixdev.libs.org.bson.BsonObjectId;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonSymbol;
import dev.artixdev.libs.org.bson.BsonUndefined;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;

final class ByteBufBsonHelper {
   static BsonValue readBsonValue(ByteBuf byteBuf, BsonBinaryReader bsonReader) {
      Object value;
      switch(bsonReader.getCurrentBsonType()) {
      case DOCUMENT:
         ByteBuf documentByteBuf = byteBuf.duplicate();
         value = new ByteBufBsonDocument(documentByteBuf);
         bsonReader.skipValue();
         break;
      case ARRAY:
         ByteBuf arrayByteBuf = byteBuf.duplicate();
         value = new ByteBufBsonArray(arrayByteBuf);
         bsonReader.skipValue();
         break;
      case INT32:
         value = new BsonInt32(bsonReader.readInt32());
         break;
      case INT64:
         value = new BsonInt64(bsonReader.readInt64());
         break;
      case DOUBLE:
         value = new BsonDouble(bsonReader.readDouble());
         break;
      case DECIMAL128:
         value = new BsonDecimal128(bsonReader.readDecimal128());
         break;
      case DATE_TIME:
         value = new BsonDateTime(bsonReader.readDateTime());
         break;
      case TIMESTAMP:
         value = bsonReader.readTimestamp();
         break;
      case BOOLEAN:
         value = new BsonBoolean(bsonReader.readBoolean());
         break;
      case OBJECT_ID:
         value = new BsonObjectId(bsonReader.readObjectId());
         break;
      case STRING:
         value = new BsonString(bsonReader.readString());
         break;
      case BINARY:
         value = bsonReader.readBinaryData();
         break;
      case SYMBOL:
         value = new BsonSymbol(bsonReader.readSymbol());
         break;
      case UNDEFINED:
         bsonReader.readUndefined();
         value = new BsonUndefined();
         break;
      case REGULAR_EXPRESSION:
         value = bsonReader.readRegularExpression();
         break;
      case DB_POINTER:
         value = bsonReader.readDBPointer();
         break;
      case JAVASCRIPT:
         value = new BsonJavaScript(bsonReader.readJavaScript());
         break;
      case JAVASCRIPT_WITH_SCOPE:
         String code = bsonReader.readJavaScriptWithScope();
         BsonDocument scope = (new BsonDocumentCodec()).decode(bsonReader, DecoderContext.builder().build());
         value = new BsonJavaScriptWithScope(code, scope);
         break;
      case MIN_KEY:
         bsonReader.readMinKey();
         value = new BsonMinKey();
         break;
      case MAX_KEY:
         bsonReader.readMaxKey();
         value = new BsonMaxKey();
         break;
      case NULL:
         bsonReader.readNull();
         value = new BsonNull();
         break;
      default:
         throw new UnsupportedOperationException("Unexpected BSON type: " + bsonReader.getCurrentBsonType());
      }

      return (BsonValue)value;
   }

   private ByteBufBsonHelper() {
   }
}
