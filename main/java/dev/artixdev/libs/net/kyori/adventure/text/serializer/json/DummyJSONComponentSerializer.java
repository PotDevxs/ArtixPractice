package dev.artixdev.libs.net.kyori.adventure.text.serializer.json;

import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class DummyJSONComponentSerializer implements JSONComponentSerializer {
   static final JSONComponentSerializer INSTANCE = new DummyJSONComponentSerializer();
   private static final String UNSUPPORTED_MESSAGE = "No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?";

   @NotNull
   public Component deserialize(@NotNull String input) {
      throw new UnsupportedOperationException("No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?");
   }

   @NotNull
   public String serialize(@NotNull Component component) {
      throw new UnsupportedOperationException("No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?");
   }

   static final class BuilderImpl implements JSONComponentSerializer.Builder {
      @NotNull
      public JSONComponentSerializer.Builder downsampleColors() {
         return this;
      }

      @NotNull
      public JSONComponentSerializer.Builder legacyHoverEventSerializer(@Nullable LegacyHoverEventSerializer serializer) {
         return this;
      }

      @NotNull
      public JSONComponentSerializer.Builder emitLegacyHoverEvent() {
         return this;
      }

      public JSONComponentSerializer build() {
         return DummyJSONComponentSerializer.INSTANCE;
      }
   }
}
