package dev.artixdev.libs.net.kyori.adventure.nbt;

final class EndBinaryTagImpl extends AbstractBinaryTag implements EndBinaryTag {
   static final EndBinaryTagImpl INSTANCE = new EndBinaryTagImpl();

   public boolean equals(Object that) {
      return this == that;
   }

   public int hashCode() {
      return 0;
   }
}
