package dev.artixdev.libs.net.kyori.adventure.text.serializer.gson;

import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;

final class ClickEventActionSerializer {
   static final TypeAdapter<ClickEvent.Action> INSTANCE;

   private ClickEventActionSerializer() {
   }

   static {
      INSTANCE = IndexedSerializer.lenient("click action", ClickEvent.Action.NAMES);
   }
}
