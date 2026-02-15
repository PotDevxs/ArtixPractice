package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data;

import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.ItemStack;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleItemStackData extends ParticleData implements LegacyConvertible {
   private ItemStack itemStack;

   public ParticleItemStackData(ItemStack itemStack) {
      this.itemStack = itemStack;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public void setItemStack(ItemStack itemStack) {
      this.itemStack = itemStack;
   }

   public static ParticleItemStackData read(PacketWrapper<?> wrapper) {
      return wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13) ? new ParticleItemStackData(wrapper.readItemStack()) : new ParticleItemStackData(ItemStack.builder().type(ItemTypes.getById(wrapper.getClientVersion(), wrapper.readVarInt())).build());
   }

   public static void write(PacketWrapper<?> wrapper, ParticleItemStackData data) {
      wrapper.writeItemStack(data.getItemStack());
   }

   public boolean isEmpty() {
      return false;
   }

   public LegacyParticleData toLegacy(ClientVersion version) {
      return LegacyParticleData.ofTwo(this.itemStack.getType().getId(version), this.itemStack.getLegacyData());
   }
}
