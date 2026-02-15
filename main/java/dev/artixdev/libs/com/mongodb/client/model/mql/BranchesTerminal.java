package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.List;
import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Beta({Beta.Reason.CLIENT})
public class BranchesTerminal<T extends MqlValue, R extends MqlValue> {
   private final List<Function<T, SwitchCase<R>>> branches;
   private final Function<T, R> defaults;

   BranchesTerminal(List<Function<T, SwitchCase<R>>> branches, @Nullable Function<T, R> defaults) {
      this.branches = branches;
      this.defaults = defaults;
   }

   BranchesTerminal<T, R> withDefault(Function<T, R> defaults) {
      return new BranchesTerminal(this.branches, defaults);
   }

   List<Function<T, SwitchCase<R>>> getBranches() {
      return this.branches;
   }

   @Nullable
   Function<T, R> getDefaults() {
      return this.defaults;
   }
}
