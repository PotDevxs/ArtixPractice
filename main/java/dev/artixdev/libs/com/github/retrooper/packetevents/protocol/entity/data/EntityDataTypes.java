package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.sniffer.SnifferState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.ItemStack;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.Particle;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.BlockFace;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import dev.artixdev.libs.com.github.retrooper.packetevents.resources.ResourceLocation;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.Quaternion4f;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TypesBuilder;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.TypesBuilderData;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3f;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3i;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.net.kyori.adventure.text.Component;

public class EntityDataTypes {
   private static final Map<String, EntityDataType<?>> ENTITY_DATA_TYPE_MAP = new HashMap();
   private static final Map<Byte, Map<Integer, EntityDataType<?>>> ENTITY_DATA_TYPE_ID_MAP = new HashMap();
   protected static final TypesBuilder TYPES_BUILDER;
   public static final EntityDataType<Byte> BYTE;
   public static final EntityDataType<Short> SHORT;
   public static final EntityDataType<Integer> INT;
   public static final EntityDataType<Long> LONG;
   public static final EntityDataType<Float> FLOAT;
   public static final EntityDataType<String> STRING;
   /** @deprecated */
   @Deprecated
   public static final EntityDataType<String> COMPONENT;
   public static final EntityDataType<Component> ADV_COMPONENT;
   /** @deprecated */
   @Deprecated
   public static final EntityDataType<Optional<String>> OPTIONAL_COMPONENT;
   public static final EntityDataType<Optional<Component>> OPTIONAL_ADV_COMPONENT;
   public static final EntityDataType<ItemStack> ITEMSTACK;
   public static final EntityDataType<Optional<ItemStack>> OPTIONAL_ITEMSTACK;
   public static final EntityDataType<Boolean> BOOLEAN;
   public static final EntityDataType<Vector3f> ROTATION;
   public static final EntityDataType<Vector3i> BLOCK_POSITION;
   public static final EntityDataType<Optional<Vector3i>> OPTIONAL_BLOCK_POSITION;
   public static final EntityDataType<BlockFace> BLOCK_FACE;
   public static final EntityDataType<Optional<UUID>> OPTIONAL_UUID;
   public static final EntityDataType<Integer> BLOCK_STATE;
   public static final EntityDataType<Integer> OPTIONAL_BLOCK_STATE;
   public static final EntityDataType<NBTCompound> NBT;
   public static final EntityDataType<Particle> PARTICLE;
   public static final EntityDataType<VillagerData> VILLAGER_DATA;
   public static final EntityDataType<Optional<Integer>> OPTIONAL_INT;
   public static final EntityDataType<EntityPose> ENTITY_POSE;
   public static final EntityDataType<Integer> CAT_VARIANT;
   public static final EntityDataType<Integer> FROG_VARIANT;
   public static final EntityDataType<Optional<WorldBlockPosition>> OPTIONAL_GLOBAL_POSITION;
   public static final EntityDataType<Integer> PAINTING_VARIANT_TYPE;
   public static final EntityDataType<SnifferState> SNIFFER_STATE;
   public static final EntityDataType<Vector3f> VECTOR3F;
   public static final EntityDataType<Quaternion4f> QUATERNION;

   public static EntityDataType<?> getById(ClientVersion version, int id) {
      int index = TYPES_BUILDER.getDataIndex(version);
      Map<Integer, EntityDataType<?>> typeIdMap = (Map)ENTITY_DATA_TYPE_ID_MAP.get((byte)index);
      return (EntityDataType)typeIdMap.get(id);
   }

   public static EntityDataType<?> getByName(String name) {
      return (EntityDataType)ENTITY_DATA_TYPE_MAP.get(name);
   }

   public static <T> EntityDataType<T> define(String name, Function<PacketWrapper<?>, T> deserializer, BiConsumer<PacketWrapper<?>, T> serializer) {
      TypesBuilderData data = TYPES_BUILDER.define(name);
      EntityDataType<T> type = new EntityDataType(name, data.getData(), deserializer, serializer);
      ENTITY_DATA_TYPE_MAP.put(type.getName(), type);
      for (ClientVersion version : TYPES_BUILDER.getVersions()) {
         int index = TYPES_BUILDER.getDataIndex(version);
         if (index != -1) {
            Map<Integer, EntityDataType<?>> typeIdMap = (Map)ENTITY_DATA_TYPE_ID_MAP.computeIfAbsent((byte)index, (k) -> {
               return new HashMap();
            });
            typeIdMap.put(type.getId(version), type);
         }
      }

      return type;
   }

   private static <T> Function<PacketWrapper<?>, T> readIntDeserializer() {
      return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9) ? (wrapper) -> {
         @SuppressWarnings("unchecked")
         T result = (T) (Integer) wrapper.readVarInt();
         return result;
      } : (wrapper) -> {
         @SuppressWarnings("unchecked")
         T result = (T) (Integer) wrapper.readInt();
         return result;
      };
   }

   private static <T> BiConsumer<PacketWrapper<?>, T> writeIntSerializer() {
      return (wrapper, value) -> {
         int output = 0;
         if (value instanceof Byte) {
            output = ((Byte)value).intValue();
         } else if (value instanceof Short) {
            output = ((Short)value).intValue();
         } else if (value instanceof Integer) {
            output = (Integer)value;
         } else if (value instanceof Long) {
            output = ((Long)value).intValue();
         }

         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeVarInt(output);
         } else {
            wrapper.writeInt(output);
         }

      };
   }

   /** @deprecated */
   @Deprecated
   private static Function<PacketWrapper<?>, Optional<String>> readOptionalComponentJSONDeserializer() {
      return (wrapper) -> {
         return wrapper.readBoolean() ? Optional.of(wrapper.readComponentJSON()) : Optional.empty();
      };
   }

   /** @deprecated */
   @Deprecated
   private static BiConsumer<PacketWrapper<?>, Optional<String>> writeOptionalComponentJSONSerializer() {
      return (wrapper, value) -> {
         if (value != null && value.isPresent()) {
            wrapper.writeBoolean(true);
            wrapper.writeComponentJSON((String)value.get());
         } else {
            wrapper.writeBoolean(false);
         }

      };
   }

   private static Function<PacketWrapper<?>, Optional<Component>> readOptionalComponentDeserializer() {
      return (wrapper) -> {
         return wrapper.readBoolean() ? Optional.of(wrapper.readComponent()) : Optional.empty();
      };
   }

   private static BiConsumer<PacketWrapper<?>, Optional<Component>> writeOptionalComponentSerializer() {
      return (wrapper, value) -> {
         if (value != null && value.isPresent()) {
            wrapper.writeBoolean(true);
            wrapper.writeComponent((Component)value.get());
         } else {
            wrapper.writeBoolean(false);
         }

      };
   }

   private static <T> Function<PacketWrapper<?>, T> readOptionalBlockPositionDeserializer() {
      return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9) ? (wrapper) -> {
         @SuppressWarnings("unchecked")
         T result = (T) (wrapper.readBoolean() ? Optional.of(wrapper.readBlockPosition()) : Optional.empty());
         return result;
      } : (wrapper) -> {
         if (wrapper.readBoolean()) {
            int x = wrapper.readInt();
            int y = wrapper.readInt();
            int z = wrapper.readInt();
            @SuppressWarnings("unchecked")
            T result = (T) Optional.of(new Vector3i(x, y, z));
            return result;
         } else {
            @SuppressWarnings("unchecked")
            T result = (T) Optional.empty();
            return result;
         }
      };
   }

   private static <T> BiConsumer<PacketWrapper<?>, T> writeOptionalBlockPositionSerializer() {
      return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9) ? (wrapper, value) -> {
         if (value instanceof Optional) {
            Optional<?> optional = (Optional)value;
            if (optional.isPresent()) {
               wrapper.writeBoolean(true);
               wrapper.writeBlockPosition((Vector3i)optional.get());
            } else {
               wrapper.writeBoolean(false);
            }
         } else {
            wrapper.writeBoolean(false);
         }

      } : (wrapper, value) -> {
         if (value instanceof Optional) {
            Optional<?> optional = (Optional)value;
            if (optional.isPresent()) {
               wrapper.writeBoolean(true);
               Vector3i position = (Vector3i)optional.get();
               wrapper.writeInt(position.getX());
               wrapper.writeInt(position.getY());
               wrapper.writeInt(position.getZ());
            } else {
               wrapper.writeBoolean(false);
            }
         } else {
            wrapper.writeBoolean(false);
         }

      };
   }

   static {
      TYPES_BUILDER = new TypesBuilder("entity/entity_data_type_mappings", new ClientVersion[]{ClientVersion.V_1_8, ClientVersion.V_1_9, ClientVersion.V_1_11, ClientVersion.V_1_13, ClientVersion.V_1_14, ClientVersion.V_1_19, ClientVersion.V_1_19_3, ClientVersion.V_1_19_4});
      BYTE = define("byte", PacketWrapper::readByte, PacketWrapper::writeByte);
      SHORT = define("short", PacketWrapper::readShort, PacketWrapper::writeShort);
      INT = define("int", (wrapper) -> {
         return wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9) ? wrapper.readVarInt() : wrapper.readInt();
      }, (wrapper, value) -> {
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeVarInt(value);
         } else {
            wrapper.writeInt(value);
         }

      });
      LONG = define("long", PacketWrapper::readVarLong, PacketWrapper::writeVarLong);
      FLOAT = define("float", PacketWrapper::readFloat, PacketWrapper::writeFloat);
      STRING = define("string", PacketWrapper::readString, PacketWrapper::writeString);
      COMPONENT = define("component", PacketWrapper::readComponentJSON, PacketWrapper::writeComponentJSON);
      ADV_COMPONENT = define("component", PacketWrapper::readComponent, PacketWrapper::writeComponent);
      OPTIONAL_COMPONENT = define("optional_component", readOptionalComponentJSONDeserializer(), writeOptionalComponentJSONSerializer());
      OPTIONAL_ADV_COMPONENT = define("optional_component", readOptionalComponentDeserializer(), writeOptionalComponentSerializer());
      ITEMSTACK = define("itemstack", PacketWrapper::readItemStack, PacketWrapper::writeItemStack);
      OPTIONAL_ITEMSTACK = define("optional_itemstack", (wrapper) -> {
         return Optional.of(wrapper.readItemStack());
      }, (wrapper, value) -> {
         wrapper.writeItemStack(value.orElse(null));
      });
      BOOLEAN = define("boolean", PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
      ROTATION = define("rotation", (wrapper) -> {
         return new Vector3f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat());
      }, (wrapper, value) -> {
         wrapper.writeFloat(value.x);
         wrapper.writeFloat(value.y);
         wrapper.writeFloat(value.z);
      });
      BLOCK_POSITION = define("block_position", (wrapper) -> {
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return wrapper.readBlockPosition();
         } else {
            int x = wrapper.readInt();
            int y = wrapper.readInt();
            int z = wrapper.readInt();
            return new Vector3i(x, y, z);
         }
      }, (wrapper, blockPosition) -> {
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeBlockPosition(blockPosition);
         } else {
            wrapper.writeInt(blockPosition.getX());
            wrapper.writeInt(blockPosition.getY());
            wrapper.writeInt(blockPosition.getZ());
         }

      });
      OPTIONAL_BLOCK_POSITION = define("optional_block_position", readOptionalBlockPositionDeserializer(), writeOptionalBlockPositionSerializer());
      BLOCK_FACE = define("block_face", (wrapper) -> {
         int id = wrapper.readVarInt();
         return BlockFace.getBlockFaceByValue(id);
      }, (wrapper, value) -> {
         wrapper.writeVarInt(value.getFaceValue());
      });
      OPTIONAL_UUID = define("optional_uuid", (wrapper) -> {
         return Optional.ofNullable((UUID)wrapper.readOptional(PacketWrapper::readUUID));
      }, (wrapper, value) -> {
         wrapper.writeOptional(value.orElse(null), PacketWrapper::writeUUID);
      });
      BLOCK_STATE = define("block_state", readIntDeserializer(), writeIntSerializer());
      OPTIONAL_BLOCK_STATE = define("optional_block_state", readIntDeserializer(), writeIntSerializer());
      NBT = define("nbt", PacketWrapper::readNBT, PacketWrapper::writeNBT);
      PARTICLE = define("particle", (wrapper) -> {
         int id = wrapper.readVarInt();
         ParticleType type = ParticleTypes.getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), id);
         return new Particle(type, (ParticleData)type.readDataFunction().apply(wrapper));
      }, (wrapper, particle) -> {
         wrapper.writeVarInt(particle.getType().getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()));
         particle.getType().writeDataFunction().accept(wrapper, particle.getData());
      });
      VILLAGER_DATA = define("villager_data", PacketWrapper::readVillagerData, PacketWrapper::writeVillagerData);
      OPTIONAL_INT = define("optional_int", (wrapper) -> {
         int i = wrapper.readVarInt();
         return i == 0 ? Optional.empty() : Optional.of(i - 1);
      }, (wrapper, value) -> {
         wrapper.writeVarInt((Integer)value.orElse(-1) + 1);
      });
      ENTITY_POSE = define("entity_pose", (wrapper) -> {
         int id = wrapper.readVarInt();
         return EntityPose.getById(wrapper.getServerVersion().toClientVersion(), id);
      }, (wrapper, value) -> {
         wrapper.writeVarInt(value.getId(wrapper.getServerVersion().toClientVersion()));
      });
      CAT_VARIANT = define("cat_variant_type", readIntDeserializer(), writeIntSerializer());
      FROG_VARIANT = define("frog_variant_type", readIntDeserializer(), writeIntSerializer());
      OPTIONAL_GLOBAL_POSITION = define("optional_global_position", (wrapper) -> {
         return Optional.ofNullable((WorldBlockPosition)wrapper.readOptional((w) -> {
            return new WorldBlockPosition(new ResourceLocation(w.readString(32767)), w.readBlockPosition());
         }));
      }, (wrapper, value) -> {
         wrapper.writeOptional(value.orElse(null), (w, globalPos) -> {
            w.writeString(globalPos.getWorld().toString());
            w.writeBlockPosition(globalPos.getBlockPosition());
         });
      });
      PAINTING_VARIANT_TYPE = define("painting_variant_type", readIntDeserializer(), writeIntSerializer());
      SNIFFER_STATE = define("sniffer_state", (wrapper) -> {
         int id = wrapper.readVarInt();
         return SnifferState.values()[id];
      }, (wrapper, value) -> {
         wrapper.writeVarInt(value.ordinal());
      });
      VECTOR3F = define("vector3f", (wrapper) -> {
         return new Vector3f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat());
      }, (wrapper, value) -> {
         wrapper.writeFloat(value.x);
         wrapper.writeFloat(value.y);
         wrapper.writeFloat(value.z);
      });
      QUATERNION = define("quaternion", (wrapper) -> {
         return new Quaternion4f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat());
      }, (wrapper, value) -> {
         wrapper.writeFloat(value.getX());
         wrapper.writeFloat(value.getY());
         wrapper.writeFloat(value.getZ());
         wrapper.writeFloat(value.getW());
      });
   }
}
