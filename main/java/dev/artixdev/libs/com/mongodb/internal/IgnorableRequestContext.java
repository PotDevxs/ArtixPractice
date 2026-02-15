package dev.artixdev.libs.com.mongodb.internal;

import java.util.Map.Entry;
import java.util.stream.Stream;
import dev.artixdev.libs.com.mongodb.RequestContext;

public final class IgnorableRequestContext implements RequestContext {
   public static final IgnorableRequestContext INSTANCE = new IgnorableRequestContext();

   private IgnorableRequestContext() {
   }

   public <T> T get(Object key) {
      throw new UnsupportedOperationException();
   }

   public boolean hasKey(Object key) {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      throw new UnsupportedOperationException();
   }

   public void put(Object key, Object value) {
      throw new UnsupportedOperationException();
   }

   public void delete(Object key) {
      throw new UnsupportedOperationException();
   }

   public int size() {
      throw new UnsupportedOperationException();
   }

   public Stream<Entry<Object, Object>> stream() {
      throw new UnsupportedOperationException();
   }
}
