package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

import java.util.Arrays;

public class NBTLongArray extends NBT {
   protected final long[] array;

   public NBTLongArray(long[] array) {
      this.array = array;
   }

   public NBTType<NBTLongArray> getType() {
      return NBTType.LONG_ARRAY;
   }

   public long[] getValue() {
      return this.array;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         NBTLongArray other = (NBTLongArray)obj;
         return Arrays.equals(this.array, other.array);
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.array);
   }

   public NBTLongArray copy() {
      long[] along = new long[this.array.length];
      System.arraycopy(this.array, 0, along, 0, this.array.length);
      return new NBTLongArray(along);
   }
}
