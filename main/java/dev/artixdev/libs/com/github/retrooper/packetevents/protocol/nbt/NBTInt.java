package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

public class NBTInt extends NBTNumber {
   protected final int value;

   public NBTInt(int value) {
      this.value = value;
   }

   public NBTType<NBTInt> getType() {
      return NBTType.INT;
   }

   public Number getAsNumber() {
      return this.value;
   }

   public byte getAsByte() {
      return (byte)this.value;
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

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         NBTInt other = (NBTInt)obj;
         return this.value == other.value;
      }
   }

   public int hashCode() {
      return Integer.hashCode(this.value);
   }

   public NBTInt copy() {
      return this;
   }
}
