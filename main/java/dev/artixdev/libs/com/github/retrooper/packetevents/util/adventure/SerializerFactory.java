package dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure;

import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.TypeAdapterFactory;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.BlockNBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;

public class SerializerFactory implements TypeAdapterFactory {
   private final boolean downsampleColors;
   private final boolean emitLegacyHover;

   SerializerFactory(boolean downsampleColors, boolean emitLegacyHover) {
      this.downsampleColors = downsampleColors;
      this.emitLegacyHover = emitLegacyHover;
   }

   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      Class<? super T> rawType = type.getRawType();
      if (Component.class.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>)AdventureReflectionUtil.COMPONENT_SERIALIZER_CREATE.apply(gson);
      } else if (Key.class.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>)AdventureReflectionUtil.KEY_SERIALIZER_INSTANCE;
      } else if (Style.class.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>)StyleSerializerExtended.create(this.emitLegacyHover, gson);
      } else if (ClickEvent.Action.class.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>)AdventureReflectionUtil.CLICK_EVENT_ACTION_SERIALIZER_INSTANCE;
      } else if (HoverEvent.Action.class.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>)AdventureReflectionUtil.HOVER_EVENT_ACTION_SERIALIZER_INSTANCE;
      } else if (HoverEvent.ShowItem.class.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>)AdventureReflectionUtil.SHOW_ITEM_SERIALIZER_CREATE.apply(gson);
      } else if (HoverEvent.ShowEntity.class.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>)AdventureReflectionUtil.SHOW_ENTITY_SERIALIZER_CREATE.apply(gson);
      } else if (TextColorWrapper.class.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>)TextColorWrapper.Serializer.INSTANCE;
      } else if (TextColor.class.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>)(this.downsampleColors ? AdventureReflectionUtil.TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE : AdventureReflectionUtil.TEXT_COLOR_SERIALIZER_INSTANCE);
      } else if (TextDecoration.class.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>)AdventureReflectionUtil.TEXT_DECORATION_SERIALIZER_INSTANCE;
      } else {
         return BlockNBTComponent.Pos.class.isAssignableFrom(rawType) ? (TypeAdapter<T>)AdventureReflectionUtil.BLOCK_NBT_POS_SERIALIZER_INSTANCE : null;
      }
   }
}
