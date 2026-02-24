package dev.artixdev.libs.it.unimi.dsi.fastutil.booleans;

import java.lang.invoke.SerializedLambda;
import java.util.Comparator;

@FunctionalInterface
public interface BooleanComparator extends Comparator<Boolean> {
   int compare(boolean var1, boolean var2);

   default BooleanComparator reversed() {
      return BooleanComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Boolean ok1, Boolean ok2) {
      return this.compare(ok1.booleanValue(), ok2.booleanValue());
   }

   default BooleanComparator thenComparing(BooleanComparator second) {
      return (k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      };
   }

   default Comparator<Boolean> thenComparing(Comparator<? super Boolean> second) {
      return second instanceof BooleanComparator ? this.thenComparing((BooleanComparator)second) : Comparator.super.thenComparing(second);
   }

   // $FF: synthetic method
   public static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      int var2 = -1;
      switch(var1.hashCode()) {
      case -1692070962:
         if (var1.equals("lambda$thenComparing$e8be742d$1")) {
            var2 = 0;
         }
      default:
         switch(var2) {
         case 0:
            if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("dev/artixdev/libs/it/unimi/dsi/fastutil/booleans/BooleanComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(ZZ)I") && lambda.getImplClass().equals("dev/artixdev/libs/it/unimi/dsi/fastutil/booleans/BooleanComparator") && lambda.getImplMethodSignature().equals("(Ldev/artixdev/libs/it/unimi/dsi/fastutil/booleans/BooleanComparator;Ldev/artixdev/libs/it/unimi/dsi/fastutil/booleans/BooleanComparator;ZZ)I")) {
               BooleanComparator first = (BooleanComparator)lambda.getCapturedArg(0);
               BooleanComparator second = (BooleanComparator)lambda.getCapturedArg(1);
               return (BooleanComparator)((boolean k1, boolean k2) -> {
                  int comp = first.compare(k1, k2);
                  return comp == 0 ? second.compare(k1, k2) : comp;
               });
            }
         default:
            throw new IllegalArgumentException("Invalid lambda deserialization");
         }
      }
   }
}
