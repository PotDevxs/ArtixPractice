package dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection.Reflection;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.BlockNBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.net.kyori.adventure.util.Index;

public class AdventureReflectionUtil {
   static TypeAdapter<Key> KEY_SERIALIZER_INSTANCE;
   static Function<Gson, Object> COMPONENT_SERIALIZER_CREATE;
   static TypeAdapter<ClickEvent.Action> CLICK_EVENT_ACTION_SERIALIZER_INSTANCE;
   static TypeAdapter<HoverEvent.Action<?>> HOVER_EVENT_ACTION_SERIALIZER_INSTANCE;
   static Function<Gson, Object> SHOW_ITEM_SERIALIZER_CREATE;
   static Function<Gson, Object> SHOW_ENTITY_SERIALIZER_CREATE;
   static TypeAdapter<TextColor> TEXT_COLOR_SERIALIZER_INSTANCE;
   static TypeAdapter<TextColor> TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE;
   static TypeAdapter<TextDecoration> TEXT_DECORATION_SERIALIZER_INSTANCE;
   static TypeAdapter<BlockNBTComponent.Pos> BLOCK_NBT_POS_SERIALIZER_INSTANCE;

   static Object getSafe(Field field) {
      return getSafe(field, (Object)null);
   }

   static Object getSafe(Field field, Object instance) {
      if (field == null) {
         return null;
      } else {
         try {
            return field.get(instance);
         } catch (Exception e) {
            return null;
         }
      }
   }

   static Object invokeSafe(Method method, Object... params) {
      return invokeSafe((Object)null, method, params);
   }

   static Object invokeSafe(Object instance, Method method, Object... params) {
      if (method == null) {
         return null;
      } else {
         try {
            return method.invoke(instance, params);
         } catch (Exception e) {
            return null;
         }
      }
   }

   static Object invokeSafe(Constructor<?> constructor, Object... params) {
      if (constructor == null) {
         return null;
      } else {
         try {
            return constructor.newInstance(params);
         } catch (Exception e) {
            return null;
         }
      }
   }

   static {
      Class<?> KEY_SERIALIZER = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.KeySerializer");
      Field KEY_SERIALIZER_INSTANCE_FIELD = Reflection.getField(KEY_SERIALIZER, "INSTANCE");
      KEY_SERIALIZER_INSTANCE = (TypeAdapter)getSafe(KEY_SERIALIZER_INSTANCE_FIELD);
      Class<?> COMPONENT_SERIALIZER = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.ComponentSerializerImpl");
      Class<?> SHOW_ITEM_SERIALIZER = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.ShowItemSerializer");
      Class<?> SHOW_ENTITY_SERIALIZER = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.ShowEntitySerializer");
      Class INDEXED_SERIALIZER_CLASS;
      Field TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE_FIELD;
      Class BLOCK_NBT_COMPONENT_POS_SERIALIZER;
      Field BLOCK_NBT_COMPONENT_POS_SERIALIZER_INSTANCE_FIELD;
      if (GsonComponentSerializerExtended.LEGACY_ADVENTURE) {
         Constructor<?> COMPONENT_SERIALIZER_CONSTRUCTOR = Reflection.getConstructor(COMPONENT_SERIALIZER, 0);
         COMPONENT_SERIALIZER_CREATE = (gson) -> {
            return invokeSafe(COMPONENT_SERIALIZER_CONSTRUCTOR);
         };
         INDEXED_SERIALIZER_CLASS = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.IndexedSerializer");
         Method INDEXED_SERIALIZER_CREATE_METHOD = Reflection.getMethod(INDEXED_SERIALIZER_CLASS, "of", String.class, Index.class);
         CLICK_EVENT_ACTION_SERIALIZER_INSTANCE = (TypeAdapter)invokeSafe(INDEXED_SERIALIZER_CREATE_METHOD, "click action", ClickEvent.Action.NAMES);
         HOVER_EVENT_ACTION_SERIALIZER_INSTANCE = (TypeAdapter)invokeSafe(INDEXED_SERIALIZER_CREATE_METHOD, "hover action", HoverEvent.Action.NAMES);
         TEXT_DECORATION_SERIALIZER_INSTANCE = (TypeAdapter)invokeSafe(INDEXED_SERIALIZER_CREATE_METHOD, "text decoration", TextDecoration.NAMES);
         Constructor<?> SHOW_ITEM_SERIALIZER_CONSTRUCTOR = Reflection.getConstructor(SHOW_ITEM_SERIALIZER, 0);
         SHOW_ITEM_SERIALIZER_CREATE = (gson) -> {
            return invokeSafe(SHOW_ITEM_SERIALIZER_CONSTRUCTOR);
         };
         Constructor<?> SHOW_ENTITY_SERIALIZER_CONSTRUCTOR = Reflection.getConstructor(SHOW_ENTITY_SERIALIZER, 0);
         SHOW_ENTITY_SERIALIZER_CREATE = (gson) -> {
            return invokeSafe(SHOW_ENTITY_SERIALIZER_CONSTRUCTOR);
         };
      } else {
         Method COMPONENT_SERIALIZER_CREATE_METHOD = Reflection.getMethod(COMPONENT_SERIALIZER, "create", Gson.class);
         COMPONENT_SERIALIZER_CREATE = (gson) -> {
            return invokeSafe(COMPONENT_SERIALIZER_CREATE_METHOD, gson);
         };
         INDEXED_SERIALIZER_CLASS = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.ClickEventActionSerializer");
         TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE_FIELD = Reflection.getField(INDEXED_SERIALIZER_CLASS, "INSTANCE");
         CLICK_EVENT_ACTION_SERIALIZER_INSTANCE = (TypeAdapter)getSafe(TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE_FIELD);
         BLOCK_NBT_COMPONENT_POS_SERIALIZER = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.HoverEventActionSerializer");
         BLOCK_NBT_COMPONENT_POS_SERIALIZER_INSTANCE_FIELD = Reflection.getField(BLOCK_NBT_COMPONENT_POS_SERIALIZER, "INSTANCE");
         HOVER_EVENT_ACTION_SERIALIZER_INSTANCE = (TypeAdapter)getSafe(BLOCK_NBT_COMPONENT_POS_SERIALIZER_INSTANCE_FIELD);
         Class<?> TEXT_DECORATION_SERIALIZER = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.TextDecorationSerializer");
         Field TEXT_DECORATION_SERIALIZER_INSTANCE_FIELD = Reflection.getField(TEXT_DECORATION_SERIALIZER, "INSTANCE");
         TEXT_DECORATION_SERIALIZER_INSTANCE = (TypeAdapter)getSafe(TEXT_DECORATION_SERIALIZER_INSTANCE_FIELD);
         Method SHOW_ITEM_SERIALIZER_CREATE_METHOD = Reflection.getMethod(SHOW_ITEM_SERIALIZER, "create", Gson.class);
         SHOW_ITEM_SERIALIZER_CREATE = (gson) -> {
            return invokeSafe(SHOW_ITEM_SERIALIZER_CREATE_METHOD, gson);
         };
         Method SHOW_ENTITY_SERIALIZER_CREATE_METHOD = Reflection.getMethod(SHOW_ENTITY_SERIALIZER, "create", Gson.class);
         SHOW_ENTITY_SERIALIZER_CREATE = (gson) -> {
            return invokeSafe(SHOW_ENTITY_SERIALIZER_CREATE_METHOD, gson);
         };
      }

      Class<?> TEXT_COLOR_SERIALIZER = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.TextColorSerializer");
      Field TEXT_COLOR_SERIALIZER_INSTANCE_FIELD = Reflection.getField(TEXT_COLOR_SERIALIZER, "INSTANCE");
      TEXT_COLOR_SERIALIZER_INSTANCE = (TypeAdapter)getSafe(TEXT_COLOR_SERIALIZER_INSTANCE_FIELD);
      TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE_FIELD = Reflection.getField(TEXT_COLOR_SERIALIZER, "DOWNSAMPLE_COLOR");
      TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE = (TypeAdapter)getSafe(TEXT_COLOR_SERIALIZER_DOWNSAMPLE_COLOR_INSTANCE_FIELD);
      BLOCK_NBT_COMPONENT_POS_SERIALIZER = Reflection.getClassByNameWithoutException("dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.BlockNBTComponentPosSerializer");
      BLOCK_NBT_COMPONENT_POS_SERIALIZER_INSTANCE_FIELD = Reflection.getField(BLOCK_NBT_COMPONENT_POS_SERIALIZER, "INSTANCE");
      BLOCK_NBT_POS_SERIALIZER_INSTANCE = (TypeAdapter)getSafe(BLOCK_NBT_COMPONENT_POS_SERIALIZER_INSTANCE_FIELD);
   }
}
