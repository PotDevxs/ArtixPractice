package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.GameMode;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.Difficulty;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.Dimension;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.DimensionType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.WorldType;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class WrapperPlayServerJoinGame extends PacketWrapper<WrapperPlayServerJoinGame> {
   private int entityID;
   private boolean hardcore;
   private GameMode gameMode;
   @Nullable
   private GameMode previousGameMode;
   private List<String> worldNames;
   private NBTCompound dimensionCodec;
   private Dimension dimension;
   private Difficulty difficulty;
   private String worldName;
   private long hashedSeed;
   private int maxPlayers;
   private int viewDistance;
   private int simulationDistance;
   private boolean reducedDebugInfo;
   private boolean enableRespawnScreen;
   private boolean limitedCrafting;
   private boolean isDebug;
   private boolean isFlat;
   private WorldBlockPosition lastDeathPosition;
   private Integer portalCooldown;

   public WrapperPlayServerJoinGame(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerJoinGame(int entityID, boolean hardcore, GameMode gameMode, @Nullable GameMode previousGameMode, List<String> worldNames, NBTCompound dimensionCodec, Dimension dimension, Difficulty difficulty, String worldName, long hashedSeed, int maxPlayers, int viewDistance, int simulationDistance, boolean reducedDebugInfo, boolean enableRespawnScreen, boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
      this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, dimension, difficulty, worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo, enableRespawnScreen, false, isDebug, isFlat, lastDeathPosition, portalCooldown);
   }

   public WrapperPlayServerJoinGame(int entityID, boolean hardcore, GameMode gameMode, @Nullable GameMode previousGameMode, List<String> worldNames, NBTCompound dimensionCodec, Dimension dimension, Difficulty difficulty, String worldName, long hashedSeed, int maxPlayers, int viewDistance, int simulationDistance, boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting, boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
      super((PacketTypeCommon)PacketType.Play.Server.JOIN_GAME);
      this.entityID = entityID;
      this.hardcore = hardcore;
      this.gameMode = gameMode;
      this.previousGameMode = previousGameMode;
      this.worldNames = worldNames;
      this.dimensionCodec = dimensionCodec;
      this.dimension = dimension;
      this.difficulty = difficulty;
      this.worldName = worldName;
      this.hashedSeed = hashedSeed;
      this.maxPlayers = maxPlayers;
      this.viewDistance = viewDistance;
      this.simulationDistance = simulationDistance;
      this.reducedDebugInfo = reducedDebugInfo;
      this.enableRespawnScreen = enableRespawnScreen;
      this.limitedCrafting = limitedCrafting;
      this.isDebug = isDebug;
      this.isFlat = isFlat;
      this.lastDeathPosition = lastDeathPosition;
      this.portalCooldown = portalCooldown;
   }

   public void read() {
      this.entityID = this.readInt();
      boolean v1_20_2 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2);
      boolean v1_19 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
      boolean v1_18 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18);
      boolean v1_16 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
      boolean v1_15 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
      boolean v1_14 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
      if (v1_16) {
         this.hardcore = this.readBoolean();
         if (!v1_20_2) {
            this.gameMode = this.readGameMode();
         }
      } else {
         int gameModeId = this.readUnsignedByte();
         this.hardcore = (gameModeId & 8) == 8;
         this.gameMode = GameMode.getById(gameModeId & -9);
      }

      if (v1_16) {
         if (!v1_20_2) {
            this.previousGameMode = this.readGameMode();
         }

         int worldCount = this.readVarInt();
         this.worldNames = new ArrayList(worldCount);

         for(int i = 0; i < worldCount; ++i) {
            this.worldNames.add(this.readString());
         }

         if (!v1_20_2) {
            this.dimensionCodec = this.readNBT();
            this.dimension = this.readDimension();
            this.worldName = this.readString();
         }
      } else {
         this.previousGameMode = this.gameMode;
         this.dimensionCodec = new NBTCompound();
         this.dimension = new Dimension(this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9_2) ? this.readInt() : this.readByte());
         if (!v1_14) {
            this.difficulty = Difficulty.getById(this.readByte());
         }
      }

      if (v1_15 && !v1_20_2) {
         this.hashedSeed = this.readLong();
      }

      if (v1_16) {
         this.maxPlayers = this.readVarInt();
         this.viewDistance = this.readVarInt();
         if (v1_18) {
            this.simulationDistance = this.readVarInt();
         }

         this.reducedDebugInfo = this.readBoolean();
         this.enableRespawnScreen = this.readBoolean();
         if (v1_20_2) {
            this.limitedCrafting = this.readBoolean();
            this.dimension = this.readDimension();
            this.worldName = this.readString();
            this.hashedSeed = this.readLong();
            this.gameMode = this.readGameMode();
            this.previousGameMode = this.readGameMode();
         }

         this.isDebug = this.readBoolean();
         this.isFlat = this.readBoolean();
      } else {
         this.maxPlayers = this.readUnsignedByte();
         String levelType = this.readString(16);
         this.isFlat = DimensionType.isFlat(levelType);
         this.isDebug = DimensionType.isDebug(levelType);
         if (v1_14) {
            this.viewDistance = this.readVarInt();
         }

         this.reducedDebugInfo = this.readBoolean();
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
            this.enableRespawnScreen = this.readBoolean();
         }
      }

      if (v1_19) {
         this.lastDeathPosition = (WorldBlockPosition)this.readOptional(PacketWrapper::readWorldBlockPosition);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
         this.portalCooldown = this.readVarInt();
      }

   }

   public void write() {
      this.writeInt(this.entityID);
      boolean v1_20_2 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2);
      boolean v1_19 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
      boolean v1_18 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18);
      boolean v1_16 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
      boolean v1_14 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
      boolean v1_15 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
      int pCooldown;
      if (v1_16) {
         this.writeBoolean(this.hardcore);
         if (!v1_20_2) {
            this.writeGameMode(this.gameMode);
         }
      } else {
         pCooldown = this.gameMode.getId();
         if (this.hardcore) {
            pCooldown |= 8;
         }

         this.writeByte(pCooldown);
      }

      if (v1_16) {
         if (this.previousGameMode == null) {
            this.previousGameMode = this.gameMode;
         }

         if (!v1_20_2) {
            this.writeGameMode(this.previousGameMode);
         }

         this.writeVarInt(this.worldNames.size());
         for (String name : this.worldNames) {
            this.writeString(name);
         }

         if (!v1_20_2) {
            this.writeNBT(this.dimensionCodec);
            this.writeDimension(this.dimension);
            this.writeString(this.worldName);
         }
      } else {
         this.previousGameMode = this.gameMode;
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.writeInt(this.dimension.getId());
         } else {
            this.writeByte(this.dimension.getId());
         }

         if (!v1_14) {
            this.writeByte(this.difficulty.getId());
         }
      }

      if (v1_15 && !v1_20_2) {
         this.writeLong(this.hashedSeed);
      }

      if (v1_16) {
         this.writeVarInt(this.maxPlayers);
         this.writeVarInt(this.viewDistance);
         if (v1_18) {
            this.writeVarInt(this.simulationDistance);
         }

         this.writeBoolean(this.reducedDebugInfo);
         this.writeBoolean(this.enableRespawnScreen);
         if (v1_20_2) {
            this.writeBoolean(this.limitedCrafting);
            this.writeDimension(this.dimension);
            this.writeString(this.worldName);
            this.writeLong(this.hashedSeed);
            this.writeGameMode(this.gameMode);
            this.writeGameMode(this.previousGameMode);
         }

         this.writeBoolean(this.isDebug);
         this.writeBoolean(this.isFlat);
      } else {
         this.writeByte(this.maxPlayers);
         String levelType;
         if (this.isFlat) {
            levelType = WorldType.FLAT.getName();
         } else if (this.isDebug) {
            levelType = WorldType.DEBUG_ALL_BLOCK_STATES.getName();
         } else {
            levelType = WorldType.DEFAULT.getName();
         }

         this.writeString(levelType, 16);
         if (v1_14) {
            this.writeVarInt(this.viewDistance);
         }

         this.writeBoolean(this.reducedDebugInfo);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
            this.writeBoolean(this.enableRespawnScreen);
         }
      }

      if (v1_19) {
         this.writeOptional(this.lastDeathPosition, PacketWrapper::writeWorldBlockPosition);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
         pCooldown = this.portalCooldown != null ? this.portalCooldown : 0;
         this.writeVarInt(pCooldown);
      }

   }

   public void copy(WrapperPlayServerJoinGame wrapper) {
      this.entityID = wrapper.entityID;
      this.hardcore = wrapper.hardcore;
      this.gameMode = wrapper.gameMode;
      this.previousGameMode = wrapper.previousGameMode;
      this.worldNames = wrapper.worldNames;
      this.dimensionCodec = wrapper.dimensionCodec;
      this.dimension = wrapper.dimension;
      this.difficulty = wrapper.difficulty;
      this.worldName = wrapper.worldName;
      this.hashedSeed = wrapper.hashedSeed;
      this.maxPlayers = wrapper.maxPlayers;
      this.viewDistance = wrapper.viewDistance;
      this.simulationDistance = wrapper.simulationDistance;
      this.reducedDebugInfo = wrapper.reducedDebugInfo;
      this.enableRespawnScreen = wrapper.enableRespawnScreen;
      this.limitedCrafting = wrapper.limitedCrafting;
      this.isDebug = wrapper.isDebug;
      this.isFlat = wrapper.isFlat;
      this.lastDeathPosition = wrapper.lastDeathPosition;
      this.portalCooldown = wrapper.portalCooldown;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public boolean isHardcore() {
      return this.hardcore;
   }

   public void setHardcore(boolean hardcore) {
      this.hardcore = hardcore;
   }

   public GameMode getGameMode() {
      return this.gameMode;
   }

   public void setGameMode(GameMode gameMode) {
      this.gameMode = gameMode;
   }

   @Nullable
   public GameMode getPreviousGameMode() {
      return this.previousGameMode;
   }

   public void setPreviousGameMode(@Nullable GameMode previousGameMode) {
      this.previousGameMode = previousGameMode;
   }

   public List<String> getWorldNames() {
      return this.worldNames;
   }

   public void setWorldNames(List<String> worldNames) {
      this.worldNames = worldNames;
   }

   public NBTCompound getDimensionCodec() {
      return this.dimensionCodec;
   }

   public void setDimensionCodec(NBTCompound dimensionCodec) {
      this.dimensionCodec = dimensionCodec;
   }

   public Dimension getDimension() {
      return this.dimension;
   }

   public void setDimension(Dimension dimension) {
      this.dimension = dimension;
   }

   public Difficulty getDifficulty() {
      return this.difficulty;
   }

   public void setDifficulty(Difficulty difficulty) {
      this.difficulty = difficulty;
   }

   public String getWorldName() {
      return this.worldName;
   }

   public void setWorldName(String worldName) {
      this.worldName = worldName;
   }

   public long getHashedSeed() {
      return this.hashedSeed;
   }

   public void setHashedSeed(long hashedSeed) {
      this.hashedSeed = hashedSeed;
   }

   public int getMaxPlayers() {
      return this.maxPlayers;
   }

   public void setMaxPlayers(int maxPlayers) {
      this.maxPlayers = maxPlayers;
   }

   public int getViewDistance() {
      return this.viewDistance;
   }

   public void setViewDistance(int viewDistance) {
      this.viewDistance = viewDistance;
   }

   public int getSimulationDistance() {
      return this.simulationDistance;
   }

   public void setSimulationDistance(int simulationDistance) {
      this.simulationDistance = simulationDistance;
   }

   public boolean isReducedDebugInfo() {
      return this.reducedDebugInfo;
   }

   public void setReducedDebugInfo(boolean reducedDebugInfo) {
      this.reducedDebugInfo = reducedDebugInfo;
   }

   public boolean isRespawnScreenEnabled() {
      return this.enableRespawnScreen;
   }

   public void setRespawnScreenEnabled(boolean enableRespawnScreen) {
      this.enableRespawnScreen = enableRespawnScreen;
   }

   public boolean isLimitedCrafting() {
      return this.limitedCrafting;
   }

   public void setLimitedCrafting(boolean limitedCrafting) {
      this.limitedCrafting = limitedCrafting;
   }

   public boolean isDebug() {
      return this.isDebug;
   }

   public void setDebug(boolean isDebug) {
      this.isDebug = isDebug;
   }

   public boolean isFlat() {
      return this.isFlat;
   }

   public void setFlat(boolean isFlat) {
      this.isFlat = isFlat;
   }

   @Nullable
   public WorldBlockPosition getLastDeathPosition() {
      return this.lastDeathPosition;
   }

   public void setLastDeathPosition(@Nullable WorldBlockPosition lastDeathPosition) {
      this.lastDeathPosition = lastDeathPosition;
   }

   public Optional<Integer> getPortalCooldown() {
      return Optional.ofNullable(this.portalCooldown);
   }

   public void setPortalCooldown(int portalCooldown) {
      this.portalCooldown = portalCooldown;
   }
}
