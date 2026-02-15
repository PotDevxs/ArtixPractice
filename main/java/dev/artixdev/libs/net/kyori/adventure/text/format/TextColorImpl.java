package dev.artixdev.libs.net.kyori.adventure.text.format;

import dev.artixdev.libs.net.kyori.adventure.util.HSVLike;
import dev.artixdev.libs.org.jetbrains.annotations.Debug;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@Debug.Renderer(
   text = "asHexString()"
)
final class TextColorImpl implements TextColor {
   private final int value;

   TextColorImpl(int value) {
      this.value = value;
   }

   public int value() {
      return this.value;
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof TextColorImpl)) {
         return false;
      } else {
         TextColorImpl that = (TextColorImpl)other;
         return this.value == that.value;
      }
   }

   public int hashCode() {
      return this.value;
   }

   public String toString() {
      return this.asHexString();
   }

   static float distance(@NotNull HSVLike self, @NotNull HSVLike other) {
      float hueDistance = 3.0F * Math.min(Math.abs(self.h() - other.h()), 1.0F - Math.abs(self.h() - other.h()));
      float saturationDiff = self.s() - other.s();
      float valueDiff = self.v() - other.v();
      return hueDistance * hueDistance + saturationDiff * saturationDiff + valueDiff * valueDiff;
   }
}
