package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2LongFunction<K> implements Serializable, Object2LongFunction<K> {
   private static final long serialVersionUID = -4940583368468432370L;
   protected long defRetValue;

   protected AbstractObject2LongFunction() {
   }

   public void defaultReturnValue(long rv) {
      this.defRetValue = rv;
   }

   public long defaultReturnValue() {
      return this.defRetValue;
   }
}
