package dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson;

import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;

final class HoverEventActionSerializer {
   static final TypeAdapter<HoverEvent.Action<?>> INSTANCE;

   private HoverEventActionSerializer() {
   }

   static {
      INSTANCE = IndexedSerializer.lenient("hover action", HoverEvent.Action.NAMES);
   }
}
