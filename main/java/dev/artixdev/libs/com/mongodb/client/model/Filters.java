package dev.artixdev.libs.com.mongodb.client.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.geojson.Geometry;
import dev.artixdev.libs.com.mongodb.client.model.geojson.Point;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWriter;
import dev.artixdev.libs.org.bson.BsonDouble;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonRegularExpression;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class Filters {
   private Filters() {
   }

   public static <TItem> Bson eq(@Nullable TItem value) {
      return eq("_id", value);
   }

   public static <TItem> Bson eq(String fieldName, @Nullable TItem value) {
      return new Filters.SimpleEncodingFilter(fieldName, value);
   }

   @Beta({Beta.Reason.SERVER})
   public static <TItem> Bson eqFull(String fieldName, @Nullable TItem value) {
      return new Filters.OperatorFilter("$eq", fieldName, value);
   }

   public static <TItem> Bson ne(String fieldName, @Nullable TItem value) {
      return new Filters.OperatorFilter("$ne", fieldName, value);
   }

   public static <TItem> Bson gt(String fieldName, TItem value) {
      return new Filters.OperatorFilter("$gt", fieldName, value);
   }

   public static <TItem> Bson lt(String fieldName, TItem value) {
      return new Filters.OperatorFilter("$lt", fieldName, value);
   }

   public static <TItem> Bson gte(String fieldName, TItem value) {
      return new Filters.OperatorFilter("$gte", fieldName, value);
   }

   public static <TItem> Bson lte(String fieldName, TItem value) {
      return new Filters.OperatorFilter("$lte", fieldName, value);
   }

   @SafeVarargs
   public static <TItem> Bson in(String fieldName, TItem... values) {
      return in(fieldName, (Iterable)Arrays.asList(values));
   }

   public static <TItem> Bson in(String fieldName, Iterable<TItem> values) {
      return new Filters.IterableOperatorFilter(fieldName, "$in", values);
   }

   @SafeVarargs
   public static <TItem> Bson nin(String fieldName, TItem... values) {
      return nin(fieldName, (Iterable)Arrays.asList(values));
   }

   public static <TItem> Bson nin(String fieldName, Iterable<TItem> values) {
      return new Filters.IterableOperatorFilter(fieldName, "$nin", values);
   }

   public static Bson and(Iterable<Bson> filters) {
      return new Filters.AndFilter(filters);
   }

   public static Bson and(Bson... filters) {
      return and((Iterable)Arrays.asList(filters));
   }

   public static Bson or(Iterable<Bson> filters) {
      return new Filters.OrNorFilter(Filters.OrNorFilter.Operator.OR, filters);
   }

   public static Bson or(Bson... filters) {
      return or((Iterable)Arrays.asList(filters));
   }

   public static Bson not(Bson filter) {
      return new Filters.NotFilter(filter);
   }

   public static Bson nor(Bson... filters) {
      return nor((Iterable)Arrays.asList(filters));
   }

   public static Bson nor(Iterable<Bson> filters) {
      return new Filters.OrNorFilter(Filters.OrNorFilter.Operator.NOR, filters);
   }

   public static Bson exists(String fieldName) {
      return exists(fieldName, true);
   }

   public static Bson exists(String fieldName, boolean exists) {
      return new Filters.OperatorFilter("$exists", fieldName, BsonBoolean.valueOf(exists));
   }

   public static Bson type(String fieldName, BsonType type) {
      return new Filters.OperatorFilter("$type", fieldName, new BsonInt32(type.getValue()));
   }

   public static Bson type(String fieldName, String type) {
      return new Filters.OperatorFilter("$type", fieldName, new BsonString(type));
   }

   public static Bson mod(String fieldName, long divisor, long remainder) {
      return new Filters.OperatorFilter("$mod", fieldName, new BsonArray(Arrays.asList(new BsonInt64(divisor), new BsonInt64(remainder))));
   }

   public static Bson regex(String fieldName, String pattern) {
      return regex(fieldName, pattern, (String)null);
   }

   public static Bson regex(String fieldName, String pattern, @Nullable String options) {
      Assertions.notNull("pattern", pattern);
      return new Filters.SimpleFilter(fieldName, new BsonRegularExpression(pattern, options));
   }

   public static Bson regex(String fieldName, Pattern pattern) {
      Assertions.notNull("pattern", pattern);
      return new Filters.SimpleEncodingFilter(fieldName, pattern);
   }

   public static Bson text(String search) {
      Assertions.notNull("search", search);
      return text(search, new TextSearchOptions());
   }

   public static Bson text(String search, TextSearchOptions textSearchOptions) {
      Assertions.notNull("search", search);
      Assertions.notNull("textSearchOptions", textSearchOptions);
      return new Filters.TextFilter(search, textSearchOptions);
   }

   public static Bson where(String javaScriptExpression) {
      Assertions.notNull("javaScriptExpression", javaScriptExpression);
      return new BsonDocument("$where", new BsonString(javaScriptExpression));
   }

   public static <TExpression> Bson expr(TExpression expression) {
      return new Filters.SimpleEncodingFilter("$expr", expression);
   }

   @SafeVarargs
   public static <TItem> Bson all(String fieldName, TItem... values) {
      return all(fieldName, (Iterable)Arrays.asList(values));
   }

   public static <TItem> Bson all(String fieldName, Iterable<TItem> values) {
      return new Filters.IterableOperatorFilter(fieldName, "$all", values);
   }

   public static Bson elemMatch(final String fieldName, final Bson filter) {
      return new Bson() {
         public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            return new BsonDocument(fieldName, new BsonDocument("$elemMatch", filter.toBsonDocument(documentClass, codecRegistry)));
         }
      };
   }

   public static Bson size(String fieldName, int size) {
      return new Filters.OperatorFilter("$size", fieldName, size);
   }

   public static Bson bitsAllClear(String fieldName, long bitmask) {
      return new Filters.OperatorFilter("$bitsAllClear", fieldName, bitmask);
   }

   public static Bson bitsAllSet(String fieldName, long bitmask) {
      return new Filters.OperatorFilter("$bitsAllSet", fieldName, bitmask);
   }

   public static Bson bitsAnyClear(String fieldName, long bitmask) {
      return new Filters.OperatorFilter("$bitsAnyClear", fieldName, bitmask);
   }

   public static Bson bitsAnySet(String fieldName, long bitmask) {
      return new Filters.OperatorFilter("$bitsAnySet", fieldName, bitmask);
   }

   public static Bson geoWithin(String fieldName, Geometry geometry) {
      return new Filters.GeometryOperatorFilter("$geoWithin", fieldName, geometry);
   }

   public static Bson geoWithin(String fieldName, Bson geometry) {
      return new Filters.GeometryOperatorFilter("$geoWithin", fieldName, geometry);
   }

   public static Bson geoWithinBox(String fieldName, double lowerLeftX, double lowerLeftY, double upperRightX, double upperRightY) {
      BsonDocument box = new BsonDocument("$box", new BsonArray(Arrays.asList(new BsonArray(Arrays.asList(new BsonDouble(lowerLeftX), new BsonDouble(lowerLeftY))), new BsonArray(Arrays.asList(new BsonDouble(upperRightX), new BsonDouble(upperRightY))))));
      return new Filters.OperatorFilter("$geoWithin", fieldName, box);
   }

   public static Bson geoWithinPolygon(String fieldName, List<List<Double>> points) {
      BsonArray pointsArray = new BsonArray(points.size());
      Iterator var3 = points.iterator();

      while(var3.hasNext()) {
         List<Double> point = (List)var3.next();
         pointsArray.add((BsonValue)(new BsonArray(Arrays.asList(new BsonDouble((Double)point.get(0)), new BsonDouble((Double)point.get(1))))));
      }

      BsonDocument polygon = new BsonDocument("$polygon", pointsArray);
      return new Filters.OperatorFilter("$geoWithin", fieldName, polygon);
   }

   public static Bson geoWithinCenter(String fieldName, double x, double y, double radius) {
      BsonDocument center = new BsonDocument("$center", new BsonArray(Arrays.asList(new BsonArray(Arrays.asList(new BsonDouble(x), new BsonDouble(y))), new BsonDouble(radius))));
      return new Filters.OperatorFilter("$geoWithin", fieldName, center);
   }

   public static Bson geoWithinCenterSphere(String fieldName, double x, double y, double radius) {
      BsonDocument centerSphere = new BsonDocument("$centerSphere", new BsonArray(Arrays.asList(new BsonArray(Arrays.asList(new BsonDouble(x), new BsonDouble(y))), new BsonDouble(radius))));
      return new Filters.OperatorFilter("$geoWithin", fieldName, centerSphere);
   }

   public static Bson geoIntersects(String fieldName, Bson geometry) {
      return new Filters.GeometryOperatorFilter("$geoIntersects", fieldName, geometry);
   }

   public static Bson geoIntersects(String fieldName, Geometry geometry) {
      return new Filters.GeometryOperatorFilter("$geoIntersects", fieldName, geometry);
   }

   public static Bson near(String fieldName, Point geometry, @Nullable Double maxDistance, @Nullable Double minDistance) {
      return new Filters.GeometryOperatorFilter("$near", fieldName, geometry, maxDistance, minDistance);
   }

   public static Bson near(String fieldName, Bson geometry, @Nullable Double maxDistance, @Nullable Double minDistance) {
      return new Filters.GeometryOperatorFilter("$near", fieldName, geometry, maxDistance, minDistance);
   }

   public static Bson near(String fieldName, double x, double y, @Nullable Double maxDistance, @Nullable Double minDistance) {
      return createNearFilterDocument(fieldName, x, y, maxDistance, minDistance, "$near");
   }

   public static Bson nearSphere(String fieldName, Point geometry, @Nullable Double maxDistance, @Nullable Double minDistance) {
      return new Filters.GeometryOperatorFilter("$nearSphere", fieldName, geometry, maxDistance, minDistance);
   }

   public static Bson nearSphere(String fieldName, Bson geometry, @Nullable Double maxDistance, @Nullable Double minDistance) {
      return new Filters.GeometryOperatorFilter("$nearSphere", fieldName, geometry, maxDistance, minDistance);
   }

   public static Bson nearSphere(String fieldName, double x, double y, @Nullable Double maxDistance, @Nullable Double minDistance) {
      return createNearFilterDocument(fieldName, x, y, maxDistance, minDistance, "$nearSphere");
   }

   public static Bson jsonSchema(Bson schema) {
      return new Filters.SimpleEncodingFilter("$jsonSchema", schema);
   }

   public static Bson empty() {
      return new BsonDocument();
   }

   private static Bson createNearFilterDocument(String fieldName, double x, double y, @Nullable Double maxDistance, @Nullable Double minDistance, String operator) {
      BsonDocument nearFilter = new BsonDocument(operator, new BsonArray(Arrays.asList(new BsonDouble(x), new BsonDouble(y))));
      if (maxDistance != null) {
         nearFilter.append("$maxDistance", new BsonDouble(maxDistance));
      }

      if (minDistance != null) {
         nearFilter.append("$minDistance", new BsonDouble(minDistance));
      }

      return new BsonDocument(fieldName, nearFilter);
   }

   private static String operatorFilterToString(String fieldName, String operator, Object value) {
      return "Operator Filter{fieldName='" + fieldName + '\'' + ", operator='" + operator + '\'' + ", value=" + value + '}';
   }

   private static class SimpleEncodingFilter<TItem> implements Bson {
      private final String fieldName;
      private final TItem value;

      SimpleEncodingFilter(String fieldName, @Nullable TItem value) {
         this.fieldName = (String)Assertions.notNull("fieldName", fieldName);
         this.value = value;
      }

      public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
         BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
         writer.writeStartDocument();
         writer.writeName(this.fieldName);
         BuildersHelper.encodeValue(writer, this.value, codecRegistry);
         writer.writeEndDocument();
         return writer.getDocument();
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Filters.SimpleEncodingFilter<?> that = (Filters.SimpleEncodingFilter)o;
            return !this.fieldName.equals(that.fieldName) ? false : Objects.equals(this.value, that.value);
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.fieldName.hashCode();
         result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
         return result;
      }

      public String toString() {
         return "Filter{fieldName='" + this.fieldName + '\'' + ", value=" + this.value + '}';
      }
   }

   private static final class OperatorFilter<TItem> implements Bson {
      private final String operatorName;
      private final String fieldName;
      private final TItem value;

      OperatorFilter(String operatorName, String fieldName, @Nullable TItem value) {
         this.operatorName = (String)Assertions.notNull("operatorName", operatorName);
         this.fieldName = (String)Assertions.notNull("fieldName", fieldName);
         this.value = value;
      }

      public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
         BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
         writer.writeStartDocument();
         writer.writeName(this.fieldName);
         writer.writeStartDocument();
         writer.writeName(this.operatorName);
         BuildersHelper.encodeValue(writer, this.value, codecRegistry);
         writer.writeEndDocument();
         writer.writeEndDocument();
         return writer.getDocument();
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Filters.OperatorFilter<?> that = (Filters.OperatorFilter)o;
            if (!this.operatorName.equals(that.operatorName)) {
               return false;
            } else {
               return !this.fieldName.equals(that.fieldName) ? false : Objects.equals(this.value, that.value);
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.operatorName.hashCode();
         result = 31 * result + this.fieldName.hashCode();
         result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
         return result;
      }

      public String toString() {
         return Filters.operatorFilterToString(this.fieldName, this.operatorName, this.value);
      }
   }

   private static class IterableOperatorFilter<TItem> implements Bson {
      private final String fieldName;
      private final String operatorName;
      private final Iterable<TItem> values;

      IterableOperatorFilter(String fieldName, String operatorName, Iterable<TItem> values) {
         this.fieldName = (String)Assertions.notNull("fieldName", fieldName);
         this.operatorName = (String)Assertions.notNull("operatorName", operatorName);
         this.values = (Iterable)Assertions.notNull("values", values);
      }

      public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
         BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
         writer.writeStartDocument();
         writer.writeName(this.fieldName);
         writer.writeStartDocument();
         writer.writeName(this.operatorName);
         writer.writeStartArray();
         Iterator var4 = this.values.iterator();

         while(var4.hasNext()) {
            TItem value = (TItem) var4.next();
            BuildersHelper.encodeValue(writer, value, codecRegistry);
         }

         writer.writeEndArray();
         writer.writeEndDocument();
         writer.writeEndDocument();
         return writer.getDocument();
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Filters.IterableOperatorFilter<?> that = (Filters.IterableOperatorFilter)o;
            if (!this.fieldName.equals(that.fieldName)) {
               return false;
            } else {
               return !this.operatorName.equals(that.operatorName) ? false : this.values.equals(that.values);
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.fieldName.hashCode();
         result = 31 * result + this.operatorName.hashCode();
         result = 31 * result + this.values.hashCode();
         return result;
      }

      public String toString() {
         return Filters.operatorFilterToString(this.fieldName, this.operatorName, this.values);
      }
   }

   private static class AndFilter implements Bson {
      private final Iterable<Bson> filters;

      AndFilter(Iterable<Bson> filters) {
         this.filters = (Iterable)Assertions.notNull("filters", filters);
      }

      public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
         BsonArray clauses = new BsonArray();
         Iterator var4 = this.filters.iterator();

         while(var4.hasNext()) {
            Bson filter = (Bson)var4.next();
            clauses.add((BsonValue)filter.toBsonDocument(documentClass, codecRegistry));
         }

         return new BsonDocument("$and", clauses);
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Filters.AndFilter andFilter = (Filters.AndFilter)o;
            return this.filters.equals(andFilter.filters);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.filters.hashCode();
      }

      public String toString() {
         return "And Filter{filters=" + this.filters + '}';
      }
   }

   private static class OrNorFilter implements Bson {
      private final Filters.OrNorFilter.Operator operator;
      private final Iterable<Bson> filters;

      OrNorFilter(Filters.OrNorFilter.Operator operator, Iterable<Bson> filters) {
         this.operator = (Filters.OrNorFilter.Operator)Assertions.notNull("operator", operator);
         this.filters = (Iterable)Assertions.notNull("filters", filters);
      }

      public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
         BsonDocument orRenderable = new BsonDocument();
         BsonArray filtersArray = new BsonArray();
         Iterator var5 = this.filters.iterator();

         while(var5.hasNext()) {
            Bson filter = (Bson)var5.next();
            filtersArray.add((BsonValue)filter.toBsonDocument(documentClass, codecRegistry));
         }

         orRenderable.put((String)this.operator.name, (BsonValue)filtersArray);
         return orRenderable;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Filters.OrNorFilter that = (Filters.OrNorFilter)o;
            return this.operator != that.operator ? false : this.filters.equals(that.filters);
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.operator.hashCode();
         result = 31 * result + this.filters.hashCode();
         return result;
      }

      public String toString() {
         return this.operator.toStringName + " Filter{filters=" + this.filters + '}';
      }

      private static enum Operator {
         OR("$or", "Or"),
         NOR("$nor", "Nor");

         private final String name;
         private final String toStringName;

         private Operator(String name, String toStringName) {
            this.name = name;
            this.toStringName = toStringName;
         }

         // $FF: synthetic method
         private static Filters.OrNorFilter.Operator[] $values() {
            return new Filters.OrNorFilter.Operator[]{OR, NOR};
         }
      }
   }

   private static class NotFilter implements Bson {
      private static final Set<String> DBREF_KEYS = Collections.unmodifiableSet(new HashSet(Arrays.asList("$ref", "$id")));
      private static final Set<String> DBREF_KEYS_WITH_DB = Collections.unmodifiableSet(new HashSet(Arrays.asList("$ref", "$id", "$db")));
      private final Bson filter;

      NotFilter(Bson filter) {
         this.filter = (Bson)Assertions.notNull("filter", filter);
      }

      public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
         BsonDocument filterDocument = this.filter.toBsonDocument(documentClass, codecRegistry);
         if (filterDocument.size() == 1) {
            Entry<String, BsonValue> entry = (Entry)filterDocument.entrySet().iterator().next();
            return this.createFilter((String)entry.getKey(), (BsonValue)entry.getValue());
         } else {
            BsonArray values = new BsonArray(filterDocument.size());
            Iterator var5 = filterDocument.entrySet().iterator();

            while(var5.hasNext()) {
               Entry<String, BsonValue> docs = (Entry)var5.next();
               values.add((BsonValue)(new BsonDocument((String)docs.getKey(), (BsonValue)docs.getValue())));
            }

            return this.createFilter("$and", values);
         }
      }

      private boolean containsOperator(BsonDocument value) {
         Set<String> keys = value.keySet();
         if (!keys.equals(DBREF_KEYS) && !keys.equals(DBREF_KEYS_WITH_DB)) {
            Iterator var3 = keys.iterator();

            String key;
            do {
               if (!var3.hasNext()) {
                  return false;
               }

               key = (String)var3.next();
            } while(!key.startsWith("$"));

            return true;
         } else {
            return false;
         }
      }

      private BsonDocument createFilter(String fieldName, BsonValue value) {
         if (fieldName.startsWith("$")) {
            return new BsonDocument("$not", new BsonDocument(fieldName, value));
         } else {
            return (!value.isDocument() || !this.containsOperator(value.asDocument())) && !value.isRegularExpression() ? new BsonDocument(fieldName, new BsonDocument("$not", new BsonDocument("$eq", value))) : new BsonDocument(fieldName, new BsonDocument("$not", value));
         }
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Filters.NotFilter notFilter = (Filters.NotFilter)o;
            return this.filter.equals(notFilter.filter);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.filter.hashCode();
      }

      public String toString() {
         return "Not Filter{filter=" + this.filter + '}';
      }
   }

   private static final class SimpleFilter implements Bson {
      private final String fieldName;
      private final BsonValue value;

      private SimpleFilter(String fieldName, BsonValue value) {
         this.fieldName = (String)Assertions.notNull("fieldName", fieldName);
         this.value = (BsonValue)Assertions.notNull("value", value);
      }

      public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
         return new BsonDocument(this.fieldName, this.value);
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Filters.SimpleFilter that = (Filters.SimpleFilter)o;
            return !this.fieldName.equals(that.fieldName) ? false : this.value.equals(that.value);
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.fieldName.hashCode();
         result = 31 * result + this.value.hashCode();
         return result;
      }

      public String toString() {
         return Filters.operatorFilterToString(this.fieldName, "$eq", this.value);
      }

      // $FF: synthetic method
      SimpleFilter(String x0, BsonValue x1, Object x2) {
         this(x0, x1);
      }
   }

   private static class TextFilter implements Bson {
      private final String search;
      private final TextSearchOptions textSearchOptions;

      TextFilter(String search, TextSearchOptions textSearchOptions) {
         this.search = search;
         this.textSearchOptions = textSearchOptions;
      }

      public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
         BsonDocument searchDocument = new BsonDocument("$search", new BsonString(this.search));
         String language = this.textSearchOptions.getLanguage();
         if (language != null) {
            searchDocument.put((String)"$language", (BsonValue)(new BsonString(language)));
         }

         Boolean caseSensitive = this.textSearchOptions.getCaseSensitive();
         if (caseSensitive != null) {
            searchDocument.put((String)"$caseSensitive", (BsonValue)BsonBoolean.valueOf(caseSensitive));
         }

         Boolean diacriticSensitive = this.textSearchOptions.getDiacriticSensitive();
         if (diacriticSensitive != null) {
            searchDocument.put((String)"$diacriticSensitive", (BsonValue)BsonBoolean.valueOf(diacriticSensitive));
         }

         return new BsonDocument("$text", searchDocument);
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Filters.TextFilter that = (Filters.TextFilter)o;
            return !Objects.equals(this.search, that.search) ? false : Objects.equals(this.textSearchOptions, that.textSearchOptions);
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.search != null ? this.search.hashCode() : 0;
         result = 31 * result + (this.textSearchOptions != null ? this.textSearchOptions.hashCode() : 0);
         return result;
      }

      public String toString() {
         return "Text Filter{search='" + this.search + '\'' + ", textSearchOptions=" + this.textSearchOptions + '}';
      }
   }

   private static class GeometryOperatorFilter<TItem> implements Bson {
      private final String operatorName;
      private final String fieldName;
      private final TItem geometry;
      private final Double maxDistance;
      private final Double minDistance;

      GeometryOperatorFilter(String operatorName, String fieldName, TItem geometry) {
         this(operatorName, fieldName, geometry, (Double)null, (Double)null);
      }

      GeometryOperatorFilter(String operatorName, String fieldName, TItem geometry, @Nullable Double maxDistance, @Nullable Double minDistance) {
         this.operatorName = operatorName;
         this.fieldName = (String)Assertions.notNull("fieldName", fieldName);
         this.geometry = Assertions.notNull("geometry", geometry);
         this.maxDistance = maxDistance;
         this.minDistance = minDistance;
      }

      public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
         BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
         writer.writeStartDocument();
         writer.writeName(this.fieldName);
         writer.writeStartDocument();
         writer.writeName(this.operatorName);
         writer.writeStartDocument();
         writer.writeName("$geometry");
         BuildersHelper.encodeValue(writer, this.geometry, codecRegistry);
         if (this.maxDistance != null) {
            writer.writeDouble("$maxDistance", this.maxDistance);
         }

         if (this.minDistance != null) {
            writer.writeDouble("$minDistance", this.minDistance);
         }

         writer.writeEndDocument();
         writer.writeEndDocument();
         writer.writeEndDocument();
         return writer.getDocument();
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Filters.GeometryOperatorFilter<?> that = (Filters.GeometryOperatorFilter)o;
            if (!Objects.equals(this.operatorName, that.operatorName)) {
               return false;
            } else if (!this.fieldName.equals(that.fieldName)) {
               return false;
            } else if (!this.geometry.equals(that.geometry)) {
               return false;
            } else {
               return !Objects.equals(this.maxDistance, that.maxDistance) ? false : Objects.equals(this.minDistance, that.minDistance);
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.operatorName != null ? this.operatorName.hashCode() : 0;
         result = 31 * result + this.fieldName.hashCode();
         result = 31 * result + this.geometry.hashCode();
         result = 31 * result + (this.maxDistance != null ? this.maxDistance.hashCode() : 0);
         result = 31 * result + (this.minDistance != null ? this.minDistance.hashCode() : 0);
         return result;
      }

      public String toString() {
         return "Geometry Operator Filter{fieldName='" + this.fieldName + '\'' + ", operator='" + this.operatorName + '\'' + ", geometry=" + this.geometry + ", maxDistance=" + this.maxDistance + ", minDistance=" + this.minDistance + '}';
      }
   }
}
