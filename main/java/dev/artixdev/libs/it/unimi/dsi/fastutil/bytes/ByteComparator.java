package dev.artixdev.libs.it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.Comparator;

@FunctionalInterface
public interface ByteComparator extends Comparator<Byte> {
   int compare(byte var1, byte var2);

   default ByteComparator reversed() {
      return ByteComparators.oppositeComparator(this);
   }

   /** @deprecated */
   @Deprecated
   default int compare(Byte ok1, Byte ok2) {
      return this.compare(ok1, ok2);
   }

   default ByteComparator thenComparing(ByteComparator second) {
      return (ByteComparator)((Serializable)((k1, k2) -> {
         int comp = this.compare(k1, k2);
         return comp == 0 ? second.compare(k1, k2) : comp;
      }));
   }

   default Comparator<Byte> thenComparing(Comparator<? super Byte> second) {
      return (Comparator)(second instanceof ByteComparator ? this.thenComparing((ByteComparator)second) : super.thenComparing(second));
   }

   // $FF: synthetic method
   private static Object $deserializeLambda$(SerializedLambda lambda) {
      String var1 = lambda.getImplMethodName();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case 1974965104:
         if (var1.equals("lambda$thenComparing$6e387fbf$1")) {
            var2 = 0;
         }
      default:
         switch(var2) {
         case 0:
            if (lambda.getImplMethodKind() == 7 && lambda.getFunctionalInterfaceClass().equals("xyz/refinedev/libs/it/unimi/dsi/fastutil/bytes/ByteComparator") && lambda.getFunctionalInterfaceMethodName().equals("compare") && lambda.getFunctionalInterfaceMethodSignature().equals("(BB)I") && lambda.getImplClass().equals("xyz/refinedev/libs/it/unimi/dsi/fastutil/bytes/ByteComparator") && lambda.getImplMethodSignature().equals("(Lit/unimi/dsi/fastutil/bytes/ByteComparator;BB)I")) {
               ByteComparator var10000 = (ByteComparator)lambda.getCapturedArg(0);
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
