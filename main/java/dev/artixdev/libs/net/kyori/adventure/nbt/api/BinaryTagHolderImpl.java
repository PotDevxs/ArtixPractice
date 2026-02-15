package dev.artixdev.libs.net.kyori.adventure.nbt.api;

import java.util.Objects;
import dev.artixdev.libs.net.kyori.adventure.util.Codec;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

final class BinaryTagHolderImpl implements BinaryTagHolder {
   private final String string;

   BinaryTagHolderImpl(String string) {
      this.string = (String)Objects.requireNonNull(string, "string");
   }

   @NotNull
   public String string() {
      return this.string;
   }

   @NotNull
   public <T, DX extends Exception> T get(@NotNull Codec<T, String, DX, ?> codec) throws DX {
      return codec.decode(this.string);
   }

   public int hashCode() {
      return 31 * this.string.hashCode();
   }

   public boolean equals(Object that) {
      return !(that instanceof BinaryTagHolderImpl) ? false : this.string.equals(((BinaryTagHolderImpl)that).string);
   }

   public String toString() {
      return this.string;
   }
}
