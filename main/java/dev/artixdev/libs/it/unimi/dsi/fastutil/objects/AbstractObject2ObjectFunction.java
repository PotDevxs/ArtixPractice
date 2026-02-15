package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

public abstract class AbstractObject2ObjectFunction<K, V> implements Serializable, Object2ObjectFunction<K, V> {
   private static final long serialVersionUID = -4940583368468432370L;
   protected V defRetValue;

   protected AbstractObject2ObjectFunction() {
   }

   public void defaultReturnValue(V rv) {
      this.defRetValue = rv;
   }

   public V defaultReturnValue() {
      return this.defRetValue;
   }
}
