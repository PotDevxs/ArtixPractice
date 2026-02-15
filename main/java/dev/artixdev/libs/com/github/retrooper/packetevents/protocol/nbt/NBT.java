package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

public abstract class NBT {
   public abstract NBTType<?> getType();

   public abstract boolean equals(Object var1);

   public abstract int hashCode();

   public String toString() {
      return "nbt";
   }

   public abstract NBT copy();
}
