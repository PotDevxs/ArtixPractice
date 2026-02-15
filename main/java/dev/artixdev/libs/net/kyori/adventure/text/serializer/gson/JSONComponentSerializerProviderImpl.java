package dev.artixdev.libs.net.kyori.adventure.text.serializer.gson;

import java.util.function.Supplier;
import dev.artixdev.libs.net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import dev.artixdev.libs.net.kyori.adventure.util.Services;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class JSONComponentSerializerProviderImpl implements JSONComponentSerializer.Provider, Services.Fallback {
   @NotNull
   public JSONComponentSerializer instance() {
      return GsonComponentSerializer.gson();
   }

   @NotNull
   public Supplier<JSONComponentSerializer.Builder> builder() {
      return GsonComponentSerializer::builder;
   }

   public String toString() {
      return "JSONComponentSerializerProviderImpl[GsonComponentSerializer]";
   }
}
