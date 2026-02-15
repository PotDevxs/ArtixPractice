package dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.Set;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonPrimitive;
import dev.artixdev.libs.com.google.gson.JsonSyntaxException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonToken;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.net.kyori.adventure.util.Codec;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class StyleSerializer extends TypeAdapter<Style> {
   private static final TextDecoration[] DECORATIONS;
   private final dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHover;
   private final boolean emitLegacyHover;
   private final Gson gson;

   static TypeAdapter<Style> create(@Nullable dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHover, boolean emitLegacyHover, Gson gson) {
      return (new StyleSerializer(legacyHover, emitLegacyHover, gson)).nullSafe();
   }

   private StyleSerializer(@Nullable dev.artixdev.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHover, boolean emitLegacyHover, Gson gson) {
      this.legacyHover = legacyHover;
      this.emitLegacyHover = emitLegacyHover;
      this.gson = gson;
   }

   public Style read(JsonReader in) throws IOException {
      in.beginObject();
      Style.Builder style = Style.style();

      while(true) {
         while(in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("font")) {
               style.font((Key)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.KEY_TYPE));
            } else if (fieldName.equals("color")) {
               TextColorWrapper color = (TextColorWrapper)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.COLOR_WRAPPER_TYPE);
               if (color.color != null) {
                  style.color(color.color);
               } else if (color.decoration != null) {
                  style.decoration(color.decoration, TextDecoration.State.TRUE);
               }
            } else if (TextDecoration.NAMES.keys().contains(fieldName)) {
               style.decoration((TextDecoration)TextDecoration.NAMES.value(fieldName), this.readBoolean(in));
            } else if (fieldName.equals("insertion")) {
               style.insertion(in.nextString());
            } else if (!fieldName.equals("clickEvent")) {
               if (fieldName.equals("hoverEvent")) {
                  JsonObject hoverEventObject = (JsonObject)this.gson.fromJson((JsonReader)in, (Type)JsonObject.class);
                  if (hoverEventObject != null) {
                     JsonPrimitive serializedAction = hoverEventObject.getAsJsonPrimitive("action");
                     if (serializedAction != null) {
                        HoverEvent.Action<Object> action = (HoverEvent.Action)this.gson.fromJson((JsonElement)serializedAction, (Class)SerializerFactory.HOVER_ACTION_TYPE);
                        if (action.readable()) {
                           Class<?> actionType = action.type();
                           Object value;
                           JsonElement rawValue;
                           if (hoverEventObject.has("contents")) {
                              rawValue = hoverEventObject.get("contents");
                              if (isNullOrEmpty(rawValue)) {
                                 value = null;
                              } else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
                                 value = this.gson.fromJson(rawValue, SerializerFactory.COMPONENT_TYPE);
                              } else if (SerializerFactory.SHOW_ITEM_TYPE.isAssignableFrom(actionType)) {
                                 value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ITEM_TYPE);
                              } else if (SerializerFactory.SHOW_ENTITY_TYPE.isAssignableFrom(actionType)) {
                                 value = this.gson.fromJson(rawValue, SerializerFactory.SHOW_ENTITY_TYPE);
                              } else {
                                 value = null;
                              }
                           } else if (hoverEventObject.has("value")) {
                              rawValue = hoverEventObject.get("value");
                              if (isNullOrEmpty(rawValue)) {
                                 value = null;
                              } else if (SerializerFactory.COMPONENT_TYPE.isAssignableFrom(actionType)) {
                                 Component componentValue = (Component)this.gson.fromJson(rawValue, SerializerFactory.COMPONENT_TYPE);
                                 value = this.legacyHoverEventContents(action, componentValue);
                              } else if (SerializerFactory.STRING_TYPE.isAssignableFrom(actionType)) {
                                 value = this.gson.fromJson(rawValue, SerializerFactory.STRING_TYPE);
                              } else {
                                 value = null;
                              }
                           } else {
                              value = null;
                           }

                           if (value != null) {
                              style.hoverEvent(HoverEvent.hoverEvent(action, value));
                           }
                        }
                     }
                  }
               } else {
                  in.skipValue();
               }
            } else {
               in.beginObject();
               ClickEvent.Action action = null;
               String value = null;

               while(in.hasNext()) {
                  String clickEventField = in.nextName();
                  if (clickEventField.equals("action")) {
                     action = (ClickEvent.Action)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.CLICK_ACTION_TYPE);
                  } else if (clickEventField.equals("value")) {
                     value = in.peek() == JsonToken.NULL ? null : in.nextString();
                  } else {
                     in.skipValue();
                  }
               }

               if (action != null && action.readable() && value != null) {
                  style.clickEvent(ClickEvent.clickEvent(action, value));
               }

               in.endObject();
            }
         }

         in.endObject();
         return style.build();
      }
   }

   private static boolean isNullOrEmpty(@Nullable JsonElement element) {
      return element == null || element.isJsonNull() || element.isJsonArray() && element.getAsJsonArray().size() == 0 || element.isJsonObject() && element.getAsJsonObject().size() == 0;
   }

   private boolean readBoolean(JsonReader in) throws IOException {
      JsonToken peek = in.peek();
      if (peek == JsonToken.BOOLEAN) {
         return in.nextBoolean();
      } else if (peek != JsonToken.STRING && peek != JsonToken.NUMBER) {
         throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a boolean");
      } else {
         return Boolean.parseBoolean(in.nextString());
      }
   }

   private Object legacyHoverEventContents(HoverEvent.Action<?> action, Component rawValue) {
      if (action == HoverEvent.Action.SHOW_TEXT) {
         return rawValue;
      } else {
         if (this.legacyHover != null) {
            try {
               if (action == HoverEvent.Action.SHOW_ENTITY) {
                  return this.legacyHover.deserializeShowEntity(rawValue, this.decoder());
               }

               if (action == HoverEvent.Action.SHOW_ITEM) {
                  return this.legacyHover.deserializeShowItem(rawValue);
               }
            } catch (IOException e) {
               throw new JsonParseException(e);
            }
         }

         throw new UnsupportedOperationException();
      }
   }

   private Codec.Decoder<Component, String, JsonParseException> decoder() {
      return (string) -> {
         return (Component)this.gson.fromJson(string, SerializerFactory.COMPONENT_TYPE);
      };
   }

   private Codec.Encoder<Component, String, JsonParseException> encoder() {
      return (component) -> {
         return this.gson.toJson((Object)component, (Type)SerializerFactory.COMPONENT_TYPE);
      };
   }

   public void write(JsonWriter out, Style value) throws IOException {
      out.beginObject();
      int i = 0;

      for(int length = DECORATIONS.length; i < length; ++i) {
         TextDecoration decoration = DECORATIONS[i];
         TextDecoration.State state = value.decoration(decoration);
         if (state != TextDecoration.State.NOT_SET) {
            String name = (String)TextDecoration.NAMES.key(decoration);

            assert name != null;

            out.name(name);
            out.value(state == TextDecoration.State.TRUE);
         }
      }

      TextColor color = value.color();
      if (color != null) {
         out.name("color");
         this.gson.toJson(color, SerializerFactory.COLOR_TYPE, (JsonWriter)out);
      }

      String insertion = value.insertion();
      if (insertion != null) {
         out.name("insertion");
         out.value(insertion);
      }

      ClickEvent clickEvent = value.clickEvent();
      if (clickEvent != null) {
         out.name("clickEvent");
         out.beginObject();
         out.name("action");
         this.gson.toJson(clickEvent.action(), SerializerFactory.CLICK_ACTION_TYPE, (JsonWriter)out);
         out.name("value");
         out.value(clickEvent.value());
         out.endObject();
      }

      HoverEvent<?> hoverEvent = value.hoverEvent();
      if (hoverEvent != null && (hoverEvent.action() != HoverEvent.Action.SHOW_ACHIEVEMENT || this.emitLegacyHover)) {
         out.name("hoverEvent");
         out.beginObject();
         out.name("action");
         HoverEvent.Action<?> action = hoverEvent.action();
         this.gson.toJson(action, SerializerFactory.HOVER_ACTION_TYPE, (JsonWriter)out);
         if (action != HoverEvent.Action.SHOW_ACHIEVEMENT) {
            out.name("contents");
            if (action == HoverEvent.Action.SHOW_ITEM) {
               this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ITEM_TYPE, (JsonWriter)out);
            } else if (action == HoverEvent.Action.SHOW_ENTITY) {
               this.gson.toJson(hoverEvent.value(), SerializerFactory.SHOW_ENTITY_TYPE, (JsonWriter)out);
            } else {
               if (action != HoverEvent.Action.SHOW_TEXT) {
                  throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
               }

               this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, (JsonWriter)out);
            }
         }

         if (this.emitLegacyHover) {
            out.name("value");
            this.serializeLegacyHoverEvent(hoverEvent, out);
         }

         out.endObject();
      }

      Key font = value.font();
      if (font != null) {
         out.name("font");
         this.gson.toJson(font, SerializerFactory.KEY_TYPE, (JsonWriter)out);
      }

      out.endObject();
   }

   private void serializeLegacyHoverEvent(HoverEvent<?> hoverEvent, JsonWriter out) throws IOException {
      if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
         this.gson.toJson(hoverEvent.value(), SerializerFactory.COMPONENT_TYPE, (JsonWriter)out);
      } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
         this.gson.toJson(hoverEvent.value(), String.class, (JsonWriter)out);
      } else if (this.legacyHover != null) {
         Component serialized = null;

         try {
            if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
               serialized = this.legacyHover.serializeShowEntity((HoverEvent.ShowEntity)hoverEvent.value(), this.encoder());
            } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
               serialized = this.legacyHover.serializeShowItem((HoverEvent.ShowItem)hoverEvent.value());
            }
         } catch (IOException e) {
            throw new JsonSyntaxException(e);
         }

         if (serialized != null) {
            this.gson.toJson(serialized, SerializerFactory.COMPONENT_TYPE, (JsonWriter)out);
         } else {
            out.nullValue();
         }
      } else {
         out.nullValue();
      }

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
}
