package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.time.Instant;
import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.conversions.Bson;

@Sealed
@Beta({Beta.Reason.CLIENT})
public interface MqlDocument extends MqlValue {
   MqlBoolean hasField(String var1);

   MqlDocument setField(String var1, MqlValue var2);

   MqlDocument unsetField(String var1);

   MqlValue getField(String var1);

   MqlBoolean getBoolean(String var1);

   MqlBoolean getBoolean(String var1, MqlBoolean var2);

   default MqlBoolean getBoolean(String fieldName, boolean other) {
      Assertions.notNull("fieldName", fieldName);
      return this.getBoolean(fieldName, MqlValues.of(other));
   }

   MqlNumber getNumber(String var1);

   MqlNumber getNumber(String var1, MqlNumber var2);

   default MqlNumber getNumber(String fieldName, Number other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getNumber(fieldName, MqlValues.numberToMqlNumber(other));
   }

   MqlInteger getInteger(String var1);

   MqlInteger getInteger(String var1, MqlInteger var2);

   default MqlInteger getInteger(String fieldName, int other) {
      Assertions.notNull("fieldName", fieldName);
      return this.getInteger(fieldName, MqlValues.of(other));
   }

   default MqlInteger getInteger(String fieldName, long other) {
      Assertions.notNull("fieldName", fieldName);
      return this.getInteger(fieldName, MqlValues.of(other));
   }

   MqlString getString(String var1);

   MqlString getString(String var1, MqlString var2);

   default MqlString getString(String fieldName, String other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getString(fieldName, MqlValues.of(other));
   }

   MqlDate getDate(String var1);

   MqlDate getDate(String var1, MqlDate var2);

   default MqlDate getDate(String fieldName, Instant other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getDate(fieldName, MqlValues.of(other));
   }

   MqlDocument getDocument(String var1);

   MqlDocument getDocument(String var1, MqlDocument var2);

   default MqlDocument getDocument(String fieldName, Bson other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getDocument(fieldName, MqlValues.of(other));
   }

   <T extends MqlValue> MqlMap<T> getMap(String var1);

   <T extends MqlValue> MqlMap<T> getMap(String var1, MqlMap<? extends T> var2);

   default <T extends MqlValue> MqlMap<T> getMap(String fieldName, Bson other) {
      Assertions.notNull("fieldName", fieldName);
      Assertions.notNull("other", other);
      return this.getMap(fieldName, MqlValues.ofMap(other));
   }

   <T extends MqlValue> MqlArray<T> getArray(String var1);

   <T extends MqlValue> MqlArray<T> getArray(String var1, MqlArray<? extends T> var2);

   MqlDocument merge(MqlDocument var1);

   <T extends MqlValue> MqlMap<T> asMap();

   <R extends MqlValue> R passDocumentTo(Function<? super MqlDocument, ? extends R> var1);

   <R extends MqlValue> R switchDocumentOn(Function<Branches<MqlDocument>, ? extends BranchesTerminal<MqlDocument, ? extends R>> var1);
}
