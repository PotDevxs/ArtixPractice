package dev.artixdev.libs.it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;

@FunctionalInterface
public interface ShortComparator extends Comparator<Short> {
   int compare(short var1, short var2);

   default ShortComparator reversed() {
      return ShortComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Short ok1, Short ok2) {
      return this.compare(ok1, ok2);
   }

   default ShortComparator thenComparing(ShortComparator second) {
      return (ShortComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Short> thenComparing(Comparator<? super Short> second) {
      return (Comparator)(second instanceof ShortComparator ? this.thenComparing((ShortComparator)second) : super.thenComparing(second));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case 1233647318:
         if (var1.equals("lambda$thenComparing$953dd6d$1")) {
            var2 = 0;
         }
      default:
         switch(var2) {
         case 0:
            if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("xyz/refinedev/libs/it/unimi/dsi/fastutil/shorts/ShortComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(SS)I") && lambda.getImplClass().equals("xyz/refinedev/libs/it/unimi/dsi/fastutil/shorts/ShortComparator") && lambda.getImplMethodSignature().equals("(Lit/unimi/dsi/fastutil/shorts/ShortComparator;SS)I")) {
               ShortComparator var10000 = (ShortComparator)lambda.getCapturedArg(0);
               return (k1, k2) -> {
                  int comp = this.compare(k1, k2);
                  return comp == 0 ? second.compare(k1, k2) : comp;
               };
            }
         default:
            throw new IllegalArgumentException("Invalid lambda deserialization");
         }
      }
   }
}
