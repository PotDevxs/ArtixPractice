package dev.artixdev.libs.com.google.gson;

public enum LongSerializationPolicy {
   DEFAULT {
      public JsonElement serialize(Long value) {
         return (JsonElement)(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
      }
   },
   STRING {
      public JsonElement serialize(Long value) {
         return (JsonElement)(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value.toString()));
      }
   };

   private LongSerializationPolicy() {
   }

   public abstract JsonElement serialize(Long value);

   LongSerializationPolicy(Object ignored) {
      this();
   }
}
