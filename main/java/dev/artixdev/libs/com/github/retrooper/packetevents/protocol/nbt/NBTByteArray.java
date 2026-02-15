package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

import java.util.Arrays;

public class NBTByteArray extends NBT {
   protected final byte[] array;

   public NBTByteArray(byte[] array) {
      this.array = array;
   }

   public NBTType<NBTByteArray> getType() {
      return NBTType.BYTE_ARRAY;
   }

   public byte[] getValue() {
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
         NBTByteArray other = (NBTByteArray)obj;
         return Arrays.equals(this.array, other.array);
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.array);
   }

   public NBTByteArray copy() {
      byte[] abyte = new byte[this.array.length];
      System.arraycopy(this.array, 0, abyte, 0, this.array.length);
      return new NBTByteArray(abyte);
   }
}
