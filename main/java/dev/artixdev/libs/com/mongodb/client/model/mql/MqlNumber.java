package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Sealed
@Beta({Beta.Reason.CLIENT})
public interface MqlNumber extends MqlValue {
   MqlNumber multiply(MqlNumber var1);

   default MqlNumber multiply(Number other) {
      Assertions.notNull("other", other);
      return this.multiply(MqlValues.numberToMqlNumber(other));
   }

   MqlNumber divide(MqlNumber var1);

   default MqlNumber divide(Number other) {
      Assertions.notNull("other", other);
      return this.divide(MqlValues.numberToMqlNumber(other));
   }

   MqlNumber add(MqlNumber var1);

   default MqlNumber add(Number other) {
      Assertions.notNull("other", other);
      return this.add(MqlValues.numberToMqlNumber(other));
   }

   MqlNumber subtract(MqlNumber var1);

   default MqlNumber subtract(Number other) {
      Assertions.notNull("other", other);
      return this.subtract(MqlValues.numberToMqlNumber(other));
   }

   MqlNumber max(MqlNumber var1);

   MqlNumber min(MqlNumber var1);

   MqlInteger round();

   MqlNumber round(MqlInteger var1);

   MqlNumber abs();

   <R extends MqlValue> R passNumberTo(Function<? super MqlNumber, ? extends R> var1);

   <R extends MqlValue> R switchNumberOn(Function<Branches<MqlNumber>, ? extends BranchesTerminal<MqlNumber, ? extends R>> var1);
}
