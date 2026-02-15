package dev.artixdev.libs.net.kyori.adventure.text.serializer.json;

import java.io.IOException;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.util.Codec;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface LegacyHoverEventSerializer {
   @NotNull
   HoverEvent.ShowItem deserializeShowItem(@NotNull Component var1) throws IOException;

   @NotNull
   Component serializeShowItem(@NotNull HoverEvent.ShowItem var1) throws IOException;

   @NotNull
   HoverEvent.ShowEntity deserializeShowEntity(@NotNull Component var1, Codec.Decoder<Component, String, ? extends RuntimeException> var2) throws IOException;

   @NotNull
   Component serializeShowEntity(@NotNull HoverEvent.ShowEntity var1, Codec.Encoder<Component, String, ? extends RuntimeException> var2) throws IOException;
}
