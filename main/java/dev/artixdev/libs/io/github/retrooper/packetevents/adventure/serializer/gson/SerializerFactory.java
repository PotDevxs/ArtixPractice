package dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson;

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
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class SerializerFactory implements TypeAdapterFactory {
   static final Class<Key> KEY_TYPE = Key.class;
   static final Class<Component> COMPONENT_TYPE = Component.class;
   static final Class<Style> STYLE_TYPE = Style.class;
   static final Class<ClickEvent.Action> CLICK_ACTION_TYPE = ClickEvent.Action.class;
   static final Class<HoverEvent.Action> HOVER_ACTION_TYPE = HoverEvent.Action.class;
   static final Class<HoverEvent.ShowItem> SHOW_ITEM_TYPE = HoverEvent.ShowItem.class;
   static final Class<HoverEvent.ShowEntity> SHOW_ENTITY_TYPE = HoverEvent.ShowEntity.class;
   static final Class<String> STRING_TYPE = String.class;
   static final Class<TextColorWrapper> COLOR_WRAPPER_TYPE = TextColorWrapper.class;
   static final Class<TextColor> COLOR_TYPE = TextColor.class;
   static final Class<TextDecoration> TEXT_DECORATION_TYPE = TextDecoration.class;
   static final Class<BlockNBTComponent.Pos> BLOCK_NBT_POS_TYPE = BlockNBTComponent.Pos.class;
   private final boolean downsampleColors;
   private final dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer;
   private final boolean emitLegacyHover;

   SerializerFactory(boolean downsampleColors, @Nullable dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer, boolean emitLegacyHover) {
      this.downsampleColors = downsampleColors;
      this.legacyHoverSerializer = legacyHoverSerializer;
      this.emitLegacyHover = emitLegacyHover;
   }

   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      Class<? super T> rawType = type.getRawType();
      if (COMPONENT_TYPE.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>) ComponentSerializerImpl.create(gson);
      } else if (KEY_TYPE.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>) KeySerializer.INSTANCE;
      } else if (STYLE_TYPE.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>) StyleSerializer.create(this.legacyHoverSerializer, this.emitLegacyHover, gson);
      } else if (CLICK_ACTION_TYPE.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>) ClickEventActionSerializer.INSTANCE;
      } else if (HOVER_ACTION_TYPE.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>) HoverEventActionSerializer.INSTANCE;
      } else if (SHOW_ITEM_TYPE.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>) ShowItemSerializer.create(gson);
      } else if (SHOW_ENTITY_TYPE.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>) ShowEntitySerializer.create(gson);
      } else if (COLOR_WRAPPER_TYPE.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>) TextColorWrapper.Serializer.INSTANCE;
      } else if (COLOR_TYPE.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>) (this.downsampleColors ? TextColorSerializer.DOWNSAMPLE_COLOR : TextColorSerializer.INSTANCE);
      } else if (TEXT_DECORATION_TYPE.isAssignableFrom(rawType)) {
         return (TypeAdapter<T>) TextDecorationSerializer.INSTANCE;
      } else {
         return BLOCK_NBT_POS_TYPE.isAssignableFrom(rawType) ? (TypeAdapter<T>) BlockNBTComponentPosSerializer.INSTANCE : null;
      }
   }
}
