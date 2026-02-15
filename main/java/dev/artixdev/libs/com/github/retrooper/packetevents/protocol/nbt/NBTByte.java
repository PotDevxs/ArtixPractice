package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

public class NBTByte extends NBTNumber {
   protected final byte value;

   public NBTByte(byte value) {
      this.value = value;
   }

   public NBTByte(boolean value) {
      this((byte)(value ? 1 : 0));
   }

   public NBTType<NBTByte> getType() {
      return NBTType.BYTE;
   }

   public Number getAsNumber() {
      return this.value;
   }

   public byte getAsByte() {
      return this.value;
   }

   public short getAsShort() {
      return (short)this.value;
   }

   public int getAsInt() {
      return this.value;
   }

   public long getAsLong() {
      return (long)this.value;
   }

   public float getAsFloat() {
      return (float)this.value;
   }

   public double getAsDouble() {
      return (double)this.value;
   }

   public int hashCode() {
      return Byte.hashCode(this.value);
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         NBTByte other = (NBTByte)obj;
         return this.value == other.value;
      }
   }

   public NBTByte copy() {
      return this;
   }
}
