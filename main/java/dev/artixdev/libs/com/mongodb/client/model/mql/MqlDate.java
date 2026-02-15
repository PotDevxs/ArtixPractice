package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;

@Sealed
@Beta({Beta.Reason.CLIENT})
public interface MqlDate extends MqlValue {
   MqlInteger year(MqlString var1);

   MqlInteger month(MqlString var1);

   MqlInteger dayOfMonth(MqlString var1);

   MqlInteger dayOfWeek(MqlString var1);

   MqlInteger dayOfYear(MqlString var1);

   MqlInteger hour(MqlString var1);

   MqlInteger minute(MqlString var1);

   MqlInteger second(MqlString var1);

   MqlInteger week(MqlString var1);

   MqlInteger millisecond(MqlString var1);

   MqlString asString(MqlString var1, MqlString var2);

   <R extends MqlValue> R passDateTo(Function<? super MqlDate, ? extends R> var1);

   <R extends MqlValue> R switchDateOn(Function<Branches<MqlDate>, ? extends BranchesTerminal<MqlDate, ? extends R>> var1);
}
