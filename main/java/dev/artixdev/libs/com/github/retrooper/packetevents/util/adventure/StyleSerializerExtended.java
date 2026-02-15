package dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure;

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

public class StyleSerializerExtended extends TypeAdapter<Style> {
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
   private final Gson gson;
   private final HoverSerializer hoverSerializer;

   static TypeAdapter<Style> create(boolean emitLegacyHover, Gson gson) {
      return (new StyleSerializerExtended(emitLegacyHover, gson)).nullSafe();
   }

   private StyleSerializerExtended(boolean emitLegacyHover, Gson gson) {
      this.emitLegacyHover = emitLegacyHover;
      this.gson = gson;
      this.hoverSerializer = new HoverSerializer();
   }

   public Style read(JsonReader in) throws IOException {
      in.beginObject();
      Style.Builder style = Style.style();

      while(true) {
         while(in.hasNext()) {
            String fieldName = in.nextName();
            if (fieldName.equals("font")) {
               style.font((Key)this.gson.fromJson((JsonReader)in, (Type)Key.class));
            } else if (fieldName.equals("color")) {
               TextColorWrapper color = (TextColorWrapper)this.gson.fromJson((JsonReader)in, (Type)TextColorWrapper.class);
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
                        boolean legacy = false;
                        JsonElement rawValue;
                        if (hoverEventObject.has("contents")) {
                           rawValue = hoverEventObject.get("contents");
                        } else if (hoverEventObject.has("value")) {
                           rawValue = hoverEventObject.get("value");
                           legacy = true;
                        } else {
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
                              value = this.gson.fromJson(rawValue, Component.class);
                              break;
                           case 1:
                              action = HoverEvent.Action.SHOW_ITEM;
                              value = this.hoverSerializer.deserializeShowItem(HoverSerializer.GsonLike.fromGson(this.gson), rawValue, legacy);
                              break;
                           case 2:
                              action = HoverEvent.Action.SHOW_ENTITY;
                              value = this.hoverSerializer.deserializeShowEntity(HoverSerializer.GsonLike.fromGson(this.gson), rawValue, this.decoder(), legacy);
                              break;
                           case 3:
                              action = HoverEvent.Action.SHOW_TEXT;
                              value = this.hoverSerializer.deserializeShowAchievement(rawValue);
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
                     action = (ClickEvent.Action)this.gson.fromJson((JsonReader)in, (Type)ClickEvent.Action.class);
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

   private Codec.Decoder<Component, String, JsonParseException> decoder() {
      return (string) -> {
         return (Component)this.gson.fromJson(string, Component.class);
      };
   }

   private Codec.Encoder<Component, String, JsonParseException> encoder() {
      return (component) -> {
         return this.gson.toJson((Object)component, (Type)Component.class);
      };
   }

   public void write(JsonWriter out, Style value) throws IOException {
      out.beginObject();
      TextDecoration[] var3 = DECORATIONS;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         TextDecoration decoration = var3[var5];
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
         this.gson.toJson(color, TextColor.class, (JsonWriter)out);
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
         this.gson.toJson(clickEvent.action(), ClickEvent.Action.class, (JsonWriter)out);
         out.name("value");
         out.value(clickEvent.value());
         out.endObject();
      }

      HoverEvent<?> hoverEvent = value.hoverEvent();
      if (hoverEvent != null) {
         out.name("hoverEvent");
         out.beginObject();
         out.name("action");
         HoverEvent.Action<?> action = hoverEvent.action();
         this.gson.toJson(action, HoverEvent.Action.class, (JsonWriter)out);
         if (this.emitLegacyHover) {
            out.name("value");
            this.serializeLegacyHoverEvent(hoverEvent, out);
         } else {
            out.name("contents");
            if (action == HoverEvent.Action.SHOW_ITEM) {
               this.gson.toJson(hoverEvent.value(), HoverEvent.ShowItem.class, (JsonWriter)out);
            } else if (action == HoverEvent.Action.SHOW_ENTITY) {
               this.gson.toJson(hoverEvent.value(), HoverEvent.ShowEntity.class, (JsonWriter)out);
            } else {
               if (action != HoverEvent.Action.SHOW_TEXT) {
                  throw new JsonParseException("Don't know how to serialize " + hoverEvent.value());
               }

               this.gson.toJson(hoverEvent.value(), Component.class, (JsonWriter)out);
            }
         }

         out.endObject();
      }

      Key font = value.font();
      if (font != null) {
         out.name("font");
         this.gson.toJson(font, Key.class, (JsonWriter)out);
      }

      out.endObject();
   }

   private void serializeLegacyHoverEvent(HoverEvent<?> hoverEvent, JsonWriter out) throws IOException {
      if (hoverEvent.action() == HoverEvent.Action.SHOW_TEXT) {
         this.gson.toJson(hoverEvent.value(), Component.class, (JsonWriter)out);
      } else {
         Component serialized = null;

         try {
            if (hoverEvent.action() == HoverEvent.Action.SHOW_ENTITY) {
               serialized = this.hoverSerializer.serializeShowEntity((HoverEvent.ShowEntity)hoverEvent.value(), this.encoder());
            } else if (hoverEvent.action() == HoverEvent.Action.SHOW_ITEM) {
               serialized = this.hoverSerializer.serializeShowItem((HoverEvent.ShowItem)hoverEvent.value());
            }
         } catch (IOException e) {
            throw new JsonSyntaxException(e);
         }

         if (serialized != null) {
            this.gson.toJson(serialized, Component.class, (JsonWriter)out);
         } else {
            out.nullValue();
         }

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
