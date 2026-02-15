package dev.artixdev.libs.net.kyori.adventure.nbt.api;

import dev.artixdev.libs.net.kyori.adventure.util.Codec;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface BinaryTagHolder {
   @NotNull
   static <T, EX extends Exception> BinaryTagHolder encode(@NotNull T nbt, @NotNull Codec<? super T, String, ?, EX> codec) throws EX {
      return new BinaryTagHolderImpl((String)codec.encode(nbt));
   }

   @NotNull
   static BinaryTagHolder binaryTagHolder(@NotNull String string) {
      return new BinaryTagHolderImpl(string);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static BinaryTagHolder of(@NotNull String string) {
      return new BinaryTagHolderImpl(string);
   }

   @NotNull
   String string();

   @NotNull
   <T, DX extends Exception> T get(@NotNull Codec<T, String, DX, ?> var1) throws DX;
}
