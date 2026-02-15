package dev.artixdev.libs.net.kyori.adventure.text.serializer.gson;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.GsonBuilder;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import dev.artixdev.libs.net.kyori.adventure.util.Buildable;
import dev.artixdev.libs.net.kyori.adventure.util.PlatformAPI;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface GsonComponentSerializer extends JSONComponentSerializer, Buildable<GsonComponentSerializer, GsonComponentSerializer.Builder> {
   @NotNull
   static GsonComponentSerializer gson() {
      return GsonComponentSerializerImpl.Instances.INSTANCE;
   }

   @NotNull
   static GsonComponentSerializer colorDownsamplingGson() {
      return GsonComponentSerializerImpl.Instances.LEGACY_INSTANCE;
   }

   static GsonComponentSerializer.Builder builder() {
      return new GsonComponentSerializerImpl.BuilderImpl();
   }

   @NotNull
   Gson serializer();

   @NotNull
   UnaryOperator<GsonBuilder> populator();

   @NotNull
   Component deserializeFromTree(@NotNull JsonElement var1);

   @NotNull
   JsonElement serializeToTree(@NotNull Component var1);

   @PlatformAPI
   @ApiStatus.Internal
   public interface Provider {
      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      GsonComponentSerializer gson();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      GsonComponentSerializer gsonLegacy();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      Consumer<GsonComponentSerializer.Builder> builder();
   }

   public interface Builder extends AbstractBuilder<GsonComponentSerializer>, JSONComponentSerializer.Builder, Buildable.Builder<GsonComponentSerializer> {
      @NotNull
      GsonComponentSerializer.Builder downsampleColors();

      /** @deprecated */
      @Deprecated
      @NotNull
      default GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable LegacyHoverEventSerializer serializer) {
         return this.legacyHoverEventSerializer((dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer)serializer);
      }

      @NotNull
      GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer var1);

      @NotNull
      GsonComponentSerializer.Builder emitLegacyHoverEvent();

      @NotNull
      GsonComponentSerializer build();
   }
}
