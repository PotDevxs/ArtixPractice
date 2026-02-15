package dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.GsonBuilder;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.util.Services;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class GsonComponentSerializerImpl implements GsonComponentSerializer {
   private static final Optional<GsonComponentSerializer.Provider> SERVICE = Services.service(GsonComponentSerializer.Provider.class);
   static final Consumer<GsonComponentSerializer.Builder> BUILDER;
   private final Gson serializer;
   private final UnaryOperator<GsonBuilder> populator;
   private final boolean downsampleColor;
   @Nullable
   private final dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer;
   private final boolean emitLegacyHover;

   GsonComponentSerializerImpl(boolean downsampleColor, @Nullable dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer, boolean emitLegacyHover) {
      this.downsampleColor = downsampleColor;
      this.legacyHoverSerializer = legacyHoverSerializer;
      this.emitLegacyHover = emitLegacyHover;
      this.populator = (builder) -> {
         builder.registerTypeAdapterFactory(new SerializerFactory(downsampleColor, legacyHoverSerializer, emitLegacyHover));
         return builder;
      };
      this.serializer = ((GsonBuilder)this.populator.apply((new GsonBuilder()).disableHtmlEscaping())).create();
   }

   @NotNull
   public Gson serializer() {
      return this.serializer;
   }

   @NotNull
   public UnaryOperator<GsonBuilder> populator() {
      return this.populator;
   }

   @NotNull
   public Component deserialize(@NotNull String string) {
      Component component = (Component)this.serializer().fromJson(string, Component.class);
      if (component == null) {
         throw ComponentSerializerImpl.notSureHowToDeserialize(string);
      } else {
         return component;
      }
   }

   @Nullable
   public Component deserializeOr(@Nullable String input, @Nullable Component fallback) {
      if (input == null) {
         return fallback;
      } else {
         Component component = (Component)this.serializer().fromJson(input, Component.class);
         return component == null ? fallback : component;
      }
   }

   @NotNull
   public String serialize(@NotNull Component component) {
      return this.serializer().toJson((Object)component);
   }

   @NotNull
   public Component deserializeFromTree(@NotNull JsonElement input) {
      Component component = (Component)this.serializer().fromJson(input, Component.class);
      if (component == null) {
         throw ComponentSerializerImpl.notSureHowToDeserialize(input);
      } else {
         return component;
      }
   }

   @NotNull
   public JsonElement serializeToTree(@NotNull Component component) {
      return this.serializer().toJsonTree(component);
   }

   @NotNull
   public GsonComponentSerializer.Builder toBuilder() {
      return new GsonComponentSerializerImpl.BuilderImpl(this);
   }

   static {
      BUILDER = (Consumer)SERVICE.map(GsonComponentSerializer.Provider::builder).orElseGet(() -> {
         return (builder) -> {
         };
      });
   }

   static final class BuilderImpl implements GsonComponentSerializer.Builder {
      private boolean downsampleColor;
      @Nullable
      private dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer;
      private boolean emitLegacyHover;

      BuilderImpl() {
         this.downsampleColor = false;
         this.emitLegacyHover = false;
         GsonComponentSerializerImpl.BUILDER.accept(this);
      }

      BuilderImpl(GsonComponentSerializerImpl serializer) {
         this();
         this.downsampleColor = serializer.downsampleColor;
         this.emitLegacyHover = serializer.emitLegacyHover;
         this.legacyHoverSerializer = serializer.legacyHoverSerializer;
      }

      @NotNull
      public GsonComponentSerializer.Builder downsampleColors() {
         this.downsampleColor = true;
         return this;
      }

      @NotNull
      public GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer serializer) {
         this.legacyHoverSerializer = serializer;
         return this;
      }

      @NotNull
      public GsonComponentSerializer.Builder emitLegacyHoverEvent() {
         this.emitLegacyHover = true;
         return this;
      }

      @NotNull
      public GsonComponentSerializer build() {
         if (this.legacyHoverSerializer == null) {
            return this.downsampleColor ? GsonComponentSerializerImpl.Instances.LEGACY_INSTANCE : GsonComponentSerializerImpl.Instances.INSTANCE;
         } else {
            return new GsonComponentSerializerImpl(this.downsampleColor, this.legacyHoverSerializer, this.emitLegacyHover);
         }
      }
   }

   static final class Instances {
      static final GsonComponentSerializer INSTANCE;
      static final GsonComponentSerializer LEGACY_INSTANCE;

      static {
         INSTANCE = (GsonComponentSerializer)GsonComponentSerializerImpl.SERVICE.map(GsonComponentSerializer.Provider::gson).orElseGet(() -> {
            return new GsonComponentSerializerImpl(false, (dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer)null, false);
         });
         LEGACY_INSTANCE = (GsonComponentSerializer)GsonComponentSerializerImpl.SERVICE.map(GsonComponentSerializer.Provider::gsonLegacy).orElseGet(() -> {
            return new GsonComponentSerializerImpl(true, (dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer)null, true);
         });
      }
   }
}
