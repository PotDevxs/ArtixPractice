package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;

@Sealed
@Beta({Beta.Reason.CLIENT})
public interface MqlInteger extends MqlNumber {
   MqlInteger multiply(MqlInteger var1);

   default MqlInteger multiply(int other) {
      return this.multiply(MqlValues.of(other));
   }

   MqlInteger add(MqlInteger var1);

   default MqlInteger add(int other) {
      return this.add(MqlValues.of(other));
   }

   MqlInteger subtract(MqlInteger var1);

   default MqlInteger subtract(int other) {
      return this.subtract(MqlValues.of(other));
   }

   MqlInteger max(MqlInteger var1);

   MqlInteger min(MqlInteger var1);

   MqlInteger abs();

   MqlDate millisecondsAsDate();

   <R extends MqlValue> R passIntegerTo(Function<? super MqlInteger, ? extends R> var1);

   <R extends MqlValue> R switchIntegerOn(Function<Branches<MqlInteger>, ? extends BranchesTerminal<MqlInteger, ? extends R>> var1);
}
