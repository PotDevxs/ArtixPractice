package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;

@Sealed
@Beta({Beta.Reason.CLIENT})
public interface MqlArray<T extends MqlValue> extends MqlValue {
   MqlArray<T> filter(Function<? super T, ? extends MqlBoolean> var1);

   <R extends MqlValue> MqlArray<R> map(Function<? super T, ? extends R> var1);

   MqlInteger size();

   MqlBoolean any(Function<? super T, MqlBoolean> var1);

   MqlBoolean all(Function<? super T, MqlBoolean> var1);

   MqlNumber sum(Function<? super T, ? extends MqlNumber> var1);

   MqlNumber multiply(Function<? super T, ? extends MqlNumber> var1);

   T max(T var1);

   T min(T var1);

   MqlArray<T> maxN(MqlInteger var1);

   MqlArray<T> minN(MqlInteger var1);

   MqlString joinStrings(Function<? super T, MqlString> var1);

   <R extends MqlValue> MqlArray<R> concatArrays(Function<? super T, ? extends MqlArray<? extends R>> var1);

   <R extends MqlValue> MqlArray<R> unionArrays(Function<? super T, ? extends MqlArray<? extends R>> var1);

   <R extends MqlValue> MqlMap<R> asMap(Function<? super T, ? extends MqlEntry<? extends R>> var1);

   T elementAt(MqlInteger var1);

   default T elementAt(int i) {
      return this.elementAt(MqlValues.of(i));
   }

   T first();

   T last();

   MqlBoolean contains(T var1);

   MqlArray<T> concat(MqlArray<? extends T> var1);

   MqlArray<T> slice(MqlInteger var1, MqlInteger var2);

   default MqlArray<T> slice(int start, int length) {
      return this.slice(MqlValues.of(start), MqlValues.of(length));
   }

   MqlArray<T> union(MqlArray<? extends T> var1);

   MqlArray<T> distinct();

   <R extends MqlValue> R passArrayTo(Function<? super MqlArray<T>, ? extends R> var1);

   <R extends MqlValue> R switchArrayOn(Function<Branches<MqlArray<T>>, ? extends BranchesTerminal<MqlArray<T>, ? extends R>> var1);
}
