package dev.artixdev.libs.org.bson.codecs;

import java.math.BigDecimal;
import dev.artixdev.libs.org.bson.BsonInvalidOperationException;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.types.Decimal128;

final class NumberCodecHelper {
   static int decodeInt(BsonReader reader) {
      BsonType bsonType = reader.getCurrentBsonType();
      int intValue;
      switch(bsonType) {
      case INT32:
         intValue = reader.readInt32();
         break;
      case INT64:
         long longValue = reader.readInt64();
         intValue = (int)longValue;
         if (longValue != (long)intValue) {
            throw invalidConversion(Integer.class, longValue);
         }
         break;
      case DOUBLE:
         double doubleValue = reader.readDouble();
         intValue = (int)doubleValue;
         if (doubleValue != (double)intValue) {
            throw invalidConversion(Integer.class, doubleValue);
         }
         break;
      case DECIMAL128:
         Decimal128 decimal128 = reader.readDecimal128();
         intValue = decimal128.intValue();
         if (!decimal128.equals(new Decimal128((long)intValue))) {
            throw invalidConversion(Integer.class, decimal128);
         }
         break;
      default:
         throw new BsonInvalidOperationException(String.format("Invalid numeric type, found: %s", bsonType));
      }

      return intValue;
   }

   static long decodeLong(BsonReader reader) {
      BsonType bsonType = reader.getCurrentBsonType();
      long longValue;
      switch(bsonType) {
      case INT32:
         longValue = (long)reader.readInt32();
         break;
      case INT64:
         longValue = reader.readInt64();
         break;
      case DOUBLE:
         double doubleValue = reader.readDouble();
         longValue = (long)doubleValue;
         if (doubleValue != (double)longValue) {
            throw invalidConversion(Long.class, doubleValue);
         }
         break;
      case DECIMAL128:
         Decimal128 decimal128 = reader.readDecimal128();
         longValue = decimal128.longValue();
         if (!decimal128.equals(new Decimal128(longValue))) {
            throw invalidConversion(Long.class, decimal128);
         }
         break;
      default:
         throw new BsonInvalidOperationException(String.format("Invalid numeric type, found: %s", bsonType));
      }

      return longValue;
   }

   static double decodeDouble(BsonReader reader) {
      BsonType bsonType = reader.getCurrentBsonType();
      double doubleValue;
      switch(bsonType) {
      case INT32:
         doubleValue = (double)reader.readInt32();
         break;
      case INT64:
         long longValue = reader.readInt64();
         doubleValue = (double)longValue;
         if (longValue != (long)doubleValue) {
            throw invalidConversion(Double.class, longValue);
         }
         break;
      case DOUBLE:
         doubleValue = reader.readDouble();
         break;
      case DECIMAL128:
         Decimal128 decimal128 = reader.readDecimal128();

         try {
            doubleValue = decimal128.doubleValue();
            if (!decimal128.equals(new Decimal128(new BigDecimal(doubleValue)))) {
               throw invalidConversion(Double.class, decimal128);
            }
            break;
         } catch (NumberFormatException e) {
            throw invalidConversion(Double.class, decimal128);
         }
      default:
         throw new BsonInvalidOperationException(String.format("Invalid numeric type, found: %s", bsonType));
      }

      return doubleValue;
   }

   private static <T extends Number> BsonInvalidOperationException invalidConversion(Class<T> clazz, Number value) {
      return new BsonInvalidOperationException(String.format("Could not convert `%s` to a %s without losing precision", value, clazz));
   }

   private NumberCodecHelper() {
   }
}
