package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

public class NBTFloat extends NBTNumber {
   protected final float value;

   public NBTFloat(float value) {
      this.value = value;
   }

   public NBTType<NBTFloat> getType() {
      return NBTType.FLOAT;
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
      return this.value;
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
         NBTFloat other = (NBTFloat)obj;
         return Float.floatToIntBits(this.value) == Float.floatToIntBits(other.value);
      }
   }

   public int hashCode() {
      return Float.hashCode(this.value);
   }

   public NBTFloat copy() {
      return this;
   }
}
