package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

public class NBTLong extends NBTNumber {
   protected final long value;

   public NBTLong(long value) {
      this.value = value;
   }

   public NBTType<NBTLong> getType() {
      return NBTType.LONG;
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
      return this.value;
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
         NBTLong other = (NBTLong)obj;
         return this.value == other.value;
      }
   }

   public int hashCode() {
      return Long.hashCode(this.value);
   }

   public NBTLong copy() {
      return this;
   }
}
