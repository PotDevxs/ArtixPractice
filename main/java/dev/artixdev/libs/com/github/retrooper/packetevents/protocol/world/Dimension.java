package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTString;

public class Dimension {
   private int id;
   private NBTCompound attributes;

   /** @deprecated */
   @Deprecated
   public Dimension(DimensionType type) {
      this.id = type.getId();
      this.attributes = new NBTCompound();
   }

   public Dimension(int id) {
      this.id = id;
      this.attributes = new NBTCompound();
   }

   public Dimension(NBTCompound attributes) {
      this.attributes = attributes;
   }

   public String getDimensionName() {
      return this.getAttributes().getStringTagValueOrDefault("effects", "");
   }

   public void setDimensionName(String name) {
      NBTCompound compound = this.getAttributes();
      compound.setTag("effects", new NBTString(name));
      this.setAttributes(compound);
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   /** @deprecated */
   @Deprecated
   public DimensionType getType() {
      return DimensionType.getById(this.id);
   }

   /** @deprecated */
   @Deprecated
   public void setType(DimensionType type) {
      this.id = type.getId();
   }

   public NBTCompound getAttributes() {
      return this.attributes;
   }

   public void setAttributes(NBTCompound attributes) {
      this.attributes = attributes;
   }
}
