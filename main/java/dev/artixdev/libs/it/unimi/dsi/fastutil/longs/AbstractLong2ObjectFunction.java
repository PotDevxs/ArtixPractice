package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.Serializable;

public abstract class AbstractLong2ObjectFunction<V> implements Serializable, Long2ObjectFunction<V> {
   private static final long serialVersionUID = -4940583368468432370L;
   protected V defRetValue;

   protected AbstractLong2ObjectFunction() {
   }

   public void defaultReturnValue(V rv) {
      this.defRetValue = rv;
   }

   public V defaultReturnValue() {
      return this.defRetValue;
   }
}
