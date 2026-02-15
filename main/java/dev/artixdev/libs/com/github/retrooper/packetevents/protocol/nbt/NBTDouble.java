package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

public class NBTDouble extends NBTNumber {
   protected final double value;

   public NBTDouble(double value) {
      this.value = value;
   }

   public NBTType<NBTDouble> getType() {
      return NBTType.DOUBLE;
   }

   public Number getAsNumber() {
      return this.value;
   }

   public byte getAsByte() {
      return (byte)((int)this.value);
   }

   public short getAsShort() {
      return (short)((int)this.value);
   }

   public int getAsInt() {
      return (int)this.value;
   }

   public long getAsLong() {
      return (long)this.value;
   }

   public float getAsFloat() {
      return (float)this.value;
   }

   public double getAsDouble() {
      return this.value;
   }

   public int hashCode() {
      return Double.hashCode(this.value);
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         NBTDouble other = (NBTDouble)obj;
         return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(other.value);
      }
   }

   public NBTDouble copy() {
      return this;
   }
}
