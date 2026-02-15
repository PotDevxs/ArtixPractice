package dev.artixdev.libs.com.google.gson.internal.bind;

import dev.artixdev.libs.com.google.gson.TypeAdapter;

public abstract class SerializationDelegatingTypeAdapter<T> extends TypeAdapter<T> {
   public abstract TypeAdapter<T> getSerializationDelegate();
}
