package dev.artixdev.libs.com.google.gson.internal.bind;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.TypeAdapterFactory;
import dev.artixdev.libs.com.google.gson.internal.$Gson$Types;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonToken;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;

public final class ArrayTypeAdapter<E> extends TypeAdapter<Object> {
   public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
         Type type = typeToken.getType();
         if (type instanceof GenericArrayType || type instanceof Class && ((Class)type).isArray()) {
            Type componentType = $Gson$Types.getArrayComponentType(type);
            TypeAdapter<?> componentTypeAdapter = gson.getAdapter(TypeToken.get(componentType));
            TypeAdapter<T> arrayAdapter = new ArrayTypeAdapter(gson, componentTypeAdapter, $Gson$Types.getRawType(componentType));
            return arrayAdapter;
         } else {
            return null;
         }
      }
   };
   private final Class<E> componentType;
   private final TypeAdapter<E> componentTypeAdapter;

   public ArrayTypeAdapter(Gson context, TypeAdapter<E> componentTypeAdapter, Class<E> componentType) {
      this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper(context, componentTypeAdapter, componentType);
      this.componentType = componentType;
   }

   public Object read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
         in.nextNull();
         return null;
      } else {
         ArrayList<E> list = new ArrayList();
         in.beginArray();

         while(in.hasNext()) {
            E instance = this.componentTypeAdapter.read(in);
            list.add(instance);
         }

         in.endArray();
         int size = list.size();
         if (!this.componentType.isPrimitive()) {
            @SuppressWarnings("unchecked")
            E[] array = (E[])Array.newInstance(this.componentType, size);
            return list.toArray(array);
         } else {
            Object array = Array.newInstance(this.componentType, size);

            for(int i = 0; i < size; ++i) {
               Array.set(array, i, list.get(i));
            }

            return array;
         }
      }
   }

   public void write(JsonWriter out, Object array) throws IOException {
      if (array == null) {
         out.nullValue();
      } else {
         out.beginArray();
         int i = 0;

         for(int length = Array.getLength(array); i < length; ++i) {
            @SuppressWarnings("unchecked")
            E value = (E)Array.get(array, i);
            this.componentTypeAdapter.write(out, value);
         }

         out.endArray();
      }
   }
}
