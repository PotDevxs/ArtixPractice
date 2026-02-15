package dev.artixdev.libs.net.kyori.adventure.text.serializer.gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonToken;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.BlockNBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.BuildableComponent;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.ComponentBuilder;
import dev.artixdev.libs.net.kyori.adventure.text.EntityNBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.KeybindComponent;
import dev.artixdev.libs.net.kyori.adventure.text.NBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.NBTComponentBuilder;
import dev.artixdev.libs.net.kyori.adventure.text.ScoreComponent;
import dev.artixdev.libs.net.kyori.adventure.text.SelectorComponent;
import dev.artixdev.libs.net.kyori.adventure.text.StorageNBTComponent;
import dev.artixdev.libs.net.kyori.adventure.text.TextComponent;
import dev.artixdev.libs.net.kyori.adventure.text.TranslatableComponent;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class ComponentSerializerImpl extends TypeAdapter<Component> {
   static final Type COMPONENT_LIST_TYPE = (new TypeToken<List<Component>>() {
   }).getType();
   private final Gson gson;

   static TypeAdapter<Component> create(Gson gson) {
      return (new ComponentSerializerImpl(gson)).nullSafe();
   }

   private ComponentSerializerImpl(Gson gson) {
      this.gson = gson;
   }

   public BuildableComponent<?, ?> read(JsonReader in) throws IOException {
      JsonToken token = in.peek();
      if (token != JsonToken.STRING && token != JsonToken.NUMBER && token != JsonToken.BOOLEAN) {
         if (token == JsonToken.BEGIN_ARRAY) {
            ComponentBuilder<?, ?> parent = null;
            in.beginArray();

            while(in.hasNext()) {
               BuildableComponent<?, ?> child = this.read(in);
               if (parent == null) {
                  parent = child.toBuilder();
               } else {
                  parent.append((Component)child);
               }
            }

            if (parent == null) {
               throw notSureHowToDeserialize(in.getPath());
            } else {
               in.endArray();
               return parent.build();
            }
         } else if (token != JsonToken.BEGIN_OBJECT) {
            throw notSureHowToDeserialize(in.getPath());
         } else {
            JsonObject style = new JsonObject();
            List<Component> extra = Collections.emptyList();
            String text = null;
            String translate = null;
            String translateFallback = null;
            List<Component> translateWith = null;
            String scoreName = null;
            String scoreObjective = null;
            String scoreValue = null;
            String selector = null;
            String keybind = null;
            String nbt = null;
            boolean nbtInterpret = false;
            BlockNBTComponent.Pos nbtBlock = null;
            String nbtEntity = null;
            Key nbtStorage = null;
            Component separator = null;
            in.beginObject();

            while(true) {
               while(true) {
                  while(true) {
                     while(true) {
                        while(true) {
                           while(in.hasNext()) {
                              String fieldName = in.nextName();
                              if (!fieldName.equals("text")) {
                                 if (!fieldName.equals("translate")) {
                                    if (!fieldName.equals("fallback")) {
                                       if (!fieldName.equals("with")) {
                                          if (fieldName.equals("score")) {
                                             in.beginObject();

                                             while(in.hasNext()) {
                                                String scoreFieldName = in.nextName();
                                                if (scoreFieldName.equals("name")) {
                                                   scoreName = in.nextString();
                                                } else if (scoreFieldName.equals("objective")) {
                                                   scoreObjective = in.nextString();
                                                } else if (scoreFieldName.equals("value")) {
                                                   scoreValue = in.nextString();
                                                } else {
                                                   in.skipValue();
                                                }
                                             }

                                             if (scoreName == null || scoreObjective == null) {
                                                throw new JsonParseException("A score component requires a name and objective");
                                             }

                                             in.endObject();
                                          } else if (fieldName.equals("selector")) {
                                             selector = in.nextString();
                                          } else if (fieldName.equals("keybind")) {
                                             keybind = in.nextString();
                                          } else if (fieldName.equals("nbt")) {
                                             nbt = in.nextString();
                                          } else if (fieldName.equals("interpret")) {
                                             nbtInterpret = in.nextBoolean();
                                          } else if (fieldName.equals("block")) {
                                             nbtBlock = (BlockNBTComponent.Pos)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.BLOCK_NBT_POS_TYPE);
                                          } else if (fieldName.equals("entity")) {
                                             nbtEntity = in.nextString();
                                          } else if (fieldName.equals("storage")) {
                                             nbtStorage = (Key)this.gson.fromJson((JsonReader)in, (Type)SerializerFactory.KEY_TYPE);
                                          } else if (fieldName.equals("extra")) {
                                             extra = (List)this.gson.fromJson(in, COMPONENT_LIST_TYPE);
                                          } else if (fieldName.equals("separator")) {
                                             separator = this.read(in);
                                          } else {
                                             style.add(fieldName, (JsonElement)this.gson.fromJson((JsonReader)in, (Type)JsonElement.class));
                                          }
                                       } else {
                                          translateWith = (List)this.gson.fromJson(in, COMPONENT_LIST_TYPE);
                                       }
                                    } else {
                                       translateFallback = in.nextString();
                                    }
                                 } else {
                                    translate = in.nextString();
                                 }
                              } else {
                                 text = readString(in);
                              }
                           }

                           Object builder;
                           if (text != null) {
                              builder = Component.text().content(text);
                           } else if (translate != null) {
                              if (translateWith != null) {
                                 builder = Component.translatable().key(translate).fallback(translateFallback).args(translateWith);
                              } else {
                                 builder = Component.translatable().key(translate).fallback(translateFallback);
                              }
                           } else if (scoreName != null && scoreObjective != null) {
                              if (scoreValue == null) {
                                 builder = Component.score().name(scoreName).objective(scoreObjective);
                              } else {
                                 builder = Component.score().name(scoreName).objective(scoreObjective).value(scoreValue);
                              }
                           } else if (selector != null) {
                              builder = Component.selector().pattern(selector).separator(separator);
                           } else if (keybind != null) {
                              builder = Component.keybind().keybind(keybind);
                           } else {
                              if (nbt == null) {
                                 throw notSureHowToDeserialize(in.getPath());
                              }

                              if (nbtBlock != null) {
                                 builder = ((BlockNBTComponent.Builder)nbt(Component.blockNBT(), nbt, nbtInterpret, separator)).pos(nbtBlock);
                              } else if (nbtEntity != null) {
                                 builder = ((EntityNBTComponent.Builder)nbt(Component.entityNBT(), nbt, nbtInterpret, separator)).selector(nbtEntity);
                              } else {
                                 if (nbtStorage == null) {
                                    throw notSureHowToDeserialize(in.getPath());
                                 }

                                 builder = ((StorageNBTComponent.Builder)nbt(Component.storageNBT(), nbt, nbtInterpret, separator)).storage(nbtStorage);
                              }
                           }

                           ((ComponentBuilder)builder).style((Style)this.gson.fromJson((JsonElement)style, (Class)SerializerFactory.STYLE_TYPE)).append((Iterable)extra);
                           in.endObject();
                           return ((ComponentBuilder)builder).build();
                        }
                     }
                  }
               }
            }
         }
      } else {
         return Component.text(readString(in));
      }
   }

   private static String readString(JsonReader in) throws IOException {
      JsonToken peek = in.peek();
      if (peek != JsonToken.STRING && peek != JsonToken.NUMBER) {
         if (peek == JsonToken.BOOLEAN) {
            return String.valueOf(in.nextBoolean());
         } else {
            throw new JsonParseException("Token of type " + peek + " cannot be interpreted as a string");
         }
      } else {
         return in.nextString();
      }
   }

   private static <C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> B nbt(B builder, String nbt, boolean interpret, @Nullable Component separator) {
      return builder.nbtPath(nbt).interpret(interpret).separator(separator);
   }

   public void write(JsonWriter out, Component value) throws IOException {
      out.beginObject();
      if (value.hasStyling()) {
         JsonElement style = this.gson.toJsonTree(value.style(), SerializerFactory.STYLE_TYPE);
         if (style.isJsonObject()) {
            Iterator var4 = style.getAsJsonObject().entrySet().iterator();

            while(var4.hasNext()) {
               Entry<String, JsonElement> entry = (Entry)var4.next();
               out.name((String)entry.getKey());
               this.gson.toJson((JsonElement)entry.getValue(), out);
            }
         }
      }

      if (!value.children().isEmpty()) {
         out.name("extra");
         this.gson.toJson(value.children(), COMPONENT_LIST_TYPE, (JsonWriter)out);
      }

      if (value instanceof TextComponent) {
         out.name("text");
         out.value(((TextComponent)value).content());
      } else if (value instanceof TranslatableComponent) {
         TranslatableComponent translatable = (TranslatableComponent)value;
         out.name("translate");
         out.value(translatable.key());
         String fallback = translatable.fallback();
         if (fallback != null) {
            out.name("fallback");
            out.value(fallback);
         }

         if (!translatable.args().isEmpty()) {
            out.name("with");
            this.gson.toJson(translatable.args(), COMPONENT_LIST_TYPE, (JsonWriter)out);
         }
      } else if (value instanceof ScoreComponent) {
         ScoreComponent score = (ScoreComponent)value;
         out.name("score");
         out.beginObject();
         out.name("name");
         out.value(score.name());
         out.name("objective");
         out.value(score.objective());
         if (score.value() != null) {
            out.name("value");
            out.value(score.value());
         }

         out.endObject();
      } else if (value instanceof SelectorComponent) {
         SelectorComponent selector = (SelectorComponent)value;
         out.name("selector");
         out.value(selector.pattern());
         this.serializeSeparator(out, selector.separator());
      } else if (value instanceof KeybindComponent) {
         out.name("keybind");
         out.value(((KeybindComponent)value).keybind());
      } else {
         if (!(value instanceof NBTComponent)) {
            throw notSureHowToSerialize(value);
         }

         NBTComponent<?, ?> nbt = (NBTComponent)value;
         out.name("nbt");
         out.value(nbt.nbtPath());
         out.name("interpret");
         out.value(nbt.interpret());
         this.serializeSeparator(out, nbt.separator());
         if (value instanceof BlockNBTComponent) {
            out.name("block");
            this.gson.toJson(((BlockNBTComponent)value).pos(), SerializerFactory.BLOCK_NBT_POS_TYPE, (JsonWriter)out);
         } else if (value instanceof EntityNBTComponent) {
            out.name("entity");
            out.value(((EntityNBTComponent)value).selector());
         } else {
            if (!(value instanceof StorageNBTComponent)) {
               throw notSureHowToSerialize(value);
            }

            out.name("storage");
            this.gson.toJson(((StorageNBTComponent)value).storage(), SerializerFactory.KEY_TYPE, (JsonWriter)out);
         }
      }

      out.endObject();
   }

   private void serializeSeparator(JsonWriter out, @Nullable Component separator) throws IOException {
      if (separator != null) {
         out.name("separator");
         this.write(out, separator);
      }

   }

   static JsonParseException notSureHowToDeserialize(Object element) {
      return new JsonParseException("Don't know how to turn " + element + " into a Component");
   }

   private static IllegalArgumentException notSureHowToSerialize(Component component) {
      return new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
   }
}
