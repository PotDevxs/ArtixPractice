package dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure;

import java.util.function.UnaryOperator;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection.Reflection;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.GsonBuilder;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.GsonComponentSerializer;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.BlockNBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class GsonComponentSerializerExtended implements GsonComponentSerializer {
   static boolean LEGACY_ADVENTURE = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.SerializerFactory") == null;
   private final Gson serializer;
   private final UnaryOperator<GsonBuilder> populator;

   public GsonComponentSerializerExtended(boolean downsampleColor, boolean emitLegacyHover) {
      if (LEGACY_ADVENTURE) {
         Gson tempGson = new GsonBuilder().create();
         this.populator = (builder) -> {
            builder.registerTypeHierarchyAdapter(Key.class, AdventureReflectionUtil.KEY_SERIALIZER_INSTANCE);
            builder.registerTypeHierarchyAdapter(Component.class, AdventureReflectionUtil.COMPONENT_SERIALIZER_CREATE.apply(tempGson));
            builder.registerTypeHierarchyAdapter(Style.class, new Legacy_StyleSerializerExtended(emitLegacyHover));
            builder.registerTypeAdapter(ClickEvent.Action.class, AdventureReflectionUtil.CLICK_EVENT_ACTION_SERIALIZER_INSTANCE);
            builder.registerTypeAdapter(HoverEvent.Action.class, AdventureReflectionUtil.HOVER_EVENT_ACTION_SERIALIZER_INSTANCE);
            builder.registerTypeAdapter(HoverEvent.ShowItem.class, AdventureReflectionUtil.SHOW_ITEM_SERIALIZER_CREATE.apply(tempGson));
            builder.registerTypeAdapter(HoverEvent.ShowEntity.class, AdventureReflectionUtil.SHOW_ENTITY_SERIALIZER_CREATE.apply(tempGson));
            builder.registerTypeAdapter(TextColorWrapper.class, TextColorWrapper.Serializer.INSTANCE);
            builder.registerTypeHierarchyAdapter(TextColor.class, downsampleColor ? AdventureReflectionUtil.TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE : AdventureReflectionUtil.TEXT_COLOR_SERIALIZER_INSTANCE);
            builder.registerTypeAdapter(TextDecoration.class, AdventureReflectionUtil.TEXT_DECORATION_SERIALIZER_INSTANCE);
            builder.registerTypeHierarchyAdapter(BlockNBTComponent.Pos.class, AdventureReflectionUtil.BLOCK_NBT_POS_SERIALIZER_INSTANCE);
            return builder;
         };
      } else {
         this.populator = (builder) -> {
            builder.registerTypeAdapterFactory(new SerializerFactory(downsampleColor, emitLegacyHover));
            return builder;
         };
      }

      this.serializer = ((GsonBuilder)this.populator.apply(new GsonBuilder())).create();
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
         throw new JsonParseException("Don't know how to turn " + string + " into a Component");
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
         throw new JsonParseException("Don't know how to turn " + input + " into a Component");
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
      throw new UnsupportedOperationException("Builder pattern is not supported.");
   }
}
