package dev.artixdev.libs.com.google.gson.internal.bind;

import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonSerializer;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.TypeAdapterFactory;
import dev.artixdev.libs.com.google.gson.annotations.JsonAdapter;
import dev.artixdev.libs.com.google.gson.internal.ConstructorConstructor;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;

public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory {
   private final ConstructorConstructor constructorConstructor;

   public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
      this.constructorConstructor = constructorConstructor;
   }

   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
      Class<? super T> rawType = targetType.getRawType();
      JsonAdapter annotation = (JsonAdapter)rawType.getAnnotation(JsonAdapter.class);
      if (annotation == null) {
         return null;
      }
      @SuppressWarnings("unchecked")
      TypeAdapter<T> result = (TypeAdapter<T>)this.getTypeAdapter(this.constructorConstructor, gson, targetType, annotation);
      return result;
   }

   TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson, TypeToken<?> type, JsonAdapter annotation) {
      Object instance = constructorConstructor.get(TypeToken.get(annotation.value())).construct();
      boolean nullSafe = annotation.nullSafe();
      Object typeAdapter;
      if (instance instanceof TypeAdapter) {
         typeAdapter = (TypeAdapter)instance;
      } else if (instance instanceof TypeAdapterFactory) {
         typeAdapter = ((TypeAdapterFactory)instance).create(gson, type);
      } else {
         if (!(instance instanceof JsonSerializer) && !(instance instanceof JsonDeserializer)) {
            throw new IllegalArgumentException("Invalid attempt to bind an instance of " + instance.getClass().getName() + " as a @JsonAdapter for " + type.toString() + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer.");
         }

         JsonSerializer<?> serializer = instance instanceof JsonSerializer ? (JsonSerializer)instance : null;
         JsonDeserializer<?> deserializer = instance instanceof JsonDeserializer ? (JsonDeserializer)instance : null;
         TypeAdapter<?> tempAdapter = new TreeTypeAdapter(serializer, deserializer, gson, type, (TypeAdapterFactory)null, nullSafe);
         typeAdapter = tempAdapter;
         nullSafe = false;
      }

      if (typeAdapter != null && nullSafe) {
         typeAdapter = ((TypeAdapter)typeAdapter).nullSafe();
      }

      return (TypeAdapter)typeAdapter;
   }
}
