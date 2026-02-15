package dev.artixdev.libs.net.kyori.adventure.nbt;

import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.Debug;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@Debug.Renderer(
   text = "String.valueOf(this.value) + \"s\"",
   hasChildren = "false"
)
final class ShortBinaryTagImpl extends AbstractBinaryTag implements ShortBinaryTag {
   private final short value;

   ShortBinaryTagImpl(short value) {
      this.value = value;
   }

   public short value() {
      return this.value;
   }

   public byte byteValue() {
      return (byte)(this.value & 255);
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public float floatValue() {
      return (float)this.value;
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public short shortValue() {
      return this.value;
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         ShortBinaryTagImpl that = (ShortBinaryTagImpl)other;
         return this.value == that.value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Short.hashCode(this.value);
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("value", this.value));
   }
}
