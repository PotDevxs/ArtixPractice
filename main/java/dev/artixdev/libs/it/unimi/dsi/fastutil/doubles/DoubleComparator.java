package dev.artixdev.libs.it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;

@FunctionalInterface
public interface DoubleComparator extends Comparator<Double> {
   int compare(double var1, double var3);

   default DoubleComparator reversed() {
      return DoubleComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Double ok1, Double ok2) {
      return this.compare(ok1, ok2);
   }

   default DoubleComparator thenComparing(DoubleComparator second) {
      return (DoubleComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Double> thenComparing(Comparator<? super Double> second) {
      return (Comparator)(second instanceof DoubleComparator ? this.thenComparing((DoubleComparator)second) : super.thenComparing(second));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1318285536:
         if (var1.equals("lambda$thenComparing$f8e9881b$1")) {
            var2 = 0;
         }
      default:
         switch(var2) {
         case 0:
            if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("xyz/refinedev/libs/it/unimi/dsi/fastutil/doubles/DoubleComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(DD)I") && lambda.getImplClass().equals("xyz/refinedev/libs/it/unimi/dsi/fastutil/doubles/DoubleComparator") && lambda.getImplMethodSignature().equals("(Lit/unimi/dsi/fastutil/doubles/DoubleComparator;DD)I")) {
               DoubleComparator var10000 = (DoubleComparator)lambda.getCapturedArg(0);
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
