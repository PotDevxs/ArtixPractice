package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data;

import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleShriekData extends ParticleData {
   private int delay;

   public ParticleShriekData(int delay) {
      this.delay = delay;
   }

   public int getDelay() {
      return this.delay;
   }

   public void setDelay(int delay) {
      this.delay = delay;
   }

   public static ParticleShriekData read(PacketWrapper<?> wrapper) {
      return new ParticleShriekData(wrapper.readVarInt());
   }

   public static void write(PacketWrapper<?> wrapper, ParticleShriekData data) {
      wrapper.writeVarInt(data.getDelay());
   }

   public boolean isEmpty() {
      return false;
   }
}
