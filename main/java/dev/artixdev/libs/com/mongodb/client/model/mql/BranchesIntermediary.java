package dev.artixdev.libs.com.mongodb.client.model.mql;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

@Beta({Beta.Reason.CLIENT})
public final class BranchesIntermediary<T extends MqlValue, R extends MqlValue> extends BranchesTerminal<T, R> {
   BranchesIntermediary(List<Function<T, SwitchCase<R>>> branches) {
      super(branches, (Function)null);
   }

   private BranchesIntermediary<T, R> with(Function<T, SwitchCase<R>> switchCase) {
      List<Function<T, SwitchCase<R>>> v = new ArrayList(this.getBranches());
      v.add(switchCase);
      return new BranchesIntermediary(v);
   }

   private static <T extends MqlValue> MqlExpression<?> mqlEx(T value) {
      return (MqlExpression)value;
   }

   public BranchesIntermediary<T, R> is(Function<? super T, MqlBoolean> predicate, Function<? super T, ? extends R> mapping) {
      Assertions.notNull("predicate", predicate);
      Assertions.notNull("mapping", mapping);
      return this.with((value) -> {
         return new SwitchCase((MqlBoolean)predicate.apply(value), (MqlValue)mapping.apply(value));
      });
   }

   public BranchesIntermediary<T, R> eq(T v, Function<? super T, ? extends R> mapping) {
      Assertions.notNull("v", v);
      Assertions.notNull("mapping", mapping);
      return this.is((value) -> {
         return value.eq(v);
      }, mapping);
   }

   public BranchesIntermediary<T, R> lt(T v, Function<? super T, ? extends R> mapping) {
      Assertions.notNull("v", v);
      Assertions.notNull("mapping", mapping);
      return this.is((value) -> {
         return value.lt(v);
      }, mapping);
   }

   public BranchesIntermediary<T, R> lte(T v, Function<? super T, ? extends R> mapping) {
      Assertions.notNull("v", v);
      Assertions.notNull("mapping", mapping);
      return this.is((value) -> {
         return value.lte(v);
      }, mapping);
   }

   public BranchesIntermediary<T, R> isBoolean(Function<? super MqlBoolean, ? extends R> mapping) {
      Assertions.notNull("mapping", mapping);
      return this.is((v) -> {
         return mqlEx(v).isBoolean();
      }, (v) -> {
         return mapping.apply((MqlBoolean)v);
      });
   }

   public BranchesIntermediary<T, R> isNumber(Function<? super MqlNumber, ? extends R> mapping) {
      Assertions.notNull("mapping", mapping);
      return this.is((v) -> {
         return mqlEx(v).isNumber();
      }, (v) -> {
         return mapping.apply((MqlNumber)v);
      });
   }

   public BranchesIntermediary<T, R> isInteger(Function<? super MqlInteger, ? extends R> mapping) {
      Assertions.notNull("mapping", mapping);
      return this.is((v) -> {
         return mqlEx(v).isInteger();
      }, (v) -> {
         return mapping.apply((MqlInteger)v);
      });
   }

   public BranchesIntermediary<T, R> isString(Function<? super MqlString, ? extends R> mapping) {
      Assertions.notNull("mapping", mapping);
      return this.is((v) -> {
         return mqlEx(v).isString();
      }, (v) -> {
         return mapping.apply((MqlString)v);
      });
   }

   public BranchesIntermediary<T, R> isDate(Function<? super MqlDate, ? extends R> mapping) {
      Assertions.notNull("mapping", mapping);
      return this.is((v) -> {
         return mqlEx(v).isDate();
      }, (v) -> {
         return mapping.apply((MqlDate)v);
      });
   }

   public <Q extends MqlValue> BranchesIntermediary<T, R> isArray(Function<? super MqlArray<Q>, ? extends R> mapping) {
      Assertions.notNull("mapping", mapping);
      return this.is((v) -> {
         return mqlEx(v).isArray();
      }, (v) -> {
         return mapping.apply((MqlArray)v);
      });
   }

   public BranchesIntermediary<T, R> isDocument(Function<? super MqlDocument, ? extends R> mapping) {
      Assertions.notNull("mapping", mapping);
      return this.is((v) -> {
         return mqlEx(v).isDocumentOrMap();
      }, (v) -> {
         return mapping.apply((MqlDocument)v);
      });
   }

   public <Q extends MqlValue> BranchesIntermediary<T, R> isMap(Function<? super MqlMap<Q>, ? extends R> mapping) {
      Assertions.notNull("mapping", mapping);
      return this.is((v) -> {
         return mqlEx(v).isDocumentOrMap();
      }, (v) -> {
         return mapping.apply((MqlMap)v);
      });
   }

   public BranchesIntermediary<T, R> isNull(Function<? super MqlValue, ? extends R> mapping) {
      Assertions.notNull("mapping", mapping);
      return this.is((v) -> {
         return mqlEx(v).isNull();
      }, (v) -> {
         return mapping.apply(v);
      });
   }

   public BranchesTerminal<T, R> defaults(Function<? super T, ? extends R> mapping) {
      Assertions.notNull("mapping", mapping);
      return this.withDefault((value) -> {
         return mapping.apply(value);
      });
   }
}
