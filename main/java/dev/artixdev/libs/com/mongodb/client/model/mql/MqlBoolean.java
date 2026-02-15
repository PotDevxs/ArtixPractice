package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Sealed;

@Sealed
@Beta({Beta.Reason.CLIENT})
public interface MqlBoolean extends MqlValue {
   MqlBoolean not();

   MqlBoolean or(MqlBoolean var1);

   MqlBoolean and(MqlBoolean var1);

   <T extends MqlValue> T cond(T var1, T var2);

   <R extends MqlValue> R passBooleanTo(Function<? super MqlBoolean, ? extends R> var1);

   <R extends MqlValue> R switchBooleanOn(Function<Branches<MqlBoolean>, ? extends BranchesTerminal<MqlBoolean, ? extends R>> var1);
}
