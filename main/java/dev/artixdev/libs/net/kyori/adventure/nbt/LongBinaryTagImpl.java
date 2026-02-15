package dev.artixdev.libs.net.kyori.adventure.nbt;

import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.Debug;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@Debug.Renderer(
   text = "String.valueOf(this.value) + \"l\"",
   hasChildren = "false"
)
final class LongBinaryTagImpl extends AbstractBinaryTag implements LongBinaryTag {
   private final long value;

   LongBinaryTagImpl(long value) {
      this.value = value;
   }

   public long value() {
      return this.value;
   }

   public byte byteValue() {
      return (byte)((int)(this.value & 255L));
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public float floatValue() {
      return (float)this.value;
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return this.value;
   }

   public short shortValue() {
      return (short)((int)(this.value & 65535L));
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         LongBinaryTagImpl that = (LongBinaryTagImpl)other;
         return this.value == that.value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Long.hashCode(this.value);
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("value", this.value));
   }
}
