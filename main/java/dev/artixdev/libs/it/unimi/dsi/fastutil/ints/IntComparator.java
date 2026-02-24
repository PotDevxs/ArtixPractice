package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;

@FunctionalInterface
public interface IntComparator extends Comparator<Integer> {
   int compare(int var1, int var2);

   default IntComparator reversed() {
      return IntComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Integer ok1, Integer ok2) {
      return this.compare(ok1.intValue(), ok2.intValue());
   }

   default IntComparator thenComparing(IntComparator second) {
      return (IntComparator & Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      });
   }

   default Comparator<Integer> thenComparing(Comparator<? super Integer> second) {
      return second instanceof IntComparator ? this.thenComparing((IntComparator)second) : Comparator.super.thenComparing(second);
   }

   // $FF: synthetic method
   static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      int var2 = -1;
      switch(var1.hashCode()) {
      case -1554871547:
         if (var1.equals("lambda$thenComparing$931d6fed$1")) {
            var2 = 0;
         }
      default:
         switch(var2) {
         case 0:
            if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("dev/artixdev/libs/it/unimi/dsi/fastutil/ints/IntComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(II)I") && lambda.getImplClass().equals("dev/artixdev/libs/it/unimi/dsi/fastutil/ints/IntComparator") && lambda.getImplMethodSignature().equals("(Ldev/artixdev/libs/it/unimi/dsi/fastutil/ints/IntComparator;Ldev/artixdev/libs/it/unimi/dsi/fastutil/ints/IntComparator;II)I")) {
               IntComparator var10000 = (IntComparator)lambda.getCapturedArg(0);
               IntComparator second = (IntComparator)lambda.getCapturedArg(1);
               return (IntComparator & Serializable)((k1, k2) -> {
                  int comp = var10000.compare(k1, k2);
                  return comp == 0 ? second.compare(k1, k2) : comp;
               });
            }
         default:
            throw new IllegalArgumentException("Invalid lambda deserialization");
         }
      }
   }
}
