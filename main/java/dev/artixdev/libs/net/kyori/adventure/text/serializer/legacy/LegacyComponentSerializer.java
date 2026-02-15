package dev.artixdev.libs.net.kyori.adventure.text.serializer.legacy;

import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.TextComponent;
import dev.artixdev.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import dev.artixdev.libs.net.kyori.adventure.util.Buildable;
import dev.artixdev.libs.net.kyori.adventure.util.PlatformAPI;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface LegacyComponentSerializer extends ComponentSerializer<Component, TextComponent, String>, Buildable<LegacyComponentSerializer, LegacyComponentSerializer.Builder> {
   char SECTION_CHAR = '§';
   char AMPERSAND_CHAR = '&';
   char HEX_CHAR = '#';

   @NotNull
   static LegacyComponentSerializer legacySection() {
      return LegacyComponentSerializerImpl.Instances.SECTION;
   }

   @NotNull
   static LegacyComponentSerializer legacyAmpersand() {
      return LegacyComponentSerializerImpl.Instances.AMPERSAND;
   }

   @NotNull
   static LegacyComponentSerializer legacy(char legacyCharacter) {
      if (legacyCharacter == 167) {
         return legacySection();
      } else {
         return legacyCharacter == '&' ? legacyAmpersand() : builder().character(legacyCharacter).build();
      }
   }

   @Nullable
   static LegacyFormat parseChar(char character) {
      return LegacyComponentSerializerImpl.legacyFormat(character);
   }

   @NotNull
   static LegacyComponentSerializer.Builder builder() {
      return new LegacyComponentSerializerImpl.BuilderImpl();
   }

   @NotNull
   TextComponent deserialize(@NotNull String var1);

   @NotNull
   String serialize(@NotNull Component var1);

   @PlatformAPI
   @ApiStatus.Internal
   public interface Provider {
      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      LegacyComponentSerializer legacyAmpersand();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      LegacyComponentSerializer legacySection();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      Consumer<LegacyComponentSerializer.Builder> legacy();
   }

   public interface Builder extends AbstractBuilder<LegacyComponentSerializer>, Buildable.Builder<LegacyComponentSerializer> {
      @NotNull
      LegacyComponentSerializer.Builder character(char var1);

      @NotNull
      LegacyComponentSerializer.Builder hexCharacter(char var1);

      @NotNull
      LegacyComponentSerializer.Builder extractUrls();

      @NotNull
      LegacyComponentSerializer.Builder extractUrls(@NotNull Pattern var1);

      @NotNull
      LegacyComponentSerializer.Builder extractUrls(@Nullable Style var1);

      @NotNull
      LegacyComponentSerializer.Builder extractUrls(@NotNull Pattern var1, @Nullable Style var2);

      @NotNull
      LegacyComponentSerializer.Builder hexColors();

      @NotNull
      LegacyComponentSerializer.Builder useUnusualXRepeatedCharacterHexFormat();

      @NotNull
      LegacyComponentSerializer.Builder flattener(@NotNull ComponentFlattener var1);

      @NotNull
      LegacyComponentSerializer.Builder formats(@NotNull List<CharacterAndFormat> var1);

      @NotNull
      LegacyComponentSerializer build();
   }
}
