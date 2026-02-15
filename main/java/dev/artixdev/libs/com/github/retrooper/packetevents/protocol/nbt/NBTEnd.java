package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

public class NBTEnd extends NBT {
   public static final NBTEnd INSTANCE = new NBTEnd();

   public NBTType<NBTEnd> getType() {
      return NBTType.END;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else {
         return this.getClass() == obj.getClass();
      }
   }

   public int hashCode() {
      return 0;
   }

   public NBTEnd copy() {
      return this;
   }
}
