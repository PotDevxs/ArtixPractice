package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.type;

import java.util.Objects;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.MaterialType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

public class StateType {
   private final String name;
   private final float blastResistance;
   private final float hardness;
   private final boolean isSolid;
   private final boolean isBlocking;
   private final boolean isAir;
   private final boolean requiresCorrectTool;
   private final boolean exceedsCube;
   private final MaterialType materialType;

   public StateType(String name, float blastResistance, float hardness, boolean isSolid, boolean isBlocking, boolean isAir, boolean requiresCorrectTool, boolean isShapeExceedsCube, MaterialType materialType) {
      this.name = name;
      this.blastResistance = blastResistance;
      this.hardness = hardness;
      this.isSolid = isSolid;
      this.isBlocking = isBlocking;
      this.isAir = isAir;
      this.requiresCorrectTool = requiresCorrectTool;
      this.exceedsCube = isShapeExceedsCube;
      this.materialType = materialType;
   }

   public WrappedBlockState createBlockState() {
      return WrappedBlockState.getDefaultState(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), this);
   }

   public WrappedBlockState createBlockState(ClientVersion version) {
      return WrappedBlockState.getDefaultState(version, this);
   }

   public String getName() {
      return this.name;
   }

   public float getBlastResistance() {
      return this.blastResistance;
   }

   public float getHardness() {
      return this.hardness;
   }

   public boolean isSolid() {
      return this.isSolid;
   }

   public boolean isBlocking() {
      return this.isBlocking;
   }

   public boolean isAir() {
      return this.isAir;
   }

   public boolean isRequiresCorrectTool() {
      return this.requiresCorrectTool;
   }

   public boolean isReplaceable() {
      switch(this.getMaterialType()) {
      case AIR:
      case STRUCTURAL_AIR:
      case REPLACEABLE_PLANT:
      case REPLACEABLE_FIREPROOF_PLANT:
      case REPLACEABLE_WATER_PLANT:
      case WATER:
      case BUBBLE_COLUMN:
      case LAVA:
      case TOP_SNOW:
      case FIRE:
         return true;
      default:
         return false;
      }
   }

   public boolean exceedsCube() {
      return this.exceedsCube;
   }

   public MaterialType getMaterialType() {
      return this.materialType;
   }

   public String toString() {
      return this.name;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         StateType stateType = (StateType)o;
         return Float.compare(this.blastResistance, stateType.blastResistance) == 0 && Float.compare(this.hardness, stateType.hardness) == 0 && this.isSolid == stateType.isSolid && this.isBlocking == stateType.isBlocking && this.isAir == stateType.isAir && this.requiresCorrectTool == stateType.requiresCorrectTool && this.exceedsCube == stateType.exceedsCube && Objects.equals(this.name, stateType.name) && this.materialType == stateType.materialType;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.name, this.blastResistance, this.hardness, this.isSolid, this.isBlocking, this.isAir, this.requiresCorrectTool, this.exceedsCube, this.materialType});
   }
}
