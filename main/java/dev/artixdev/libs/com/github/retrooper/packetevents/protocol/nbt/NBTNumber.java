package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

public abstract class NBTNumber extends NBT {
   public abstract Number getAsNumber();

   public abstract byte getAsByte();

   public abstract short getAsShort();

   public abstract int getAsInt();

   public abstract long getAsLong();

   public abstract float getAsFloat();

   public abstract double getAsDouble();
}
