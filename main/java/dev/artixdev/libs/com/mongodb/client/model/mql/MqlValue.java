package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;

@Sealed
@Beta({Beta.Reason.CLIENT})
public interface MqlValue {
   boolean equals(Object var1);

   MqlBoolean eq(MqlValue var1);

   MqlBoolean ne(MqlValue var1);

   MqlBoolean gt(MqlValue var1);

   MqlBoolean gte(MqlValue var1);

   MqlBoolean lt(MqlValue var1);

   MqlBoolean lte(MqlValue var1);

   MqlBoolean isBooleanOr(MqlBoolean var1);

   MqlNumber isNumberOr(MqlNumber var1);

   MqlInteger isIntegerOr(MqlInteger var1);

   MqlString isStringOr(MqlString var1);

   MqlDate isDateOr(MqlDate var1);

   <T extends MqlValue> MqlArray<T> isArrayOr(MqlArray<? extends T> var1);

   <T extends MqlDocument> T isDocumentOr(T var1);

   <T extends MqlValue> MqlMap<T> isMapOr(MqlMap<? extends T> var1);

   MqlString asString();

   <R extends MqlValue> R passTo(Function<? super MqlValue, ? extends R> var1);

   <R extends MqlValue> R switchOn(Function<Branches<MqlValue>, ? extends BranchesTerminal<MqlValue, ? extends R>> var1);
}
