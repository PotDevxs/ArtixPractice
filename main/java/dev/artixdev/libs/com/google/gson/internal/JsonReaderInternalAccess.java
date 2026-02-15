package dev.artixdev.libs.com.google.gson.internal;

import java.io.IOException;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;

public abstract class JsonReaderInternalAccess {
   public static JsonReaderInternalAccess INSTANCE;

   public abstract void promoteNameToValue(JsonReader reader) throws IOException;
}
