package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

public class NBTShort extends NBTNumber {
   protected final short value;

   public NBTShort(short value) {
      this.value = value;
   }

   public NBTType<NBTShort> getType() {
      return NBTType.SHORT;
   }

   public Number getAsNumber() {
      return this.value;
   }

   public byte getAsByte() {
      return (byte)this.value;
   }

   public short getAsShort() {
      return this.value;
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
         NBTShort other = (NBTShort)obj;
         return this.value == other.value;
      }
   }

   public int hashCode() {
      return Short.hashCode(this.value);
   }

   public NBTShort copy() {
      return this;
   }
}
