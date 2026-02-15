package dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.stats.Statistics;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonPrimitive;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.nbt.BinaryTag;
import dev.artixdev.libs.net.kyori.adventure.nbt.CompoundBinaryTag;
import dev.artixdev.libs.net.kyori.adventure.nbt.TagStringIO;
import dev.artixdev.libs.net.kyori.adventure.nbt.api.BinaryTagHolder;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.TextComponent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.net.kyori.adventure.util.Codec;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.Range;

public class HoverSerializer {
   private static final TagStringIO SNBT_IO = TagStringIO.get();
   private static final Codec<CompoundBinaryTag, String, IOException, IOException> SNBT_CODEC;
   static final String ITEM_TYPE = "id";
   static final String ITEM_COUNT = "Count";
   static final String ITEM_TAG = "tag";
   static final String ENTITY_NAME = "name";
   static final String ENTITY_TYPE = "type";
   static final String ENTITY_ID = "id";
   static final Pattern LEGACY_NAME_PATTERN;

   public HoverEvent.ShowItem deserializeShowItem(HoverSerializer.GsonLike gson, JsonElement input, boolean legacy) throws IOException {
      if (legacy) {
         Component component = (Component)gson.fromJson(input, Component.class);
         assertTextComponent(component);
         CompoundBinaryTag contents = (CompoundBinaryTag)SNBT_CODEC.decode(((TextComponent)component).content());
         CompoundBinaryTag tag = contents.getCompound("tag");
         return createShowItem(Key.key(contents.getString("id")), contents.getByte("Count", (byte)1), tag == CompoundBinaryTag.empty() ? null : BinaryTagHolder.encode(tag, SNBT_CODEC));
      } else {
         return (HoverEvent.ShowItem)gson.fromJson(input, HoverEvent.ShowItem.class);
      }
   }

   public HoverEvent.ShowEntity deserializeShowEntity(HoverSerializer.GsonLike gson, JsonElement input, Codec.Decoder<Component, String, ? extends RuntimeException> componentCodec, boolean legacy) throws IOException {
      if (legacy) {
         Component component = (Component)gson.fromJson(input, Component.class);
         assertTextComponent(component);
         CompoundBinaryTag contents = (CompoundBinaryTag)SNBT_CODEC.decode(((TextComponent)component).content());
         String type = contents.getString("type");
         Matcher matcher = LEGACY_NAME_PATTERN.matcher(type);
         if (matcher.matches()) {
            StringJoiner joiner = new StringJoiner("_");
            joiner.add(matcher.group(1));
            if (matcher.group(2) != null) {
               joiner.add(matcher.group(2));
            }

            type = joiner.toString().toLowerCase(Locale.ROOT);
         }

         return createShowEntity(Key.key(type), UUID.fromString(contents.getString("id")), (Component)componentCodec.decode(contents.getString("name")));
      } else {
         return (HoverEvent.ShowEntity)gson.fromJson(input, HoverEvent.ShowEntity.class);
      }
   }

   public Component deserializeShowAchievement(JsonElement input) {
      assertStringValue(input);
      return Statistics.getById(input.getAsString()).display();
   }

   private static void assertStringValue(JsonElement element) {
      if (!element.isJsonPrimitive() || !((JsonPrimitive)element).isString()) {
         throw new IllegalArgumentException("Legacy events must be single Component instances");
      }
   }

   private static void assertTextComponent(Component component) {
      if (!(component instanceof TextComponent) || !component.children().isEmpty()) {
         throw new IllegalArgumentException("Legacy events must be single Component instances");
      }
   }

   @NotNull
   public Component serializeShowItem(@NotNull HoverEvent.ShowItem input) throws IOException {
      CompoundBinaryTag.Builder builder = (CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)CompoundBinaryTag.builder().putString("id", input.item().asString())).putByte("Count", (byte)input.count());
      BinaryTagHolder nbt = input.nbt();
      if (nbt != null) {
         builder.put("tag", (BinaryTag)nbt.get(SNBT_CODEC));
      }

      return Component.text((String)SNBT_CODEC.encode(builder.build()));
   }

   @NotNull
   public Component serializeShowEntity(@NotNull HoverEvent.ShowEntity input, Codec.Encoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
      CompoundBinaryTag.Builder builder = (CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)CompoundBinaryTag.builder().putString("id", input.id().toString())).putString("type", input.type().asString());
      Component name = input.name();
      if (name != null) {
         builder.putString("name", (String)componentCodec.encode(name));
      }

      return Component.text((String)SNBT_CODEC.encode(builder.build()));
   }

   private static HoverEvent.ShowItem createShowItem(@NotNull Key item, @Range(from = 0L,to = 2147483647L) int count, @Nullable BinaryTagHolder nbt) {
      try {
         return HoverEvent.ShowItem.showItem(item, count, nbt);
      } catch (NoSuchMethodError e) {
         return HoverEvent.ShowItem.of(item, count, nbt);
      }
   }

   private static HoverEvent.ShowEntity createShowEntity(@NotNull Key type, @NotNull UUID id, @Nullable Component name) {
      try {
         return HoverEvent.ShowEntity.showEntity(type, id, name);
      } catch (NoSuchMethodError e) {
         return HoverEvent.ShowEntity.of(type, id, name);
      }
   }

   @NotNull
   private static <D, E, DX extends Throwable, EX extends Throwable> Codec<D, E, DX, EX> createCodec(@NotNull Codec.Decoder<D, E, DX> decoder, @NotNull Codec.Encoder<D, E, EX> encoder) {
      try {
         return Codec.codec(decoder, encoder);
      } catch (NoSuchMethodError e) {
         return Codec.of(decoder, encoder);
      }
   }

   static {
      Codec.Decoder<CompoundBinaryTag, String, IOException> decoder = SNBT_IO::asCompound;
      Codec.Encoder<CompoundBinaryTag, String, IOException> encoder = SNBT_IO::asString;
      SNBT_CODEC = createCodec(decoder, encoder);
      LEGACY_NAME_PATTERN = Pattern.compile("([A-Z][a-z]+)([A-Z][a-z]+)?");
   }

   public interface GsonLike {
      static HoverSerializer.GsonLike fromGson(Gson gson) {
         Objects.requireNonNull(gson);
         return gson::fromJson;
      }

      <T> T fromJson(@Nullable JsonElement element, Class<T> type);
   }
}
