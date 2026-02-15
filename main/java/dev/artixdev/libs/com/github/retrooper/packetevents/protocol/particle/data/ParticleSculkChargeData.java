package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data;

import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleSculkChargeData extends ParticleData {
   private float roll;

   public ParticleSculkChargeData(float roll) {
      this.roll = roll;
   }

   public float getRoll() {
      return this.roll;
   }

   public void setRoll(float roll) {
      this.roll = roll;
   }

   public static ParticleSculkChargeData read(PacketWrapper<?> wrapper) {
      return new ParticleSculkChargeData(wrapper.readFloat());
   }

   public static void write(PacketWrapper<?> wrapper, ParticleSculkChargeData data) {
      wrapper.writeFloat(data.getRoll());
   }

   public boolean isEmpty() {
      return false;
   }
}
