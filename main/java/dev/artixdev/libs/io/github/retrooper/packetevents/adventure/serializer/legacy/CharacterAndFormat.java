package dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.legacy;

import java.util.List;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextFormat;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface CharacterAndFormat extends Examinable {
   CharacterAndFormat BLACK = characterAndFormat('0', NamedTextColor.BLACK);
   CharacterAndFormat DARK_BLUE = characterAndFormat('1', NamedTextColor.DARK_BLUE);
   CharacterAndFormat DARK_GREEN = characterAndFormat('2', NamedTextColor.DARK_GREEN);
   CharacterAndFormat DARK_AQUA = characterAndFormat('3', NamedTextColor.DARK_AQUA);
   CharacterAndFormat DARK_RED = characterAndFormat('4', NamedTextColor.DARK_RED);
   CharacterAndFormat DARK_PURPLE = characterAndFormat('5', NamedTextColor.DARK_PURPLE);
   CharacterAndFormat GOLD = characterAndFormat('6', NamedTextColor.GOLD);
   CharacterAndFormat GRAY = characterAndFormat('7', NamedTextColor.GRAY);
   CharacterAndFormat DARK_GRAY = characterAndFormat('8', NamedTextColor.DARK_GRAY);
   CharacterAndFormat BLUE = characterAndFormat('9', NamedTextColor.BLUE);
   CharacterAndFormat GREEN = characterAndFormat('a', NamedTextColor.GREEN);
   CharacterAndFormat AQUA = characterAndFormat('b', NamedTextColor.AQUA);
   CharacterAndFormat RED = characterAndFormat('c', NamedTextColor.RED);
   CharacterAndFormat LIGHT_PURPLE = characterAndFormat('d', NamedTextColor.LIGHT_PURPLE);
   CharacterAndFormat YELLOW = characterAndFormat('e', NamedTextColor.YELLOW);
   CharacterAndFormat WHITE = characterAndFormat('f', NamedTextColor.WHITE);
   CharacterAndFormat OBFUSCATED = characterAndFormat('k', TextDecoration.OBFUSCATED);
   CharacterAndFormat BOLD = characterAndFormat('l', TextDecoration.BOLD);
   CharacterAndFormat STRIKETHROUGH = characterAndFormat('m', TextDecoration.STRIKETHROUGH);
   CharacterAndFormat UNDERLINED = characterAndFormat('n', TextDecoration.UNDERLINED);
   CharacterAndFormat ITALIC = characterAndFormat('o', TextDecoration.ITALIC);
   CharacterAndFormat RESET = characterAndFormat('r', Reset.INSTANCE);

   @NotNull
   static CharacterAndFormat characterAndFormat(char character, @NotNull TextFormat format) {
      return new CharacterAndFormatImpl(character, format);
   }

   @NotNull
   @Unmodifiable
   static List<CharacterAndFormat> defaults() {
      return CharacterAndFormatImpl.Defaults.DEFAULTS;
   }

   char character();

   @NotNull
   TextFormat format();

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("character", this.character()), ExaminableProperty.of("format", (Object)this.format()));
   }
}
