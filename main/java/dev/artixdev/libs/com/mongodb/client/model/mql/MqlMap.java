package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Sealed
@Beta({Beta.Reason.CLIENT})
public interface MqlMap<T extends MqlValue> extends MqlValue {
   MqlBoolean has(MqlString var1);

   default MqlBoolean has(String key) {
      Assertions.notNull("key", key);
      return this.has(MqlValues.of(key));
   }

   T get(MqlString var1);

   default T get(String key) {
      Assertions.notNull("key", key);
      return this.get(MqlValues.of(key));
   }

   T get(MqlString var1, T var2);

   default T get(String key, T other) {
      Assertions.notNull("key", key);
      Assertions.notNull("other", other);
      return this.get(MqlValues.of(key), other);
   }

   MqlMap<T> set(MqlString var1, T var2);

   default MqlMap<T> set(String key, T value) {
      Assertions.notNull("key", key);
      Assertions.notNull("value", value);
      return this.set(MqlValues.of(key), value);
   }

   MqlMap<T> unset(MqlString var1);

   default MqlMap<T> unset(String key) {
      Assertions.notNull("key", key);
      return this.unset(MqlValues.of(key));
   }

   MqlMap<T> merge(MqlMap<? extends T> var1);

   MqlArray<MqlEntry<T>> entries();

   <R extends MqlDocument> R asDocument();

   <R extends MqlValue> R passMapTo(Function<? super MqlMap<T>, ? extends R> var1);

   <R extends MqlValue> R switchMapOn(Function<Branches<MqlMap<T>>, ? extends BranchesTerminal<MqlMap<T>, ? extends R>> var1);
}
