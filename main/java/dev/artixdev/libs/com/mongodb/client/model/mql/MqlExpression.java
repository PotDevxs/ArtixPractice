package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

final class MqlExpression<T extends MqlValue> implements MqlArray<T>, MqlBoolean, MqlDate, MqlDocument, MqlEntry<T>, MqlInteger, MqlMap<T>, MqlNumber, MqlString, MqlValue {
   private final Function<CodecRegistry, MqlExpression.AstPlaceholder> fn;

   MqlExpression(Function<CodecRegistry, MqlExpression.AstPlaceholder> fn) {
      this.fn = fn;
   }

   BsonValue toBsonValue(CodecRegistry codecRegistry) {
      return ((MqlExpression.AstPlaceholder)this.fn.apply(codecRegistry)).bsonValue;
   }

   private MqlExpression.AstPlaceholder astDoc(String name, BsonDocument value) {
      return new MqlExpression.AstPlaceholder(new BsonDocument(name, value));
   }

   public MqlString getKey() {
      return new MqlExpression(this.getFieldInternal("k"));
   }

   public T getValue() {
      return newMqlExpression(this.getFieldInternal("v"));
   }

   public MqlEntry<T> setValue(T value) {
      Assertions.notNull("value", value);
      return this.setFieldInternal("v", value);
   }

   public MqlEntry<T> setKey(MqlString key) {
      Assertions.notNull("key", key);
      return this.setFieldInternal("k", key);
   }

   private Function<CodecRegistry, MqlExpression.AstPlaceholder> ast(String name) {
      return (cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonDocument(name, this.toBsonValue(cr)));
      };
   }

   private Function<CodecRegistry, MqlExpression.AstPlaceholder> astWrapped(String name) {
      return (cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonDocument(name, new BsonArray(Collections.singletonList(this.toBsonValue(cr)))));
      };
   }

   private Function<CodecRegistry, MqlExpression.AstPlaceholder> ast(String name, MqlValue param1) {
      return (cr) -> {
         BsonArray value = new BsonArray();
         value.add(this.toBsonValue(cr));
         value.add(toBsonValue(cr, param1));
         return new MqlExpression.AstPlaceholder(new BsonDocument(name, value));
      };
   }

   private Function<CodecRegistry, MqlExpression.AstPlaceholder> ast(String name, MqlValue param1, MqlValue param2) {
      return (cr) -> {
         BsonArray value = new BsonArray();
         value.add(this.toBsonValue(cr));
         value.add(toBsonValue(cr, param1));
         value.add(toBsonValue(cr, param2));
         return new MqlExpression.AstPlaceholder(new BsonDocument(name, value));
      };
   }

   static BsonValue toBsonValue(CodecRegistry cr, MqlValue mqlValue) {
      return ((MqlExpression)mqlValue).toBsonValue(cr);
   }

   <R extends MqlValue> R assertImplementsAllExpressions() {
      return (R) this;
   }

   private static <R extends MqlValue> R newMqlExpression(Function<CodecRegistry, MqlExpression.AstPlaceholder> ast) {
      return ((MqlExpression<R>) new MqlExpression(ast)).assertImplementsAllExpressions();
   }

   private <R extends MqlValue> R variable(String variable) {
      return newMqlExpression((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonString(variable));
      });
   }

   public MqlBoolean not() {
      return new MqlExpression(this.ast("$not"));
   }

   public MqlBoolean or(MqlBoolean other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$or", other));
   }

   public MqlBoolean and(MqlBoolean other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$and", other));
   }

   public <R extends MqlValue> R cond(R ifTrue, R ifFalse) {
      Assertions.notNull("ifTrue", ifTrue);
      Assertions.notNull("ifFalse", ifFalse);
      return newMqlExpression(this.ast("$cond", ifTrue, ifFalse));
   }

   private Function<CodecRegistry, MqlExpression.AstPlaceholder> getFieldInternal(String fieldName) {
      return (cr) -> {
         BsonValue value = fieldName.startsWith("$") ? new BsonDocument("$literal", new BsonString(fieldName)) : new BsonString(fieldName);
         return this.astDoc("$getField", (new BsonDocument()).append("input", ((MqlExpression.AstPlaceholder)this.fn.apply(cr)).bsonValue).append("field", (BsonValue)value));
      };
   }

   public MqlValue getField(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return new MqlExpression(this.getFieldInternal(fieldName));
   }

   public MqlBoolean getBoolean(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return new MqlExpression(this.getFieldInternal(fieldName));
   }

   public MqlBoolean getBoolean(String fieldName, MqlBoolean other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getBoolean(fieldName).isBooleanOr(other);
   }

   public MqlNumber getNumber(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return new MqlExpression(this.getFieldInternal(fieldName));
   }

   public MqlNumber getNumber(String fieldName, MqlNumber other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getNumber(fieldName).isNumberOr(other);
   }

   public MqlInteger getInteger(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return new MqlExpression(this.getFieldInternal(fieldName));
   }

   public MqlInteger getInteger(String fieldName, MqlInteger other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getInteger(fieldName).isIntegerOr(other);
   }

   public MqlString getString(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return new MqlExpression(this.getFieldInternal(fieldName));
   }

   public MqlString getString(String fieldName, MqlString other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getString(fieldName).isStringOr(other);
   }

   public MqlDate getDate(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return new MqlExpression(this.getFieldInternal(fieldName));
   }

   public MqlDate getDate(String fieldName, MqlDate other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getDate(fieldName).isDateOr(other);
   }

   public MqlDocument getDocument(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return new MqlExpression(this.getFieldInternal(fieldName));
   }

   public <R extends MqlValue> MqlMap<R> getMap(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return new MqlExpression(this.getFieldInternal(fieldName));
   }

   public <R extends MqlValue> MqlMap<R> getMap(String fieldName, MqlMap<? extends R> other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getMap(fieldName).isMapOr(other);
   }

   public MqlDocument getDocument(String fieldName, MqlDocument other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getDocument(fieldName).isDocumentOr(other);
   }

   public <R extends MqlValue> MqlArray<R> getArray(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return new MqlExpression(this.getFieldInternal(fieldName));
   }

   public <R extends MqlValue> MqlArray<R> getArray(String fieldName, MqlArray<? extends R> other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getArray(fieldName).isArrayOr(other);
   }

   public MqlDocument merge(MqlDocument other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$mergeObjects", other));
   }

   public MqlDocument setField(String fieldName, MqlValue value) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("value", value);
      return this.setFieldInternal(fieldName, value);
   }

   private MqlExpression<T> setFieldInternal(String fieldName, MqlValue value) {
      Assertions.notNull("fieldName", fieldName);
      return (MqlExpression)newMqlExpression((cr) -> {
         return this.astDoc("$setField", (new BsonDocument()).append("field", new BsonString(fieldName)).append("input", this.toBsonValue(cr)).append("value", toBsonValue(cr, value)));
      });
   }

   public MqlDocument unsetField(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return (MqlDocument)newMqlExpression((cr) -> {
         return this.astDoc("$unsetField", (new BsonDocument()).append("field", new BsonString(fieldName)).append("input", this.toBsonValue(cr)));
      });
   }

   public <R extends MqlValue> R passTo(Function<? super MqlValue, ? extends R> f) {
      Assertions.notNull("f", f);
      return (R) f.apply(this.assertImplementsAllExpressions());
   }

   public <R extends MqlValue> R switchOn(Function<Branches<MqlValue>, ? extends BranchesTerminal<MqlValue, ? extends R>> mapping) {
      Assertions.notNull("mapping", mapping);
      return (R) this.switchMapInternal(this.assertImplementsAllExpressions(), (BranchesTerminal)mapping.apply(new Branches()));
   }

   public <R extends MqlValue> R passBooleanTo(Function<? super MqlBoolean, ? extends R> f) {
      Assertions.notNull("f", f);
      return (R) f.apply(this.assertImplementsAllExpressions());
   }

   public <R extends MqlValue> R switchBooleanOn(Function<Branches<MqlBoolean>, ? extends BranchesTerminal<MqlBoolean, ? extends R>> mapping) {
      Assertions.notNull("mapping", mapping);
      return (R) this.switchMapInternal((MqlBoolean)this.assertImplementsAllExpressions(), (BranchesTerminal)mapping.apply(new Branches()));
   }

   public <R extends MqlValue> R passIntegerTo(Function<? super MqlInteger, ? extends R> f) {
      Assertions.notNull("f", f);
      return (R) f.apply(this.assertImplementsAllExpressions());
   }

   public <R extends MqlValue> R switchIntegerOn(Function<Branches<MqlInteger>, ? extends BranchesTerminal<MqlInteger, ? extends R>> mapping) {
      Assertions.notNull("mapping", mapping);
      return (R) this.switchMapInternal((MqlInteger)this.assertImplementsAllExpressions(), (BranchesTerminal)mapping.apply(new Branches()));
   }

   public <R extends MqlValue> R passNumberTo(Function<? super MqlNumber, ? extends R> f) {
      Assertions.notNull("f", f);
      return (R) f.apply(this.assertImplementsAllExpressions());
   }

   public <R extends MqlValue> R switchNumberOn(Function<Branches<MqlNumber>, ? extends BranchesTerminal<MqlNumber, ? extends R>> mapping) {
      Assertions.notNull("mapping", mapping);
      return (R) this.switchMapInternal((MqlNumber)this.assertImplementsAllExpressions(), (BranchesTerminal)mapping.apply(new Branches()));
   }

   public <R extends MqlValue> R passStringTo(Function<? super MqlString, ? extends R> f) {
      Assertions.notNull("f", f);
      return (R) f.apply(this.assertImplementsAllExpressions());
   }

   public <R extends MqlValue> R switchStringOn(Function<Branches<MqlString>, ? extends BranchesTerminal<MqlString, ? extends R>> mapping) {
      Assertions.notNull("mapping", mapping);
      return (R) this.switchMapInternal((MqlString)this.assertImplementsAllExpressions(), (BranchesTerminal)mapping.apply(new Branches()));
   }

   public <R extends MqlValue> R passDateTo(Function<? super MqlDate, ? extends R> f) {
      Assertions.notNull("f", f);
      return (R) f.apply(this.assertImplementsAllExpressions());
   }

   public <R extends MqlValue> R switchDateOn(Function<Branches<MqlDate>, ? extends BranchesTerminal<MqlDate, ? extends R>> mapping) {
      Assertions.notNull("mapping", mapping);
      return (R) this.switchMapInternal((MqlDate)this.assertImplementsAllExpressions(), (BranchesTerminal)mapping.apply(new Branches()));
   }

   public <R extends MqlValue> R passArrayTo(Function<? super MqlArray<T>, ? extends R> f) {
      Assertions.notNull("f", f);
      return (R) f.apply(this.assertImplementsAllExpressions());
   }

   public <R extends MqlValue> R switchArrayOn(Function<Branches<MqlArray<T>>, ? extends BranchesTerminal<MqlArray<T>, ? extends R>> mapping) {
      Assertions.notNull("mapping", mapping);
      return (R) this.switchMapInternal((MqlArray)this.assertImplementsAllExpressions(), (BranchesTerminal)mapping.apply(new Branches()));
   }

   public <R extends MqlValue> R passMapTo(Function<? super MqlMap<T>, ? extends R> f) {
      Assertions.notNull("f", f);
      return (R) f.apply(this.assertImplementsAllExpressions());
   }

   public <R extends MqlValue> R switchMapOn(Function<Branches<MqlMap<T>>, ? extends BranchesTerminal<MqlMap<T>, ? extends R>> mapping) {
      Assertions.notNull("mapping", mapping);
      return (R) this.switchMapInternal((MqlMap)this.assertImplementsAllExpressions(), (BranchesTerminal)mapping.apply(new Branches()));
   }

   public <R extends MqlValue> R passDocumentTo(Function<? super MqlDocument, ? extends R> f) {
      Assertions.notNull("f", f);
      return (R) f.apply(this.assertImplementsAllExpressions());
   }

   public <R extends MqlValue> R switchDocumentOn(Function<Branches<MqlDocument>, ? extends BranchesTerminal<MqlDocument, ? extends R>> mapping) {
      Assertions.notNull("mapping", mapping);
      return (R) this.switchMapInternal((MqlDocument)this.assertImplementsAllExpressions(), (BranchesTerminal)mapping.apply(new Branches()));
   }

   private <T0 extends MqlValue, R0 extends MqlValue> R0 switchMapInternal(T0 value, BranchesTerminal<T0, R0> construct) {
      return newMqlExpression((cr) -> {
         BsonArray branches = new BsonArray();
         Iterator var5 = construct.getBranches().iterator();

         while(var5.hasNext()) {
            Function<T0, SwitchCase<R0>> fn = (Function)var5.next();
            SwitchCase<R0> result = (SwitchCase)fn.apply(value);
            branches.add((BsonValue)(new BsonDocument()).append("case", toBsonValue(cr, result.getCaseValue())).append("then", toBsonValue(cr, result.getThenValue())));
         }

         BsonDocument switchBson = (new BsonDocument()).append("branches", branches);
         if (construct.getDefaults() != null) {
            switchBson = switchBson.append("default", toBsonValue(cr, (MqlValue)construct.getDefaults().apply(value)));
         }

         return this.astDoc("$switch", switchBson);
      });
   }

   public MqlBoolean eq(MqlValue other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$eq", other));
   }

   public MqlBoolean ne(MqlValue other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$ne", other));
   }

   public MqlBoolean gt(MqlValue other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$gt", other));
   }

   public MqlBoolean gte(MqlValue other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$gte", other));
   }

   public MqlBoolean lt(MqlValue other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$lt", other));
   }

   public MqlBoolean lte(MqlValue other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$lte", other));
   }

   MqlBoolean isBoolean() {
      return (new MqlExpression(this.astWrapped("$type"))).eq(MqlValues.of("bool"));
   }

   public MqlBoolean isBooleanOr(MqlBoolean other) {
      Assertions.notNull("other", other);
      return (MqlBoolean)this.isBoolean().cond(this, other);
   }

   MqlBoolean isNumber() {
      return new MqlExpression(this.astWrapped("$isNumber"));
   }

   public MqlNumber isNumberOr(MqlNumber other) {
      Assertions.notNull("other", other);
      return (MqlNumber)this.isNumber().cond(this, other);
   }

   MqlBoolean isInteger() {
      return (MqlBoolean)this.switchOn((on) -> {
         return on.isNumber((v) -> {
            return v.round().eq(v);
         }).defaults((v) -> {
            return MqlValues.of(false);
         });
      });
   }

   public MqlInteger isIntegerOr(MqlInteger other) {
      Assertions.notNull("other", other);
      return (MqlInteger)this.switchOn((on) -> {
         return on.isNumber((v) -> {
            return (MqlInteger)v.round().eq(v).cond(v, other);
         }).defaults((v) -> {
            return other;
         });
      });
   }

   MqlBoolean isString() {
      return (new MqlExpression(this.astWrapped("$type"))).eq(MqlValues.of("string"));
   }

   public MqlString isStringOr(MqlString other) {
      Assertions.notNull("other", other);
      return (MqlString)this.isString().cond(this, other);
   }

   MqlBoolean isDate() {
      return MqlValues.ofStringArray("date").contains(new MqlExpression(this.astWrapped("$type")));
   }

   public MqlDate isDateOr(MqlDate other) {
      Assertions.notNull("other", other);
      return (MqlDate)this.isDate().cond(this, other);
   }

   MqlBoolean isArray() {
      return new MqlExpression(this.astWrapped("$isArray"));
   }

   public <R extends MqlValue> MqlArray<R> isArrayOr(MqlArray<? extends R> other) {
      Assertions.notNull("other", other);
      return (MqlArray)this.isArray().cond((MqlArray)this.assertImplementsAllExpressions(), other);
   }

   MqlBoolean isDocumentOrMap() {
      return (new MqlExpression(this.astWrapped("$type"))).eq(MqlValues.of("object"));
   }

   public <R extends MqlDocument> R isDocumentOr(R other) {
      Assertions.notNull("other", other);
      return (R) this.isDocumentOrMap().cond((MqlDocument)this.assertImplementsAllExpressions(), other);
   }

   public <R extends MqlValue> MqlMap<R> isMapOr(MqlMap<? extends R> other) {
      Assertions.notNull("other", other);
      MqlExpression<?> isMap = (MqlExpression)this.isDocumentOrMap();
      return (MqlMap)newMqlExpression(isMap.ast("$cond", this.assertImplementsAllExpressions(), other));
   }

   MqlBoolean isNull() {
      return this.eq(MqlValues.ofNull());
   }

   public MqlString asString() {
      return new MqlExpression(this.astWrapped("$toString"));
   }

   private Function<CodecRegistry, MqlExpression.AstPlaceholder> convertInternal(String to, MqlValue other) {
      return (cr) -> {
         return this.astDoc("$convert", (new BsonDocument()).append("input", ((MqlExpression.AstPlaceholder)this.fn.apply(cr)).bsonValue).append("onError", toBsonValue(cr, other)).append("to", new BsonString(to)));
      };
   }

   public MqlInteger parseInteger() {
      MqlValue asLong = new MqlExpression(this.ast("$toLong"));
      return new MqlExpression(this.convertInternal("int", asLong));
   }

   public <R extends MqlValue> MqlArray<R> map(Function<? super T, ? extends R> in) {
      Assertions.notNull("in", in);
      T varThis = this.variable("$$this");
      return new MqlExpression((cr) -> {
         return this.astDoc("$map", (new BsonDocument()).append("input", this.toBsonValue((CodecRegistry)cr)).append("in", toBsonValue((CodecRegistry)cr, (MqlValue)in.apply(varThis))));
      });
   }

   public MqlArray<T> filter(Function<? super T, ? extends MqlBoolean> predicate) {
      Assertions.notNull("predicate", predicate);
      T varThis = this.variable("$$this");
      return new MqlExpression((cr) -> {
         return this.astDoc("$filter", (new BsonDocument()).append("input", this.toBsonValue((CodecRegistry)cr)).append("cond", toBsonValue((CodecRegistry)cr, (MqlValue)predicate.apply(varThis))));
      });
   }

   MqlArray<T> sort() {
      return new MqlExpression((cr) -> {
         return this.astDoc("$sortArray", (new BsonDocument()).append("input", this.toBsonValue((CodecRegistry)cr)).append("sortBy", new BsonInt32(1)));
      });
   }

   private T reduce(T initialValue, BinaryOperator<T> in) {
      T varThis = this.variable("$$this");
      T varValue = this.variable("$$value");
      return newMqlExpression((cr) -> {
         return this.astDoc("$reduce", (new BsonDocument()).append("input", this.toBsonValue((CodecRegistry)cr)).append("initialValue", toBsonValue((CodecRegistry)cr, initialValue)).append("in", toBsonValue((CodecRegistry)cr, (MqlValue)in.apply(varValue, varThis))));
      });
   }

   public MqlBoolean any(Function<? super T, MqlBoolean> predicate) {
      Assertions.notNull("predicate", predicate);
      MqlExpression<MqlBoolean> array = (MqlExpression)this.map(predicate);
      return (MqlBoolean)array.reduce(MqlValues.of(false), (a, b) -> {
         return a.or(b);
      });
   }

   public MqlBoolean all(Function<? super T, MqlBoolean> predicate) {
      Assertions.notNull("predicate", predicate);
      MqlExpression<MqlBoolean> array = (MqlExpression)this.map(predicate);
      return (MqlBoolean)array.reduce(MqlValues.of(true), (a, b) -> {
         return a.and(b);
      });
   }

   public MqlNumber sum(Function<? super T, ? extends MqlNumber> mapper) {
      Assertions.notNull("mapper", mapper);
      MqlExpression<MqlNumber> array = (MqlExpression)this.map(mapper);
      return (MqlNumber)array.reduce(MqlValues.of(0), (a, b) -> {
         return a.add(b);
      });
   }

   public MqlNumber multiply(Function<? super T, ? extends MqlNumber> mapper) {
      Assertions.notNull("mapper", mapper);
      MqlExpression<MqlNumber> array = (MqlExpression)this.map(mapper);
      return (MqlNumber)array.reduce(MqlValues.of(1), (a, b) -> {
         return a.multiply(b);
      });
   }

   public T max(T other) {
      Assertions.notNull("other", other);
      return this.size().eq(MqlValues.of(0)).cond(other, this.maxN(MqlValues.of(1)).first());
   }

   public T min(T other) {
      Assertions.notNull("other", other);
      return this.size().eq(MqlValues.of(0)).cond(other, this.minN(MqlValues.of(1)).first());
   }

   public MqlArray<T> maxN(MqlInteger n) {
      Assertions.notNull("n", n);
      return (MqlArray)newMqlExpression((cr) -> {
         return this.astDoc("$maxN", (new BsonDocument()).append("input", toBsonValue(cr, this)).append("n", toBsonValue(cr, n)));
      });
   }

   public MqlArray<T> minN(MqlInteger n) {
      Assertions.notNull("n", n);
      return (MqlArray)newMqlExpression((cr) -> {
         return this.astDoc("$minN", (new BsonDocument()).append("input", toBsonValue(cr, this)).append("n", toBsonValue(cr, n)));
      });
   }

   public MqlString joinStrings(Function<? super T, MqlString> mapper) {
      Assertions.notNull("mapper", mapper);
      MqlExpression<MqlString> array = (MqlExpression)this.map(mapper);
      return (MqlString)array.reduce(MqlValues.of(""), (a, b) -> {
         return a.append(b);
      });
   }

   public <R extends MqlValue> MqlArray<R> concatArrays(Function<? super T, ? extends MqlArray<? extends R>> mapper) {
      Assertions.notNull("mapper", mapper);
      MqlExpression<MqlArray<R>> array = (MqlExpression)this.map(mapper);
      return (MqlArray)array.reduce(MqlValues.ofArray(), (a, b) -> {
         return a.concat(b);
      });
   }

   public <R extends MqlValue> MqlArray<R> unionArrays(Function<? super T, ? extends MqlArray<? extends R>> mapper) {
      Assertions.notNull("mapper", mapper);
      Assertions.notNull("mapper", mapper);
      MqlExpression<MqlArray<R>> array = (MqlExpression)this.map(mapper);
      return (MqlArray)array.reduce(MqlValues.ofArray(), (a, b) -> {
         return a.union(b);
      });
   }

   public MqlInteger size() {
      return new MqlExpression(this.astWrapped("$size"));
   }

   public T elementAt(MqlInteger i) {
      Assertions.notNull("i", i);
      return (T) (new MqlExpression(this.ast("$arrayElemAt", i))).assertImplementsAllExpressions();
   }

   public T first() {
      return (T) (new MqlExpression(this.astWrapped("$first"))).assertImplementsAllExpressions();
   }

   public T last() {
      return (T) (new MqlExpression(this.astWrapped("$last"))).assertImplementsAllExpressions();
   }

   public MqlBoolean contains(T value) {
      Assertions.notNull("value", value);
      String name = "$in";
      return (MqlBoolean)(new MqlExpression((cr) -> {
         BsonArray array = new BsonArray();
         array.add(toBsonValue((CodecRegistry)cr, value));
         array.add(this.toBsonValue((CodecRegistry)cr));
         return new MqlExpression.AstPlaceholder(new BsonDocument(name, array));
      })).assertImplementsAllExpressions();
   }

   public MqlArray<T> concat(MqlArray<? extends T> other) {
      Assertions.notNull("other", other);
      return (MqlArray)(new MqlExpression(this.ast("$concatArrays", other))).assertImplementsAllExpressions();
   }

   public MqlArray<T> slice(MqlInteger start, MqlInteger length) {
      Assertions.notNull("start", start);
      Assertions.notNull("length", length);
      return (MqlArray)(new MqlExpression(this.ast("$slice", start, length))).assertImplementsAllExpressions();
   }

   public MqlArray<T> union(MqlArray<? extends T> other) {
      Assertions.notNull("other", other);
      return (MqlArray)(new MqlExpression(this.ast("$setUnion", other))).assertImplementsAllExpressions();
   }

   public MqlArray<T> distinct() {
      return new MqlExpression(this.astWrapped("$setUnion"));
   }

   public MqlInteger multiply(MqlNumber other) {
      Assertions.notNull("other", other);
      return (MqlInteger)newMqlExpression(this.ast("$multiply", other));
   }

   public MqlNumber add(MqlNumber other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$add", other));
   }

   public MqlNumber divide(MqlNumber other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$divide", other));
   }

   public MqlNumber max(MqlNumber other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$max", other));
   }

   public MqlNumber min(MqlNumber other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$min", other));
   }

   public MqlInteger round() {
      return new MqlExpression(this.ast("$round"));
   }

   public MqlNumber round(MqlInteger place) {
      Assertions.notNull("place", place);
      return new MqlExpression(this.ast("$round", place));
   }

   public MqlInteger multiply(MqlInteger other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$multiply", other));
   }

   public MqlInteger abs() {
      return (MqlInteger)newMqlExpression(this.ast("$abs"));
   }

   public MqlDate millisecondsAsDate() {
      return (MqlDate)newMqlExpression(this.ast("$toDate"));
   }

   public MqlNumber subtract(MqlNumber other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$subtract", other));
   }

   public MqlInteger add(MqlInteger other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$add", other));
   }

   public MqlInteger subtract(MqlInteger other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$subtract", other));
   }

   public MqlInteger max(MqlInteger other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$max", other));
   }

   public MqlInteger min(MqlInteger other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$min", other));
   }

   private MqlExpression<MqlValue> usingTimezone(String name, MqlString timezone) {
      return new MqlExpression((cr) -> {
         return this.astDoc(name, (new BsonDocument()).append("date", this.toBsonValue((CodecRegistry)cr)).append("timezone", toBsonValue((CodecRegistry)cr, timezone)));
      });
   }

   public MqlInteger year(MqlString timezone) {
      Assertions.notNull("timezone", timezone);
      return this.usingTimezone("$year", timezone);
   }

   public MqlInteger month(MqlString timezone) {
      Assertions.notNull("timezone", timezone);
      return this.usingTimezone("$month", timezone);
   }

   public MqlInteger dayOfMonth(MqlString timezone) {
      Assertions.notNull("timezone", timezone);
      return this.usingTimezone("$dayOfMonth", timezone);
   }

   public MqlInteger dayOfWeek(MqlString timezone) {
      Assertions.notNull("timezone", timezone);
      return this.usingTimezone("$dayOfWeek", timezone);
   }

   public MqlInteger dayOfYear(MqlString timezone) {
      Assertions.notNull("timezone", timezone);
      return this.usingTimezone("$dayOfYear", timezone);
   }

   public MqlInteger hour(MqlString timezone) {
      Assertions.notNull("timezone", timezone);
      return this.usingTimezone("$hour", timezone);
   }

   public MqlInteger minute(MqlString timezone) {
      Assertions.notNull("timezone", timezone);
      return this.usingTimezone("$minute", timezone);
   }

   public MqlInteger second(MqlString timezone) {
      Assertions.notNull("timezone", timezone);
      return this.usingTimezone("$second", timezone);
   }

   public MqlInteger week(MqlString timezone) {
      Assertions.notNull("timezone", timezone);
      return this.usingTimezone("$week", timezone);
   }

   public MqlInteger millisecond(MqlString timezone) {
      Assertions.notNull("timezone", timezone);
      return this.usingTimezone("$millisecond", timezone);
   }

   public MqlString asString(MqlString timezone, MqlString format) {
      Assertions.notNull("timezone", timezone);
      Assertions.notNull("format", format);
      return (MqlString)newMqlExpression((cr) -> {
         return this.astDoc("$dateToString", (new BsonDocument()).append("date", this.toBsonValue(cr)).append("format", toBsonValue(cr, format)).append("timezone", toBsonValue(cr, timezone)));
      });
   }

   public MqlDate parseDate(MqlString timezone, MqlString format) {
      Assertions.notNull("timezone", timezone);
      Assertions.notNull("format", format);
      return (MqlDate)newMqlExpression((cr) -> {
         return this.astDoc("$dateFromString", (new BsonDocument()).append("dateString", this.toBsonValue(cr)).append("format", toBsonValue(cr, format)).append("timezone", toBsonValue(cr, timezone)));
      });
   }

   public MqlDate parseDate(MqlString format) {
      Assertions.notNull("format", format);
      return (MqlDate)newMqlExpression((cr) -> {
         return this.astDoc("$dateFromString", (new BsonDocument()).append("dateString", this.toBsonValue(cr)).append("format", toBsonValue(cr, format)));
      });
   }

   public MqlDate parseDate() {
      return (MqlDate)newMqlExpression((cr) -> {
         return this.astDoc("$dateFromString", (new BsonDocument()).append("dateString", this.toBsonValue(cr)));
      });
   }

   public MqlString toLower() {
      return new MqlExpression(this.ast("$toLower"));
   }

   public MqlString toUpper() {
      return new MqlExpression(this.ast("$toUpper"));
   }

   public MqlString append(MqlString other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$concat", other));
   }

   public MqlInteger length() {
      return new MqlExpression(this.ast("$strLenCP"));
   }

   public MqlInteger lengthBytes() {
      return new MqlExpression(this.ast("$strLenBytes"));
   }

   public MqlString substr(MqlInteger start, MqlInteger length) {
      Assertions.notNull("start", start);
      Assertions.notNull("length", length);
      return new MqlExpression(this.ast("$substrCP", start, length));
   }

   public MqlString substrBytes(MqlInteger start, MqlInteger length) {
      Assertions.notNull("start", start);
      Assertions.notNull("length", length);
      return new MqlExpression(this.ast("$substrBytes", start, length));
   }

   public MqlBoolean has(MqlString key) {
      Assertions.notNull("key", key);
      return this.get(key).ne(ofRem());
   }

   public MqlBoolean hasField(String fieldName) {
      Assertions.notNull("fieldName", fieldName);
      return this.has(MqlValues.of(fieldName));
   }

   static <R extends MqlValue> R ofRem() {
      return (new MqlExpression<R>((cr) -> {
         return new MqlExpression.AstPlaceholder(new BsonString("$$REMOVE"));
      })).assertImplementsAllExpressions();
   }

   public T get(MqlString key) {
      Assertions.notNull("key", key);
      return newMqlExpression((cr) -> {
         return this.astDoc("$getField", (new BsonDocument()).append("input", ((MqlExpression.AstPlaceholder)this.fn.apply(cr)).bsonValue).append("field", toBsonValue(cr, key)));
      });
   }

   public T get(MqlString key, T other) {
      Assertions.notNull("key", key);
      Assertions.notNull("other", other);
      MqlExpression<?> mqlExpression = (MqlExpression)this.get(key);
      return (T) mqlExpression.eq(ofRem()).cond(other, mqlExpression);
   }

   public MqlMap<T> set(MqlString key, T value) {
      Assertions.notNull("key", key);
      Assertions.notNull("value", value);
      return (MqlMap)newMqlExpression((cr) -> {
         return this.astDoc("$setField", (new BsonDocument()).append("field", toBsonValue(cr, key)).append("input", this.toBsonValue(cr)).append("value", toBsonValue(cr, value)));
      });
   }

   public MqlMap<T> unset(MqlString key) {
      Assertions.notNull("key", key);
      return (MqlMap)newMqlExpression((cr) -> {
         return this.astDoc("$unsetField", (new BsonDocument()).append("field", toBsonValue(cr, key)).append("input", this.toBsonValue(cr)));
      });
   }

   public MqlMap<T> merge(MqlMap<? extends T> other) {
      Assertions.notNull("other", other);
      return new MqlExpression(this.ast("$mergeObjects", other));
   }

   public MqlArray<MqlEntry<T>> entries() {
      return (MqlArray)newMqlExpression(this.ast("$objectToArray"));
   }

   public <R extends MqlValue> MqlMap<R> asMap(Function<? super T, ? extends MqlEntry<? extends R>> mapper) {
      Assertions.notNull("mapper", mapper);
      MqlExpression<MqlEntry<? extends R>> array = (MqlExpression)this.map(mapper);
      return (MqlMap)newMqlExpression(array.astWrapped("$arrayToObject"));
   }

   public <Q extends MqlValue> MqlMap<Q> asMap() {
      return (MqlMap<Q>) this;
   }

   public <R extends MqlDocument> R asDocument() {
      return (R) this;
   }

   static final class AstPlaceholder {
      private final BsonValue bsonValue;

      AstPlaceholder(BsonValue bsonValue) {
         this.bsonValue = bsonValue;
      }
   }
}
