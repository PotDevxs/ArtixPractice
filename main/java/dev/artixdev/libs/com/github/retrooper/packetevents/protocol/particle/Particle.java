package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.type.ParticleType;

public class Particle {
   private ParticleType type;
   private ParticleData data;

   public Particle(ParticleType type, ParticleData data) {
      this.type = type;
      this.data = data;
   }

   public Particle(ParticleType type) {
      this(type, new ParticleData());
   }

   public ParticleType getType() {
      return this.type;
   }

   public void setType(ParticleType type) {
      this.type = type;
   }

   public ParticleData getData() {
      return this.data;
   }

   public void setData(ParticleData data) {
      this.data = data;
   }
}
