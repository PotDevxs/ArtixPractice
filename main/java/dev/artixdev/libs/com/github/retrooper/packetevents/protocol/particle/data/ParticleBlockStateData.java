package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data;

import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleBlockStateData extends ParticleData implements LegacyConvertible {
   private WrappedBlockState blockState;

   public ParticleBlockStateData(WrappedBlockState blockState) {
      this.blockState = blockState;
   }

   public WrappedBlockState getBlockState() {
      return this.blockState;
   }

   public void setBlockState(WrappedBlockState blockState) {
      this.blockState = blockState;
   }

   public static ParticleBlockStateData read(PacketWrapper<?> wrapper) {
      int blockID;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
         blockID = wrapper.readVarInt();
      } else if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
         blockID = wrapper.readInt();
      } else {
         blockID = wrapper.readVarInt();
      }

      return new ParticleBlockStateData(WrappedBlockState.getByGlobalId(wrapper.getServerVersion().toClientVersion(), blockID));
   }

   public static void write(PacketWrapper<?> wrapper, ParticleBlockStateData data) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
         wrapper.writeVarInt(data.getBlockState().getGlobalId());
      } else {
         wrapper.writeInt(data.getBlockState().getGlobalId());
      }

   }

   public boolean isEmpty() {
      return false;
   }

   public LegacyParticleData toLegacy(ClientVersion version) {
      return LegacyParticleData.ofOne(this.blockState.getGlobalId());
   }
}
