package dev.artixdev.libs.com.google.gson;

public interface ExclusionStrategy {
   boolean shouldSkipField(FieldAttributes fieldAttributes);

   boolean shouldSkipClass(Class<?> clazz);
}
