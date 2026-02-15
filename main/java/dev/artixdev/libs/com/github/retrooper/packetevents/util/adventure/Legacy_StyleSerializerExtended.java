package dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import dev.artixdev.libs.com.google.gson.JsonDeserializationContext;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonNull;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonPrimitive;
import dev.artixdev.libs.com.google.gson.JsonSerializationContext;
import dev.artixdev.libs.com.google.gson.JsonSerializer;
import dev.artixdev.libs.com.google.gson.JsonSyntaxException;
import dev.artixdev.libs.com.google.gson.internal.Streams;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.net.kyori.adventure.util.Codec;

public class Legacy_StyleSerializerExtended implements JsonDeserializer<Style>, JsonSerializer<Style> {
   private static final TextDecoration[] DECORATIONS;
   static final String FONT = "font";
   static final String COLOR = "color";
   static final String INSERTION = "insertion";
   static final String CLICK_EVENT = "clickEvent";
   static final String CLICK_EVENT_ACTION = "action";
   static final String CLICK_EVENT_VALUE = "value";
   static final String HOVER_EVENT = "hoverEvent";
   static final String HOVER_EVENT_ACTION = "action";
   static final String HOVER_EVENT_CONTENTS = "contents";
   /** @deprecated */
   @Deprecated
   static final String HOVER_EVENT_VALUE = "value";
   private final boolean emitLegacyHover;
   private final HoverSerializer hoverSerializer;

   Legacy_StyleSerializerExtended(boolean emitLegacyHover) {
      this.emitLegacyHover = emitLegacyHover;
      this.hoverSerializer = new HoverSerializer();
   }

   public Style deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonObject object = json.getAsJsonObject();
      return this.deserialize(object, context);
   }

   private Style deserialize(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
      Style.Builder style = Style.style();
      if (json.has("font")) {
         style.font((Key)context.deserialize(json.get("font"), Key.class));
      }

      if (json.has("color")) {
         TextColorWrapper color = (TextColorWrapper)context.deserialize(json.get("color"), TextColorWrapper.class);
         if (color.color != null) {
            style.color(color.color);
         } else if (color.decoration != null) {
            style.decoration(color.decoration, true);
         }
      }

      TextDecoration[] var12 = DECORATIONS;
      int var5 = var12.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         TextDecoration decoration = var12[var6];
         String value = (String)TextDecoration.NAMES.key(decoration);
         if (json.has(value)) {
            style.decoration(decoration, json.get(value).getAsBoolean());
         }
      }

      if (json.has("insertion")) {
         style.insertion(json.get("insertion").getAsString());
      }

      JsonObject hoverEventObject;
      if (json.has("clickEvent")) {
         hoverEventObject = json.getAsJsonObject("clickEvent");
         if (hoverEventObject != null) {
            ClickEvent.Action action = (ClickEvent.Action)optionallyDeserialize(hoverEventObject.getAsJsonPrimitive("action"), context, ClickEvent.Action.class);
            if (action != null && action.readable()) {
               JsonPrimitive rawValue = hoverEventObject.getAsJsonPrimitive("value");
               String value = rawValue == null ? null : rawValue.getAsString();
               if (value != null) {
                  style.clickEvent(ClickEvent.clickEvent(action, value));
               }
            }
         }
      }

      if (json.has("hoverEvent")) {
         hoverEventObject = json.getAsJsonObject("hoverEvent");
         if (hoverEventObject != null) {
            JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive("action");
            if (serializedAction != null) {
               JsonElement rawValue;
               boolean legacy;
               if (hoverEventObject.has("contents")) {
                  legacy = false;
                  rawValue = hoverEventObject.get("contents");
               } else if (hoverEventObject.has("value")) {
                  rawValue = hoverEventObject.get("value");
                  legacy = true;
               } else {
                  legacy = false;
                  rawValue = null;
               }

               if (rawValue != null) {
                  HoverEvent.Action action = null;
                  Object value = null;
                  String var10 = serializedAction.getAsString();
                  byte var11 = -1;
                  switch(var10.hashCode()) {
                  case -1903644907:
                     if (var10.equals("show_item")) {
                        var11 = 1;
                     }
                     break;
                  case -1903331025:
                     if (var10.equals("show_text")) {
                        var11 = 0;
                     }
                     break;
                  case -422365971:
                     if (var10.equals("show_achievement")) {
                        var11 = 3;
                     }
                     break;
                  case 133701477:
                     if (var10.equals("show_entity")) {
                        var11 = 2;
                     }
                  }

                  switch(var11) {
                  case 0:
                     action = HoverEvent.Action.SHOW_TEXT;
                     value = context.deserialize(rawValue, Component.class);
                     break;
                  case 1:
                     action = HoverEvent.Action.SHOW_ITEM;
                     value = this.tryIgnoring(() -> {
                        HoverSerializer var10000 = this.hoverSerializer;
                        Objects.requireNonNull(context);
                        return var10000.deserializeShowItem(context::deserialize, rawValue, legacy);
                     });
                     break;
                  case 2:
                     action = HoverEvent.Action.SHOW_ENTITY;
                     value = this.tryIgnoring(() -> {
                        HoverSerializer var10000 = this.hoverSerializer;
                        Objects.requireNonNull(context);
                        return var10000.deserializeShowEntity(context::deserialize, rawValue, this.decoder(context), legacy);
                     });
                     break;
                  case 3:
                     action = HoverEvent.Action.SHOW_TEXT;
                     value = this.tryIgnoring(() -> {
                        return this.hoverSerializer.deserializeShowAchievement(rawValue);
                     });
                  }

                  if (value != null) {
                     style.hoverEvent(HoverEvent.hoverEvent(action, value));
                  }
               }
            }
         }
      }

      if (json.has("font")) {
         style.font((Key)context.deserialize(json.get("font"), Key.class));
      }

      return style.build();
   }

   private <T> T tryIgnoring(Legacy_StyleSerializerExtended.ExceptionalFunction<T> function) {
      try {
         return function.invoke();
      } catch (IOException e) {
         return null;
      }
   }

   private static <T> T optionallyDeserialize(JsonElement json, JsonDeserializationContext context, Class<T> type) {
      return json == null ? null : context.deserialize(json, type);
   }

   private Codec.Decoder<Component, String, JsonParseException> decoder(JsonDeserializationContext ctx) {
      return (string) -> {
         JsonReader reader = new JsonReader(new StringReader(string));
         return (Component)ctx.deserialize(Streams.parse(reader), Component.class);
      };
   }

   public JsonElement serialize(Style src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject json = new JsonObject();
      TextDecoration[] var5 = DECORATIONS;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         TextDecoration decoration = var5[var7];
         TextDecoration.State state = src.decoration(decoration);
         if (state != TextDecoration.State.NOT_SET) {
            String name = (String)TextDecoration.NAMES.key(decoration);

            assert name != null;

            json.addProperty(name, state == TextDecoration.State.TRUE);
         }
      }

      TextColor color = src.color();
      if (color != null) {
         json.add("color", context.serialize(color));
      }

      String insertion = src.insertion();
      if (insertion != null) {
         json.addProperty("insertion", insertion);
      }

      ClickEvent clickEvent = src.clickEvent();
      if (clickEvent != null) {
         JsonObject eventJson = new JsonObject();
         eventJson.add("action", context.serialize(clickEvent.action()));
         eventJson.addProperty("value", clickEvent.value());
         json.add("clickEvent", eventJson);
      }

      HoverEvent<?> hoverEvent = src.hoverEvent();
      if (hoverEvent != null) {
         JsonObject eventJson = new JsonObject();
         eventJson.add("action", context.serialize(hoverEvent.action()));
         JsonElement modernContents = context.serialize(hoverEvent.value());
         eventJson.add("contents", modernContents);
         if (this.emitLegacyHover) {
            eventJson.add("value", this.serializeLegacyHoverEvent(hoverEvent, modernContents, context));
         }

         json.add("hoverEvent", eventJson);
      }

      Key font = src.font();
      if (font != null) {
         json.add("font", context.serialize(font));
      }

      return json;
   }

   private JsonElement serializeLegacyHoverEvent(HoverEvent<?> hoverEvent, JsonElement modernContents, JsonSerializationContext context) {
      if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
         return modernContents;
      } else {
         Component serialized = null;

         try {
            if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
               serialized = this.hoverSerializer.serializeShowEntity((HoverEvent.ShowEntity)hoverEvent.value(), this.encoder(context));
            } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
               serialized = this.hoverSerializer.serializeShowItem((HoverEvent.ShowItem)hoverEvent.value());
            }
         } catch (IOException e) {
            throw new JsonSyntaxException(e);
         }

         return (JsonElement)(serialized == null ? JsonNull.INSTANCE : context.serialize(serialized));
      }
   }

   private Codec.Encoder<Component, String, RuntimeException> encoder(JsonSerializationContext ctx) {
      return (component) -> {
         return ctx.serialize(component).toString();
      };
   }

   static {
      DECORATIONS = new TextDecoration[]{TextDecoration.BOLD, TextDecoration.ITALIC, TextDecoration.UNDERLINED, TextDecoration.STRIKETHROUGH, TextDecoration.OBFUSCATED};
      Set<TextDecoration> knownDecorations = EnumSet.allOf(TextDecoration.class);
      TextDecoration[] var1 = DECORATIONS;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         TextDecoration decoration = var1[var3];
         knownDecorations.remove(decoration);
      }

      if (!knownDecorations.isEmpty()) {
         throw new IllegalStateException("Gson serializer is missing some text decorations: " + knownDecorations);
      }
   }

   @FunctionalInterface
   interface ExceptionalFunction<T> {
      T invoke() throws IOException;
   }
}
