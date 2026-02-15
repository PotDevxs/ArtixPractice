package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data;

import java.util.Optional;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3i;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class ParticleVibrationData extends ParticleData {
   private Vector3i startingPosition;
   private ParticleVibrationData.PositionType type;
   @Nullable
   private Vector3i blockPosition;
   @Nullable
   private Integer entityId;
   private int ticks;

   public ParticleVibrationData(Vector3i startingPosition, @Nullable Vector3i blockPosition, int ticks) {
      this.startingPosition = startingPosition;
      this.type = ParticleVibrationData.PositionType.BLOCK;
      this.blockPosition = blockPosition;
      this.entityId = null;
      this.ticks = ticks;
   }

   public ParticleVibrationData(Vector3i startingPosition, int entityId, int ticks) {
      this.startingPosition = startingPosition;
      this.type = ParticleVibrationData.PositionType.ENTITY;
      this.blockPosition = null;
      this.entityId = entityId;
      this.ticks = ticks;
   }

   public Vector3i getStartingPosition() {
      return this.startingPosition;
   }

   public void setStartingPosition(Vector3i startingPosition) {
      this.startingPosition = startingPosition;
   }

   public ParticleVibrationData.PositionType getType() {
      return this.type;
   }

   public Optional<Vector3i> getBlockPosition() {
      return Optional.ofNullable(this.blockPosition);
   }

   public void setBlockPosition(@Nullable Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }

   public Optional<Integer> getEntityId() {
      return Optional.ofNullable(this.entityId);
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public int getTicks() {
      return this.ticks;
   }

   public void setTicks(int ticks) {
      this.ticks = ticks;
   }

   public static ParticleVibrationData read(PacketWrapper<?> wrapper) {
      Vector3i startingPos = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4) ? Vector3i.zero() : wrapper.readBlockPosition();
      ParticleVibrationData.PositionType positionType;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         positionType = ParticleVibrationData.PositionType.getById(wrapper.readVarInt());
      } else {
         String positionTypeName = wrapper.readString();
         positionType = ParticleVibrationData.PositionType.getByName(positionTypeName);
         if (positionType == null) {
            throw new IllegalArgumentException("Unknown position type: " + positionTypeName);
         }
      }

      switch(positionType) {
      case BLOCK:
         return new ParticleVibrationData(startingPos, wrapper.readBlockPosition(), wrapper.readVarInt());
      case ENTITY:
         return new ParticleVibrationData(startingPos, wrapper.readVarInt(), wrapper.readVarInt());
      default:
         throw new IllegalArgumentException("Illegal position type: " + positionType);
      }
   }

   public static void write(PacketWrapper<?> wrapper, ParticleVibrationData data) {
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_19_4)) {
         wrapper.writeBlockPosition(data.getStartingPosition());
      }

      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         wrapper.writeVarInt(data.getType().getId(wrapper.getServerVersion().toClientVersion()));
      } else {
         wrapper.writeIdentifier(data.getType().getName());
      }

      if (data.getType() == ParticleVibrationData.PositionType.BLOCK) {
         wrapper.writeBlockPosition((Vector3i)data.getBlockPosition().get());
      } else if (data.getType() == ParticleVibrationData.PositionType.ENTITY) {
         wrapper.writeVarInt((Integer)data.getEntityId().get());
      }

      wrapper.writeVarInt(data.getTicks());
   }

   public boolean isEmpty() {
      return false;
   }

   public static enum PositionType implements MappedEntity {
      BLOCK(new ResourceLocation("minecraft:block")),
      ENTITY(new ResourceLocation("minecraft:entity"));

      private final ResourceLocation name;

      private PositionType(ResourceLocation name) {
         this.name = name;
      }

      public ResourceLocation getName() {
         return this.name;
      }

      public static ParticleVibrationData.PositionType getById(int id) {
         switch(id) {
         case 0:
            return BLOCK;
         case 1:
            return ENTITY;
         default:
            throw new IllegalArgumentException("Illegal position type id: " + id);
         }
      }

      @Nullable
      public static ParticleVibrationData.PositionType getByName(String name) {
         return getByName(new ResourceLocation(name));
      }

      @Nullable
      public static ParticleVibrationData.PositionType getByName(ResourceLocation name) {
         ParticleVibrationData.PositionType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ParticleVibrationData.PositionType type = var1[var3];
            if (type.getName().equals(name)) {
               return type;
            }
         }

         return null;
      }

      public int getId(ClientVersion version) {
         return this.ordinal();
      }

      // $FF: synthetic method
      private static ParticleVibrationData.PositionType[] $values() {
         return new ParticleVibrationData.PositionType[]{BLOCK, ENTITY};
      }
   }
}
