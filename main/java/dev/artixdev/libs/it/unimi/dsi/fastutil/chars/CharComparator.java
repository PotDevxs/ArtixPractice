package dev.artixdev.libs.it.unimi.dsi.fastutil.chars;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;

@FunctionalInterface
public interface CharComparator extends Comparator<Character> {
   int compare(char var1, char var2);

   default CharComparator reversed() {
      return CharComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Character ok1, Character ok2) {
      return this.compare(ok1, ok2);
   }

   default CharComparator thenComparing(CharComparator second) {
      return (CharComparator & Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      });
   }

   default Comparator<Character> thenComparing(Comparator<? super Character> second) {
      return second instanceof CharComparator ? this.thenComparing((CharComparator)second) : Comparator.super.thenComparing(second);
   }

   // $FF: synthetic method
   static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1352911883:
         if (var1.equals("lambda$thenComparing$2b1ecd07$1")) {
            var2 = 0;
         }
      default:
         switch(var2) {
         case 0:
            if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("dev/artixdev/libs/it/unimi/dsi/fastutil/chars/CharComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(CC)I") && lambda.getImplClass().equals("dev/artixdev/libs/it/unimi/dsi/fastutil/chars/CharComparator") && lambda.getImplMethodSignature().equals("(Lit/unimi/dsi/fastutil/chars/CharComparator;CC)I")) {
               CharComparator var10000 = (CharComparator)lambda.getCapturedArg(0);
               CharComparator second = (CharComparator)lambda.getCapturedArg(1);
               return (CharComparator & Serializable)((k1, k2) -> {
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
