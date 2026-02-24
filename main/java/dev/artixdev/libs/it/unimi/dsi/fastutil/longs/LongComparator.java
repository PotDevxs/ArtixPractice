package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;

@FunctionalInterface
public interface LongComparator extends Comparator<Long> {
   int compare(long var1, long var3);

   default LongComparator reversed() {
      return LongComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Long ok1, Long ok2) {
      return this.compare(ok1.longValue(), ok2.longValue());
   }

   default LongComparator thenComparing(LongComparator second) {
      return (LongComparator & Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      });
   }

   default Comparator<Long> thenComparing(Comparator<? super Long> second) {
      return second instanceof LongComparator ? this.thenComparing((LongComparator)second) : Comparator.super.thenComparing(second);
   }

   // $FF: synthetic method
   static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      int var2 = -1;
      switch(var1.hashCode()) {
      case -2072572564:
         if (var1.equals("lambda$thenComparing$3d6e68ef$1")) {
            var2 = 0;
         }
      default:
         switch(var2) {
         case 0:
            if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("dev/artixdev/libs/it/unimi/dsi/fastutil/longs/LongComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(JJ)I") && lambda.getImplClass().equals("dev/artixdev/libs/it/unimi/dsi/fastutil/longs/LongComparator") && lambda.getImplMethodSignature().equals("(Ldev/artixdev/libs/it/unimi/dsi/fastutil/longs/LongComparator;Ldev/artixdev/libs/it/unimi/dsi/fastutil/longs/LongComparator;JJ)I")) {
               LongComparator var10000 = (LongComparator)lambda.getCapturedArg(0);
               LongComparator second = (LongComparator)lambda.getCapturedArg(1);
               return (LongComparator & Serializable)((k1, k2) -> {
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
