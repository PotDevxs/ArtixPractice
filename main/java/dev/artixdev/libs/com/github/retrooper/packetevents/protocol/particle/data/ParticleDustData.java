package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data;

import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3f;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleDustData extends ParticleData {
   private float scale;
   private float red;
   private float green;
   private float blue;

   public ParticleDustData(float scale, float red, float green, float blue) {
      this.scale = scale;
      this.red = red;
      this.green = green;
      this.blue = blue;
   }

   public ParticleDustData(float scale, Vector3f rgb) {
      this(scale, rgb.getX(), rgb.getY(), rgb.getZ());
   }

   public float getRed() {
      return this.red;
   }

   public void setRed(float red) {
      this.red = red;
   }

   public float getGreen() {
      return this.green;
   }

   public void setGreen(float green) {
      this.green = green;
   }

   public float getBlue() {
      return this.blue;
   }

   public void setBlue(float blue) {
      this.blue = blue;
   }

   public float getScale() {
      return this.scale;
   }

   public void setScale(float scale) {
      this.scale = scale;
   }

   public static ParticleDustData read(PacketWrapper<?> wrapper) {
      float red = wrapper.readFloat();
      float green = wrapper.readFloat();
      float blue = wrapper.readFloat();
      float scale = wrapper.readFloat();
      return new ParticleDustData(scale, red, green, blue);
   }

   public static void write(PacketWrapper<?> wrapper, ParticleDustData data) {
      wrapper.writeFloat(data.getRed());
      wrapper.writeFloat(data.getGreen());
      wrapper.writeFloat(data.getBlue());
      wrapper.writeFloat(data.getScale());
   }

   public boolean isEmpty() {
      return false;
   }
}
