package dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.legacy;

import java.util.Objects;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public final class LegacyFormat implements Examinable {
   static final LegacyFormat RESET = new LegacyFormat(true);
   @Nullable
   private final NamedTextColor color;
   @Nullable
   private final TextDecoration decoration;
   private final boolean reset;

   LegacyFormat(@Nullable NamedTextColor color) {
      this.color = color;
      this.decoration = null;
      this.reset = false;
   }

   LegacyFormat(@Nullable TextDecoration decoration) {
      this.color = null;
      this.decoration = decoration;
      this.reset = false;
   }

   private LegacyFormat(boolean reset) {
      this.color = null;
      this.decoration = null;
      this.reset = reset;
   }

   @Nullable
   public TextColor color() {
      return this.color;
   }

   @Nullable
   public TextDecoration decoration() {
      return this.decoration;
   }

   public boolean reset() {
      return this.reset;
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         LegacyFormat that = (LegacyFormat)other;
         return this.color == that.color && this.decoration == that.decoration && this.reset == that.reset;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = Objects.hashCode(this.color);
      result = 31 * result + Objects.hashCode(this.decoration);
      result = 31 * result + Boolean.hashCode(this.reset);
      return result;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("color", (Object)this.color), ExaminableProperty.of("decoration", (Object)this.decoration), ExaminableProperty.of("reset", this.reset));
   }
}
