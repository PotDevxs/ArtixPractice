package dev.artixdev.libs.com.google.gson.internal.bind;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;

final class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T> {
   private final Gson context;
   private final TypeAdapter<T> delegate;
   private final Type type;

   TypeAdapterRuntimeTypeWrapper(Gson context, TypeAdapter<T> delegate, Type type) {
      this.context = context;
      this.delegate = delegate;
      this.type = type;
   }

   public T read(JsonReader in) throws IOException {
      return this.delegate.read(in);
   }

   public void write(JsonWriter out, T value) throws IOException {
      TypeAdapter<T> chosen = this.delegate;
      Type runtimeType = getRuntimeTypeIfMoreSpecific(this.type, value);
      if (runtimeType != this.type) {
         TypeAdapter<T> runtimeTypeAdapter = (TypeAdapter<T>) this.context.getAdapter(TypeToken.get(runtimeType));
         if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
            chosen = runtimeTypeAdapter;
         } else if (!isReflective(this.delegate)) {
            chosen = this.delegate;
         } else {
            chosen = runtimeTypeAdapter;
         }
      }

      chosen.write(out, value);
   }

   private static boolean isReflective(TypeAdapter<?> typeAdapter) {
      while(true) {
         if (typeAdapter instanceof SerializationDelegatingTypeAdapter) {
            TypeAdapter<?> delegate = ((SerializationDelegatingTypeAdapter)typeAdapter).getSerializationDelegate();
            if (delegate != typeAdapter) {
               typeAdapter = delegate;
               continue;
            }
         }

         return typeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter;
      }
   }

   private static Type getRuntimeTypeIfMoreSpecific(Type type, Object value) {
      if (value != null && (type instanceof Class || type instanceof TypeVariable)) {
         type = value.getClass();
      }

      return (Type)type;
   }
}
