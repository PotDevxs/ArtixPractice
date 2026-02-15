package dev.artixdev.libs.com.google.gson;

import dev.artixdev.libs.com.google.gson.reflect.TypeToken;

public interface TypeAdapterFactory {
   <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type);
}
