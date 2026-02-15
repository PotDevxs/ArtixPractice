package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDateTime;
import dev.artixdev.libs.org.bson.BsonDecimal128;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDouble;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonNull;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;
import dev.artixdev.libs.org.bson.types.Decimal128;

@Beta({Beta.Reason.CLIENT})
public final class MqlValues {
   private MqlValues() {
   }

   public static MqlBoolean of(boolean of) {
      return new MqlExpression((codecRegistry) -> {
         return new MqlExpression.AstPlaceholder(new BsonBoolean(of));
      });
   }

   public static MqlArray<MqlBoolean> ofBooleanArray(boolean... array) {
      Assertions.notNull("array", array);
      List<BsonValue> list = new ArrayList();
      boolean[] var2 = array;
      int var3 = array.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         boolean b = var2[var4];
         list.add(new BsonBoolean(b));
      }

      return new MqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonArray(list));
      });
   }

   public static MqlInteger of(int of) {
      return new MqlExpression((codecRegistry) -> {
         return new MqlExpression.AstPlaceholder(new BsonInt32(of));
      });
   }

   public static MqlArray<MqlInteger> ofIntegerArray(int... array) {
      Assertions.notNull("array", array);
      List<BsonValue> list = new ArrayList();
      int[] var2 = array;
      int var3 = array.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int i = var2[var4];
         list.add(new BsonInt32(i));
      }

      return new MqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonArray(list));
      });
   }

   public static MqlInteger of(long of) {
      return new MqlExpression((codecRegistry) -> {
         return new MqlExpression.AstPlaceholder(new BsonInt64(of));
      });
   }

   public static MqlArray<MqlInteger> ofIntegerArray(long... array) {
      Assertions.notNull("array", array);
      List<BsonValue> list = new ArrayList();
      long[] var2 = array;
      int var3 = array.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         long i = var2[var4];
         list.add(new BsonInt64(i));
      }

      return new MqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonArray(list));
      });
   }

   public static MqlNumber of(double of) {
      return new MqlExpression((codecRegistry) -> {
         return new MqlExpression.AstPlaceholder(new BsonDouble(of));
      });
   }

   public static MqlArray<MqlNumber> ofNumberArray(double... array) {
      Assertions.notNull("array", array);
      List<BsonValue> list = new ArrayList();
      double[] var2 = array;
      int var3 = array.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         double n = var2[var4];
         list.add(new BsonDouble(n));
      }

      return new MqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonArray(list));
      });
   }

   public static MqlNumber of(Decimal128 of) {
      Assertions.notNull("Decimal128", of);
      return new MqlExpression((codecRegistry) -> {
         return new MqlExpression.AstPlaceholder(new BsonDecimal128(of));
      });
   }

   public static MqlArray<MqlNumber> ofNumberArray(Decimal128... array) {
      Assertions.notNull("array", array);
      List<BsonValue> result = new ArrayList();
      Decimal128[] var2 = array;
      int var3 = array.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Decimal128 e = var2[var4];
         Assertions.notNull("elements of array", e);
         result.add(new BsonDecimal128(e));
      }

      return new MqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonArray(result));
      });
   }

   public static MqlDate of(Instant of) {
      Assertions.notNull("Instant", of);
      return new MqlExpression((codecRegistry) -> {
         return new MqlExpression.AstPlaceholder(new BsonDateTime(of.toEpochMilli()));
      });
   }

   public static MqlArray<MqlDate> ofDateArray(Instant... array) {
      Assertions.notNull("array", array);
      List<BsonValue> result = new ArrayList();
      Instant[] var2 = array;
      int var3 = array.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Instant e = var2[var4];
         Assertions.notNull("elements of array", e);
         result.add(new BsonDateTime(e.toEpochMilli()));
      }

      return new MqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonArray(result));
      });
   }

   public static MqlString of(String of) {
      Assertions.notNull("String", of);
      return new MqlExpression((codecRegistry) -> {
         return new MqlExpression.AstPlaceholder(wrapString(of));
      });
   }

   public static MqlArray<MqlString> ofStringArray(String... array) {
      Assertions.notNull("array", array);
      List<BsonValue> result = new ArrayList();
      String[] var2 = array;
      int var3 = array.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String e = var2[var4];
         Assertions.notNull("elements of array", e);
         result.add(wrapString(e));
      }

      return new MqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonArray(result));
      });
   }

   private static BsonValue wrapString(String s) {
      BsonString bson = new BsonString(s);
      return (BsonValue)(s.contains("$") ? new BsonDocument("$literal", bson) : bson);
   }

   public static MqlDocument current() {
      return (MqlDocument)(new MqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonString("$$CURRENT"));
      })).assertImplementsAllExpressions();
   }

   public static <R extends MqlValue> MqlMap<R> currentAsMap() {
      return (MqlMap)(new MqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonString("$$CURRENT"));
      })).assertImplementsAllExpressions();
   }

   @SafeVarargs
   public static <T extends MqlValue> MqlArray<T> ofArray(T... array) {
      Assertions.notNull("array", array);
      return new MqlExpression((cr) -> {
         List<BsonValue> list = new ArrayList<>();
         MqlValue[] var3 = array;
         int var4 = array.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            T v = (T) var3[var5];
            Assertions.notNull("elements of array", v);
            list.add(((MqlExpression<?>)v).toBsonValue((CodecRegistry)cr));
         }

         return new MqlExpression.AstPlaceholder(new BsonArray(list));
      });
   }

   public static <T extends MqlValue> MqlEntry<T> ofEntry(MqlString k, T v) {
      Assertions.notNull("k", k);
      Assertions.notNull("v", v);
      return new MqlExpression<T>((cr) -> {
         BsonDocument document = new BsonDocument();
         document.put("k", MqlExpression.toBsonValue((CodecRegistry)cr, k));
         document.put("v", MqlExpression.toBsonValue((CodecRegistry)cr, v));
         return new MqlExpression.AstPlaceholder(document);
      });
   }

   public static <T extends MqlValue> MqlMap<T> ofMap() {
      return ofMap(new BsonDocument());
   }

   public static <T extends MqlValue> MqlMap<T> ofMap(Bson map) {
      Assertions.notNull("map", map);
      return new MqlExpression<T>((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonDocument("$literal", map.toBsonDocument(BsonDocument.class, (CodecRegistry)cr)));
      });
   }

   public static MqlDocument of(Bson document) {
      Assertions.notNull("document", document);
      return new MqlExpression<MqlDocument>((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonDocument("$literal", document.toBsonDocument(BsonDocument.class, (CodecRegistry)cr)));
      });
   }

   public static MqlValue ofNull() {
      return (new MqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonNull());
      })).assertImplementsAllExpressions();
   }

   static MqlNumber numberToMqlNumber(Number number) {
      Assertions.notNull("number", number);
      if (number instanceof Integer) {
         return of((Integer)number);
      } else if (number instanceof Long) {
         return of((Long)number);
      } else if (number instanceof Double) {
         return of((Double)number);
      } else if (number instanceof Decimal128) {
         return of((Decimal128)number);
      } else {
         throw new IllegalArgumentException("Number must be one of: Integer, Long, Double, Decimal128");
      }
   }
}
