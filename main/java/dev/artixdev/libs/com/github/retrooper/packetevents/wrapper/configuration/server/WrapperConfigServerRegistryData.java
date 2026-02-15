package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.configuration.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigServerRegistryData extends PacketWrapper<WrapperConfigServerRegistryData> {
   private NBTCompound registryData;

   public WrapperConfigServerRegistryData(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerRegistryData(NBTCompound registryData) {
      super((PacketTypeCommon)PacketType.Configuration.Server.REGISTRY_DATA);
      this.registryData = registryData;
   }

   public void read() {
      this.registryData = this.readNBT();
   }

   public void write() {
      this.writeNBT(this.registryData);
   }

   public void copy(WrapperConfigServerRegistryData wrapper) {
      this.registryData = wrapper.registryData;
   }

   public NBTCompound getRegistryData() {
      return this.registryData;
   }

   public void setRegistryData(NBTCompound registryData) {
      this.registryData = registryData;
   }
}
