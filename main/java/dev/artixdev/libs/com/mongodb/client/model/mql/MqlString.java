package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;

@Sealed
@Beta({Beta.Reason.CLIENT})
public interface MqlString extends MqlValue {
   MqlString toLower();

   MqlString toUpper();

   MqlString append(MqlString var1);

   MqlInteger length();

   MqlInteger lengthBytes();

   MqlString substr(MqlInteger var1, MqlInteger var2);

   default MqlString substr(int start, int length) {
      return this.substr(MqlValues.of(start), MqlValues.of(length));
   }

   MqlString substrBytes(MqlInteger var1, MqlInteger var2);

   default MqlString substrBytes(int start, int length) {
      return this.substrBytes(MqlValues.of(start), MqlValues.of(length));
   }

   MqlInteger parseInteger();

   MqlDate parseDate();

   MqlDate parseDate(MqlString var1);

   MqlDate parseDate(MqlString var1, MqlString var2);

   <R extends MqlValue> R passStringTo(Function<? super MqlString, ? extends R> var1);

   <R extends MqlValue> R switchStringOn(Function<Branches<MqlString>, ? extends BranchesTerminal<MqlString, ? extends R>> var1);
}
