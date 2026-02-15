package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2IntFunction<K> implements Serializable, Object2IntFunction<K> {
   private static final long serialVersionUID = -4940583368468432370L;
   protected int defRetValue;

   protected AbstractObject2IntFunction() {
   }

   public void defaultReturnValue(int rv) {
      this.defRetValue = rv;
   }

   public int defaultReturnValue() {
      return this.defRetValue;
   }
}
