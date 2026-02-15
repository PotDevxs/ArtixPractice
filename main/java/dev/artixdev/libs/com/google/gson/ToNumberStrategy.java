package dev.artixdev.libs.com.google.gson;

import java.io.IOException;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;

public interface ToNumberStrategy {
   Number readNumber(JsonReader reader) throws IOException;
}
