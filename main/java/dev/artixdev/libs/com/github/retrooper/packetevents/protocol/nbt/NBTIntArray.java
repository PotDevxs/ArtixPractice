package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

import java.util.Arrays;

public class NBTIntArray extends NBT {
   protected final int[] array;

   public NBTIntArray(int[] array) {
      this.array = array;
   }

   public NBTType<NBTIntArray> getType() {
      return NBTType.INT_ARRAY;
   }

   public int[] getValue() {
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
         NBTIntArray other = (NBTIntArray)obj;
         return Arrays.equals(this.array, other.array);
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.array);
   }

   public NBTIntArray copy() {
      int[] aint = new int[this.array.length];
      System.arraycopy(this.array, 0, aint, 0, this.array.length);
      return new NBTIntArray(aint);
   }
}
